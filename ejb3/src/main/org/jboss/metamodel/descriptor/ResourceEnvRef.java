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

import org.jboss.logging.Logger;

/**
 * Represents a <resource-env-ref> element of the ejb-jar.xml deployment descriptor for the
 * 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 45240 $</tt>
 */
public class ResourceEnvRef extends Ref
{
   private static final Logger log = Logger.getLogger(ResourceEnvRef.class);
   
   private String resRefName;

   private String resType;

   private String resAuth;

   private String resSharingScope;

   private String mappedName;
   
   private String jndiName;
   
   private String resourceName;
   
   public String getResourceName()
   {
      return resourceName;
   }

   public void setResourceName(String resourceName)
   {
      this.resourceName = resourceName;
   }
   
   public String getJndiName()
   {
      return jndiName;
   }

   public void setJndiName(String jndiName)
   {
      this.jndiName = jndiName;
      this.mappedName = jndiName;
   }

   public String getMappedName()
   {
      return mappedName;
   }

   public void setMappedName(String mappedName)
   {
      this.mappedName = mappedName;
   }

   public String getResRefName()
   {
      return resRefName;
   }

   public void setResRefName(String resRefName)
   {
      this.resRefName = resRefName;
   }

   public String getResType()
   {
      return resType;
   }

   public void setResType(String resType)
   {
      this.resType = resType;
   }

   public String getResAuth()
   {
      return resAuth;
   }

   public void setResAuth(String resAuth)
   {
      this.resAuth = resAuth;
   }

   public String getAuthorizationType()
   {
      return resAuth;
   }

   public String getResSharingScope()
   {
      return resSharingScope;
   }

   public void setResSharingScope(String resSharingScope)
   {
      this.resSharingScope = resSharingScope;
   }

   public boolean isShareable()
   {
      if (resSharingScope == null || resSharingScope.equals("Shareable"))
         return true;
      else
         return false;
   }
   
   public void merge(ResourceEnvRef ref)
   {
      if (ref.getJndiName() != null)
      {
         this.setJndiName(ref.getJndiName());
         
         setMappedName(ref.getJndiName());
      }
      
      if (ref.getResourceName() != null)
      {
         this.setResourceName(ref.getResourceName());
         
         String mappedName = ref.getResourceName();
         if (mappedName.startsWith("java:"))
            this.setMappedName(ref.getResourceName());
         else
            this.setMappedName("java:" + ref.getResourceName());
      }
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[ResourceEnvRef:");
      sb.append("resRefName=").append(resRefName);
      sb.append(",resType=").append(resType);
      sb.append(",jndiName=").append(jndiName);
      sb.append(",mappedName=").append(mappedName);
      sb.append("]");
      return sb.toString();
   }
}
