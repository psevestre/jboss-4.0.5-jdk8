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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.jboss.metamodel.descriptor.WebServiceRef;
import org.jboss.metamodel.descriptor.EnvironmentRefGroup;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 55144 $</tt>
 */
public class WebServiceHandler implements InjectionHandler
{
   private static final Logger log = Logger.getLogger(WebServiceHandler.class);

   public void loadXml(EnvironmentRefGroup xml, InjectionContainer container)
   {
      if (xml == null) return;
      if (xml.getWebServiceRefs() == null) return;
      for (WebServiceRef wsRef : xml.getWebServiceRefs())
      {
         if (wsRef.getMappedName() == null || wsRef.getMappedName().equals(""))
            throw new RuntimeException("mapped-name is required for <service-ref> " + wsRef.getServiceRefName() + " of " + container.getIdentifier());

         String encName = "env/" + wsRef.getServiceRefName();
         if (!container.getEncInjectors().containsKey(encName))
         {
            container.getEncInjectors().put(encName, new LinkRefEncInjector(encName, wsRef.getMappedName(), "jndi ref"));
         }
         InjectionUtil.injectionTarget(encName, wsRef, container, container.getEncInjections());
      }
   }

   public void handleClassAnnotations(Class clazz, InjectionContainer container)
   {
      javax.xml.ws.WebServiceRef ref = container.getAnnotation(javax.xml.ws.WebServiceRef.class, clazz);
      if (ref == null) return;
      String encName = ref.name();
      if (encName == null || encName.equals(""))
      {
         throw new RuntimeException("JBoss requires name() for class level @WebServiceRef");
      }
      encName = "env/" + ref.name();
      if (container.getEncInjectors().containsKey(encName)) return;

      String mappedName = ref.mappedName();
      if (mappedName == null || mappedName.equals(""))
      {
         throw new RuntimeException("You did not specify a @WebServiceRef.mappedName() on " + clazz.getName() + " and there is no binding for that enc name in XML");
      }
      container.getEncInjectors().put(encName, new LinkRefEncInjector(encName, ref.mappedName(), "@WebServiceRef"));
   }

   public void handleMethodAnnotations(Method method, InjectionContainer container, Map<AccessibleObject, Injector> injectors)
   {
      javax.xml.ws.WebServiceRef ref = method.getAnnotation(javax.xml.ws.WebServiceRef.class);
      if (ref == null) return;
      if (!method.getName().startsWith("set"))
         throw new RuntimeException("@ javax.xml.ws.WebServiceRef can only be used with a set method: " + method);
      String encName = ref.name();
      if (encName == null || encName.equals(""))
      {
         encName = InjectionUtil.getEncName(method);
      }
      else
      {
         encName = "env/" + encName;
      }
      if (!container.getEncInjectors().containsKey(encName))
      {
         container.getEncInjectors().put(encName, new LinkRefEncInjector(encName, ref.mappedName(), "@ javax.xml.ws.WebServiceRef"));
      }

      injectors.put(method, new JndiMethodInjector(method, encName, container.getEnc()));
   }

   public void handleFieldAnnotations(Field field, InjectionContainer container, Map<AccessibleObject, Injector> injectors)
   {
      javax.xml.ws.WebServiceRef ref = field.getAnnotation(javax.xml.ws.WebServiceRef.class);
      if (ref == null) return;
      String encName = ref.name();
      if (encName == null || encName.equals(""))
      {
         encName = InjectionUtil.getEncName(field);
      }
      else
      {
         encName = "env/" + encName;
      }
      if (!container.getEncInjectors().containsKey(encName))
      {
         container.getEncInjectors().put(encName, new LinkRefEncInjector(encName, ref.mappedName(), "@ javax.xml.ws.WebServiceRef"));
      }

      injectors.put(field, new JndiFieldInjector(field, encName, container.getEnc()));
   }
}
