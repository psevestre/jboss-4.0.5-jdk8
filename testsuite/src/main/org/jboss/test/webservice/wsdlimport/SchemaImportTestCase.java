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
import org.jboss.test.webservice.admindevel.Hello;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.net.URL;

/**
 * Test schema import in the wsdl.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 07-Feb-2005
 */
public class SchemaImportTestCase extends WebserviceTestBase
{
   public SchemaImportTestCase(String name)
   {
      super(name);
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(SchemaImportTestCase.class, "ws4ee-wsdlimport-schema.war, ws4ee-wsdlimport-schema-client.jar");
   }

   public void testHello() throws Exception
   {
      InitialContext iniCtx = getClientContext();
      Service service = (Service)iniCtx.lookup("java:comp/env/service/HelloService");
      Hello helloPort = (Hello)service.getPort(Hello.class);

      String retstr = helloPort.helloString("Tom");
      assertEquals("Hello Tom!", retstr);
   }
}
