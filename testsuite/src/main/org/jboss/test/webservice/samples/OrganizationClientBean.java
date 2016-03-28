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
package org.jboss.test.webservice.samples;

import org.jboss.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.rpc.Service;
import java.rmi.RemoteException;

/**
 * An example of a SLSB
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class OrganizationClientBean implements SessionBean
{
   // provide logging
   private static final Logger log = Logger.getLogger(OrganizationClientBean.class);

   /** Get the contact info from the EJB service */
   public String getContactInfoEJB(String organization) throws RemoteException
   {
      Service service = null;
      try
      {
         InitialContext iniCtx = new InitialContext();
         service = (Service)iniCtx.lookup("java:/comp/env/service/OrganizationServiceEJB");
         Organization endpoint = (Organization)service.getPort(Organization.class);
         String info = endpoint.getContactInfo(organization);
         return info;
      }
      catch (NamingException e)
      {
         throw new EJBException(e);
      }
      catch (Exception e)
      {
         throw new EJBException("Cannot invoke webservice", e);
      }
   }

   /** Get the contact info from the JSE service */
   public String getContactInfoJSE(String organization) throws RemoteException
   {
      try
      {
         InitialContext iniCtx = new InitialContext();
         OrganizationService service = (OrganizationService)iniCtx.lookup("java:comp/env/service/OrganizationServiceJSE");
         Organization endpoint = (Organization)service.getOrganizationPort();
         String info = endpoint.getContactInfo(organization);
         return info;
      }
      catch (NamingException e)
      {
         throw new EJBException(e);
      }
      catch (Exception e)
      {
         throw new EJBException("Cannot invoke webservice", e);
      }
   }

   // SLSB Lifecycle ***************************************************************************************************

   public void ejbCreate()
   {
   }

   public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException
   {
   }

   public void ejbRemove() throws EJBException, RemoteException
   {
   }

   public void ejbActivate() throws EJBException, RemoteException
   {
   }

   public void ejbPassivate() throws EJBException, RemoteException
   {
   }
}
