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
package org.jboss.webservice.metadata.wsdl;

// $Id: WSDL11DefinitionFactory.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import org.jboss.logging.Logger;
import org.xml.sax.InputSource;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLLocator;
import javax.wsdl.xml.WSDLReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A factory that creates a WSDL-1.1 <code>Definition</code> from an URL.
 *
 * @author Thomas.Diesler@jboss.org
 * @since 15-April-2004
 */
public class WSDL11DefinitionFactory
{
   // provide logging
   private static Logger log = Logger.getLogger(WSDL11DefinitionFactory.class);

   // This feature is set by default in wsdl4j, it means the object structore contains the imported arguments
   public static final String FEATURE_IMPORT_DOCUMENTS = "javax.wsdl.importDocuments";
   // Set this feature for additional debugging output
   public static final String FEATURE_VERBOSE = "javax.wsdl.verbose";

   // The WSDLReader that is used by this factory
   private WSDLReader wsdlReader;

   // Hide constructor
   private WSDL11DefinitionFactory() throws WSDLException
   {
      WSDLFactory wsdlFactory = WSDLFactory.newInstance();
      wsdlReader = wsdlFactory.newWSDLReader();
      wsdlReader.setFeature(WSDL11DefinitionFactory.FEATURE_VERBOSE, false);
   }

   /** Create a new instance of a wsdl factory */
   public static WSDL11DefinitionFactory newInstance() throws WSDLException
   {
      return new WSDL11DefinitionFactory();
   }

   /** Set a feature on the underlying reader */
   public void setFeature(String name, boolean value) throws IllegalArgumentException
   {
      wsdlReader.setFeature(name, value);
   }

   /**
    * Read the wsdl document from the given URL
    */
   public Definition parse(URL wsdlLocation) throws WSDLException
   {
      if (wsdlLocation == null)
         throw new IllegalArgumentException("URL cannot be null");

      Definition wsdlDefinition = wsdlReader.readWSDL(new WSDLLocatorImpl(wsdlLocation));
      return wsdlDefinition;
   }

   /* A WSDLLocator that can handle wsdl imports
   */
   public static class WSDLLocatorImpl implements WSDLLocator
   {
      private URL wsdlURL;
      private String latestImportURI;

      public WSDLLocatorImpl(URL wsdlFile)
      {
         if (wsdlFile == null)
            throw new IllegalArgumentException("WSDL file argument cannot be null");

         this.wsdlURL = wsdlFile;
      }

      public InputSource getBaseInputSource()
      {
         log.debug("getBaseInputSource [wsdlUrl=" + wsdlURL + "]");
         try
         {
            InputStream is = wsdlURL.openStream();
            if (is == null)
               throw new IllegalArgumentException("Cannot obtain wsdl from [" + wsdlURL + "]");

            return new InputSource(is);
         }
         catch (IOException e)
         {
            throw new RuntimeException("Cannot access wsdl from [" + wsdlURL + "], " + e.getMessage());
         }
      }

      public String getBaseURI()
      {
         return wsdlURL.toExternalForm();
      }

      public InputSource getImportInputSource(String parent, String resource)
      {
         log.debug("getImportInputSource [parent=" + parent + ",resource=" + resource + "]");

         URL parentURL = null;
         try
         {
            parentURL = new URL(parent);
         }
         catch (MalformedURLException e)
         {
            log.error("Not a valid URL: " + parent);
            return null;
         }

         String wsdlImport = null;
         String external = parentURL.toExternalForm();

         // An external URL
         if (resource.startsWith("http://") || resource.startsWith("https://"))
         {
            wsdlImport = resource;
         }

         // Absolute path
         else if (resource.startsWith("/"))
         {
            String beforePath = external.substring(0, external.indexOf(parentURL.getPath()));
            wsdlImport = beforePath + resource;
         }

         // A relative path
         else
         {
            String parentDir = external.substring(0, external.lastIndexOf("/"));

            // remove references to current dir
            while (resource.startsWith("./"))
               resource = resource.substring(2);

            // remove references to parentdir
            while (resource.startsWith("../"))
            {
               parentDir = parentDir.substring(0, parentDir.lastIndexOf("/"));
               resource = resource.substring(3);
            }

            wsdlImport = parentDir + "/" + resource;
         }

         try
         {
            log.debug("Resolved to: " + wsdlImport);
            InputStream is = new URL(wsdlImport).openStream();
            if (is == null)
               throw new IllegalArgumentException("Cannot import wsdl from [" + wsdlImport + "]");

            latestImportURI = wsdlImport;
            return new InputSource(is);
         }
         catch (IOException e)
         {
            throw new RuntimeException("Cannot access imported wsdl [" + wsdlImport + "], " + e.getMessage());
         }
      }

      public String getLatestImportURI()
      {
         return latestImportURI;
      }
   }
}
