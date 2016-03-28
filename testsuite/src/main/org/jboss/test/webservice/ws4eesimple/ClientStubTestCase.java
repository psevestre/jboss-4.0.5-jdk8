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
package org.jboss.test.webservice.ws4eesimple;

import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.naming.Context;
import javax.xml.rpc.Service;
import javax.xml.rpc.Stub;

/**
 * Tests the JBossWS client stub
 *
 * @author Thomas.Diesler@jboss.org
 * @since 09-Nov-2004
 */
public class ClientStubTestCase extends WebserviceTestBase
{
   /**
    * Construct the test case with a given name
    */
   public ClientStubTestCase(String name)
   {
      super(name);
   }

   /**
    * deploy the test archives
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(ClientStubTestCase.class, "ws4ee-simple.jar, ws4ee-simple-client.ear");
   }

   /**
    * Test client application access
    */
   public void testClientStub() throws Exception
   {
      Context iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/HelloService");
      HelloWs port = (HelloWs)service.getPort(HelloWs.class);

      String res = port.sayHello("Hello");
      assertEquals("'Hello' to you too!", res);

      Stub stub = (Stub)port;
      stub._setProperty(Stub.USERNAME_PROPERTY, "kermit");
      assertEquals("kermit", stub._getProperty(Stub.USERNAME_PROPERTY));

      stub._setProperty(Stub.PASSWORD_PROPERTY, "thefrog");
      assertEquals("thefrog", stub._getProperty(Stub.PASSWORD_PROPERTY));

      Boolean sessionMaintain = (Boolean)stub._getProperty(Stub.SESSION_MAINTAIN_PROPERTY);
      assertTrue(sessionMaintain == null || sessionMaintain.booleanValue() == false);
      stub._setProperty(Stub.SESSION_MAINTAIN_PROPERTY, new Boolean(true));
      assertTrue(((Boolean)stub._getProperty(Stub.SESSION_MAINTAIN_PROPERTY)).booleanValue());

      assertEquals("http://" + getServerHost() + ":8080/ws4ee-simple/HelloEjb", stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY));
      stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://" + getServerHost() + ":8080/ws4ee-simple/DummyService");
      assertEquals("http://" + getServerHost() + ":8080/ws4ee-simple/DummyService", stub._getProperty(Stub.ENDPOINT_ADDRESS_PROPERTY));
   }
}
