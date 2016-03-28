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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import javax.ejb.EJBContext;
import javax.persistence.EntityManager;
import org.jboss.aop.metadata.SimpleMetaData;
import org.jboss.ejb3.Container;
import org.jboss.ejb3.interceptor.InterceptorInfo;

/**
 * Comment
 * 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class ProxiedStatefulBeanContext extends StatefulBeanContext implements
      Externalizable
{
   private transient StatefulBeanContext delegate;

   private Object oid;

   private String containerId;
   
   private StatefulBeanContextReference parentRef;

   public ProxiedStatefulBeanContext(StatefulBeanContext delegate)
   {
      this.delegate = delegate;
      oid = delegate.getId();
      containerId = delegate.getContainer().getObjectName().getCanonicalName();
   }

   public ProxiedStatefulBeanContext()
   {
   }

   protected StatefulBeanContext getDelegate()
   {
      if (delegate == null)
      {
         for (StatefulBeanContext ctx : parentRef.getBeanContext()
               .getContains())
         {
            Object matchingOid = ctx.getId();
            if (oid.equals(matchingOid)
                  && ctx.getContainer().getObjectName().getCanonicalName()
                        .equals(containerId))
            {
               delegate = ctx;
               break;
            }
         }
         if (delegate == null)
            throw new RuntimeException("Failed to read delegate");
      }
      return delegate;
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      out.writeObject(oid);
      out.writeUTF(containerId);
      if(parentRef == null)
      {
         parentRef = new StatefulBeanContextReference(getDelegate()
            .getContainedIn());
      }
      out.writeObject(parentRef);
   }

   public void readExternal(ObjectInput in) throws IOException,
         ClassNotFoundException
   {
      oid = in.readObject();
      containerId = in.readUTF();
      parentRef = (StatefulBeanContextReference) in.readObject();
   }

//   public void prePassivate()
//   {
//   }

//   public void postActivate()
//   {
//   }

   public List<StatefulBeanContext> getContains()
   {
      return getDelegate().getContains();
   }

   public EntityManager getExtendedPersistenceContext(String id)
   {
      return getDelegate().getExtendedPersistenceContext(id);
   }

   public void addExtendedPersistenceContext(String id, EntityManager pc)
   {
      getDelegate().addExtendedPersistenceContext(id, pc);
   }

   public Map<String, EntityManager> getExtendedPersistenceContexts()
   {
      return getDelegate().getExtendedPersistenceContexts();
   }

   public StatefulBeanContext getContainedIn()
   {
      return getDelegate().getContainedIn();
   }

   public void addContains(StatefulBeanContext ctx)
   {
      getDelegate().addContains(ctx);
   }

   public StatefulBeanContext pushContainedIn()
   {
      return getDelegate().pushContainedIn();
   }

   public void popContainedIn()
   {
      getDelegate().popContainedIn();
   }

   public boolean isDiscarded()
   {
      return getDelegate().isDiscarded();
   }

   public void setDiscarded(boolean discarded)
   {
      getDelegate().setDiscarded(discarded);
   }

   public ReentrantLock getLock()
   {
      return getDelegate().getLock();
   }

   public boolean isInInvocation()
   {
      return getDelegate().isInInvocation();
   }

   public void setInInvocation(boolean inInvocation)
   {
      getDelegate().setInInvocation(inInvocation);
   }

   public Object getId()
   {
      return getDelegate().getId();
   }

   public void setId(Object id)
   {
      this.oid = id;
      getDelegate().setId(id);
   }

   public boolean isTxSynchronized()
   {
      return getDelegate().isTxSynchronized();
   }

   public void setTxSynchronized(boolean txSynchronized)
   {
      getDelegate().setTxSynchronized(txSynchronized);
   }

   public void remove()
   {
      getDelegate().remove();
   }

   public void setContainer(Container container)
   {
      getDelegate().setContainer(container);
   }

   public Container getContainer()
   {
      return getDelegate().getContainer();
   }

   public Object getInstance()
   {
      return getDelegate().getInstance();
   }

   public SimpleMetaData getMetaData()
   {
      return getDelegate().getMetaData();
   }

   public Object[] getInterceptorInstances(InterceptorInfo[] interceptorInfos)
   {
      return getDelegate().getInterceptorInstances(interceptorInfos);
   }

   public void extractBeanAndInterceptors()
   {
      getDelegate().extractBeanAndInterceptors();
   }

   public void setInstance(Object instance)
   {
      getDelegate().setInstance(instance);
   }

   public void initialiseInterceptorInstances()
   {
      getDelegate().initialiseInterceptorInstances();
   }

   public EJBContext getEJBContext()
   {
      return getDelegate().getEJBContext();
   }

}
