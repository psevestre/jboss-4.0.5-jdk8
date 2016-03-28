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
package org.jboss.test.cmp2.cmr.interfaces;

import java.rmi.RemoteException;
import java.util.SortedMap;

import javax.ejb.EJBObject;

/**
 * Remote interface for CMRBugManagerEJB.
 */
public interface CMRBugManagerEJB extends EJBObject
{
   /**
    * Describe <code>createCMRBugs</code> method here.
    * @param cmrBugs a <code>SortedMap</code> value
    */
   public void createCMRBugs( SortedMap cmrBugs )
      throws java.rmi.RemoteException;

   /**
    * Describe <code>getParentFor</code> method here.
    * @param id a <code>String</code> value
    * @return a <code>String[]</code> value
    */
   public String[] getParentFor( String id )
      throws RemoteException;

   public void setupLoadFKState(  )
      throws Exception, RemoteException;

   public void moveLastNodeBack(  )
      throws Exception, RemoteException;

   public boolean lastHasNextNode(  )
      throws Exception, RemoteException;

   public void tearDownLoadFKState(  )
      throws Exception, RemoteException;

}
