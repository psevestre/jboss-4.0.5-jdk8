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
package org.jboss.test.xml.multispaced;

/**
 * @version <tt>$Revision: 57211 $</tt>
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public class XMBeanOperationMetaData
{
   private String description;
   private String name;
   private String returnType;

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getReturnType()
   {
      return returnType;
   }

   public void setReturnType(String returnType)
   {
      this.returnType = returnType;
   }

   public String toString()
   {
      return "[name=" + name + ", return-type=" + returnType + ", description=" + description + ']';
   }

   public boolean equals(Object o)
   {
      if(this == o) return true;
      if(!(o instanceof XMBeanOperationMetaData)) return false;

      final XMBeanOperationMetaData mBeanOperationMetaData = (XMBeanOperationMetaData)o;

      if(description != null ? !description.equals(mBeanOperationMetaData.description) : mBeanOperationMetaData.description != null) return false;
      if(name != null ? !name.equals(mBeanOperationMetaData.name) : mBeanOperationMetaData.name != null) return false;
      if(returnType != null ? !returnType.equals(mBeanOperationMetaData.returnType) : mBeanOperationMetaData.returnType != null) return false;

      return true;
   }

   public int hashCode()
   {
      int result;
      result = (description != null ? description.hashCode() : 0);
      result = 29 * result + (name != null ? name.hashCode() : 0);
      result = 29 * result + (returnType != null ? returnType.hashCode() : 0);
      return result;
   }
}
