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
package org.jboss.console.manager;

import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfig;
import org.jboss.system.server.ServerConfigLocator;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.URL;

/**
 * This class wraps the file system
 * for deployments.  It gives a file-based
 * persistence mechanism for deployments.
 * Used by web-console to store -service.xml files,
 * -ds.xml files, etc..., really anything text based.
 *
 * Deployments are tied to a specific name and that name
 * corresponds to the base file name.
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57191 $
 *
 **/
public class DeploymentFileRepository extends ServiceMBeanSupport implements DeploymentFileRepositoryMBean
{
   private String baseDir;
   private File base;
   /** The server's home directory, for relative paths. */
   protected File serverHome;
   protected URL serverHomeURL;

   /**
    *
    * @param folder relative directory
    * @param name  base name of file.  Whitespace will be removed from name and replaced with '_'
    * @param fileExtension must have a '.' in ext
    * @param data
    * @param noHotDeploy  keep timestamp of file so it doesn't do a redeploy
    * @throws IOException
    */
   public void store(String folder, String name, String fileExtension, String data, boolean noHotDeploy) throws IOException
   {
      log.debug("store called");
      File dir = new File(base, folder);
      log.debug("respository folder: " + dir.toString());
      log.debug("absolute: " + dir.getAbsolutePath());
      if (!dir.exists())
      {
         if (!dir.mkdirs())
         {
            throw new RuntimeException("Failed to create directory: " + dir.toString());
         }
      }
      String filename = name.replace(' ', '_') + fileExtension;
      File file = new File(dir, filename);
      File tmpfile = new File(dir, filename + ".tmp");
      PrintWriter writer = new PrintWriter(new FileOutputStream(tmpfile));
      writer.write(data);
      writer.close();
      if (file.exists() && noHotDeploy)
      {
         long modified = file.lastModified();
         tmpfile.setLastModified(modified);
         file.delete();
      }
      if (!tmpfile.renameTo(file))
      {
         throw new RuntimeException("Failed to rename tmpfile to " + file.toString());
      }
   }

   public void remove(String folder, String name, String fileExtension)
   {
      File dir = new File(base, folder);
      String filename = name.replace(' ', '_') + fileExtension;
      File file = new File(dir, filename);
      file.delete();
   }

   public boolean isStored(String folder, String name, String fileExtension)
   {
      File dir = new File(base, folder);
      String filename = name.replace(' ', '_') + fileExtension;
      File file = new File(dir, filename);
      return file.exists();
   }

   public String getBaseDir()
   {
      return baseDir;
   }

   public void setBaseDir(String baseDir)
   {
      this.baseDir = baseDir;
      this.base = new File(serverHome, baseDir);
   }


   public ObjectName preRegister(MBeanServer server, ObjectName name)
      throws Exception
   {
      // get server's home for relative paths, need this for setting
      // attribute final values, so we need to do it here
      ServerConfig serverConfig = ServerConfigLocator.locate();
      serverHome = serverConfig.getServerHomeDir();
      return super.preRegister(server, name);
   }

}
