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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 57207 $
 */
public class Interceptor implements Injectable
{
   String interceptorClass;
   Method aroundInvoke;
   Method postConstruct;
   Method postActivate;
   Method preDestroy;
   Method prePassivate;
   
   private List<EnvEntry> envEntries = new ArrayList();

   private List<EjbLocalRef> ejbLocalRefs = new ArrayList();

   private List<EjbRef> ejbRefs = new ArrayList();

   private List<ResourceEnvRef> resourceEnvRefs = new ArrayList<ResourceEnvRef>();
   
   private List<ResourceRef> resourceRefs = new ArrayList<ResourceRef>();
   
   private List messageDestinationRefs = new ArrayList();
   
   private HashMap<String, WebServiceRef> webServiceRefs = new HashMap();

   public Interceptor()
   {
   }

   public Method getAroundInvoke()
   {
      return aroundInvoke;
   }

   public void setAroundInvoke(Method aroundInvoke)
   {
      this.aroundInvoke = aroundInvoke;
   }

   public String getInterceptorClass()
   {
      return interceptorClass;
   }

   public void setInterceptorClass(String interceptorClass)
   {
      this.interceptorClass = interceptorClass;
   }

   public Method getPostActivate()
   {
      return postActivate;
   }

   public void setPostActivate(Method postActivate)
   {
      this.postActivate = postActivate;
   }

   public Method getPostConstruct()
   {
      return postConstruct;
   }

   public void setPostConstruct(Method postConstruct)
   {
      this.postConstruct = postConstruct;
   }

   public Method getPreDestroy()
   {
      return preDestroy;
   }

   public void setPreDestroy(Method preDestroy)
   {
      this.preDestroy = preDestroy;
   }

   public Method getPrePassivate()
   {
      return prePassivate;
   }

   public void setPrePassivate(Method prePassivate)
   {
      this.prePassivate = prePassivate;
   }

   public List getEnvEntries()
   {
      return envEntries;
   }

   public void addEnvEntry(EnvEntry entry)
   {
      envEntries.add(entry);
   }

   public List<EjbLocalRef> getEjbLocalRefs()
   {
      return ejbLocalRefs;
   }

   public void setEjbLocalRefs(List ejbLocalRefs)
   {
      this.ejbLocalRefs = ejbLocalRefs;
   }

   public void addEjbLocalRef(EjbLocalRef ref)
   {
      ejbLocalRefs.add(ref);
   }

   public List<EjbRef> getEjbRefs()
   {
      return ejbRefs;
   }

   public void setEjbRefs(List ejbRefs)
   {
      this.ejbRefs = ejbRefs;
   }

   public void addEjbRef(EjbRef ref)
   {
      ejbRefs.add(ref);
   }

   public List<ResourceEnvRef> getResourceEnvRefs()
   {
      return resourceEnvRefs;
   }

   public void addResourceEnvRef(ResourceEnvRef envRef)
   {
      resourceEnvRefs.add(envRef);
   }
   
   public List<ResourceRef> getResourceRefs()
   {
      return resourceRefs;
   }

   public void addResourceRef(ResourceRef envRef)
   {
      resourceRefs.add(envRef);
   }

   public List<MessageDestinationRef> getMessageDestinationRefs()
   {
      return messageDestinationRefs;
   }

   public void addMessageDestinationRef(MessageDestinationRef messageDestinationRef)
   {
      messageDestinationRefs.add(messageDestinationRef);
   }
   
   public Collection<WebServiceRef> getWebServiceRefs()
   {
      return webServiceRefs.values();
   }

   public void addWebServiceRef(WebServiceRef ref)
   {
      webServiceRefs.put(ref.getServiceRefName(), ref);
   }

   private List<PersistenceContextRef> persistenceContextRefs = new ArrayList<PersistenceContextRef>();
   private List<PersistenceUnitRef> persistenceUnitRefs = new ArrayList<PersistenceUnitRef>();

   public List<PersistenceContextRef> getPersistenceContextRefs()
   {
      return persistenceContextRefs;
   }

   public List<PersistenceUnitRef> getPersistenceUnitRefs()
   {
      return persistenceUnitRefs;
   }

   public void addPersistenceContextRef(PersistenceContextRef ref)
   {
      persistenceContextRefs.add(ref);
   }

   public void addPersistenceUnitRef(PersistenceUnitRef ref)
   {
      persistenceUnitRefs.add(ref);
   }

}
