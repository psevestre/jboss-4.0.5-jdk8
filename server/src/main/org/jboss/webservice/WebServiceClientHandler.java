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
package org.jboss.webservice;

// $Id: WebServiceClientHandler.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.mx.util.ObjectNameFactory;

/**
 * Abstract differences in JBoss-WS4EE and JBossWS client deployment 
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 05-May-2004
 */
public class WebServiceClientHandler
{
   // provide logging
   private static final Logger log = Logger.getLogger(WebServiceClientHandler.class);

   /**
    * This binds a jaxrpc Service into the callers ENC for every service-ref element
    *
    * @param envCtx      ENC to bind the javax.rpc.xml.Service object to
    * @param serviceRefs An iterator of the service-ref elements in the client deployment descriptor
    * @param di          The client's deployment info
    * @throws org.jboss.deployment.DeploymentException if it goes wrong
    */
   public static void setupServiceRefEnvironment(Context envCtx, Iterator serviceRefs, DeploymentInfo di) throws DeploymentException
   {
      // nothing to do
      if (serviceRefs.hasNext() == false)
         return;

      MBeanServer server = MBeanServerLocator.locateJBoss();
      ObjectName ws4eeObjectName = ObjectNameFactory.create("jboss.ws4ee:service=ServiceClientDeployer");
      ObjectName jbosswsObjectName = ObjectNameFactory.create("jboss.ws:service=WebServiceClientDeployer");

      ObjectName objectName = null;
      WebServiceClientDeployment wsClientDeployment;
      try
      {
         if (server.isRegistered(ws4eeObjectName))
         {
            objectName = ws4eeObjectName;
            wsClientDeployment = (WebServiceClientDeployment)MBeanProxyExt.create(WebServiceClientDeployment.class, ws4eeObjectName, server);
         }
         else if (server.isRegistered(jbosswsObjectName))
         {
            objectName = jbosswsObjectName;
            wsClientDeployment = (WebServiceClientDeployment)MBeanProxyExt.create(WebServiceClientDeployment.class, jbosswsObjectName, server);
         }
         else
         {
            log.warn("No web service client deployer registered");
            return;
         }
      }
      catch (Exception e)
      {
         throw new DeploymentException("Cannot create proxy to the web service client deployer: " + objectName, e);
      }

      // Delegate to the web service client deloyer
      wsClientDeployment.setupServiceRefEnvironment(envCtx, serviceRefs, di);
   }
}
