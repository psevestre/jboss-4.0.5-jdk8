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

import org.jboss.aop.metadata.SimpleMetaData;
import org.jboss.ejb3.BaseContext;
import org.jboss.ejb3.Container;
import org.jboss.ejb3.Ejb3Registry;
import org.jboss.ejb3.ThreadLocalStack;
import org.jboss.ejb3.interceptor.InterceptorInfo;
import org.jboss.ejb3.tx.TxUtil;
import org.jboss.serial.io.MarshalledObject;
import org.jboss.tm.TxUtils;

import javax.persistence.EntityManager;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class StatefulBeanContext extends BaseContext implements Externalizable
{
   protected Object id;

   protected boolean txSynchronized = false;

   protected boolean inInvocation = false;

   protected MarshalledObject beanMO;

   protected ReentrantLock lock = new ReentrantLock();

   protected boolean discarded;

   // this two are needed for propagated extended persistence contexts when one
   // SFSB injects another.
   public static ThreadLocalStack<StatefulBeanContext> propagatedContainedIn = new ThreadLocalStack<StatefulBeanContext>();

   public static ThreadLocalStack<StatefulBeanContext> currentBean = new ThreadLocalStack<StatefulBeanContext>();

   protected StatefulBeanContext containedIn;

   protected List<StatefulBeanContext> contains;

   protected HashMap<String, EntityManager> persistenceContexts;

   protected boolean removed;

   protected String containerName;

   public StatefulBeanContext()

   {

   }

   public List<StatefulBeanContext> getContains()
   {
      if (bean == null)
         extractBeanAndInterceptors();
      return contains;
   }

   public EntityManager getExtendedPersistenceContext(String id)
   {
      EntityManager found = null;
      Map<String, EntityManager> extendedPCS = getExtendedPersistenceContexts();
      if (extendedPCS != null)
      {
         found = extendedPCS.get(id);
      }
      if (found != null)
         return found;
      if (containedIn != null)
      {
         found = containedIn.getExtendedPersistenceContext(id);
      }
      return found;
   }

   public void addExtendedPersistenceContext(String id, EntityManager pc)
   {
      Map<String, EntityManager> extendedPCS = getExtendedPersistenceContexts();
      if (extendedPCS == null)
      {
         extendedPCS = persistenceContexts = new HashMap<String, EntityManager>();
      }
      extendedPCS.put(id, pc);
   }

   public Map<String, EntityManager> getExtendedPersistenceContexts()
   {
      if (persistenceContexts == null)
      {
         if (bean == null)
            getInstance(); // unmarshall
      }
      return persistenceContexts;
   }

   public StatefulBeanContext getContainedIn()
   {
      return containedIn;
   }

   public void addContains(StatefulBeanContext ctx)
   {
      if (contains == null)
         contains = new ArrayList<StatefulBeanContext>();
      contains.add(ctx);
      ctx.containedIn = this;
   }

   public StatefulBeanContext pushContainedIn()
   {
      StatefulBeanContext thisPtr = this;
      if (propagatedContainedIn.getList() != null)
      {
         // if this is a nested stateful bean, within another nested stateful
         // bean
         // we need to create a nested bean context. The nested one will be put
         // in the
         // parents list and owned by it. It is a special class because we do
         // not want
         // to put its state in a marshalled object as we want to maintain
         // object references
         // We also do not want to put the nested context within its containers
         // cache
         // instead, we return a proxy to it that will be stored in its
         // containers cache
         containedIn = propagatedContainedIn.get();
         NestedStatefulBeanContext nested = new NestedStatefulBeanContext();
         nested.id = id;
         nested.container = getContainer();
         nested.containerName = containerName;
         nested.bean = bean;
         containedIn.addContains(nested);
         thisPtr = new ProxiedStatefulBeanContext(nested);
      }
      propagatedContainedIn.push(thisPtr);
      return thisPtr;
   }

   public void prePassivate()
   {
      getContainer().invokePrePassivate(this);
   }

   public void postActivate()
   {
      getContainer().invokePostActivate(this); // handled in getInstance()
   }

   public void popContainedIn()
   {
      propagatedContainedIn.pop();
   }

   public boolean isDiscarded()
   {
      return discarded;
   }

   public void setDiscarded(boolean discarded)
   {
      this.discarded = discarded;
   }

   public ReentrantLock getLock()
   {
      return lock;
   }

   public boolean isInInvocation()
   {
      return inInvocation;
   }

   public void setInInvocation(boolean inInvocation)
   {
      this.inInvocation = inInvocation;
   }

   public Object getId()
   {
      return id;
   }

   public void setId(Object id)
   {
      this.id = id;
   }

   public boolean isTxSynchronized()
   {
      return txSynchronized;
   }

   public void setTxSynchronized(boolean txSynchronized)
   {
      this.txSynchronized = txSynchronized;
   }

   public void remove()
   {
      if (removed)
         return;
      removed = true;
      if (contains != null)
      {
         for (StatefulBeanContext contained : contains)
         {
            ((StatefulContainer) contained.getContainer()).getCache().remove(
                    contained.getId());
         }
      }
      try
      {
         Transaction tx = TxUtil.getTransactionManager().getTransaction();
         if (tx != null && TxUtils.isActive(tx))
         {
            tx.registerSynchronization(new Synchronization()
            {
               public void beforeCompletion()
               {
               }

               public void afterCompletion(int status)
               {
                  closeExtendedPCs();
               }
            });
         }
         else
         {
            closeExtendedPCs();
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   private void closeExtendedPCs()
   {
      Map<String, EntityManager> extendedPCS = getExtendedPersistenceContexts();
      if (extendedPCS != null)
      {
         for (EntityManager pc : extendedPCS.values())
         {
            pc.close();
         }
      }
   }

   public void setContainer(Container container)
   {
      super.setContainer(container);
      containerName = container.getObjectName().getCanonicalName();
   }

   public Container getContainer()
   {
      if (container == null)
      {
         container = Ejb3Registry.getContainer(containerName);
      }
      return container;
   }

   @Override
   public Object getInstance()
   {
      if (bean == null)
      {
         extractBeanAndInterceptors();
         // getContainer().invokePostActivate(this);
      }
      return bean;
   }

   @Override
   public SimpleMetaData getMetaData()
   {
      return super.getMetaData();
   }

   // these are public for fast concurrent access/update
   public volatile boolean markedForPassivation = false;

   public volatile boolean inUse = false;

   public volatile long lastUsed = System.currentTimeMillis();

   @Override
   public Object[] getInterceptorInstances(InterceptorInfo[] interceptorInfos)
   {
      if (bean == null)
      {
         extractBeanAndInterceptors();
      }
      return super.getInterceptorInstances(interceptorInfos);
   }

   protected void extractBeanAndInterceptors()
   {
      try
      {
         Object[] beanAndInterceptors = (Object[]) beanMO.get();
         bean = beanAndInterceptors[0];
         persistenceContexts = (HashMap<String, EntityManager>) beanAndInterceptors[1];
         ArrayList list = (ArrayList) beanAndInterceptors[2];
         interceptorInstances = new HashMap<Class, Object>();
         if (list != null)
         {
            for (Object o : list)
            {
               interceptorInstances.put(o.getClass(), o);
            }
         }
         contains = (List<StatefulBeanContext>) beanAndInterceptors[3];
/* We should let pm to handle this.
         if (contains != null)
         {
            for (StatefulBeanContext ctx : contains)
            {
               ctx.getContainer().invokePostActivate(ctx);
            }
         }
*/
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      out.writeUTF(containerName);
      out.writeObject(id);
      out.writeObject(metadata);
      if (bean == null)
      {
         out.writeObject(beanMO);
      }
      else
      {
         Object[] beanAndInterceptors = new Object[4];
         beanAndInterceptors[0] = bean;
         beanAndInterceptors[1] = persistenceContexts;
         if (interceptorInstances != null && interceptorInstances.size() > 0)
         {
            ArrayList list = new ArrayList();
            list.addAll(interceptorInstances.values());
            beanAndInterceptors[2] = list;
         }
         beanAndInterceptors[3] = contains;
/* Since replication also uses this. We c'ant call this directly. Let pm handle this.
         if (contains != null)
         {
            for (StatefulBeanContext ctx : contains)
            {
               ctx.prePassivate();
            }
         }
*/
         beanMO = new MarshalledObject(beanAndInterceptors);
         out.writeObject(beanMO);
      }
   }

   public void readExternal(ObjectInput in) throws IOException,
           ClassNotFoundException
   {
      containerName = in.readUTF();
      id = in.readObject();
      metadata = (SimpleMetaData) in.readObject();
      beanMO = (MarshalledObject) in.readObject();
   }

   public Object getInvokedMethodKey()
   {
      return this.getId();
   }
}
