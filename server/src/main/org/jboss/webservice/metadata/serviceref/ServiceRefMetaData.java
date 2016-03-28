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
package org.jboss.webservice.metadata.serviceref;

// $Id: ServiceRefMetaData.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;

import org.jboss.deployment.DeploymentException;
import org.jboss.metadata.MetaData;
import org.jboss.webservice.metadata.jaxrpcmapping.JavaWsdlMapping;
import org.jboss.webservice.metadata.jaxrpcmapping.JavaWsdlMappingFactory;
import org.jboss.webservice.metadata.wsdl.WSDL11DefinitionFactory;
import org.jboss.xb.QNameBuilder;
import org.w3c.dom.Element;

/** The metdata data from service-ref element in web.xml, ejb-jar.xml, and
 * application-client.xml.
 *
 * @author Thomas.Diesler@jboss.org
 * @version $Revision: 57209 $
 */
public class ServiceRefMetaData implements Serializable
{
   private static final long serialVersionUID = -3723577995017108437L;

   // The required <service-ref-name> element
   private String serviceRefName;
   // The required <service-interface> element
   private String serviceInterface;
   // The optional <wsdl-file> element
   private String wsdlFile;
   // The optional <jaxrpc-mapping-file> element
   private String jaxrpcMappingFile;
   // The optional <service-qname> element
   private QName serviceQName;
   // The LinkedHashMap<String, PortComponentRefMetaData> for <port-component-ref> elements
   private LinkedHashMap portComponentRefs = new LinkedHashMap();
   // The optional <handler> elements
   private ArrayList handlers = new ArrayList();

   // The optional JBossWS config-name
   private String configName;
   // The optional JBossWS config-file
   private String configFile;
   /** The URL of the actual WSDL to use, <wsdl-override> */
   private URL wsdlOverride;
   /** Arbitrary proxy properties given by <call-property> */
   private Properties callProperties;

   // The wsdl definition, if we have one
   private Object wsdlDefinition;
   // The java/wsdl mapping, if we have one
   private Object javaWsdlMapping;

   /** The ClassLoader to load additional resources */
   private transient URLClassLoader resourceCL;

   /** Default constructor, used when unmarshalling on the client side
    */
   public ServiceRefMetaData()
   {
   }

   /** Constructor with a given resource classloader, used on the server side
    */
   public ServiceRefMetaData(URLClassLoader resourceCl)
   {
      setResourceCL(resourceCl);
   }

   /** Set the resource classloader that can load the wsdl file
    * On the client side this is set expicitly after unmarshalling.
    */
   public void setResourceCL(URLClassLoader resourceCL)
   {
      if (resourceCL == null)
         throw new IllegalArgumentException("ResourceClassLoader cannot be null");

      this.resourceCL = resourceCL;
   }

   public URLClassLoader getResourceCL()
   {
      if (resourceCL == null)
         resourceCL = new URLClassLoader(new URL[] {}, Thread.currentThread().getContextClassLoader());

      return resourceCL;
   }

   public String getJaxrpcMappingFile()
   {
      return jaxrpcMappingFile;
   }

   public URL getJavaWsdlMappingURL()
   {
      URL mappingURL = null;
      if (jaxrpcMappingFile != null)
      {
         mappingURL = getResourceCL().findResource(jaxrpcMappingFile);
         if (mappingURL == null)
            throw new IllegalStateException("Cannot find resource: " + jaxrpcMappingFile);
      }
      return mappingURL;
   }

   public JavaWsdlMapping getJavaWsdlMapping()
   {
      if (javaWsdlMapping == null)
      {
         URL mappingURL = getJavaWsdlMappingURL();
         if (mappingURL != null)
         {
            try
            {
               // setup the XML binding Unmarshaller
               JavaWsdlMappingFactory mappingFactory = JavaWsdlMappingFactory.newInstance();
               javaWsdlMapping = mappingFactory.parse(mappingURL);
            }
            catch (Exception e)
            {
               throw new JAXRPCException("Cannot unmarshal jaxrpc-mapping-file: " + jaxrpcMappingFile, e);
            }
         }
      }
      return (JavaWsdlMapping)javaWsdlMapping;
   }

   public Object getUntypedJavaWsdlMapping()
   {
      return javaWsdlMapping;
   }

   public void setUntypedJavaWsdlMapping(Object javaWsdlMapping)
   {
      this.javaWsdlMapping = javaWsdlMapping;
   }

   public PortComponentRefMetaData[] getPortComponentRefs()
   {
      PortComponentRefMetaData[] array = new PortComponentRefMetaData[portComponentRefs.size()];
      portComponentRefs.values().toArray(array);
      return array;
   }

   public HandlerMetaData[] getHandlers()
   {
      HandlerMetaData[] array = new HandlerMetaData[handlers.size()];
      handlers.toArray(array);
      return array;
   }

   public String getServiceInterface()
   {
      return serviceInterface;
   }

   public QName getServiceQName()
   {
      return serviceQName;
   }

   public String getServiceRefName()
   {
      return serviceRefName;
   }

   public String getWsdlFile()
   {
      return wsdlFile;
   }

   public String getConfigFile()
   {
      return configFile;
   }

   public void setConfigFile(String configFile)
   {
      this.configFile = configFile;
   }

   public String getConfigName()
   {
      return configName;
   }

   public void setConfigName(String configName)
   {
      this.configName = configName;
   }
   public URL getWsdlOverride()
   {
      return wsdlOverride;
   }

   public URL getWsdlURL()
   {
      URL wsdlURL = wsdlOverride;
      if (wsdlURL == null && wsdlFile != null)
      {
         wsdlURL = getResourceCL().findResource(wsdlFile);
         if (wsdlURL == null)
            throw new IllegalStateException("Cannot find resource: " + wsdlFile);
      }
      return wsdlURL;
   }

   public Properties getCallProperties()
   {
      return callProperties;
   }

   public Definition getWsdlDefinition()
   {
      if (wsdlDefinition == null)
      {
         URL wsdlURL = getWsdlURL();
         if (wsdlURL != null)
         {
            try
            {
               WSDL11DefinitionFactory factory = WSDL11DefinitionFactory.newInstance();
               wsdlDefinition = factory.parse(wsdlURL);
            }
            catch (WSDLException e)
            {
               throw new IllegalStateException("Cannot unmarshall wsdl, cause: " + e.toString());
            }
         }
      }
      return (Definition)wsdlDefinition;
   }

   public Object getUntypedWsdlDefinition()
   {
      return wsdlDefinition;
   }

   public void setUntypedWsdlDefinition(Object wsdlDefinition)
   {
      this.wsdlDefinition = wsdlDefinition;
   }

   public void importStandardXml(Element element) throws DeploymentException
   {
      serviceRefName = MetaData.getUniqueChildContent(element, "service-ref-name");

      serviceInterface = MetaData.getUniqueChildContent(element, "service-interface");

      wsdlFile = MetaData.getOptionalChildContent(element, "wsdl-file");

      jaxrpcMappingFile = MetaData.getOptionalChildContent(element, "jaxrpc-mapping-file");

      Element qnameElement = MetaData.getOptionalChild(element, "service-qname");
      if (qnameElement != null)
         serviceQName = QNameBuilder.buildQName(qnameElement, MetaData.getElementContent(qnameElement));

      // Parse the port-component-ref elements
      Iterator iterator = MetaData.getChildrenByTagName(element, "port-component-ref");
      while (iterator.hasNext())
      {
         Element pcrefElement = (Element)iterator.next();
         PortComponentRefMetaData pcrefMetaData = new PortComponentRefMetaData(this);
         pcrefMetaData.importStandardXml(pcrefElement);
         portComponentRefs.put(pcrefMetaData.getServiceEndpointInterface(), pcrefMetaData);
      }

      // Parse the handler elements
      iterator = MetaData.getChildrenByTagName(element, "handler");
      while (iterator.hasNext())
      {
         Element handlerElement = (Element)iterator.next();
         HandlerMetaData handlerMetaData = new HandlerMetaData();
         handlerMetaData.importStandardXml(handlerElement);
         handlers.add(handlerMetaData);
      }
   }

   /** Parse jboss specific service-ref child elements
    * @param element
    * @throws DeploymentException
    */
   public void importJBossXml(Element element) throws DeploymentException
   {
      configName = MetaData.getOptionalChildContent(element, "config-name");
      
      configFile = MetaData.getOptionalChildContent(element, "config-file");
      String wsdlOverrideOption = MetaData.getOptionalChildContent(element, "wsdl-override");
      try
      {
         if (wsdlOverrideOption != null)
            wsdlOverride = new URL(wsdlOverrideOption);
      }
      catch (MalformedURLException e)
      {
         throw new DeploymentException("Invalid WSDL override: " + wsdlOverrideOption);
      }

      // Parse the port-component-ref elements
      Iterator iterator = MetaData.getChildrenByTagName(element, "port-component-ref");
      while (iterator.hasNext())
      {
         Element pcrefElement = (Element)iterator.next();
         String name = MetaData.getOptionalChildContent(pcrefElement, "service-endpoint-interface");
         if (name != null)
         {
            PortComponentRefMetaData pcrefMetaData = (PortComponentRefMetaData)portComponentRefs.get(name);
            if (pcrefMetaData == null)
            {
               // Its ok to only have the <port-component-ref> in jboss.xml and not in ejb-jar.xml 
               pcrefMetaData = new PortComponentRefMetaData(this);
               pcrefMetaData.importStandardXml(pcrefElement);
               portComponentRefs.put(pcrefMetaData.getServiceEndpointInterface(), pcrefMetaData);
            }

            pcrefMetaData.importJBossXml(pcrefElement);
         }
      }

      // Parse the call-property elements
      iterator = MetaData.getChildrenByTagName(element, "call-property");
      while (iterator.hasNext())
      {
         Element propElement = (Element)iterator.next();
         String name = MetaData.getUniqueChildContent(propElement, "prop-name");
         String value = MetaData.getUniqueChildContent(propElement, "prop-value");
         if (callProperties == null)
            callProperties = new Properties();
         callProperties.setProperty(name, value);
      }
   }
}
