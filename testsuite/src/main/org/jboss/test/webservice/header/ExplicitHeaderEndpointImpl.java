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
package org.jboss.test.webservice.header;

import org.jboss.logging.Logger;
import org.jboss.util.id.UID;

import java.rmi.RemoteException;

/**
 * @author Thomas.Diesler@jboss.org
 * @since 26-Nov-2004
 */
public class ExplicitHeaderEndpointImpl implements ExplicitHeaderEndpoint
{
   // provide logging
   private final Logger log = Logger.getLogger(ExplicitHeaderEndpointImpl.class);

   public boolean doStuff(String parameter, SessionHeaderHolder headerHolder) throws RemoteException
   {
      if (parameter == null)
         throw new IllegalArgumentException("Null parameter");

      if (headerHolder == null)
         throw new IllegalArgumentException("Null headerHolder parameter");

      if (headerHolder.value == null)
         throw new IllegalArgumentException("Null headerHolder.value parameter");

      SessionHeader header = headerHolder.value;
      String username = header.getUsername();
      String sessionID = header.getSessionID();

      log.info("username: " + username);
      log.info("sessionID: " + sessionID);

      if (sessionID == null)
         header.setSessionID(UID.asString());

      return true;
   }
}
