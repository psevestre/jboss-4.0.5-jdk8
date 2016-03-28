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
package org.jboss.test.webservice.jbws153;

import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

/** Test nested xsd imports
 *
 *  http://jira.jboss.com/jira/browse/JBWS-153
 *
 * @author Thomas.Diesler@jboss.org
 * @since 22-Mar-2005
 */
public class JBWS153TestCase extends WebserviceTestBase
{
   /** Construct the test case with a given name
    */
   public JBWS153TestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS153TestCase.class, "ws4ee-jbws153.war, ws4ee-jbws153-client.jar");
   }

   /**
    * Test JSE endpoint
    */
   public void testEndpoint() throws Exception
   {
      InitialContext iniContext = getClientContext();
      Service service = (Service)iniContext.lookup("java:comp/env/service/OrderService");
      Order port = (Order)service.getPort(Order.class);

      String getMessage = "Ferrari";
      String retStr = port.getOrder(getMessage);
      assertEquals(getMessage, retStr);
   }
}
