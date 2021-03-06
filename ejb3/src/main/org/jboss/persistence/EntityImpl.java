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
package org.jboss.persistence;

import javax.persistence.Entity;

/**
 * // *
 *
 * @author <a href="mailto:bdecoste@jboss.org">William DeCoste</a>
 * @version $Revision: 57207 $
 */
public class EntityImpl implements Entity
{
   private String[] values;
   private String name;

   public EntityImpl(String[] values)
   {
      this.values = values;
   }

   public String name()
   {
      return javax.persistence.Entity.class.getName();
   }

   public Class annotationType()
   {
      return javax.persistence.Entity.class;
   }

   public String[] values()
   {
      return values;
   }
}
