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

// $Id: InitParamMetaData.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import java.io.Serializable;

/**
 * XML Binding and ws4ee meta-data element for
 * <code>webservices/webservice-description/port-component/handler/init-param</code>
 *
 * @author Thomas.Diesler@jboss.org
 * @since 06-May-2004
 */
public class InitParamMetaData implements Serializable
{
   static final long serialVersionUID = 849652901282654531L;
   
   // The required <handler-name> element
   private String paramName;
   // The required <handler-class> element
   private String paramValue;

   public String getParamName()
   {
      return paramName;
   }

   public void setParamName(String paramName)
   {
      this.paramName = paramName;
   }

   public String getParamValue()
   {
      return paramValue;
   }

   public void setParamValue(String paramValue)
   {
      this.paramValue = paramValue;
   }

   public String toString()
   {
      return "[name=" + paramName + ",value=" + paramValue + "]";
   }
}
