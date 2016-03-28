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
package org.jboss.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jboss.logging.Logger;
import org.jboss.security.SecurityConstants;

/** A simple implementation of LoginModule for use by JBoss clients for
 the establishment of the caller identity and credentials. This simply sets
 the SecurityAssociation principal to the value of the NameCallback
 filled in by the CallbackHandler, and the SecurityAssociation credential
 to the value of the PasswordCallback filled in by the CallbackHandler.
 
 It has the following options:
 <ul>
 <li>multi-threaded=[true|false]
 When the multi-threaded option is set to true, the SecurityAssociation.setServer()
 so that each login thread has its own principal and credential storage.
 <li>restore-login-identity=[true|false]
 When restore-login-identity is true, the SecurityAssociation principal
 and credential seen on entry to the login() method are saved and restored
 on either abort or logout. When false (the default), the abort and logout
 simply clears the SecurityAssociation. A restore-login-identity of true is
 needed if one need to change identities and then restore the original
 caller identity.
 <li>password-stacking=tryFirstPass|useFirstPass
 When password-stacking option is set, this module first looks for a shared
 username and password using "javax.security.auth.login.name" and
 "javax.security.auth.login.password" respectively. This allows a module configured
 prior to this one to establish a valid username and password that should be passed
 to JBoss.
 </ul>
 
 @author <a href="mailto:on@ibis.odessa.ua">Oleg Nitz</a>
 @author Scott.Stark@jboss.org
 */
public class ClientLoginModule implements LoginModule
{
   private static Logger log = Logger.getLogger(ClientLoginModule.class);
   private Subject subject;
   private CallbackHandler callbackHandler;
   /** The principal set during login() */
   private Principal loginPrincipal;
   /** The credential set during login() */
   private Object loginCredential;
   /** Shared state between login modules */
   private Map sharedState;
   /** Flag indicating if the shared password should be used */
   private boolean useFirstPass;
   /** Flag indicating if the SecurityAssociation existing at login should
    be restored on logout.
    */
   private boolean restoreLoginIdentity;
   private boolean trace;

   /** Initialize this LoginModule. This checks for the options:
    multi-threaded
    restore-login-identity
    password-stacking
    */
   public void initialize(Subject subject, CallbackHandler callbackHandler,
                          Map sharedState, Map options)
   {
      this.trace = log.isTraceEnabled();
      this.subject = subject;
      this.callbackHandler = callbackHandler;
      this.sharedState = sharedState;

      //log securityDomain, if set.
      if(trace)
	 log.trace("Security domain: " + 
		   (String)options.get(SecurityConstants.SECURITY_DOMAIN_OPTION));

      // Check for multi-threaded option
      String flag = (String) options.get("multi-threaded");
      if (Boolean.valueOf(flag).booleanValue() == true)
      {
         /* Turn on the server mode which uses thread local storage for
            the principal information.
         */
         if(trace)
            log.trace("Enabling multi-threaded mode");
         SecurityAssociationActions.setServer();
      }

      flag = (String) options.get("restore-login-identity");
      restoreLoginIdentity = Boolean.valueOf(flag).booleanValue();
      if(trace)
	 log.trace("Enabling restore-login-identity mode");

      /* Check for password sharing options. Any non-null value for
          password_stacking sets useFirstPass as this module has no way to
          validate any shared password.
       */
      String passwordStacking = (String) options.get("password-stacking");
      useFirstPass = passwordStacking != null;
      if(trace && useFirstPass)
	 log.trace("Enabling useFirstPass mode");
   }

   /**
    * Method to authenticate a Subject (phase 1).
    */
   public boolean login() throws LoginException
   {
      if( trace )
         log.trace("Begin login");
      // If useFirstPass is true, look for the shared password
      if (useFirstPass == true)
      {
         try
         {
            Object name = sharedState.get("javax.security.auth.login.name");
            if ((name instanceof Principal) == false)
            {
               String username = name != null ? name.toString() : "";
               loginPrincipal = new SimplePrincipal(username);
            } else
            {
               loginPrincipal = (Principal) name;
            }
            loginCredential = sharedState.get("javax.security.auth.login.password");
            return true;
         }
         catch (Exception e)
         {   // Dump the exception and continue
            log.debug("Failed to obtain shared state", e);
         }
      }

      /* There is no password sharing or we are the first login module. Get
          the username and password from the callback hander.
       */
      if (callbackHandler == null)
         throw new LoginException("Error: no CallbackHandler available " +
            "to garner authentication information from the user");

      PasswordCallback pc = new PasswordCallback("Password: ", false);
      NameCallback nc = new NameCallback("User name: ", "guest");
      Callback[] callbacks = {nc, pc};
      try
      {
         String username;
         char[] password = null;
         char[] tmpPassword;

         callbackHandler.handle(callbacks);
         username = nc.getName();
         loginPrincipal = new SimplePrincipal(username);
         tmpPassword = pc.getPassword();
         if (tmpPassword != null)
         {
            password = new char[tmpPassword.length];
            System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
            pc.clearPassword();
         }
         loginCredential = password;
         if( trace )
         {
            String credType = "null";
            if( loginCredential != null )
               credType = loginCredential.getClass().getName();
            log.trace("Obtained login: "+loginPrincipal
               +", credential.class: " + credType);
         }
      }
      catch (IOException ioe)
      {
         LoginException ex = new LoginException(ioe.toString());
         ex.initCause(ioe);
         throw ex;
      }
      catch (UnsupportedCallbackException uce)
      {
         LoginException ex = new LoginException("Error: " + uce.getCallback().toString() +
            ", not able to use this callback for username/password");
         ex.initCause(uce);
         throw ex;
      }
      if( trace )
         log.trace("End login");
      return true;
   }

   /**
    * Method to commit the authentication process (phase 2).
    */
   public boolean commit() throws LoginException
   {
      if( trace )
         log.trace("commit, subject="+subject);
      // Set the login principal and credential and subject
      SecurityAssociationActions.setPrincipalInfo(loginPrincipal, loginCredential, subject);

      // Add the login principal to the subject if is not there
      Set principals = subject.getPrincipals();
      if (principals.contains(loginPrincipal) == false)
         principals.add(loginPrincipal);
      return true;
   }

   /**
    * Method to abort the authentication process (phase 2).
    */
   public boolean abort() throws LoginException
   {
      if( trace )
         log.trace("abort");
      if( restoreLoginIdentity == true )
      {
         SecurityAssociationActions.popPrincipalInfo();
      }
      else
      {
         // Clear the entire security association stack
         SecurityAssociationActions.clear();         
      }

      return true;
   }

   public boolean logout() throws LoginException
   {
      if( trace )
         log.trace("logout");
      if( restoreLoginIdentity == true )
      {
         SecurityAssociationActions.popPrincipalInfo();
      }
      else
      {
         // Clear the entire security association stack
         SecurityAssociationActions.clear();         
      }
      Set principals = subject.getPrincipals();
      principals.remove(loginPrincipal);
      return true;
   }
}
