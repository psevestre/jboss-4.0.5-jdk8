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
package org.jboss.jms.container;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.jms.JMSException;

import org.jboss.aop.Advised;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.metadata.MetaDataResolver;
import org.jboss.aop.metadata.SimpleMetaData;

/**
 * A JMS container
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class Container implements InvocationHandler 
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   protected Interceptor[] interceptors;

   protected SimpleMetaData metadata = new SimpleMetaData();

   /**
    * The frontend proxy
    */
   private Object proxy;

   /**
    * The parent
    */
   private Container parent;
   
   /**
    * The children
    */
   private Set children = Collections.synchronizedSet(new HashSet());

   // Static --------------------------------------------------------

   /**
    * Get the container from a proxy
    * 
    * @param object the proxy
    * @returns the container
    * @throws Throwable for any error
    */
   public static Container getContainer(Object object)
      throws Throwable
   {
      Proxy proxy = (Proxy) object;
      return (Container) Proxy.getInvocationHandler(proxy);
   }

   /**
    * Get the container from an invocation
    * 
    * @param invocation the conatiner
    * @returns the container
    */
   public static Container getContainer(Invocation invocation)
   {
      return (Container) invocation.getMetaData("JMS", "Container");
   }

   /**
    * Get the proxy from an invocation
    * 
    * @param invocation the conatiner
    * @returns the proxy
    */
   public static Object getProxy(Invocation invocation)
   {
      return getContainer(invocation).getProxy();
   }

   // Constructors --------------------------------------------------

   /**
    * Create a new container
    * 
    * @param interceptors the interceptors
    * @param metadata the meta data
    * @throws JMSException for any error
    */
   public Container(Container parent, Interceptor[] interceptors, SimpleMetaData metadata)
      throws JMSException
   {
      this.interceptors = interceptors;
      this.parent = parent;
      if (metadata != null)
         this.metadata = metadata;
      this.metadata.addMetaData("JMS", "Container", this);

      if (parent != null)
         parent.children.add(this);
   }

   // Public --------------------------------------------------------

   public Object getProxy()
   {
      return proxy;
   }

   public void setProxy(Object proxy)
   {
      this.proxy = proxy;
   }
   
   public Container getParent()
   {
      return parent;
   }
   
   public Set getChildren()
   {
      return children;
   }
   
   public void addChild(Container child)
   {
      children.add(child);
   }
   
   public void removeChild(Container child)
   {
      children.remove(child);
   }

   public Object invoke (Invocation invocation)
      throws Throwable
   {
      //MetaDataResolver oldMetaData = invocation.instanceResolver;
      //invocation.instanceResolver = getMetaData();
      try
      {
         return invocation.invokeNext(interceptors);
      }
      finally
      {
         //invocation.instanceResolver = oldMetaData;
      }
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      MethodInfo info = new MethodInfo();
      MethodInvocation invocation = new MethodInvocation(info, interceptors);
      //invocation.instanceResolver = getMetaData();
      //invocation.method = m; 
      //invocation.arguments = args;
      return invocation.invokeNext();
   }

   // Protected ------------------------------------------------------

   // Package Private ------------------------------------------------

   // Private --------------------------------------------------------

   // Inner Classes --------------------------------------------------
}
