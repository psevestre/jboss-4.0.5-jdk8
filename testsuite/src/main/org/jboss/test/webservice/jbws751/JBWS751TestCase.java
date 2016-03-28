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
package org.jboss.test.webservice.jbws751;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;

/** 
 * Multiple schema imports with the same namespace
 * http://jira.jboss.com/jira/browse/JBWS-751
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 16-Mar-2006
 */
public class JBWS751TestCase extends WebserviceTestBase
{
   private static ITranHistory port;

   public JBWS751TestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS751TestCase.class, null);
      //return getDeploySetup(JBWS751TestCase.class, "ws4ee-jbws751.war, ws4ee-jbws751-client.jar");
   }

   public void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         //Service service = (Service)iniCtx.lookup("java:comp/env/service/TestService");
         //port = (ITranHistory)service.getPort(ITranHistory.class);
      }
   }

   public void testSimpleAccess() throws Exception
   {
      TransactionHistoryRq req = new TransactionHistoryRq();
      req.setSessionId("sessionID");

      System.out.println("FIXME: JBWS-751");
      //TransactionHistoryRs res = port.getTransactionHistory(req);
      //assertEquals(req.getSessionId(), res.getSessionId());
   }
}
