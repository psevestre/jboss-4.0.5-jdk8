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
package org.jboss.jaxr.juddi;

import javax.management.ObjectName;
import org.jboss.mx.util.ObjectNameFactory;

/**
 * MBean interface.
 * @since Nov 8, 2004
 */
public interface JUDDIServiceMBean extends org.jboss.system.ServiceMBean {

   //default object name
   public static final ObjectName OBJECT_NAME = ObjectNameFactory.create("jboss:service=juddi");

  boolean isDropOnStart() ;

   /**
    * Sets the dropOnStart.
    * @param dropOnStart The dropOnStart to set
    */
  void setDropOnStart(boolean dropOnStart) ;

  boolean isDropOnStop() ;

   /**
    * Sets the dropOnStop.
    * @param dropOnStop The dropOnStop to set
    */
  void setDropOnStop(boolean dropOnStop) ;

  boolean isCreateOnStart() ;

   /**
    * Sets the createOnStart.
    * @param createOnStart The createOnStart to set
    */
  void setCreateOnStart(boolean createOnStart) ;

  java.lang.String getDataSource() ;

   /**
    * Sets the Datasource Url.
    * @param ds The datasourceurl to set
    */
  void setDataSourceUrl(java.lang.String ds) ;

  java.lang.String getRegistryOperator() ;

   /**
    * Sets the RegistryOperator.
    * @param ro The datasourceurl to set
    */
  void setRegistryOperator(java.lang.String ro) ;

   /**
    * gets the JAXR ConnectionFactory.
    */
  java.lang.String getBindJaxr() ;

   /**
    * Sets the JAXR ConnectionFactory.
    * @param str The context to bind the Jaxr factory to set
    */
  void setBindJaxr(java.lang.String str) ;

   /**
    * gets the JAXR ConnectionFactory.
    */
  boolean getShouldBindJaxr() ;

   /**
    * Sets the JAXR ConnectionFactory.
    * @param str Should a Jaxr Connection Factory bound
    */
  void setShouldBindJaxr(boolean str) ;

   /**
    * gets the JAXR ConnectionFactory.
    */
  boolean getDropDB() ;

   /**
    * Sets the JAXR ConnectionFactory.
    * @param b Should a Jaxr Connection Factory bound
    */
  void setDropDB(boolean b) ;

}

