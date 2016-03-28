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
package org.jboss.test.foedeployer.ejb.m2mb;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;

import org.apache.log4j.Category;

/**
 * Models a developer.
 *
 * @ejb.bean
 *    name="Developer"
 *    generate="true"
 *    view-type="local"
 *    type="CMP"
 *    local-jndi-name="DeveloperEJB.DeveloperHome"
 *    reentrant="False"
 *    cmp-version="2.x"
 *    primkey-field="name"
 *
 * @ejb.pk
 *    class="java.lang.String"
 *    generate="false"
 *
 * @ejb.transaction type="Required"
 *
 * @@ejb:finder signature="Collection findAll()"
 *
 * @@ejb.persistence table-name="developer"
 * @weblogic:table-name developer
 *
 * @jboss.create-table "${jboss.create.table}"
 * @jboss.remove-table "${jboss.remove.table}"
 *
 * @author <a href="mailto:loubyansky@hotmail.com">Alex Loubyansky</a>
 */
public abstract class DeveloperBean
   implements EntityBean
{
   // Constants -----------------------------------------------------
   static Category log = Category.getInstance( DeveloperBean.class );

   // Attributes ----------------------------------------------------
   private EntityContext ctx;

   // CMP
   
   /**
    * Developer's name: primary key field
    *
    * @ejb.pk-field
    * @ejb.persistent-field
    * @ejb.interface-method
    *
    * xdoclet needs update
    * @@ejb.persistence
    *    column-name="name"
    *    jdbc-type="VARCHAR"
    *    sql-type="VARCHAR(50)"
    *
    * @weblogic:dbms-column name
    */
   public abstract String getName();
   public abstract void setName(String name);

   
   // CMR

   /**
    * Projects: m2m bidirectional CMR
    *
    * @ejb.interface-method
    *
    * @ejb.relation
    *    name="Developer-Project"
    *    role-name="Developer-Has-Projects"
    *
    * @jboss.relation-table
    *    table-name="developer_project"
    *    create-table="${jboss.create.table}"
    *    remove-table="${jboss.remove.table}"
    * @jboss.relation
    *    fk-contraint="false"
    *    related-pk-field="name"
    *    fk-column="project"
    *
    * @weblogic.relation join-table-name="developer_project"
    * @weblogic.column-map
    *    foreign-key-column="developer"
    *    key-column="name"
    */
   public abstract Collection getProjects();
   /**
    * @ejb.interface-method
    */
   public abstract void setProjects(Collection projects);

   // EntityBean Implementation -------------------------------------
   /**
    * @ejb.create-method
    */
   public String ejbCreate( String name )
      throws CreateException
   {
      setName(name);
      return null; // See 9.4.2 of the EJB 1.1 specification
   }

   public void ejbPostCreate( String name ) { }

   public void ejbRemove()
      throws RemoveException
   {
      log.debug("removed: " + ctx.getPrimaryKey() );
   }
   
   public void setEntityContext(EntityContext ctx)
   {
      this.ctx = ctx;
   }
   
   public void unsetEntityContext()
   {
      ctx = null;
   }
   
   public void ejbActivate() { }
   public void ejbPassivate() { }
   public void ejbLoad() { }   
   public void ejbStore() { }
}
