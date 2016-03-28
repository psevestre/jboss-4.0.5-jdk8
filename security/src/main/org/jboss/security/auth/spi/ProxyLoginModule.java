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

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/** A proxy LoginModule that loads a delegate LoginModule using
the current thread context class loader. The purpose of this
module is to work around the current JAAS class loader limitation
that requires LoginModules to be on the classpath. Some LoginModules
use core JBoss classes that would have to be moved into the jboss-jaas.jar
and packaging becomes a mess. Instead, these LoginModules are left
in the jbosssx.jar and the ProxyLoginModule is used to bootstrap
the non-classpath LoginModule.

@author Scott.Stark@jboss.org
@version $Revision: 57203 $
*/
public class ProxyLoginModule implements LoginModule
{
    private String moduleName;
    private LoginModule delegate;

    public ProxyLoginModule()
    {
    }

// --- Begin LoginModule interface methods
    /** Initialize this LoginModule. This method loads the LoginModule
        specified by the moduleName option using the current thread
        context class loader and then delegates the initialize call
        to it.

    @param options, include:
        moduleName: the classname of the module that this proxy module
        delegates all calls to.
     */
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
    {
        moduleName = (String) options.get("moduleName");
        if( moduleName == null )
        {
            System.out.println("Required moduleName option not given");
            return;
        }

        // Load the delegate module using the thread class loader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try
        {
            Class clazz = loader.loadClass(moduleName);
            delegate = (LoginModule) clazz.newInstance();
        }
        catch(Throwable t)
        {
            System.out.println("ProxyLoginModule failed to load: "+moduleName);
            t.printStackTrace();
            return;
        }

        delegate.initialize(subject, callbackHandler, sharedState, options);
    }

    /** Perform the login. If either the moduleName option was not
        specified or the module could not be loaded in initalize(),
        this method throws a LoginException.
    @exception LoginException, throw in the delegate login module failed.
    */
    public boolean login() throws LoginException
    {
        if( moduleName == null )
            throw new LoginException("Required moduleName option not given");
        if( delegate == null )
            throw new LoginException("Failed to load LoginModule: "+moduleName);

        return delegate.login();
    }

    public boolean commit() throws LoginException
    {
        boolean ok = false;
        if( delegate != null )
            ok = delegate.commit();
        return ok;
    }

    public boolean abort() throws LoginException
    {
        boolean ok = true;
        if( delegate != null )
            ok = delegate.abort();
        return ok;
    }

    public boolean logout() throws LoginException
    {
        boolean ok = true;
        if( delegate != null )
            ok = delegate.logout();
        return ok;
    }
// --- End LoginModule interface methods

}
