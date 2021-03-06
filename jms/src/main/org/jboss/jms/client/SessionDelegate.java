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
package org.jboss.jms.client;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.transaction.xa.XAResource;

/**
 * The implementation of a session
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public interface SessionDelegate
   extends Lifecycle 
{
   // Constants -----------------------------------------------------

   // Public --------------------------------------------------------

   /**
    * 
    * Acknowledge a message
    * 
    * @param message the message to acknowledge
    * @param acknowledge true for acknowledge, false for a Nack
    */
   void acknowledge(Message message, boolean acknowledge);

   /**
    * Commit a session
    * 
    * @throws JMSException for any error
    */
   void commit() throws JMSException;

   /**
    * Create a queue browser
    * 
    * @param queue the queue
    * @param selector the message selector
    * @return the browser
    * @throws JMSException for any error
    */
   BrowserDelegate createBrowser(Queue queue, String selector) throws JMSException;

   /**
    * Create a bytes message
    * 
    * @return the message
    * @throws JMSException for any error
    */
   BytesMessage createBytesMessage() throws JMSException;

   /**
    * Create a consumer
    * 
    * @param destination the destination
    * @param subscription the subscription name
    * @param selector the message selector
    * @param noLocal the no local flag
    * @return the consumer
    * @throws JMSException for any error
    */
   ConsumerDelegate createConsumer(Destination destination, String subscription, String selector, boolean noLocal) throws JMSException;

   /**
    * Create a map message
    * 
    * @return the message
    * @throws JMSException for any error
    */
   MapMessage createMapMessage() throws JMSException;

   /**
    * Create a message
    * 
    * @return the message
    * @throws JMSException for any error
    */
   Message createMessage() throws JMSException;

   /**
    * Create an object message
    * 
    * @param object the object
    * @return the message
    * @throws JMSException for any error
    */
   ObjectMessage createObjectMessage(Serializable object) throws JMSException;

   /**
    * Create a producer
    * 
    * @param destination the destination
    * @return the producer
    * @throws JMSException for any error
    */
   ProducerDelegate createProducer(Destination destination) throws JMSException;

   /**
    * Create a stream message
    * 
    * @return the message
    * @throws JMSException for any error
    */
   StreamMessage createStreamMessage() throws JMSException;

   /**
    * Create a temporary destination
    * 
    * @param type the type of temporary destination
    * @return the temporary destination
    * @throws JMSException for any error
    */
   Destination createTempDestination(int type) throws JMSException;

   /**
    * Create a text message
    * 
    * @param text the text
    * @return the message
    * @throws JMSException for any error
    */
   TextMessage createTextMessage(String text) throws JMSException;

   /**
    * Retrieve a destination
    * 
    * @param name the implementation dependent name
    * @return the destination
    * @throws JMSException for any error
    */
   Destination getDestination(String name) throws JMSException;

   /**
    * Retrieve the XAResource for this session
    * 
    * @return the XAResource
    */
   XAResource getXAResource();

   /**
    * Recover a session
    * 
    * @throws JMSException for any error
    */
   void recover() throws JMSException;

   /**
    * Rollback a session
    * 
    * @throws JMSException for any error
    */
   void rollback() throws JMSException;

   /**
    * Run the session listener
    * 
    * @throws JMSException for any error
    */
   void run();

   /**
    * Set the session's message listener
    * 
    * @param listener the message listener
    * @throws JMSException for any error
    */
   void setMessageListener(MessageListener listener) throws JMSException;

   /**
    * Unsubscribe the name
    * 
    * @param name the name of the subscription
    * @throws JMSException for any error
    */
   void unsubscribe(String name) throws JMSException;

   // Inner Classes --------------------------------------------------
}
