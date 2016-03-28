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
import org.jboss.logging.Logger;

/**
 * Represents <assembly-descriptor> elements of the ejb-jar.xml deployment descriptor for the 1.4
 * schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class AssemblyDescriptor
{
   private static final Logger log = Logger.getLogger(AssemblyDescriptor.class);

   private List securityRoles = new ArrayList();

   private List methodPermissions = new ArrayList();

   private List containerTransactions = new ArrayList();
   
   private HashMap<String, MessageDestination> messageDestinations = new HashMap();

   private ExcludeList excludeList;

   private ApplicationExceptionList applicationExceptionList;

   private InitList initList;

   private List injects = new ArrayList();
   
   public Collection getMessageDestinations()
   {
      return messageDestinations.values();
   }

   public void addMessageDestination(MessageDestination messageDestination)
   {
      messageDestinations.put(messageDestination.getMessageDestinationName(), messageDestination);
   }
   
   public MessageDestination findMessageDestination(String name)
   {
      return messageDestinations.get(name);
   }

   private List interceptorBindings = new ArrayList();

   public InitList getInitList()
   {
      return initList;
   }

   public void setInitList(InitList initList)
   {
      this.initList = initList;
   }

   public ExcludeList getExcludeList()
   {
      return excludeList;
   }

   public void setExcludeList(ExcludeList excludeList)
   {
      this.excludeList = excludeList;
   }
   
   public ApplicationExceptionList getApplicationExceptionList()
   {
      return applicationExceptionList;
   }

   public void setApplicationExceptionList(ApplicationExceptionList applicationExceptionList)
   {
      this.applicationExceptionList = applicationExceptionList;
   }

   public List getSecurityRoles()
   {
      return securityRoles;
   }

   public void setSecurityRoles(List securityRoles)
   {
      this.securityRoles = securityRoles;
   }

   public void addSecurityRole(SecurityRole securityRole)
   {
      securityRoles.add(securityRole);
   }

//   public List getCallbacks()
//   {
//      return callbacks;
//   }
//
//   public void addCallback(Callback callback)
//   {
//      callbacks.add(callback);
//   }

   public List<InterceptorBinding> getInterceptorBindings()
   {
      return interceptorBindings;
   }

   public void addInterceptorBinding(InterceptorBinding binding)
   {
      interceptorBindings.add(binding);
   }

   public List getInjects()
   {
      return injects;
   }

   public void addInject(Inject inject)
   {
      injects.add(inject);
   }

   public List getMethodPermissions()
   {
      return methodPermissions;
   }

   public void setMethodPermissions(List methodPermissions)
   {
      this.methodPermissions = methodPermissions;
   }

   public void addMethodPermission(MethodPermission methodPermission)
   {
      methodPermissions.add(methodPermission);
   }

   public List getContainerTransactions()
   {
      return containerTransactions;
   }

   public void setContainerTransactions(List containerTransactions)
   {
      this.containerTransactions = containerTransactions;
   }

   public void addContainerTransaction(ContainerTransaction containerTransaction)
   {
      containerTransactions.add(containerTransaction);
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("securityRoles=").append(securityRoles);
      sb.append(", applicationExceptionList=").append(applicationExceptionList);
      sb.append("]");
      return sb.toString();
   }
}
