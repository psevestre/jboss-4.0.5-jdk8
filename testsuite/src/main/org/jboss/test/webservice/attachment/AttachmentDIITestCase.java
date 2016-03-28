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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.rmi.RemoteException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceFactory;

import junit.framework.Test;

import org.jboss.test.webservice.WebserviceTestBase;

/**
 * Test SOAP with Attachements (SwA) through the JAXRPC DII layer.
 *
 * [ 1039881 ] Cannot create multipart/mixed SOAP attachment
 *
 * @author Thomas.Diesler@jboss.org
 * @since Nov 16, 2004
 */
public class AttachmentDIITestCase extends WebserviceTestBase
{
   private static final String NS_URI = "http://org.jboss.webservice/attachment";
   private static final QName SERVICE_NAME = new QName(NS_URI, "Attachment");
   private static final QName PORT_NAME = new QName(NS_URI, "AttachmentPort");

   private static final QName XSD_STRING = new QName("http://www.w3.org/2001/XMLSchema", "string");
   private static final QName AXIS_MIME_IMAGE = new QName("http://xml.apache.org/xml-soap", "Image");
   private static final QName AXIS_MIME_PLAINTEXT = new QName("http://xml.apache.org/xml-soap", "PlainText");
   private static final QName AXIS_MIME_MULTIPART = new QName("http://xml.apache.org/xml-soap", "Multipart");
   private static final QName AXIS_MIME_SOURCE = new QName("http://xml.apache.org/xml-soap", "Source");

   private static final String JBOSS_MIME_NS = "http://www.jboss.org/jbossws/attachment/mimetype";
   private static final QName JBOSS_MIME_IMAGE_JPEG = new QName(JBOSS_MIME_NS, "image_jpeg");
   private static final QName JBOSS_MIME_IMAGE_GIF = new QName(JBOSS_MIME_NS, "image_gif");
   private static final QName JBOSS_MIME_MULTIPART_MIXED = new QName(JBOSS_MIME_NS, "multipart_mixed");
   private static final QName JBOSS_MIME_TEXT_PLAIN = new QName(JBOSS_MIME_NS, "text_plain");
   private static final QName JBOSS_MIME_TEXT_XML = new QName(JBOSS_MIME_NS, "text_xml");
   private static final QName JBOSS_MIME_APPLICATION_XML = new QName(JBOSS_MIME_NS, "application_xml");

   private final QName MIME_IMAGE_JPEG;
   private final QName MIME_IMAGE_GIF;
   private final QName MIME_MULTIPART_MIXED;
   private final QName MIME_TEXT_PLAIN;
   private final QName MIME_TEXT_XML;
   private final QName MIME_APPLICATION_XML;

   public AttachmentDIITestCase(String name)
   {
      super(name);

      if (isWS4EEAvailable())
      {
         MIME_IMAGE_GIF = AXIS_MIME_IMAGE;
         MIME_IMAGE_JPEG = AXIS_MIME_IMAGE;
         MIME_MULTIPART_MIXED = AXIS_MIME_MULTIPART;
         MIME_TEXT_PLAIN = AXIS_MIME_PLAINTEXT;
         MIME_TEXT_XML = AXIS_MIME_SOURCE;
         MIME_APPLICATION_XML = AXIS_MIME_SOURCE;
      }
      else
      {
         MIME_IMAGE_GIF = JBOSS_MIME_IMAGE_GIF;
         MIME_IMAGE_JPEG = JBOSS_MIME_IMAGE_JPEG;
         MIME_MULTIPART_MIXED = JBOSS_MIME_MULTIPART_MIXED;
         MIME_TEXT_PLAIN = JBOSS_MIME_TEXT_PLAIN;
         MIME_TEXT_XML = JBOSS_MIME_TEXT_XML;
         MIME_APPLICATION_XML = JBOSS_MIME_APPLICATION_XML;
      }
   }

   /** Deploy the test ear */
   public static Test suite() throws Exception
   {
      return getDeploySetup(AttachmentDIITestCase.class, "ws4ee-attachment.war");
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeImageGIF() throws Exception
   {
      String rpcMethodName = "sendMimeImageGIF";
      Call call = setupMimeMessage(rpcMethodName, "image/gif");

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
         sendAndValidateMimeMessage(call, new DataHandler(url));
      }
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeImageJPEG() throws Exception
   {
      String rpcMethodName = "sendMimeImageJPEG";
      Call call = setupMimeMessage(rpcMethodName, "image/jpeg");

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
         sendAndValidateMimeMessage(call, new DataHandler(url));
      }
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeTextPlain() throws Exception
   {
      String rpcMethodName = "sendMimeTextPlain";
      Call call = setupMimeMessage(rpcMethodName, "text/plain");

      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      sendAndValidateMimeMessage(call, new DataHandler(url));
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeMultipart() throws Exception
   {
      String rpcMethodName = "sendMimeMultipart";
      Call call = setupMimeMessage(rpcMethodName, "multipart/mixed");

      URL url = new File("resources/webservice/attachment/attach.txt").toURL();
      MimeMultipart mimepart = new MimeMultipart("mixed");
      MimeBodyPart bodyPart = new MimeBodyPart();
      bodyPart.setDataHandler(new DataHandler(url));
      String bpct = bodyPart.getContentType();
      bodyPart.setHeader("Content-Type", bpct);
      mimepart.addBodyPart(bodyPart);

      sendAndValidateMimeMessage(call, mimepart);
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeTextXML() throws Exception
   {
      String rpcMethodName = "sendMimeTextXML";
      Call call = setupMimeMessage(rpcMethodName, "text/xml");

      DataSource ds = new ExFileDataSource("resources/webservice/attachment/attach.xml", "text/xml");
      sendAndValidateMimeMessage(call, new DataHandler(ds));
   }

   /** Send a multipart message with a text/plain attachment part
    */
   public void testSendMimeApplicationXML() throws Exception
   {
      String rpcMethodName = "sendMimeApplicationXML";
      Call call = setupMimeMessage(rpcMethodName, "application/xml");

      DataSource ds = new ExFileDataSource("resources/webservice/attachment/attach.xml", "application/xml");
      sendAndValidateMimeMessage(call, new DataHandler(ds));
   }

   /** Setup the multipart/related MIME message
    */
   private Call setupMimeMessage(String rpcMethodName, String contentType)
           throws Exception
   {
      ServiceFactory factory = ServiceFactory.newInstance();
      Service service = factory.createService(SERVICE_NAME);

      Call call = service.createCall();
      call.setOperationName(new QName(NS_URI, rpcMethodName));
      call.addParameter("message", XSD_STRING, ParameterMode.IN);

      if (contentType.equals("image/gif"))
         call.addParameter("mimepart", MIME_IMAGE_GIF, ParameterMode.IN);
      else if (contentType.equals("image/jpeg"))
         call.addParameter("mimepart", MIME_IMAGE_JPEG, ParameterMode.IN);
      else if (contentType.equals("text/plain"))
         call.addParameter("mimepart", MIME_TEXT_PLAIN, ParameterMode.IN);
      else if (contentType.startsWith("multipart/"))
         call.addParameter("mimepart", MIME_MULTIPART_MIXED, ParameterMode.IN);
      else if (contentType.equals("text/xml"))
         call.addParameter("mimepart", MIME_TEXT_XML, ParameterMode.IN);
      else if (contentType.equals("application/xml"))
         call.addParameter("mimepart", MIME_APPLICATION_XML, ParameterMode.IN);

      call.setReturnType(XSD_STRING);

      call.setTargetEndpointAddress("http://" + getServerHost() + ":8080/ws4ee-attachment");

      return call;
   }

   /** Send the message and validate the result
    */
   private void sendAndValidateMimeMessage(Call call, Object mimepart)
           throws RemoteException
   {
      String message = "Some text message";
      String value = (String)call.invoke(new Object[]{message, mimepart});
      
      assertEquals("[pass]", value);
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
