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
package org.jboss.system;

/**
 * MBean interface.
 */
public interface BarrierControllerMBean extends org.jboss.system.ListenerServiceMBean
{

   //default object name
   public static final javax.management.ObjectName OBJECT_NAME = org.jboss.mx.util.ObjectNameFactory
         .create("jboss:service=BarrierController");

   /**
    * The controlled barrier StateString.
    */
   java.lang.String getBarrierStateString();

   /**
    * The controlled barrier ObjectName.
    */
   void setBarrierObjectName(javax.management.ObjectName barrierName);

   /**
    * The controlled barrier ObjectName.
    */
   javax.management.ObjectName getBarrierObjectName();

   /**
    * The initial state of the barrier. If set, it overrides the internal call to enableOnStartup() which will never get called.
    */
   void setBarrierEnabledOnStartup(java.lang.Boolean enableOnStartup);

   /**
    * The initial state of the barrier. Use the value set through setBarrierEnabledOnStartup() otherwise call the internal enableOnStartup() override to make a decision.
    */
   java.lang.Boolean getBarrierEnabledOnStartup();

   /**
    * The notification subscription handback string that starts the barrier.
    */
   void setStartBarrierHandback(java.lang.String startHandback);

   /**
    * The notification subscription handback string that starts the barrier.
    */
   java.lang.String getStartBarrierHandback();

   /**
    * The notification subscription handback string that stops the barrier.
    */
   void setStopBarrierHandback(java.lang.String stopHandback);

   /**
    * The notification subscription handback string that stops the barrier.
    */
   java.lang.String getStopBarrierHandback();

   /**
    * The ability to dynamically subscribe for notifications.
    */
   void setDynamicSubscriptions(java.lang.Boolean dynamicSubscriptions);

   /**
    * The ability to dynamically subscribe for notifications.
    */
   java.lang.Boolean getDynamicSubscriptions();

   /**
    * Manually start the controlled barrier
    */
   void startBarrier();

   /**
    * Manually stop the controlled barrier
    */
   void stopBarrier();

}
