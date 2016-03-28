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
package org.jboss.test.webservice.addressrewrite;

import java.net.URL;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;

import org.jboss.test.JBossTestCase;

/**
 * Test soap:address rewriting in the WSDL.
 *
 * @author jason.greene@jboss.com
 */
public class AddressRewriteTestCase extends JBossTestCase
{
   private static String NAMESPACE = "http://test.jboss.org/ws4eesimple";
   private String WSDL_LOCATION = "http://" + getServerHost() + ":8080/ws4ee-addressrewrite/ValidURL?wsdl";
   private String WSDL_LOCATION_SEC = "http://" + getServerHost() + ":8080/ws4ee-addressrewrite-sec/ValidURL?wsdl";

   private ObjectName objectName;
   private Boolean alwaysModifySOAPAddress;
   private String wsHost;

   /**
    * Construct the test case with a given name
    */
   public AddressRewriteTestCase(String name)
   {
      super(name);
   }

   public void setUp() throws Exception
   {
      ObjectName ws4eeServiceName = new ObjectName("jboss.ws4ee:service=AxisService");
      ObjectName jbosswsServiceName = new ObjectName("jboss.ws:service=ServiceEndpointManager");

      if (getServer().isRegistered(ws4eeServiceName))
         objectName = ws4eeServiceName;
      else if (getServer().isRegistered(jbosswsServiceName))
         objectName = jbosswsServiceName;

      alwaysModifySOAPAddress = (Boolean)getServer().getAttribute(objectName, "AlwaysModifySOAPAddress");
      wsHost = (String)getServer().getAttribute(objectName, "WebServiceHost");
   }

   public void tearDown() throws Exception
   {
      Attribute attr = new Attribute("AlwaysModifySOAPAddress", alwaysModifySOAPAddress);
      getServer().setAttribute(objectName, attr);
   }

   public void testRewrite() throws Exception
   {
      setupAlwaysModify(new Boolean(true));
      deploy("ws4ee-addressrewrite.war");
      try
      {
         ServiceFactory serviceFactory = ServiceFactory.newInstance();

         Service service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "ValidURLService"));
         Call call = (Call)service.createCall(new QName(NAMESPACE, "ValidURLPort"), "sayHello");
         assertEquals("http://" + wsHost + ":8080/ws4ee-addressrewrite/ValidURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "InvalidURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidURLPort"), "sayHello");
         assertEquals("http://" + wsHost + ":8080/ws4ee-addressrewrite/InvalidURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "ValidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "ValidSecureURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite/ValidSecureURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "InvalidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidSecureURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite/InvalidSecureURL", call.getTargetEndpointAddress());
      }
      finally
      {
         undeploy("ws4ee-addressrewrite.war");
      }
   }

   /**
    * Rewrite soap:address URL according to transport guarantee
    * 
    * http://jira.jboss.org/jira/browse/JBWS-454
    */
   public void testSecureRewrite() throws Exception
   {
      deploy("ws4ee-addressrewrite-sec.war");
      try
      {
         ServiceFactory serviceFactory = ServiceFactory.newInstance();

         Service service = serviceFactory.createService(new URL(WSDL_LOCATION_SEC), new QName(NAMESPACE, "ValidURLService"));
         Call call = (Call)service.createCall(new QName(NAMESPACE, "ValidURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite-sec/ValidURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION_SEC), new QName(NAMESPACE, "InvalidURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite-sec/InvalidURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION_SEC), new QName(NAMESPACE, "ValidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "ValidSecureURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite-sec/ValidSecureURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION_SEC), new QName(NAMESPACE, "InvalidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidSecureURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite-sec/InvalidSecureURL", call.getTargetEndpointAddress());
      }
      finally
      {
         undeploy("ws4ee-addressrewrite-sec.war");
      }
   }

   public void testNoRewrite() throws Exception
   {
      setupAlwaysModify(new Boolean(false));
      deploy("ws4ee-addressrewrite.war");
      try
      {
         ServiceFactory serviceFactory = ServiceFactory.newInstance();

         Service service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "ValidURLService"));
         Call call = (Call)service.createCall(new QName(NAMESPACE, "ValidURLPort"), "sayHello");
         assertEquals("http://somehost:80/somepath", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "InvalidURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidURLPort"), "sayHello");
         assertEquals("http://" + wsHost + ":8080/ws4ee-addressrewrite/InvalidURL", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "ValidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "ValidSecureURLPort"), "sayHello");
         assertEquals("https://somehost:443/some-secure-path", call.getTargetEndpointAddress());

         service = serviceFactory.createService(new URL(WSDL_LOCATION), new QName(NAMESPACE, "InvalidSecureURLService"));
         call = (Call)service.createCall(new QName(NAMESPACE, "InvalidSecureURLPort"), "sayHello");
         assertEquals("https://" + wsHost + ":8443/ws4ee-addressrewrite/InvalidSecureURL", call.getTargetEndpointAddress());
      }
      finally
      {
         undeploy("ws4ee-addressrewrite.war");
      }
   }

   private void setupAlwaysModify(Boolean value) throws Exception
   {
      Attribute attr = new Attribute("AlwaysModifySOAPAddress", value);
      getServer().setAttribute(objectName, attr);
   }
}