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
package org.jboss.test.webservice.attachmentstepbystep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;
import junit.framework.Test;
import org.jboss.test.JBossTestCase;
import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.test.webservice.attachment.AttachmentDIITestCase;

public class MyServiceClientTestCase extends WebserviceTestBase
{
   public MyServiceClientTestCase(String name)
   {
      super(name);
   }

   /** Deploy the test  */
   public static Test suite() throws Exception
   {
      return getDeploySetup(MyServiceClientTestCase.class, "myservice.war");
   }

   public void testMyService() throws Exception
   {
      String NS_URI = "http://org.jboss.test.webservice.attachmentstepbystep/MyService";
      QName ATTACHMENT_TYPE;

      if (isWS4EEAvailable())
         ATTACHMENT_TYPE = new QName("http://xml.apache.org/xml-soap", "DataHandler");
      else
         ATTACHMENT_TYPE = new QName("http://www.jboss.org/jbossws/attachment/mimetype", "text_plain");

      ServiceFactory factory = ServiceFactory.newInstance();
      Service service = factory.createService(new QName(NS_URI, "MyService"));
      Call call = service.createCall(new QName(NS_URI, "MyService"));
      call.setTargetEndpointAddress("http://" + getServerHost() + ":8080/myservice/MyService");

      // send the text file
      call.setOperationName(new QName(NS_URI, "myService"));
      call.addParameter("mimepart", ATTACHMENT_TYPE, DataHandler.class, ParameterMode.IN);
      call.setReturnType(ATTACHMENT_TYPE, DataHandler.class);
      DataHandler dh = (DataHandler)call.invoke(new Object[] { new DataHandler(new FileDataSource(
            "resources/webservice/attachmentstepbystep/attachment_client2server.txt")) });

      assertNotNull(dh);

      if (dh != null)
      {
         BufferedReader in = new BufferedReader(new InputStreamReader(dh.getInputStream()));
         String line = "";
         String result = "";
         line = in.readLine();
         result = "" + line;
         while (line != null)
         {
            line = in.readLine();
            result = result + line;
         }
         in.close();
         assertEquals(192, result.length());
      }
   }
}
