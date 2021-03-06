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
package org.jboss.test.security.ejb;

import java.rmi.RemoteException;
import java.security.Principal;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.jboss.test.security.interfaces.Entity;
import org.jboss.test.security.interfaces.EntityHome;
import org.jboss.test.security.interfaces.StatelessSession;
import org.jboss.test.security.interfaces.StatelessSessionHome;

import org.apache.log4j.Category;

/** A SessionBean that accesses an Entity bean in its echo() method to test runAs
 identity propagation. It also access its own excluded() method to test that the runAs
 identity is also see on methods of this bean that are invoked through the
 remote interface.
 
 @author Scott.Stark@jboss.org
 @version $Revision: 57211 $
 */
public class StatelessSessionBean3 implements SessionBean
{
   private static Category log = Category.getInstance(StatelessSessionBean3.class);
   private SessionContext sessionContext;
   
   public void ejbCreate() throws RemoteException, CreateException
   {
      log.debug("ejbCreate() called");
   }

   public void ejbActivate() throws RemoteException
   {
      log.debug("ejbActivate() called");
   }

   public void ejbPassivate() throws RemoteException
   {
      log.debug("ejbPassivate() called");
   }

   public void ejbRemove() throws RemoteException
   {
      log.debug("ejbRemove() called");
   }

   public void setSessionContext(SessionContext context) throws RemoteException
   {
      sessionContext = context;
   }

   /** This method creates an instance of the entity bean bound under
    java:comp/env/ejb/Entity and then invokes its echo method. This
    method should be accessible by user's with a role of Echo, while
    the Entity bean should only be accessible by the runAs role.
    */
   public String echo(String arg)
   {
      log.debug("echo, arg="+arg);
      // This call should fail if the bean is not secured
      Principal p = sessionContext.getCallerPrincipal();
      log.debug("echo, callerPrincipal="+p);
      String echo = null;
      try
      {
         InitialContext ctx = new InitialContext();
         EntityHome home = (EntityHome) ctx.lookup("java:comp/env/ejb/Entity");
         Entity bean = home.findByPrimaryKey(arg);
         echo = bean.echo(arg);
      }
      catch(Exception e)
      {
         log.debug("failed", e);
         e.fillInStackTrace();
         throw new EJBException(e);
      }
      return echo;
   }
   
   public String forward(String echoArg)
   {
      log.debug("forward, echoArg="+echoArg);
      String echo = null;
      try
      {
         InitialContext ctx = new InitialContext();
         StatelessSessionHome home = (StatelessSessionHome) ctx.lookup("java:comp/env/ejb/Session");
         StatelessSession bean = home.create();
         echo = bean.echo(echoArg);
      }
      catch(Exception e)
      {
         log.debug("failed", e);
         e.fillInStackTrace();
         throw new EJBException(e);
      }
      return echo;
   }
   
   /** This method gets this bean's remote interface and invokes the
    excluded() method to test that the method is accessed as the
    runAs role.
    */
   public void noop()
   {
      log.debug("noop calling excluded...");
      StatelessSession myEJB = (StatelessSession) sessionContext.getEJBObject();
      try
      {
         myEJB.excluded();
      }
      catch(RemoteException e)
      {
         throw new EJBException("Failed to access excluded: "+e.detail);
      }
   }
   
   public void npeError()
   {
      log.debug("npeError");
      Object obj = null;
      obj.toString();
   }
   public void unchecked()
   {
      Principal p = sessionContext.getCallerPrincipal();
      log.debug("StatelessSessionBean.unchecked, callerPrincipal="+p);
   }
   
   /** This method should be assigned access to the runAs role and no user
    should have this role.
    */
   public void excluded()
   {
      log.debug("excluded, accessed");
      // This call should fail if the bean is not secured
      Principal p = sessionContext.getCallerPrincipal();
      log.debug("excluded, callerPrincipal="+p);
   }
}
