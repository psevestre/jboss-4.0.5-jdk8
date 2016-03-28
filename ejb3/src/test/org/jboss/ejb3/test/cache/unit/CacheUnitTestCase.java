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

import junit.framework.Test;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.test.JBossTestCase;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * Sample client for the jboss container.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Id: CacheUnitTestCase.java 57207 2006-09-26 12:06:13Z dimitris@jboss.org $
 */

public class CacheUnitTestCase
extends JBossTestCase
{
   org.apache.log4j.Category log = getLog();

   static boolean deployed = false;
   static int test = 0;

   public CacheUnitTestCase(String name)
   {

      super(name);

   }

   public void testSimple() throws Exception
   {
      /* Tests are in StatefulUnit now.
      MBeanServerConnection server = getServer();
      ObjectName testerName = new ObjectName("jboss.ejb3:service=Tester,test=cache");
      Object[] params = {};
      String[] sig = {};
      server.invoke(testerName, "test", params, sig);
      */
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(CacheUnitTestCase.class, "testejb3-cache-service.xml, cache-test.sar");
   }

}
