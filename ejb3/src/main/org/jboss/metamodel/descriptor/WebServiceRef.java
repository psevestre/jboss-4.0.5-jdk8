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
package org.jboss.metamodel.descriptor;

import org.jboss.logging.Logger;

import org.jboss.metamodel.descriptor.Ref;

/**
 * Represents a <resource-ref> element of the ejb-jar.xml deployment descriptor for the
 * 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 45249 $</tt>
 */
public class WebServiceRef extends Ref
{
   private static final Logger log = Logger.getLogger(WebServiceRef.class);
   
   private String serviceRefName;

   private String resType;

   private String serviceInterface;

   private String wsdlFile;

   private String mappedName;
   
   private String jndiName;
   
   private String jaxRpcMappingFile;
   
   public String getJaxRpcMappingFile()
   {
      return jaxRpcMappingFile;
   }

   public void setJaxRpcMappingFile(String jaxRpcMappingFile)
   {
      this.jaxRpcMappingFile = jaxRpcMappingFile;
   }
   
   public String getJndiName()
   {
      return jndiName;
   }

   public void setJndiName(String jndiName)
   {
      this.jndiName = jndiName;
   }

   public String getMappedName()
   {
      return mappedName;
   }

   public void setMappedName(String mappedName)
   {
      this.mappedName = mappedName;
   }

   public String getServiceRefName()
   {
      return serviceRefName;
   }

   public void setServiceRefName(String serviceRefName)
   {
      this.serviceRefName = serviceRefName;
   }

   public String getResType()
   {
      return resType;
   }

   public void setResType(String resType)
   {
      this.resType = resType;
   }

   public String getServiceInterface()
   {
      return serviceInterface;
   }

   public void setServiceInterface(String serviceInterface)
   {
      this.serviceInterface = serviceInterface;
   }

   public String getWsdlFile()
   {
      return wsdlFile;
   }

   public void setWsdlFile(String wsdlFile)
   {
      this.wsdlFile = wsdlFile;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[" + this.getClass().getName() + ": ");
      sb.append("serviceRefName=").append(serviceRefName);
      sb.append(", jndiName=").append(jndiName);
      sb.append(", jaxRpcMappingFile=").append(jaxRpcMappingFile);
      sb.append(", resType=").append(resType);
      sb.append(", mappedName=").append(mappedName);
      sb.append("]");
      return sb.toString();
   }
}
