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
package org.jboss.resource.security;

import java.security.acl.Group;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Map;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.management.ObjectName;
import javax.management.MBeanServer;

import org.jboss.security.SimplePrincipal;
import org.jboss.logging.Logger;

/** A login module for statically defining a data source username and password
 that uses a password that has been ecrypted by a JaasSecurityDomain. The
 base64 format of the data source password may be generated using the PBEUtils
 command:
 
 java -cp jbosssx.jar org.jboss.security.plugins.PBEUtils salt count
   domain-password data-source-password

 salt : the Salt attribute from the JaasSecurityDomain
 count : the IterationCount attribute from the JaasSecurityDomain
 domain-password : the plaintext password that maps to the KeyStorePass
   attribute from the JaasSecurityDomain
 data-source-password : the plaintext password for the data source that
   should be encrypted with the JaasSecurityDomain password

 for example:

 java -cp jbosssx.jar org.jboss.security.plugins.PBEUtils abcdefgh 13 master ''
 Encoded password: E5gtGMKcXPP

 A sample login-config.xml configuration entry would be:
 
 <application-policy name = "EncryptedHsqlDbRealm">
    <authentication>
       <login-module code = "org.jboss.resource.security.JaasSecurityDomainIdentityLoginModule"
       flag = "required">
          <module-option name = "userName">sa</module-option>
          <module-option name = "password">E5gtGMKcXPP</module-option>
          <module-option name = "managedConnectionFactoryName">jboss.jca:service=LocalTxCM,name=DefaultDS</module-option>
          <module-option name = "jaasSecurityDomain">jboss.security:service=JaasSecurityDomain,domain=ServerMasterPassword</module-option>
       </login-module>
    </authentication>
 </application-policy>


 @author Scott.Stark@jboss.org
 @author <a href="mailto:noel.rocher@jboss.org">Noel Rocher</a> 29, june 2004 username & userName issue
 @version $Revision: 57189 $
 */
public class JaasSecurityDomainIdentityLoginModule
   extends AbstractPasswordCredentialLoginModule
{
   private static final Logger log = Logger.getLogger(JaasSecurityDomainIdentityLoginModule.class);

   private String username;
   private String password;
   private ObjectName jaasSecurityDomain;

   public void initialize(Subject subject, CallbackHandler handler,
      Map sharedState, Map options)
   {
      super.initialize(subject, handler, sharedState, options);
      // NR : we keep this username for compatibility
      username = (String) options.get("username");
      if( username == null )
      {
      	// NR : try with userName
        username = (String) options.get("userName");      	
        if( username == null )
        {
         throw new IllegalArgumentException("The user name is a required option");
        }
     }

      password = (String) options.get("password");
      if( password == null )
      {
         throw new IllegalArgumentException("The password is a required option");
      }

      String name = (String) options.get("jaasSecurityDomain");
      if( name == null )
      {
         throw new IllegalArgumentException("The jaasSecurityDomain is a required option");
      }

      try
      {
         jaasSecurityDomain = new ObjectName(name);
      }
      catch(Exception e)
      {
         throw new IllegalArgumentException("Invalid jaasSecurityDomain: " + e.getMessage());
      }
   }

   public boolean login() throws LoginException
   {
      log.trace("login called");
      if( super.login() == true )
         return true;

      super.loginOk = true;
      return true;
   }

   public boolean commit() throws LoginException
   {
      Principal principal = new SimplePrincipal(username);
      SubjectActions.addPrincipals(subject, principal);
      sharedState.put("javax.security.auth.login.name", username);
      // Decode the encrypted password
      try
      {
         char[] decodedPassword = DecodeAction.decode(password,
            jaasSecurityDomain, getServer());
         PasswordCredential cred = new PasswordCredential(username, decodedPassword);
         cred.setManagedConnectionFactory(getMcf());
         SubjectActions.addCredentials(subject, cred);
      }
      catch(Exception e)
      {
         log.debug("Failed to decode password", e);
         throw new LoginException("Failed to decode password: " + e.getMessage());
      }
      return true;
   }

   public boolean abort()
   {
      username = null;
      password = null;
      return true;
   }

   protected Principal getIdentity()
   {
      log.trace("getIdentity called, username=" + username);
      Principal principal = new SimplePrincipal(username);
      return principal;
   }

   protected Group[] getRoleSets() throws LoginException
   {
      Group[] empty = new Group[0];
      return empty;
   }

   private static class DecodeAction implements PrivilegedExceptionAction
   {
      String password;
      ObjectName jaasSecurityDomain;
      MBeanServer server;
      
      DecodeAction(String password, ObjectName jaasSecurityDomain,
         MBeanServer server)
      {
         this.password = password;
         this.jaasSecurityDomain = jaasSecurityDomain;
         this.server = server;
      }

      /**
       * 
       * @return
       * @throws Exception
       */ 
      public Object run() throws Exception
      {
         // Invoke the jaasSecurityDomain.decodeb64 op
         Object[] args = {password};
         String[] sig = {String.class.getName()};
         byte[] secret = (byte[]) server.invoke(jaasSecurityDomain,
            "decode64", args, sig);
         // Convert to UTF-8 base char array
         String secretPassword = new String(secret, "UTF-8");
         return secretPassword.toCharArray();
      }
      static char[] decode(String password, ObjectName jaasSecurityDomain,
         MBeanServer server)
         throws Exception
      {
         DecodeAction action = new DecodeAction(password, jaasSecurityDomain, server);
         try
         {
            char[] decode = (char[]) AccessController.doPrivileged(action);
            return decode;
         }
         catch(PrivilegedActionException e)
         {
            throw e.getException();
         }
      }
   }
}
