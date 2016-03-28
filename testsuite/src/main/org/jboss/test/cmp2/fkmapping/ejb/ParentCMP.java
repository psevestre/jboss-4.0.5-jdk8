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
package org.jboss.test.cmp2.fkmapping.ejb;

/**
 * CMP layer for Parent.
 */
public abstract class ParentCMP
   extends  ParentCMPBean
   implements javax.ejb.EntityBean
{

   public void ejbLoad() 
   {
      super.ejbLoad();
   }

   public void ejbStore() 
   {
      super.ejbStore();
   }

   public void ejbActivate() 
   {
      super.ejbActivate();
   }

   public void ejbPassivate() 
   {
      super.ejbPassivate(); 
   }

   public void setEntityContext(javax.ejb.EntityContext ctx) 
   {
      super.setEntityContext(ctx);
   }

   public void unsetEntityContext() 
   {
      super.unsetEntityContext();
   }

   public void ejbRemove() throws javax.ejb.RemoveException
   {
      super.ejbRemove();

   }

   public abstract Long getId() ;

   public abstract void setId( Long id ) ;

   public abstract String getFirstName() ;

   public abstract void setFirstName( String firstName ) ;

}
