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
import org.jboss.ejb3.test.clusteredsession.NodeAnswer;
import org.jboss.ejb3.test.clusteredsession.ClusteredStatelessRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;

/**
 * Test slsb for load-balancing behaviour and others.
 *
 * @author  Ben.Wang@jboss.org
 * @version $Revision: 57207 $
 */
public class StatelessUnitTestCase extends JBossClusteredTestCase
{
   static boolean deployed = false;
   public static int test = 0;
   static Date startDate = new Date();

   protected final String namingFactory =
   System.getProperty(Context.INITIAL_CONTEXT_FACTORY);

   protected final String providerURL =
   System.getProperty(Context.PROVIDER_URL);

   public StatelessUnitTestCase (String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      final String jarName = "clusteredsession-test.jar";
      return JBossClusteredTestCase.getDeploySetup(StatelessUnitTestCase.class,
              jarName);
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

   public void testLoadbalance()
   throws Exception
   {
      getLog().debug(++StatelessUnitTestCase.test +"- "+"Trying the context...");

      InitialContext ctx = getInitialContext(0);

      getLog().debug("Test Stateless Bean loadbalancing");
      getLog().debug("==================================");
      getLog().debug(++StatelessUnitTestCase.test +"- "
              +"Looking up testStatless...");
      ClusteredStatelessRemote stateless = (ClusteredStatelessRemote) ctx.lookup("clusteredStateless/remote");

      NodeAnswer node1 = stateless.getNodeState ();
      getLog ().debug ("Node 1 ID: " +node1);

      stateless = (ClusteredStatelessRemote) ctx.lookup("clusteredStateless/remote");
      NodeAnswer node2 = stateless.getNodeState ();
      getLog ().debug ("Node 2 ID : " +node2);

      assertNotSame (node1.nodeId, node2.nodeId);
   }
}
