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
package org.jboss.ejb.plugins.cmp.jdbc.bridge;

import org.jboss.ejb.EntityEnterpriseContext;
import org.jboss.ejb.plugins.cmp.jdbc.JDBCStoreManager;
import org.jboss.ejb.plugins.cmp.jdbc.metadata.JDBCCMPFieldMetaData;
import org.jboss.deployment.DeploymentException;

/**
 *
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 */
public class JDBCLongVersionFieldBridge extends JDBCCMP2xVersionFieldBridge
{
   private static final Long FIRST_VERSION = new Long(1);

   public JDBCLongVersionFieldBridge(JDBCStoreManager manager,
                                     JDBCCMPFieldMetaData metadata)
      throws DeploymentException
   {
      super(manager, metadata);
   }

   public JDBCLongVersionFieldBridge(JDBCCMP2xFieldBridge cmpField)
      throws DeploymentException
   {
      super(cmpField);
   }

   public void setFirstVersion(EntityEnterpriseContext ctx)
   {
      setInstanceValue(ctx, FIRST_VERSION);
   }

   public Object updateVersion(EntityEnterpriseContext ctx)
   {
      final Long value = (Long)getInstanceValue(ctx);
      Long next = value == null ? FIRST_VERSION : new Long(value.longValue() + 1);
      setInstanceValue(ctx, next);
      return next;
   }
}
