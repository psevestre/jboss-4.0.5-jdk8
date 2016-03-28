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
package org.jboss.test.webservice.samples;

import org.jboss.logging.Logger;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

/**
 * An example of a java service endpoint.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class OrganizationJSEEndpoint implements Organization, ServiceLifecycle
{
   // provide logging
   private static final Logger log = Logger.getLogger(OrganizationJSEEndpoint.class);

   private ServletEndpointContext ctx;

   public String getContactInfo(String organization)
   {
      log.info("getContactInfo: " + organization);
      return "The '" + organization + "' boss is currently out of office, please call again.";
   }

   /** Used for initialization of a service endpoint.
    */
   public void init(Object context) throws ServiceException
   {
      ctx = (ServletEndpointContext)context;
      log.info("init: " + ctx);
   }

   /** JAX-RPC runtime system ends the lifecycle of a service endpoint instance by
    * invoking the destroy method. 
    */
   public void destroy()
   {
      log.info("destroy: " + ctx);
   }
}
