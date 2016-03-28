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
package org.jboss.metadata.serviceref;

// $Id: PortComponentRefMetaData.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import org.jboss.deployment.DeploymentException;
import org.jboss.metadata.MetaData;
import org.w3c.dom.Element;

import javax.xml.rpc.JAXRPCException;
import java.io.Serializable;
import java.util.Properties;
import java.util.Iterator;

/** The metdata data from service-ref/port-component-ref element in web.xml, ejb-jar.xml, and application-client.xml.
 *
 * @author Thomas.Diesler@jboss.org
 * @version $Revision: 57209 $
 */
public class PortComponentRefMetaData implements Serializable
{
   static final long serialVersionUID = 3856598615591044263L;
   
   // The parent service-ref
   private ServiceRefMetaData serviceRefMetaData;

   // The required <service-endpoint-interface> element
   private String serviceEndpointInterface;
   // The optional <port-component-link> element
   private String portComponentLink;

   /** Arbitrary proxy properties given by <call-property> */
   private Properties callProperties;

   public PortComponentRefMetaData(ServiceRefMetaData serviceRefMetaData)
   {
      this.serviceRefMetaData = serviceRefMetaData;
   }

   public ServiceRefMetaData getServiceRefMetaData()
   {
      return serviceRefMetaData;
   }

   public String getPortComponentLink()
   {
      return portComponentLink;
   }

   public String getServiceEndpointInterface()
   {
      return serviceEndpointInterface;
   }

   public Class getServiceEndpointInterfaceClass()
   {
      try
      {
         ClassLoader cl = serviceRefMetaData.getResourceCL();
         return cl.loadClass(serviceEndpointInterface);
      }
      catch (ClassNotFoundException e)
      {
         throw new JAXRPCException("Cannot load service endpoint interface: " + serviceEndpointInterface);
      }
   }

   public Properties getCallProperties()
   {
      return callProperties;
   }

   public void importStandardXml(Element element)
           throws DeploymentException
   {
      serviceEndpointInterface = MetaData.getUniqueChildContent(element, "service-endpoint-interface");

      portComponentLink = MetaData.getOptionalChildContent(element, "port-component-link");
   }

   public void importJBossXml(Element element) throws DeploymentException
   {
      // Look for call-property elements
      Iterator iterator = MetaData.getChildrenByTagName(element, "call-property");
      while (iterator.hasNext())
      {
         Element propElement = (Element) iterator.next();
         String name = MetaData.getUniqueChildContent(propElement, "prop-name");
         String value = MetaData.getUniqueChildContent(propElement, "prop-value");
         if( callProperties == null )
            callProperties = new Properties();
         callProperties.setProperty(name, value);
      }

   }
}
