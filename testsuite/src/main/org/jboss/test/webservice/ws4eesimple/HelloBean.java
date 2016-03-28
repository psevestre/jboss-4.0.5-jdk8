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
package org.jboss.test.webservice.ws4eesimple;

import org.jboss.logging.Logger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

public class HelloBean implements SessionBean
{
   private Logger log = Logger.getLogger(HelloBean.class);

   public HelloBean()
   {
      log.debug("new HelloBean");
   }

   // business methods *************************************************************************************************

   public String sayHello(String input) throws RemoteException
   {
      log.debug("sayHello: " + input);
      return "'" + input + "' to you too!";
   }

   // EJB life cycle methods *******************************************************************************************

   public void ejbCreate()
   {
      log.debug("ejbCreate");
   }

   public void ejbRemove()
   {
      log.debug("ejbRemove");
   }

   public void ejbActivate()
   {
      log.debug("ejbActivate");
   }

   public void ejbPassivate()
   {
      log.debug("ejbPassivate");
   }

   public void setSessionContext(SessionContext sc)
   {
      log.debug("setSessionContext");
   }
}
