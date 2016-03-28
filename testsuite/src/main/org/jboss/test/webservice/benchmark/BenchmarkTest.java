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
package org.jboss.test.webservice.benchmark;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import org.jboss.test.webservice.WebserviceTestBase;

/**
 * Test Benchmark EJB Service
 *
 * @author anders.hedstrom@home.se
 * @since 9-Nov-2005
 */
public abstract class BenchmarkTest extends WebserviceTestBase
{
   private int runcount = 10;
   
   private static BenchmarkService port;
   private static long beginTime = 0;

   public BenchmarkTest(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/Benchmark");
         port = (BenchmarkService)service.getPort(BenchmarkService.class);
         beginTime = System.currentTimeMillis();
      }
   }

   public void testEchoSimpleType() throws Exception
   {
      SimpleUserType in = new SimpleUserType(1, 1.0f, "test");
      
      for (int rc = 0; rc < runcount; rc++)
      {
         SimpleUserType out = port.echoSimpleType(in);
         assertEquals(in, out);
      }      
   }

   public void testEchoArrayOfSimpleUserType() throws Exception
   {
      int count = 10;
      SimpleUserType[] inArr = new SimpleUserType[count];
      for (int i=0; i < count; i++)
      {
         inArr[i] = new SimpleUserType(i, (float)(i + 0.5), "test-" + i);
      }
      
      System.out.println("FIXME: enable array tests");
      /*
      for (int rc = 0; rc < runcount; rc++)
      {
         SimpleUserType[] outArr = port.echoArrayOfSimpleUserType(inArr);
         for (int i = 0; i < count; i++)
         {
            assertEquals(inArr[i], outArr[i]);
         }
      }
      */      
   }

   public void testEchoSynthetic() throws Exception
   {
      Synthetic in = new Synthetic("test", new SimpleUserType(1, 1.0f, "test"), "test".getBytes());
      for (int rc = 0; rc < runcount; rc++)
      {
         Synthetic out = port.echoSynthetic(in);
         assertEquals(in, out);
      }      
   }

   public void testGetOrder() throws Exception
   {
      int orderItems = 500;
      for (int rc = 0; rc < runcount; rc++)
      {
         Order order = port.getOrder(orderItems, 1);
         assertEquals(orderItems, order.getLineItems().length);
      }      
   }

   public void testTiming() throws Exception
   {
      long endTime = System.currentTimeMillis();
      System.out.println((endTime - beginTime) + "ms");
   }
}
