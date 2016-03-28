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
package org.jboss.ejb3.cache.simple;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.EJBNoSuchObjectException;
import org.jboss.annotation.ejb.cache.simple.CacheConfig;
import org.jboss.annotation.ejb.cache.simple.PersistenceManager;
import org.jboss.aop.Advisor;
import org.jboss.ejb3.Container;
import org.jboss.ejb3.EJBContainer;
import org.jboss.ejb3.Pool;
import org.jboss.ejb3.cache.StatefulCache;
import org.jboss.ejb3.stateful.StatefulBeanContext;
import org.jboss.util.id.GUID;
import org.jboss.logging.Logger;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class SimpleStatefulCache implements StatefulCache
{
   private static final Logger log = Logger.getLogger(SimpleStatefulCache.class);

   private Pool pool;
   private CacheMap cacheMap;
   private int maxSize = 1000;
   private StatefulSessionPersistenceManager pm;
   private long sessionTimeout = 300; // 5 minutes
   private SessionTimeoutTask timeoutTask;
   private boolean running = true;

   private class CacheMap extends LinkedHashMap
   {
      public CacheMap()
      {
         super(maxSize, 0.75F, true);
      }

      public boolean removeEldestEntry(Map.Entry entry)
      {
         boolean removeIt = size() > maxSize;
         if (removeIt)
         {
            StatefulBeanContext centry = (StatefulBeanContext) entry.getValue();
            synchronized (centry)
            {
               if (centry.inUse)
               {
                  centry.markedForPassivation = true;
               }
               else
               {
                  passivate(centry);
               }
               // its ok to evict because bean will be passivated.
            }
         }
         return removeIt;
      }
   }

   private class SessionTimeoutTask extends Thread
   {
      public SessionTimeoutTask(String name)
      {
         super(name);
      }

      public void run()
      {
         while (running)
         {
            try
            {
               Thread.sleep(sessionTimeout * 1000);
            }
            catch (InterruptedException e)
            {
               running = false;
               return;
            }
            try
            {
               synchronized (cacheMap)
               {
                  if (!running) return;
                  Iterator it = cacheMap.entrySet().iterator();
                  long now = System.currentTimeMillis();
                  while (it.hasNext())
                  {
                     Map.Entry entry = (Map.Entry) it.next();
                     StatefulBeanContext centry = (StatefulBeanContext) entry.getValue();
                     if (now - centry.lastUsed >= sessionTimeout * 1000)
                     {
                        synchronized (centry)
                        {                     
                           if (centry.inUse)
                           {
                              centry.markedForPassivation = true;
                           }
                           else
                           {
                              passivate(centry);
                           }
                           // its ok to evict because it will be passivated. 
                           it.remove();
                        }
                     }
                  }
               }
            }
            catch (Exception ex)
            {
               log.error("problem passivation thread", ex);
            }
         }
      }
   }

   public void initialize(Container container) throws Exception
   {
      Advisor advisor = (Advisor) container;
      this.pool = container.getPool();
      cacheMap = new CacheMap();
      PersistenceManager pmConfig = (PersistenceManager) advisor.resolveAnnotation(PersistenceManager.class);
      this.pm = (StatefulSessionPersistenceManager) pmConfig.value().newInstance();
      pm.initialize(container);
      CacheConfig config = (CacheConfig) advisor.resolveAnnotation(CacheConfig.class);
      maxSize = config.maxSize();
      sessionTimeout = config.idleTimeoutSeconds();
      log.info("Initializing SimpleStatefulCache with maxSize: " +maxSize + " timeout: " +sessionTimeout +
              " for " +container.getObjectName().getCanonicalName() );
      timeoutTask = new SessionTimeoutTask("SFSB Passivation Thread - " + container.getObjectName().getCanonicalName());
   }

   public SimpleStatefulCache()
   {
   }

   public void start()
   {
      running = true;
      timeoutTask.start();
   }

   public void stop()
   {
      synchronized (cacheMap)
      {
         running = false;
         timeoutTask.interrupt();
         cacheMap.clear();
         try
         {
            pm.destroy();
         }
         catch (Exception e)
         {
            throw new RuntimeException(e);
         }
      }
   }

   protected void passivate(StatefulBeanContext ctx)
   {
      ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
      try
      {
         Thread.currentThread().setContextClassLoader(((EJBContainer) ctx.getContainer()).getClassloader());
         pm.passivateSession(ctx);
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldCl);
      }
   }

   public StatefulBeanContext create()
   {
      StatefulBeanContext ctx = null;
      try
      {
         ctx = (StatefulBeanContext) pool.get();
         synchronized (cacheMap)
         {
            cacheMap.put(ctx.getId(), ctx);
         }
         ctx.inUse = true;
         ctx.lastUsed = System.currentTimeMillis();
      }
      catch (EJBException e)
      {
         e.printStackTrace();
         throw e;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new EJBException(e);
      }
      return ctx;
   }

   public StatefulBeanContext create(Class[] initTypes, Object[] initValues)
   {
      StatefulBeanContext ctx = null;
      try
      {
         ctx = (StatefulBeanContext) pool.get(initTypes, initValues);
         synchronized (cacheMap)
         {
            cacheMap.put(ctx.getId(), ctx);
         }
         ctx.inUse = true;
         ctx.lastUsed = System.currentTimeMillis();
      }
      catch (EJBException e)
      {
         e.printStackTrace();
         throw e;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new EJBException(e);
      }
      return ctx;
   }

   public StatefulBeanContext get(Object key) throws EJBException
   {
      StatefulBeanContext entry = null;
      synchronized (cacheMap)
      {
         entry = (StatefulBeanContext) cacheMap.get(key);
      }
      if (entry == null)
      {
         Object bean = pm.activateSession(key);
         if (bean == null)
         {
            throw new EJBNoSuchObjectException("Could not find Stateful bean: " + key);
         }
         entry = (StatefulBeanContext) bean;
         synchronized (cacheMap)
         {
            cacheMap.put(key, entry);
         }
      }
      entry.inUse = true;
      entry.lastUsed = System.currentTimeMillis();
      return entry;
   }

   public void finished(StatefulBeanContext ctx)
   {
      synchronized (ctx)
      {
         ctx.inUse = false;
         ctx.lastUsed = System.currentTimeMillis();
         if (ctx.markedForPassivation)
         {
            passivate(ctx);
         }
      }
   }

   public void remove(Object key)
   {
      StatefulBeanContext ctx = null;
      synchronized (cacheMap)
      {
         ctx = (StatefulBeanContext) cacheMap.remove(key);
      }
      if (ctx != null) pool.remove(ctx);
   }


}
