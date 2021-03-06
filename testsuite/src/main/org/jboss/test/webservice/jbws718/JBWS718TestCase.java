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
package org.jboss.test.webservice.jbws718;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;

/** 
 * DOM Serialization in ServiceReferencable leads to stack overflow
 * http://jira.jboss.com/jira/browse/JBWS-718
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 16-Feb-2006
 */
public class JBWS718TestCase extends WebserviceTestBase
{
   private static ReportingServiceSoap port;
   
   public JBWS718TestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS718TestCase.class, "ws4ee-jbws718.war, ws4ee-jbws718-client.jar");
   }

   public void setUp() throws Exception
   {
      super.setUp();
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/TestService");
         port = (ReportingServiceSoap)service.getPort(ReportingServiceSoap.class);
      }
   }
   
   public void testEndpoint() throws Exception
   {
      assertEquals("some link", port.getReportLink("some link"));
   }
}
