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
package org.jboss.test.webservice.header;

import org.jboss.logging.Logger;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import java.rmi.RemoteException;
import java.util.Iterator;

/**
 * @author Thomas.Diesler@jboss.org
 * @since 26-Nov-2004
 */
public class ImplicitHeaderEndpointImpl implements ImplicitHeaderEndpoint, ServiceLifecycle
{
   // provide logging
   private final Logger log = Logger.getLogger(ImplicitHeaderEndpointImpl.class);

   private static final String NAMESPACE_URI = ImplicitHeaderEndpoint.NAMESPACE_URI;
   private static final String PREFIX = ImplicitHeaderEndpoint.PREFIX;

   private ServletEndpointContext context;

   public boolean doStuff(String parameter) throws RemoteException
   {
      if (parameter == null)
         throw new IllegalArgumentException("Null parameter");

      try
      {
         SOAPFactory factory = SOAPFactory.newInstance();

         SOAPMessageContext msgContext = (SOAPMessageContext)context.getMessageContext();
         SOAPMessage soapMessage = msgContext.getMessage();
         SOAPHeader soapHeader = soapMessage.getSOAPHeader();
         Iterator it = soapHeader.extractAllHeaderElements();
         if (it.hasNext() == false)
            throw new IllegalArgumentException("No header elements");

         SOAPHeaderElement headerElement = (SOAPHeaderElement)it.next();
         Name usernameName = factory.createName("username", PREFIX, NAMESPACE_URI);
         SOAPElement usrElement = (SOAPElement)headerElement.getChildElements(usernameName).next();
         String username = usrElement.getValue();

         Name sessionIDName = factory.createName("sessionID", PREFIX, NAMESPACE_URI);
         SOAPElement seElement = (SOAPElement)headerElement.getChildElements(sessionIDName).next();
         String sessionID = seElement.getValue();

         log.info("username: " + username);
         log.info("sessionID: " + sessionID);

         return true;
      }
      catch (SOAPException e)
      {
         log.error(e);
         return false;
      }
   }

   public void init(Object context) throws ServiceException
   {
      log.info("init: " + context);
      this.context = (ServletEndpointContext)context;
   }

   public void destroy()
   {
      log.info("destroy: " + context);
   }
}
