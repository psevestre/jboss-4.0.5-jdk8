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
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javassist.scopedpool.ScopedClassPoolFactory;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.jboss.aop.AspectManager;
import org.jboss.aop.AspectNotificationHandler;
import org.jboss.aop.AspectXmlLoader;
import org.jboss.aop.ClassLoaderValidation;
import org.jboss.aop.Deployment;
import org.jboss.aop.hook.JDK14Transformer;
import org.jboss.aop.hook.JDK14TransformerManager;
import org.jboss.aop.instrument.InstrumentorFactory;
import org.jboss.mx.server.ServerConstants;
import org.jboss.mx.util.ObjectNameFactory;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfig;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57186 $
 * @jmx:mbean extends="org.jboss.system.ServiceMBean"
 */
public class AspectManagerService
        extends ServiceMBeanSupport
        implements AspectManagerServiceMBean, AspectNotificationHandler
{
   public static final ObjectName DEFAULT_LOADER_REPOSITORY = ObjectNameFactory.create(ServerConstants.DEFAULT_LOADER_NAME);

   // Attributes ---------------------------------------------------

   boolean created = false;
   protected File tmpClassesDir;
   protected boolean enableTransformer = false;
   protected boolean enableLoadtimeWeaving = false;
   protected boolean suppressTransformationErrors = true;
   protected boolean suppressReferenceErrors = true;
   protected String exclude;
   protected String include;
   protected String ignore;

   // Static -------------------------------------------------------

   // Constructors -------------------------------------------------
   public AspectManagerService()
   {
   }

   // Public -------------------------------------------------------

   protected ScopedClassPoolFactory createFactory() throws Exception
   {
      return new JBossClassPoolFactory(tmpClassesDir);
   }

   protected ClassLoaderValidation createClassLoaderValidation()
   {
      return new JBossClassLoaderValidator();
   }

   protected void createService()
           throws Exception
   {
      // Set a default tmp classes dir to the jboss tmp dir/aopclasses
      if (tmpClassesDir == null)
      {
         String jbossTmpDir = System.getProperty(ServerConfig.SERVER_TEMP_DIR);
         if (jbossTmpDir == null)
            jbossTmpDir = System.getProperty("java.io.tmpdir");
         tmpClassesDir = new File(jbossTmpDir, "aopdynclasses");
      }
      // Validate the the tmp dir exists
      if (tmpClassesDir.exists() == false && tmpClassesDir.mkdirs() == false)
         throw new FileNotFoundException("Failed to create tmpClassesDir: " + tmpClassesDir.getAbsolutePath());
      AspectManager.setClassPoolFactory(createFactory());
      
      AspectManager.classLoaderValidator = createClassLoaderValidation();
      // Add the tmp classes dir to our UCL classpath

      Deployment.searchClasspath = false; // turn off dynamic finding of DDs
      AspectManager.suppressTransformationErrors = suppressTransformationErrors;
      if (enableTransformer && enableLoadtimeWeaving) throw new RuntimeException("Cannot set both EnableTransformer and EnableLoadtimeWeaving");
      if (enableTransformer)
      {
         attachDeprecatedTranslator();
      }
      if (enableLoadtimeWeaving)
      {
         attachTranslator();
      }
      created = true;
      AspectManager.notificationHandler = this;
      
      AspectManager.scopedCLHelper = new JBossScopedClassLoaderHelper();
      
      baseAop();
   }

   protected void baseAop()
   {
      try
      {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         URL base = cl.getResource("base-aop.xml");
         AspectXmlLoader.deployXML(base);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
      }
   }

   protected void attachDeprecatedTranslator()
   {
      log.warn("EnableTransformer has been deprecated, please use EnableLoadtimeWeaving.  See docs for more details");
      AspectManager mgr = AspectManager.instance();
      try
      {
         server.setAttribute(DEFAULT_LOADER_REPOSITORY, new Attribute("Translator", mgr));
      }
      catch (InstanceNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (AttributeNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (InvalidAttributeValueException e)
      {
         throw new RuntimeException(e);
      }
      catch (MBeanException e)
      {
         throw new RuntimeException(e);
      }
      catch (ReflectionException e)
      {
         throw new RuntimeException(e);
      }
   }

   protected void detachDeprecatedTranslator()
   {
      try
      {
         server.setAttribute(DEFAULT_LOADER_REPOSITORY, new Attribute("Translator", null));
      }
      catch (InstanceNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (AttributeNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (InvalidAttributeValueException e)
      {
         throw new RuntimeException(e);
      }
      catch (MBeanException e)
      {
         throw new RuntimeException(e);
      }
      catch (ReflectionException e)
      {
         throw new RuntimeException(e);
      }
   }

   protected void attachTranslator()
   {
      JDK14TransformerManager.transformer = new JDK14Transformer()
      {
         public byte[] transform(ClassLoader loader, String classname, byte[] classBytes)
         {
            try
            {
               //Make sure that we use the correct classloader, in order to get the correct domain if it is a scoped loader
               return AspectManager.instance(loader).translate(classname, loader, classBytes);
            }
            catch (Exception e)
            {
               throw new RuntimeException(e);
            }
         }
      };
   }

   protected void detachTranslator()
   {
      JDK14TransformerManager.transformer = null;
   }

   public void attachClass(String classname)
   {
      Notification msg = new Notification("AOP class attached", this, getNextNotificationSequenceNumber());
      msg.setUserData(classname);
      sendNotification(msg);
   }

   protected void startService()
           throws Exception
   {
   }

   protected void stopService()
   {
   }

   public boolean getPrune()
   {
      return AspectManager.getPrune();
   }

   public void setPrune(boolean prune)
   {
      AspectManager.setPrune(prune);
   }

   public String getExclude()
   {
      return exclude;
   }

   public void setExclude(String exclude)
   {
      this.exclude = exclude;
      ArrayList list = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(exclude, ",");
      while (tokenizer.hasMoreTokens())
      {
         list.add(tokenizer.nextToken().trim());
      }
      AspectManager.instance().setExclude(list);
   }

   public String getInclude()
   {
      return include;
   }

   public void setInclude(String include)
   {
      this.include = include;
      ArrayList list = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(include, ",");
      while (tokenizer.hasMoreTokens())
      {
         list.add(tokenizer.nextToken().trim());
      }
      AspectManager.instance().setInclude(list);
   }

   public String getIgnore()
   {
      return ignore;
   }

   public void setIgnore(String ignore)
   {
      this.ignore = ignore;
      ArrayList list = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(ignore, ",");
      while (tokenizer.hasMoreTokens())
      {
         list.add(tokenizer.nextToken().trim());
      }
      AspectManager.instance().setIgnore(list);
   }

   
   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-attribute
    */
   public File getTmpClassesDir()
   {
      return tmpClassesDir;
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-attribute
    */
   public void setTmpClassesDir(File tmpClassesDir)
   {
      this.tmpClassesDir = tmpClassesDir;
   }

   /**
    * Set the verbosity of aop logging.  It doesn't use log4j
    *
    * @jmx:managed-attribute
    */
   public boolean getVerbose()
   {
      return AspectManager.verbose;
   }

   /**
    * Set the verbosity of aop logging.  It doesn't use log4j
    *
    * @jmx:managed-attribute
    */
   public void setVerbose(boolean verbose)
   {
      AspectManager.verbose = verbose;
   }

   /**
    * Use aop optimizations.  Optional just in case there is a bug
    *
    * @jmx:managed-attribute
    */
   public boolean getOptimized()
   {
      return AspectManager.optimize;
   }

   /**
    * Use aop optimizations.  Optional just in case there is a bug
    *
    * @jmx:managed-attribute
    */
   public void setOptimized(boolean verbose)
   {
      AspectManager.optimize = verbose;
   }

   /**
    * @jmx:managed-attribute
    */
   public boolean getSuppressTransformationErrors()
   {
      return suppressTransformationErrors;
   }

   /**
    * @jmx:managed-attribute
    */
   public void setSuppressTransformationErrors(boolean suppressTransformationErrors)
   {
      this.suppressTransformationErrors = suppressTransformationErrors;
      AspectManager.suppressTransformationErrors = suppressTransformationErrors;
   }

   /**
    * @jmx:managed-attribute
    */
   public boolean getSuppressReferenceErrors()
   {
      return suppressReferenceErrors;
   }

   /**
    * @jmx:managed-attribute
    */
   public void setSuppressReferenceErrors(boolean suppressReferenceErrors)
   {
      this.suppressReferenceErrors = suppressReferenceErrors;
      AspectManager.suppressReferenceErrors = suppressReferenceErrors;
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-attribute
    */
   public boolean getEnableTransformer()
   {
      return enableTransformer;
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-operation
    */
   public String interceptorFactories()
   {
      Map factories = AspectManager.instance().getInterceptorFactories();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-operation
    */
   public String aspectDefinitions()
   {
      Map factories = AspectManager.instance().getAspectDefinitions();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * @jmx:managed-operation
    */
   public String introductions()
   {
      Map factories = AspectManager.instance().getInterfaceIntroductions();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-operation
    */
   public String stacks()
   {
      Map factories = AspectManager.instance().getInterceptorStacks();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-operation
    */
   public String bindings()
   {
      Map factories = AspectManager.instance().getBindings();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-operation
    */
   public String registeredClassLoaders()
   {
      Map factories = AspectManager.instance().getRegisteredCLs();
      Iterator it = factories.keySet().iterator();
      StringBuffer buffer = new StringBuffer("");
      while (it.hasNext())
      {
         buffer.append(it.next() + "<br>");
      }
      return buffer.toString();
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-attribute
    */
   public void setEnableTransformer(boolean enableTransformer)
   {
      // Testsuite uses enableTransformer, we may be testing new loadtime features though.

      if (enableLoadtimeWeaving)
      {
         log.warn("enabledLoadtimeWeaving alread set");
         return;
      }
      if (this.enableTransformer == enableTransformer) return;
      if (this.getState() == STARTED)
      {
         if (enableTransformer)
         {
            attachDeprecatedTranslator();
         }
         else
         {
            detachDeprecatedTranslator();
         }
      }
      this.enableTransformer = enableTransformer;
   }

   public boolean getEnableLoadtimeWeaving()
   {
      return enableLoadtimeWeaving;
   }

   /**
    * The temporary directory to which dyn class files are written
    *
    * @jmx:managed-attribute
    */
   public void setEnableLoadtimeWeaving(boolean enableTransformer)
   {
      if (this.enableLoadtimeWeaving == enableTransformer) return;
      if (this.getState() == STARTED)
      {
         if (enableTransformer)
         {
            attachTranslator();
         }
         else
         {
            detachTranslator();
         }
      }
      this.enableLoadtimeWeaving = enableTransformer;
   }
   
   public String getInstrumentor()
   {
      return InstrumentorFactory.getInstrumentorName();
   }
   
   public void setInstrumentor(String instrumentor)
   {
      InstrumentorFactory.initialise(instrumentor);
   }

}
