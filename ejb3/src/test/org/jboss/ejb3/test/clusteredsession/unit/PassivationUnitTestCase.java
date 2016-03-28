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
package org.jboss.ejb3.test.clusteredsession.unit;

import org.jboss.test.JBossClusteredTestCase;
import org.jboss.ejb3.test.clusteredsession.StatefulRemote;
import org.jboss.ejb3.test.clusteredsession.NodeAnswer;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;

/**
 * Test SFSB for load-balancing and failover behaviour
 *
 * @author  Ben.Wang@jboss.org
 * @version $Revision: 57207 $
 */
public class PassivationUnitTestCase extends JBossClusteredTestCase
{
   static boolean deployed = false;
   public static int test = 0;
   static Date startDate = new Date();

   protected final String namingFactory =
   System.getProperty(Context.INITIAL_CONTEXT_FACTORY);

   protected final String providerURL =
   System.getProperty(Context.PROVIDER_URL);

   public PassivationUnitTestCase (String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      final String jarName = "clusteredsession-test.jar";
      Test t1 = JBossClusteredTestCase.getDeploySetup(PassivationUnitTestCase.class,
              jarName);
      return t1;
   }

   protected InitialContext getInitialContext(int node) throws Exception {
      // Connect to the server0 JNDI
      String[] urls = getNamingURLs();
      Properties env1 = new Properties();
      env1.setProperty(Context.INITIAL_CONTEXT_FACTORY,
         "org.jnp.interfaces.NamingContextFactory");
      env1.setProperty(Context.PROVIDER_URL, urls[node]);
      return new InitialContext(env1);
   }

   /**
    */
   public void testStatefulPassivation()
      throws Exception
   {
      log.info("+++ testStatefulPassivation");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext(0);

      getLog().debug("==================================");
      getLog().debug(++PassivationUnitTestCase.test +"- "
              +"Looking up testStateful...");
      StatefulRemote remote = (StatefulRemote) ctx.lookup("testStateful/remote");

      remote.reset();
      remote.setState("hello");
      sleep_(11000);
      assertEquals("hello", remote.getState());
      assertEquals(1, remote.getPrePassivate());
      assertEquals(1, remote.getPostActivate());

      sleep_(11000);
      remote.remove();

   }

   /** This is to test failover with passivation
    */
   public void XtestFailoverStatefulPassivation()
      throws Exception
   {
      log.info("+++ testFiloverStatefulPassivation");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext(0);

      getLog().debug("==================================");
      getLog().debug(++PassivationUnitTestCase.test +"- "
              +"Looking up testStateful...");
      StatefulRemote stateful = (StatefulRemote) ctx.lookup("testStateful/remote");
      stateful.reset();

      stateful.setName("The Code");
      NodeAnswer node1 = stateful.getNodeState ();
      getLog ().debug ("Node 1 ID: " +node1);

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());
      sleep_(10000);

      // Now we switch to the other node, simulating a failure on node 1
      //
      stateful.setUpFailover("once");
      NodeAnswer node2 = stateful.getNodeState ();
      assertNotNull("State node: ", node2);
      getLog ().debug ("Node 2 ID : " +node2);

      assertNotSame ("No failover has occured!", node1.nodeId, node2.nodeId);

      assertEquals ("Node 1: ", "The Code", node1.answer);
      assertEquals ("Node 2: ", "The Code", node2.answer);

      stateful.resetActivationCounter(); // This will activate the bean.
      sleep_(10000); // let it get passivated again.
      assertEquals("Counter: ", 3, stateful.increment());
      assertEquals("Counter: ", 4, stateful.increment());
      assertEquals(1, stateful.getPostActivate());
      assertEquals(1, stateful.getPrePassivate());

      stateful.remove();
   }

   protected void sleep_(long msec)
   {
      try {
         Thread.sleep(msec);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
