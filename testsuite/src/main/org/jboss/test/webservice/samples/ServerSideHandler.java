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
package org.jboss.test.webservice.samples;

import org.jboss.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

/**
 * A simple server side handler
 * 
 * @author thomas.diesler@jboss.org
 */
public class ServerSideHandler extends GenericHandler
{
   // provide logging
   private static final Logger log = Logger.getLogger(ServerSideHandler.class);

   // header blocks processed by this Handler instance
   private QName[] headers;

   /**
    * Gets the header blocks processed by this Handler instance.
    *
    * @return Array of QNames of header blocks processed by this handler instance.
    * QName is the qualified name of the outermost element of the Header block.
    */
   public QName[] getHeaders()
   {
      return headers;
   }

   /**
    * The init method to enable the Handler instance to initialize itself. This method should be overridden if the
    * derived Handler class needs to specialize implementation of this method.
    * @param config handler configuration
    */
   public void init(HandlerInfo config)
   {
      log.info("init");
      headers = config.getHeaders();
   }

   /**
    * The destroy method indicates the end of lifecycle for a Handler instance. This method should be overridden if
    * the derived Handler class needs to specialize implementation of this method.
    */
   public void destroy()
   {
      log.info("destroy");
   }

   /**
    * The handleRequest method processes the request SOAP message. The default implementation of this method returns true.
    * This indicates that the handler chain should continue processing of the request SOAP message.
    * This method should be overridden if the derived Handler class needs to specialize implementation of this method.
    * @param msgContext the message msgContext
    * @return true/false
    */
   public boolean handleRequest(MessageContext msgContext)
   {
      log.info("handleRequest");
      return true;
   }

   /**
    * The handleResponse method processes the response message. The default implementation of this method returns true.
    * This indicates that the handler chain should continue processing of the response SOAP message.
    * This method should be overridden if the derived Handler class needs to specialize implementation of this method.
    * @param msgContext the message msgContext
    * @return true/false
    */
   public boolean handleResponse(MessageContext msgContext)
   {
      log.info("handleResponse");
      return true;
   }

   /**
    * The handleFault method processes the SOAP faults based on the SOAP message processing model.
    * The default implementation of this method returns true. This indicates that the handler chain should continue
    * processing of the SOAP fault. This method should be overridden if the derived Handler class needs to specialize
    * implementation of this method.
    * @param msgContext the message msgContext
    * @return the message msgContext
    */
   public boolean handleFault(MessageContext msgContext)
   {
      log.info("handleFault");
      return true;
   }
}
