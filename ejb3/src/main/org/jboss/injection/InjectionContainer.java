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
package org.jboss.injection;

import org.jboss.ejb3.Container;
import org.jboss.ejb3.DependencyPolicy;
import org.jboss.ejb3.entity.PersistenceUnitDeployment;
import org.jboss.metamodel.descriptor.EnvironmentRefGroup;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;

/**
 * This is the container that manages all injections.  Could be an EJB Container
 * or a WAR.
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 56037 $
 */
public interface InjectionContainer
{
   /**
    * Some identifier that can be used in error messages
    *
    * @return
    */
   String getIdentifier();

   /**
    * For error messages
    *
    * @return  ejb-jar.xml, web.xml, etc..
    */
   String getDeploymentDescriptorType();

   ClassLoader getClassloader();

   Map<String, EncInjector> getEncInjectors();
   Map<String, Map<AccessibleObject, Injector>> getEncInjections();

   // EncInjectors/Handlers may need to add extra instance injectors
   List<Injector> getInjectors();

   Context getEnc();
   Context getEncEnv();


   PersistenceUnitDeployment getPersistenceUnitDeployment(String unitName) throws NameNotFoundException;

   Container resolveEjbContainer(String link, Class businessIntf);
   Container resolveEjbContainer(Class businessIntf) throws NameNotFoundException;
   String getEjbJndiName(Class businessInterface) throws NameNotFoundException;
   String getEjbJndiName(String link, Class businessInterface);

   /**
    * If class has container overridable annotations, this method will
    * discover those overriden annotations.
    *
    * @param annotationType
    * @param clazz
    * @return
    */
   <T> T getAnnotation(Class<T> annotationType, Class clazz);
   
   /**
    * If class has container overridable annotations, this method will
    * discover those overriden annotations.

    * @param annotationType
    * @param clazz
    * @param method
    * @return
    */
   <T> T getAnnotation(Class<T> annotationType, Class clazz, Method method);
   
   <T> T getAnnotation(Class<T> annotationType, Method method);

   /**
    * If class has container overridable annotations, this method will
    * discover those overriden annotations.

    * @param annotationType
    * @param clazz
    * @param field
    * @return
    */
   <T> T getAnnotation(Class<T> annotationType, Class clazz, Field field);
   
   <T> T getAnnotation(Class<T> annotationType, Field field);

   DependencyPolicy getDependencyPolicy();
   
   EnvironmentRefGroup getEnvironmentRefGroup();
   
}
