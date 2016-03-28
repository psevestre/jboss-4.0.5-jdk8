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

import java.io.ObjectStreamException;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

import org.jboss.test.bank.interfaces.AccountData;
import org.jboss.test.bank.interfaces.Customer;

/**
 *      
 *   @author Rickard Oberg
 *   @author $Author: dimitris@jboss.org $
 *   @version $Revision: 57211 $
 */
public class AccountBeanCMP
   extends AccountBean
{
   // Constants -----------------------------------------------------
    
   // Attributes ----------------------------------------------------
   public String id;
   public float balance;
   public Customer owner;
   
   private boolean dirty;
   
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
      dirty = true;
   }
   
   public float getBalance()
   {
      return balance;
   }
   
   public void setBalance(float balance)
   {
      this.balance = balance;
      dirty = true;
   }
   
   public Customer getOwner()
   {
      return owner;
   }
   
   public void setOwner(Customer owner)
   {
      this.owner = owner;
      dirty = true;
   }
   
   public void setData(AccountData data)
   {
      setBalance(data.getBalance());
      setOwner(data.getOwner());
   }
   
   public AccountData getData()
   {
      AccountData data = new AccountData();
      data.setId(id);
      data.setBalance(balance);
      data.setOwner(owner);
      return data;
   }
   
   public boolean isModified()
   {
      return dirty;
   }
   
   // EntityBean implementation -------------------------------------
   public String ejbCreate(AccountData data) 
      throws RemoteException, CreateException
   { 
      setId(data.id);
      setData(data);
      dirty = false;
      return null;
   }
   
   public void ejbPostCreate(AccountData data) 
      throws RemoteException, CreateException
   { 
   }
   
   public void ejbLoad()
      throws RemoteException
   {
      super.ejbLoad();
      dirty = false;
   }
}

/*
 *   $Id: AccountBeanCMP.java 57211 2006-09-26 12:39:46Z dimitris@jboss.org $
 *   Currently locked by:$Locker$
 *   Revision:
 *   $Log$
 *   Revision 1.2.32.2  2005/10/29 05:04:33  starksm
 *   Update the LGPL header
 *
 *   Revision 1.2.32.1  2005/04/06 16:25:03  starksm
 *   Fix the license header
 *
 *   Revision 1.2  2001/01/07 23:14:34  peter
 *   Trying to get JAAS to work within test suite.
 *
 *   Revision 1.1.1.1  2000/06/21 15:52:37  oberg
 *   Initial import of jBoss test. This module contains CTS tests, some simple examples, and small bean suites.
 *
 *
 *  
 */
