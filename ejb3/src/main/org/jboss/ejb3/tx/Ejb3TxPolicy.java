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
package org.jboss.ejb3.tx;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.ApplicationException;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.transaction.Transaction;
import org.jboss.ejb.ApplicationExceptionImpl;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.ejb3.EJBContainer;
import org.jboss.ejb3.metamodel.AssemblyDescriptor;
import org.jboss.ejb3.stateful.StatefulContainer;
import org.jboss.ejb3.stateful.StatefulContainerInvocation;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class Ejb3TxPolicy extends org.jboss.aspects.tx.TxPolicy
{
   public void throwMandatory(Invocation invocation)
   {
      throw new EJBTransactionRequiredException(((MethodInvocation) invocation).getActualMethod().toString());
   }

   public void handleExceptionInOurTx(Invocation invocation, Throwable t, Transaction tx) throws Throwable
   {
      ApplicationException ae = getApplicationException(t.getClass(), invocation);
      if (ae != null)
      {
         if (ae.rollback()) setRollbackOnly(tx);
         throw t;
      }
      if (!(t instanceof RuntimeException || t instanceof RemoteException))
      {
         throw t;
      }
      setRollbackOnly(tx);
      if (t instanceof RuntimeException && !(t instanceof EJBException))
      {
         throw new EJBException((Exception) t);
      }
      throw t;
   }

   public void handleInCallerTx(Invocation invocation, Throwable t, Transaction tx) throws Throwable
   {
      ApplicationException ae = getApplicationException(t.getClass(), invocation);
   
      if (ae != null)
      {
         if (ae.rollback()) setRollbackOnly(tx);
         throw t;
      }
      if (!(t instanceof RuntimeException || t instanceof RemoteException))
      {
         throw t;
      }
      setRollbackOnly(tx);
      // its either a RuntimeException or RemoteException
      
      if (t instanceof EJBTransactionRolledbackException)
         throw t;
      else
         throw new EJBTransactionRolledbackException((Exception) t);
   }
   
   protected ApplicationException getApplicationException(Class exceptionClass, Invocation invocation)
   {
      MethodInvocation ejb = (MethodInvocation) invocation;
      EJBContainer container = (EJBContainer) ejb.getAdvisor();
      
      if (exceptionClass.isAnnotationPresent(ApplicationException.class))
         return (ApplicationException)exceptionClass.getAnnotation(ApplicationException.class);
      
      AssemblyDescriptor assembly = container.getAssemblyDescriptor();
      
      if (assembly != null)
      {
         List exceptions = assembly.getApplicationExceptions();
         if (exceptions.size() > 0)
         {
            Iterator exceptionIterator = exceptions.iterator();
            while (exceptionIterator.hasNext())
            {
               org.jboss.ejb3.metamodel.ApplicationException exception = (org.jboss.ejb3.metamodel.ApplicationException)exceptionIterator.next();
               if (exception.getExceptionClass().equals(exceptionClass.getName()))
                  return new ApplicationExceptionImpl(exception.getRollback());
            }
         }
         
      }
      return null;
   }
}
