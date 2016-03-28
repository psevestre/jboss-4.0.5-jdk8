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

import org.jboss.ha.framework.interfaces.GenericClusteringException;
import org.jboss.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Used for testing clustering: allows to explicitly makes a call to node fail
 * This will mimic a dead server. This is used as a ejb3 interceptor now.
 * @author Ben Wang
 *
 */
public class ExplicitFailoverInterceptor
{
   private Logger log = Logger.getLogger(ExplicitFailoverInterceptor.class);

   @AroundInvoke
   public Object invoke(InvocationContext ctx)
      throws Exception
   {
      checkFailoverNeed (ctx);
      return ctx.proceed();
   }

   protected void checkFailoverNeed (InvocationContext ctx)
      throws Exception
   {
      if(ctx.getMethod().getName().equals("setUpFailover"))
      {
         return;
      }

      String failover = (String)System.getProperty ("JBossCluster-DoFail");
      boolean doFail = false;

      if (failover != null)
      {
         String strFailover = failover;
         if (strFailover.equalsIgnoreCase ("true"))
         {
            doFail = true;
         }
         else if (strFailover.equalsIgnoreCase ("once"))
         {
            doFail = true;
            System.setProperty ("JBossCluster-DoFail", "false");
         }
      }

      if (doFail)
      {
         log.debug ("WE FAILOVER IN EJB INTERCEPTOR (explicit failover)!");

         throw new GenericClusteringException
            (GenericClusteringException.COMPLETED_NO, "Test failover from ejb interceptor", false);
      }
   }
}
