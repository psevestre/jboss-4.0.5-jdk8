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
package org.jboss.test.webservice.samples2;

/**
 * An example of a complex user type
 *
 * @author Thomas.Diesler@jboss.org
 * @since 12-Feb-2005
 */
public class OrderItem
{
   private String name;
   private int quantity;

   public OrderItem()
   {
   }

   public OrderItem(String name, int quantity)
   {
      this.quantity = quantity;
      this.name = name;
   }

   public int getQuantity()
   {
      return quantity;
   }

   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof OrderItem)) return false;

      final OrderItem orderItem = (OrderItem)o;

      if (quantity != orderItem.quantity) return false;
      if (!name.equals(orderItem.name)) return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = name.hashCode();
      result = 29 * result + quantity;
      return result;
   }

   public String toString()
   {
      return "[name=" + name + ",quantity=" + quantity + "]";
   }
}
