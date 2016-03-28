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
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.spi.HandleDelegate;
import javax.rmi.PortableRemoteObject;

import org.jboss.iiop.CorbaORB;
import org.jboss.proxy.ejb.handle.HandleDelegateImpl;

/**
 * A CORBA-based EJBObject handle implementation.
 *      
 * @author  <a href="mailto:rickard.oberg@telkel.com">Rickard Öberg</a>.
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author  <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 57194 $
 */
public class HandleImplIIOP implements Handle, Serializable
{
   /** @since 4.0.0 */
   static final long serialVersionUID = -501170775289648475L;

   /**
    * This handle encapsulates an stringfied CORBA reference for an 
    * <code>EJBObject</code>. 
    */
   private String ior;
   
   /**
    * Constructs an <code>HandleImplIIOP</code>.
    *
    * @param ior An stringfied CORBA reference for an <code>EJBObject</code>.
    */
   public HandleImplIIOP(String ior) 
   {
      this.ior = ior;
   }
   
   /**
    * Constructs an <tt>HandleImplIIOP</tt>.
    *
    * @param obj An <code>EJBObject</code>.
    */
   public HandleImplIIOP(EJBObject obj) 
   {
      this((org.omg.CORBA.Object)obj);
   }
   
   /**
    * Constructs an <tt>HandleImplIIOP</tt>.
    *
    * @param obj A CORBA reference for an <code>EJBObject</code>.
    */
   public HandleImplIIOP(org.omg.CORBA.Object obj) 
   {
      this.ior = CorbaORB.getInstance().object_to_string(obj);
   }
   
   // Public --------------------------------------------------------
   
   // Handle implementation -----------------------------------------
   
   /**
    * Obtains the <code>EJBObject</code> represented by this handle.
    *
    * @return  a reference to an <code>EJBObject</code>.
    *
    * @throws RemoteException
    */
   public EJBObject getEJBObject() 
         throws RemoteException 
   {
      try 
      {
         return (EJBObject)PortableRemoteObject.narrow(
                                  CorbaORB.getInstance().string_to_object(ior),
                                  EJBObject.class);
      }
      catch (Exception e) 
      {
         throw new RemoteException("Could not get EJBObject from Handle");
      }
   }

   private void writeObject(ObjectOutputStream oostream) throws IOException
   {
      HandleDelegate delegate = HandleDelegateImpl.getDelegate();
      delegate.writeEJBObject(getEJBObject(), oostream);
   }

   private void readObject(ObjectInputStream oistream) throws IOException, ClassNotFoundException
   {
      HandleDelegate delegate = HandleDelegateImpl.getDelegate();
      EJBObject obj = delegate.readEJBObject(oistream);
      this.ior = CorbaORB.getInstance().object_to_string((org.omg.CORBA.Object) obj);
   }
}
