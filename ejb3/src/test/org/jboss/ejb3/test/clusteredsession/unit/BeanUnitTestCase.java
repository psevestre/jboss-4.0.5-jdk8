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

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;
import org.jboss.ejb3.test.clusteredsession.StatefulRemote;
import org.jboss.ejb3.test.clusteredsession.NodeAnswer;

/**
 * Test SFSB for load-balancing and failover behaviour
 *
 * @author  Ben.Wang@jboss.org
 * @version $Revision: 57207 $
 */
public class BeanUnitTestCase extends JBossClusteredTestCase
{
   static boolean deployed = false;
   public static int test = 0;
   static Date startDate = new Date();

   protected final String namingFactory =
   System.getProperty(Context.INITIAL_CONTEXT_FACTORY);

   protected final String providerURL =
   System.getProperty(Context.PROVIDER_URL);

   public BeanUnitTestCase (String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      final String jarName = "clusteredsession-test.jar";
      Test t1 = JBossClusteredTestCase.getDeploySetup(BeanUnitTestCase.class,
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

   public void testBasic()
   throws Exception
   {
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "
              +"Trying the context...");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext(0);

      getLog().debug("Test Stateful Bean");
      getLog().debug("==================================");
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "
              +"Looking up testBasic...");
      StatefulRemote stateful = (StatefulRemote) ctx.lookup("testStateful/remote");

      stateful.setName("The Code");
      _sleep(300);

      NodeAnswer node1 = stateful.getNodeState ();
      getLog ().debug ("Node 1 ID: " +node1);

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());

      stateful.remove();
      getLog().debug("ok");
   }

   public void testStatefulBeanCounterFailover()
   throws Exception
   {
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "+"Trying the context...");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext(0);

      getLog().debug("Test Stateful Bean Failover");
      getLog().debug("==================================");
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "
              +"Looking up testStateful...");
      StatefulRemote stateful = (StatefulRemote) ctx.lookup("testStateful/remote");

      stateful.setName("The Code");
      NodeAnswer node1 = stateful.getNodeState ();
      getLog ().debug ("Node 1 ID: " +node1);

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());
      _sleep(300);

      // Now we switch to the other node, simulating a failure on node 1
      //
      stateful.setUpFailover("once");
      NodeAnswer node2 = stateful.getNodeState ();
      assertNotNull("State node: ", node2);
      getLog ().debug ("Node 2 ID : " +node2);

      assertNotSame ("No failover has occured!", node1.nodeId, node2.nodeId);

      assertEquals ("Node 1: ", "The Code", node1.answer);
      assertEquals ("Node 2: ", "The Code", node2.answer);

      assertEquals("Counter: ", 3, stateful.increment());
      assertEquals("Counter: ", 4, stateful.increment());

      stateful.remove();
      getLog().debug("ok");
   }

   public void testStatefulBeanFailover()
   throws Exception
   {
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "+"Trying the context...");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext(0);

      getLog().debug("Test Stateful Bean Failover");
      getLog().debug("==================================");
      getLog().debug(++org.jboss.ejb3.test.clusteredsession.unit.BeanUnitTestCase.test +"- "
              +"Looking up testStateful...");
      StatefulRemote stateful = (StatefulRemote) ctx.lookup("testStateful/remote");

      stateful.setName("Bupple-Dupple");
      _sleep(300);

      NodeAnswer node1 = stateful.getNodeState ();
      getLog ().debug ("Node 1 ID: " +node1);

      // Now we switch to the other node, simulating a failure on node 1
      //
      stateful.setUpFailover("once");
      NodeAnswer node2 = stateful.getNodeState ();
      assertNotNull("State node: ", node2);
      getLog ().debug ("Node 2 ID : " +node2);

      assertNotSame ("No failover has occured!", node1.nodeId, node2.nodeId);

      assertEquals ("Node 1: ", "Bupple-Dupple", node1.answer);
      assertEquals ("Node 2: ", "Bupple-Dupple", node2.answer);

      // we change our name to see if it replicates to node 1
      //
      stateful.setName ("Changed");
      _sleep(300);

      // now we travel back on node 1
      //
      stateful.setUpFailover("once");
      node1 = stateful.getNodeState ();
      assertNotNull("State node id should not be null: ", node1.nodeId);
      getLog ().debug (node1);

      _sleep(300);
      assertNotSame ("No failover has occured!", node1.nodeId, node2.nodeId);

      assertEquals ("Value is not identical on replicated node", "Changed", node1.answer );

      stateful.remove();
      getLog().debug("ok");
   }

   protected void _sleep(long time)
   {
      try {
         Thread.sleep(time);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
