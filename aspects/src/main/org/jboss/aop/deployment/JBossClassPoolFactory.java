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
package org.jboss.aop.deployment;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jboss.aop.AspectManager;
import org.jboss.aop.classpool.AOPClassPool;
import org.jboss.mx.loading.RepositoryClassLoader;
import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57186 $
 *
 **/
public class JBossClassPoolFactory implements ScopedClassPoolFactory
{
   protected File tmpClassesDir;

   public JBossClassPoolFactory(File tmpClassesDir) throws IOException
   {
      this.tmpClassesDir = tmpClassesDir;

   }
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      try
      {
         if (cl instanceof RepositoryClassLoader)
         {
            File tempdir = createTempDir(cl);
            URL tmpCP = createURLAndAddToLoader(cl, tempdir);
            if (AspectManager.scopedCLHelper.ifScopedDeploymentGetScopedParentUclForCL(cl) != null)
            {
               //It is scoped
               return new ScopedJBossClassPool(cl, src, repository, tempdir, tmpCP);
            }
            return new JBossClassPool(cl, src, repository, tempdir, tmpCP);
         }
         return new AOPClassPool(cl, src, repository);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository)
   {
      return new JBossClassPool(src, repository);
   }

   public File createTempDir(ClassLoader cl) throws IOException
   {
      File tempdir = File.createTempFile("ucl", "", tmpClassesDir);
      tempdir.delete();
      tempdir.mkdir();
      tempdir.deleteOnExit();

      return tempdir;
   }
   
   private URL createURLAndAddToLoader(ClassLoader cl, File tempdir) throws IOException
   {
      URL tmpURL = tempdir.toURL();
      URL tmpCP = new URL(tmpURL, "?dynamic=true");

      RepositoryClassLoader ucl = (RepositoryClassLoader) cl;

      // We may be undeploying.
      if (ucl.getLoaderRepository() != null)
      {
         ucl.addURL(tmpCP);
      }
      
      return tmpCP;
   }
}
