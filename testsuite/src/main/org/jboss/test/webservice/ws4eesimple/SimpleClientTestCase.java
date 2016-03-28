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
import javax.naming.InitialContext;
import javax.xml.rpc.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Tests of the ws4ee functionality for a simple Hello EJB.
 *
 * @author Thomas.Diesler@jboss.org
 * @author Christoph.Jung@infor.de
 * @since 06-May-2004
 */
public class SimpleClientTestCase extends WebserviceTestBase
{

   /**
    * Construct the test case with a given name
    */
   public SimpleClientTestCase(String name)
   {
      super(name);
   }

   /**
    * deploy the test archives
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(SimpleClientTestCase.class, "ws4ee-simple.jar, ws4ee-simple-client.ear");
   }

   /**
    * Test client application access
    */
   public void testApplClient() throws Exception
   {
      Context envCtx = getClientContext();
      Service service = (Service)envCtx.lookup("java:comp/env/service/HelloService");
      HelloWs ws = (HelloWs)service.getPort(HelloWs.class);
      String res = ws.sayHello("Hello");
      assertEquals("'Hello' to you too!", res);
   }

   /**
    * Test servlet client access
    */
   public void testWebClient() throws Exception
   {
      URL url = new URL("http://" + getServerHost() + ":8080/ws4ee-simple-client/HelloWsClientServlet?input=Hello");
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      String res = br.readLine();
      br.close();

      assertEquals("'Hello' to you too!", res);
   }

   /**
    * Test EJB client access
    */
   public void testEJBClient() throws Exception
   {
      // test direct EJB access
      InitialContext iniCtx = getClientContext();
      HelloHome home = (HelloHome)iniCtx.lookup("java:comp/env/HelloClientEjb");
      Hello ejb = home.create();

      String output = ejb.sayHello("Hello");
      assertEquals("'Hello' to you too!", output);

      // test webservice access
      Context envCtx = getClientContext();
      Service svc = (Service)envCtx.lookup("java:comp/env/service/HelloService");

      HelloWs sei = (HelloWs)svc.getPort(HelloWs.class);
      output = sei.sayHello("Hello");
      assertEquals("'Hello' to you too!", output);
   }
}
