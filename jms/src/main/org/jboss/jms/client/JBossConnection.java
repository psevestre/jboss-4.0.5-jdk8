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

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.XAConnection;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicConnection;
import javax.jms.XATopicSession;

/**
 * A connection
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class JBossConnection 
   implements Connection, QueueConnection, TopicConnection,
              XAConnection, XAQueueConnection, XATopicConnection
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   /** The connection delegate */
   private ConnectionDelegate delegate;

   /** The exception listener */
   private ExceptionListener listener;

   /** Are we an XAConnection */
   private boolean isXAConnection;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public JBossConnection(ConnectionDelegate delegate, boolean isXAConnection)
      throws JMSException
   {
      this.delegate = delegate;
      this.isXAConnection = isXAConnection;
   }

   // Public --------------------------------------------------------

   /**
    * Retrieve the extension property names
    */
   public Enumeration getJMSXPropertyNames()
      throws JMSException
   {
      return delegate.getJMSXPropertyNames();
   }

   // Connection implementation -------------------------------------

	public void close() throws JMSException
	{
      delegate.closing();
      delegate.close();
	}

	public ConnectionConsumer createConnectionConsumer(
		Destination destination,
		String messageSelector,
		ServerSessionPool sessionPool,
		int maxMessages)
		throws JMSException
	{
		// TODO createConnectionConsumer
		return null;
	}

	public ConnectionConsumer createDurableConnectionConsumer(
		Topic topic,
		String subscriptionName,
		String messageSelector,
		ServerSessionPool sessionPool,
		int maxMessages)
		throws JMSException
	{
		// TODO createDurableConnectionConsumer
		return null;
	}

	public Session createSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
      if (transacted)
         acknowledgeMode = Session.SESSION_TRANSACTED;
      return new JBossSession(delegate.createSession(false, transacted, acknowledgeMode), false, transacted, acknowledgeMode);
	}

	public String getClientID() throws JMSException
	{
      return delegate.getClientID();
	}

	public ExceptionListener getExceptionListener() throws JMSException
	{
      return listener;
	}

	public ConnectionMetaData getMetaData() throws JMSException
	{
      return new JBossConnectionMetaData(delegate);
	}

	public void setClientID(String clientID) throws JMSException
	{
      delegate.setClientID(clientID);
	}

	public void setExceptionListener(ExceptionListener listener) throws JMSException
	{
      delegate.setExceptionListener(listener);
      this.listener = listener;
	}

	public void start() throws JMSException
	{
      delegate.start();
	}

	public void stop() throws JMSException
	{
      delegate.stop();
	}

   // QueueConnection implementation --------------------------------

	public ConnectionConsumer createConnectionConsumer(
		Queue queue,
		String messageSelector,
		ServerSessionPool sessionPool,
		int maxMessages)
		throws JMSException
	{
      return createConnectionConsumer((Destination) queue, messageSelector, sessionPool, maxMessages);
	}

	public QueueSession createQueueSession(boolean transacted, int acknowledgeMode) throws JMSException
	{
      return (QueueSession) createSession(transacted, acknowledgeMode);
	}

   // TopicConnection implementation --------------------------------

	public ConnectionConsumer createConnectionConsumer(
		Topic topic,
		String messageSelector,
		ServerSessionPool sessionPool,
		int maxMessages)
		throws JMSException
	{
      return createConnectionConsumer((Destination) topic, messageSelector, sessionPool, maxMessages);
	}

	public TopicSession createTopicSession(boolean transacted, int acknowledgeMode)
      throws JMSException
	{
      return (TopicSession) createSession(transacted, acknowledgeMode);
	}

   // XAConnection implementation -----------------------------------

   public XASession createXASession() throws JMSException
   {
      if (isXAConnection == false)
         throw new JMSException("Not an xa connection");
      return new JBossSession(delegate.createSession(true, true, Session.SESSION_TRANSACTED), true, true, Session.SESSION_TRANSACTED);
   }

   // XAQueueConnection implementation ------------------------------

   public XAQueueSession createXAQueueSession() throws JMSException
   {
      return (XAQueueSession) createXASession();
   }

   // XATopicConnection implementation ------------------------------

   public XATopicSession createXATopicSession() throws JMSException
   {
      return (XATopicSession) createXASession();
   }

   // Protected ------------------------------------------------------

   // Package Private ------------------------------------------------

   // Private --------------------------------------------------------

   // Inner Classes --------------------------------------------------
}
