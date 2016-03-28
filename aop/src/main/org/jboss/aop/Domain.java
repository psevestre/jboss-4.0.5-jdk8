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
package org.jboss.aop;

import org.jboss.aop.advice.AdviceStack;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.pointcut.Typedef;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57185 $
 */
public class Domain extends AspectManager
{
   protected AspectManager parent;
   protected boolean parentFirst;

   public Domain(AspectManager manager, boolean parentFirst)
   {
      this.parent = manager;
      this.parentFirst = parentFirst;
   }

   public InterceptorFactory getInterceptorFactory(String name)
   {
      InterceptorFactory factory = null;
      if (parentFirst)
      {
         factory = parent.getInterceptorFactory(name);
         if (factory != null) return factory;
      }
      factory = super.getInterceptorFactory(name);

      if (factory != null) return factory;
      return parent.getInterceptorFactory(name);
   }

   public AdviceStack getAdviceStack(String name)
   {
      AdviceStack factory = null;
      if (parentFirst)
      {
         factory = parent.getAdviceStack(name);
         if (factory != null) return factory;
      }
      factory = super.getAdviceStack(name);

      if (factory != null) return factory;
      return parent.getAdviceStack(name);
   }

   public Object getPerVMAspect(AspectDefinition def)
   {
      Object factory = null;
      if (parentFirst)
      {
         factory = parent.getPerVMAspect(def);
         if (factory != null) return factory;
      }
      factory = super.getPerVMAspect(def);

      if (factory != null) return factory;
      return parent.getPerVMAspect(def);
   }

   public Object getPerVMAspect(String def)
   {
      Object factory = null;
      if (parentFirst)
      {
         factory = parent.getPerVMAspect(def);
         if (factory != null) return factory;
      }
      factory = super.getPerVMAspect(def);

      if (factory != null) return factory;
      return parent.getPerVMAspect(def);
   }

   public AspectDefinition getAspectDefinition(String name)
   {
      AspectDefinition factory = null;
      if (parentFirst)
      {
         factory = parent.getAspectDefinition(name);
         if (factory != null) return factory;
      }
      factory = super.getAspectDefinition(name);

      if (factory != null) return factory;
      return parent.getAspectDefinition(name);
   }

   public Typedef getTypedef(String name)
   {
      Typedef factory = null;
      if (parentFirst)
      {
         factory = parent.getTypedef(name);
         if (factory != null) return factory;
      }
      factory = super.getTypedef(name);

      if (factory != null) return factory;
      return parent.getTypedef(name);
   }

}
