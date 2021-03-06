/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ejb3.mdb;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import org.jboss.jms.jndi.JMSProviderAdapter;
import org.jboss.system.ServiceMBeanSupport;

/**
 * Places redeliveded messages on a Dead Letter Queue.
 * <p/>
 * <p/>
 * The Dead Letter Queue handler is used to not set JBoss in an endles loop
 * when a message is resent on and on due to transaction rollback for
 * message receipt.
 * <p/>
 * <p/>
 * It sends message to a dead letter queue (configurable, defaults to
 * queue/DLQ) when the message has been resent a configurable amount of times,
 * defaults to 10.
 * <p/>
 * <p/>
 * The handler is configured through the element MDBConfig in
 * container-invoker-conf.
 * <p/>
 * <p/>
 * The JMS property JBOSS_ORIG_DESTINATION in the resent message is set
 * to the name of the original destination (Destionation.toString()).
 * <p/>
 * <p/>
 * The JMS property JBOSS_ORIG_MESSAGEID in the resent message is set
 * to the id of the original message.
 * <p/>
 * Created: Thu Aug 23 21:17:26 2001
 *
 * @author ???
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class DLQHandler
        extends ServiceMBeanSupport
{
   /**
    * JMS property name holding original destination.
    */
   public static final String JBOSS_ORIG_DESTINATION = "JBOSS_ORIG_DESTINATION";

   /**
    * JMS property name holding original JMS message id.
    */
   public static final String JBOSS_ORIG_MESSAGEID = "JBOSS_ORIG_MESSAGEID";

   /**
    * Properties copied from org.jboss.mq.SpyMessage
    */
   private static final String JMS_JBOSS_REDELIVERY_COUNT = "JMS_JBOSS_REDELIVERY_COUNT";
   private static final String JMS_JBOSS_REDELIVERY_LIMIT = "JMS_JBOSS_REDELIVERY_LIMIT";
   
   // Configuratable stuff

   // May become configurable
   
   /**
    * Delivery mode for message, Message.DEFAULT_DELIVERY_MODE.
    */
   private int deliveryMode = Message.DEFAULT_DELIVERY_MODE;

   /**
    * Priority for the message, Message.DEFAULT_PRIORITY
    */
   private int priority = Message.DEFAULT_PRIORITY;

   // Private stuff
   private QueueConnection connection;
   private Queue dlq;
   private JMSProviderAdapter providerAdapter;
   private Hashtable resentBuffer = new Hashtable();

   private MDBConfig config;

   public DLQHandler(final JMSProviderAdapter providerAdapter, MDBConfig config)
   {
      this.providerAdapter = providerAdapter;
      this.config = config;
   }

   //--- Service
   
   /**
    * Initalize the service.
    *
    * @throws Exception Service failed to initalize.
    */
   protected void createService() throws Exception
   {
      Context ctx = providerAdapter.getInitialContext();

      try
      {
         String factoryName = providerAdapter.getQueueFactoryRef();
         QueueConnectionFactory factory = (QueueConnectionFactory)
                 ctx.lookup(factoryName);
         log.debug("Using factory: " + factory);

         if (config.getDlqUser() == null)
            connection = factory.createQueueConnection();
         else
            connection = factory.createQueueConnection(config.getDlqUser(), config.getDlqPassword());
         log.debug("Created connection: " + connection);

         dlq = (Queue) ctx.lookup(config.getDlq());
         log.debug("Using Queue: " + dlq);
      }
      catch (Exception e)
      {
         if (e instanceof JMSException)
            throw e;
         else
         {
            JMSException x = new JMSException("Error creating the dlq connection: " + e.getMessage());
            x.setLinkedException(e);
            throw x;
         }
      }
      finally
      {
         ctx.close();
      }
   }

   protected void startService() throws Exception
   {
      connection.start();
   }

   protected void stopService() throws Exception
   {
      connection.stop();
   }

   protected void destroyService() throws Exception
   {
      // Help the GC
      if (connection != null)
         connection.close();
      connection = null;
      dlq = null;
      providerAdapter = null;
   }
   
   //--- Logic
   
   /**
    * Check if a message has been redelivered to many times.
    * <p/>
    * If message has been redelivered to many times, send it to the
    * dead letter queue (default to queue/DLQ).
    *
    * @return true if message is handled (i.e resent), false if not.
    */
   public boolean handleRedeliveredMessage(final Message msg, final Transaction tx)
   {
      boolean handled = false;
      int max = config.getDlqMaxTimesRedelivered();
      String id = null;
      boolean jbossmq = true;
      int count = 0;

      try
      {

         if (msg.propertyExists(JMS_JBOSS_REDELIVERY_LIMIT))
            max = msg.getIntProperty(JMS_JBOSS_REDELIVERY_LIMIT);

         if (msg.propertyExists(JMS_JBOSS_REDELIVERY_COUNT))
            count = msg.getIntProperty(JMS_JBOSS_REDELIVERY_COUNT);
         else
         {
            id = msg.getJMSMessageID();
            if (id == null)
            {
               // if we can't get the id we are basically fucked
               log.error("Message id is null, can't handle message");
               return false;
            }
            count = incrementResentCount(id);
            jbossmq = false;
         }

         if (count > max)
         {
            id = msg.getJMSMessageID();
            log.warn("Message resent too many times; sending it to DLQ; message id=" + id);

            sendMessage(msg);
            deleteFromBuffer(id);

            handled = true;
         }
         else if (jbossmq == false && tx != null)
         {
            // Register a synchronization to remove the buffer entry
            // should the transaction commit
            DLQSynchronization synch = new DLQSynchronization(id);
            try
            {
               tx.registerSynchronization(synch);
            }
            catch (Exception e)
            {
               log.warn("Error registering DlQ Synchronization with transaction " + tx, e);
            }
         }
      }
      catch (JMSException e)
      {
         // If we can't send it ahead, we do not dare to just drop it...or?
         log.error("Could not send message to Dead Letter Queue", e);
      }

      return handled;
   }

   /**
    * Send message to the configured dead letter queue, defaults to queue/DLQ.
    */
   protected void sendMessage(Message msg) throws JMSException
   {
      boolean trace = log.isTraceEnabled();

      QueueSession session = null;
      QueueSender sender = null;

      try
      {
         msg = makeWritable(msg, trace); // Don't know yet if we are gona clone or not
         
         // Set the properties
         msg.setStringProperty(JBOSS_ORIG_MESSAGEID, msg.getJMSMessageID());
         // Some providers (say Websphere MQ) don't set this to something we can use
         Destination d = msg.getJMSDestination();
         if (d != null)
            msg.setStringProperty(JBOSS_ORIG_DESTINATION, d.toString());

         session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
         sender = session.createSender(dlq);
         if (trace)
         {
            log.trace("Sending message to DLQ; destination=" +
                      dlq + ", session=" + session + ", sender=" + sender);
         }

         sender.send(msg, deliveryMode, priority, config.getDlqTimeToLive());

         if (trace)
         {
            log.trace("Message sent.");
         }

      }
      finally
      {
         try
         {
            if (sender != null) sender.close();
            if (session != null) session.close();
         }
         catch (Exception e)
         {
            log.warn("Failed to close sender or session; ignoring", e);
         }
      }
   }

   /**
    * Increment the counter for the specific JMS message id.
    *
    * @return the new counter value.
    */
   protected int incrementResentCount(String id)
   {
      BufferEntry entry = null;
      boolean trace = log.isTraceEnabled();
      if (!resentBuffer.containsKey(id))
      {
         if (trace)
            log.trace("Making new entry for id " + id);
         entry = new BufferEntry();
         entry.id = id;
         entry.count = 1;
         resentBuffer.put(id, entry);
      }
      else
      {
         entry = (BufferEntry) resentBuffer.get(id);
         entry.count++;
         if (trace)
            log.trace("Incremented old entry for id " + id + " count " + entry.count);
      }
      return entry.count;
   }

   /**
    * Delete the entry in the message counter buffer for specifyed JMS id.
    */
   protected void deleteFromBuffer(String id)
   {
      resentBuffer.remove(id);
   }

   /**
    * Make the Message properties writable.
    *
    * @return the writable message.
    */
   protected Message makeWritable(Message msg, boolean trace) throws JMSException
   {
      Hashtable tmp = new Hashtable();

      // Save properties
      for (Enumeration en = msg.getPropertyNames(); en.hasMoreElements();)
      {
         String key = (String) en.nextElement();
         tmp.put(key, msg.getObjectProperty(key));
      }
      
      // Make them writable
      msg.clearProperties();

      Enumeration keys = tmp.keys();
      while (keys.hasMoreElements())
      {
         String key = (String) keys.nextElement();
         try
         {
            msg.setObjectProperty(key, tmp.get(key));
         }
         catch (JMSException ignored)
         {
            if (trace)
               log.trace("Could not copy message property " + key, ignored);
         }
      }

      return msg;
   }

   public String toString()
   {
      return super.toString() +
             "{ destinationJNDI=" + config.getDlq() +
             ", maxResent=" + config.getDlqMaxTimesRedelivered() +
             ", timeToLive=" + config.getDlqTimeToLive() +
             " }";
   }

   private class BufferEntry
   {
      int count;
      String id;
   }

   /**
    * Remove a redelivered message from the DLQ's buffer when it is acknowledged
    */
   protected class DLQSynchronization
           implements Synchronization
   {
      /**
       * The message id
       */
      String id;

      public DLQSynchronization(String id)
      {
         this.id = id;
      }

      public void beforeCompletion()
      {
      }

      /**
       * Forget the message when the transaction commits
       */
      public void afterCompletion(int status)
      {
         if (status == Status.STATUS_COMMITTED)
            deleteFromBuffer(id);
      }
   }
}
