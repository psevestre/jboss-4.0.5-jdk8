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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class MDBConfig
{
   protected int minPoolSize = 1;
   protected int maxPoolSize = 15;
   protected int keepAlive = 30000;
   protected int maxMessages = 1;
   protected String serverSessionPoolFactoryJNDI = "java:/StdJMSPool";
   protected String providerAdapterJNDI = "java:/DefaultJMSProvider";
   protected long reconnectInterval = 10000;

   protected boolean useDLQ = true;
   protected String dlq = "queue/DLQ";
   protected int dlqMaxTimesRedelivered = 10;
   protected int dlqTimeToLive = 0;
   protected String dlqUser;
   protected String dlqPassword;

   protected String messageSelector;
   protected String destinationType;
   protected String user;
   protected String password;
   protected String destination;
   protected String acknowledgeMode;
   protected String clientID;
   protected String durability;
   protected String subscriptionName;
   protected String messagingType;
   protected String resourceAdaptorName;

   public String getDlqUser()
   {
      return dlqUser;
   }

   public void setDlqUser(String dlqUser)
   {
      this.dlqUser = dlqUser;
   }

   public String getDlqPassword()
   {
      return dlqPassword;
   }

   public void setDlqPassword(String dlqPassword)
   {
      this.dlqPassword = dlqPassword;
   }

   /**
    * Use a Dead-letter queue for undeliverable messages?
    *
    * @return
    */
   public boolean isUseDLQ()
   {
      return useDLQ;
   }

   public void setUseDLQ(boolean useDLQ)
   {
      this.useDLQ = useDLQ;
   }

   /**
    * JNDI name of the DLQ
    *
    * @return
    */
   public String getDlq()
   {
      return dlq;
   }

   public void setDlq(String dlq)
   {
      this.dlq = dlq;
   }

   /**
    * How many times should a message be redelivered before it is put into the DLQ
    *
    * @return
    */
   public int getDlqMaxTimesRedelivered()
   {
      return dlqMaxTimesRedelivered;
   }

   public void setDlqMaxTimesRedelivered(int dlqMaxTimesRedelivered)
   {
      this.dlqMaxTimesRedelivered = dlqMaxTimesRedelivered;
   }

   public int getDlqTimeToLive()
   {
      return dlqTimeToLive;
   }

   public void setDlqTimeToLive(int dlqTimeToLive)
   {
      this.dlqTimeToLive = dlqTimeToLive;
   }

   public String getProviderAdapterJNDI()
   {
      return providerAdapterJNDI;
   }

   public void setProviderAdapterJNDI(String providerAdapterJNDI)
   {
      this.providerAdapterJNDI = providerAdapterJNDI;
   }

   public String getAcknowledgeMode()
   {
      return acknowledgeMode;
   }

   public void setAcknowledgeMode(String acknowledgeMode)
   {
      this.acknowledgeMode = acknowledgeMode;
   }

   public String getClientID()
   {
      return clientID;
   }

   public void setClientID(String clientID)
   {
      this.clientID = clientID;
   }

   /**
    * Set to "Durable" or "NonDurable"
    * <p/>
    * Specify wether or not the topic should be durable.
    *
    * @return
    */
   public String getDurability()
   {
      return durability;
   }

   public void setDurability(String durability)
   {
      this.durability = durability;
   }

   public String getSubscriptionName()
   {
      return subscriptionName;
   }

   public void setSubscriptionName(String subscriptionName)
   {
      this.subscriptionName = subscriptionName;
   }

   public String getDestination()
   {
      return destination;
   }

   public void setDestination(String destination)
   {
      this.destination = destination;
   }

   public String getUser()
   {
      return user;
   }

   public void setUser(String user)
   {
      this.user = user;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public String getMessageSelector()
   {
      return messageSelector;
   }

   public void setMessageSelector(String messageSelector)
   {
      this.messageSelector = messageSelector;
   }
   
   public String getMessagingType()
   {
      return messagingType;
   }

   public void setMessagingType(String messagingType)
   {
      this.messagingType = messagingType;
   }
   
   public String getResourceAdaptorName()
   {
      return resourceAdaptorName;
   }

   public void setResourceAdaptorName(String resourceAdaptorName)
   {
      this.resourceAdaptorName = resourceAdaptorName;
   }

   public String getDestinationType()
   {
      return destinationType;
   }

   public void setDestinationType(String destinationType)
   {
      this.destinationType = destinationType;
   }

   public int getMinPoolSize()
   {
      return minPoolSize;
   }

   public void setMinPoolSize(int minPoolSize)
   {
      this.minPoolSize = minPoolSize;
   }

   public int getMaxPoolSize()
   {
      return maxPoolSize;
   }

   public void setMaxPoolSize(int maxPoolSize)
   {
      this.maxPoolSize = maxPoolSize;
   }

   public int getKeepAlive()
   {
      return keepAlive;
   }

   public void setKeepAlive(int keepAlive)
   {
      this.keepAlive = keepAlive;
   }

   public int getMaxMessages()
   {
      return maxMessages;
   }

   public void setMaxMessages(int maxMessages)
   {
      this.maxMessages = maxMessages;
   }

   public String getServerSessionPoolFactoryJNDI()
   {
      return serverSessionPoolFactoryJNDI;
   }

   public void setServerSessionPoolFactoryJNDI(String serverSessionPoolFactoryJNDI)
   {
      this.serverSessionPoolFactoryJNDI = serverSessionPoolFactoryJNDI;
   }

   public long getReconnectInterval()
   {
      return reconnectInterval;
   }

   public void setReconnectInterval(long reconnectInterval)
   {
      this.reconnectInterval = reconnectInterval;
   }

   public static MDBConfig createMDBConfig(ActivationSpec spec)
   {
      MDBConfig instance = new MDBConfig();
      BeanInfo beanInfo = null;
      try
      {
         beanInfo = Introspector.getBeanInfo(MDBConfig.class);
      }
      catch (IntrospectionException e)
      {
         throw new RuntimeException(e.getMessage(), e);
      }
      PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
      if (descriptors == null)
      {
         descriptors = new PropertyDescriptor[0];
      }

      for (String name : spec.keySet())
      {
         setAttribute(instance, descriptors, name, spec.get(name));

      }
      return instance;
   }

   private static void setAttribute(Object instance, PropertyDescriptor[] descriptors, String attributeName, String attributeText)
   {
      for (int i = 0; i < descriptors.length; i++)
      {
         if (attributeName.equalsIgnoreCase(descriptors[i].getName()))
         {
            Class typeClass = descriptors[i].getPropertyType();

            Object value;
            PropertyEditor editor = PropertyEditorManager.findEditor(typeClass);
            if (editor == null)
            {
               throw new RuntimeException
                       ("No property editor for attribute: " + attributeName +
                        "; type=" + typeClass);
            }

            editor.setAsText(attributeText);
            value = editor.getValue();
            try
            {
               descriptors[i].getWriteMethod().invoke(instance, new Object[]{value});
            }
            catch (Exception e)
            {
               throw new RuntimeException("Error setting attribute '" +
                                          attributeName + "' in MDBConfig", e);
            }
            break;
         }
      }//for descriptors
   }

}
