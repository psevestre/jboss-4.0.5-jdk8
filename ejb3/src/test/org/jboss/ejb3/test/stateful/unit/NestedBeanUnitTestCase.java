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
package org.jboss.ejb3.test.stateful.unit;

import org.jboss.test.JBossTestCase;
import org.jboss.ejb3.test.stateful.nested.ParentStatefulRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Date;

import junit.framework.Test;

/**
 * Test nested SFSB for repeated passivation
 *
 * @author  Ben.Wang@jboss.org
 * @version $Revision: 45372 $
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
      return getDeploySetup(NestedBeanUnitTestCase.class, "stateful-test.jar");
   }

   public void testBasic()
   throws Exception
   {
      getLog().debug(++NestedBeanUnitTestCase.test +"- "
              +"Trying the context...");

      // Connect to the server0 JNDI
      InitialContext ctx = getInitialContext();

      getLog().debug("Test Stateful Bean");
      getLog().debug("==================================");
      getLog().debug(++NestedBeanUnitTestCase.test +"- "
              +"Looking up testBasic...");
      ParentStatefulRemote stateful = (ParentStatefulRemote) ctx.lookup("testParentStateful/remote");

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());

      stateful.remove();
      getLog().debug("ok");
   }

   public void testStatefulPassivation()
   throws Exception
   {
      getLog().debug(++NestedBeanUnitTestCase.test +"- "+"Trying the context...");

      InitialContext ctx = getInitialContext();

      getLog().debug("Test Stateful Bean passivation");
      getLog().debug("==================================");
      getLog().debug(++NestedBeanUnitTestCase.test +"- "
              +"Looking up testParentStateful...");
      ParentStatefulRemote stateful = (ParentStatefulRemote) ctx.lookup("testParentStateful/remote");

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());
      _sleep(10000);  // should passivated

      assertEquals("Counter: ", 3, stateful.increment());
      assertEquals("Counter: ", 4, stateful.increment());
      assertEquals(1, stateful.getPrePassivate());
      assertEquals(1, stateful.getPostActivate());

      stateful.remove();
      getLog().debug("ok");
   }

   public void testStatefulRepeatedPassivation()
   throws Exception
   {
      getLog().debug(++NestedBeanUnitTestCase.test +"- "+"Trying the context...");

      InitialContext ctx = getInitialContext();

      getLog().debug("Test Stateful Bean passivation");
      getLog().debug("==================================");
      getLog().debug(++NestedBeanUnitTestCase.test +"- "
              +"Looking up testParentStateful...");
      ParentStatefulRemote stateful = (ParentStatefulRemote) ctx.lookup("testParentStateful/remote");
      stateful.reset();

      assertEquals("Counter: ", 1, stateful.increment());
      assertEquals("Counter: ", 2, stateful.increment());
      _sleep(10000);  // should passivated

      assertEquals("Counter: ", 3, stateful.increment());
      assertEquals("Counter: ", 4, stateful.increment());
      assertEquals(1, stateful.getPrePassivate());
      assertEquals(1, stateful.getPostActivate());
      _sleep(10000);  // should passivated

      assertEquals("Counter: ", 5, stateful.increment());
      assertEquals("Counter: ", 6, stateful.increment());
      assertEquals(2, stateful.getPrePassivate());
      assertEquals(2, stateful.getPostActivate());

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
