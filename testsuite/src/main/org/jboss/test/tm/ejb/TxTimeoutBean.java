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
package org.jboss.test.tm.ejb;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.TransactionManager;

import org.jboss.test.util.ejb.SessionSupport;

public class TxTimeoutBean extends SessionSupport
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -6750278789142406435L;

   public void ejbCreate()
   {
   }
   
   /**
    * The harness should have set the default timeout to 10 secs
    */
   public void testDefaultTimeout()
   {
      try
      {
         Thread.sleep(12000);
      }
      catch (Exception ignored)
      {
         log.debug("Ignored", ignored);
      }
      if (getTxStatus() != Status.STATUS_MARKED_ROLLBACK)
         throw new EJBException("Should be marked rolled back " + getTxStatus());
   }

   /**
    * This method's timeout should be 5 secs
    */
   public void testOverriddenTimeoutExpires()
   {
      try
      {
         Thread.sleep(7000);
      }
      catch (Exception ignored)
      {
         log.debug("Ignored", ignored);
      }
      if (getTxStatus() != Status.STATUS_MARKED_ROLLBACK)
         throw new EJBException("Should be marked rolled back " + getTxStatus());
   }

   /**
    * This method's timeout should be 20 secs
    */
   public void testOverriddenTimeoutDoesNotExpire()
   {
      try
      {
         Thread.sleep(12000);
      }
      catch (Exception e)
      {
         throw new EJBException(e);
      }
      if (getTxStatus() != Status.STATUS_ACTIVE)
         throw new EJBException("Should be active " + getTxStatus());
   }
   
   private int getTxStatus()
   {
      try
      {
         InitialContext ctx = new InitialContext();
         TransactionManager tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
         return tm.getStatus();
      }
      catch (Exception e)
      {
         throw new EJBException(e);
      }
   }
}
