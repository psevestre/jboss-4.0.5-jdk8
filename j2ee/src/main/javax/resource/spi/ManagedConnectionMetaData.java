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
package javax.resource.spi;

import javax.resource.ResourceException;

/**
 * The ManagedConnectionMetaData interface provides information about the
 * underlying resource associated with a ManagedConnetion. The Application
 * Server can use this information to get information at runtime from the
 * underlying resource.
 */
public interface ManagedConnectionMetaData
{
   /**
	 * Returns product name of the underlying resource.
    * 
    * @return the product name
    * @throws ResourceException for a generic error 
	 */
   public String getEISProductName() throws ResourceException;

   /**
	 * Returns product version of the underlying resource.
    * 
    * @return the product version
    * @throws ResourceException for a generic error 
	 */
   public String getEISProductVersion() throws ResourceException;

   /**
	 * Returns the maximum supported number of connections allowed to the
	 * underlying resource.
    * 
    * @return the maximum number of connections
    * @throws ResourceException for a generic error 
	 */
   public int getMaxConnections() throws ResourceException;

   /**
	 * Returns user name associated with the underlying connection.
    * 
    * @return the user name
    * @throws ResourceException for a generic error 
	 */
   public String getUserName() throws ResourceException;
}