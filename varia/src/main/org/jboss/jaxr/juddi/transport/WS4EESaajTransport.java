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
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * Transport based on SAAJ
 * Used for the Axis based stack
 *
 * @author Anil Saldhana (anil@apache.org)
 */
public class WS4EESaajTransport implements Transport
{
    // private reference to the jUDDI logger
    private static Log log = LogFactory.getLog(WS4EESaajTransport.class);

    private String debugProp = System.getProperty("jaxr.debug","false");

    public Element send(Element request, URL endpointURL)
            throws RegistryException
    {
        log.debug("Request message:" + XMLUtils.toString(request));
        if("true".equalsIgnoreCase(debugProp))
            System.out.println("Request Element:" + XMLUtils.toString(request));

        Element response = null;
        try
        {
            MessageFactory msgFactory = MessageFactory.newInstance();
            SOAPMessage message = msgFactory.createMessage();
            message.getSOAPHeader().detachNode();
            SOAPPart soapPart = message.getSOAPPart();
            SOAPBody soapBody = soapPart.getEnvelope().getBody();
            soapBody.addChildElement(getSOAPElement(soapBody, request));
            //There seems to be a bug in the Saaj/Axis implementation that requires
            //message to be written to an output stream
            ByteArrayOutputStream by = new ByteArrayOutputStream();
            message.writeTo(by); //Does not do anything
            by.close();
            if("true".equalsIgnoreCase(debugProp))
              message.writeTo(System.out);

            //Make the SAAJ Call now
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = connection.call(message, endpointURL);
            if("true".equalsIgnoreCase(debugProp))
            {
               System.out.println("Response is:");
               soapResponse.writeTo(System.out);
            }

            soapBody = soapResponse.getSOAPBody();
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
            ex.printStackTrace();
            log.error(ex);
            throw new RegistryException(ex);
        }

        log.debug("Response message:" + XMLUtils.getText(response));


        return response;
    }

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

    private SOAPElement getSOAPElement(SOAPBody soapBody, Element elem)
    {
        String xmlns = IRegistry.UDDI_V2_NAMESPACE;
        SOAPElement soapElement = null;
        SOAPFactory factory = null;
        try
        {
            factory = SOAPFactory.newInstance();
            //Go through the element
            String name = elem.getNodeName();
            String nsuri = elem.getNamespaceURI();
            if (nsuri == null)
                nsuri = xmlns;
            soapElement = factory.createElement(name, "ns1", nsuri);
            //Get Attributes
            if (elem.hasAttributes())
            {
                NamedNodeMap nnm = elem.getAttributes();
                int len = nnm != null ? nnm.getLength() : 0;
                for (int i = 0; i < len; i++)
                {
                    Node n = nnm.item(i);
                    String nodename = n.getNodeName();
                    String nodevalue = n.getNodeValue();
                    soapElement.addAttribute(factory.createName(nodename), nodevalue);
                }
            } else
            {
                soapElement.addAttribute(factory.createName("xmlns:ns1"), nsuri);
            }

            NodeList nlist = elem.getChildNodes();
            int len = nlist != null ? nlist.getLength() : 0;

            for (int i = 0; i < len; i++)
            {
                Node node = nlist.item(i);
                short nodeType = node != null ? node.getNodeType() : -100;
                if (Node.ELEMENT_NODE == nodeType)
                {
                    soapElement.addChildElement(getSOAPElement(soapBody, (Element) node));
                } else if (nodeType == Node.TEXT_NODE)
                {
                    soapElement.addTextNode(node.getNodeValue());
                }

            }
        } catch (SOAPException e)
        {
            e.printStackTrace();
        }
        return soapElement;
    }

    public static Element getElement(String xmlFrag)
    {
        Document doc = null;
        Element reqElement = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xmlFrag)));
            reqElement = doc.getDocumentElement();
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        return reqElement;

    }

    public Element getFirstChildElement(Element el)
    {
        return getFirstChildElement(el, null);
    }

    /**
     * Return the first child element for a givenname
     */
    public Element getFirstChildElement(Element el, String tagName)
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
        return childEl;
    }
}
