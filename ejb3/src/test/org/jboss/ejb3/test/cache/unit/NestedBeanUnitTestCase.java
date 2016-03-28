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
package org.jboss.ejb3.test.cache.unit;

import org.jboss.test.JBossTestCase;
import org.jboss.ejb3.test.cache.nested.ParentStatefulRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;

/**
 * Test nested SFSB for load-balancing and failover behaviour
 *
 * @author  Ben.Wang@jboss.org
 * @version $Revision: 57207 $
 */
public class NestedBeanUnitTestCase extends JBossTestCase
{
   static boolean deployed = false;
   public static int test = 0;
   static Date startDate = new Date();

   protected final String namingFactory =
   System.getProperty(Context.INITIAL_CONTEXT_FACTORY);

   protected final String providerURL =
   System.getProperty(Context.PROVIDER_URL);

   public NestedBeanUnitTestCase (String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(NestedBeanUnitTestCase.class, "cache-test.jar");
   }

   public void testBasic()
   throws Exception
   {
      getLog().debug(NestedBeanUnitTestCase.test +"- "
              +"Trying the context...");

      getLog().debug("Test Nested Stateful Bean");
      getLog().debug("==================================");
      getLog().debug(++NestedBeanUnitTestCase.test +"- "
              +"Looking up testBasic...");
      InitialContext ctx = getInitialContext();
      ParentStatefulRemote stateful = (ParentStatefulRemote) ctx.lookup("testParentStateful/remote");

      stateful.setName("The Code");
      _sleep(300);

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());

      stateful.remove();
      getLog().debug("ok");
   }

   public void testPassivation()
   throws Exception
   {
      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext();

      getLog().debug("Test Nested Stateful Bean");
      getLog().debug("==================================");
      ParentStatefulRemote stateful = (ParentStatefulRemote) ctx.lookup("testParentStateful/remote");

      stateful.setName("The Code");
      _sleep(300);

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());
      _sleep(11000);
      assertEquals("Counter: ", 3, stateful.increment());
      assertEquals(1, stateful.getPrePassivate());
      assertEquals(1, stateful.getPostActivate());
      assertEquals(1, stateful.getNestedPrePassivate());
      assertEquals(1, stateful.getNestedPostActivate());

      stateful.remove();
      getLog().debug("ok: done");
      // passivate for the second time.
      _sleep(12000);
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
