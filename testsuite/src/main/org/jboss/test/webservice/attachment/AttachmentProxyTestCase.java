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
package org.jboss.test.webservice.attachment;

import junit.framework.Test;
import org.jboss.test.webservice.WebserviceTestBase;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.xml.rpc.Service;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

/**
 * Test SOAP with Attachements (SwA) through the JAXRPC dynamic proxy layer.
 *
 * [ 1039881 ] Cannot create multipart/mixed SOAP attachment
 *
 * @author Thomas.Diesler@jboss.org
 * @since 16-Nov-2004
 */
public class AttachmentProxyTestCase extends WebserviceTestBase
{
   private static Attachment port;

   public AttachmentProxyTestCase(String name)
   {
      super(name);
   }

   /** Deploy the test ear */
   public static Test suite() throws Exception
   {
      return getDeploySetup(AttachmentProxyTestCase.class, "ws4ee-attachment.war, ws4ee-attachment-client.jar");
   }

   protected void setUp() throws Exception
   {
      if (port == null)
      {
         InitialContext iniCtx = getClientContext();
         Service service = (Service)iniCtx.lookup("java:comp/env/service/AttachmentService");
         port = (Attachment)service.getPort(Attachment.class);
      }
   }

   public void testSendMimeImageGIF() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.gif").toURL();

      // On Linux the X11 server must be installed properly to create images successfully.
      // If the image cannot be created in the test VM, we assume it cannot be done on the
      // server either, so we just skip the test
      Image image = null;
      try
      {
         image = Toolkit.getDefaultToolkit().createImage(url);
      }
      catch (Throwable th)
      {
         log.warn("Cannot create Image: " + th);
      }

      if (image != null)
      {
         String value = port.sendMimeImageGIF("Some text message", new DataHandler(url));
         assertEquals("[pass]", value);
      }
   }

   public void testSendMimeImageJPEG() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.jpeg").toURL();

      // On Linux the X11 server must be installed properly to create images successfully.
      // If the image cannot be created in the test VM, we assume it cannot be done on the
      // server either, so we just skip the test
      Image image = null;
      try
      {
         image = Toolkit.getDefaultToolkit().createImage(url);
      }
      catch (Throwable th)
      {
         log.warn("Cannot create Image: " + th);
      }

      if (image != null)
      {
         String value = port.sendMimeImageJPEG("Some text message", new DataHandler(url));
         assertEquals("[pass]", value);
      }
   }

   public void testSendMimeTextPlain() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      String value = port.sendMimeTextPlain("Some text message", new DataHandler(url));
      assertEquals("[pass]", value);
   }

   public void testSendMimeMultipart() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      MimeMultipart multipart = new MimeMultipart("mixed");
      MimeBodyPart bodyPart = new MimeBodyPart();
      bodyPart.setDataHandler(new DataHandler(url));
      String bpct = bodyPart.getContentType();
      bodyPart.setHeader("Content-Type", bpct);
      multipart.addBodyPart(bodyPart);

      String value = port.sendMimeMultipart("Some text message", multipart);
      assertEquals("[pass]", value);
   }

   public void testSendMimeTextXML() throws Exception
   {
      // URLDataSource uses the JVM /lib/content-types.properties,
      // which defines all xml files as application/xml.
      ExFileDataSource dataSource = new ExFileDataSource("resources/webservice/attachment/attach.xml", "text/xml");
      String value = port.sendMimeTextXML("Some text message", new DataHandler(dataSource));
      assertEquals("[pass]", value);
   }

   public void testSendMimeApplicationXML() throws Exception
   {
      ExFileDataSource dataSource = new ExFileDataSource("resources/webservice/attachment/attach.xml", "application/xml");
      String value = port.sendMimeApplicationXML("Some text message", new DataHandler(dataSource));
      assertEquals("[pass]", value);
   }

   /* Test JPEG return values.
    *
    * Note that we do not test echoing a GIF because the SUN JVM does not support
    * outputing GIF files because of patent issues
    */
   public void testEchoMimeImageJPEG() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.jpeg").toURL();

      // On Linux the X11 server must be installed properly to create images successfully.
      // If the image cannot be created in the test VM, we assume it cannot be done on the
      // server either, so we just skip the test
      Image image = null;
      try
      {
         image = Toolkit.getDefaultToolkit().createImage(url);
      }
      catch (Throwable th)
      {
         log.warn("Cannot create Image: " + th);
      }

      if (image != null)
      {
         Object retValue = port.echoMimeImageJPEG(new DataHandler(url));
         assertTrue("Unexpected: " + retValue, retValue instanceof Image);
      }
   }

   public void testEchoMimeTextPlain() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      Object retValue = port.echoMimeTextPlain(new DataHandler(url));
      assertTrue("Unexpected: " + retValue, retValue instanceof String);
      assertEquals("This is a plain text attachment.", ((String)retValue).trim());
   }

   public void testEchoMimeMultipart() throws Exception
   {
      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      MimeMultipart multipart = new MimeMultipart("mixed");
      MimeBodyPart bodyPart = new MimeBodyPart();
      bodyPart.setDataHandler(new DataHandler(url));
      String bpct = bodyPart.getContentType();
      bodyPart.setHeader("Content-Type", bpct);
      multipart.addBodyPart(bodyPart);

      Object retValue = port.echoMimeMultipart(multipart);
      assertTrue("Unexpected: " + retValue, retValue instanceof MimeMultipart);
   }

   public void testEchoMimeTextXML() throws Exception
   {
      ExFileDataSource dataSource = new ExFileDataSource("resources/webservice/attachment/attach.xml", "text/xml");
      Object retValue = port.echoMimeTextXML(new DataHandler(dataSource));
      assertTrue("Unexpected: " + retValue, retValue instanceof Source);
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testEchoMimeApplicationXML() throws Exception
   {
      FileInputStream stream = new FileInputStream("resources/webservice/attachment/attach.xml");
      Object retValue = port.echoMimeApplicationXML(new StreamSource(stream));
      assertTrue("Unexpected: " + retValue, retValue instanceof Source);
   }

   public void testEchoHandler() throws Exception
   {
      ExFileDataSource dataSource = new ExFileDataSource("resources/webservice/attachment/attach.xml", "text/xml");
      DataHandler retValue = port.echoHandler(new DataHandler(dataSource));
      String mimeType = retValue.getContentType();
      assertEquals("Invalid mime-type: ", "text/xml", mimeType);
   }

   static class ExFileDataSource extends FileDataSource
   {
      private String contentType;

      public ExFileDataSource(String source, String contentType)
      {
         super(source);
         this.contentType = contentType;
      }

      public String getContentType()
      {
         return contentType;
      }
   }
}