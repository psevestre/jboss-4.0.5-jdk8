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
package org.jboss.jms.client.standard;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

import org.jboss.jms.client.JBossConnectionFactory;
import org.jboss.remoting.Client;
import org.jboss.remoting.InvokerLocator;

/**
 * A factory for standard implementations
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class StandardImplementationFactory
   implements ObjectFactory
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   // Public --------------------------------------------------------

   // ObjectFactory implementation ----------------------------------

   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception
   {
      try
      {
         Reference reference = (Reference) obj;
         String className = reference.getClassName();
         if (className.equals(JBossConnectionFactory.class.getName()))
            return new JBossConnectionFactory(getImplementation(reference));
      }
      catch (Exception ignored)
      {
      }
      return null;
   }

   // Protected ------------------------------------------------------

   /**
    * Get the implementation from the reference
    * 
    * @param reference the reference
    * @return the implementation
    */
   protected StandardImplementation getImplementation(Reference reference)
      throws Exception
   {
      String locatorURI = (String) reference.get("locatorURI").getContent();
      InvokerLocator locator = new InvokerLocator(locatorURI);
      Client client = new Client(locator, "JMS");
      return new StandardImplementation(client);
   }
   
   // Package Private ------------------------------------------------

   // Private --------------------------------------------------------

   // Inner Classes --------------------------------------------------

}
