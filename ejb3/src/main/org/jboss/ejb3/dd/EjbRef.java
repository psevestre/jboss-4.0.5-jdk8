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

/**
 * Represents an ejb-ref element of the ejb-jar.xml deployment descriptor for
 * the 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class EjbRef
{
   private String ejbRefName;

   private String ejbRefType;

   private String home;
   
   private String remote;

   private String ejbLink;

   private String mappedName;

   private InjectionTarget injectionTarget;
   private boolean ignoreDependency;

   public boolean isIgnoreDependency()
   {
      return ignoreDependency;
   }

   public void setIgnoreDependency(boolean ignoreDependency)
   {
      this.ignoreDependency = ignoreDependency;
   }

   public String getMappedName()
   {
      return mappedName;
   }

   public void setMappedName(String mappedName)
   {
      this.mappedName = mappedName;
   }

   public InjectionTarget getInjectionTarget()
   {
      return injectionTarget;
   }

   public void setInjectionTarget(InjectionTarget injectionTarget)
   {
      this.injectionTarget = injectionTarget;
   }

   public String getEjbRefName()
   {
      return ejbRefName;
   }

   public void setEjbRefName(String ejbRefName)
   {
      this.ejbRefName = ejbRefName;
   }

   public String getEjbRefType()
   {
      return ejbRefType;
   }

   public void setEjbRefType(String ejbRefType)
   {
      this.ejbRefType = ejbRefType;
   }

   public String getHome()
   {
      return home;
   }

   public void setHome(String home)
   {
      this.home = home;
   }

   public String getRemote()
   {
      return remote;
   }

   public void setRemote(String remote)
   {
      this.remote = remote;
   }

   public String getEjbLink()
   {
      return ejbLink;
   }

   public void setEjbLink(String ejbLink)
   {
      this.ejbLink = ejbLink;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("ejbRefName=").append(ejbRefName);
      sb.append(", ejbRefType=").append(ejbRefType);
      sb.append(", injectionTarget=").append(injectionTarget);
      sb.append(", remote=").append(remote);
      sb.append(", jndiName=").append(mappedName);
      sb.append("]");
      return sb.toString();
   }
}
