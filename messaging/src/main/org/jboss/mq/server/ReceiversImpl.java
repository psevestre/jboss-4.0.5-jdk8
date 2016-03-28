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
package org.jboss.mq.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.jboss.mq.Subscription;

/**
 * The default implementation of the Receivers interface.
 *
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 57198 $
 */
public class ReceiversImpl implements Receivers
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   /** The receivers */
   HashSet receivers = new HashSet();
   
   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   // Public --------------------------------------------------------

   // Receivers implementation --------------------------------------

   public void add(Subscription sub)
   {
      receivers.add(sub);
   }
   
   public Iterator iterator()
   {
      return receivers.iterator();
   }
   
   public ArrayList listReceivers()
   {
      return new ArrayList(receivers);
   }
   
   public void remove(Subscription sub)
   {
      receivers.remove(sub);
   }
   
   public int size()
   {
      return receivers.size();
   }
   
   // Protected -----------------------------------------------------
   
   // Package Private -----------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}
