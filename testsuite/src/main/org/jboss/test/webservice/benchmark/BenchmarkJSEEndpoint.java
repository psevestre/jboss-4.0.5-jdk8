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
package org.jboss.test.webservice.benchmark;

import java.rmi.RemoteException;

/**
 * A BenchmarkJSEEndpoint.
 * 
 * @author <a href="anders.hedstrom@home.se">Anders Hedstrom</a>
 * @version $Revision: 57211 $
 */
public class BenchmarkJSEEndpoint implements BenchmarkService {


   public SimpleUserType echoSimpleType(SimpleUserType simpleType) throws RemoteException
   {
      return simpleType;
   }

   public SimpleUserType[] echoArrayOfSimpleUserType(SimpleUserType[] array) throws RemoteException
   {
      return array;
   }

   public Synthetic echoSynthetic(Synthetic synthetic) throws RemoteException
   {
      return synthetic;
   }

   public Order getOrder(int orderId, int customerId) throws RemoteException
   {
      return new Order(orderId,customerId);
   }

}
