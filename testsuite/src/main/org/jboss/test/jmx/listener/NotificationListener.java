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
package org.jboss.test.jmx.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.Notification;

import org.jboss.system.ListenerServiceMBeanSupport;

/**
 * A test service that subscribes and stores JMX Notifications.
 * 
 * The subscription are specified declaratively using the
 * ListenerServiceMBean SubscriptionList attribute.
 * 
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision: 57211 $
 */
public class NotificationListener extends ListenerServiceMBeanSupport
   implements NotificationListenerMBean
{
   // Private Data --------------------------------------------------
   
  /** Stored notifications */
  private List notifList;
  
  // Constructors --------------------------------------------------
  
  /**
   * CTOR
  **/
  public NotificationListener()
  {
     notifList = Collections.synchronizedList(new ArrayList());
  }
  
  // NotificationListenerMBean implementation -----------------------
  
   /**
    * Number of notifications received.
    */
   public int getNotificationCount()
   {
      return notifList.size();
   }
   
   /**
    * @return a list of Notifications received
    */
   public List retrieveNotifications()
   {
      return new ArrayList(notifList);
   }

   /**
    * Clear the stored notifications, reset the counter
    */
   public void clearNotifications()
   {
      notifList.clear();
   }
   
   // Lifecycle -----------------------------------------------------
   
   protected void startService() throws Exception
   {
      clearNotifications();
      super.subscribe(false);
   }
   
   protected void stopService()
   {
      super.unsubscribe();
      clearNotifications();
   }
   
   // ListenerServiceMBeanSupport -----------------------------------
   
   public void handleNotification2(Notification notification, Object handback)
   {
      notifList.add(notification);
      log.debug("Received notification #" + notifList.size() + ": " + notification);
   }
}
