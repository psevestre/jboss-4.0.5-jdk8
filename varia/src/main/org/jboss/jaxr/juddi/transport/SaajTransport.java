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
package org.jboss.jaxr.juddi.transport;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.IRegistry;
import org.apache.juddi.error.RegistryException;
import org.apache.juddi.proxy.Transport;
import org.apache.juddi.util.xml.XMLUtils;
import org.jboss.util.xml.DOMWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Transport based on SAAJ
 *
 * @author Anil Saldhana (anil@apache.org)
 */
public class SaajTransport implements Transport
{
    // private reference to the jUDDI logger
    private static Log log = LogFactory.getLog(SaajTransport.class); 
    
    /**
     * @see Transport#send(org.w3c.dom.Element, java.net.URL)
     */
    public Element send(Element request, URL endpointURL)
            throws RegistryException
    {
       String requestMessage = XMLUtils.toString(request);
        log.debug("Request message:" + requestMessage); 

        Element response = null;
        try
        {
           SOAPMessage message = this.createSOAPMessage(request);
           //Make the SAAJ Call now
           SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
           SOAPConnection connection = soapConnectionFactory.createConnection();
           SOAPMessage soapResponse = connection.call(message, endpointURL); 
           
           SOAPBody soapBody = soapResponse.getSOAPBody();
           boolean hasFault = soapBody.hasFault();
           if(hasFault)
           {
              SOAPFault soapFault = soapBody.getFault();
              String faultStr = soapFault.getFaultCode() + "::" + soapFault.getFaultString();
              throw new RegistryException(faultStr);
           }
           response = getFirstChildElement(soapBody);
        } catch (Exception ex)
        { 
            log.error("Exception::",ex);
            throw new RegistryException(ex);
        }

        log.debug("Response message:" + XMLUtils.getText(response)); 
        return response;
    }

    /**
     * @see Transport#send(java.lang.String, java.net.URL)
     */
    public String send(String request, URL endpointURL)
            throws RegistryException
    {
        Element reqEl = getElement(request);
        Element respEl = this.send(reqEl, endpointURL);
        StringWriter sw = new StringWriter();

        DOMWriter dw = new DOMWriter(sw);
        dw.print(respEl);
        return sw.toString();
        //return XMLUtils.toString(respEl);
    }
    
    // PRIVATE METHODS
    
    private SOAPMessage createSOAPMessage(Element elem) throws Exception
    {
       String prefix = "uddi";
       MessageFactory msgFactory = MessageFactory.newInstance();
       SOAPFactory factory = SOAPFactory.newInstance();
       
       SOAPMessage message = msgFactory.createMessage();
       message.getSOAPHeader().detachNode();
       SOAPPart soapPart = message.getSOAPPart();
       SOAPBody soapBody = soapPart.getEnvelope().getBody(); 
       //Create the outer body element
       String uddins = IRegistry.UDDI_V2_NAMESPACE;
       Name bodyName = factory.createName(elem.getNodeName(),prefix,uddins);
       SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
       bodyElement.addNamespaceDeclaration(prefix,uddins);      
       appendAttributes(bodyElement, elem.getAttributes(), factory);
       appendElements(bodyElement,elem.getChildNodes(), factory); 
       return message;
    }
    
    private void appendAttributes(SOAPElement bodyElement, NamedNodeMap nnm,
          SOAPFactory factory) throws SOAPException
    {
       int len = nnm != null ? nnm.getLength() : 0;
       for (int i = 0; i < len; i++)
       {
           Node n = nnm.item(i);
           String nodename = n.getNodeName();
           String nodevalue = n.getNodeValue(); 
           if("xmlns".equals(nodename))
              continue; 
           //Special case:  xml:lang
           if("xml:lang".equals(nodename))
           {
              Name xmlLang = factory.createName("lang","xml",
                    "http://www.w3.org/TR/REC-xml/");
              bodyElement.addAttribute(xmlLang, nodevalue);
           }
           else
               bodyElement.addAttribute(factory.createName(nodename), nodevalue);
       } 
    }
    
    private void appendElements(SOAPElement bodyElement, NodeList nlist,
          SOAPFactory factory) 
    throws SOAPException
    {  
       String prefix = "uddi";
       String uddins = IRegistry.UDDI_V2_NAMESPACE;
       int len = nlist != null ? nlist.getLength() : 0;

       for (int i = 0; i < len; i++)
       {
           Node node = nlist.item(i);
           short nodeType = node != null ? node.getNodeType() : -100;
           if (Node.ELEMENT_NODE == nodeType)
           {
              Element el = (Element)node;
              Name name = factory.createName(el.getNodeName(), prefix,uddins);
              SOAPElement attachedEl = bodyElement.addChildElement(name); 
              appendAttributes(attachedEl, el.getAttributes(), factory);
              appendElements(attachedEl, el.getChildNodes(), factory);
           } else if (nodeType == Node.TEXT_NODE)
           {
               bodyElement.addTextNode(node.getNodeValue());
           } 
       } 
    } 
    

    /**
     * Get an Element given a string
     * 
     * @param xmlFrag
     * @return
     */
    private static Element getElement(String xmlFrag)
    {
        Document doc = null;
        Element reqElement = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlFrag)));
            reqElement = doc.getDocumentElement();
        } catch (Exception e)
        {
            log.error("Exception:",e);
        }  

        return reqElement; 
    }

    /**
     * Return the first child element
     * 
     * @param el
     * @return
     */
    private Element getFirstChildElement(Element el)
    {
        return getFirstChildElement(el, null);
    }

    /**
     * Return the first child element for a givenname
     */
    private Element getFirstChildElement(Element el, String tagName)
    {
        Element childEl = null;
        NodeList nlist = el != null ? el.getChildNodes() : null;
        int len = nlist != null ? nlist.getLength() : 0;
        for (int i = 0; childEl == null && i < len; i++)
        {
            Node node = nlist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                if (tagName == null || tagName.equals(node.getLocalName()))
                    childEl = (Element) node;
            }
        }
        String responseObtained =  XMLUtils.toString(childEl);
        log.debug("Response obtained="+responseObtained);
        return childEl;
    }
}
