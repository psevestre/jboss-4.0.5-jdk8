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

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

import org.jboss.logging.Logger;

import java.rmi.RemoteException;

public class HelloBean implements SessionBean
{
   private static final Logger log = Logger.getLogger(HelloBean.class);
   
   private Service service;

   public String[] sayHello(String input) throws RemoteException
   {
      try
      {
         HandlerTracker.clear();
         HelloEndpoint sei = (HelloEndpoint)service.getPort(HelloEndpoint.class);
         String msg = sei.sayHello(input);
         HandlerTracker.trackMessage("ejb: " + msg);
         return HandlerTracker.getProtocol();
      }
      catch (ServiceException e)
      {
         throw new EJBException(e);
      }
   }

   // EJB life cycle methods *******************************************************************************************

   public void ejbCreate()
   {
   }

   public void ejbRemove()
   {
   }

   public void ejbActivate()
   {
   }

   public void ejbPassivate()
   {
   }

   public void setSessionContext(SessionContext sc)
   {
      try
      {
         InitialContext iniCtx = new InitialContext();
         service = (Service)iniCtx.lookup("java:comp/env/service/HelloService");
      }
      catch (NamingException e)
      {
         log.error("Cannot lookup service ref", e);
         throw new EJBException(e);
      }
   }
}
