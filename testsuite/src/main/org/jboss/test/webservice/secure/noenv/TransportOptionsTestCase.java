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
package org.jboss.test.webservice.secure.noenv;

import java.io.File;

import javax.naming.Context;
import javax.xml.rpc.Service;
import javax.xml.rpc.Stub;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.JBossTestCase;
import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.test.webservice.ws4eesimple.HelloWs;

/**
 * Tests SSL transport options
 *
 * @author <a href="mailto:jason.greene@jboss.com">Jason T. Greene</a>
 */
public class TransportOptionsTestCase extends WebserviceTestBase
{
   public TransportOptionsTestCase(String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      // JBAS-3610, the execution order of tests in this test case is important
      // so it must be defined explicitly when running under some JVMs
      TestSuite suite = new TestSuite();
      suite.addTest(new TransportOptionsTestCase("testWithTransportOptions"));
      suite.addTest(new TransportOptionsTestCase("testWithoutKeystore"));
      
      return JBossTestCase.getDeploySetup(suite, "ws4ee-simple-secure.war, ws4ee-simple-secure-client.jar");  
   }


   public void testWithTransportOptions() throws Exception
   {
      Context envCtx = getClientContext();
      Service service = (Service)envCtx.lookup("java:comp/env/service/HelloWsSecureService");
      HelloWs port = (HelloWs)service.getPort(HelloWs.class);

      String keyStore = "resources/test-configs/webservice-ssl/conf/client.keystore";
      assertTrue("Keystore exists", new File(keyStore).exists());

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

      res = port.sayHello("Hello2");
      assertEquals("'Hello2' to you too!", res);
   }

   public void testWithoutKeystore() throws Exception
   {
      Context envCtx = getClientContext();
      Service service = (Service)envCtx.lookup("java:comp/env/service/HelloWsSecureService");
      HelloWs port = (HelloWs)service.getPort(HelloWs.class);

      String keyStore = "resources/test-configs/webservice-ssl/conf/client.keystore";
      assertTrue("Keystore exists", new File(keyStore).exists());

      Stub stub = (Stub)port;
      if (isWS4EEAvailable())
      {
         stub._setProperty("org.jboss.webservice.trustStore", keyStore);
         stub._setProperty("org.jboss.webservice.trustStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.webservice.trustStoreType", "JKS");
      }
      if (isJBossWSAvailable())
      {
         stub._setProperty("org.jboss.ws.trustStore", keyStore);
         stub._setProperty("org.jboss.ws.trustStorePassword", "unit-tests-client");
         stub._setProperty("org.jboss.ws.trustStoreType", "JKS");
      }

      try
      {
         String res = port.sayHello("Hello");
         System.out.println("FIXME: JBWS-777");
         //fail("Expected security exception");
      }
      catch (Exception e)
      {
         //ignore
      }
   }
}
