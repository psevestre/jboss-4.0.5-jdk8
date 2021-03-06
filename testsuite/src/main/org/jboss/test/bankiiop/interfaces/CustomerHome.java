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
package org.jboss.test.bankiiop.interfaces;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

/**
 *      
 *   @author Rickard Oberg
 *   @author $Author: dimitris@jboss.org $
 *   @version $Revision: 57211 $
 */
public interface CustomerHome
   extends EJBHome
{
   // Constants -----------------------------------------------------
   public static final String COMP_NAME = "java:comp/env/ejb/Customer";
   public static final String JNDI_NAME = "bank-iiop/Customer";
    
   // Attributes ----------------------------------------------------
   
   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------
   public Customer create(String id, String name)
      throws RemoteException, CreateException;
   
   public Customer findByPrimaryKey(CustomerPK pk)
      throws RemoteException, FinderException;
      
   public Collection findAll()
      throws RemoteException, FinderException;
}
