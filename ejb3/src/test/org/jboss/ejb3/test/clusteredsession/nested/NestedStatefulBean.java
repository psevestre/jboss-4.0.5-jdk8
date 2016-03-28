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
package org.jboss.ejb3.test.clusteredsession.nested;

import org.jboss.annotation.ejb.Clustered;
import org.jboss.annotation.ejb.cache.tree.CacheConfig;
import org.jboss.logging.Logger;

import javax.ejb.*;

/**
 * Nested SFSB
 *
 * @author Ben Wang
 * @version $Revision: 57207 $
 */
@Stateful(name="testNestedStateful")
@Clustered
@CacheConfig(maxSize=1000, idleTimeoutSeconds=3)   // this will get evicted the second time eviction thread wakes up
public class NestedStatefulBean implements java.io.Serializable, NestedStateful
{
   private static Logger log = Logger.getLogger(NestedStatefulBean.class);
   private int counter = 0;

   public int increment()
   {
      log.debug("INCREMENT - nested counter: " + (counter++));
      return counter;
   }

   public static int postActivateCalled = 0;
   public static int prePassivateCalled = 0;

   public int getPostActivate()
   {
      return postActivateCalled;
   }

   public int getPrePassivate()
   {
      return prePassivateCalled;
   }

   public void reset()
   {
      counter = 0;
      NestedStatefulBean.postActivateCalled = 0;
      NestedStatefulBean.prePassivateCalled = 0;
   }

   @PostActivate
   public void postActivate()
   {
      ++NestedStatefulBean.postActivateCalled;
   }

   @PrePassivate
   public void prePassivate()
   {
      log.debug("prePassivate: - counter: " + prePassivateCalled);
      ++NestedStatefulBean.prePassivateCalled;
   }
}
