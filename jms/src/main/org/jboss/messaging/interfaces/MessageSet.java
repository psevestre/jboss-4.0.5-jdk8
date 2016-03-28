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
package org.jboss.messaging.interfaces;

/**
 * A message set.
 * 
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public interface MessageSet
{
   // Constants -----------------------------------------------------

   // Public --------------------------------------------------------
   
   /**
    * Add a message to the message set.
    * 
    * @param reference the message reference to add
    */
   void add(MessageReference reference);

   /**
    * Remove a message from the message set.
    * 
    * @param consumer the consumer used to accept the message
    * @return a message or null if there are no messages
    */
   MessageReference remove(Consumer consumer);

   /**
    * Lock the message set
    */
   void lock();
   
   /**
    * Unlock the message set
    */
   void unlock();
   
   /**
    * Set the consumer for out of band notifications
    * 
    * @param consumer the consumer
    */
   void setConsumer(Consumer consumer);
   
   // Inner Classes --------------------------------------------------
}
