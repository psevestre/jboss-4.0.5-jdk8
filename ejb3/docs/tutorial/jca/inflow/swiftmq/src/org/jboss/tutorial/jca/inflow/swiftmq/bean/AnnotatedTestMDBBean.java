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
package org.jboss.tutorial.jca.inflow.swiftmq.bean;

import org.jboss.annotation.ejb.ResourceAdapter;

import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;


@MessageDriven(name="testmdb", activationConfig =
{
@ActivationConfigProperty(propertyName="messagingType", propertyValue="javax.jms.MessageListener"),
@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
@ActivationConfigProperty(propertyName="Destination", propertyValue="testqueue"),
@ActivationConfigProperty(propertyName="ConnectionFactoryName", propertyValue="ConnectionFactory"),
@ActivationConfigProperty(propertyName="Transacted", propertyValue="true"),
@ActivationConfigProperty(propertyName="Xa", propertyValue="true"),
@ActivationConfigProperty(propertyName="DeliveryOption", propertyValue="B"),
@ActivationConfigProperty(propertyName="SubscriptionDurability", propertyValue="Durable"),
@ActivationConfigProperty(propertyName="MaxPoolSize", propertyValue="20"),
@ActivationConfigProperty(propertyName="MaxMessages", propertyValue="1"),
})
@ResourceAdapter("swiftmq.rar")
public class AnnotatedTestMDBBean
   implements MessageListener
{
   public void onMessage(Message message)
   {
      System.out.println(message);
   }
}
