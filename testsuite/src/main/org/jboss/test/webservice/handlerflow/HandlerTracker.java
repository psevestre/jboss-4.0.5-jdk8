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
package org.jboss.test.webservice.handlerflow;

import org.jboss.logging.Logger;

import javax.xml.rpc.handler.Handler;
import java.util.ArrayList;

public final class HandlerTracker
{
   private static final Logger log = Logger.getLogger(HandlerTracker.class);

   private static ArrayList protocol = new ArrayList();

   // hide constructor
   private HandlerTracker()
   {
   }

   public static String[] getProtocol()
   {
      String[] arr = new String[protocol.size()];
      protocol.toArray(arr);
      return arr;
   }

   public static void trackInit(Handler handler)
   {
      String hName = handler.getClass().getName();
      hName = hName.substring(hName.lastIndexOf(".") + 1);
      String msg = hName + " init " + trackerInfo(handler);
      protocol.add(msg);
      log.info(msg);
   }

   public static void trackDestroy(Handler handler)
   {
      String hName = handler.getClass().getName();
      hName = hName.substring(hName.lastIndexOf(".") + 1);
      String msg = hName + " destroy " + trackerInfo(handler);
      protocol.add(msg);
      log.info(msg);
   }

   public static void trackHandleRequest(Handler handler)
   {
      String hName = handler.getClass().getName();
      hName = hName.substring(hName.lastIndexOf(".") + 1);
      String msg = hName + " handleRequest " + trackerInfo(handler);
      protocol.add(msg);
      log.info(msg);
   }

   public static void trackHandleResponse(Handler handler)
   {
      String hName = handler.getClass().getName();
      hName = hName.substring(hName.lastIndexOf(".") + 1);
      String msg = hName + " handleResponse " + trackerInfo(handler);
      protocol.add(msg);
      log.info(msg);
   }

   public static void trackHandleFault(Handler handler)
   {
      String hName = handler.getClass().getName();
      hName = hName.substring(hName.lastIndexOf(".") + 1);
      String msg = hName + " handleFault " + trackerInfo(handler);
      protocol.add(msg);
      log.info(msg);
   }

   public static void trackMessage(String msg)
   {
      protocol.add(msg);
      log.info(msg);
   }

   public static void clear()
   {
      protocol.clear();
      log.info("clear [tracker=" + HandlerTracker.class.hashCode() + "]");
   }

   private static String trackerInfo(Handler handler)
   {
      String hstr = "handler=" + handler.hashCode();
      String tstr = "tracker=" + HandlerTracker.class.hashCode();
      return "[" + hstr + "," + tstr + "]";
   }
}
