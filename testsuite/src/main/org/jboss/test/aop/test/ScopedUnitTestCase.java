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
package org.jboss.test.aop.test;

import java.net.URL;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.JBossTestCase;
import org.jboss.test.util.web.HttpUtils;

/**
 * Sample client for the jboss container.
 *
 * @author <a href="mailto:bill@burkecentral.com">Kabir Khan</a>
 * @version $Id$
 */
public class ScopedUnitTestCase
        extends JBossTestCase
{
   org.apache.log4j.Category log = getLog();

   static boolean deployed = false;
   static int test = 0;
   static AOPClassLoaderHookTestSetup setup;

   public ScopedUnitTestCase(String name)
   {
      super(name);
   }

   public void testExpectedValues1() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester1");
      Integer iv = (Integer)server.getAttribute(testerName, "ExpectedInterceptorValue");
      Integer ia = (Integer)server.getAttribute(testerName, "ExpectedAspectValue");
      assertEquals(11, iv.intValue());
      assertEquals(21, ia.intValue());
   }
   
   public void testExpectedValues2() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester2");
      Integer iv = (Integer)server.getAttribute(testerName, "ExpectedInterceptorValue");
      Integer ia = (Integer)server.getAttribute(testerName, "ExpectedAspectValue");
      assertEquals(12, iv.intValue());
      assertEquals(22, ia.intValue());
   }
   
   public void testScoped1() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester1");
      Object[] params = {};
      String[] sig = {};
      server.invoke(testerName, "testScoped", params, sig);
   }

   public void testScoped2() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester2");
      Object[] params = {};
      String[] sig = {};
      server.invoke(testerName, "testScoped", params, sig);
   }
   
   
   public void testIntroduction1() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester1");
      Object[] params = {};
      String[] sig = {};
      server.invoke(testerName, "testIntroduction1", params, sig);
   }
   
   public void testIntroduction2() throws Exception
   {
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.aop:name=ScopedTester2");
      Object[] params = {};
      String[] sig = {};
      server.invoke(testerName, "testIntroduction2", params, sig);
   }

   public void testEar1() throws Exception
   {
      URL url = new URL(HttpUtils.getBaseURL() + "ear1/srv");
      HttpUtils.accessURL(url);      
   }

   public void testEar2() throws Exception
   {
      URL url = new URL(HttpUtils.getBaseURL() + "ear2/srv");
      HttpUtils.accessURL(url);
   }

   public static Test suite() throws Exception
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(ScopedUnitTestCase.class));

      setup = new AOPClassLoaderHookTestSetup(suite, "aop-scopedtest1.sar,aop-scopedtest2.sar,aop-scopedear1.ear,aop-scopedear2.ear");
      return setup;
   }

}
