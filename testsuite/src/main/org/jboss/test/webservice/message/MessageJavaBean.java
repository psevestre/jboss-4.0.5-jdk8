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
package org.jboss.test.webservice.message;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;

import org.jboss.logging.Logger;
import org.jboss.util.xml.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ibm.wsdl.util.xml.DOM2Writer;

/**
 * @author Thomas.Diesler@jboss.org
 * @since 26-Nov-2004
 */
public class MessageJavaBean implements Message
{
   // provide logging
   private final Logger log = Logger.getLogger(MessageJavaBean.class);

   /** org.w3c.dom.Element
    */
   public Element processElement(Element msg) throws RemoteException
   {
      StringWriter swr = new StringWriter();
      DOM2Writer.serializeAsXML(msg, swr);
      log.info("processElement: " + swr);

      try
      {
         Element reqEl = (Element)msg;

         // verify order element
         QName qname = new QName(NSURI_1, "Order", PREFIX_1);
         QName elementName = new QName(reqEl.getNamespaceURI(), reqEl.getLocalName(), reqEl.getPrefix());
         if (qname.equals(elementName) == false)
            throw new IllegalArgumentException("Unexpected element: " + elementName);

         // Verify the custom attribute
         String attrVal = reqEl.getAttribute("attrval");
         if ("somevalue".equals(attrVal) == false)
            throw new IllegalArgumentException("Unexpected attribute value: " + attrVal);

         // Namespace attribute handling seems to be broken
         if (isWS4EEAvailable() == false)
         {
            // [TODO] The reqEl should not contain the xmlns:env declaration
            //NamedNodeMap attributes = reqEl.getAttributes();
            //if (attributes.getLength() != 3)
            //   throw new IllegalArgumentException("Unexpected number of attributes: " + attributes.getLength());

            // Verify NS declarations
            String nsURI_1 = reqEl.getAttribute("xmlns:" + PREFIX_1);
            if (NSURI_1.equals(nsURI_1) == false)
               throw new IllegalArgumentException("Unexpected namespace URI: " + nsURI_1);

            String nsURI_2 = reqEl.getAttribute("xmlns:" + PREFIX_2);
            if (NSURI_2.equals(nsURI_2) == false)
               throw new IllegalArgumentException("Unexpected namespace URI: " + nsURI_2);
         }

         // Test getElementsByTagNameNS
         // http://jira.jboss.com/jira/browse/JBWS-99
         NodeList nodeList1 = reqEl.getElementsByTagNameNS(NSURI_2, "Customer");
         if (nodeList1.getLength() != 1)
            throw new IllegalArgumentException("Cannot getElementsByTagNameNS");

         // Test getElementsByTagName
         // http://jira.jboss.com/jira/browse/JBWS-99
         NodeList nodeList2 = reqEl.getElementsByTagName("Item");
         if (nodeList2.getLength() != 1)
            throw new IllegalArgumentException("Cannot getElementsByTagName");

         // verify customer element
         qname = new QName(NSURI_2, "Customer", PREFIX_2);
         Element custEl = DOMUtils.getFirstChildElement(reqEl, qname);
         String elementValue = DOMUtils.getTextContent(custEl);
         if ("Customer".equals(custEl.getLocalName()) == false || "Kermit".equals(elementValue) == false)
            throw new IllegalArgumentException("Unexpected element value: " + elementValue);

         // verify item element
         qname = new QName("Item");
         Element itemEl = DOMUtils.getFirstChildElement(reqEl, qname);
         elementValue = DOMUtils.getTextContent(itemEl);
         if ("Item".equals(itemEl.getLocalName()) == false || "Ferrari".equals(elementValue) == false)
            throw new IllegalArgumentException("Unexpected element value: " + elementValue);

         // Setup document builder
         DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
         docBuilderFactory.setNamespaceAware(true);

         // Prepare response
         DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
         Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));
         Element resElement = doc.getDocumentElement();

         return resElement;
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RemoteException(e.toString(), e);
      }
   }

   private boolean isWS4EEAvailable()
   {
      try
      {
         ServiceFactory factory = ServiceFactory.newInstance();
         if ("org.jboss.webservice.client.ServiceFactoryImpl".equals(factory.getClass().getName()))
            return true;
      }
      catch (ServiceException e)
      {
         // ignore
      }
      return false;
   }
}
