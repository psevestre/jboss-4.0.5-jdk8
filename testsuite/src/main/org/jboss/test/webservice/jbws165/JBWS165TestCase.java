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
package org.jboss.test.webservice.jbws165;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;

/**
 * Tests <context-root> and <port-component-root> elements in EJB endpoints
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 10-Jun-2005
 */
public class JBWS165TestCase extends WebserviceTestBase
{
   /**
    * Construct the test case with a given name
    */
   public JBWS165TestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(JBWS165TestCase.class, "ws4ee-jbws165.ear");
   }

   public void testNone() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service) iniCtx.lookup("java:comp/env/service/HelloNone");
      Hello port = (Hello) service.getPort(Hello.class);
      String retObj = port.hello(getName());
      assertEquals(getName(), retObj);
   }
   
   public void testPortComponentURIOne() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service) iniCtx.lookup("java:comp/env/service/HelloPCOne");
      QName portName = new QName("http://org.jboss.test.webservice/jbws165", "HelloPortOne");
      Hello port = (Hello) service.getPort(portName, Hello.class);
      String retObj = port.hello(getName());
      assertEquals(getName(), retObj);
   }
   
   public void testPortComponentURITwo() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service) iniCtx.lookup("java:comp/env/service/HelloPCTwo");
      QName portName = new QName("http://org.jboss.test.webservice/jbws165", "HelloPortTwo");
      Hello port = (Hello) service.getPort(portName, Hello.class);
      String retObj = port.hello(getName());
      assertEquals(getName(), retObj);
   }
   
   public void testContextRoot() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service) iniCtx.lookup("java:comp/env/service/HelloContextRoot");
      Hello port = (Hello) service.getPort(Hello.class);
      String retObj = port.hello(getName());
      assertEquals(getName(), retObj);
   }
   
   public void testBoth() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service) iniCtx.lookup("java:comp/env/service/HelloBoth");
      Hello port = (Hello) service.getPort(Hello.class);
      String retObj = port.hello(getName());
      assertEquals(getName(), retObj);
   }
}
