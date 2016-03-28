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
package org.jboss.mq;

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;

/**
 * This class implements javax.jms.TemporaryQueue
 *
 * @author Norbert Lataille (Norbert.Lataille@m4x.org)
 * @author Hiram Chirino (Cojonudo14@hotmail.com)
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 57198 $
 */
public class SpyTemporaryQueue extends SpyQueue implements TemporaryQueue
{
   // Constants -----------------------------------------------------

   /** The serialVersionUID */
   static final long serialVersionUID = 1216493383395688001L;
   
   // Attributes ----------------------------------------------------
   
   /** The DistributedConnection of its creator */
   ConnectionToken dc;

   /** Connection to the creator used from the client side */
   private transient Connection con = null;
   
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------

   /**
    * Create a new SpyTemporaryQueue
    *
    * @param queueName the queue name
    * @param dc_ the connection token
    */
   public SpyTemporaryQueue(String queueName, ConnectionToken dc_)
   {
      super(queueName);
      dc = dc_;
   }
   
   // Public --------------------------------------------------------

   /**
    * Client-side temporary queues need a reference to the connection that
    * created them in case delete() is called.
    * 
    * @param con the connection
    */
   public void setConnection(Connection con)
   {
      this.con = con;
   }
   
   // TemporaryQueue implementation ---------------------------------

   public void delete() throws JMSException
   {
      try
      {
         con.deleteTemporaryDestination(this);
      }
      catch (Exception e)
      {
         throw new SpyJMSException("Cannot delete the TemporaryQueue", e);
      }
   }
   
   // Package protected ---------------------------------------------
   
   // Protected -----------------------------------------------------
   
   // Private -------------------------------------------------------
   
   // Inner classes -------------------------------------------------
}