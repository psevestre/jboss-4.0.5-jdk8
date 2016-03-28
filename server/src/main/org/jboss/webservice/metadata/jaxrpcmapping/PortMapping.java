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
package org.jboss.webservice.metadata.jaxrpcmapping;

import java.io.Serializable;

// $Id: PortMapping.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

/**
 * XML mapping of the java-wsdl-mapping/service-interface-mapping/port-mapping element.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 14-May-2004
 */
public class PortMapping implements Serializable
{
   private static final long serialVersionUID = 8229257516720800393L;

   // The parent <service-interface-mapping> element
   private ServiceInterfaceMapping serviceInterfaceMapping;

   // The required <port-name> element
   private String portName;
   // The required <java-port-name> element
   private String javaPortName;

   public PortMapping(ServiceInterfaceMapping serviceInterfaceMapping)
   {
      this.serviceInterfaceMapping = serviceInterfaceMapping;
   }

   public ServiceInterfaceMapping getServiceInterfaceMapping()
   {
      return serviceInterfaceMapping;
   }

   public String getJavaPortName()
   {
      return javaPortName;
   }

   public void setJavaPortName(String javaPortName)
   {
      this.javaPortName = javaPortName;
   }

   public String getPortName()
   {
      return portName;
   }

   public void setPortName(String portName)
   {
      this.portName = portName;
   }
   
   public String serialize()
   {
      StringBuffer sb = new StringBuffer();
      sb.append("<port-mapping><port-name>").append(portName).append("</port-name><java-port-name>");
      sb.append(javaPortName).append("</java-port-name></port-mapping>");
      return sb.toString(); 
   }
}

