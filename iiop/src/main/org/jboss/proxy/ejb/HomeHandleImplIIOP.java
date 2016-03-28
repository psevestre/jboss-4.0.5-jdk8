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
package org.jboss.proxy.ejb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.HomeHandle;
import javax.ejb.spi.HandleDelegate;
import javax.rmi.PortableRemoteObject;

import org.jboss.iiop.CorbaORB;
import org.jboss.proxy.ejb.handle.HandleDelegateImpl;

/**
 * A CORBA-based EJB home handle implementation.
 *      
 * @author  <a href="mailto:rickard.oberg@telkel.com">Rickard Öberg</a>.
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author  <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 57194 $
 */
public class HomeHandleImplIIOP
      implements HomeHandle 
{
   /**
    * This handle encapsulates an stringfied CORBA reference for an 
    * <code>EJBHome</code>. 
    */
   private String ior;
   
   /**
    * Constructs a <code>HomeHandleImplIIOP</code>.
    *
    * @param ior     An stringfied CORBA reference for an <code>EJBHome</code>.
    */
   public HomeHandleImplIIOP(String ior) 
   {
      this.ior = ior;
   }
   
   /**
    * Constructs a <tt>HomeHandleImplIIOP</tt>.
    *
    * @param home    An <code>EJBHome</code>.
    */
   public HomeHandleImplIIOP(EJBHome home) 
   {
      this((org.omg.CORBA.Object)home);
   }
   
   /**
    * Constructs a <tt>HomeHandleImplIIOP</tt>.
    *
    * @param home    A CORBA reference for an <code>EJBHome</code>.
    */
   public HomeHandleImplIIOP(org.omg.CORBA.Object home) 
   {
      this.ior = CorbaORB.getInstance().object_to_string(home);
   }
   
   // Public --------------------------------------------------------
   
   // Handle implementation -----------------------------------------
   
   /**
    * Obtains the <code>EJBHome</code> represented by this home handle.
    *
    * @return  a reference to an <code>EJBHome</code>.
    *
    * @throws RemoteException
    */
   public EJBHome getEJBHome() 
         throws RemoteException 
   {
      try 
      {
         return (EJBHome)PortableRemoteObject.narrow(
                                  CorbaORB.getInstance().string_to_object(ior),
                                  EJBHome.class);
      }
      catch (Exception e) 
      {
         throw new RemoteException("Could not get EJBHome from HomeHandle");
      }
   }
   

   public void writeObject(ObjectOutputStream oostream) throws IOException
   {
      HandleDelegate delegate = HandleDelegateImpl.getDelegate();
      delegate.writeEJBHome(getEJBHome(), oostream);
   }

   public void readObject(ObjectInputStream oistream) throws IOException, ClassNotFoundException
   {
      HandleDelegate delegate = HandleDelegateImpl.getDelegate();
      EJBHome obj = delegate.readEJBHome(oistream);
      this.ior = CorbaORB.getInstance().object_to_string((org.omg.CORBA.Object) obj);
   }
}
