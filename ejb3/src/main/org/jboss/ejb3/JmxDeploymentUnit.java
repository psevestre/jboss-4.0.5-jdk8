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
package org.jboss.ejb3;

import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jboss.deployment.DeploymentInfo;
import org.jboss.ejb3.interceptor.InterceptorInfoRepository;
import org.jboss.mx.util.MBeanProxyExt;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
public class JmxDeploymentUnit implements DeploymentUnit
{
   private DeploymentInfo deploymentInfo;
   InterceptorInfoRepository interceptorInfoRepository = new InterceptorInfoRepository();

   URL extractDescriptorUrl(String resource)
   {
      String urlStr = deploymentInfo.url.getFile();
      // However the jar must also contain at least one ejb-jar.xml
      try
      {
         URL dd = deploymentInfo.localCl.findResource(resource);
         if (dd == null)
         {
            return null;
         }

         // If the DD url is not a subset of the urlStr then this is coming
         // from a jar referenced by the deployment jar manifest and the
         // this deployment jar it should not be treated as an ejb-jar
         if (deploymentInfo.localUrl != null)
         {
            urlStr = deploymentInfo.localUrl.toString();
         }

         String ddStr = dd.toString();
         if (ddStr.indexOf(urlStr) >= 0)
         {
            return dd;
         }
      }
      catch (Exception ignore)
      {
      }
      return null;
   }

   public URL getPersistenceXml()
   {
      return extractDescriptorUrl("META-INF/persistence.xml");
   }

   public URL getEjbJarXml()
   {
      return extractDescriptorUrl("META-INF/ejb-jar.xml");
   }

   public URL getJbossXml()
   {
      return extractDescriptorUrl("META-INF/jboss.xml");
   }

/*
      public URL getPersistenceXml()
      {
         return getResourceLoader().getResource("META-INF/persistence.xml");
      }

      public URL getEjbJarXml()
      {
         return getResourceLoader().getResource("META-INF/ejb-jar.xml");
      }

      public URL getJbossXml()
      {
         return getResourceLoader().getResource("META-INF/jboss.xml");
      }
*/

   public List<Class> getClasses()
   {
      return null;
   }

   public JmxDeploymentUnit(DeploymentInfo deploymentInfo)
   {
      this.deploymentInfo = deploymentInfo;
   }

   public ClassLoader getClassLoader()
   {
      return deploymentInfo.ucl;
   }

   public ClassLoader getResourceLoader()
   {
      return deploymentInfo.localCl;
   }

   public String getShortName()
   {
      return deploymentInfo.shortName;
   }

   public URL getUrl()
   {
      return deploymentInfo.url;
   }

   public String getDefaultEntityManagerName()
   {
      String url = getUrl().toString();
      String name = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
      return name;
   }

   public Map getDefaultPersistenceProperties()
   {
      try
      {
         EJB3DeployerMBean deployer = (EJB3DeployerMBean) MBeanProxyExt.create(EJB3DeployerMBean.class, EJB3DeployerMBean.OBJECT_NAME,
                                                                         deploymentInfo.getServer());

         return deployer.getDefaultProperties();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }


   public Hashtable getJndiProperties()
   {
      return null;
   }

   public InterceptorInfoRepository getInterceptorInfoRepository()
   {
      return interceptorInfoRepository;
   }
}
