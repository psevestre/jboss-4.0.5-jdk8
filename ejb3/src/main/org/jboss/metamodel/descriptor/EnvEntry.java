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
package org.jboss.metamodel.descriptor;

/**
 * Represents an env-entry element of the ejb-jar.xml deployment descriptor for
 * the 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 45240 $</tt>
 */
public class EnvEntry extends Ref
{
   private String description;

   private String envEntryName;

   private String envEntryType;

   private String envEntryValue;

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getEnvEntryName()
   {
      return envEntryName;
   }

   public void setEnvEntryName(String envEntryName)
   {
      this.envEntryName = envEntryName;
   }

   public String getEnvEntryType()
   {
      return envEntryType;
   }

   public void setEnvEntryType(String envEntryType)
   {
      this.envEntryType = envEntryType;
   }

   public String getEnvEntryValue()
   {
      return envEntryValue;
   }

   public void setEnvEntryValue(String envEntryValue)
   {
      this.envEntryValue = envEntryValue;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("envEntryName=").append(envEntryName);
      sb.append(",envEntryType=").append(envEntryType);
      sb.append(",envEntryValue=").append(envEntryValue);
      sb.append("]");
      return sb.toString();
   }
}
