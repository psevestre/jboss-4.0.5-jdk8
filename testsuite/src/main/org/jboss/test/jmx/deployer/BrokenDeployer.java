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
package org.jboss.test.jmx.deployer;

import java.io.File;
import java.net.URL;
import javax.management.ObjectName;
import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.system.ServiceMBeanSupport;

/**
 * This is a test of many possible problems with deployments.
 * .xaa DeploymentException in init
 * .xbb NPE in init
 * .xcc DeploymentException in deploy
 * .xdd NPE in deploy
 * .xee DeploymentException in undeploy
 * .xff NPE in undeploy
 * .xgg deployment with a non-existent watch. (caused looping in bug 515537)
 *
 * The build script creates a BrokenDeployer.sar, which should be deployed before any 
 * of the test files as noted above are deployed.
 *
 * Created: Sun Feb 10 20:41:29 2002
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version $Revision: 57211 $
 * 
 * @jmx:mbean name="jboss.test:service=BrokenDeployer"
 * @jmx:interface extends="org.jboss.deployment.DeployerMBean"
 */
public class BrokenDeployer 
   extends ServiceMBeanSupport
   implements BrokenDeployerMBean
{
   public BrokenDeployer ()
   {      
   }

   public String getName()
   {
      return "Broken Deployer";
   }

   protected void startService() throws Exception
   {
      try
      {
         // Register with the main deployer
         server.invoke(
            org.jboss.deployment.MainDeployerMBean.OBJECT_NAME,
            "addDeployer",
            new Object[] {this},
            new String[] {"org.jboss.deployment.DeployerMBean"});
      }
      catch (Exception e) {log.error("Could not register with MainDeployer", e);}
  
      log.info("BrokenDeployer started");
   }
   
   
      
   
   /** undeploys all deployments */
   protected void stopService()
   {
      log.info("BrokenDeployer stopped");
      
      try
      {
         // Register with the main deployer
         server.invoke(
            org.jboss.deployment.MainDeployerMBean.OBJECT_NAME,
            "removeDeployer",
            new Object[] {this},
            new String[] {"org.jboss.deployment.DeployerMBean"});
      }
      catch (Exception e) {log.error("Could not register with MainDeployer", e);}
  
   }

   public boolean accepts(DeploymentInfo sdi)
   {
      log.info("asking about file: " + sdi.url.toString());
      String file = new File(sdi.url.getFile()).toString();
      
      log.info("now asking about file: " + file);
      return file.endsWith("xaa") 
         || file.endsWith("xbb")
         || file.endsWith("xcc")
         || file.endsWith("xdd")
         || file.endsWith("xee")
         || file.endsWith("xff")
         || file.endsWith("xgg");
   }

   public void init(DeploymentInfo sdi)
      throws DeploymentException
   {
      String file = new File(sdi.url.getFile()).toString();
      if (file.endsWith("xaa")) 
      {
         throw new DeploymentException("DeploymentException in init");
      } // end of if ()
      if (file.endsWith("xbb")) 
      {
         throw new NullPointerException("NullPointerException in init");
      } // end of if ()
      if (file.endsWith("xgg")) 
      {
         try 
         {
            sdi.watch = new URL("File:/nowhere.jar");
         }
         catch (Exception e)
         {
            log.error("could not create fake url");
         } // end of try-catch
         
      } // end of if ()
      
      
   }

   public void deploy(DeploymentInfo sdi)
      throws DeploymentException
   {
      String file = new File(sdi.url.getFile()).toString();
      if (file.endsWith("xcc")) 
      {
         throw new DeploymentException("DeploymentException in deploy");
      } // end of if ()
      if (file.endsWith("xdd")) 
      {
         throw new NullPointerException("NullPointerException in deploy");
      } // end of if ()
   }

   public void undeploy(DeploymentInfo sdi)
      throws DeploymentException
   {
      String file = new File(sdi.url.getFile()).toString();
      if (file.endsWith("xee")) 
      {
         throw new DeploymentException("DeploymentException in undeploy");
      } // end of if ()
      if (file.endsWith("xff")) 
      {
         throw new NullPointerException("NullPointerException in undeploy");
      } // end of if ()
   }
}// BrokenDeployer
