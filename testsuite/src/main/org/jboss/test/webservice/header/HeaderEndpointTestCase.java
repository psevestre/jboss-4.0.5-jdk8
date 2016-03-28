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
package org.jboss.test.webservice.header;

import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.util.id.UID;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

/**
 * A collection of header tests.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Nov-2004
 */
public class HeaderEndpointTestCase extends WebserviceTestBase
{
   public HeaderEndpointTestCase(String name)
   {
      super(name);
   }

   /** Deploy the test ear */
   public static Test suite() throws Exception
   {
      return getDeploySetup(HeaderEndpointTestCase.class, "ws4ee-header.war, ws4ee-header-client.jar");
   }
   
   /** Send a message with IN header.
    * The service endpoint interface sees the header.
    */
   public void testSimpleHeaderEndpoint() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/SimpleHeaderService");
      SimpleHeaderEndpoint port = (SimpleHeaderEndpoint)service.getPort(SimpleHeaderEndpoint.class);

      boolean result = port.doStuff("Hello World!", "kermit");
      assertTrue(result);
   }

   /** Send a message with complex IN header.
    * The service endpoint interface sees the header.
    */
   public void testBeanHeaderEndpoint() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/BeanHeaderService");
      BeanHeaderEndpoint port = (BeanHeaderEndpoint)service.getPort(BeanHeaderEndpoint.class);

      SessionHeader header = new SessionHeader();
      header.setUsername("kermit");
      header.setSessionID(UID.asString());

      boolean result = port.doStuff("Hello World!", header);
      assertTrue(result);
   }

   /** Send a message with INOUT headers. The headers are processed by handlers.
    * The service endpoint interface does not see the header.
    */
   public void testImplicitHeaderEndpoint() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/ImplicitHeaderService");
      ImplicitHeaderEndpoint port = (ImplicitHeaderEndpoint)service.getPort(ImplicitHeaderEndpoint.class);

      boolean result = port.doStuff("Hello World!");
      assertTrue(result);

      // Do a second call to the endpoint, this time with sessionID
      result = port.doStuff("Hello World!");
      assertTrue(result);
   }

   /** Send a message with INOUT headers.
    * The service endpoint interface sees the header.
    */
   public void testExplicitHeaderEndpoint() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/ExplicitHeaderService");
      ExplicitHeaderEndpoint port = (ExplicitHeaderEndpoint)service.getPort(ExplicitHeaderEndpoint.class);

      SessionHeader header = new SessionHeader();
      header.setUsername("kermit");
      SessionHeaderHolder holder = new SessionHeaderHolder();
      holder.value = header;

      boolean result = port.doStuff("Hello World!", holder);
      assertTrue(result);
      assertNotNull(holder.value);

      header = holder.value;
      assertEquals("kermit", header.getUsername());
      assertNotNull(header.getSessionID());

      // Do a second call to the endpoint, this time with sessionID
      result = port.doStuff("Hello World!", holder);
      assertTrue(result);
      assertNotNull(holder.value);

      header = holder.value;
      assertEquals("kermit", header.getUsername());
      assertNotNull(header.getSessionID());
   }
}
