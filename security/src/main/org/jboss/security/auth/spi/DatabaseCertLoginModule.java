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
package org.jboss.security.auth.spi;

import java.security.acl.Group;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

/**
 * A Certificate Login Module that gets its role information from a database.
 * 
 * This module is the functional equivelant of the 
 * {@link org.jboss.security.auth.spi.DatabaseServerLoginModule} minus the
 * usersQuery.
 * @see org.jboss.security.auth.spi.DatabaseServerLoginModule
 *
 * @author <a href="mailto:jasone@greenrivercomputing.com">Jason Essington</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
public class DatabaseCertLoginModule extends BaseCertLoginModule
{
   /** The JNDI name of the DataSource to use */
   private String dsJndiName;
   /** The sql query to obtain the user roles */
   private String rolesQuery = "select Role, RoleGroup from Roles where PrincipalID=?";
   /** Whether to suspend resume transactions during database operations */
   protected boolean suspendResume = true;
   
   /**
    * @param options -
    * dsJndiName: The name of the DataSource of the database containing the
    *    Principals, Roles tables
    * rolesQuery: The prepared statement query, equivalent to:
    *    "select Role, RoleGroup from Roles where PrincipalID=?"
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
      Map sharedState, Map options)
   {
      super.initialize(subject, callbackHandler, sharedState, options);
      dsJndiName = (String) options.get("dsJndiName");
      if( dsJndiName == null )
         dsJndiName = "java:/DefaultDS";
      
      Object tmp = options.get("rolesQuery");
      if( tmp != null )
         rolesQuery = tmp.toString();

      tmp = options.get("suspendResume");
      if( tmp != null )
         suspendResume = Boolean.valueOf(tmp.toString()).booleanValue();

      if (log.isTraceEnabled())
      {
         log.trace("DatabaseServerLoginModule, dsJndiName="+dsJndiName);
         log.trace("rolesQuery="+rolesQuery);
         log.trace("suspendResume="+suspendResume);
      }
   }

   /**
    * @see org.jboss.security.auth.spi.DatabaseServerLoginModule#getRoleSets
    */
   protected Group[] getRoleSets() throws LoginException
   {
      String username = getUsername();
      Group[] roleSets = Util.getRoleSets(username, dsJndiName, rolesQuery, this, suspendResume);
      return roleSets;
   }
   
}
