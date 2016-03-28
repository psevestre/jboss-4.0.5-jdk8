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
package org.jboss.test.webservice.handlerflow;

import java.util.Arrays;
import java.util.List;

import javax.naming.InitialContext;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.JBossTestCase;
import org.jboss.test.webservice.WebserviceTestBase;

/**
 * The HelloEjb is the web service client conecting to HelloJSE.
 * Both the client and the server have handlers configured, we test
 * the flow of handler invocations.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 12-May-2004
 */
public class HandlerFlowTestCase extends WebserviceTestBase
{
   private static HelloRemote ejb;
   
   public HandlerFlowTestCase(String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      // JBAS-3608, the execution order of tests in this test case is important
      // so it must be defined explicitly when running under some JVMs
      TestSuite suite = new TestSuite();
      suite.addTest(new HandlerFlowTestCase("testHandlerFlowAllPass"));
      suite.addTest(new HandlerFlowTestCase("testHandlerFlowClientReturn"));
      suite.addTest(new HandlerFlowTestCase("testHandlerFlowServerReturn"));      
      
      return JBossTestCase.getDeploySetup(suite, "ws4ee-handlerflow.jar, ws4ee-handlerflow.war");  
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      
      if (ejb == null)
      {
         InitialContext iniCtx = getClientContext();
         HelloHome home = (HelloHome)iniCtx.lookup("ejb/HelloEjb");
         ejb = home.create();
      }
   }

   /**
    * Call the ejb and verify the hander tracker protocol
    */
   public void testHandlerFlowAllPass() throws Exception
   {
      // test direct EJB access
      List protocol = Arrays.asList(ejb.sayHello("Hello"));

      String[] exp = {
         "ClientHandler1 init",
         "ClientHandler2 init",
         "ClientHandler1 handleRequest",
         "ClientHandler2 handleRequest",
         "ServerHandler1 init",
         "ServerHandler2 init",
         "ServerHandler1 handleRequest",
         "ServerHandler2 handleRequest",
         "jse: 'Hello' to you too!",
         "ServerHandler2 handleResponse",
         "ServerHandler1 handleResponse",
         "ClientHandler2 handleResponse",
         "ClientHandler1 handleResponse",
         "ejb: 'Hello' to you too!"
      };
      
      assertHandlerProtocol(exp, protocol);
   }

   /**
    * ClientHandler2 should return false and terminate the call
    */
   public void testHandlerFlowClientReturn() throws Exception
   {
      if (isWS4EEAvailable() == false)
      {
         List protocol = Arrays.asList(ejb.sayHello("ClientReturn"));

         String[] exp = { 
               "ClientHandler1 handleRequest", 
               "ClientHandler2 handleRequest", 
               "ClientHandler2 handleResponse", 
               "ClientHandler1 handleResponse",
               "ejb: Return in ClientHandler2" };

         assertHandlerProtocol(exp, protocol);
      }
   }


   /**
    * ClientHandler2 should return false and terminate the call
    */
   public void testHandlerFlowServerReturn() throws Exception
   {
      if (isWS4EEAvailable() == false)
      {
         List protocol = Arrays.asList(ejb.sayHello("ServerReturn"));

         String[] exp = { 
               "ClientHandler1 handleRequest", 
               "ClientHandler2 handleRequest", 
               "ServerHandler1 handleRequest", 
               "ServerHandler2 handleRequest",
               "ServerHandler2 handleResponse", 
               "ServerHandler1 handleResponse", 
               "ClientHandler2 handleResponse", 
               "ClientHandler1 handleResponse",
               "ejb: Return in ServerHandler2" };

         assertHandlerProtocol(exp, protocol);
      }
   }

   private void assertHandlerProtocol(String[] exp, List protocol)
   {
      assertEquals("Wrong number of entries: " + protocol, exp.length, protocol.size());

      for (int i = 0; i < protocol.size(); i++)
      {
         String msg = (String)protocol.get(i);
         boolean equals = msg.startsWith(exp[i]);
         assertTrue("Wrong entry: " + msg + " in " + protocol, equals);
      }
   }
}
