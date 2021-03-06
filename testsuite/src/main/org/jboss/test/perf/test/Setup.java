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
package org.jboss.test.perf.test;

import javax.rmi.PortableRemoteObject;
import javax.security.auth.login.LoginContext;

import junit.framework.TestSuite;

import org.jboss.test.perf.interfaces.EntityPK;
import org.jboss.test.perf.interfaces.Entity2PK;
import org.jboss.test.perf.interfaces.EntityHome;
import org.jboss.test.perf.interfaces.Entity2Home;

import org.jboss.test.util.AppCallbackHandler;
import org.jboss.test.JBossTestSetup;

/** Setup utility class.
 
 @author Scott.Stark@jboss.org
 @version $Revision: 57211 $
 */
public class Setup extends JBossTestSetup 
{
   LoginContext lc = null;
   String filename;
   boolean isSecure;

   Setup(TestSuite suite, String filename, boolean isSecure) throws Exception
   {
      super(suite);
      this.filename = filename;
      this.isSecure = isSecure;
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      getLog().debug("+++ Performing the TestSuite setup");
      if( isSecure )
      {
         login();
      }
      deploy(filename);
      removeAll();
      createEntityBeans(getBeanCount());
      createEntity2Beans(getBeanCount());
   }
   protected void tearDown() throws Exception
   {
      getLog().debug("+++ Performing the TestSuite tear down");
      removeAll();
      undeploy(filename);
      if( isSecure )
      {
         logout();
      }
   }

   private void removeAll()
   {
      try 
      {
         removeEntityBeans(getBeanCount());
      }
      catch (Exception e)
      {
         //ignore
      } // end of try-catch
      try 
      {
         removeEntity2Beans(getBeanCount());
      }
      catch (Exception e)
      {
         //ignore
      } // end of try-catch
   }

   private void createEntityBeans(int max) throws Exception
   {
      String jndiName = isSecure ? "secure/perf/Entity" : "perfEntity";
      Object obj = getInitialContext().lookup(jndiName);
      obj = PortableRemoteObject.narrow(obj, EntityHome.class);
      EntityHome home = (EntityHome) obj;
      getLog().debug("Creating "+max+" Entity beans");
      for(int n = 0; n < max; n ++)
         home.create(n, n);
   }
   private void removeEntityBeans(int max) throws Exception
   {
      String jndiName = isSecure ? "secure/perf/Entity" : "perfEntity";
      Object obj = getInitialContext().lookup(jndiName);
      obj = PortableRemoteObject.narrow(obj, EntityHome.class);
      EntityHome home = (EntityHome) obj;
      getLog().debug("Removing "+max+" Entity beans");
      for(int n = 0; n < max; n ++)
         home.remove(new EntityPK(n));
   }
   private void createEntity2Beans(int max) throws Exception
   {
      String jndiName = isSecure ? "secure/perf/Entity2" : "perfEntity2";
      Object obj = getInitialContext().lookup(jndiName);
      obj = PortableRemoteObject.narrow(obj, Entity2Home.class);
      Entity2Home home = (Entity2Home) obj;
      getLog().debug("Creating "+max+" Entity2 beans");
      for(int n = 0; n < max; n ++)
         home.create(n, "String"+n, new Double(n), n);
   }
   private void removeEntity2Beans(int max) throws Exception
   {
      String jndiName = isSecure ? "secure/perf/Entity2" : "perfEntity2";
      Object obj = getInitialContext().lookup(jndiName);
      obj = PortableRemoteObject.narrow(obj, Entity2Home.class);
      Entity2Home home = (Entity2Home) obj;
      getLog().debug("Removing "+max+" Entity2 beans");
      for(int n = 0; n < max; n ++)
         home.remove(new Entity2PK(n, "String"+n, new Double(n)));
   }

   private void login() throws Exception
   {
      flushAuthCache();
      String username = "jduke";
      char[] password = "theduke".toCharArray();
      AppCallbackHandler handler = new AppCallbackHandler(username, password);
      getLog().debug("Creating LoginContext(other)");
      lc = new LoginContext("spec-test", handler);
      lc.login();
      getLog().debug("Created LoginContext, subject="+lc.getSubject());
   }

   private void logout()
   {
      try
      {
         lc.logout();
      }
      catch(Exception e)
      {
         getLog().error("logout error: ", e);
      }
   }
}
