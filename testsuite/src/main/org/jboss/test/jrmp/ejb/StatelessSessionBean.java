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
package org.jboss.test.jrmp.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.jboss.test.jrmp.interfaces.IString;

/** A simple session bean for testing access over custom RMI sockets.

@author Scott_Stark@displayscape.com
@version $Revision: 57211 $
*/
public class StatelessSessionBean implements SessionBean
{
    static org.apache.log4j.Category log =
       org.apache.log4j.Category.getInstance(StatelessSessionBean.class);
   
    private SessionContext sessionContext;

    public void ejbCreate() throws CreateException
    {
        log.debug("StatelessSessionBean.ejbCreate() called");
    }

    public void ejbActivate()
    {
        log.debug("StatelessSessionBean.ejbActivate() called");
    }

    public void ejbPassivate()
    {
        log.debug("StatelessSessionBean.ejbPassivate() called");
    }

    public void ejbRemove()
    {
        log.debug("StatelessSessionBean.ejbRemove() called");
    }

    public void setSessionContext(SessionContext context)
    {
        sessionContext = context;
    }

    public String echo(String arg)
    {
        log.debug("StatelessSessionBean.echo, arg="+arg);
        return arg;
    }

    public IString copy(String arg)
    {
        log.debug("StatelessSessionBean.copy, arg="+arg);
        return new AString(arg);
    }
}
