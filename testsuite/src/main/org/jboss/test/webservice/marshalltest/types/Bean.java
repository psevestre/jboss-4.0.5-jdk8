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
package org.jboss.test.webservice.marshalltest.types;

import java.util.Arrays;

public class Bean implements java.io.Serializable
{
   static final long serialVersionUID = 6584528323279029461L;
   private int x;
   private int y;
   private byte[] base64;

   public Bean()
   {
   }

   public int getX()
   {
      return x;
   }

   public void setX(int x)
   {
      this.x = x;
   }

   public int getY()
   {
      return y;
   }

   public void setY(int y)
   {
      this.y = y;
   }

   public byte[] getBase64()
   {
      return base64;
   }

   public void setBase64(byte[] base64)
   {
      this.base64 = base64;
   }

   public boolean equals(Object o)
   {
      if (this == o) return true;
      if (!(o instanceof Bean)) return false;

      final Bean bean = (Bean)o;

      if (x != bean.x) return false;
      if (y != bean.y) return false;
      if (!Arrays.equals(base64, bean.base64)) return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = x;
      result = 29 * result + y;
      return result;
   }

   public String toString()
   {
      String str = (base64 != null ? new String(base64) : null);
      return "[x=" + x + ",y=" + y + ",base64=" + str + "]";
   }
}
