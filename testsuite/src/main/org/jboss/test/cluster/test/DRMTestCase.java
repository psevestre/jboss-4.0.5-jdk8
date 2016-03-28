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
package org.jboss.test.cluster.test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.List;
import java.util.HashSet;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.Notification;

import junit.framework.Test;

import org.jboss.test.JBossClusteredTestCase;
import org.jboss.test.cluster.drm.IReplicants;
import org.jboss.test.cluster.drm.MockHAPartition;
import org.jboss.ha.framework.interfaces.ClusterNode;
import org.jboss.ha.framework.interfaces.DistributedReplicantManager;
import org.jboss.ha.framework.interfaces.DistributedReplicantManager.ReplicantListener;
import org.jboss.ha.framework.server.DistributedReplicantManagerImpl;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.jmx.adaptor.rmi.RMIAdaptorExt;
import org.jboss.jmx.adaptor.rmi.RMINotificationListener;
import org.jgroups.stack.IpAddress;
import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.Semaphore;

/** Tests of the DistributedReplicantManagerImpl
 *
 * @author  Scott.Stark@jboss.org
 * @author  Brian.Stansberry@jboss.com
 * @version $Revision: 57457 $
 */
public class DRMTestCase extends JBossClusteredTestCase
{
   static class TestListener extends UnicastRemoteObject
      implements RMINotificationListener
   {
      private Logger log;
      TestListener(Logger log) throws RemoteException
      {
         this.log = log;
      }
      public void handleNotification(Notification notification, Object handback)
         throws RemoteException
      {
         log.info("handleNotification, "+notification);
      }
   }
   
   /**
    * Thread that will first register a DRM ReplicantLister that synchronizes
    * on the test class' lock object, and then calls DRM add or remove,
    * causing the thread to block if the lock object's monitor is held.
    */
   static class BlockingListenerThread extends Thread 
      implements DistributedReplicantManager.ReplicantListener
   {
      private DistributedReplicantManagerImpl drm;
      private String nodeName;
      private boolean add;
      private boolean blocking;
      private Exception ex;
      
      BlockingListenerThread(DistributedReplicantManagerImpl drm,
                             boolean add,
                             String nodeName)
      {
         this.drm = drm;
         this.add =add;
         this.nodeName = nodeName;
         drm.registerListener("TEST", this);         
      }

      public void replicantsChanged(String key, List newReplicants, int newReplicantsViewId)
      {
         blocking = true;
         synchronized(lock)
         {
            blocking = false;
         }
      }
      
      public void run()
      {
         try
         {
            if (add)
            {
               if (nodeName == null)
                  drm.add("TEST", "local-replicant");
               else
                  drm._add("TEST", nodeName, "remote-replicant");
            }
            else 
            {
               if (nodeName == null)
                  drm.remove("TEST");
               else
                  drm._remove("TEST", nodeName);
            }
         }
         catch (Exception e)
         {
            ex = e;
         }
      }
      
      public boolean isBlocking()
      {
         return blocking;
      }
      
      public Exception getException()
      {
         return ex;
      }
      
   }
   
   /**
    * Thread that registers and then unregisters a DRM ReplicantListener.
    */
   static class RegistrationThread extends Thread
   {
      private DistributedReplicantManager drm;
      private boolean registered = false;
      private boolean unregistered = true;
      
      RegistrationThread(DistributedReplicantManager drm)
      {
         this.drm = drm;
      }
      
      public void run()
      {
         NullListener listener = new NullListener();
         drm.registerListener("DEADLOCK", listener);
         registered = true;
         drm.unregisterListener("DEADLOCK", listener);
         unregistered = true;
      }
      
      public boolean isRegistered()
      {
         return registered;
      }
      
      public boolean isUnregistered()
      {
         return unregistered;
      }
      
   }
   
   /**
    * A DRM ReplicantListener that does nothing.
    */
   static class NullListener
      implements DistributedReplicantManager.ReplicantListener
   {
      public void replicantsChanged(String key, List newReplicants, 
                                    int newReplicantsViewId)
      {
         // no-op
      }
   }
   
   /**
    * DRM ReplicantListener that mimics the HASingletonDeployer service
    * by deploying/undeploying a service if it's notified that by that DRM
    * that it is the master replica for its key.
    */
   static class MockHASingletonDeployer
      implements DistributedReplicantManager.ReplicantListener
   {
      DistributedReplicantManager drm;
      MockDeployer deployer;
      String key;
      boolean master = false;
      NullListener deploymentListener = new NullListener();
      Exception ex;
      Logger log;
      Object mutex = new Object();
      
      MockHASingletonDeployer(MockDeployer deployer, String key, Logger log)
      {
         this.drm = deployer.getDRM();
         this.deployer = deployer;
         this.key = key;
         this.log = log;
      }

      public void replicantsChanged(String key, 
                                    List newReplicants, 
                                    int newReplicantsViewId)
      {
         if (this.key.equals(key))
         {
            synchronized(mutex)
            {
               boolean nowMaster = drm.isMasterReplica(key);
               
               try
               {
                  if (!master && nowMaster) {
                     log.debug(Thread.currentThread().getName() + 
                               " Deploying " + key);
                     deployer.deploy(key + "A", key, deploymentListener);
                  }
                  else if (master && !nowMaster) {
                     log.debug(Thread.currentThread().getName() + 
                               " undeploying " + key);
                     deployer.undeploy(key + "A", deploymentListener);
                  }
                  else 
                  {
                     log.debug(Thread.currentThread().getName() + 
                               " -- no status change in " + key + 
                               " -- master = " + master);   
                  }
                  master = nowMaster;
               }
               catch (Exception e)
               {
                  e.printStackTrace();
                  if (ex == null)
                     ex = e;
               }
            }
         }         
      }
      
      public Exception getException()
      {
         return ex;
      }
      
   }
   
   /**
    * Thread the repeatedly deploys and undeploys a MockHASingletonDeployer.
    */
   static class DeployerThread extends Thread
   {
      Semaphore semaphore;
      MockDeployer deployer;
      DistributedReplicantManager.ReplicantListener listener;
      String key;
      Exception ex;
      int count = -1;
      Logger log;
      
      DeployerThread(MockDeployer deployer, 
                     String key, 
                     DistributedReplicantManager.ReplicantListener listener,
                     Semaphore semaphore,
                     Logger log)
      {
         super("Deployer " + key);
         this.deployer = deployer;
         this.listener = listener;
         this.key = key;
         this.semaphore = semaphore;
         this.log = log;
      }
      
      public void run()
      {
         boolean acquired = false;
         try
         {
            acquired = semaphore.attempt(60000);
            if (!acquired)
               throw new Exception("Cannot acquire semaphore");
            SecureRandom random = new SecureRandom();
            for (count = 0; count < LOOP_COUNT; count++)
            {
               deployer.deploy(key, "JGroups", listener);

               sleepThread(random.nextInt(50));
               deployer.undeploy(key, listener);
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
            ex = e;
         }
         finally
         {
            if (acquired)
               semaphore.release();
         }
      }
      
      public Exception getException()
      {
         return ex;
      }
      
      public int getCount()
      {
         return count;
      }
   }
   
   /**
    * Thread that mimics the JGroups up-handler thread that calls into the DRM.
    * Repeatedly and randomly calls adds or removes a replicant for a set
    * of keys. 
    */
   static class JGroupsThread extends Thread
   {
      Semaphore semaphore;
      DistributedReplicantManagerImpl drm;
      String[] keys;
      String nodeName;
      Exception ex;
      int count = -1;
      int weightFactor;
      
      JGroupsThread(DistributedReplicantManagerImpl drm, 
                    String[] keys,
                    String nodeName,
                    Semaphore semaphore)
      {
         super("JGroups");
         this.drm = drm;
         this.keys = keys;
         this.semaphore = semaphore;
         this.nodeName = nodeName;
         this.weightFactor = (int) 2.5 * keys.length;
      }
      
      public void run()
      {
         boolean acquired = false;
         try
         {
            acquired = semaphore.attempt(60000);
            if (!acquired)
               throw new Exception("Cannot acquire semaphore");
            boolean[] added = new boolean[keys.length];
            SecureRandom random = new SecureRandom();
            
            for (count = 0; count < weightFactor * LOOP_COUNT; count++)
            {
               int pos = random.nextInt(keys.length);
               if (added[pos])
               {
                  drm._remove(keys[pos], nodeName);
                  added[pos] = false;
               }
               else
               {
                  drm._add(keys[pos], nodeName, "");
                  added[pos] = true;
               }
               sleepThread(random.nextInt(30));
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
            ex = e;
         }
         finally
         {
            if (acquired)
               semaphore.release();
         }
      }
      
      public Exception getException()
      {
         return ex;
      }
      
      public int getCount()
      {
         return (count / weightFactor);
      }
      
   }
   
   /**
    * Mocks the deployer of a service that registers/unregisters DRM listeners 
    * and replicants. Only allows a single thread of execution, a la the
    * org.jboss.system.ServiceController.
    */
   static class MockDeployer
   {
      DistributedReplicantManager drm;
      
      MockDeployer(DistributedReplicantManager drm)
      {
         this.drm = drm;
      }
      
      void deploy(String key, String replicant, 
                  DistributedReplicantManager.ReplicantListener listener)
            throws Exception 
      {
         synchronized(this)
         {
            drm.registerListener(key, listener);
            drm.add(key, replicant);
            sleepThread(10);
         }
      }
      
      void undeploy(String key, 
                    DistributedReplicantManager.ReplicantListener listener)
         throws Exception 
      {
         synchronized(this)
         {
            drm.remove(key);
            drm.unregisterListener(key, listener);
            sleepThread(10);
         }
      }
      
      DistributedReplicantManager getDRM()
      {
         return drm;
      }
   }

   /** ReplicantListener that caches the list of replicants */
   static class CachingListener implements ReplicantListener
   {
      List replicants = null;
      boolean clean = true;
      
      public void replicantsChanged(String key, List newReplicants, 
                                    int newReplicantsViewId)
      {
         this.replicants = newReplicants;
         if (clean && newReplicants != null)
         {
            int last = Integer.MIN_VALUE;
            for (Iterator iter = newReplicants.iterator(); iter.hasNext(); )
            {
               int cur = ((Integer) iter.next()).intValue();
               if (last >= cur)
               {
                  clean = false;
                  break;
               }
               
               last = cur;
            }
         }
      }
      
   }

   private static Object lock = new Object();
   private static int LOOP_COUNT = 30;
   
   public static Test suite() throws Exception
   {
      Test t1 = getDeploySetup(DRMTestCase.class, "drm-tests.sar");
      return t1;
   }

   public DRMTestCase(String name)
   {
      super(name);
   }

   public void testStateReplication()
      throws Exception
   {
      log.debug("+++ testStateReplication");
      log.info("java.rmi.server.hostname="+System.getProperty("java.rmi.server.hostname"));
      RMIAdaptor[] adaptors = getAdaptors();
      String[] servers = super.getServers();
      RMIAdaptorExt server0 = (RMIAdaptorExt) adaptors[0];
      log.info("server0: "+server0);
      ObjectName clusterService = new ObjectName("jboss:service=DefaultPartition");
      Vector view0 = (Vector) server0.getAttribute(clusterService, "CurrentView");
      log.info("server0: CurrentView, "+view0);
      ObjectName drmService = new ObjectName("jboss.test:service=DRMTestCase");
      IReplicants drm0 = (IReplicants)
         MBeanServerInvocationHandler.newProxyInstance(server0, drmService,
         IReplicants.class, true);
      log.info(MBeanServerInvocationHandler.class.getProtectionDomain());
      TestListener listener = new TestListener(log);
      server0.addNotificationListener(drmService, listener, null, null);
      log.info("server0 addNotificationListener");
      String address = (String) drm0.lookupLocalReplicant();
      log.info("server0: lookupLocalReplicant: "+address);
      assertTrue("server0: address("+address+") == server0("+servers[0]+")",
         address.equals(servers[0]));

      RMIAdaptorExt server1 = (RMIAdaptorExt) adaptors[1];
      log.info("server1: "+server1);
      Vector view1 = (Vector) server1.getAttribute(clusterService, "CurrentView");
      log.info("server1: CurrentView, "+view1);
      IReplicants drm1 = (IReplicants)
         MBeanServerInvocationHandler.newProxyInstance(server1, drmService,
         IReplicants.class, true);
      server1.addNotificationListener(drmService, listener, null, null);
      log.info("server1 addNotificationListener");
      address = (String) drm1.lookupLocalReplicant();
      log.info("server1: lookupLocalReplicant: "+address);
      assertTrue("server1: address("+address+") == server1("+servers[1]+")",
         address.equals(servers[1]));

      List replicants0 = drm0.lookupReplicants();
      List replicants1 = drm1.lookupReplicants();
      assertTrue("size of replicants0 == replicants1)",
         replicants0.size() == replicants1.size());
      HashSet testSet = new HashSet(replicants0);
      for(int n = 0; n < replicants0.size(); n ++)
      {
         Object entry = replicants1.get(n);
         assertTrue("replicants0 contains:"+entry, testSet.contains(entry));
      }

      //
      for(int n = 0; n < 10; n ++)
      {
         drm0.add("key"+n, "data"+n+".0");
         drm1.add("key"+n, "data"+n+".1");
      }
      for(int n = 0; n < 10; n ++)
      {
         String key = "key"+n;
         log.info("key: "+key);
         replicants0 = drm0.lookupReplicants(key);
         replicants1 = drm1.lookupReplicants(key);
         log.info("replicants0: "+replicants0);
         log.info("replicants1: "+replicants1);
         HashSet testSet0 = new HashSet(replicants0);
         HashSet testSet1 = new HashSet(replicants1);
         assertTrue("size of replicants0 == replicants1)",
            replicants0.size() == replicants1.size());
         Object entry = drm0.lookupLocalReplicant(key);
         log.info("drm0.lookupLocalReplicant, key="+key+", entry="+entry);
         assertTrue("replicants0 contains:"+entry, testSet0.contains(entry));
         assertTrue("replicants1 contains:"+entry, testSet1.contains(entry));
      }

      for(int n = 0; n < 10; n ++)
         drm0.remove("key"+n);

      server0.removeNotificationListener(drmService, listener);
      server1.removeNotificationListener(drmService, listener);
   }
   
   /**
    * Tests the functionality of isMasterReplica(), also testing merge
    * handling.
    * 
    * TODO move this test out of the testsuite and into the cluster module
    *      itself, since it doesn't rely on the container.
    * 
    * @throws Exception
    */
   public void testIsMasterReplica() throws Exception
   {
      log.debug("+++ testIsMasterReplica()");
      
      MBeanServer mbeanServer = 
         MBeanServerFactory.createMBeanServer("mockPartition");
      try {
         ClusterNode localAddress = new ClusterNode(new IpAddress("127.0.0.1", 12345));
         MockHAPartition partition = new MockHAPartition(localAddress);
      
         DistributedReplicantManagerImpl drm = 
               new DistributedReplicantManagerImpl(partition, mbeanServer);

         drm.init();
         
         // Create a fake view for the MockHAPartition
         
         Vector remoteAddresses = new Vector();
         for (int i = 1; i < 5; i++)
            remoteAddresses.add(new ClusterNode(new IpAddress("127.0.0.1", 12340 + i)));
         
         Vector allNodes = new Vector(remoteAddresses);
         allNodes.add(localAddress);
         partition.setCurrentViewClusterNodes(allNodes);
         
         // Pass fake state to DRMImpl
      
         HashMap replicants = new HashMap();
         ArrayList remoteResponses = new ArrayList();
         for (int i = 0; i < remoteAddresses.size(); i++)
         {
            ClusterNode node = (ClusterNode) remoteAddresses.elementAt(i);
            Integer replicant = new Integer(i + 1);
            replicants.put(node.getName(), replicant);
            HashMap localReplicant = new HashMap();
            localReplicant.put("Mock", replicant);
            remoteResponses.add(new Object[] {node.getName(), localReplicant});
         }
         HashMap services = new HashMap();
         services.put("Mock", replicants);
         
         int hash = 0;
         for (int i = 1; i < 5; i++)
            hash += (new Integer(i)).hashCode();
         
         HashMap intraviewIds = new HashMap();
         intraviewIds.put("Mock", new Integer(hash));
      
         partition.setRemoteReplicants(remoteResponses);
         
         drm.setCurrentState(new Object[] {services, intraviewIds });
         
         drm.start();
         
         // add a local replicant
         
         drm.add("Mock", new Integer(5));
         
         // test that this node is not the master replica
         
         assertFalse("Local node is not master after startup", 
                     drm.isMasterReplica("Mock")); 
      
         // simulate a split where this node is the coord
         
         Vector localOnly = new Vector();
         localOnly.add(localAddress);
         
         partition.setCurrentViewClusterNodes(localOnly);
         partition.setRemoteReplicants(new ArrayList());
         
         drm.membershipChanged(remoteAddresses, new Vector(), localOnly);
         
         // test that this node is the master replica
         
         assertTrue("Local node is master after split", drm.isMasterReplica("Mock"));
         
         // Remove our local replicant
         
         drm.remove("Mock");
         
         // test that this node is not the master replica
         
         assertFalse("Local node is not master after dropping replicant", 
                     drm.isMasterReplica("Mock"));
         
         // Restore the local replicant 
         
         drm.add("Mock", new Integer(5));
         
         // simulate a merge
         
         Vector mergeGroups = new Vector();
         mergeGroups.add(remoteAddresses);
         mergeGroups.add(localOnly);
         
         partition.setCurrentViewClusterNodes(allNodes);
         partition.setRemoteReplicants(remoteResponses);
         
         drm.membershipChangedDuringMerge(new Vector(), remoteAddresses, 
                                          allNodes, mergeGroups);         
         
         // Merge processing is done asynchronously, so pause a bit
         sleepThread(100);
         
         // test that this node is not the master replica
         
         assertFalse("Local node is not master after merge", 
                     drm.isMasterReplica("Mock")); 
      }
      finally {
         MBeanServerFactory.releaseMBeanServer(mbeanServer);
      }
   }
   
   
   /**
    * Tests that one thread blocking in DRM.notifyKeyListeners() does not
    * prevent other threads registering/unregistering listeners. JBAS-2539
    * 
    * TODO move this test out of the testsuite and into the cluster module
    *      itself, since it doesn't rely on the container.
    * 
    * @throws Exception
    */
   public void testKeyListenerDeadlock() throws Exception
   {
      log.debug("+++ testKeyListenerDeadlock()");
      
      MBeanServer mbeanServer = 
         MBeanServerFactory.createMBeanServer("mockPartition");
      try {
         ClusterNode localAddress = new ClusterNode(new IpAddress("127.0.0.1", 12345));
         MockHAPartition partition = new MockHAPartition(localAddress);
      
         DistributedReplicantManagerImpl drm = 
               new DistributedReplicantManagerImpl(partition, mbeanServer);

         drm.init();
         
         // Create a fake view for the MockHAPartition
         
         Vector remoteAddresses = new Vector();
         for (int i = 1; i < 5; i++)
            remoteAddresses.add(new ClusterNode(new IpAddress("127.0.0.1", 12340 + i)));
         
         Vector allNodes = new Vector(remoteAddresses);
         allNodes.add(localAddress);
         partition.setCurrentViewClusterNodes(allNodes);
         
         drm.start();
         
         BlockingListenerThread blt = 
            new BlockingListenerThread(drm, true, null);
         
         // Hold the lock monitor so the test thread can't acquire it
         // This keeps the blocking thread alive.
         synchronized(lock) {
            // Spawn a thread that will change a key and then block on the
            // notification back to itself
            blt.start();

            sleepThread(50);
            
            assertTrue("Test thread is alive", blt.isAlive());            
            assertTrue("Test thread is blocking", blt.isBlocking());
            
            RegistrationThread rt = new RegistrationThread(drm);
            rt.start();

            sleepThread(50);
            
            assertTrue("No deadlock on listener registration", rt.isRegistered());
            
            assertTrue("No deadlock on listener unregistration", rt.isUnregistered());
            
            assertNull("No exception in deadlock tester", blt.getException());
            
            assertTrue("Test thread is still blocking", blt.isBlocking());
            assertTrue("Test thread is still alive", blt.isAlive());
         }
         
         drm.unregisterListener("TEST", blt);
         
         sleepThread(50);
         
         // Test going through remove
         blt = new BlockingListenerThread(drm, false, null);
         
         // Hold the lock monitor so the test thread can't acquire it
         // This keeps the blocking thread alive.
         synchronized(lock) {
            // Spawn a thread that will change a key and then block on the
            // notification back to itself
            blt.start();

            sleepThread(50);
            
            assertTrue("Test thread is alive", blt.isAlive());            
            assertTrue("Test thread is blocking", blt.isBlocking());
            
            RegistrationThread rt = new RegistrationThread(drm);
            rt.start();

            sleepThread(50);
            
            assertTrue("No deadlock on listener registration", rt.isRegistered());
            
            assertTrue("No deadlock on listener unregistration", rt.isUnregistered());
            
            assertNull("No exception in deadlock tester", blt.getException());
            
            assertTrue("Test thread is still blocking", blt.isBlocking());
            assertTrue("Test thread is still alive", blt.isAlive());
         }
      }
      finally {
         MBeanServerFactory.releaseMBeanServer(mbeanServer);
      }
   }
   
   
   /**
    * Tests that remotely-originated calls don't block.
    * 
    * TODO move this test out of the testsuite and into the cluster module
    *      itself, since it doesn't rely on the container.
    * 
    * @throws Exception
    */
   public void testRemoteCallBlocking() throws Exception
   {
      log.debug("+++ testRemoteCallBlocking()");
      
      MBeanServer mbeanServer = 
         MBeanServerFactory.createMBeanServer("mockPartition");
      try {
         ClusterNode localAddress = new ClusterNode(new IpAddress("127.0.0.1", 12345));
         MockHAPartition partition = new MockHAPartition(localAddress);
      
         DistributedReplicantManagerImpl drm = 
               new DistributedReplicantManagerImpl(partition, mbeanServer);

         drm.init();
         
         // Create a fake view for the MockHAPartition
         
         Vector remoteAddresses = new Vector();
         for (int i = 1; i < 5; i++)
            remoteAddresses.add(new ClusterNode(new IpAddress("127.0.0.1", 12340 + i)));
         
         Vector allNodes = new Vector(remoteAddresses);
         allNodes.add(localAddress);
         partition.setCurrentViewClusterNodes(allNodes);
         
         drm.start();
         
         String sender = ((ClusterNode)remoteAddresses.get(0)).getName();
         BlockingListenerThread blt = 
            new BlockingListenerThread(drm, true, sender);
         
         // Hold the lock monitor so the test thread can't acquire it
         // This keeps the blocking thread alive.
         synchronized(lock) {
            // Spawn a thread that will change a key and then block on the
            // notification back to itself
            blt.start();

            sleepThread(50);
            
            assertFalse("JGroups thread is not alive", blt.isAlive());            
            assertTrue("Async handler thread is blocking", blt.isBlocking());
            
            assertNull("No exception in JGroups thread", blt.getException());
         }
         
         drm.unregisterListener("TEST", blt);
         
         sleepThread(50);
         
         // Test going through remove
         blt = new BlockingListenerThread(drm, false, sender);
         
         // Hold the lock monitor so the test thread can't acquire it
         // This keeps the blocking thread alive.
         synchronized(lock) {
            // Spawn a thread that will change a key and then block on the
            // notification back to itself
            blt.start();

            sleepThread(50);
            
            assertFalse("JGroups thread is not alive", blt.isAlive());            
            assertTrue("Async handler thread is blocking", blt.isBlocking());
            
            assertNull("No exception in JGroups thread", blt.getException());
         }
      }
      finally {
         MBeanServerFactory.releaseMBeanServer(mbeanServer);
      }
   }
   
   /**
    * Tests that one thread blocking in DRM.notifyKeyListeners() does not
    * prevent other threads that use different keys adding/removing 
    * replicants. JBAS-2169
    * 
    * TODO move this test out of the testsuite and into the cluster module
    *      itself, since it doesn't rely on the container.
    * 
    * @throws Exception
    */
   public void testNonConflictingAddRemoveDeadlock() throws Exception
   {

      log.debug("+++ testNonConflictingAddRemoveDeadlock()");
      
      addRemoveDeadlockTest(false);
   }
   
   /**
    * Tests that one thread blocking in DRM.notifyKeyListeners() does not
    * prevent other threads that use the same keys adding/removing 
    * replicants. JBAS-1151
    * 
    * NOTE: This test basically demonstrates a small race condition that can
    * happen with the way HASingletonSupport's startService() method is
    * implemented (actually HAServiceMBeanSupport, but relevant in the case
    * of subclass HASingletonSupport, and in particular in its use in the
    * HASingletonDeployer service).  However, since the test doesn't actually
    * use the relevant code, but rather uses mock objects that work the same
    * way, this test is disabled -- its purpose has been achieved.  JIRA issue 
    * JBAS-1151 tracks the real problem; when it's resolved we'll create a test 
    * case against the real code that proves that fact.
    * 
    * TODO move this test out of the testsuite and into the cluster module
    *      itself, since it doesn't rely on the container.
    * 
    * @throws Exception
    */
   public void badtestConflictingAddRemoveDeadlock() throws Exception
   {
      log.debug("+++ testConflictingAddRemoveDeadlock()");
      
      addRemoveDeadlockTest(true);
   }  
   
   private void addRemoveDeadlockTest(boolean conflicting) throws Exception
   {  
      String[] keys = { "A", "B", "C", "D", "E" };
      int count = keys.length;
      
      MBeanServer mbeanServer = 
         MBeanServerFactory.createMBeanServer("mockPartition");
      try {
         ClusterNode localAddress = new ClusterNode(new IpAddress("127.0.0.1", 12345));
         MockHAPartition partition = new MockHAPartition(localAddress);
      
         DistributedReplicantManagerImpl drm = 
               new DistributedReplicantManagerImpl(partition, mbeanServer);

         drm.init();
         
         // Create a fake view for the MockHAPartition
         
         Vector remoteAddresses = new Vector();
         ClusterNode remote = new ClusterNode(new IpAddress("127.0.0.1", 12341));
         remoteAddresses.add(remote);
         
         Vector allNodes = new Vector(remoteAddresses);
         allNodes.add(localAddress);
         partition.setCurrentViewClusterNodes(allNodes);
         
         drm.start();
         
         MockDeployer deployer = new MockDeployer(drm);
         
         if (!conflicting)
         {
            // Register a MockHASingletonDeployer, but since we're in
            // non-conflicting mode, the DeployerThreads won't deal with it
            MockHASingletonDeployer listener = 
                  new MockHASingletonDeployer(deployer, "HASingleton", log);
            
            drm.registerListener("HASingleton", listener);
            drm.add("HASingleton", "HASingleton");            
         }
         
         // Create a semaphore to gate the threads and acquire all its permits
         Semaphore semaphore = new Semaphore(count + 1);
         for (int i = 0; i <= count; i++)
            semaphore.acquire();
         
         DeployerThread[] deployers = new DeployerThread[keys.length];
         for (int i = 0; i < count; i++)
         {
            DistributedReplicantManager.ReplicantListener listener = null;
            if (conflicting)
            {
               listener = new MockHASingletonDeployer(deployer, keys[i], log);
            }
            else
            {
               listener = new NullListener();
            }
            deployers[i] = new DeployerThread(deployer, keys[i], listener, semaphore, log);
            deployers[i].start();
         }
         
         String[] jgKeys = keys;
         if (!conflicting)
         {
            // The JGroups thread also deals with the MockHASingletonDeployer
            // key that the DeployerThreads don't
            jgKeys = new String[keys.length + 1];
            System.arraycopy(keys, 0, jgKeys, 0, keys.length);
            jgKeys[keys.length] = "HASingleton";            
         }
         JGroupsThread jgThread = new JGroupsThread(drm, jgKeys, remote.getName(), semaphore);
         jgThread.start();
         
         // Launch the threads
         semaphore.release(count + 1);
         
         boolean reacquired = false;
         try
         {
            // Give the threads 5 secs to acquire the semaphore
            long maxElapsed = System.currentTimeMillis() + 5000;
            for (int i = 0; i < keys.length; i++)
            {
               if (deployers[i].getCount() < 0)
               {
                  assertTrue("Thread " + keys[i] + " started in time",
                              maxElapsed - System.currentTimeMillis() > 0);
                  sleepThread(10);
                  i--; // try again
               }   
            }
            
            while (jgThread.getCount() < 0)
            {
               assertTrue("jgThread started in time",
                           maxElapsed - System.currentTimeMillis() > 0);
               sleepThread(10);               
            }
            
            // Reaquire all the permits, thus showing the threads didn't deadlock
            
            // Give them 500 ms per loop
            maxElapsed = System.currentTimeMillis() + (500 * LOOP_COUNT);
            for (int i = 0; i <= count; i++)
            {
               long waitTime = maxElapsed - System.currentTimeMillis();
               assertTrue("Acquired thread " + i, semaphore.attempt(waitTime));
            }
            
            reacquired = true;
            
            // Ensure there were no exceptions
            for (int i = 0; i < keys.length; i++)
            {
               assertEquals("Thread " + keys[i] + " finished", LOOP_COUNT, deployers[i].getCount());
               assertNull("Thread " + keys[i] + " saw no exceptions", deployers[i].getException());
            }
            assertEquals("JGroups Thread finished", LOOP_COUNT, jgThread.getCount());
            assertNull("JGroups Thread saw no exceptions", jgThread.getException());
         }
         finally
         {

            if (!reacquired)
            {
               for (int i = 0; i < keys.length; i++)
               {
                  if (deployers[i].getException() != null)
                  {
                     System.out.println("Exception in deployer " + i);
                     deployers[i].getException().printStackTrace(System.out);
                  }
                  else
                  {
                     System.out.println("Thread " + i + " completed " + deployers[i].getCount());
                  }
               }
               if (jgThread.getException() != null)
               {
                  System.out.println("Exception in jgThread");
                  jgThread.getException().printStackTrace(System.out);
               }
               else
               {
                  System.out.println("jgThread completed " + jgThread.getCount());
               }
            }
            
            // Be sure the threads are dead
            if (jgThread.isAlive())
            {
               jgThread.interrupt();
               sleepThread(5);   
               printStackTrace(jgThread.getName(), jgThread.getException());
            }
            for (int i = 0; i < keys.length; i++)
            {
               if (deployers[i].isAlive())
               {
                  deployers[i].interrupt();
                  sleepThread(5);
                  printStackTrace(deployers[i].getName(), deployers[i].getException());
               }
            }
               
         }
      }
      finally {
         MBeanServerFactory.releaseMBeanServer(mbeanServer);
      }
   }

   public void testReplicantOrder() throws Exception
   {
      MBeanServer mbeanServer =
         MBeanServerFactory.createMBeanServer("mockPartitionA");
      try {
         
         //  Create a fake view for the MockHAPartition
         ClusterNode[] nodes = new ClusterNode[5];
         String[] names = new String[nodes.length];
         Integer[] replicants = new Integer[nodes.length];
         Vector allNodes = new Vector();
         for (int i = 0; i < nodes.length; i++)
         {
            nodes[i] = new ClusterNode(new IpAddress("127.0.0.1", 12340 + i));
            allNodes.add(nodes[i]);
            names[i] = nodes[i].getName();
            replicants[i] = new Integer(i);
         }
         
         MockHAPartition partition = new MockHAPartition(nodes[2]);
         partition.setCurrentViewClusterNodes(allNodes);
         
         DistributedReplicantManagerImpl drm = 
               new DistributedReplicantManagerImpl(partition, mbeanServer);
         drm.init();
         drm.start();
         
         CachingListener listener = new CachingListener();
         drm.registerListener("TEST", listener);
         
         SecureRandom random = new SecureRandom();
         boolean[] added = new boolean[nodes.length];
         List lookup = null;
         for (int i = 0; i < 10; i++)
         {
            int node = random.nextInt(nodes.length);
            if (added[node])
            {
               if (node == 2)
                  drm.remove("TEST");
               else
                  drm._remove("TEST", nodes[node].getName());
               added[node] = false;
            }
            else
            {     
               if (node == 2)
                  drm.add("TEST", replicants[node]);
               else
                  drm._add("TEST", nodes[node].getName(), replicants[node]);   
               added[node] = true;
            }
            
            // Confirm the proper order of the replicant node names
            lookup = maskListClass(drm.lookupReplicantsNodeNames("TEST"));
            confirmReplicantList(lookup, names, added);
            
            // Confirm the proper order of the replicants via lookupReplicants
            lookup = maskListClass(drm.lookupReplicants("TEST"));
            confirmReplicantList(lookup, replicants, added);
            
            // Confirm the listener got the same list
//            assertEquals("Listener received a correct list", lookup, 
//                         maskListClass(listener.replicants));
         }
         
         // Let the asynchronous notification thread catch up
         sleep(25);
         
         // Confirm all lists presented to the listener were properly ordered
         assertTrue("Listener saw no misordered lists", listener.clean);
         
      }
      finally {
         MBeanServerFactory.releaseMBeanServer(mbeanServer);
      }
   }
   
   private void confirmReplicantList(List current, Object[] all, boolean[] added)
   {
      Iterator iter = current.iterator();
      for (int i = 0; i < added.length; i++)
      {
         if (added[i])
         {
            assertTrue("List has more replicants", iter.hasNext());
            assertEquals("Replicant for node " + i + " is next", 
                         all[i], iter.next());
         }
      }
      assertFalse("List has no extra replicants", iter.hasNext());
   }
   
   /** Converts the given list to an ArrayList, if it isn't already */
   private List maskListClass(List toMask)
   {
      if (toMask instanceof ArrayList)
         return toMask;
      else if (toMask == null)
         return new ArrayList();
      else
         return new ArrayList(toMask);
   }

   private static void sleepThread(long millis)
   {
      try
      {
         Thread.sleep(millis);
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
   
   private static void printStackTrace(String threadName, Exception e)
   {
      if (e instanceof InterruptedException)
      {
         System.out.println("Stack trace for " + threadName);
         e.printStackTrace(System.out);
         System.out.println();
      }
   }

}
