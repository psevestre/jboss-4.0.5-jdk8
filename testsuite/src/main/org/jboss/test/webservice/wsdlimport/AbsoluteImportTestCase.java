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
package org.jboss.test.webservice.wsdlimport;

import junit.framework.Test;
import org.jboss.test.JBossTestCase;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.net.URL;

/**
 * Test an absolute import in a wsdl.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 09-Nov-2004
 */
public class AbsoluteImportTestCase extends WebserviceTestBase
{
   private final String WSDL_LOCATION = "http://" + getServerHost() + ":8080/ws4ee-wsdlimport-absolute2/HelloPort?wsdl";
   private String NAMESPACE = "http://test.jboss.org/ws4eesimple";
   private final QName SERVICE_NAME = new QName(NAMESPACE, "HelloService");

   /**
    * Construct the test case with a given name
    */
   public AbsoluteImportTestCase(String name)
   {
      super(name);
   }

   /**
    * deploy the test archives
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(AbsoluteImportTestCase.class, "ws4ee-wsdlimport-absolute1.war, ws4ee-wsdlimport-absolute2.war");
   }

   /**
    * Test DII access
    */
   public void testHello() throws Exception
   {
      ServiceFactory serviceFactory = ServiceFactory.newInstance();
      Service service = serviceFactory.createService(new URL(WSDL_LOCATION), SERVICE_NAME);
      Call call = (Call)service.createCall(new QName(NAMESPACE, "HelloWsPort"), "sayHello");
      String retstr = (String)call.invoke(new Object[]{"Hello"});
      assertEquals("'Hello' to you too!", retstr);
   }
}
