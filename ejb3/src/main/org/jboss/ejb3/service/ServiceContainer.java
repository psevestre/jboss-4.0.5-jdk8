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
package org.jboss.ejb3.service;

import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.Handle;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.jboss.aop.AspectManager;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.InvocationResponse;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.util.MethodHashing;
import org.jboss.aop.util.PayloadKey;
import org.jboss.aspects.asynch.FutureHolder;
import org.jboss.ejb.AllowedOperationsAssociation;
import org.jboss.ejb.AllowedOperationsFlags;
import org.jboss.ejb3.BeanContext;
import org.jboss.ejb3.EJBContainerInvocation;
import org.jboss.ejb3.Ejb3Deployment;
import org.jboss.ejb3.ProxyFactory;
import org.jboss.ejb3.SessionContainer;
import org.jboss.ejb3.ThreadLocalENCFactory;
import org.jboss.ejb3.asynchronous.AsynchronousInterceptor;
import org.jboss.ejb3.interceptor.InterceptorInfoRepository;
import org.jboss.ejb3.timerservice.TimedObjectInvoker;
import org.jboss.ejb3.timerservice.TimerServiceFactory;
import org.jboss.logging.Logger;
import org.jboss.injection.Injector;

/**
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 57207 $
 */
public class ServiceContainer extends SessionContainer implements TimedObjectInvoker
{
   ServiceMBeanDelegate delegate;
   Object singleton;
   boolean injected;
   BeanContext beanContext;
   MBeanServer mbeanServer;
   ObjectName delegateObjectName;
   private TimerService timerService;

   private static final Logger log = Logger.getLogger(ServiceContainer.class);

   public ServiceContainer(MBeanServer server, ClassLoader cl, String beanClassName, String ejbName,
                           AspectManager manager, Hashtable ctxProperties, InterceptorInfoRepository interceptorRepository,
                           Ejb3Deployment deployment)
   {
      super(cl, beanClassName, ejbName, manager, ctxProperties, interceptorRepository, deployment);
      beanContextClass = ServiceBeanContext.class;
      this.mbeanServer = server;
   }

   public void callTimeout(Timer timer) throws Exception
   {
      Method timeout = callbackHandler.getTimeoutCallback();
      if (timeout == null) throw new EJBException("No method has been annotated with @Timeout");
      Object[] args = {timer};
      AllowedOperationsAssociation.pushInMethodFlag(AllowedOperationsFlags.IN_EJB_TIMEOUT);
      try
      {
         localInvoke(timeout, args);
      }
      catch(Throwable throwable)
      {
         if (throwable instanceof Exception) throw (Exception) throwable;
         if(throwable instanceof Error) throw (Error) throwable;
         throw new RuntimeException(throwable);
      }
      finally
      {
         AllowedOperationsAssociation.popInMethodFlag();
      }
   }

   protected Object createSession(Class initTypes[], Object initArgs[])
   {
//      if((initTypes != null && initTypes.length > 0) || (initArgs != null && initArgs.length > 0))
//         throw new IllegalArgumentException("service bean create method must take no arguments");
      throw new RuntimeException("NYI");
   }

   public Object getSingleton()
   {
      return singleton;
   }

   public void create() throws Exception
   {
      super.create();
   }


   public void start() throws Exception
   {
      super.start();

      try
      {
         singleton = super.construct();

         initBeanContext();

         // make sure the timer service is there before injection takes place
         timerService = TimerServiceFactory.getInstance().createTimerService(this.getObjectName(), this);

         injectDependencies(beanContext);

         registerManagementInterface();
         
         TimerServiceFactory.getInstance().restoreTimerService(timerService);
      }
      catch (Exception e)
      {
         e.printStackTrace();
         stop();
      }
   }

   public void stop() throws Exception
   {
      if (timerService != null) TimerServiceFactory.getInstance().removeTimerService(timerService);

      super.stop();

      if (delegate != null)
      {
         getDeployment().getKernelAbstraction().uninstallMBean(delegateObjectName);
      }
      injected = false;
   }

   public void destroy() throws Exception
   {
      super.destroy();
   }

   public void initializePool() throws Exception
   {
      resolveInjectors();
   }

   public TimerService getTimerService()
   {
      return timerService;
   }

   public TimerService getTimerService(Object pKey)
   {
      assert timerService != null : "Timer Service not yet initialized";
      return timerService;
   }
   
   public void invokePostConstruct(BeanContext beanContext)
   {
      //Ignore
   }

   public void invokePreDestroy(BeanContext beanContext)
   {
      //Ignore
   }

   public void invokeInit(Object bean)
   {
      //Ignore
   }

   /**
    * Performs a synchronous local invocation
    */
   public Object localInvoke(Method method, Object[] args) throws Throwable
   {
      return localInvoke(method, args, null);
   }

   /**
    * Performs a synchronous or asynchronous local invocation
    *
    * @param provider If null a synchronous invocation, otherwise an asynchronous
    */
   public Object localInvoke(Method method, Object[] args, FutureHolder provider) throws Throwable
   {
      ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
      try
      {
         Thread.currentThread().setContextClassLoader(classloader);
         long hash = MethodHashing.calculateHash(method);
         MethodInfo info = (MethodInfo) methodInterceptors.get(hash);
         if (info == null)
         {
            throw new RuntimeException("Could not resolve beanClass method from proxy call: " + method.toString());
         }
         Interceptor[] aspects = info.getInterceptors();
         EJBContainerInvocation nextInvocation = new EJBContainerInvocation(info, aspects);
         nextInvocation.setAdvisor(this);
         nextInvocation.setArguments(args);

         nextInvocation = populateInvocation(nextInvocation);

         if (provider != null)
         {
            nextInvocation.getMetaData().addMetaData(AsynchronousInterceptor.ASYNCH, AsynchronousInterceptor.INVOKE_ASYNCH, "YES", PayloadKey.AS_IS);
            nextInvocation.getMetaData().addMetaData(AsynchronousInterceptor.ASYNCH, AsynchronousInterceptor.FUTURE_HOLDER, provider, PayloadKey.AS_IS);
         }
         return nextInvocation.invokeNext();
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldLoader);
      }
   }

   public InvocationResponse dynamicInvoke(Object target, Invocation invocation) throws Throwable
   {
      ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
      EJBContainerInvocation newSi = null;
      try
      {
         Thread.currentThread().setContextClassLoader(classloader);
         MethodInvocation si = (MethodInvocation) invocation;
         MethodInfo info = (MethodInfo) methodInterceptors.get(si.getMethodHash());
         if (info == null)
         {
            throw new RuntimeException("Could not resolve beanClass method from proxy call");
         }
         Interceptor[] aspects = info.getInterceptors();
         newSi = new EJBContainerInvocation(info, aspects);
         newSi.setArguments(si.getArguments());
         newSi.setMetaData(si.getMetaData());
         newSi.setAdvisor(this);

         newSi = populateInvocation(newSi);

         Object rtn = null;
         try
         {
            rtn = newSi.invokeNext();
         }
         catch (Throwable throwable)
         {
            return marshallException(invocation, throwable, newSi.getResponseContextInfo());
         }
         InvocationResponse response = SessionContainer.marshallResponse(invocation, rtn, newSi.getResponseContextInfo());

         return response;
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(oldLoader);
      }
   }

   protected void initBeanContext() throws RuntimeException
   {
      if (beanContext == null)
      {
         synchronized(singleton)
         {
            if (beanContext == null)
            {
               try
               {
                  beanContext  = (BeanContext) beanContextClass.newInstance();
                  beanContext.setContainer(this);
                  beanContext.initialiseInterceptorInstances();
                  beanContext.setInstance(singleton);
               }
               catch (InstantiationException e)
               {
                  throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
               }
               catch (IllegalAccessException e)
               {
                  throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
               }
            }
         }
      }
   }

   @Override
   protected EJBContainerInvocation populateInvocation(EJBContainerInvocation invocation)
   {
      invocation.setTargetObject(singleton);
      invocation.setBeanContext(beanContext);
      return invocation;
   }

   protected synchronized void injectDependencies(BeanContext ctx)
   {
      if (injectors != null)
      {
         try
         {
            ThreadLocalENCFactory.push(enc);
            for (Injector injector : injectors)
            {
               injector.inject(ctx);
            }
         }
         finally
         {
            ThreadLocalENCFactory.pop();
         }
      }
      injected = true;
   }

   // Dynamic MBean implementation --------------------------------------------------

   public Object getAttribute(String attribute) throws AttributeNotFoundException,
                                                       MBeanException, ReflectionException
   {
      return delegate.getAttribute(attribute);
   }

   public void setAttribute(Attribute attribute) throws AttributeNotFoundException,
                                                        InvalidAttributeValueException, MBeanException, ReflectionException
   {
      delegate.setAttribute(attribute);
   }

   public AttributeList getAttributes(String[] attributes)
   {
      return delegate.getAttributes(attributes);
   }

   public AttributeList setAttributes(AttributeList attributes)
   {
      return delegate.setAttributes(attributes);
   }

   public Object invoke(String actionName, Object params[], String signature[])
           throws MBeanException, ReflectionException
   {
      return delegate.invoke(actionName, params, signature);
   }

   @Override
   protected Object invokeEJBObjectMethod(ProxyFactory factory, Object id, MethodInfo info, Object[] args) throws Exception
   {
      throw new RuntimeException("NYI");
   }

   public MBeanInfo getMBeanInfo()
   {
      return delegate.getMBeanInfo();
   }


   private void registerManagementInterface()
   {
      try
      {
         Management annotation = (Management)resolveAnnotation(Management.class);

         Class intf = null;
         if (annotation != null)
            intf = annotation.value();

         if (intf ==null)
         {
            Class[] interfaces = this.getBeanClass().getInterfaces();
            int interfaceIndex = 0;
            while (intf == null && interfaceIndex < interfaces.length)
            {
               if (interfaces[interfaceIndex].getAnnotation(Management.class) != null)
                  intf = interfaces[interfaceIndex];
               else
                  ++interfaceIndex;
            }
         }

         if (intf != null)
         {
            if (mbeanServer == null)
               mbeanServer = org.jboss.mx.util.MBeanServerLocator.locateJBoss();

            if (mbeanServer == null)
               throw new RuntimeException("There is a @Management interface on " + ejbName + " but the MBeanServer has not been initialized for it");

            Service service = (Service)resolveAnnotation(Service.class);

            String objname = service.objectName();
            delegateObjectName = (objname == null || objname.equals("")) ?
                            new ObjectName(getObjectName().getCanonicalName() + ",type=ManagementInterface") : new ObjectName(service.objectName());

            delegate = new ServiceMBeanDelegate(mbeanServer, this, intf, delegateObjectName);

            Object securityDomainAnnotation = resolveAnnotation(org.jboss.annotation.security.SecurityDomain.class);

            getDeployment().getKernelAbstraction().installMBean(delegateObjectName, getDependencyPolicy(), delegate);

         }
      }
      catch (Exception e)
      {
         throw new RuntimeException("Problem registering @Management interface for @Service " + getBeanClass(), e);
      }
   }

   protected void removeHandle(Handle handle)
   {
      throw new RuntimeException("Don't do this");
   }
}
