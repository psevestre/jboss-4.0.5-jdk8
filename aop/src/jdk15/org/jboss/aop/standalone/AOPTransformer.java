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
package org.jboss.aop.standalone;

import org.jboss.aop.AspectManager;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57185 $
 */
public class AOPTransformer implements ClassFileTransformer
{
   public boolean isNonAdvisableClassName(String classname)
   {
      return (classname.startsWith("org.jboss.aop") ||
      classname.endsWith("$aop") ||
      classname.startsWith("javassist") ||
      classname.startsWith("org.jboss.util.") ||
      classname.startsWith("gnu.trove.") ||
      classname.startsWith("EDU.oswego.cs.dl.util.concurrent.") ||
      // System classes
      classname.startsWith("org.apache.crimson") ||
      classname.startsWith("org.apache.xalan") ||
      classname.startsWith("org.apache.xml") ||
      classname.startsWith("org.apache.xpath") ||
      classname.startsWith("org.ietf.") ||
      classname.startsWith("org.omg.") ||
      classname.startsWith("org.w3c.") ||
      classname.startsWith("org.xml.sax.") ||
      classname.startsWith("sunw.") ||
      classname.startsWith("sun.") ||
      classname.startsWith("java.") ||
      classname.startsWith("javax.") ||
      classname.startsWith("com.sun.")
      );
   }

   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException
   {
      if (isNonAdvisableClassName(className)) return null;
      return aspectTransform(className, loader, classBeingRedefined, protectionDomain, classfileBuffer);
   }

   private byte[] aspectTransform(String className, ClassLoader loader, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
   {
      try
      {
         className = className.replace('/', '.');
         return AspectManager.instance().transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
}
