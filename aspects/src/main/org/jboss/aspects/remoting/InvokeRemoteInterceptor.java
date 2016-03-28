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
package org.jboss.aspects.remoting;

import java.io.ObjectStreamException;

import org.jboss.remoting.Client;
import org.jboss.remoting.InvokerLocator;
/**
 * Checks to see if this object is local in VM
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57186 $
 */
public class InvokeRemoteInterceptor implements org.jboss.aop.advice.Interceptor, java.io.Serializable
{
   private static final long serialVersionUID = -145166951731929406L;
   
   public static final InvokeRemoteInterceptor singleton = new InvokeRemoteInterceptor();
   public static final String REMOTING = "REMOTING";
   public static final String INVOKER_LOCATOR = "INVOKER_LOCATOR";
   public static final String SUBSYSTEM = "SUBSYSTEM";

   public String getName() { return "InvokeRemoteInterceptor"; }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      InvokerLocator locator = (InvokerLocator)invocation.getMetaData(REMOTING, INVOKER_LOCATOR);
      if (locator == null)
      {
         throw new RuntimeException("No InvokerLocator supplied.  Can't invoke remotely!");
      }
      String subsystem = (String)invocation.getMetaData(REMOTING, SUBSYSTEM);
      if (subsystem == null) subsystem = "AOP";
      Client client = new Client(locator, subsystem);
      org.jboss.aop.joinpoint.InvocationResponse response = (org.jboss.aop.joinpoint.InvocationResponse)client.invoke(invocation, null);
      invocation.setResponseContextInfo(response.getContextInfo());
      return response.getResponse();
   }

   Object readResolve() throws ObjectStreamException {
      return singleton;
   }
}
