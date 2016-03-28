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
package org.jboss.webservice.transport.jms;

import javax.xml.rpc.ServiceFactory;

/** 
 * Create the SOAP message dispatcher for the installed SOAP stack
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 19-Feb-2006
 */
public class MessageDispatcherFactory
{
   // Hide constructor
   private MessageDispatcherFactory()
   {
   }

   /** Create the SOAP message dispatcher for the installed SOAP stack
    */
   public static MessageDispatcher createMessageDispatcher()
   {
      MessageDispatcher msgDispatcher = null;
      try
      {
         ServiceFactory factory = ServiceFactory.newInstance();
         ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
         if ("org.jboss.webservice.client.ServiceFactoryImpl".equals(factory.getClass().getName()))
         {
            // Load the jboss-ws4ee message dispatcher
            Class clazz= ctxLoader.loadClass("org.jboss.webservice.transport.jms.AxisMessageDispatcher");
            msgDispatcher = (MessageDispatcher)clazz.newInstance();
         }
         else if ("org.jboss.ws.jaxrpc.ServiceFactoryImpl".equals(factory.getClass().getName()))
         {
            // Load the jbossws message dispatcher
            Class clazz= ctxLoader.loadClass("org.jboss.ws.transport.jms.JMSMessageDispatcher");
            msgDispatcher = (MessageDispatcher)clazz.newInstance();
         }
      }
      catch (Exception ex)
      {
         // ignore
      }
      return msgDispatcher;
   }
}