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
package org.jboss.test.cmp2.jbas979;


import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.transaction.TransactionManager;
import javax.transaction.SystemException;
import org.jboss.tm.TransactionManagerLocator;


/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 57211 $</tt>
 */
public abstract class ABean implements EntityBean
{
   private Object longTx;

   // CMP accessors --------------------------------------------
   /**
    * @ejb.pk-field
    * @ejb.persistent-field
    * @ejb.interface-method
    */
   public abstract Integer getId();

   public abstract void setId(Integer id);

   /**
    * @ejb.persistent-field
    * @ejb.interface-method
    */
   public abstract String getName();

  /**
   * @ejb.interface-method
   */
   public abstract void setName(String name);

   public void longTx() throws Exception
   {
      TransactionManager tm = TransactionManagerLocator.getInstance().locate();
      longTx = tm.getTransaction();
      if(longTx == null)
      {
         throw new EJBException("longTx invoked w/o a transaction!");
      }
   }

   /**
    * @throws javax.ejb.CreateException
    * @ejb.create-method
    */
   public Integer ejbCreate(Integer id, String name)
      throws CreateException
   {
      setId(id);
      setName(name);
      return null;
   }

   public void ejbPostCreate(Integer id, String name)
   {
   }

   /**
    * @param ctx The new entityContext value
    */
   public void setEntityContext(EntityContext ctx)
   {
   }

   /**
    * Unset the associated entity context.
    */
   public void unsetEntityContext()
   {
   }

   public void ejbActivate()
   {
   }

   public void ejbLoad()
   {
   }

   public void ejbPassivate()
   {
      TransactionManager tm = TransactionManagerLocator.getInstance().locate();
      try
      {
         // is it safe to check like this? could there be a race condition?
         if(longTx != null && longTx.equals(tm.getTransaction()))
         {
            JBAS979UnitTestCase.PASSIVATED_IN_AFTER_COMPLETION = true;
         }
         else
         {
            JBAS979UnitTestCase.PASSIVATED_IN_AFTER_COMPLETION = false;
         }
         JBAS979UnitTestCase.ERROR_IN_EJB_PASSIVATE = null;
      }
      catch(SystemException e)
      {
         JBAS979UnitTestCase.ERROR_IN_EJB_PASSIVATE = e;
      }
   }

   public void ejbRemove() throws RemoveException
   {
   }

   public void ejbStore()
   {
   }
}
