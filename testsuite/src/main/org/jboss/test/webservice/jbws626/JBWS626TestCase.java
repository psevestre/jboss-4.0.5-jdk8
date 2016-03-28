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
package org.jboss.test.webservice.jbws626;

import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

/** 
 * No serializer found exception for method returning an array of objects
 *
 * http://jira.jboss.com/jira/browse/JBWS-626
 *
 * @author Thomas.Diesler@jboss.org
 * @since 03-Jan-2006
 */
public class JBWS626TestCase extends WebserviceTestBase
{
   private static DemoEndpoint port;

   public JBWS626TestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS626TestCase.class, "ws4ee-jbws626.ear");
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service) iniCtx.lookup("java:comp/env/service/TestService");
         port = (DemoEndpoint) service.getPort(DemoEndpoint.class);
      }
   }

   public void testGetArray() throws Exception
   {
      ValueObj[] expArr = new ValueObj[]
      {new ValueObj("a", "b"), new ValueObj("c", "d")};
      ValueObj[] retArr = port.getArray();
      assertEquals(expArr[0], retArr[0]);
      assertEquals(expArr[1], retArr[1]);
   }

   public void testEmptyArray() throws Exception
   {
      ValueObj[] retArr = port.getEmptyArray();
      assertEquals(0, retArr.length);
   }

   public void testNullArray() throws Exception
   {
      ValueObj[] retArr = port.getNullArray();
      assertNull("Expected null return", retArr);
   }

   public void testEchoArray() throws Exception
   {
      ValueObj[] expArr = new ValueObj[]
      {new ValueObj("a", "b"), new ValueObj("c", "d")};
      ValueObj[] retArr = port.echoArray(expArr);
      assertEquals(expArr[0], retArr[0]);
      assertEquals(expArr[1], retArr[1]);
   }
}
