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
package org.jboss.test.webservice.jbws663;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.test.webservice.jbws663.holders.ResponseInfoHolder;
import org.jboss.test.webservice.jbws663.holders.SubscriptionInfoHolder;

/**
 * Multiple bindings are not supported
 * 
 * http://jira.jboss.org/jira/browse/JBWS-663
 *
 * @author Thomas.Diesler@jboss.org
 * @since 21-Jan-2006
 */
public class JBWS663BareBoundTestCase extends WebserviceTestBase
{
   private static SMSTextMessagingSoapBareBound port;
   
   public JBWS663BareBoundTestCase(String name)
   {
      super(name);
   }
   
   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS663BareBoundTestCase.class, "ws4ee-jbws663bb.war, ws4ee-jbws663bb-client.jar");
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/SMSService");
         port = (SMSTextMessagingSoapBareBound)service.getPort(SMSTextMessagingSoapBareBound.class);
      }
   }

   public void testCreateService() throws Exception
   {
      assertNotNull("port not null", port);
   }
   
   public void testSendMessage() throws Exception
   {
      if (isJBossWSAvailable())
      {
         LicenseInfo li = new LicenseInfo(null, new RegisteredUser("kermit", "thefrog"));
         ResponseInfoHolder rih = new ResponseInfoHolder();
         SubscriptionInfoHolder sih = new SubscriptionInfoHolder();
         SendMessage reqMsg = new SendMessage("1234", "5678", "Kermit", "I luv Piggy");
         SendMessageResponse resMsg = port.sendMessage(reqMsg, li, rih, sih);
         SMSTextMessageTargetStatus status = resMsg.getSendMessageResult();

         assertNotNull("status not null", status);
         assertEquals(100, status.getMessageStatus().getStatusCode());
         assertEquals("ok", status.getMessageStatus().getStatusText());
         assertEquals("kermit", status.getMessageStatus().getStatusExtra());

         assertEquals("all ok", rih.value.getResponse());
         assertEquals("valid", sih.value.getLicenseStatus());
      }
   }
}
