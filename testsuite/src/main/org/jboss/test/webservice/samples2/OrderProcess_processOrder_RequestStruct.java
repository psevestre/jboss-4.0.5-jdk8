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
package org.jboss.test.webservice.samples2;

public class OrderProcess_processOrder_RequestStruct
{

   protected org.jboss.test.webservice.samples2.OrderItem[] arrayOfOrderItem_1;

   protected org.jboss.test.webservice.samples2.Person person_2;

   public OrderProcess_processOrder_RequestStruct()
   {
   }

   public OrderProcess_processOrder_RequestStruct(org.jboss.test.webservice.samples2.OrderItem[] arrayOfOrderItem_1,
         org.jboss.test.webservice.samples2.Person person_2)
   {
      this.arrayOfOrderItem_1 = arrayOfOrderItem_1;
      this.person_2 = person_2;
   }

   public org.jboss.test.webservice.samples2.OrderItem[] getArrayOfOrderItem_1()
   {
      return arrayOfOrderItem_1;
   }

   public void setArrayOfOrderItem_1(org.jboss.test.webservice.samples2.OrderItem[] arrayOfOrderItem_1)
   {
      this.arrayOfOrderItem_1 = arrayOfOrderItem_1;
   }

   public org.jboss.test.webservice.samples2.Person getPerson_2()
   {
      return person_2;
   }

   public void setPerson_2(org.jboss.test.webservice.samples2.Person person_2)
   {
      this.person_2 = person_2;
   }

}
