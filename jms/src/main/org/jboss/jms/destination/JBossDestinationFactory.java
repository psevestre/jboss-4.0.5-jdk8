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
package org.jboss.jms.destination;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

/**
 * A factory for destinations
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class JBossDestinationFactory
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
         if (className.equals(JBossQueue.class.getName()))
            return new JBossQueue(getName(reference));
         if (className.equals(JBossTopic.class.getName()))
            return new JBossTopic(getName(reference));
      }
      catch (Exception ignored)
      {
      }
      return null;
   }

   // Protected ------------------------------------------------------

   /**
    * Get the name from the reference
    * 
    * @param reference the reference
    * @return the name
    */
   protected String getName(Reference reference)
   {
      return (String) reference.get("Name").getContent();
   }
   
   // Package Private ------------------------------------------------

   // Private --------------------------------------------------------

   // Inner Classes --------------------------------------------------

}
