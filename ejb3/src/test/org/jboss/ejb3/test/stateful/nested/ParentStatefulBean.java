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
package org.jboss.ejb3.test.stateful.nested;

import org.jboss.annotation.ejb.cache.simple.CacheConfig;
import org.jboss.logging.Logger;

import javax.ejb.*;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 * Parent SFSB that contains nested SFSB
 *
 * @author Ben Wang
 * @version $Revision: 45473 $
 */
@Stateful(name="testParentStateful")
@CacheConfig(maxSize=1000, idleTimeoutSeconds=3)   // this will get evicted the second time eviction thread wakes up
@Remote(ParentStatefulRemote.class)
public class ParentStatefulBean implements java.io.Serializable, ParentStatefulRemote
{
   private Logger log = Logger.getLogger(ParentStatefulBean.class);
   private int counter = 0;

   @EJB
   private NestedStateful nested;

   public int increment()
   {
      counter = nested.increment();

      log.debug("INCREMENT - counter: " + counter);
      return counter;
   }

   public static int postActivateCalled = 0;
   public static int prePassivateCalled = 0;

   /**
    * Sleep to test
    * @throws Exception
    */
   public void longRunning() throws Exception
   {
      log.debug("+++ longRunning() enter ");
      Thread.sleep(10000);
      log.debug("+++ longRunning() leave ");
   }

   public int getPostActivate()
   {
      return ParentStatefulBean.postActivateCalled;
   }

   public int getPrePassivate()
   {
      return ParentStatefulBean.prePassivateCalled;
   }

   public void reset()
   {
      counter = 0;
      ParentStatefulBean.postActivateCalled = 0;
      ParentStatefulBean.prePassivateCalled = 0;
   }

   @PostActivate
   public void postActivate()
   {
      ++ParentStatefulBean.postActivateCalled;
      log.debug("Activate with counter: " + counter);
   }

   @PrePassivate
   public void prePassivate()
   {
      ++ParentStatefulBean.prePassivateCalled;
      log.debug("Passivate with counter: " + counter);
   }

   @Remove
   public void remove()
   {
   }

   @PostConstruct
   public void ejbCreate()
   {
   }

   // Remote Interface implementation ----------------------------------------------

}
