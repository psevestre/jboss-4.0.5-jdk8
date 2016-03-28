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
package javax.enterprise.deploy.model;

import javax.enterprise.deploy.shared.ModuleType;

/**
 * The root of a DDBean tree (a standard deployment descriptor)
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 57196 $
 */
public interface DDBeanRoot extends DDBean
{
   // Constants -----------------------------------------------------
   
   // Public --------------------------------------------------------

   /**
    * Return the module type of this descriptor
    *
    * @return the module type
    */
   ModuleType getType();
   
   /**
    * Get the deployable object for this descriptor
    *
    * @return the deployable object 
    */
   DeployableObject getDeployableObject();
   
   /**
    * A convenience method for information on the deployable object
    * 
    * @deprecated use getDDBeanRootVersion
    * @return the dtd version
    */
   String getModuleDTDVersion();
   
   /**
    * Return the version number of the j2ee xml document
    *
    * @return the version
    */
   String getDDBeanRootVersion();
   
   /**
    * Returns the xpath, the root path is "/"
    *
    * @return the root path "/"
    */
   String getXpath();
   
   /**
    * Returns the filename relative to the root of the module this XML
    * instance document.
    *
    * @return The file name relative to the root of the module
    */
   String getFilename();
}
