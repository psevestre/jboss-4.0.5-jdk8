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
package org.jboss.test.cts.test;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import junit.framework.Test;

import org.jboss.test.JBossTestCase;
import org.jboss.test.cts.interfaces.CtsCmpHome;
import org.jboss.test.cts.interfaces.CtsCmp;
import org.jboss.test.cts.keys.AccountPK;

/** Tests of accessing the UserTransaction interface.
 *
 *  @author Scott.Stark@jboss.org
 *  @version $Revision: 57211 $
 */
public class UserTransactionLookupTestCase extends JBossTestCase
{

   public UserTransactionLookupTestCase(String name)
   {
      super(name);
   }

   /**
    * Test that one can obtain the UserTransaction when a non-default
    *    InitialContext is needed.
    * 
    * See jira issue JBAS-1270
    * @throws Exception
    */ 
   public void testWithInvalidDefaultJndiContext() throws Exception
   {
      System.setProperty(Context.PROVIDER_URL, getServerHost() + ":65535");
      Properties env = new Properties(System.getProperties());
      env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.NamingContextFactory");
      env.setProperty(Context.PROVIDER_URL, getServerHost() + ":1099");
      env.setProperty("jnp.disableDiscovery", "true");
      InitialContext ctx = new InitialContext(env);
      UserTransaction ut = (UserTransaction) ctx.lookup("UserTransaction");
      ut.getStatus();

      ut.begin();
      CtsCmpHome home = (CtsCmpHome) ctx.lookup("ejbcts/CMPBean");
      AccountPK pk = new AccountPK("testWithInvalidDefaultJndiContext");
      CtsCmp bean = home.create(pk, "Scott");
      bean.setPersonsAge(40);
      ut.commit();

      assertTrue("age == 40", bean.getPersonsAge() == 40);

      ut.begin();
      bean.setPersonsAge(100);
      ut.rollback();
      assertTrue("age == 40", bean.getPersonsAge() == 40);

      bean.remove();
      ctx.close();
   }

   public void testWithDefaultJndiContext() throws Exception
   {
      log.info("+++ testWithDefaultJndiContext");
      System.setProperty(Context.PROVIDER_URL, getServerHost() + ":1099");
      InitialContext ctx = new InitialContext();
      UserTransaction ut = (UserTransaction) ctx.lookup("UserTransaction");
      ut.getStatus();

      ut.begin();
      CtsCmpHome home = (CtsCmpHome) ctx.lookup("ejbcts/CMPBean");
      AccountPK pk = new AccountPK("testWithDefaultJndiContext");
      CtsCmp bean = home.create(pk, "Scott");
      bean.setPersonsAge(40);
      ut.commit();

      assertTrue("age == 40", bean.getPersonsAge() == 40);

      ut.begin();
      bean.setPersonsAge(100);
      ut.rollback();
      assertTrue("age == 40", bean.getPersonsAge() == 40);

      bean.remove();
      ctx.close();
   }

	public static Test suite() throws Exception
   {
		return getDeploySetup(UserTransactionLookupTestCase.class, "cts.jar");
   }

}
