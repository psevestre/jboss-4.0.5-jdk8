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
package org.jboss.test.webservice.encstyle;

import java.rmi.RemoteException;

/**
 * @author Thomas.Diesler@jboss.org
 * @since 11-Nov-2004
 */
public class SampleEndpointImplDOC implements SampleEndpointDOC
{

   public SampleEndpoint_changeSalary_ResponseStruct changeSalary(SampleEndpoint_changeSalary_RequestStruct in) throws RemoteException
   {
      UserType user = in.getUserType_1();
      Integer amount = in.getInteger_2();
      String retstr = "Hello " + user.getFirstName() + " " + user.getLastName() + "! Your salary is: " + amount;
      return new SampleEndpoint_changeSalary_ResponseStruct(retstr);
   }

}
