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
package org.jboss.test.cache.bean;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * Remote interface for test/TreeCacheTester.
 */
public interface TreeCacheTester extends EJBObject
{ 
   public java.util.Vector getMembers()
      throws RemoteException;

   public void setCacheMode( int mode )
      throws Exception, RemoteException;

   public void setIsolationLevel( org.jboss.cache.lock.IsolationLevel level )
      throws RemoteException;

   public java.util.Set getKeys( String fqn )
      throws org.jboss.cache.CacheException, RemoteException;

   public Object get( String fqn,String key )
      throws org.jboss.cache.CacheException, RemoteException;

   public boolean exists( String fqn )
      throws RemoteException;

   public void put( String fqn,java.util.Map data )
      throws Exception, RemoteException;

   public Object put( String fqn,String key,Object value )
      throws Exception, RemoteException;

   public void remove( String fqn ) throws Exception, RemoteException;

   public Object remove( String fqn,String key )
      throws Exception, RemoteException;

   public void releaseAllLocks( String fqn ) throws RemoteException;

   public String print( String fqn ) throws RemoteException;

   public java.util.Set getChildrenNames( String fqn )
      throws org.jboss.cache.CacheException, RemoteException;

   public String printDetails() throws RemoteException;

   public String printLockInfo() throws RemoteException;

}
