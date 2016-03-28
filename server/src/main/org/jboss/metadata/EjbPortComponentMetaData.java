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
package org.jboss.metadata;

// $Id: EjbPortComponentMetaData.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import java.util.StringTokenizer;

import org.jboss.deployment.DeploymentException;
import org.w3c.dom.Element;

/** The metdata data for session/port-component element from jboss.xml
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57209 $
 */
public class EjbPortComponentMetaData
{
   private SessionMetaData sessionMetaData;

   private String portComponentName;
   private String portComponentURI;
   private String authMethod;
   private String transportGuarantee;
   
   public EjbPortComponentMetaData(SessionMetaData sessionMetaData)
   {
      this.sessionMetaData = sessionMetaData;
   }

   public String getPortComponentName()
   {
      return portComponentName;
   }

   public String getPortComponentURI()
   {
      return portComponentURI;
   }

   public String getURLPattern()
   {
      String pattern = "/*";
      if (portComponentURI != null)
      {
         return portComponentURI;
      }
      return pattern;
   }

   public String getAuthMethod()
   {
      return authMethod;
   }

   public String getTransportGuarantee()
   {
      return transportGuarantee;
   }

   public void importStandardXml(Element element)
      throws DeploymentException
   {
   }

   /** Parse the port-component contents
    * @param element
    * @throws DeploymentException
    */
   public void importJBossXml(Element element) throws DeploymentException
   {
      ApplicationMetaData appMetaData = sessionMetaData.getApplicationMetaData();
      String contextRoot = appMetaData.getWebServiceContextRoot();

      // port-component/port-component-name
      portComponentName = MetaData.getUniqueChildContent(element, "port-component-name");
      
      // port-component/port-component-uri?
      portComponentURI = MetaData.getOptionalChildContent(element, "port-component-uri");
      if (portComponentURI != null)
      {
         if (portComponentURI.charAt(0) != '/')
            portComponentURI = "/" + portComponentURI;

         if (contextRoot == null)
         {
            // The first token is the webservice context root
            StringTokenizer st = new StringTokenizer(portComponentURI, "/");
            if (st.countTokens() < 2)
               throw new DeploymentException("Expected at least two tokens <port-component-uri>");
   
            contextRoot = "/" + st.nextToken();
            String prevContextRoot = contextRoot;
            if (prevContextRoot != null && prevContextRoot.equals(contextRoot) == false)
               throw new DeploymentException("Invalid <port-component-uri>, expected to start with: " + prevContextRoot);
   
            appMetaData.setWebServiceContextRoot(contextRoot);
            portComponentURI = portComponentURI.substring(portComponentURI.indexOf('/', 1));
         }
         else if (portComponentURI.startsWith(contextRoot))
         {
            portComponentURI = portComponentURI.substring(contextRoot.length());
         }
      }
      else
      {
         portComponentURI = "/" + sessionMetaData.getEjbName();
         // The context root will be derived from deployment short name
      }
      
      // port-component/auth-method?,
      authMethod = MetaData.getOptionalChildContent(element, "auth-method");
      // port-component/transport-guarantee?
      transportGuarantee = MetaData.getOptionalChildContent(element, "transport-guarantee");

      // Deprecated in jboss-4.0.1
      if (MetaData.getOptionalChildContent(element, "port-uri") != null)
         throw new DeploymentException("Deprecated element <port-uri>, use <port-component-uri> instead");
   }
}
