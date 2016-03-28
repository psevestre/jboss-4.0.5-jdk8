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
package org.jboss.ejb3.dd;

import org.jboss.logging.Logger;


/**
 * Represents a <message-destination-ref> element of the ejb-jar.xml deployment descriptor for the
 * 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class MessageDestinationRef
{
   private static final Logger log = Logger.getLogger(MessageDestinationRef.class);
   
   private String description;

   private String messageDestinationRefName;

   private String messageDestinationType;

   private String messageDestinationUsage;

   private String messageDestinationLink;

   private InjectionTarget injectionTarget;

   private String mappedName;
   
   private String jndiName;
   
   public String getJndiName()
   {
      return jndiName;
   }

   public void setJndiName(String jndiName)
   {
      this.jndiName = jndiName;
   }

   public String getMappedName()
   {     
      return mappedName;
   }

   public void setMappedName(String mappedName)
   {
      this.mappedName = mappedName;
   }

   public InjectionTarget getInjectionTarget()
   {
      return injectionTarget;
   }

   public void setInjectionTarget(InjectionTarget injectionTarget)
   {
      this.injectionTarget = injectionTarget;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getMessageDestinationRefName()
   {
      return messageDestinationRefName;
   }

   public void setMessageDestinationRefName(String messageDestinationRefName)
   {
      this.messageDestinationRefName = messageDestinationRefName;
   }

   public String getMessageDestinationType()
   {
      return messageDestinationType;
   }

   public void setMessageDestinationType(String messageDestinationType)
   {
      this.messageDestinationType = messageDestinationType;
   }

   public String getMessageDestinationUsage()
   {
      return messageDestinationUsage;
   }

   public void setMessageDestinationUsage(String messageDestinationUsage)
   {
      this.messageDestinationUsage = messageDestinationUsage;
   }

   public String getMessageDestinationLink()
   {
      return messageDestinationLink;
   }

   public void setMessageDestinationLink(String messageDestinationLink)
   {
      this.messageDestinationLink = messageDestinationLink;
   }
   
   public void merge(MessageDestinationRef ref)
   {
      if (ref.getJndiName() != null)
      {
         this.setJndiName(ref.getJndiName());
         
         setMappedName(ref.getJndiName());
      }
   }


   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("messageDestinationRefName=").append(messageDestinationRefName);
      sb.append("]");
      return sb.toString();
   }
}
