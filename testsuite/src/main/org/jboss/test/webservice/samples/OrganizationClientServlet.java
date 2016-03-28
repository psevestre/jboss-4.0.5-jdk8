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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.Service;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An example of a webservice client servlet
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class OrganizationClientServlet extends HttpServlet
{
   // provide logging
   private static final Logger log = Logger.getLogger(OrganizationClientServlet.class);

   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      String info = null;

      String endpoint = req.getParameter("endpoint");
      String method = req.getParameter("method");
      String organization = req.getParameter("organization");
      if ("EJB".equals(endpoint))
      {
         if ("info".equals(method))
            info = getContactInfoEJB(organization);
      }
      if ("JSE".equals(endpoint))
      {
         if ("info".equals(method))
            info = getContactInfoJSE(organization);
      }

      PrintWriter out = res.getWriter();
      out.print(info);
      out.close();
   }

   /** Get the contact info from the EJB service */
   public String getContactInfoEJB(String organization) throws ServletException
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
         throw new ServletException(e);
      }
      catch (Exception e)
      {
         throw new ServletException("Cannot invoke webservice", e);
      }
   }

   /** Get the contact info from the JSE service */
   public String getContactInfoJSE(String organization) throws ServletException
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
         throw new ServletException(e);
      }
      catch (Exception e)
      {
         throw new ServletException("Cannot invoke webservice", e);
      }
   }
}
