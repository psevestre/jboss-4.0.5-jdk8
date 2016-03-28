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

import org.jboss.logging.Logger;
import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.xml.sax.Attributes;

/**
 * Represents the jboss.xml deployment descriptor for the 2.1 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class JBossDDObjectFactory implements ObjectModelFactory
{
   private static final Logger log = Logger
           .getLogger(JBossDDObjectFactory.class);

   private EjbJarDD dd;
   private Class ejbClass;

   public JBossDDObjectFactory(EjbJarDD dd)
   {
      super();
      this.dd = dd;
   }

   /**
    * Return the root.
    */
   public Object newRoot(Object root, UnmarshallingContext navigator,
                         String namespaceURI, String localName, Attributes attrs)
   {
      return dd;
   }

   public Object completeRoot(Object root, UnmarshallingContext ctx,
                              String uri, String name)
   {
      return root;
   }

   // Methods discovered by introspection

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbJarDD dd, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("enterprise-beans"))
      {
         child = dd.getEnterpriseBeans();
         if (child == null)
         {
            dd.setEnterpriseBeans(new EnterpriseBeans());
            child = dd.getEnterpriseBeans();
         }
      }
      else if (localName.equals("assembly-descriptor"))
      {
         child = dd.getAssemblyDescriptor();
         if (child == null)
         {
            dd.setAssemblyDescriptor(new AssemblyDescriptor());
            child = dd.getAssemblyDescriptor();
         }
      }
      else if (localName.equals("resource-manager"))
      {
         child = new ResourceManager();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(Consumer consumer, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("current-message"))
      {
         child = new CurrentMessage();
      }
      else if (localName.equals("message-properties"))
      {
         child = new MessageProperties();
      }
      else if (localName.equals("producer"))
      {
         child = new Producer(false);
      }
      else if (localName.equals("local-producer"))
      {
         child = new Producer(true);
      }
      else if (localName.equals("ejb-ref"))
      {
         child = new EjbRef();
      }
      else if (localName.equals("ejb-local-ref"))
      {
         child = new EjbLocalRef();
      }
      else if (localName.equals("resource-ref"))
      {
         child = new ResourceRef();
      }
      else if (localName.equals("resource-env-ref"))
      {
         child = new ResourceEnvRef();
      }
      else if (localName.equals("message-destination-ref"))
      {
         child = new MessageDestinationRef();
      }
      else if (localName.equals("ignore-dependency"))
      {
         child = new InjectionTarget();
      }
      
      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(Service service, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("ejb-ref"))
      {
         child = new EjbRef();
      }
      else if (localName.equals("ejb-local-ref"))
      {
         child = new EjbLocalRef();
      }
      else if (localName.equals("resource-ref"))
      {
         child = new ResourceRef();
      }
      else if (localName.equals("resource-env-ref"))
      {
         child = new ResourceEnvRef();
      }
      else if (localName.equals("message-destination-ref"))
      {
         child = new MessageDestinationRef();
      }
      else if (localName.equals("ignore-dependency"))
      {
         child = new InjectionTarget();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(CurrentMessage message, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("method"))
      {
         child = new Method();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(MessageProperties properties, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("method"))
      {
         child = new Method();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(MethodAttributes attributes, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("method"))
      {
         child = new Method();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(AssemblyDescriptor descriptor,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("message-destination"))
      {
         child = new MessageDestination();
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EnterpriseBeans ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("session"))
      {
         ejbClass = SessionEnterpriseBean.class;
         child = ejbs;
      }
      else if (localName.equals("message-driven"))
      {
         ejbClass = MessageDrivenBean.class;
         child = ejbs;
      }
      else if (localName.equals("service"))
      {
         child = new Service();
      }
      else if (localName.equals("consumer"))
      {
         child = new Consumer();
      }
      else if (localName.equals("ejb-ref"))
      {
         child = new EjbRef();
      }
      else if (localName.equals("resource-ref"))
      {
         child = new ResourceRef();
      }
      else if (localName.equals("resource-env-ref"))
      {
         child = new ResourceEnvRef();
      }
      else if (localName.equals("env-entry"))
      {
         child = new EnvEntry();
      }
      else if (localName.equals("message-destination-ref"))
      {
         child = new MessageDestinationRef();
      }
      else if (localName.equals("method-attributes"))
      {
         child = new MethodAttributes();
      }
      else if (localName.equals("ignore-dependency"))
      {
         child = new InjectionTarget();
      }
      
      return child;
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, Producer producer,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      if (producer.isLocal())
         parent.addLocalProducer(producer);
      else
         parent.addProducer(producer);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, MessageDestination destination,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMessageDestination(destination);
   }
   
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, MethodAttributes attributes,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setMethodAttributes(attributes);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, InjectionTarget ignoreDependency,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addIgnoreDependency(ignoreDependency);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MethodAttributes parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, CurrentMessage message,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setCurrentMessage(message);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, MessageProperties message,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setMessageProperties(message);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(CurrentMessage parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageProperties parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, Service service,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnterpriseBean(service);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Service parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Service parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbLocalRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Service parent, ResourceRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Service parent, ResourceEnvRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbLocalRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, ResourceRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Consumer parent, ResourceEnvRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, Consumer consumer,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnterpriseBean(consumer);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbJarDD parent, ResourceManager manager,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceManager(manager);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, EnterpriseBeans ejbs,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {

   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.updateEjbRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.updateEjbLocalRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, ClusterConfig config,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setClusterConfig(config);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, ResourceRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, MessageDestinationRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMessageDestinationRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, ResourceEnvRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(ref);
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ResourceManager manager, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("res-name"))
      {
         manager.setResourceName(value);
      }
      else if (localName.equals("res-jndi-name"))
      {
         manager.setResourceJndiName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MessageDestinationRef envRef, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("message-destination-ref-name"))
      {
         envRef.setMessageDestinationRefName(value);
      }
      else if (localName.equals("jndi-name"))
      {
         envRef.setJndiName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MessageDestination destination, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("message-destination-name"))
      {
         destination.setMessageDestinationName(value);
      }
      else if (localName.equals("jndi-name"))
      {
         destination.setJndiName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ResourceRef envRef, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("res-ref-name")
              || localName.equals("resource-env-ref-name"))
      {
         envRef.setResRefName(value);
      }
      else if (localName.equals("res-type")
              || localName.equals("resource-env-ref-type"))
      {
         envRef.setResType(value);
      }
      else if (localName.equals("res-auth"))
      {
         envRef.setResAuth(value);
      }
      else if (localName.equals("res-sharing-scope"))
      {
         envRef.setResSharingScope(value);
      }
      else if (localName.equals("mapped-name"))
      {
         envRef.setMappedName(value);
      }
      else if (localName.equals("resource-name"))
      {
         envRef.setResourceName(value);
         envRef.setMappedName(value);
      }
      else if (localName.equals("jndi-name"))
      {
         envRef.setJndiName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ResourceEnvRef envRef, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("res-ref-name")
              || localName.equals("resource-env-ref-name"))
      {
         envRef.setResRefName(value);
      }
      else if (localName.equals("res-type")
              || localName.equals("resource-env-ref-type"))
      {
         envRef.setResType(value);
      }
      else if (localName.equals("res-auth"))
      {
         envRef.setResAuth(value);
      }
      else if (localName.equals("res-sharing-scope"))
      {
         envRef.setResSharingScope(value);
      }
      else if (localName.equals("mapped-name"))
      {
         envRef.setMappedName(value);
      }
      else if (localName.equals("jndi-name"))
      {
         envRef.setJndiName(value);
      }
   }
   
   public void setValue(EnterpriseBeans ejbs, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-name"))
      {
         ejbs.setCurrentEjbName(value, ejbClass);
      }
      else if (localName.equals("jndi-name"))
      {
         ejbs.setJndiName(value);
      }
      else if (localName.equals("local-jndi-name"))
      {
         ejbs.setLocalJndiName(value);
      }
      else if (localName.equals("security-domain"))
      {
         ejbs.setSecurityDomain(value);
      }
      else if (localName.equals("depends"))
      {
         ejbs.addDependency(value);
      }
      else if (localName.equals("run-as-principal"))
      {
         ejbs.setRunAsPrincipal(value);
      }
      else if (localName.equals("aop-domain-name"))
      {
         ejbs.setAopDomainName(value);
      }
      else if (localName.equals("resource-adapter-name"))
      {
         ejbs.setResourceAdapterName(value);
      }
      else if (localName.equals("destination-jndi-name"))
      {
         ejbs.setDestinationJndiName(value);
      }
      else if (localName.equals("mdb-user"))
      {
         ejbs.setMdbUser(value);
      }
      else if (localName.equals("mdb-passwd"))
      {
         ejbs.setMdbPassword(value);
      }
      else if (localName.equals("interceptor-stack"))
      {
         ejbs.setInterceptorStack(value);
      }
      else if (localName.equals("proxy-factory"))
      {
         ejbs.setProxyFactory(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Service service, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-name"))
      {
         service.setEjbName(value);
      }
      if (localName.equals("object-name"))
      {
         service.setObjectName(value);
      }
      else if (localName.equals("ejb-class"))
      {
         service.setEjbClass(value);
      }
      else if (localName.equals("local"))
      {
         service.setLocal(value);
      }
      else if (localName.equals("remote"))
      {
         service.setRemote(value);
      }
      else if (localName.equals("management"))
      {
         service.setManagement(value);
      }
      else if (localName.equals("jndi-name"))
      {
         service.setJndiName(value);
      }
      else if (localName.equals("local-jndi-name"))
      {
         service.setLocalJndiName(value);
      }
      else if (localName.equals("security-domain"))
      {
         service.setSecurityDomain(value);
      }
      else if (localName.equals("aop-domain-name"))
      {
         service.setAopDomainName(value);
      }
      else if (localName.equals("depends"))
      {
         service.addDependency(value);
      }
      else if (localName.equals("interceptor-stack"))
      {
         service.setInterceptorStack(value);
      }
      else if (localName.equals("proxy-factory"))
      {
         service.setProxyFactory(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Consumer consumer, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("message-destination"))
      {
         consumer.setDestination(value);
      }
      else if (localName.equals("message-destination-type"))
      {
         consumer.setDestinationType(value);
      }
      else if (localName.equals("ejb-class"))
      {
         consumer.setEjbClass(value);
         consumer.setEjbName(value);
      }
      else if (localName.equals("local"))
      {
         consumer.setLocal(value);
      }
      else if (localName.equals("remote"))
      {
         consumer.setRemote(value);
      }
      else if (localName.equals("jndi-name"))
      {
         consumer.setJndiName(value);
      }
      else if (localName.equals("local-jndi-name"))
      {
         consumer.setLocalJndiName(value);
      }
      else if (localName.equals("security-domain"))
      {
         consumer.setSecurityDomain(value);
      }
      else if (localName.equals("run-as-principal"))
      {
         consumer.setRunAsPrincipal(value);
      }
      else if (localName.equals("aop-domain-name"))
      {
         consumer.setAopDomainName(value);
      }
      else if (localName.equals("depends"))
      {
         consumer.addDependency(value);
      }
      else if (localName.equals("interceptor-stack"))
      {
         consumer.setInterceptorStack(value);
      }
      else if (localName.equals("proxy-factory"))
      {
         consumer.setProxyFactory(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MessageProperties properties, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("delivery"))
      {
         properties.setDelivery(value);
      }
      else if (localName.equals("class"))
      {
         properties.setClassName(value);
      }
      else if (localName.equals("priority"))
      {
         properties.setPriority(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ClusterConfig config, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("load-balance-policy"))
      {
         config.setLoadBalancePolicy(value);
      }
      else if (localName.equals("partition-name"))
      {
         config.setPartition(value);
      }
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbRef ref,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("ignore-dependency"))
      {
         ref.setIgnoreDependency(true);
      }
      else if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbLocalRef ref, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("ignore-dependency"))
      {
         ref.setIgnoreDependency(true);
      }
      else if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(MessageDestinationRef ref, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

     if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EnvEntry ref, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(ResourceRef ref, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

     if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(ResourceEnvRef ref, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

     if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ref.setInjectionTarget(target);
         child = target;
      }
      return child;
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(InjectionTarget target, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("injection-target-class"))
      {
         target.setTargetClass(value);
      }
      else if (localName.equals("injection-target-name"))
      {
         target.setTargetName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-ref-name"))
      {
         ref.setEjbRefName(value);
      }
      else if (localName.equals("ejb-ref-type"))
      {
         ref.setEjbRefType(value);
      }
      else if (localName.equals("home"))
      {
         ref.setHome(value);
      }
      else if (localName.equals("remote"))
      {
         ref.setRemote(value);
      }
      else if (localName.equals("ejb-link"))
      {
         ref.setEjbLink(value);
      }
      else if (localName.equals("mapped-name"))
      {
         ref.setMappedName(value);
      }
      else if (localName.equals("jndi-name"))
      {
         ref.setMappedName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Method method, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("method-name"))
      {
         method.setMethodName(value);
      }
      else if (localName.equals("transaction-timeout"))
      {
         method.setTransactionTimeout(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbLocalRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-ref-name"))
      {
         ref.setEjbRefName(value);
      }
      else if (localName.equals("local-jndi-name"))
      {
         ref.setMappedName(value);
      }
      else if (localName.equals("ignore-dependency"))
      {
         ref.setIgnoreDependency(true);
      }
      else if (localName.equals("ejb-ref-type"))
      {
         ref.setEjbRefType(value);
      }
      else if (localName.equals("local-home"))
      {
         ref.setLocalHome(value);
      }
      else if (localName.equals("local"))
      {
         ref.setLocal(value);
      }
      else if (localName.equals("ejb-link"))
      {
         ref.setEjbLink(value);
      }
      else if (localName.equals("mapped-name"))
      {
         ref.setMappedName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbJarDD dd, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("security-domain"))
      {
         dd.setSecurityDomain(value);
      }
      else if (localName.equals("unauthenticated-principal"))
      {
         dd.setUnauthenticatedPrincipal(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Producer producer, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("class"))
      {
         producer.setClassName(value);
      }
      else if (localName.equals("connection-factory"))
      {
         producer.setConnectionFactory(value);
      }
   }

}
