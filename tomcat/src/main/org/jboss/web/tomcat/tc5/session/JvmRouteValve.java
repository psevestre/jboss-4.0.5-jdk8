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
package org.jboss.web.tomcat.tc5.session;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.apache.catalina.*;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.ValveBase;
import org.jboss.logging.Logger;

/**
 * Web request valve to specifically handle Tomcat jvmRoute using mod_jk(2)
 * module. We assume that the session is set by cookie only for now, i.e., no
 * support of that from URL. Furthermore, the session id has a format of
 * id.jvmRoute where jvmRoute is used by JK module to determine sticky session
 * during load balancing.
 *
 * @author Ben Wang
 * @version $Revision: 57206 $
 */
public class JvmRouteValve extends ValveBase implements Lifecycle
{
   // The info string for this Valve
   private static final String info = "JvmRouteValve/1.0";

   protected static Logger log_ = Logger.getLogger(JvmRouteValve.class);

   // Valve-lifecycle_ helper object
   protected LifecycleSupport support = new LifecycleSupport(this);

   protected AbstractJBossManager manager_;

   /**
    * Create a new Valve.
    *
    */
   public JvmRouteValve(AbstractJBossManager manager)
   {
      super();
      manager_ = manager;
   }

   /**
    * Get information about this Valve.
    */
   public String getInfo()
   {
      return info;
   }

   public void invoke(Request request, Response response) throws IOException, ServletException
   {

      // Need to check it before let it through. This is ok because this 
      // valve is inserted only when mod_jk option is configured.
      checkJvmRoute(request, response);

      // let the servlet invokation go through
      getNext().invoke(request, response);
   }

   public void checkJvmRoute(Request req, Response res)
      throws IOException, ServletException
   {
      HttpSession session = req.getSession(false);
      if (session != null)
      {
         String sessionId = session.getId();

         // Obtain JvmRoute
         String jvmRoute = manager_.getJvmRoute();
         if (log_.isDebugEnabled())
         {
            log_.debug("checkJvmRoute(): check if need to re-route based on JvmRoute. Session id: " +
               sessionId + " jvmRoute: " + jvmRoute);
         }

         if (jvmRoute == null)
         {
            throw new RuntimeException("JvmRouteValve.checkJvmRoute(): Tomcat JvmRoute is null. " +
               "Need to assign a value in Tomcat server.xml for load balancing.");
         }

         // Check if incoming session id has JvmRoute appended. If not, append it.
         boolean setCookie = !req.isRequestedSessionIdFromURL();
         handleJvmRoute(sessionId, jvmRoute, res, setCookie);
      }
   }

   protected void handleJvmRoute(String sessionId, 
                                 String jvmRoute, 
                                 HttpServletResponse response,
                                 boolean setCookie)
   {
      // Get requested jvmRoute.
      // TODO. The current format is assumed to be id.jvmRoute. Can be generalized later.
      String requestedJvmRoute = null;
      int index = sessionId.lastIndexOf(".");
      if (index > 0)
      {
         requestedJvmRoute = sessionId.substring(index + 1, sessionId.length());
      }

      String newId = sessionId;
      if (!jvmRoute.equals(requestedJvmRoute))
      {
         if (requestedJvmRoute == null)
         {
            // If this valve is turned on, we assume we have an appendix of jvmRoute. 
            // So this request is new.
            newId = sessionId + "." + jvmRoute;
         }         
         else 
         {
            // We just had a failover since jvmRoute does not match. 
            // We will replace the old one with the new one.         
            if (log_.isDebugEnabled())
            {
               log_.debug("handleJvmRoute(): We have detected a failover with different jvmRoute." +
                  " old one: " + requestedJvmRoute + " new one: " + jvmRoute + ". Will reset the session id.");
            }
            
            String base = sessionId.substring(0, index);
            newId = base + "." + jvmRoute;
         }
         
         resetSessionId(sessionId, newId);
         
         if (setCookie)
            manager_.setNewSessionCookie(newId, response);
      }
   }
   
   private void resetSessionId(String oldId, String newId)
   {
      try
      {
         ClusteredSession session = (ClusteredSession)manager_.findSession(oldId);
         // change session id with the new one using local jvmRoute.
         if( session != null )
         {
            // Note this will trigger a session remove from the super Tomcat class.
            session.resetIdWithRouteInfo(newId);
            if (log_.isDebugEnabled())
            {
               log_.debug("resetSessionId(): changed catalina session to= [" + newId + "] old one= [" + oldId + "]");
            }
         }
         else if (log_.isDebugEnabled())
         {
            log_.debug("resetSessionId(): no session with id " + newId + " found");
         }
      }
      catch (IOException e)
      {
         if (log_.isDebugEnabled())
         {
            log_.debug("resetSessionId(): manager_.findSession() unable to find session= [" + oldId + "]", e);
         }
         throw new RuntimeException("JvmRouteValve.resetSessionId(): cannot find session [" + oldId + "]", e);
      }
   }

   // Lifecycle Interface
   public void addLifecycleListener(LifecycleListener listener)
   {
      support.addLifecycleListener(listener);
   }

   public void removeLifecycleListener(LifecycleListener listener)
   {
      support.removeLifecycleListener(listener);
   }

   public LifecycleListener[] findLifecycleListeners()
   {
      return support.findLifecycleListeners();
   }

   public void start() throws LifecycleException
   {
      support.fireLifecycleEvent(START_EVENT, this);
   }

   public void stop() throws LifecycleException
   {
      support.fireLifecycleEvent(STOP_EVENT, this);
   }

}
