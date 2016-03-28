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
package org.jboss.test.webservice.samples;

import junit.framework.Test;
import org.jboss.test.JBossTestCase;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import java.net.URL;


/**
 * A web service client that connects to a SLSB endpoint using
 * the dynamic invokation interface (DII).
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class ServerSideEJBTestCase extends JBossTestCase
{
   public ServerSideEJBTestCase(String name)
   {
      super(name);
   }

   /** Deploy the test */
   public static Test suite() throws Exception
   {
      return getDeploySetup(ServerSideEJBTestCase.class, "ws4ee-samples-server-ejb.jar");
   }

   public void testEJBEndpointAccess() throws Exception
   {
      URL url = new URL("http://" + getServerHost() + ":8080/ws4ee-samples-server-ejb/Organization?wsdl");
      QName qname = new QName("http://org.jboss.test.webservice/samples", "OrganizationService");
      ServiceFactory factory = ServiceFactory.newInstance();
      Service service = factory.createService(url, qname);

      Organization endpoint = (Organization)service.getPort(Organization.class);
      String info = endpoint.getContactInfo("mafia");
      assertEquals("The 'mafia' boss is currently out of office, please call again.", info);
   }
}
