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
package org.jboss.test.webservice;

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.JBossTestCase;

/** Common functionality for web services test cases.
 *
 * @author Thomas.Diesler@jboss.org
 * @version $Revision: 57211 $
 */
public class WebserviceTestBase extends JBossTestCase
{

   public WebserviceTestBase(String name)
   {
      super(name);
   }

   /**
    * Get the client's env context, see tracker [840598] for details
    */
   protected InitialContext getClientContext(String clientName) throws NamingException
   {
      InitialContext initialContext = new InitialContext();
      Hashtable jndiEnv = initialContext.getEnvironment();
      jndiEnv.put("java.naming.factory.url.pkgs", "org.jboss.naming.client");
      jndiEnv.put("j2ee.clientName", clientName);
      log.debug("jndiEnv: " + jndiEnv);
      return new InitialContext(jndiEnv);
   }

   /**
    * Get the client's env context, see tracker [840598] for details
    */
   protected InitialContext getClientContext() throws NamingException
   {
      return getClientContext("ws4ee-client");
   }

   /** Return true if the Axis based stack is available
    */
   public static boolean isWS4EEAvailable()
   {
      try
      {
         ServiceFactory factory = ServiceFactory.newInstance();
         if ("org.jboss.webservice.client.ServiceFactoryImpl".equals(factory.getClass().getName()))
            return true;
      }
      catch (ServiceException e)
      {
         // ignore
      }
      return false;
   }

   /** Return true if the JBossWS stack is available
    */
   public static boolean isJBossWSAvailable()
   {
      try
      {
         ServiceFactory factory = ServiceFactory.newInstance();
         if ("org.jboss.ws.jaxrpc.ServiceFactoryImpl".equals(factory.getClass().getName()))
            return true;
      }
      catch (ServiceException e)
      {
         // ignore
      }
      return false;
   }

   public static Test getDeploySetupForWS4EE(final Class clazz, String jarName) throws Exception
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(clazz));
      if (isWS4EEAvailable() == false)
      {
         jarName = null;
      }
      return getDeploySetup(suite, jarName);
   }

   public static Test getDeploySetupForJBossWS(final Class clazz, String jarName) throws Exception
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(clazz));
      if (isJBossWSAvailable() == false)
      {
         jarName = null;
      }
      return getDeploySetup(suite, jarName);
   }
}
