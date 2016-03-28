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
package org.jboss.test.webservice.handlerflow;

import java.io.ByteArrayInputStream;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;


public class ClientHandler2 extends HandlerBase
{
   public boolean handleRequest(MessageContext msgContext)
   {
      boolean retflag = super.handleRequest(msgContext);
      
      try
      {
         SOAPMessageContext soapContext = (SOAPMessageContext)msgContext;
         SOAPMessage soapMessage = soapContext.getMessage();
         SOAPBody soapBody = soapMessage.getSOAPBody();
         SOAPElement soapElement = (SOAPElement)soapBody.getChildElements().next();
         soapElement = (SOAPElement)soapElement.getChildElements().next();
         String value = soapElement.getValue();
         
         /*
          * Return false to indicate blocking of the request handler chain. In this case, further
          * processing of the request handler chain is blocked and the target service endpoint is
          * not dispatched. The JAX-RPC runtime system takes the responsibility of invoking the
          * response handler chain next with the appropriate SOAPMessageContext. The Handler
          * implementation class has the responsibility of setting the response SOAP message in
          * the handleRequest method and perform additional processing in the
          * handleResponse method. In the default processing model, the response handler
          * chain starts processing from the same Handler instance (that returned false) and
          * goes backward in the execution sequence.
          */
         if ("ClientReturn".equals(value))
         {
            String resMsg = 
               "<env:Envelope xmlns:env='http://schemas.xmlsoap.org/soap/envelope/'>" +
               "  <env:Header/>" +
               "  <env:Body>" +
               "    <ns1:sayHelloResponse xmlns:ns1='http://test.jboss.org/webservice/handlerflow'>" +
               "      <result>Return in ClientHandler2</result>" +
               "    </ns1:sayHelloResponse>" +
               "  </env:Body>" +
               "</env:Envelope>";
            
            MessageFactory factory = MessageFactory.newInstance();
            soapMessage = factory.createMessage(null, new ByteArrayInputStream(resMsg.getBytes()));
            soapContext.setMessage(soapMessage);
            
            return false;
         }
      }
      catch (Exception ex)
      {
         throw new IllegalStateException("Cannot handle request::" + ex.getMessage());
      }
      return retflag;
   }
}
