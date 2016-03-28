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
package org.jboss.metamodel.descriptor;

import javax.persistence.PersistenceContextType;

import org.jboss.metamodel.descriptor.Ref;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 46506 $
 */
public class PersistenceContextRef extends Ref
{
   private String refName;
   private String unitName;
   private PersistenceContextType persistenceContextType;

   public String getRefName()
   {
      return refName;
   }

   public void setRefName(String refName)
   {
      this.refName = refName;
   }

   public String getUnitName()
   {
      return unitName;
   }

   public void setUnitName(String unitName)
   {
      this.unitName = unitName;
   }

   public PersistenceContextType getPersistenceContextType()
   {
      return persistenceContextType;
   }

   public void setPersistenceContextType(PersistenceContextType persistenceContextType)
   {
      this.persistenceContextType = persistenceContextType;
   }
}
