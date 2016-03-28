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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import javax.ejb.TransactionManagementType;
import org.jboss.logging.Logger;

/**
 * Represents an EJB element of the ejb-jar.xml deployment descriptor for the
 * 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public abstract class EnterpriseBean implements Injectable 
{
   private static final Logger log = Logger.getLogger(EnterpriseBean.class);
   
   public static final String BEAN = "Bean";

   public static final String CONTAINER = "Container";

   // ejb-jar.xml
   private String ejbName = null;

   private String home = null;

   private String remote = null;

   private String localHome = null;

   private String local = null;

   private String ejbClass = null;

   private HashMap<String, EjbLocalRef> ejbLocalRefs = new HashMap();

   private HashMap<String, EjbRef> ejbRefs = new HashMap();

   private HashMap<String, EnvEntry> envEntries = new HashMap();

   private HashMap<String, ResourceEnvRef> resourceEnvRefs = new HashMap();
   
   private HashMap<String, ResourceRef> resourceRefs = new HashMap();
   
   private HashMap<String, WebServiceRef> webServiceRefs = new HashMap();

   private SecurityIdentity securityIdentity;

   protected TransactionManagementType tmType = null;

   private HashMap<String, MessageDestinationRef> messageDestinationRefs = new HashMap();

   // jboss.xml
   private String jndiName;

   private String localJndiName;

   private String securityDomain;

   private boolean callByValue = false;

   private ClusterConfig clusterConfig = null;

   private String aopDomainName = null;
   
   private MethodAttributes methodAttributes = null;

   private Collection<String> dependencies = new HashSet<String>();
   
   private Collection<InjectionTarget> ignoreDependencies = new HashSet<InjectionTarget>();
   
   private String interceptorStack;
   
   private String proxyFactory;
   
   public void addDependency(String depends)
   {
      dependencies.add(depends);
   }

   public Collection<String> getDependencies()
   {
      return dependencies;
   }
   
   public void addIgnoreDependency(InjectionTarget ignore)
   {
      ignoreDependencies.add(ignore);
   }
   
   public Collection<InjectionTarget> getIgnoreDependencies()
   {
      return ignoreDependencies;
   }

   public void mergeMessageDestinationRef(MessageDestinationRef ref)
   {
      MessageDestinationRef tmpRef = messageDestinationRefs.get(ref.getMessageDestinationRefName());
      if (tmpRef != null)
         tmpRef.merge(ref);
   }

   public void mergeResourceRef(ResourceRef ref)
   {
      ResourceRef tmpRef = resourceRefs.get(ref.getResRefName());
      if (tmpRef != null)
         tmpRef.merge(ref);
   }
   
   public void mergeResourceEnvRef(ResourceEnvRef ref)
   {
      ResourceEnvRef tmpRef = resourceEnvRefs.get(ref.getResRefName());
      if (tmpRef != null)
         tmpRef.merge(ref);
   }
   
   public void setMethodAttributes(MethodAttributes methodAttributes)
   {
      this.methodAttributes = methodAttributes;
   }
   
   public MethodAttributes getMethodAttributes()
   {
      return methodAttributes;
   }
   
   public String getInterceptorStack()
   {
      return interceptorStack;
   }
   
   public void setInterceptorStack(String interceptorStack)
   {
      this.interceptorStack = interceptorStack;
   }
   
   public String getProxyFactory()
   {
      return proxyFactory;
   }
   
   public void setProxyFactory(String proxyFactory)
   {
      this.proxyFactory = proxyFactory;
   }

   public void updateEjbRef(EjbRef updatedRef)
   {
      EjbRef ref = ejbRefs.get(updatedRef.getEjbRefName());
      if (ref != null)
      {
         ref.setMappedName(updatedRef.getMappedName());
         ref.setIgnoreDependency(updatedRef.isIgnoreDependency());
      }
      else
      {
         ejbRefs.put(updatedRef.getEjbRefName(), updatedRef);
      }
   }

   public void updateEjbLocalRef(EjbLocalRef updatedRef)
   {
      EjbLocalRef ref = ejbLocalRefs.get(updatedRef.getEjbRefName());
      if (ref != null)
      {
         ref.setMappedName(updatedRef.getMappedName());
         ref.setIgnoreDependency(updatedRef.isIgnoreDependency());
      }
      else
      {
         ejbLocalRefs.put(updatedRef.getEjbRefName(), updatedRef);
      }

   }

   public void setAopDomainName(String aopDomainName)
   {
      this.aopDomainName = aopDomainName;
   }

   public String getAopDomainName()
   {
      return aopDomainName;
   }

   public void setClusterConfig(ClusterConfig clusterConfig)
   {
      this.clusterConfig = clusterConfig;
   }

   public boolean isClustered()
   {
      return clusterConfig != null;
   }

   public ClusterConfig getClusterConfig()
   {
      return clusterConfig;
   }

   public void setRunAsPrincipal(String principal)
   {
      if (securityIdentity != null)
         securityIdentity.setRunAsPrincipal(principal);
   }

   public void setCallByValue(boolean callByValue)
   {
      this.callByValue = callByValue;
   }

   public boolean isCallByValue()
   {
      return callByValue;
   }

   public String getSecurityDomain()
   {
      return securityDomain;
   }

   public void setSecurityDomain(String securityDomain)
   {
      this.securityDomain = securityDomain;
   }

   public Collection<MessageDestinationRef> getMessageDestinationRefs()
   {
      return messageDestinationRefs.values();
   }

   public void addMessageDestinationRef(MessageDestinationRef messageDestinationRef)
   {
      messageDestinationRefs.put(messageDestinationRef.getMessageDestinationRefName(), messageDestinationRef);
   }

   public String getJndiName()
   {
      return jndiName;
   }

   public void setJndiName(String jndiName)
   {
      this.jndiName = jndiName;
   }

   public String getLocalJndiName()
   {
      return localJndiName;
   }

   public void setLocalJndiName(String localJndiName)
   {
      this.localJndiName = localJndiName;
   }

   public TransactionManagementType getTransactionManagementType()
   {
      return tmType;
   }
   
   public void setTransactionManagementType(String transactionType)
   {
      if (transactionType.equals(BEAN))
         tmType = TransactionManagementType.BEAN;
      else if (transactionType.equals(CONTAINER))
         tmType = TransactionManagementType.CONTAINER;
   }

   public boolean isSessionBean()
   {
      return this instanceof SessionEnterpriseBean;
   }
   
   public boolean isConsumer()
   {
      return this instanceof Consumer;
   }

   public boolean isEntityBean()
   {
      return this instanceof EntityEnterpriseBean;
   }

   public boolean isMessageDrivenBean()
   {
      return this instanceof MessageDrivenBean;
   }
   
   public boolean isService()
   {
      return this instanceof Service;
   }

   public String getEjbName()
   {
      return ejbName;
   }

   public void setEjbName(String ejbName)
   {
      this.ejbName = ejbName;
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

   public String getLocalHome()
   {
      return localHome;
   }

   public void setLocalHome(String localHome)
   {
      this.localHome = localHome;
   }

   public String getLocal()
   {
      return local;
   }

   public void setLocal(String local)
   {
      this.local = local;
   }

   public String getEjbClass()
   {
      return ejbClass;
   }

   public void setEjbClass(String ejbClass)
   {
      this.ejbClass = ejbClass;
   }

   public Collection<EjbLocalRef> getEjbLocalRefs()
   {
      return ejbLocalRefs.values();
   }

   public void addEjbLocalRef(EjbLocalRef ref)
   {
      ejbLocalRefs.put(ref.getEjbRefName(), ref);
   }

   public Collection<EjbRef> getEjbRefs()
   {
      return ejbRefs.values();
   }

   public void addEjbRef(EjbRef ref)
   {
      ejbRefs.put(ref.getEjbRefName(), ref);
   }

   public Collection<EnvEntry> getEnvEntries()
   {
      return envEntries.values();
   }

   public void addEnvEntry(EnvEntry entry)
   {
      envEntries.put(entry.getEnvEntryName(), entry);
   }

   public Collection<ResourceEnvRef> getResourceEnvRefs()
   {
      return resourceEnvRefs.values();
   }

   public void addResourceEnvRef(ResourceEnvRef envRef)
   {
      resourceEnvRefs.put(envRef.getResRefName(), envRef);
   }
   
   public Collection<ResourceRef> getResourceRefs()
   {
      return resourceRefs.values();
   }

   public void addResourceRef(ResourceRef envRef)
   {
      resourceRefs.put(envRef.getResRefName(), envRef);
   }
   
   public Collection<WebServiceRef> getWebServiceRefs()
   {
      return webServiceRefs.values();
   }

   public void addWebServiceRef(WebServiceRef ref)
   {
      webServiceRefs.put(ref.getServiceRefName(), ref);
   }

   public SecurityIdentity getSecurityIdentity()
   {
      return securityIdentity;
   }

   public void setSecurityIdentity(SecurityIdentity securityIdentity)
   {
      this.securityIdentity = securityIdentity;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("ejbName=").append(ejbName);
      sb.append(",jndiName=").append(jndiName);
      sb.append(",local=").append(local);
      sb.append(",remote=").append(remote);
      sb.append(",home=").append(home);
      sb.append(",localHome=").append(localHome);
      sb.append(",ejbClass=").append(ejbClass);
      sb.append(",ejbRefs=").append(ejbRefs);
      sb.append(",ejbLocalRefs=").append(ejbLocalRefs);
      sb.append(",resourceRefs=").append(resourceRefs);
      sb.append(",resourceEnvRefs=").append(resourceEnvRefs);
      sb.append(",methodAttributes=").append(methodAttributes);
      sb.append(",aopDomainName=").append(aopDomainName);
      sb.append(",dependencies=").append(dependencies);
      return sb.toString();
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
