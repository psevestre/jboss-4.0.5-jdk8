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

import org.jboss.mq.il.ServerIL;
import org.jboss.mq.il.ServerILFactory;

/**
 *  Implements the ServerILFactory interface to create a new JVMServerIL
 *
 * @author     David Maplesden
 * @created    August 16, 2001
 * @version    $Revision: 57198 $
 */
public class JVMServerILFactory implements ServerILFactory {

   /**
    *  Used to construct the GenericConnectionFactory (bindJNDIReferences()
    *  builds it)
    *
    * @return                The ServerIL value
    * @exception  Exception  Description of Exception
    * @returns               ServerIL the instance of this IL
    */
   public ServerIL getServerIL()
      throws Exception {
      // We leave this for now, since a ServerIL seems to be bound in JNDI
      // in JVMServerILService.bindJNDIReferences()
      // FIXME, removed return of server, since it bypasses the new Invoker logic. Where is this used?
      //return new JVMServerIL( JMSServer.lookupJMSServer() );
      throw new Exception("WOW, JVM does not find its server to invok.!!!");
   }


   public void init( Properties init ) {
   }

}
