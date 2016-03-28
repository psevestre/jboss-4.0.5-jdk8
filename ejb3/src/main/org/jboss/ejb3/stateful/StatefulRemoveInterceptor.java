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
package org.jboss.ejb3.stateful;

import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.SystemException;
import javax.transaction.RollbackException;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.ejb3.tx.TxUtil;
import org.jboss.logging.Logger;
import org.jboss.tm.TxUtils;

/**
 * Handles @Remove on a Stateful bean.
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public class StatefulRemoveInterceptor implements Interceptor
{
   private static final Logger log = Logger.getLogger(StatefulRemoveInterceptor.class);
   protected boolean retainIfException;

   public StatefulRemoveInterceptor(boolean removeOnException)
   {
      this.retainIfException = removeOnException;
   }

   public String getName()
   {
      return this.getClass().getName();
   }

   private static class RemoveSynchronization implements Synchronization
   {
      private StatefulContainer container;
      private Object id;

      public RemoveSynchronization(StatefulContainer container, Object id)
      {
         this.container = container;
         this.id = id;
      }


      public void beforeCompletion()
      {
      }

      public void afterCompletion(int status)
      {
         container.getCache().remove(id);
      }
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      Object rtn = null;
      try
      {
         rtn = invocation.invokeNext();
      }
      catch (Throwable t)
      {
         if (!retainIfException)
         {
            removeSession(invocation);
         }
         throw t;
      }
      removeSession(invocation);
      return rtn;
   }

   protected void removeSession(Invocation invocation)
   {
      StatefulContainerInvocation ejb = (StatefulContainerInvocation) invocation;
      Object id = ejb.getId();
      StatefulContainer container = (StatefulContainer) ejb.getAdvisor();
      Transaction tx = null;
      try
      {
         tx = TxUtil.getTransactionManager().getTransaction();
      }
      catch (SystemException e)
      {
         throw new RuntimeException(e);
      }
      if (tx != null && TxUtils.isActive(tx))
      {
         try
         {
            tx.registerSynchronization(new RemoveSynchronization(container, id));
         }
         catch (RollbackException e)
         {
            throw new RuntimeException(e);
         }
         catch (SystemException e)
         {
            throw new RuntimeException(e);
         }
      }
      else
      {
         // Release it properly.
         StatefulBeanContext ctx = container.getCache().get(id);
         container.getCache().remove(id);
      }
   }
}
