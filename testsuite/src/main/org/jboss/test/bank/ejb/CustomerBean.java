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
package org.jboss.test.bank.ejb;

import java.util.*;

import javax.naming.InitialContext;

import org.jboss.test.util.ejb.EntitySupport;
import org.jboss.test.bank.interfaces.*;

/**
 *      
 *   @author Rickard Oberg
 *   @author $Author: dimitris@jboss.org $
 *   @version $Revision: 57211 $
 */
public class CustomerBean
   extends EntitySupport
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
   public String id;
   public String name;
   public Collection accounts;
   
   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------
   public String getId()
   {
      return id;
   }
   
   public void setId(String id)
   {
      this.id = id;
   }
   
   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public Collection getAccounts()
   {
		return accounts;
   }
   
   public void addAccount(Account acct)
   {
   		accounts.add(acct);
   }
   
   public void removeAccount(Account acct)
   {
   		accounts.remove(acct);
   }
   
   // EntityHome implementation -------------------------------------
   public CustomerPK ejbCreate(String id, String name) 
   { 
      setId(id);
      setName(name);
      accounts = new ArrayList();
      
      CustomerPK pk = new CustomerPK();
      pk.id = id;
      pk.name = name;

      return pk;
   }
   
   public void ejbPostCreate(String id, String name) 
   { 
   }
}

/*
 *   $Id: CustomerBean.java 57211 2006-09-26 12:39:46Z dimitris@jboss.org $
 *   Currently locked by:$Locker$
 *   Revision:
 *   $Log$
 *   Revision 1.6.6.2  2005/10/29 05:04:33  starksm
 *   Update the LGPL header
 *
 *   Revision 1.6.6.1  2005/04/06 16:25:03  starksm
 *   Fix the license header
 *
 *   Revision 1.6  2003/08/27 04:32:49  patriot1burke
 *   4.0 rollback to 3.2
 *
 *   Revision 1.4  2001/01/20 16:32:51  osh
 *   More cleanup to avoid verifier warnings.
 *
 *   Revision 1.3  2001/01/07 23:14:34  peter
 *   Trying to get JAAS to work within test suite.
 *
 *   Revision 1.2  2000/09/30 01:00:54  fleury
 *   Updated bank tests to work with new jBoss version
 *
 *   Revision 1.1.1.1  2000/06/21 15:52:37  oberg
 *   Initial import of jBoss test. This module contains CTS tests, some simple examples, and small bean suites.
 *
 *
 *  
 */
