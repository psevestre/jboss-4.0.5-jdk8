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
package org.jboss.test.webservice.benchmark;

import java.io.Serializable;

/**
 * A SimpleUserType.
 * 
 * @author <a href="anders.hedstrom@home.se">Anders Hedström</a>
 * @version $Revision: 57211 $
 */
public class SimpleUserType implements Serializable
{

   private int i;
   private float f;
   private String s;
   
   public SimpleUserType()
   {
   }

   public SimpleUserType(int i, float f, String s)
   {
      this.i = i;
      this.f = f;
      this.s = s;
   }
   
   public float getF()
   {
      return f;
   }
   public void setF(float f)
   {
      this.f = f;
   }
   public int getI()
   {
      return i;
   }
   public void setI(int i)
   {
      this.i = i;
   }
   public String getS()
   {
      return s;
   }
   public void setS(String s)
   {
      this.s = s;
   }
   
   public boolean equals(Object obj) 
   {
      if (!(obj instanceof SimpleUserType)) return false;
      return toString().equals("" + obj);
   }
   
   public String toString() 
   {
      return "[i=" + i + ",f=" + f + ",s=" + s + "]";
   }
}
