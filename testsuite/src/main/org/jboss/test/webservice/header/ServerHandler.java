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
import org.jboss.util.id.UID;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import java.util.Iterator;

public class ServerHandler extends GenericHandler
{
   // provide logging
   private final Logger log = Logger.getLogger(ServerHandler.class);

   private static final String NAMESPACE_URI = ImplicitHeaderEndpoint.NAMESPACE_URI;
   private static final String LOCAL_PART = ImplicitHeaderEndpoint.LOCAL_PART;
   private static final String PREFIX = ImplicitHeaderEndpoint.PREFIX;

   private String username;
   private String sessionID;

   public QName[] getHeaders()
   {
      QName qname = new QName(NAMESPACE_URI, LOCAL_PART);
      return new QName[]{qname};
   }

   public boolean handleRequest(MessageContext msgContext)
   {
      try
      {
         SOAPFactory factory = SOAPFactory.newInstance();

         SOAPMessage soapMessage = ((SOAPMessageContext)msgContext).getMessage();
         SOAPHeader soapHeader = soapMessage.getSOAPHeader();

         // Do not extract, the endpoint wants to see the header
         Iterator it = soapHeader.examineAllHeaderElements();

         if (it.hasNext() == false)
            throw new SOAPException("No header element");

         SOAPHeaderElement headerElement = (SOAPHeaderElement)it.next();

         Name usernameName = factory.createName("username", PREFIX, NAMESPACE_URI);
         SOAPElement usrElement = (SOAPElement)headerElement.getChildElements(usernameName).next();
         username = usrElement.getValue();

         Name sessionIDName = factory.createName("sessionID", PREFIX, NAMESPACE_URI);
         SOAPElement seElement = (SOAPElement)headerElement.getChildElements(sessionIDName).next();
         sessionID = seElement.getValue();

         log.info("username: " + username);
         log.info("sessionID: " + sessionID);

         if (sessionID == null)
            sessionID = UID.asString();

         return true;
      }
      catch (SOAPException e)
      {
         throw new JAXRPCException(e.toString(), e);
      }
   }

   public boolean handleResponse(MessageContext msgContext)
   {
      try
      {
         SOAPMessage soapMessage = ((SOAPMessageContext)msgContext).getMessage();
         SOAPFactory factory = SOAPFactory.newInstance();

         SOAPHeader soapHeader = soapMessage.getSOAPHeader();
         Name name = factory.createName(LOCAL_PART, PREFIX, NAMESPACE_URI);
         SOAPHeaderElement headerElement = soapHeader.addHeaderElement(name);

         SOAPElement usrElement = headerElement.addChildElement("username", PREFIX, NAMESPACE_URI);
         usrElement.setValue(username);

         SOAPElement seElement = headerElement.addChildElement("sessionID", PREFIX, NAMESPACE_URI);
         seElement.setValue(sessionID);

         log.info("username: " + username);
         log.info("sessionID: " + sessionID);
         return true;

      }
      catch (SOAPException e)
      {
         throw new JAXRPCException(e.toString(), e);
      }
   }
}
