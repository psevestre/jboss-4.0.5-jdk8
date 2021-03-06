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
package org.jboss.ejb.plugins.cmp.jdbc;

import org.jboss.ejb.plugins.cmp.jdbc.bridge.JDBCCMPFieldBridge;
import org.jboss.ejb.EntityEnterpriseContext;

/**
 *
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public interface LockingStrategy
{
   LockingStrategy VERSION = new AbstractStrategy()
   {
      public void loaded(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
         field.lockInstanceValue(ctx);
      }
   };

   LockingStrategy GROUP = new AbstractStrategy()
   {
      public void loaded(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
         field.lockInstanceValue(ctx);
      }
   };

   LockingStrategy READ = new AbstractStrategy()
   {
      public void accessed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
         field.lockInstanceValue(ctx);
      }

      public void changed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
         field.lockInstanceValue(ctx);
      }
   };

   LockingStrategy MODIFIED = new AbstractStrategy()
   {
      public void changed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
         field.lockInstanceValue(ctx);
      }
   };

   LockingStrategy NONE = new AbstractStrategy(){};

   class AbstractStrategy implements LockingStrategy
   {
      public void loaded(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
      }

      public void accessed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
      }

      public void changed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx)
      {
      }
   }

   void loaded(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx);
   void accessed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx);
   void changed(JDBCCMPFieldBridge field, EntityEnterpriseContext ctx);
}
