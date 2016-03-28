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
package org.jboss.ejb3;

import javax.ejb.TimerService;

import org.jboss.ejb.txtimer.EJBTimerService;
import org.jboss.ejb.txtimer.TimedObjectInvoker;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class EjbTimerUtil
{
   private static final Logger log = Logger.getLogger(EjbTimerUtil.class);

   public static TimerService getTimerService(Container container, TimedObjectInvoker invoker)
   {
      TimerService timerService = null;
      try
      {
         EJBTimerService service = (EJBTimerService) MBeanProxyExt.create(EJBTimerService.class, EJBTimerService.OBJECT_NAME, MBeanServerLocator.locateJBoss());
         timerService = service.createTimerService(container.getObjectName(), null, invoker);
      }
      catch (Exception e)
      {
         //throw new EJBException("Could not create timer service", e);
         if (log.isTraceEnabled())
         {
            log.trace("Unable to initialize timer service", e);
         }
         else
         {
            log.trace("Unable to initialize timer service");
         }
      }
      return timerService;
   }

   public static void removeTimerService(Container container)
   {
      try
      {
         EJBTimerService service = (EJBTimerService) MBeanProxyExt.create(EJBTimerService.class, EJBTimerService.OBJECT_NAME, MBeanServerLocator.locateJBoss());
         service.removeTimerService(container.getObjectName(), null);
      }
      catch (Exception e)
      {
         //throw new EJBException("Could not remove timer service", e);
         if (log.isTraceEnabled())
         {
            log.trace("Unable to initialize timer service", e);
         }
         else
         {
            log.trace("Unable to initialize timer service");
         }
      }
   }
}
