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

import javax.persistence.PersistenceContextType;
import org.jboss.logging.Logger;
import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.xml.sax.Attributes;

/**
 * org.jboss.xb.binding.ObjectModelFactory implementation that accepts data
 * chuncks from unmarshaller and assembles them into an EjbJarDD instance.
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class EjbJarDDObjectFactory implements ObjectModelFactory
{

   private static final Logger log = Logger
           .getLogger(EjbJarDDObjectFactory.class);

   /**
    * Return the root.
    */
   public Object newRoot(Object root, UnmarshallingContext navigator,
                         String namespaceURI, String localName, Attributes attrs)
   {

      final EjbJarDD dd;
      if (root == null)
      {
         root = dd = new EjbJarDD();
      }
      else
      {
         dd = (EjbJarDD) root;
      }

      if (attrs.getLength() > 0)
      {
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            if (attrs.getLocalName(i).equals("version"))
            {
               dd.setVersion(attrs.getValue(i));
            }
         }
      }

      return root;
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
         child = new EnterpriseBeans();
      }
      if (localName.equals("interceptors"))
      {
         child = new Interceptors();
      }
      else if (localName.equals("relationships"))
      {
         child = new Relationships();
      }
      else if (localName.equals("assembly-descriptor"))
      {
         child = new AssemblyDescriptor();
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
         child = new SessionEnterpriseBean();
      }
      else if (localName.equals("entity"))
      {
         child = new EntityEnterpriseBean();
      }
      else if (localName.equals("message-driven"))
      {
         child = new MessageDrivenBean();
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(PersistenceContextRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(PersistenceUnitRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbLocalRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(MessageDestinationRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EnvEntry ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
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
   public Object newChild(WebServiceRef ref, UnmarshallingContext navigator,
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
   public Object newChild(ResourceEnvRef ejbs, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("injection-target"))
      {
         InjectionTarget target = new InjectionTarget();
         ejbs.setInjectionTarget(target);
         child = target;
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   private Object newEjbChild(EnterpriseBean parent, String localName)
   {
      Object child = null;

      if (localName.equals("ejb-local-ref"))
      {
         child = new EjbLocalRef();
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
      else if (localName.equals("persistence-unit-ref"))
      {
         child = new PersistenceUnitRef();
      }
      else if (localName.equals("persistence-context-ref"))
      {
         child = new PersistenceContextRef();
      }
      else if (localName.equals("service-ref"))
      {
         child = new WebServiceRef();
      }
      return child;
   }
   

   private Object newEjbHasInterceptorsChild(EnterpriseBean parent, String localName)
   {
      Object child = null;
      
      if (localName.equals("around-invoke"))
      {
         child = new Method();
      }
      else if (localName.equals("post-construct"))
      {
         child = new Method();
      }
      else if (localName.equals("pre-destroy"))
      {
         child = new Method();
      }
      else if (localName.equals("post-activate"))
      {
         child = new Method();
      }
      else if (localName.equals("pre-passivate"))
      {
         child = new Method();
      }
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(MessageDrivenBean parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = newEjbChild(parent, localName);
      if (child != null) return child;
      
      child = newEjbHasInterceptorsChild(parent, localName);
      if (child != null) return child;
      
      if (localName.equals("message-driven-destination"))
      {
         child = new MessageDrivenDestination();
      }
      else if (localName.equals("activation-config"))
      {
         child = new ActivationConfig();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(ActivationConfig parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (child == null)
      {
        if (localName.equals("activation-config-property"))
         {
            child = new ActivationConfigProperty();
         }
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(SessionEnterpriseBean parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = newEjbChild(parent, localName);
      if (child != null) return child;
      
      child = newEjbHasInterceptorsChild(parent, localName);
      if (child != null) return child;

      if (localName.equals("security-identity"))
      {
         child = new SecurityIdentity();
      }
      else if (localName.equals("remove-method"))
      {
         RemoveMethod method = new RemoveMethod();
         parent.addRemoveMethod(method);
         child = method;
      }
      else if (localName.equals("init-method"))
      {
         InitMethod method = new InitMethod();
         parent.addInitMethod(method);
         child = method;
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EntityEnterpriseBean parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      child = newEjbChild(parent, localName);
      if (child == null)
      {
         if (localName.equals("cmp-field"))
         {
            child = new CmpField();
         }
         else if (localName.equals("query"))
         {
            child = new Query();
         }
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(SecurityIdentity parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("use-caller-identity"))
      {
         parent.setUseCallerIdentity(true);
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(RemoveMethod parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("bean-method"))
      {
         parent.setBeanMethod(new Method());
         child = parent.getBeanMethod();
      }

      return child;
   }
   
   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(InitMethod parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("bean-method"))
      {
         parent.setBeanMethod(new Method());
         child = parent.getBeanMethod();
      }

      return child;
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(RemoveMethod dd, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("retain-if-exception"))
      {
         dd.setRetainIfException(Boolean.parseBoolean(value));
      }
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(Relationships relationships,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("ejb-relation"))
      {
         child = new EjbRelation();
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbRelation relation, UnmarshallingContext navigator,
                          String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;

      if (localName.equals("ejb-relationship-role"))
      {
         child = new EjbRelationshipRole();
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(EjbRelationshipRole parent,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("cascade-delete"))
      {
         parent.setCascadeDelete(true);
      }
      else if (localName.equals("relationship-role-source"))
      {
         child = new RelationshipRoleSource();
      }
      else if (localName.equals("cmr-field"))
      {
         child = new CmrField();
      }

      return child;
   }

   public Object newChild(Interceptors interceptors,                          
         UnmarshallingContext navigator, String namespaceURI, String localName,
         Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("interceptor"))
      {
         return new Interceptor();
      }
      
      return child;
   }
   
   public Object newChild(Interceptor interceptor,                          
         UnmarshallingContext navigator, String namespaceURI, String localName,
         Attributes attrs)
   {
      Object child = null;
      
      if (localName.equals("around-invoke"))
      {
         return new Method();
      }
      else if (localName.equals("post-construct"))
      {
         return new Method();
      }
      else if (localName.equals("pre-destroy"))
      {
         return new Method();
      }
      else if (localName.equals("post-activate"))
      {
         return new Method();
      }
      else if (localName.equals("pre-passivate"))
      {
         return new Method();
      }
      else if (localName.equals("ejb-local-ref"))
      {
         child = new EjbLocalRef();
      }
      else if (localName.equals("ejb-ref"))
      {
         child = new EjbRef();
      }
      else if (localName.equals("persistence-unit-ref"))
      {
         child = new PersistenceUnitRef();
      }
      else if (localName.equals("persistence-context-ref"))
      {
         child = new PersistenceContextRef();
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
      else if (localName.equals("service-ref"))
      {
         child = new WebServiceRef();
      }
      
      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(AssemblyDescriptor relationships,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("security-role"))
      {
         child = new SecurityRole();
      }
      else if (localName.equals("method-permission"))
      {
         child = new MethodPermission();
      }
      if (localName.equals("container-transaction"))
      {
         child = new ContainerTransaction();
      }
      else if (localName.equals("inject"))
      {
         child = new Inject();
      }
      else if (localName.equals("exclude-list"))
      {
         child = new ExcludeList();
      }
      else if (localName.equals("application-exception"))
      {
         child = new ApplicationExceptionList();
      }
      else if (localName.equals("interceptor-binding"))
      {
         child = new InterceptorBinding();
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(Inject inject,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
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
   public Object newChild(MethodPermission permission,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("method"))
      {
         child = new Method();
      }
      else if (localName.equals("unchecked"))
      {
         permission.setUnchecked(true);
      }

      return child;
   }

   /**
    * Called when parsing of a new element started.
    */
   public Object newChild(ExcludeList list,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
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
   public Object newChild(InitList list,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
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
   public Object newChild(ContainerTransaction transaction,
                          UnmarshallingContext navigator, String namespaceURI, String localName,
                          Attributes attrs)
   {
      Object child = null;

      if (localName.equals("method"))
      {
         child = new Method();
      }

      return child;
   }

   public Object newChild(Method method,
         UnmarshallingContext navigator, String namespaceURI, String localName,
         Attributes attrs)
   {
      Object child = null;

      if (localName.equals("method-params"))
      {
         method.setHasParameters();
      }
      
      return child;
   }

   
   public Object newChild(InterceptorBinding binding,
         UnmarshallingContext navigator, String namespaceURI, String localName,
         Attributes attrs)
   {
      Object child = null;

      if (localName.equals("interceptor-order"))
      {
         child = new InterceptorOrder();
      }
      else if (localName.equals("exclude-default-interceptors"))
      {
         child = new ExcludeDefaultInterceptors();
      }
      else if (localName.equals("exclude-class-interceptors"))
      {
         child = new ExcludeClassInterceptors();
      }
      else if (localName.equals("method-params"))
      {
         binding.setHasParameters();
      }
       
      
      return child;
   }

   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, ActivationConfig config,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setActivationConfig(config);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbLocalRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, EnvEntry entry,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnvEntry(entry);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, ResourceEnvRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(envRef);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, ResourceRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(envRef);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, WebServiceRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addWebServiceRef(envRef);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(ActivationConfig parent, ActivationConfigProperty property,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addActivationConfigProperty(property);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbJarDD parent, EnterpriseBeans ejbs,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setEnterpriseBeans(ejbs);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EnterpriseBeans parent, EnterpriseBean ejb,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnterpriseBean(ejb);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, SecurityIdentity si,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setSecurityIdentity(si);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbLocalRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, PersistenceContextRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addPersistenceContextRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, PersistenceUnitRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addPersistenceUnitRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, MessageDestinationRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMessageDestinationRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, EnvEntry entry,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnvEntry(entry);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, ResourceEnvRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(envRef);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, ResourceRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(envRef);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, WebServiceRef envRef,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addWebServiceRef(envRef);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(SessionEnterpriseBean parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      if (localName.equals("around-invoke"))
      {
         parent.setAroundInvoke(method);
      }
      else if (localName.equals("post-construct"))
      {
         parent.setPostConstruct(method);
      }
      else if (localName.equals("pre-destroy"))
      {
         parent.setPreDestroy(method);
      }
      else if (localName.equals("post-activate"))
      {
         parent.setPostActivate(method);
      }
      else if (localName.equals("pre-passivate"))
      {
         parent.setPrePassivate(method);
      }
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EntityEnterpriseBean parent, CmpField field,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addCmpField(field);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EntityEnterpriseBean parent, Query query,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addQuery(query);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbJarDD parent, Relationships relationships,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setRelationships(relationships);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Relationships parent, EjbRelation relation,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRelation(relation);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbRelation parent, EjbRelationshipRole role,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRelationshipRole(role);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbRelationshipRole parent,
                        RelationshipRoleSource source, UnmarshallingContext navigator,
                        String namespaceURI, String localName)
   {
      parent.setRelationshipRoleSource(source);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbRelationshipRole parent, CmrField field,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setCmrField(field);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(EjbJarDD parent, AssemblyDescriptor descriptor,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setAssemblyDescriptor(descriptor);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, SecurityRole role,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addSecurityRole(role);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, MethodPermission permission,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethodPermission(permission);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, ExcludeList list,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setExcludeList(list);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, ApplicationExceptionList list,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setApplicationExceptionList(list);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, InitList list,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setInitList(list);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, Inject inject,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addInject(inject);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent, InterceptorBinding binding,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addInterceptorBinding(binding);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(ExcludeList parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(InitList parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(MethodPermission parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Inject parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMethod(method);
   }

   public void addChild(EjbJarDD parent, Interceptors interceptors,                           
         UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setInterceptors(interceptors);
   }

   public void addChild(Interceptors parent, Interceptor interceptor,                           
         UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addInterceptor(interceptor);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, EjbLocalRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbLocalRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, EjbRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEjbRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, PersistenceContextRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addPersistenceContextRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, PersistenceUnitRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addPersistenceUnitRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, MessageDestinationRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addMessageDestinationRef(ref);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, EnvEntry entry,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addEnvEntry(entry);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, ResourceEnvRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceEnvRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, ResourceRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addResourceRef(ref);
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(Interceptor parent, WebServiceRef ref,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addWebServiceRef(ref);
   }
   
   public void addChild(Interceptor parent, Method method,                           
         UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      if (localName.equals("around-invoke"))
      {
         parent.setAroundInvoke(method);
      }
      else if (localName.equals("post-construct"))
      {
         parent.setPostConstruct(method);
      }
      else if (localName.equals("pre-destroy"))
      {
         parent.setPreDestroy(method);
      }
      else if (localName.equals("post-activate"))
      {
         parent.setPostActivate(method);
      }
      else if (localName.equals("pre-passivate"))
      {
         parent.setPrePassivate(method);
      }
   }
   
   /**
    * Called when parsing character is complete.
    */
   public void addChild(InterceptorBinding parent,
                        InterceptorOrder order, UnmarshallingContext navigator,
                        String namespaceURI, String localName)
   {
      parent.setOrderedInterceptorClasses(order);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(InterceptorBinding parent,
                        ExcludeDefaultInterceptors exclude, UnmarshallingContext navigator,
                        String namespaceURI, String localName)
   {
      parent.setExcludeDefaultInterceptors(true);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(InterceptorBinding parent,
                        ExcludeClassInterceptors exclude, UnmarshallingContext navigator,
                        String namespaceURI, String localName)
   {
      parent.setExcludeClassInterceptors(true);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(ContainerTransaction parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setMethod(method);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, MessageDrivenDestination destination,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.setMessageDrivenDestination(destination);
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(MessageDrivenBean parent, Method method,
                        UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      if (localName.equals("around-invoke"))
      {
         parent.setAroundInvoke(method);
      }
      else if (localName.equals("post-construct"))
      {
         parent.setPostConstruct(method);
      }
      else if (localName.equals("pre-destroy"))
      {
         parent.setPreDestroy(method);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbJarDD dd, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("display-name"))
      {
         dd.setDisplayName(value);
      }
   }

   /**
    * Called when parsing character is complete.
    */
   public void addChild(AssemblyDescriptor parent,
                        ContainerTransaction transaction, UnmarshallingContext navigator,
                        String namespaceURI, String localName)
   {
      parent.addContainerTransaction(transaction);
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ActivationConfigProperty property, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("activation-config-property-name"))
      {
         property.setName(value);
      }
      else if (localName.equals("activation-config-property-value"))
      {
         property.setValue(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   private boolean isEjbParentName(EnterpriseBean ejb, String localName,
                                   String value)
   {
      if (localName.equals("ejb-name"))
      {
         ejb.setEjbName(value);
         return true;
      }
      else if (localName.equals("home"))
      {
         ejb.setHome(value);
         return true;
      }
      else if (localName.equals("remote"))
      {
         ejb.setRemote(value);
         return true;
      }
      else if (localName.equals("local-home"))
      {
         ejb.setLocalHome(value);
         return true;
      }
      else if (localName.equals("local"))
      {
         ejb.setLocal(value);
         return true;
      }
      else if (localName.equals("ejb-class"))
      {
         ejb.setEjbClass(value);
         return true;
      }

      return false;
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MessageDrivenBean ejb,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (!isEjbParentName(ejb, localName, value))
      {
         if (localName.equals("acknowledge-mode"))
         {
            ejb.setAcknowledgeMode(value);
         }
         else if (localName.equals("transaction-type"))
         {
            ejb.setTransactionType(value);
         }
         else if (localName.equals("messaging-type"))
         {
            ejb.setMessagingType(value);
         }
         else if (localName.equals("message-destination-type"))
         {
            MessageDrivenDestination destination = ejb.getMessageDrivenDestination();
            if (destination == null)
            {
               destination = new MessageDrivenDestination();
               ejb.setMessageDrivenDestination(destination);
            }
            
            destination.setDestinationType(value);
         }
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MessageDrivenDestination destination,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("destination-type"))
      {
         destination.setDestinationType(value);
      }
      else if (localName.equals("subscription-durability"))
      {
         destination.setSubscriptionDurability(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(SessionEnterpriseBean ejb,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (!isEjbParentName(ejb, localName, value))
      {
         if (localName.equals("session-type"))
         {
            ejb.setSessionType(value);
         }
         else if (localName.equals("transaction-type"))
         {
            ejb.setTransactionManagementType(value);
         }
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ApplicationExceptionList list,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("exception-class"))
      {
         list.addClass(value);
      }
      else if (localName.equals("rollback"))
      {
         list.addRollback(Boolean.valueOf(value));
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EntityEnterpriseBean ejb,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (!isEjbParentName(ejb, localName, value))
      {
         if (localName.equals("persistence-type"))
         {
            ejb.setPersistenceType(value);
         }
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(SecurityIdentity si, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("use-caller-identity"))
      {
         si.setUseCallerIdentity(true);
      }
      else if (localName.equals("role-name"))
      {
         si.setRunAs(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(PersistenceUnitRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("persistence-unit-ref-name"))
      {
         ref.setRefName(value);
      }
      else if (localName.equals("persistence-unit-name"))
      {
         ref.setUnitName(value);
      }
      else
      {
         throw new RuntimeException("INVALID element<" + localName + "> in <persistence-unit-ref>");
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(PersistenceContextRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("persistence-context-ref-name"))
      {
         ref.setRefName(value);
      }
      else if (localName.equals("persistence-unit-name"))
      {
         ref.setUnitName(value);
      }
      else if(localName.equals("persistence-context-type"))
      {
         if (value.toLowerCase().equals("transaction"))
         {
            ref.setPersistenceContextType(PersistenceContextType.TRANSACTION);
         }
         else
         {
            ref.setPersistenceContextType(PersistenceContextType.EXTENDED);
         }
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
   public void setValue(MessageDestinationRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("description"))
      {
         ref.setDescription(value);
      }
      else if (localName.equals("message-destination-ref-name"))
      {
         ref.setMessageDestinationRefName(value);
      }
      else if (localName.equals("message-destination-type"))
      {
         ref.setMessageDestinationType(value);
      }
      else if (localName.equals("message-destination-usage"))
      {
         ref.setMessageDestinationUsage(value);
      }
      else if (localName.equals("message-destination-link"))
      {
         ref.setMessageDestinationLink(value);
      }
      else if (localName.equals("mapped-name"))
      {
         ref.setMappedName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EnvEntry entry, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("description"))
      {
         entry.setDescription(value);
      }
      else if (localName.equals("env-entry-name"))
      {
         entry.setEnvEntryName(value);
      }
      else if (localName.equals("env-entry-type"))
      {
         entry.setEnvEntryType(value);
      }
      else if (localName.equals("env-entry-value"))
      {
         entry.setEnvEntryValue(value);
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
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ResourceRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("res-ref-name")
              || localName.equals("resource-env-ref-name"))
      {
         ref.setResRefName(value);
      }
      else if (localName.equals("res-type")
              || localName.equals("resource-env-ref-type"))
      {
         ref.setResType(value);
      }
      else if (localName.equals("res-auth"))
      {
         ref.setResAuth(value);
      }
      else if (localName.equals("res-sharing-scope"))
      {
         ref.setResSharingScope(value);
      }
      else if (localName.equals("mapped-name"))
      {
         ref.setMappedName(value);
      }
   }
   
   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(WebServiceRef ref, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("service-ref-name"))
      {
         ref.setServiceRefName(value);
      }
      else if (localName.equals("service-res-type"))
      {
         ref.setResType(value);
      }
      else if (localName.equals("service-interface"))
      {
         ref.setServiceInterface(value);
      }
      else if (localName.equals("wsdl-file"))
      {
         ref.setWsdlFile(value);
      }
      else if (localName.equals("jaxrpc-mapping-file"))
      {
         ref.setJaxRpcMappingFile(value);
      }
      else if (localName.equals("mapped-name"))
      {
         ref.setMappedName(value);
      }
   }

   public void setValue(Interceptor interceptor, UnmarshallingContext navigator,
         String namespaceURI, String localName, String value)
   {
      if (localName.equals("interceptor-class"))
      {
         interceptor.setInterceptorClass(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbRelation relation, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-relation-name"))
      {
         relation.setEjbRelationName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(EjbRelationshipRole role,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("ejb-relationship-role-name"))
      {
         role.setEjbRelationshipRoleName(value);
      }
      else if (localName.equals("multiplicity"))
      {
         role.setMultiplicity(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(RelationshipRoleSource source,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("ejb-name"))
      {
         source.setEjbName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(CmrField field, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("cmr-field-name"))
      {
         field.setCmrFieldName(value);
      }
      else if (localName.equals("cmr-field-type"))
      {
         field.setCmrFieldType(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(SecurityRole role, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("role-name"))
      {
         role.setRoleName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(MethodPermission permission,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("role-name"))
      {
         permission.addRoleName(value);
      }
      else if (localName.equals("unchecked"))
      {
         permission.setUnchecked(true);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(ContainerTransaction transaction,
                        UnmarshallingContext navigator, String namespaceURI, String localName,
                        String value)
   {
      if (localName.equals("trans-attribute"))
      {
         transaction.setTransAttribute(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Method method, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-name"))
      {
         method.setEjbName(value);
      }
      else if (localName.equals("method-name"))
      {
         method.setMethodName(value);
      }
      else if (localName.equals("method-param"))
      {
         method.addMethodParam(value);
      }
      else if (localName.equals("lifecycle-callback-method"))
      {
         method.setMethodName(value);
      }
   }

   /**
    * Called when a child element with simple content is read for DD.
    */
   public void setValue(Inject inject, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("jndi-name"))
      {
         inject.setJndiName(value);
      }
   }

   public void setValue(InterceptorBinding binding, UnmarshallingContext navigator,
                        String namespaceURI, String localName, String value)
   {
      if (localName.equals("ejb-name"))
      {
         binding.setEjbName(value);
      }
      else if (localName.equals("interceptor-class"))
      {
         binding.addInterceptorClass(value);
      }
      else if (localName.equals("method-name"))
      {
         binding.setMethodName(value);
      }
      else if (localName.equals("method-param"))
      {
         binding.addMethodParam(value);
      }
      else if (localName.equals("exclude-default-interceptors"))
      {
         binding.setExcludeDefaultInterceptors(true);
      }
      else if (localName.equals("exclude-class-interceptors"))
      {
         binding.setExcludeClassInterceptors(true);
      }
   }

   public void setValue(InterceptorOrder order, UnmarshallingContext navigator,
         String namespaceURI, String localName, String value)
   {
      if (localName.equals("interceptor-class"))
      {
         order.addInterceptorClass(value);
      }
   }

}
