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
package org.jboss.ejb3.dd;

import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

/**
 * Represents an <application-exception> element of the ejb-jar.xml deployment descriptor
 * for the 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class ApplicationExceptionList
{
   private static final Logger log = Logger.getLogger(ApplicationExceptionList.class);

   private List classList = new ArrayList();
   private List<Boolean> rollbackList = new ArrayList();

   public List getClassList()
   {
      return classList;
   }
   
   public List getRollbackList()
   {
      return rollbackList;
   }

   public void addClass(String className)
   {
      classList.add(className);
   }
   
   public void addRollback(boolean rollback)
   {
      rollbackList.add(rollback);
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("classes=").append(classList);
      sb.append("rollbacks=").append(rollbackList);
      sb.append("]");
      return sb.toString();
   }
}
