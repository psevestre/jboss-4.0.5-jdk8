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
package org.jboss.test.classloader.circularity.test;

import java.net.URL;
import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import org.jboss.test.classloader.circularity.support.Support;
import org.jboss.mx.loading.LoaderRepository;
import org.jboss.mx.loading.UnifiedClassLoader;
import org.jboss.mx.loading.UnifiedClassLoader3;
import org.jboss.mx.loading.UnifiedLoaderRepository3;

/**
 * @author Simone.Bordet@hp.com
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57211 $
 */
public class CircularityErrorTests
{
   private static Logger log = Logger.getLogger(CircularityErrorTests.class);

   private Object lock = new Object();
   private boolean sawCircularity;
   private boolean sawClassNotFound;

   public CircularityErrorTests()
   {
   }

   public void testClassCircularityError() throws Exception
   {
      // The scenario is this one:
      // Thread1 asks classloader1 to load class Derived
      // Thread2 triggers a loadClassInternal for classloader1 to load class Base
      // Thread2 is put in sleep by the ULR since we are loading Derived
      // Thread1 triggers a loadClassInternal for classloader1 to load class Base
      // Thread1 throws ClassCircularityError

      UnifiedLoaderRepository3 repository = new UnifiedLoaderRepository3();
      URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
      final UnifiedLoader ucl = new UnifiedLoader(url, repository);
      repository.addClassLoader(ucl);
      log.debug("Unified ClassLoader created");

      Class cls = ucl.loadClass("org.jboss.test.classloader.circularity.support.Support");

      Thread thread1 = new Thread(new Runnable()
      {
         public void run()
         {
            try
            {
               // Be sure thread2 is waiting
               try {Thread.sleep(1000);}
               catch (InterruptedException x) {}

               try
               {
                  // Ask this thread to load this class with this classloader
                  log.debug("Thread " + Thread.currentThread() + " loading...");
                  ucl.loadClass("org.jboss.test.classloader.circularity.support.Derived");
                  log.debug("Thread " + Thread.currentThread() + " loading done !");
               }
               catch (ClassCircularityError x)
               {
                  log.error("Saw ClassCircularityError", x);
                  sawCircularity = true;
               }
            }
            catch (ClassNotFoundException x)
            {
               log.error("Bug in the test: ", x);
               sawClassNotFound = true;
            }
         }
      }, "CircularityError Thread");
      thread1.start();

      // synchronized (lock)
      {
         log.debug("Thread " + Thread.currentThread() + " waiting...");
         // lock.wait();
         log.debug("Thread " + Thread.currentThread() + " woken up !");
      }

      // Ask this thread to trigger a loadClassInternal directly; the UnifiedLoaderRepository
      // will put this thread in sleep, but the JVM has already registered the fact that
      // it wants to load the class, in this case class Base
      cls.newInstance();

      // The ClassCircularityError thrown should allow the call above to complete
      if( sawCircularity )
         throw new ClassCircularityError("Got ClassCircularityError, UnifiedLoaderRepository is buggy");
      if( sawClassNotFound )
         throw new ClassNotFoundException("Got ClassNotFoundException, UnifiedLoaderRepository is buggy");
   }

   public class UnifiedLoader extends UnifiedClassLoader3
   {
      public UnifiedLoader(URL url, LoaderRepository repository)
      {
         super(url, url, repository);
      }

      public Class loadClass(String name) throws ClassNotFoundException
      {
         log.debug(Thread.currentThread() + " loadClass: " + name);
         return super.loadClass(name);
      }

      public Class loadClassLocally(String name, boolean resolve) throws ClassNotFoundException
      {
         log.debug(Thread.currentThread() + " is now asked to load class: " + name);

         if (name.equals("org.jboss.test.classloader.circularity.support.Derived"))
         {
            synchronized (lock)
            {
               lock.notifyAll();
            }

            // Wait to trigger ClassCircularityError
            // Do not release the lock on the classloader
            try
            {
               log.debug("Loading " + name + ", waiting...");
               Thread.sleep(2000);
               log.debug("Loading " + name + " end wait");
            }
            catch (InterruptedException x)
            {
               log.debug("Sleep was interrupted", x);
            }
         }

         return super.loadClassLocally(name, resolve);
      }
   }

}

