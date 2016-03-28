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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Feed back the tracker messages
 */
public class HelloFeedbackServlet extends HttpServlet
{

   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
   {
      if (req.getParameter("clear") != null)
      {
         HandlerTracker.clear();
      }
      else
      {
         res.setContentType("text/plain");
         PrintWriter pw = res.getWriter();

         String[] protocol = HandlerTracker.getProtocol();
         for (int i = 0; i < protocol.length; i++)
         {
            pw.println(protocol[i]);
         }
         pw.close();
      }
   }
}
