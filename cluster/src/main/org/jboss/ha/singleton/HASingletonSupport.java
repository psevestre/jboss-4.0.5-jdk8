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
package org.jboss.ha.singleton;

import java.util.List;

import javax.management.Notification;

import org.jboss.ha.jmx.HAServiceMBeanSupport;

/** 
 * Base class for HA-Singleton services.
 *
 * @author <a href="mailto:ivelin@apache.org">Ivelin Ivanov</a>
 * @author <a href="mailto:scott.stark@jboss.org">Scott Stark</a>
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision: 57188 $
 */
public class HASingletonSupport extends HAServiceMBeanSupport
   implements HASingletonMBean, HASingleton
{
   // Private Data --------------------------------------------------
   
   private boolean isMasterNode = false;

   // Constructors --------------------------------------------------
   
   /**
    * Default CTOR
    */
   public HASingletonSupport()
   {
      // empty
   }

   // Attributes ----------------------------------------------------
   
   /**
    * @jmx:managed-attribute
    * 
    * @return true if this cluster node has the active mbean singleton, false otherwise
    */
   public boolean isMasterNode()
   {
      return isMasterNode;
   }

   // Public --------------------------------------------------------

   /**
    * Extending classes should override this method and implement the custom
    * singleton logic. Only one node in the cluster is the active master.
    * If the current node is elected for master, this method is invoked.
    * When another node is elected for master for some reason, the
    * stopSingleton() method is invokded.
    * <p>
    * When the extending class is a stateful singleton, it will
    * usually use putDistributedState() and getDistributedState() to save in
    * the cluster environment information that will be needed by the next node
    * elected for master should the current master node fail.  
    *
    * @see HASingleton
    */
   public void startSingleton()
   {
      if (log.isDebugEnabled())
         log.debug("startSingleton() : elected for master singleton node");

      // Extending classes will implement the singleton logic here
   }

   /**
    * Extending classes should override this method and implement the custom
    * singleton logic. Only one node in the cluster is the active master.
    * If the current node is master and another node is elected for master, this
    * method is invoked.
    * 
    * @see HASingleton
    */
   public void stopSingleton()
   {
      if (log.isDebugEnabled())
         log.debug("stopSingleton() : another node in the partition (if any) is elected for master");
      
      // Extending classes will implement the singleton logic here
   }


   /**
    * When topology changes, a new master is elected based on the result
    * of the isDRMMasterReplica() call.
    * 
    * @see HAServiceMBeanSupport#partitionTopologyChanged(List, int)
    * @see  DistributedReplicantManager#isMasterReplica(String);
    */
   public void partitionTopologyChanged(List newReplicants, int newViewID)
   {
      boolean isElectedNewMaster = isDRMMasterReplica();
      
      if (log.isDebugEnabled())
      {
         log.debug("partitionTopologyChanged, isElectedNewMaster=" + isElectedNewMaster
            + ", isMasterNode=" + isMasterNode + ", viewID=" + newViewID);
      }

      // if this node is already the master, don't bother electing it again
      if (isElectedNewMaster && isMasterNode)
      {
         return;
      }
      // just becoming master
      else if (isElectedNewMaster && !isMasterNode)
      {
         makeThisNodeMaster();
      }
      // transition from master to slave
      else if (isMasterNode == true)
      {
         _stopOldMaster();
      }
   }

   /**
    * This method will be invoked twice by the local node 
    * when it stops as well as by the remote
    */
   public void _stopOldMaster()
   {
      log.debug("_stopOldMaster, isMasterNode=" + isMasterNode);
      
      try 
      {
         // since this is a cluster call, all nodes will hear it
         // so if the node is not the master, then ignore 
         if (isMasterNode == true)
         {
            isMasterNode = false;
            
            // notify stopping
            sendLocalNotification(HASINGLETON_STOPPING_NOTIFICATION);
            
            // stop the singleton
            stopSingleton();
            
            // notify stopped
            sendLocalNotification(HASINGLETON_STOPPED_NOTIFICATION);
         }
      }
      catch (Exception ex)
      {
         log.error(
            "_stopOldMaster failed. Will still try to start new master. " +
            "You need to examine the reason why the old master wouldn't stop and resolve it. " +
            "It is bad that the old singleton may still be running while we are starting a new one, " +
            "so you need to resolve this ASAP.", ex);
      }
   }

   // Protected -----------------------------------------------------
   
   protected void makeThisNodeMaster()
   {
      try
      {
         // stop the old master (if there is one) before starting the new one

         // ovidiu 09/02/04 - temporary solution for Case 1843, use an asynchronous
         // distributed call.
         //callMethodOnPartition("_stopOldMaster", new Object[0], new Class[0]);
         callAsyncMethodOnPartition("_stopOldMaster", new Object[0], new Class[0]);

         isMasterNode = true;
         
         // notify starting
         sendLocalNotification(HASINGLETON_STARTING_NOTIFICATION);

         // start new master
         startSingleton();
         
         // notify started
         sendLocalNotification(HASINGLETON_STARTED_NOTIFICATION);         
      }
      catch (Exception ex)
      {
         log.error("_stopOldMaster failed. New master singleton will not start.", ex);
      }
   }
   
   // Private -------------------------------------------------------
   
   private void sendLocalNotification(String type)
   {
      Notification n = new Notification(type, this, getNextNotificationSequenceNumber());
      super.sendNotificationToLocalListeners(n);
   }
}
