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
package org.jboss.test.webservice.samples2;


import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

/**
 * Test access to a complex rpc/literal endpoint
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class OrderProcessRPCTestCase extends WebserviceTestBase
{

   public OrderProcessRPCTestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(OrderProcessRPCTestCase.class, "ws4ee-samples2-rpc.war, ws4ee-samples2-rpc-client.jar");
   }

   /** Test a valid access */
   public void testValidAccess() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/OrderProcess");
      OrderProcess endpoint = (OrderProcess)service.getPort(OrderProcess.class);

      Person p = new Person("Tom", 3);
      OrderItem i0 = new OrderItem("Ferrari", 1);
      OrderItem i1 = new OrderItem("Twix", 10);
      OrderItem i2 = new OrderItem("IceCream", 3);

      OrderResponse res = endpoint.processOrder(new OrderItem[]{i0, i1, i2}, p);
      assertEquals(3, res.getItems().length);
      assertEquals(i0, res.getItems()[0]);
      assertEquals(i1, res.getItems()[1]);
      assertEquals(i2, res.getItems()[2]);
      assertEquals("approved", res.getMessage());
   }

   /** Test a valid access */
   public void testNullPerson() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/OrderProcess");
      OrderProcess endpoint = (OrderProcess)service.getPort(OrderProcess.class);

      OrderItem i1 = new OrderItem("Ferrari", 1);
      OrderItem i2 = new OrderItem("Twix", 10);
      OrderItem i3 = new OrderItem("IceCream", 3);

      try
      {
         endpoint.processOrder(new OrderItem[]{i1, i2, i3}, null);
         fail("OrderException expected");
      }
      catch (OrderException e)
      {
         // ignore expected exception
      }
   }

   /** Test a valid access */
   public void testTooManyItems() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/OrderProcess");
      OrderProcess endpoint = (OrderProcess)service.getPort(OrderProcess.class);

      Person p = new Person("Tom", 3);
      OrderItem i1 = new OrderItem("Ferrari", 1);
      OrderItem i2 = new OrderItem("Twix", 10);
      OrderItem i3 = new OrderItem("IceCream", 3);
      OrderItem i4 = new OrderItem("GameBoy", 1);

      try
      {
         endpoint.processOrder(new OrderItem[]{i1, i2, i3, i4}, p);
         fail("OrderException expected");
      }
      catch (OrderException e)
      {
         // ignore expected exception
      }
   }
}
