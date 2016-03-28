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
package org.jboss.tm;

import javax.transaction.InvalidTransactionException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.util.NestedRuntimeException;

/**
 * A simple helper for transaction demarcation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 57208 $
 */
public class TransactionDemarcationSupport
{
   private static final TransactionManagerLocator locator = TransactionManagerLocator.getInstance();
   
   /**
    * Suspend any transaction associated with the thread
    * 
    * @return any suspended transaction or null if there was no transaction
    */
   public static Transaction suspendAnyTransaction()
   {
      TransactionManager tm = locator.locate();
      try
      {
         return tm.suspend();
      }
      catch (SystemException e)
      {
         throw new NestedRuntimeException("Unable to suspend transaction", e);
      }
   }
   
   /**
    * Resume any transaction
    * 
    * @param tx the transaction to resume
    */
   public static void resumeAnyTransaction(Transaction tx)
   {
      // Nothing to do
      if (tx == null)
         return;
      
      TransactionManager tm = locator.locate();
      try
      {
         tm.resume(tx);
      }
      catch (InvalidTransactionException e)
      {
         throw new NestedRuntimeException("Unable to resume an invalid transaction", e);
      }
      catch (SystemException e)
      {
         throw new NestedRuntimeException("Unable to suspend transaction", e);
      }
   }
}
