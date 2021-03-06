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
package org.jboss.test.securitymgr.servlet;

import java.io.IOException;
import java.io.File;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 * Serlvet used to test security manager permission assigments.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57211 $
 */
public class FileAccessServlet extends HttpServlet
{
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException
   {
      System.out.println("FileAccessServlet, "+req.getRequestURI());
      try
      {
         resp.addHeader("X-CodeSource", ""+getClass().getProtectionDomain().getCodeSource());
         System.out.println("FileAccessServlet:CodeSource, "+getClass().getProtectionDomain().getCodeSource());
         String fileName = req.getParameter("file");
         String root = super.getServletContext().getRealPath("/");
         resp.addHeader("X-RealPath", root);
         System.out.println("FileAccessServlet:RealPath, "+root);
         File file = new File(root, fileName);
         // This triggers a read access check
         boolean exists = file.exists();
         boolean created = false;
         boolean deleted = false;
         if( exists == false )
            created = file.createNewFile();
         else
            deleted = file.delete();
         resp.addHeader("X-Exists", ""+exists);
         resp.addHeader("X-Created", ""+created);
         resp.addHeader("X-Deleted", ""+deleted);
      }
      catch(Exception e)
      {
         resp.addHeader("X-Exception", ""+e.getMessage());         
         resp.addHeader("X-ExceptionClass", e.getClass().getName());         
      }
   }
}
