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
package org.jboss.jms.client.p2p;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelException;
import org.jgroups.ChannelListener;
import org.jgroups.JChannelFactory;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.blocks.PullPushAdapter;
import org.jboss.jms.MessageImpl;
import org.jboss.jms.client.ConnectionDelegate;
import org.jboss.jms.client.SessionDelegate;
import org.jboss.jms.destination.JBossTemporaryDestination;
import org.jboss.util.id.GUID;

/**
 * The p2p connection
 * 
 * @author <a href="mailto:nathan@jboss.org">Nathan Phelps</a>
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class P2PConnectionDelegate
   implements ConnectionDelegate, ChannelListener, MessageListener
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   private String clientId = null;
   private ExceptionListener exceptionListener = null;
   private boolean closed = false;
   private String password = null;
   private String username = null;
   private List sessions = new ArrayList();

   private Channel channel = null;
   private PullPushAdapter connection = null;
   private boolean started = false;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public P2PConnectionDelegate(String username, String password)
      throws JMSException
   {
      this.username = username;
      this.password = password;

      try
      {
          URL url = Thread.currentThread().getContextClassLoader().getResource("org/jboss/jms/p2p/jgroups-config.xml");
          this.channel = new JChannelFactory().createChannel(url);
          this.channel.setChannelListener(this);
          this.channel.connect("org.jboss.jms.p2p");
          this.connection = new PullPushAdapter(this.channel, this);
          this.connection.start();
      }
      catch (ChannelException exception)
      {
          throw new JMSException(exception.getMessage());
      }

   }

   // Public --------------------------------------------------------

   // ConnectionDelegate implementation -----------------------------

	public void close() throws JMSException
	{
      Iterator iterator = this.sessions.iterator();
      while (iterator.hasNext())
      {
          ((SessionDelegate) iterator.next()).close();
          iterator.remove();
      }
      this.closed = true;
      this.connection.stop();
      this.channel.disconnect();
      this.channel.close();
	}

	public void closing() throws JMSException
	{
	}

	public SessionDelegate createSession(boolean isXA, boolean transacted, int acknowledgeMode) throws JMSException
	{
      this.throwExceptionIfClosed();
      this.generateClientIDIfNull();
      SessionDelegate session = new P2PSessionDelegate(this, transacted, acknowledgeMode);
      this.sessions.add(session);
      return session;
	}

	public String getClientID() throws JMSException
	{
      this.throwExceptionIfClosed();
      this.generateClientIDIfNull();
      return this.clientId;
	}

	public Enumeration getJMSXPropertyNames() throws JMSException
	{
		// TODO getJMSXPropertyNames
		return null;
	}
   
   public void deleteTempDestination(JBossTemporaryDestination destination)
   {
      // TODO deleteTempDestination
   }

	public void setClientID(String id) throws JMSException
	{
      this.throwExceptionIfClosed();
      if (this.clientId != null)
      {
          throw new IllegalStateException("The client Id has already been set by the provider.  To supply your own value, you must set the client ID immediatly after creating the connection.  See section 4.3.2 of the JMS specification for more information.");
      }
      this.clientId = id;
	}

	public void setExceptionListener(ExceptionListener listener) throws JMSException
	{
      this.throwExceptionIfClosed();
      this.generateClientIDIfNull();
      this.exceptionListener = listener;
	}

	public void start() throws JMSException
	{
      this.throwExceptionIfClosed();
      this.generateClientIDIfNull();
      this.started = true;
	}

	public void stop() throws JMSException
	{
      this.throwExceptionIfClosed();
      this.generateClientIDIfNull();
      this.started = false;
	}

   // ChannelListener implementation --------------------------------

   public void channelClosed(Channel arg0)
   {
      if (this.closed != false && this.exceptionListener != null)
      {
          this.exceptionListener.onException(new JMSException("We were unexpectedly disconnected"));
      }
   }

   public void channelConnected(Channel arg0)
   {
   }

   public void channelDisconnected(Channel arg0)
   {
      this.channelClosed(channel);
   }

   public void channelReconnected(Address arg0)
   {
   }

   public void channelShunned()
   {
      if (this.exceptionListener != null)
      {
          this.exceptionListener.onException(new JMSException("We were shunned."));
      }
   }

   // MessageListener implementation --------------------------------

   public byte[] getState()
   {
      return new byte[0];
   }

   public void receive(Message message)
   {
      if (this.started)
      {
          Object object = message.getObject();
          if (object instanceof List)
          {
              List theList = (List) object;
              Iterator iterator = theList.iterator();
              while (iterator.hasNext())
              {
                  Object listObject = iterator.next();
                  if (listObject instanceof MessageImpl)
                  {
                      MessageImpl currentMessage = (MessageImpl)listObject;
                      if (currentMessage.getOrigianClientID().equals(this.clientId))
                      {
                          currentMessage.setIsLocal(true);
                      }
                      Iterator sessionIterator = this.sessions.iterator();
                      while (sessionIterator.hasNext())
                      {
                          ((P2PSessionDelegate) sessionIterator.next()).deliver(currentMessage);
                      }
                  }
              }
          }
          else if (object instanceof MessageImpl)
          {
              MessageImpl theMessage = (MessageImpl) object;
              if (theMessage.getOrigianClientID().equals(this.clientId))
              {
                  theMessage.setIsLocal(true);
              }
              Iterator iterator = this.sessions.iterator();
              while (iterator.hasNext())
              {
                  ((P2PSessionDelegate) iterator.next()).deliver(theMessage);
              }
          }
      }
   }

   public void setState(byte[] arg0)
   {
   }

   // Object overrides -----------------------------------------------

   public void finalize() throws Throwable
   {
       if (!this.closed)
       {
           this.close();
       }
   }

   // Protected ------------------------------------------------------

   // Package Private ------------------------------------------------

   ///////////////////////////////////////////////////////////////////////////////////////////////
   // Methods that the session calls                                                            //
   ///////////////////////////////////////////////////////////////////////////////////////////////

   void send(MessageImpl message) throws JMSException
   {
       try
       {
           message.setOriginClientID(this.clientId);
           this.connection.send(new Message(null, null, (Serializable) message));
       }
       catch (Exception exception)
       {
           throw new JMSException(exception.getMessage());
       }
   }

   void send(Collection messages) throws JMSException
   {
       try
       {
           Iterator iterator = messages.iterator();
           while (iterator.hasNext())
           {
               ((MessageImpl)iterator.next()).setOriginClientID(this.clientId);
           }
           this.connection.send(new Message(null, null, (Serializable) messages));
       }
       catch (Exception exception)
       {
           throw new JMSException(exception.getMessage());
       }
   }

   // Private --------------------------------------------------------

   private void throwExceptionIfClosed()
   {
       if (this.closed)
       {
           throw new IllegalStateException("The connection is closed.");
       }
   }

   private synchronized void generateClientIDIfNull() throws JMSException
   {
       if (this.clientId == null)
       {
           this.setClientID(new GUID().toString().toUpperCase());
       }
   }

   // Inner Classes --------------------------------------------------

}
