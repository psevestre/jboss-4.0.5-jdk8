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
package org.jboss.ejb3.test.cache.nested;

import org.jboss.logging.Logger;
import org.jboss.system.server.ServerConfig;

import javax.interceptor.Interceptors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Remote;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.EJBException;
import java.rmi.dgc.VMID;

/**
 * SFSB with nested bean
 *
 * @author Ben Wang
 * @version $Revision: 57207 $
 */
@Stateful(name="testParentStateful")
@org.jboss.annotation.ejb.cache.simple.CacheConfig(maxSize = 1000, idleTimeoutSeconds = 1)
@Remote(ParentStatefulRemote.class)
public class ParentStatefulBean implements java.io.Serializable, ParentStatefulRemote
{
   private static Logger log = Logger.getLogger(ParentStatefulBean.class);
   private int counter = 0;
   private String state;
   public transient VMID myId = null;
   public String name;

   @EJB
   private NestedStateful nested;

   public int increment()
   {
      counter = nested.increment();

      log.debug("INCREMENT - parent counter: " + counter);
      return counter;
   }

   public String getHostAddress()
   {
      return System.getProperty(ServerConfig.SERVER_BIND_ADDRESS);
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

   public int getNestedPostActivate()
   {
      return NestedStatefulBean.postActivateCalled;
   }

   public int getNestedPrePassivate()
   {
      return NestedStatefulBean.prePassivateCalled;
   }

   public void setState(String state)
   {
      this.state = state;
   }

   public String getState()
   {
      log.debug("getState(): entering ...");
      return this.state;
   }

   public void reset()
   {
      state = null;
      counter = 0;
      ParentStatefulBean.postActivateCalled = 0;
      ParentStatefulBean.prePassivateCalled = 0;
   }

   public void resetActivationCounter()
   {
      ParentStatefulBean.postActivateCalled = 0;
      ParentStatefulBean.prePassivateCalled = 0;
      NestedStatefulBean.postActivateCalled = 0;
      NestedStatefulBean.prePassivateCalled = 0;
   }

   @PostActivate
   public void postActivate()
   {
      ++ParentStatefulBean.postActivateCalled;
      if (this.myId == null)
      {
         //it is a failover: we need to assign ourself an id
         this.myId = new VMID();
      }
      log.debug("Activate. My ID: " + this.myId + " name: " + this.name);
   }

   @PrePassivate
   public void prePassivate()
   {
      ++ParentStatefulBean.prePassivateCalled;
      log.debug("Passivate. My ID: " + this.myId + " name: " + this.name);
   }

   @Remove
   public void remove()
   {
   }

   @PostConstruct
   public void ejbCreate()
   {
      this.myId = new VMID();
      log.debug("My ID: " + this.myId);
   }

   // Remote Interface implementation ----------------------------------------------

   public void setName(String name)
   {
      this.name = name;
      log.debug("Name set to " + name);
   }

   public void setNameOnlyOnNode(String name, VMID node)
   {
      if (node.equals(this.myId))
         this.setName(name);
      else
         throw new EJBException("Trying to assign value on node " + this.myId + " but this node expected: " + node);
   }

   public void setUpFailover(String failover) {
      // To setup the failover property
      log.debug("Setting up failover property: " +failover);
      System.setProperty ("JBossCluster-DoFail", failover);
   }
}
