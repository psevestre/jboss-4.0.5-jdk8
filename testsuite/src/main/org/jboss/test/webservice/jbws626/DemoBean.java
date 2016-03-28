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
package org.jboss.test.webservice.jbws626;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.jboss.logging.Logger;

public class DemoBean implements SessionBean
{

   private static final long serialVersionUID = 1L;

   private Logger log = Logger.getLogger(DemoBean.class);

   public void ejbCreate()
   {
   }

   public ValueObj[] getArray()
   {
      log.info("getArray");
      return new ValueObj[]
      {new ValueObj("a", "b"), new ValueObj("c", "d")};
   }

   public ValueObj[] getEmptyArray()
   {
      log.info("getEmptyArray");
      return new ValueObj[0];
   }

   public ValueObj[] getNullArray()
   {
      log.info("getNullArray");
      return null;
   }

   public ValueObj[] echoArray(final ValueObj[] array)
   {
      return array;
   }

   public void ejbActivate()
   {
   }

   public void ejbPassivate()
   {
   }

   public void ejbRemove()
   {
   }

   public void setSessionContext(final SessionContext context)
   {
   }

}
