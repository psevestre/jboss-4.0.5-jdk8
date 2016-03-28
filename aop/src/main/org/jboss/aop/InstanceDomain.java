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

import java.util.LinkedHashMap;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.metadata.ClassMetaDataBinding;

/**
 * comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public class InstanceDomain extends Domain
{
   protected Advisor advisor;

   public InstanceDomain(AspectManager manager, boolean parentFirst)
   {
      super(manager, parentFirst);
   }

   public Advisor getAdvisor()
   {
      return advisor;
   }

   public void setAdvisor(Advisor advisor)
   {
      this.advisor = advisor;
   }

   public synchronized void addBinding(AdviceBinding binding)
   {
      removeBinding(binding.getName());
      synchronized (bindings)
      {
         bindings.put(binding.getName(), binding);
      }
      if (advisor != null) advisor.newBindingAdded();
   }

   public void addClassMetaData(ClassMetaDataBinding meta)
   {
      removeClassMetaData(meta.getName());
      if (advisor != null)
      {
         if (meta.matches(advisor, advisor.getClazz()))
         {
            meta.addAdvisor(advisor);
         }
      }
   }

   public LinkedHashMap getBindings()
   {
      LinkedHashMap map = new LinkedHashMap(parent.getBindings());
      map.putAll(this.bindings);
      return map;
   }

   /**
    * internal to jboss aop.  Do not call
    * <p/>
    * This is overriden so that AdviceBinding can clear an advisor that is created per-instance.
    *
    * @param advisor
    * @return
    */
   public boolean isAdvisorRegistered(Advisor advisor)
   {
      return advisor == advisor;
   }
}
