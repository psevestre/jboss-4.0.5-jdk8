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
package org.jboss.ejb3.test.mdb.unit;

import java.util.Enumeration;
import java.util.List;

import javax.jms.MessageProducer;
import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.jboss.ejb3.InitialContextFactory;
import org.jboss.ejb3.test.mdb.Stateless;
import org.jboss.ejb3.test.mdb.TestStatus;
import org.jboss.logging.Logger;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.SimplePrincipal;
import org.jboss.test.JBossTestCase;
import org.jboss.util.collection.CollectionsUtil;

import junit.framework.Test;

/**
 * Sample client for the jboss container.
 * 
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Id: MDBUnitTestCase.java 57207 2006-09-26 12:06:13Z dimitris@jboss.org $
 */
public class MDBUnitTestCase extends JBossTestCase
{
   private static final Logger log = Logger.getLogger(MDBUnitTestCase.class);

   static boolean deployed = false;

   static int test = 0;

   public MDBUnitTestCase(String name)
   {

      super(name);

   }

   public void testOverrideQueue() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("anyone"));
      SecurityAssociation.setCredential("password".toCharArray());

      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext().lookup(
            "queue/overridequeuetest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");

      sender = session.createSender(queue);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(5000);
      assertEquals(1, status.overrideQueueFired());
   }

   public void testNondurableQueue() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("anyone"));
      SecurityAssociation.setCredential("password".toCharArray());

      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext().lookup(
            "queue/nondurablemdbtest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");
      msg.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

      sender = session.createSender(queue);
      sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(1, status.nondurableQueueFired());
   }

   public void testDefaultedQueue() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("anyone"));
      SecurityAssociation.setCredential("password".toCharArray());

      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext()
            .lookup("queue/defaultedmdbtest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");

      sender = session.createSender(queue);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(1, status.defaultedQueueFired());
   }

   public void testOverrideDefaultedQueue() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("anyone"));
      SecurityAssociation.setCredential("password".toCharArray());

      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext().lookup(
            "queue/overridedefaultedmdbtest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");

      sender = session.createSender(queue);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(1, status.overrideDefaultedQueueFired());
   }

   public void testQueue() throws Exception
   {
      setSecurity("anyone", "password");

      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext().lookup("queue/mdbtest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");

      sender = session.createSender(queue);
      sender.send(msg);
      sender.send(msg);
      sender.send(msg);
      sender.send(msg);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(5, status.queueFired());
      assertTrue(status.interceptedQueue());
      assertTrue(status.postConstruct());
      assertEquals(5, status.messageCount());

      // TODO: Figure out how to test preDestroy gets invoked
      // assertTrue(status.preDestroy());

      Stateless stateless = (Stateless) getInitialContext().lookup("Stateless");
      assertNotNull(stateless);
      String state = stateless.getState();
      assertEquals("Set", state);
   }

   public void testTopic() throws Exception
   {
      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      TopicConnection cnn = null;
      MessageProducer sender = null;
      TopicSession session = null;

      Topic topic = (Topic) getInitialContext().lookup("topic/mdbtest");
      TopicConnectionFactory factory = getTopicConnectionFactory();
      cnn = factory.createTopicConnection();
      session = cnn.createTopicSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");

      sender = session.createProducer(topic);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(1, status.topicFired());
      assertTrue(status.interceptedTopic());
      assertFalse(status.postConstruct());
      assertFalse(status.preDestroy());
   }

   public void testRuntimeException() throws Exception
   {
      TestStatus status = (TestStatus) getInitialContext().lookup(
            "TestStatusBean/remote");
      clear(status);
      QueueConnection cnn = null;
      QueueSender sender = null;
      QueueSession session = null;

      Queue queue = (Queue) getInitialContext().lookup("queue/bmtmdbtest");
      QueueConnectionFactory factory = getQueueConnectionFactory();
      cnn = factory.createQueueConnection();
      session = cnn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);

      TextMessage msg = session.createTextMessage("Hello World");
      msg.setIntProperty("JMS_JBOSS_REDELIVERY_LIMIT", 1);

      sender = session.createSender(queue);
      sender.send(msg);
      session.close();
      cnn.close();

      Thread.sleep(2000);
      assertEquals(1, status.bmtQueueRan());
   }

   /**
    * Test that expired messages are moved to a separate queue.
    */
   public void testExpiredMessagesMove() throws Exception
   {
      QueueConnection connection = null;
      QueueSender sender = null;
      QueueSession session = null;

      QueueConnectionFactory factory = getQueueConnectionFactory();
      connection = factory.createQueueConnection();
      connection.start();
      session = connection.createQueueSession(false,
            QueueSession.AUTO_ACKNOWLEDGE);

      Queue queue = (Queue) getInitialContext().lookup("queue/expirytest");
      sender = session.createSender(queue);

      Queue dlq = (Queue) getInitialContext().lookup("queue/DLQ");

      ObjectName dlqName = new ObjectName(
            "jboss.mq.destination:service=Queue,name=DLQ");
      getServer().invoke(dlqName, "removeAllMessages", null, null);

      long now = System.currentTimeMillis();

      TextMessage message = session.createTextMessage();
      message.setStringProperty("foo", "bar");
      message.setStringProperty("null", null);
      String text = "expire on server";
      message.setText(text);

      sender.send(message, DeliveryMode.PERSISTENT, 4, 1);

      Thread.sleep(1000);

      assertSize(session, queue, 0);
      assertSize(session, dlq, 1);

      QueueReceiver receiver = session.createReceiver(dlq);
      message = (TextMessage) receiver.receiveNoWait();
      assertNotNull(message);
      assertEquals("QUEUE.expirytest", message
            .getStringProperty("JBOSS_ORIG_DESTINATION"));
      assertTrue(message.getLongProperty("JBOSS_ORIG_EXPIRATION") > now);
      assertEquals(0L, message.getJMSExpiration());
      assertEquals(text, message.getText());
      assertEquals("bar", message.getStringProperty("foo"));
      assertNull(message.getStringProperty("null"));

      sender.close();
      receiver.close();

      session.close();
      connection.close();
   }

   public void testDlqMaxResent() throws Exception
   {
      QueueConnection connection = null;
      QueueSender sender = null;
      QueueSession session = null;

      QueueConnectionFactory factory = getQueueConnectionFactory();
      connection = factory.createQueueConnection();
      connection.start();
      session = connection.createQueueSession(false,
            QueueSession.AUTO_ACKNOWLEDGE);

      Queue queue = (Queue) getInitialContext().lookup("queue/dlqtest");
      sender = session.createSender(queue);

      Queue dlq = (Queue) getInitialContext().lookup("queue/DLQ");

      ObjectName dlqName = new ObjectName(
            "jboss.mq.destination:service=Queue,name=DLQ");
      getServer().invoke(dlqName, "removeAllMessages", null, null);

      TextMessage message = session.createTextMessage();
      message.setStringProperty("foo", "bar");
      message.setStringProperty("null", null);
      String text = "expire on server";
      message.setText(text);

      sender.send(message);

      Thread.sleep(1000);

      assertSize(session, queue, 0);
      assertSize(session, dlq, 1);

      QueueReceiver receiver = session.createReceiver(dlq);
      message = (TextMessage) receiver.receiveNoWait();
      assertNotNull(message);
      assertEquals("QUEUE.dlqtest", message
            .getStringProperty("JBOSS_ORIG_DESTINATION"));
      assertEquals(0L, message.getJMSExpiration());
      assertEquals(text, message.getText());
      assertEquals("bar", message.getStringProperty("foo"));
      assertNull(message.getStringProperty("null"));

      sender.close();
      receiver.close();

      session.close();
      connection.close();
   }

   private List list(QueueSession session, Queue queue) throws Exception
   {
      QueueBrowser browser = session.createBrowser(queue);
      Enumeration e = browser.getEnumeration();
      List messages = CollectionsUtil.list(e);

      browser.close();

      return messages;
   }

   private int size(QueueSession session, Queue queue) throws Exception
   {
      List l = list(session, queue);
      return l.size();
   }

   private void assertSize(QueueSession session, Queue queue, int size)
         throws Exception
   {
      assertEquals(size, size(session, queue));
   }

   protected QueueConnectionFactory getQueueConnectionFactory()
         throws Exception
   {
      try
      {
         return (QueueConnectionFactory) getInitialContext().lookup(
               "ConnectionFactory");
      } catch (NamingException e)
      {
         return (QueueConnectionFactory) getInitialContext().lookup(
               "java:/ConnectionFactory");
      }
   }

   protected TopicConnectionFactory getTopicConnectionFactory()
         throws Exception
   {
      try
      {
         return (TopicConnectionFactory) getInitialContext().lookup(
               "ConnectionFactory");
      } catch (NamingException e)
      {
         return (TopicConnectionFactory) getInitialContext().lookup(
               "java:/ConnectionFactory");
      }
   }

   protected void clear(TestStatus status)
   {
      status.clear();
      assertEquals(0, status.bmtQueueRan());
      assertEquals(0, status.defaultedQueueFired());
      assertEquals(0, status.messageCount());
      assertEquals(0, status.nondurableQueueFired());
      assertEquals(0, status.overrideDefaultedQueueFired());
      assertEquals(0, status.overrideQueueFired());
      assertEquals(0, status.queueFired());
      assertEquals(0, status.topicFired());
      assertFalse(status.interceptedQueue());
      assertFalse(status.interceptedTopic());
      assertFalse(status.postConstruct());
      assertFalse(status.preDestroy());
   }

   protected void setSecurity(String user, String password)
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal(user));
      SecurityAssociation.setCredential(password.toCharArray());

      InitialContextFactory.setSecurity(user, password);
   }

   protected InitialContext getInitialContext() throws Exception
   {
      return InitialContextFactory.getInitialContext();
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(MDBUnitTestCase.class,
            "mdbtest-service.xml, mdb-test.jar");
   }

}