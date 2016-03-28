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
package org.jboss.ejb3.test.clusteredsession;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

import org.jboss.annotation.ejb.Clustered;
import org.jboss.annotation.ejb.cache.tree.CacheConfig;
import org.jboss.system.server.ServerConfig;

import java.rmi.dgc.VMID;
import org.jboss.logging.Logger;

/**
 * SFSB interface
 *
 * @author Ben Wang
 * @version $Revision: 57207 $
 */
@Stateful(name="testStateful")
@Clustered
// Mimic explict failover
@Interceptors({ExplicitFailoverInterceptor.class})
@CacheConfig(maxSize=1000, idleTimeoutSeconds=3)   // this will get evicted the second time eviction thread wakes up
@Remote(StatefulRemote.class)
public class StatefulBean implements java.io.Serializable, StatefulRemote
{
   private Logger log = Logger.getLogger(StatefulBean.class);
   private int counter = 0;
   private String state;
   public transient VMID myId = null;
   public String name;

   public int increment()
   {
      System.out.println("INCREMENT - counter: " + (counter++));
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
      Thread.sleep(20000); // 20000 will break the passivation test now.
      log.debug("+++ longRunning() leave ");
   }

   public int getPostActivate()
   {
      return postActivateCalled;
   }

   public int getPrePassivate()
   {
      return prePassivateCalled;
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
      postActivateCalled = 0;
      prePassivateCalled = 0;
   }

   public void resetActivationCounter() {
      postActivateCalled = 0;
      prePassivateCalled = 0;
   }

   @PostActivate
   public void postActivate()
   {
      ++postActivateCalled;
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
      ++prePassivateCalled;
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

   public NodeAnswer getNodeState()
   {
      if (this.myId == null)
      {
         //it is a failover: we need to assign ourself an id because of transient nature
         this.myId = new VMID();
      }

      NodeAnswer state = new NodeAnswer(this.myId, this.name);
      log.debug("getNodeState, " + state);
      return state;
   }

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
