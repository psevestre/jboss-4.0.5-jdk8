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
package org.jboss.mq.il.jvm;

import java.util.Properties;

import javax.naming.InitialContext;

import org.jboss.mq.GenericConnectionFactory;
import org.jboss.mq.il.ServerIL;
import org.jboss.mq.il.ServerILFactory;

/**
 *  Implements the ServerILJMXService which is used to manage the JVM IL.
 *
 * @author     Hiram Chirino (Cojonudo14@hotmail.com)
 * @author     David Maplesden (David.Maplesden@orion.co.nz)
 * @created    August 16, 2001
 * @version    $Revision: 57198 $
 *
 * @jmx:mbean extends="org.jboss.mq.il.ServerILJMXServiceMBean"
 */
public class JVMServerILService extends org.jboss.mq.il.ServerILJMXService implements JVMServerILServiceMBean
{

   /**
    *  Gives this JMX service a name.
    *
    * @return    The Name value
    */
   public String getName()
   {
      return "JBossMQ-JVMServerIL";
   }

   /**
    *  Used to construct the GenericConnectionFactory (bindJNDIReferences()
    *  builds it)
    *
    * @return     The ServerIL value
    * @returns    ServerIL the instance of this IL
    */
   public ServerIL getServerIL()
   {
      return new JVMServerIL(getJMSServer());
   }

   /**
    *  Used to construct the GenericConnectionFactory (bindJNDIReferences()
    *  builds it) Sets up the connection properties need by a client to use this
    *  IL
    *
    * @return    The ClientConnectionProperties value
    */
   public java.util.Properties getClientConnectionProperties()
   {
      Properties rc = super.getClientConnectionProperties();
      rc.setProperty(ServerILFactory.CLIENT_IL_SERVICE_KEY, "org.jboss.mq.il.jvm.JVMClientILService");
      rc.setProperty(ServerILFactory.SERVER_IL_FACTORY_KEY, "org.jboss.mq.il.jvm.JVMServerILFactory");
      return rc;
   }

   /**
    *  Starts this IL, and binds it to JNDI
    *
    * @exception  Exception  Description of Exception
    */
   public void startService() throws Exception
   {
      super.startService();
      bindJNDIReferences();
   }

   /**
    *  Stops this IL, and unbinds it from JNDI
    */
   public void stopService()
   {
      try
      {
         unbindJNDIReferences();
      }
      catch (Exception e)
      {
         log.error("Problem stopping JVMServerILService", e);
      }
   }

   /**
    *  Binds the connection factories for this IL
    *
    * @throws  javax.naming.NamingException  it cannot be unbound
    */
   public void bindJNDIReferences() throws javax.naming.NamingException
   {

      GenericConnectionFactory gcf = new GenericConnectionFactory(getServerIL(), getClientConnectionProperties());
      org.jboss.mq.SpyConnectionFactory scf = new org.jboss.mq.SpyConnectionFactory(gcf);
      org.jboss.mq.SpyXAConnectionFactory sxacf = new org.jboss.mq.SpyXAConnectionFactory(gcf);

      // Get an InitialContext
      InitialContext ctx = getInitialContext();
      org.jboss.naming.NonSerializableFactory.rebind(ctx, getConnectionFactoryJNDIRef(), scf);
      org.jboss.naming.NonSerializableFactory.rebind(ctx, getXAConnectionFactoryJNDIRef(), sxacf);

   }
}
