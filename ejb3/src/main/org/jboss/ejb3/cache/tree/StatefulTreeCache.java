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
package org.jboss.ejb3.cache.tree;

import javax.ejb.EJBException;
import javax.ejb.EJBNoSuchObjectException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.aop.Advisor;
import org.jboss.cache.xml.XmlHelper;
import org.jboss.cache.eviction.RegionManager;
import org.jboss.cache.eviction.Region;
import org.jboss.cache.CacheException;
import org.jboss.cache.AbstractTreeCacheListener;
import org.jboss.cache.Node;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.ejb3.Container;
import org.jboss.ejb3.Pool;
import org.jboss.ejb3.cache.ClusteredStatefulCache;
import org.jboss.ejb3.stateful.StatefulBeanContext;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.logging.Logger;
import org.jboss.annotation.ejb.cache.tree.CacheConfig;
import org.w3c.dom.Element;

import org.jboss.cache.Fqn;
import org.jboss.cache.config.Option;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class StatefulTreeCache implements ClusteredStatefulCache
{
   protected static Logger log = Logger.getLogger(StatefulTreeCache.class);
   static int FQN_SIZE = 2; // depth of fqn that we store the session in.

   private Pool pool;
   private TreeCache cache;
   private Fqn cacheNode;
   private ClusteredStatefulCacheListener listener;
   private RegionManager evictRegionManager;
   public static long MarkInUseWaitTime = 15000;

   public StatefulBeanContext create()
   {
      StatefulBeanContext ctx = null;
      try
      {
         ctx = (StatefulBeanContext) pool.get();
         cache.put(cacheNode + "/" + ctx.getId(), "bean", ctx);
         ctx.inUse = true;
         ctx.lastUsed = System.currentTimeMillis();
      }
      catch (EJBException e)
      {
         throw e;
      }
      catch (Exception e)
      {
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
         Fqn id = Fqn.fromString(cacheNode + "/" + ctx.getId());
         cache.put(id, "bean", ctx);
         ctx.inUse = true;
         ctx.lastUsed = System.currentTimeMillis();
      }
      catch (EJBException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new EJBException(e);
      }
      return ctx;
   }

   public StatefulBeanContext get(Object key) throws EJBException
   {
      StatefulBeanContext entry = null;
      Fqn id = Fqn.fromString(cacheNode + "/" +key);
      try
      {
         Object obj = cache.get(id, "bean");
         entry = (StatefulBeanContext) obj;
      }
      catch (CacheException e)
      {
         throw new RuntimeException(e);
      }
      if (entry == null)
      {
         throw new EJBNoSuchObjectException("Could not find Stateful bean: " + key);
      }
      entry.inUse = true;
      // Mark it to eviction thread that don't passivate it yet.
      evictRegionManager.markNodeCurrentlyInUse(id, MarkInUseWaitTime);
      if(log.isDebugEnabled())
      {
         log.debug("get: retrieved bean with cache id " +id.toString());
      }

      entry.lastUsed = System.currentTimeMillis();
      return entry;
   }

   public void remove(Object key)
   {
      StatefulBeanContext ctx = null;
      Fqn id = Fqn.fromString(cacheNode + "/" +key);
      try
      {
         if(log.isDebugEnabled())
         {
            log.debug("remove: cache id " +id.toString());
         }
         cache.remove(id);
      }
      catch (CacheException e)
      {
         throw new RuntimeException(e);
      }
//      if (ctx != null) pool.remove(ctx);
   }

   public void finished(StatefulBeanContext ctx)
   {
      synchronized (ctx)
      {
         ctx.inUse = false;
         ctx.lastUsed = System.currentTimeMillis();
         Fqn id = Fqn.fromString(cacheNode + "/" + ctx.getId());
         // OK, it is free to passivate now.
         evictRegionManager.unmarkNodeCurrentlyInUse(id);
      }
   }

   public void replicate(StatefulBeanContext ctx)
   {
      try
      {
         cache.put(cacheNode + "/" + ctx.getId(), "bean", ctx);
      }
      catch (CacheException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void initialize(Container container) throws Exception
   {
      Advisor advisor = (Advisor) container;
      this.pool = container.getPool();
      CacheConfig config = (CacheConfig) advisor.resolveAnnotation(CacheConfig.class);
      MBeanServer server = MBeanServerLocator.locateJBoss();
      ObjectName cacheON = new ObjectName(config.name());
      TreeCacheMBean mbean = (TreeCacheMBean) MBeanProxyExt.create(TreeCacheMBean.class, cacheON, server);
      cache = (TreeCache) mbean.getInstance();

      cacheNode = Fqn.fromString("/" + container.getEjbName() + "/");

      // Try to create an eviction region per ejb
      evictRegionManager = cache.getEvictionRegionManager();
      Element element = getElementConfig(cacheNode.toString(), config.idleTimeoutSeconds(),
              config.maxSize());
      Region region = evictRegionManager.createRegion(cacheNode, element);
      log.debug("initialize(): create eviction region: " +region + " for ejb: " +container.getEjbName());
   }

   protected Element getElementConfig(String regionName, long timeToLiveSeconds, int maxNodes) throws Exception {
      String xml = "<region name=\"" +regionName +"\" policyClass=\"org.jboss.cache.eviction.LRUPolicy\">\n" +
               "<attribute name=\"maxNodes\">" +maxNodes +"</attribute>\n" +
               "<attribute name=\"timeToLiveSeconds\">"+ timeToLiveSeconds +"</attribute>\n" +
               "</region>";
      return XmlHelper.stringToElement(xml);
   }

   public void start()
   {
      // register to listen for cache event
      // TODO this approach may not be scalable when there are many beans since then we will need to go thru
      // N listeners to figure out which this event this belongs to.
      listener = new ClusteredStatefulCacheListener();
      cache.addTreeCacheListener(listener);
   }

   public void stop()
   {
      // Remove the listener
      cache.removeTreeCacheListener(listener);

      Option opt = new Option();
      opt.setCacheModeLocal(true);
      try {
         // remove locally.
         cache.remove(cacheNode, opt);
      } catch (CacheException e) {
         log.error("Stop(): can't remove bean from the underlying distributed cache");
      }

      // Remove the eviction region
      RegionManager rm = cache.getEvictionRegionManager();
      rm.removeRegion(cacheNode);

      log.info("stop(): StatefulTreeCache stopped successfully for " +cacheNode);
   }

   /**
    * A TreeCacheListener. Note that extends it from AbstractTreeCacheListener is a bit heavy since
    * it will get all the treecache events (instead of just passivation/activation). But we will have to
    * wait untill JBossCache2.0 for the refactoring then.
    */
   public class ClusteredStatefulCacheListener extends AbstractTreeCacheListener
   {
      protected Logger log = Logger.getLogger(ClusteredStatefulCacheListener.class);

      public void nodeActivate(Fqn fqn, boolean pre) {
         if(pre) return;  // we are not interested in postActivate event
         if(fqn.size() != FQN_SIZE) return;
         if(!fqn.isChildOrEquals(cacheNode)) return;  // don't care about fqn that doesn't belong to me.

         StatefulBeanContext bean = null;
         try {
            // TODO Can this cause deadlock in the cache level? Should be ok but need review.
            bean = (StatefulBeanContext) cache.get(fqn, "bean");
         } catch (CacheException e) {
            log.error("nodeActivate(): can't retrieve bean instance from: " +fqn + " with exception: " +e);
            return;
         }
         if(bean == null)
         {
            throw new IllegalStateException("StatefuleTreeCache.nodeActivate(): null bean instance.");
         }

//         log.debug("nodeActivate(): send postActivate event on fqn: " +fqn);
         if(log.isTraceEnabled())
         {
            log.trace("nodeActivate(): send postActivate event on fqn: " +fqn);
         }

         bean.postActivate();
      }

      public void nodePassivate(Fqn fqn, boolean pre) {
         if(!pre) return;  // we are not interested in postPassivate event
         if(fqn.size() != FQN_SIZE) return;
         if(!fqn.isChildOrEquals(cacheNode)) return;  // don't care about fqn that doesn't belong to me.

         try {
            // TODO Can this cause deadlock in the cache level? Should be ok but need review.
            Node node = cache.get(fqn);
            StatefulBeanContext bean = (StatefulBeanContext) node.getData().get("bean");
            if (bean != null)
               bean.prePassivate();

         } catch (CacheException e) {
            log.error("nodePassivate(): can't retrieve bean instance from: " +fqn + " with exception: " +e);
            return;
         }

//         log.debug("nodePassivate(): send prePassivate event on fqn: " +fqn);
         if(log.isTraceEnabled())
         {
            log.trace("nodePassivate(): send prePassivate event on fqn: " +fqn);
         }

      }
   }
}
