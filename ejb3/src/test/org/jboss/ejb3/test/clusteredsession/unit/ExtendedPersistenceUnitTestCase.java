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

import org.jboss.ejb3.test.clusteredsession.Customer;
import org.jboss.ejb3.test.clusteredsession.ShoppingCart;
import org.jboss.ejb3.test.clusteredsession.StatelessRemote;
import org.jboss.test.JBossClusteredTestCase;
import junit.framework.Test;

import javax.naming.InitialContext;
import javax.naming.Context;
import java.util.Properties;

/**
 * Tests for extended persistence under clustering.
 *
 * @author Ben Wang
 * @version $Id: ExtendedPersistenceUnitTestCase.java 57207 2006-09-26 12:06:13Z dimitris@jboss.org $
 */

public class ExtendedPersistenceUnitTestCase extends JBossClusteredTestCase
{
   org.apache.log4j.Category log = getLog();

   static boolean deployed = false;
   static int test = 0;

   public ExtendedPersistenceUnitTestCase(String name)
   {

      super(name);

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

   public void testBasic() throws Exception
   {
      ShoppingCart cart = (ShoppingCart) getInitialContext(0).lookup("ShoppingCartBean/remote");
      StatelessRemote stateless = (StatelessRemote) getInitialContext(0).lookup("StatelessSessionBean/remote");
      Customer customer;

      long id = cart.createCustomer();
      customer = stateless.find(id);
      assertEquals("William", customer.getName());
      customer = cart.find(id);
      assertEquals("William", customer.getName());
      cart.update();
      customer = stateless.find(id);
      assertEquals("Bill", customer.getName());
      customer = cart.find(id);
      assertEquals("Bill", customer.getName());
      cart.update2();
      customer = stateless.find(id);
      assertEquals("Billy", customer.getName());
      customer = cart.find(id);
      assertEquals("Billy", customer.getName());
      cart.update3();
      customer = stateless.find(id);
      assertEquals("Bill Jr.", customer.getName());
      customer = cart.find(id);
      assertEquals("Bill Jr.", customer.getName());
      cart.setContainedCustomer();
      cart.checkout();
   }

   public void testPassivation() throws Exception
   {
      ShoppingCart cart = (ShoppingCart) getInitialContext(0).lookup("ShoppingCartBean/remote");
      StatelessRemote stateless = (StatelessRemote) getInitialContext(0).lookup("StatelessSessionBean/remote");
      Customer customer;

      long id = cart.createCustomer();
      customer = stateless.find(id);
      assertEquals("William", customer.getName());
      customer = cart.find(id);
      assertEquals("William", customer.getName());
      cart.update();
      customer = stateless.find(id);
      assertEquals("Bill", customer.getName());
      customer = cart.find(id);
      assertEquals("Bill", customer.getName());
      cart.update2();
      customer = stateless.find(id);
      assertEquals("Billy", customer.getName());
      customer = cart.find(id);
      assertEquals("Billy", customer.getName());
      cart.update3();
      customer = stateless.find(id);
      assertEquals("Bill Jr.", customer.getName());
      customer = cart.find(id);
      assertEquals("Bill Jr.", customer.getName());
      cart.setContainedCustomer();
      Thread.sleep(11000); // passivation
      assertTrue(stateless.isPassivated());
      cart.checkContainedCustomer();
      cart.findAndUpdateStateless();
      cart.updateContained();
      stateless.clearDestroyed();
      assertTrue(cart.isContainedActivated());
      cart.checkout();
      assertTrue(stateless.isDestroyed());
   }

   public static Test suite() throws Exception
   {
      final String jarName = "clusteredsession-test.jar";
      return JBossClusteredTestCase.getDeploySetup(ExtendedPersistenceUnitTestCase.class,
              jarName);
   }

}
