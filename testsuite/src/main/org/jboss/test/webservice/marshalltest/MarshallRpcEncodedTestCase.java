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
package org.jboss.test.webservice.marshalltest;

import javax.naming.InitialContext;
import javax.xml.rpc.Service;

import junit.framework.Test;

/**
 * Tests of the ws4ee rpc-encoded marshalling
 *
 * @author Thomas.Diesler@jboss.org
 * @since 09-May-2004
 */
public class MarshallRpcEncodedTestCase extends MarshallTestBase
{
   private static MarshallEndpoint rpcencPort;

   /**
    * Construct the test case with a given name
    */
   public MarshallRpcEncodedTestCase(String name)
   {
      super(name);
   }

   /**
    * deploy the test archives
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(MarshallRpcEncodedTestCase.class, "ws4ee-marshall-rpcenc.ear");
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      if (rpcencPort == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/MarshallRpcEncService");
         rpcencPort = (MarshallEndpoint)service.getPort(MarshallEndpoint.class);
      }
      port = rpcencPort;
   }
}
