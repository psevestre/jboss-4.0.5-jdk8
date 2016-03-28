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
package org.jboss.test.webservice.secure;

import java.io.File;

import javax.naming.Context;
import javax.xml.rpc.Service;
import javax.xml.rpc.Stub;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.test.webservice.ws4eesimple.HelloWs;

/**
 * Tests of the ws4ee functionality for a simple Hello EJB.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 06-May-2004
 */
public class SimpleClientSecureTestCase extends WebserviceTestBase
{

   /**
    * Construct the test case with a given name
    */
   public SimpleClientSecureTestCase(String name)
   {
      super(name);
   }

   /**
    * deploy the test archives
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(SimpleClientSecureTestCase.class, "ws4ee-simple-secure.war, ws4ee-simple-secure-client.jar");
   }

   /**
    * Test client application access
    */
   public void testWithTransportOptions() throws Exception
   {
      Context envCtx = getClientContext();
      Service service = (Service)envCtx.lookup("java:comp/env/service/HelloWsSecureService");
      HelloWs port = (HelloWs)service.getPort(HelloWs.class);

      String keyStore = "resources/test-configs/webservice-ssl/conf/client.keystore";
      assertTrue("Cannot find keystore", new File(keyStore).exists());

      Stub stub = (Stub)port;
      if (isWS4EEAvailable())
      {
         stub._setProperty("org.jboss.webservice.keyStore", keyStore);
         stub._setProperty("org.jboss.webservice.keyStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.webservice.keyStoreType", "JKS");
         stub._setProperty("org.jboss.webservice.trustStore", keyStore);
         stub._setProperty("org.jboss.webservice.trustStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.webservice.trustStoreType", "JKS");
      }
      if (isJBossWSAvailable())
      {
         stub._setProperty("org.jboss.ws.keyStore", keyStore);
         stub._setProperty("org.jboss.ws.keyStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.ws.keyStoreType", "JKS");
         stub._setProperty("org.jboss.ws.trustStore", keyStore);
         stub._setProperty("org.jboss.ws.trustStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.ws.trustStoreType", "JKS");
      }

      String res = port.sayHello("Hello");
      assertEquals("'Hello' to you too!", res);
   }

   public void testWithoutTransportOptions() throws Exception
   {
      Context envCtx = getClientContext();
      Service service = (Service)envCtx.lookup("java:comp/env/service/HelloWsSecureService");
      HelloWs port = (HelloWs)service.getPort(HelloWs.class);

      String res = port.sayHello("Hello");
      assertEquals("'Hello' to you too!", res);
   }
}
