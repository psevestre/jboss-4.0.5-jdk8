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
package org.jboss.test.cluster.drm;

import java.io.Serializable;
import java.util.List;
import javax.management.Notification;
import javax.naming.InitialContext;

import org.jboss.ha.framework.interfaces.DistributedReplicantManager;
import org.jboss.ha.framework.interfaces.DistributedReplicantManager.ReplicantListener;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.logging.Logger;
import org.jboss.mx.util.JBossNotificationBroadcasterSupport;

/** Tests of the DistributedReplicantManager aspect of the HAPartition service.

   @author Scott.Stark@jboss.org
   @version $Revision: 57211 $
*/
public class DRMUser extends JBossNotificationBroadcasterSupport
   implements IReplicants, ReplicantListener
{
   protected static Logger log = Logger.getLogger(DRMUser.class);

   protected DistributedReplicantManager drm;
   protected String category = "DRMUser";
   protected String partitionName = "DefaultPartition";
   protected long sequence;

   public String getPartitionName()
   {
      return partitionName;
   }
   public void setPartitionName(String partitionName)
   {
      this.partitionName = partitionName;
   }

   public String getCategory()
   {
      return category;
   }
   public void setCategory(String category)
   {
      this.category = category;
   }

   public void start() throws Exception
   {
      // Lookup the parition
      InitialContext ctx = new InitialContext();
      String jndiName = "/HAPartition/" + partitionName;
      HAPartition partition = (HAPartition) ctx.lookup(jndiName);
      drm = partition.getDistributedReplicantManager();
      log.debug("Obtained DistributedReplicantManager from partition="+partitionName);
      drm.registerListener(category, this);
      // Bind the jboss.bind.address value into the DRM
      String address = System.getProperty("jboss.bind.address");
      drm.add(category, address);
      log.info("Added: "+address+" under key: "+category);
   }
   public void stop() throws Exception
   {
      drm.remove(category);
      drm.unregisterListener(category, this);
   }

   public Serializable lookupLocalReplicant()
   {
      return drm.lookupLocalReplicant(category);
   }
   public Serializable lookupLocalReplicant(String key)
   {
      return drm.lookupLocalReplicant(key);      
   }

   public List lookupReplicants()
   {
      return drm.lookupReplicants(category);
   }
   public List lookupReplicants(String key)
   {
      return drm.lookupReplicants(key);
   }
   public void add(String key, Serializable data)
      throws Exception
   {
      drm.add(key, data);
   }
   public void remove(String key)
      throws Exception
   {
      drm.remove(key);
   }
   private synchronized long nextSequence()
   {
      return sequence ++;
   }

   public void replicantsChanged(String key, List newReplicants, int newReplicantsViewId)
   {
      NotifyData data = new NotifyData();
      data.key = key;
      data.newReplicants = newReplicants;
      data.newReplicantsViewId = newReplicantsViewId;
      String address = System.getProperty("jboss.bind.address");
      long id = nextSequence();
      Notification msg = new Notification("replicantsChanged", this, id, address);
      msg.setUserData(data);
      log.info("replicantsChanged, "+msg);
      super.sendNotification(msg);
   }
}
