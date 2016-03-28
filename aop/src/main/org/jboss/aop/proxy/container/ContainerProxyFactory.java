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
package org.jboss.aop.proxy.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassContainer;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.util.JavassistMethodHashing;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.SerialVersionUID;


/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57185 $
 */
public class ContainerProxyFactory
{
   private static Object maplock = new Object();
   private static WeakHashMap proxyCache = new WeakHashMap();

   public static ArrayList getIntroductions(Class clazz, AspectManager manager)
   {
      ArrayList list = new ArrayList();
      ClassContainer container = new ClassContainer("temp", manager);
      Iterator it = manager.getInterfaceIntroductions().values().iterator();
      while (it.hasNext())
      {
         InterfaceIntroduction intro = (InterfaceIntroduction) it.next();
         if (intro.matches(container, clazz))
         {
            list.add(intro);
         }
      }
      return list;
   }


   public static Class getProxyClass(Class clazz, AspectManager manager)
           throws Exception
   {
      // Don't make a proxy of a proxy !
      if (Delegate.class.isAssignableFrom(clazz)) clazz = clazz.getSuperclass();

      Class proxyClass = null;
      synchronized (maplock)
      {
         proxyClass = (Class) proxyCache.get(clazz);
         if (proxyClass == null)
         {

            proxyClass = generateProxy(clazz, getIntroductions(clazz, manager));
            proxyCache.put(clazz, proxyClass);
         }
      }
      return proxyClass;
   }

   private static int counter = 0;


   private static void getIntroductionInterfaces(HashMap intfs, HashMap mixins, InterfaceIntroduction intro, ArrayList mixes, int idx)
   {
      if (intro.getInterfaces() != null)
      {
         for (int i = 0; i < intro.getInterfaces().length; i++)
         {
            if (intfs.containsKey(intro.getInterfaces()[i]) || mixins.containsKey(intro.getInterfaces()[i])) throw new RuntimeException("cannot have an IntroductionInterface that introduces same interfaces");
            intfs.put(intro.getInterfaces()[i], new Integer(idx));
         }
      }
      Iterator it = intro.getMixins().iterator();
      while (it.hasNext())
      {
         InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin) it.next();
         mixes.add(mixin);
         for (int i = 0; i < mixin.getInterfaces().length; i++)
         {
            if (intfs.containsKey(mixin.getInterfaces()[i]) || mixins.containsKey(mixin.getInterfaces()[i])) throw new RuntimeException("cannot have an IntroductionInterface that introduces same interfaces");
            mixins.put(mixin.getInterfaces()[i], new Integer(idx));
         }
      }
   }

   public static CtClass createProxyCtClass(ArrayList mixins, Class clazz)
           throws Exception
   {
      ClassPool pool = AspectManager.instance().findClassPool(clazz.getClassLoader());
      if (pool == null) throw new NullPointerException("Could not find ClassPool");

      String classname = "AOPContainerProxy$" + counter++;

      CtClass template = pool.get("org.jboss.aop.proxy.container.ProxyTemplate");
      CtClass superclass = pool.get(clazz.getName());

      CtClass proxy = pool.makeClass(classname, superclass);
      CtMethod getDelegate = template.getDeclaredMethod("getDelegate");
      CtMethod setDelegate = template.getDeclaredMethod("setDelegate");
      CtMethod getAdvisor = template.getDeclaredMethod("getAdvisor");
      CtMethod setAdvisor = template.getDeclaredMethod("setAdvisor");


      proxy.addMethod(CtNewMethod.copy(getAdvisor, proxy, null));
      proxy.addMethod(CtNewMethod.copy(setAdvisor, proxy, null));

      CtField mixinField = template.getField("mixins");
      CtField advisorField = template.getField("advisor");
      CtField delegateField = template.getField("delegate");


      mixinField = new CtField(mixinField.getType(), "mixins", proxy);
      mixinField.setModifiers(Modifier.PRIVATE);
      proxy.addField(mixinField);

      advisorField = new CtField(advisorField.getType(), "advisor", proxy);
      advisorField.setModifiers(Modifier.PRIVATE);
      proxy.addField(advisorField);

      delegateField = new CtField(superclass, "delegate", proxy);
      delegateField.setModifiers(Modifier.PRIVATE);
      proxy.addField(delegateField);

      CtMethod rgetDelegate = CtNewMethod.make(getDelegate.getReturnType(), "getDelegate", getDelegate.getParameterTypes(), getDelegate.getExceptionTypes(), "{ return delegate; }", proxy);
      proxy.addMethod(rgetDelegate);
      CtMethod rsetDelegate = CtNewMethod.make(setDelegate.getReturnType(), "setDelegate", setDelegate.getParameterTypes(), setDelegate.getExceptionTypes(), "{ this.delegate = (" + clazz.getName() + ")$1; }", proxy);
      proxy.addMethod(rsetDelegate);

      proxy.addInterface(pool.get("org.jboss.aop.proxy.container.Delegate"));
      proxy.addInterface(pool.get("org.jboss.aop.proxy.container.AspectManaged"));
      proxy.addInterface(pool.get("org.jboss.aop.instrument.Untransformable"));


      HashSet addedInterfaces = new HashSet();
      HashSet addedMethods = new HashSet();
      if (mixins != null)
      {
         HashMap intfs = new HashMap();
         HashMap mixinIntfs = new HashMap();
         ArrayList mixes = new ArrayList();
         for (int i = 0; i < mixins.size(); i++)
         {
            InterfaceIntroduction introduction = (InterfaceIntroduction) mixins.get(i);
            getIntroductionInterfaces(intfs, mixinIntfs, introduction, mixes, i);
         }
         if (mixes.size() > 0)
         {
            CtConstructor con = CtNewConstructor.defaultConstructor(proxy);
            con.insertAfter("mixins = new Object[" + mixes.size() + "];");
            for (int i = 0; i < mixes.size(); i++)
            {
               InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin) mixes.get(i);
               String initializer = (mixin.getConstruction() == null) ? ("new " + mixin.getClassName() + "()") : mixin.getConstruction();
               con.insertAfter("mixins[" + i + "] = " + initializer + ";");
            }
            proxy.addConstructor(con);
         }
         Iterator it = mixinIntfs.keySet().iterator();
         while (it.hasNext())
         {
            String intf = (String) it.next();
            Integer idx = (Integer) mixinIntfs.get(intf);
            if (addedInterfaces.contains(intf)) throw new Exception("2 mixins are implementing the same interfaces");

            CtClass intfClass = pool.get(intf);
            CtMethod[] methods = intfClass.getMethods();
            HashSet mixinMethods = new HashSet();
            for (int m = 0; m < methods.length; m++)
            {
               if (methods[m].getDeclaringClass().getName().equals("java.lang.Object")) continue;
               Long hash = new Long(JavassistMethodHashing.methodHash(methods[m]));
               if (mixinMethods.contains(hash)) continue;
               if (addedMethods.contains(hash)) throw new Exception("More than one mixin has same method");
               mixinMethods.add(hash);
               addedMethods.add(hash);
               String aopReturnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
               String returnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ";
               String args = "null";
               if (methods[m].getParameterTypes().length > 0) args = "$args";
               String code = "{   " +
                             "   " + intf + " mixin = (" + intf + ")mixins[" + idx + "];" +
                             "    org.jboss.aop.MethodInfo mi = advisor.getMethodInfo(" + hash.longValue() + "L); " +
                             "    if (mi != null && mi.interceptors != (Object[])null && mi.interceptors.length > 0) { " +
                             "       org.jboss.aop.joinpoint.MethodInvocation invocation = new org.jboss.aop.joinpoint.MethodInvocation(mi, mi.interceptors); " +
                             "       invocation.setArguments(" + args + "); " +
                             "       invocation.setTargetObject(mixin); " +
                             "       " + aopReturnStr + " invocation.invokeNext(); " +
                             "    } else { " +
                             "   " + returnStr + " mixin." + methods[m].getName() + "($$);" +
                             "    } " +
                             "}";
               CtMethod newMethod = CtNewMethod.make(methods[m].getReturnType(), methods[m].getName(), methods[m].getParameterTypes(), methods[m].getExceptionTypes(), code, proxy);
               newMethod.setModifiers(Modifier.PUBLIC);
               proxy.addMethod(newMethod);
            }

            proxy.addInterface(intfClass);
            addedInterfaces.add(intfClass.getName());
         }
         it = intfs.keySet().iterator();
         while (it.hasNext())
         {
            String intf = (String) it.next();
            Integer idx = (Integer) intfs.get(intf);
            if (addedInterfaces.contains(intf)) throw new Exception("2 mixins are implementing the same interfaces");

            CtClass intfClass = pool.get(intf);
            CtMethod[] methods = intfClass.getMethods();
            HashSet mixinMethods = new HashSet();
            for (int m = 0; m < methods.length; m++)
            {
               if (methods[m].getDeclaringClass().getName().equals("java.lang.Object")) continue;
               Long hash = new Long(JavassistMethodHashing.methodHash(methods[m]));
               if (mixinMethods.contains(hash)) continue;
               if (addedMethods.contains(hash)) throw new Exception("More than one mixin has same method");
               mixinMethods.add(hash);
               addedMethods.add(hash);
               String aopReturnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
               String args = "null";
               if (methods[m].getParameterTypes().length > 0) args = "$args";
               String code = "{   " +
                             "    org.jboss.aop.MethodInfo mi = advisor.getMethodInfo(" + hash.longValue() + "L); " +
                             "    org.jboss.aop.joinpoint.MethodInvocation invocation = new org.jboss.aop.joinpoint.MethodInvocation(mi, mi.interceptors); " +
                             "    invocation.setArguments(" + args + "); " +
                             "    invocation.setTargetObject(mixins[" + idx + "]); " +
                             "    " + aopReturnStr + " invocation.invokeNext(); " +
                             "}";
               CtMethod newMethod = CtNewMethod.make(methods[m].getReturnType(), methods[m].getName(), methods[m].getParameterTypes(), methods[m].getExceptionTypes(), code, proxy);
               newMethod.setModifiers(Modifier.PUBLIC);
               proxy.addMethod(newMethod);
            }

            proxy.addInterface(intfClass);
            addedInterfaces.add(intfClass.getName());
         }
      }
      HashMap allMethods = JavassistMethodHashing.getMethodMap(superclass);

      Iterator it = allMethods.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry entry = (Map.Entry) it.next();
         CtMethod m = (CtMethod) entry.getValue();
         if (!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers())) continue;

         Long hash = (Long) entry.getKey();
         if (addedMethods.contains(hash)) continue;
         addedMethods.add(hash);
         String aopReturnStr = (m.getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
         String returnStr = (m.getReturnType().equals(CtClass.voidType)) ? "" : "return ";
         String args = "null";
         if (m.getParameterTypes().length > 0) args = "$args";
         String code = "{   " +
                       "    org.jboss.aop.MethodInfo mi = advisor.getMethodInfo(" + hash.longValue() + "L); " +
                       "    org.jboss.aop.advice.Interceptor[] interceptors = mi.interceptors; " +
                      "    if (interceptors != (Object[])null && interceptors.length > 0) { " +
                       "       org.jboss.aop.joinpoint.MethodInvocation invocation = new org.jboss.aop.joinpoint.MethodInvocation(mi, interceptors); " +
                       "       invocation.setArguments(" + args + "); " +
                       "       invocation.setTargetObject(delegate); " +
                       "       " + aopReturnStr + " invocation.invokeNext(); " +
                       "    } else { " +

                       "       " + returnStr + " delegate." + m.getName() + "($$); " +
                        "    } " +
                       "}";
         CtMethod newMethod = CtNewMethod.make(m.getReturnType(), m.getName(), m.getParameterTypes(), m.getExceptionTypes(), code, proxy);
         newMethod.setModifiers(Modifier.PUBLIC);
         proxy.addMethod(newMethod);
      }
      SerialVersionUID.setSerialVersionUID(proxy);
      return proxy;
   }

   private static Class generateProxy(Class clazz, ArrayList mixins) throws Exception
   {
      CtClass proxy = createProxyCtClass(mixins, clazz);
      Class proxyClass = proxy.toClass();
      return proxyClass;
   }

}
