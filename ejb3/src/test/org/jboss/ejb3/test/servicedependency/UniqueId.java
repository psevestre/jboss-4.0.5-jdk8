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
package org.jboss.ejb3.test.servicedependency;

import java.util.UUID;

import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;
import org.jboss.logging.Logger;

/**
 * A UniqueIdMBean.
 * 
 * @author <a href="galder.zamarreno@jboss.com">Galder Zamarreno</a>
 * @version $Revision: 57207 $
 */
@Service(objectName="acme:service=uniqueid")
@Management(UniqueIdMBean.class)
public class UniqueId implements UniqueIdMBean
{
   private static final Logger log = Logger.getLogger(UniqueId.class);

   public UUID generate()
   {
      return UUID.randomUUID();
   }

   public void create() throws Exception
   {
      log.info("create()");

   }

   public void start() throws Exception
   {
      log.info("start()");

   }

   public void stop() throws Exception
   {
      log.info("stop()");

   }

   public void destroy() throws Exception
   {
      log.info("destroy()");

   }

}
