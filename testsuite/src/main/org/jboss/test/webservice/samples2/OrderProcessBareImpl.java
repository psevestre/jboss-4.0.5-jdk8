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

/**
 * An example of a wrapped service endpoint impl
 *
 * @author Thomas.Diesler@jboss.org
 * @since 26-Apr-2004
 */
public class OrderProcessBareImpl implements OrderProcessBare
{
   public OrderProcess_processOrder_ResponseStruct processOrder(OrderProcess_processOrder_RequestStruct req) throws OrderException
   {
      OrderItem[] items = req.getArrayOfOrderItem_1();
      Person person = req.getPerson_2();

      if (person == null || person.getName() == null)
         throw new OrderException("Invalid person");

      if (items == null)
         throw new OrderException("Invalid order items");

      if (items.length > 3)
         throw new OrderException("Too many order items");

      OrderResponse ordRes = new OrderResponse(items, "approved");

      OrderProcess_processOrder_ResponseStruct res = new OrderProcess_processOrder_ResponseStruct(ordRes);
      return res;
   }
}
