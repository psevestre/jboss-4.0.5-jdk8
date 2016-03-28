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
package org.jboss.test.cluster.ds;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import javax.management.Notification;
import javax.naming.InitialContext;

import org.jboss.ha.framework.interfaces.DistributedState;
import org.jboss.ha.framework.interfaces.DistributedState.DSListenerEx;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.logging.Logger;
import org.jboss.mx.util.JBossNotificationBroadcasterSupport;

/** Tests of the  

    @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
    @version $Revision: 57211 $
*/
public class DistributedStateUser extends JBossNotificationBroadcasterSupport
   implements IDistributedState, DSListenerEx
{
   protected static Logger log = Logger.getLogger(DistributedStateUser.class);

   protected DistributedState entryMap;
   protected String category;
   protected String partitionName;
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
      this.entryMap = partition.getDistributedStateService();
      log.debug("Obtained DistributedState from partition="+partitionName);
      entryMap.registerDSListenerEx(category, this);
   }
   public void stop()
   {
      entryMap.unregisterDSListenerEx(category, this);
      flush();
   }

   public Serializable get(Serializable key)
   {
      Serializable value = entryMap.get(category, key);
      log.debug("Get: "+key+", value: "+value);
      return value;
   }

   public void put(Serializable key, Serializable value)
      throws Exception
   {
      entryMap.set(category, key, value, false);
      log.debug("Put: "+key+", value: "+value);
   }

   public void remove(Serializable key)
      throws Exception
   {
      Object value = entryMap.remove(category, key, false);
      log.debug("Removed: "+key+", value: "+value);
   }

   /** Remove all entries from the cache.
    */
   public void flush()
   {
      Collection keys = entryMap.getAllKeys(category);
      if(keys == null ) return;
      // Notify the entries of their removal
      Iterator iter = keys.iterator();
      while( iter.hasNext() )
      {
         Serializable key = (Serializable) iter.next();
         try
         {
            entryMap.remove(category, key);
         }
         catch(Exception e)
         {
            log.debug("Failed to remove: "+key, e);
         }
      }
   }

   public int size()
   {
      return entryMap.getAllKeys(category).size();
   }

   public void valueHasChanged(String category, Serializable key,
      Serializable value, boolean locallyModified)
   {
      NotifyData data = new NotifyData();
      data.category = category;
      data.key = key;
      data.value = value;
      data.locallyModified = locallyModified;
      String address = System.getProperty("jboss.bind.address");
      long id = nextSequence();
      Notification msg = new Notification("valueHasChanged", this, id, address);
      msg.setUserData(data);
      log.debug("valueHasChanged, "+msg);
      super.sendNotification(msg);
   }

   public void keyHasBeenRemoved(String category, Serializable key,
      Serializable previousContent, boolean locallyModified)
   {
      NotifyData data = new NotifyData();
      data.category = category;
      data.key = key;
      data.value = previousContent;
      data.locallyModified = locallyModified;
      String address = System.getProperty("jboss.bind.address");
      long id = nextSequence();
      Notification msg = new Notification("keyHasBeenRemoved", this, id, address);
      msg.setUserData(data);
      log.debug("keyHasBeenRemoved, "+msg);
      super.sendNotification(msg);
   }

   private synchronized long nextSequence()
   {
      return sequence ++;
   }
}
