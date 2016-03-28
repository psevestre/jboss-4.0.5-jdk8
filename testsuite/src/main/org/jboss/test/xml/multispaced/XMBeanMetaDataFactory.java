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
package org.jboss.test.xml.multispaced;

import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.jboss.test.xml.multispaced.pm.jdbc.JDBCPm;
import org.xml.sax.Attributes;

/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 57211 $</tt>
 */
public class XMBeanMetaDataFactory
   implements ObjectModelFactory
{
   public static final XMBeanMetaDataFactory INSTANCE = new XMBeanMetaDataFactory();

   private XMBeanMetaDataFactory()
   {
   }

   public Object newRoot(Object root,
                               UnmarshallingContext navigator,
                               String namespaceURI,
                               String localName,
                               Attributes attrs)
   {
      return root == null ? new XMBeanMetaData() : root;
   }

   public Object completeRoot(Object root, UnmarshallingContext ctx, String uri, String name)
   {
      return root;
   }
   
   public void setValue(XMBeanMetaData xmbean,
                        UnmarshallingContext navigator,
                        String namespaceUri,
                        String localName,
                        String value)
   {
      if("description".equals(localName))
      {
         xmbean.setDescription(value);
      }
      else if("class".equals(localName))
      {
         xmbean.setMbeanClass(value);
      }
   }

   public Object newChild(XMBeanMetaData xmbean,
                          UnmarshallingContext navigator,
                          String namespaceUri,
                          String localName,
                          Attributes attrs)
   {
      Object child;
      if("constructor".equals(localName))
      {
         child = new XMBeanConstructorMetaData();
      }
      else if("attribute".equals(localName))
      {
         final XMBeanAttributeMetaData attribute = new XMBeanAttributeMetaData();
         for(int i = 0; i < attrs.getLength(); ++i)
         {
            final String attrName = attrs.getLocalName(i);
            if("access".equals(attrName))
            {
               attribute.setAccess(attrs.getValue(i));
            }
            else if("getMethod".equals(attrName))
            {
               attribute.setGetMethod(attrs.getValue(i));
            }
            else if("setMethod".equals(attrName))
            {
               attribute.setSetMethod(attrs.getValue(i));
            }
         }
         child = attribute;
      }
      else if("operation".equals(localName))
      {
         child = new XMBeanOperationMetaData();
      }
      else if("notification".equals(localName))
      {
         child = new XMBeanNotificationMetaData();
      }
      else
      {
         child = null;
      }

      return child;
   }

   public void addChild(XMBeanMetaData xmbean, XMBeanConstructorMetaData constructor, UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      xmbean.addConstructor(constructor);
   }

   public void addChild(XMBeanMetaData xmbean, XMBeanAttributeMetaData attribute, UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      xmbean.addAttribute(attribute);
   }

   public void addChild(XMBeanMetaData xmbean, XMBeanOperationMetaData operation, UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      xmbean.addOperation(operation);
   }

   public void addChild(XMBeanMetaData xmbean, XMBeanNotificationMetaData notification, UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      xmbean.addNotification(notification);
   }

   public void addChild(XMBeanMetaData xmbean,
                        Object pm,
                        UnmarshallingContext navigator,
                        String namespaceURI,
                        String localName)
   {
      xmbean.setPersistenceManager(pm);
   }

   public void setValue(XMBeanConstructorMetaData constructor,
                        UnmarshallingContext navigator,
                        String namespaceUri,
                        String localName,
                        String value)
   {
      if("description".equals(localName))
      {
         constructor.setDescription(value);
      }
      else if("name".equals(localName))
      {
         constructor.setName(value);
      }
   }

   public void setValue(XMBeanAttributeMetaData attribute,
                        UnmarshallingContext navigator,
                        String namespaceUri,
                        String localName,
                        String value)
   {
      if("description".equals(localName))
      {
         attribute.setDescription(value);
      }
      else if("name".equals(localName))
      {
         attribute.setName(value);
      }
      else if("type".equals(localName))
      {
         attribute.setType(value);
      }
   }

   public void setValue(XMBeanOperationMetaData operation,
                        UnmarshallingContext navigator,
                        String namespaceUri,
                        String localName,
                        String value)
   {
      if("description".equals(localName))
      {
         operation.setDescription(value);
      }
      else if("name".equals(localName))
      {
         operation.setName(value);
      }
      else if("return-type".equals(localName))
      {
         operation.setReturnType(value);
      }
   }

   public void setValue(XMBeanNotificationMetaData notification,
                        UnmarshallingContext navigator,
                        String namespaceUri,
                        String localName,
                        String value)
   {
      if("description".equals(localName))
      {
         notification.setDescription(value);
      }
      else if("name".equals(localName))
      {
         notification.setName(value);
      }
      else if("notification-type".equals(localName))
      {
         notification.setNotificationType(value);
      }
   }
}
