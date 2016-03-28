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
package org.jboss.ejb3.test.simplecluster.unit;

import java.util.Properties;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import org.jboss.ejb3.test.simplecluster.Session;
import org.jboss.ejb3.test.simplecluster.StatefulRemote;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.test.JBossClusteredTestCase;
import org.jboss.test.JBossTestClusteredSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Sample client for the jboss container.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @deprecated Moved to clustersession package now.
 * @version $Id: SimpleClusterUnitTestCase.java 57207 2006-09-26 12:06:13Z dimitris@jboss.org $
 */

public class SimpleClusterUnitTestCase
extends JBossClusteredTestCase
{
   org.apache.log4j.Category log = getLog();

   static String stoppedAddress;
   static Properties prop = new Properties();


   public SimpleClusterUnitTestCase(String name)
   {
      super(name);

      prop.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      prop.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
   }

   protected static void sleepThread(long msecs)
   {
      try {
         Thread.sleep(msecs);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }

   public void testDummy()
   {

   }

   public void XtestAll() throws Exception
   {
      System.out.println("Starting test" + new java.util.Date());
      String node0 = System.getProperty("jbosstest.cluster.node0");
      String node1 = System.getProperty("jbosstest.cluster.node1");

      prop.put("java.naming.provider.url", "jnp://" + node0 + ":1099");

      InitialContext ctx = new InitialContext(prop);

      StatefulRemote stateful = (StatefulRemote) ctx.lookup("testStateful/remote");
      Session stateless = (Session)ctx.lookup("SessionBean/remote");

      int last = 0;
      for (int i = 0 ; i < 20 ; i++)
      {
         stateless.test();
         int current = stateful.increment();
         System.out.println("Got value: " + current);
         assertEquals("Wrong return value", current, last + 1);
         last = current;
         sleepThread(500);

         if (i == 10)
         {
            takeDownActiveInstance(stateful, prop);
            sleepThread(2000);
         }

      }
   }

   protected void takeDownActiveInstance(StatefulRemote stateful, Properties prop) throws Exception
   {
      String address = stateful.getHostAddress();
      stopJBossInstance(address);
      stoppedAddress = address;
   }

   protected static void stopJBossInstance(String address)throws Exception
   {
      prop.put("java.naming.provider.url", "jnp://" + address + ":1099");
      InitialContext ctx = new InitialContext(prop);
      RMIAdaptor server = (RMIAdaptor)ctx.lookup("jmx/rmi/RMIAdaptor");
      server.invoke(new ObjectName("jboss.system:type=Server"), "shutdown", new Object[0], new String[0]);
      
      sleepThread(10000);      
   }
   
   public static Test suite() throws Exception
   {
      Class clazz = SimpleClusterUnitTestCase.class;
      final String jarName = "simplecluster-test.jar";
      TestSuite suite = new TestSuite();
      
      //Set up tests manually to make sure testServerFound happens first
      //tearDown after this test will stop both servers causing testServerNotFound
      //to fail if it happens last
//      suite.addTest(new SimpleClusterUnitTestCase("testServerFound"));
      suite.addTest(new SimpleClusterUnitTestCase("testAll"));
      
      JBossTestClusteredSetup wrapper = new JBossTestClusteredSetup(suite)
      {
         protected void setUp() throws Exception
         {
            if (jarName == null) return;
            deploymentException = null;
            try
            {
               this.deploy(jarName);
               this.getLog().debug("deployed package: " + jarName);
            }
            catch (Exception ex)
            {
               // Throw this in testServerFound() instead.
               deploymentException = ex;
            }
                
            // wait a few seconds so that the cluster stabilize
            synchronized (this)
            {
               wait(2000);
            }
         }

         protected void tearDown() throws Exception
         {
            String node0 = System.getProperty("jbosstest.cluster.node0");
            String node1 = System.getProperty("jbosstest.cluster.node1");
            if (node0.equals(stoppedAddress))
            {
               stopJBossInstance(node1);
            }
            else if (node1.equals(stoppedAddress))
            {
               stopJBossInstance(node0);
            }
         }
      };
      
      return wrapper;
   }
}
