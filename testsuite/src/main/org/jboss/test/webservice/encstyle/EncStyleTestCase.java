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
package org.jboss.test.webservice.encstyle;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import org.jboss.test.webservice.WebserviceTestBase;

/**
 * Test [ 1041408 ] WebService clients don't work with document style wsdl
 *
 * @author Thomas.Diesler@jboss.org
 * @since 11-Nov-2004
 */
public class EncStyleTestCase extends WebserviceTestBase
{
   public EncStyleTestCase(String name)
   {
      super(name);
   }

   /** Test document style */
   public void testDocStyle() throws Exception
   {
      deploy("ws4ee-encstyle-doc.war");
      deploy("ws4ee-encstyle-doc-client.jar");
      try
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/SampleService");
         SampleEndpointDOC port = (SampleEndpointDOC)service.getPort(SampleEndpointDOC.class);

         UserType user = new UserType();
         user.setFirstName("John");
         user.setLastName("Smith");
         SampleEndpoint_changeSalary_RequestStruct req = new SampleEndpoint_changeSalary_RequestStruct(user, new Integer(5000));
         SampleEndpoint_changeSalary_ResponseStruct res = port.changeSalary(req);
         assertEquals("Hello John Smith! Your salary is: 5000", res.result);
      }
      finally
      {
         undeploy("ws4ee-encstyle-doc-client.jar");
         undeploy("ws4ee-encstyle-doc.war");
      }
   }

   /** Test rpc style */
   public void testRpcStyle() throws Exception
   {
      deploy("ws4ee-encstyle-rpc.war");
      deploy("ws4ee-encstyle-rpc-client.jar");
      try
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/SampleService");
         SampleEndpoint port = (SampleEndpoint)service.getPort(SampleEndpoint.class);

         UserType user = new UserType();
         user.setFirstName("John");
         user.setLastName("Smith");
         String retStr = port.changeSalary(user, new Integer(5000));
         assertEquals("Hello John Smith! Your salary is: 5000", retStr);
      }
      finally
      {
         undeploy("ws4ee-encstyle-rpc-client.jar");
         undeploy("ws4ee-encstyle-rpc.war");
      }
   }
}
