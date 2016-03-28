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
package org.jboss.ejb3.test.regression.ejbthree440.unit;

import junit.framework.Test;
import org.jboss.ejb3.test.regression.ejbthree440.model.MyResource;
import org.jboss.ejb3.test.regression.ejbthree440.session.i.IInheritanceDemo;
import org.jboss.test.JBossTestCase;
import org.jnp.interfaces.NamingContextFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import java.util.Properties;
import org.jboss.serial.io.MarshalledObject;


/**
 * Demonstrates entity inheritance.
 *
 * @author Ortwin Glück
 */
public class FailingTestCase extends JBossTestCase
{
   org.apache.log4j.Category log = getLog();

   static boolean deployed = false;
   static int test = 0;

   public FailingTestCase(String name)
   {

      super(name);

   }

   public void testSerializationError() throws Exception
   {
      try
      {
         UserTransaction tx = null;
         Context ctx = getInitialContext();

         tx = (UserTransaction) ctx.lookup("UserTransaction");
         IInheritanceDemo playground = (IInheritanceDemo) ctx.lookup("InheritanceDemo/remote");

         tx.begin();
         playground.create();
         tx.commit();

         tx.begin();
         MarshalledObject mo = playground.readFromMO();
         MyResource r = (MyResource)mo.get();
         r = (MyResource) playground.read();
         playground.remove();
         tx.commit();
         tx = null;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw e;
      }
   }


   public static Test suite() throws Exception
   {
      return getDeploySetup(FailingTestCase.class, "ejbthree440.ear");
   }
}
