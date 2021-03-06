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
package org.jboss.ejb3.session;

import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.aop.Advisor;
import org.jboss.ejb3.Container;
import org.jboss.ejb3.EJBContainer;
import org.jboss.ejb3.ProxyFactory;
import org.jboss.logging.Logger;
import org.jboss.ejb3.proxy.EJBMetaDataImpl;
import org.jboss.ejb3.proxy.handle.HomeHandleImpl;

/**
 * Comment
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version $Revision: 57207 $
 */
public abstract class BaseSessionProxyFactory implements ProxyFactory
{
   private static final Logger log = Logger.getLogger(BaseSessionProxyFactory.class);
   
   protected Container container;
   protected Advisor advisor;
  
   public Object createHomeProxy()
   {
      throw new RuntimeException("NYI");
   }
   
   public void setContainer(Container container)
   {
      this.container = container;
      this.advisor = (Advisor) container;
   }
   
   protected void setEjb21Objects(BaseSessionRemoteProxy proxy)
   {
      proxy.setHandle(getHandle());
      proxy.setHomeHandle(getHomeHandle());
      proxy.setEjbMetaData(getEjbMetaData());
   }
   
   abstract protected Handle getHandle();
   
   protected HomeHandle getHomeHandle()
   {
      EJBContainer ejbContainer = (EJBContainer)container;
      
      HomeHandleImpl homeHandle = null;
      
      RemoteBinding remoteBindingAnnotation = (RemoteBinding)ejbContainer.resolveAnnotation(RemoteBinding.class);
      if (remoteBindingAnnotation != null)
         homeHandle = new HomeHandleImpl(remoteBindingAnnotation.jndiBinding() + "Home");
      
      return homeHandle;
   }
   
   protected EJBMetaData getEjbMetaData()
   {
      Class remote = null;
      Class home = null;
      Class pkClass = Object.class;
      HomeHandleImpl homeHandle = null;
      
      EJBContainer ejbContainer = (EJBContainer)container;
      
      Remote remoteAnnotation = (Remote)ejbContainer.resolveAnnotation(Remote.class);
      if (remoteAnnotation != null)
         remote = remoteAnnotation.value()[0];
      RemoteHome homeAnnotation = (RemoteHome)ejbContainer.resolveAnnotation(RemoteHome.class);
      if (homeAnnotation != null)
         home = homeAnnotation.value();
      RemoteBinding remoteBindingAnnotation = (RemoteBinding)ejbContainer.resolveAnnotation(RemoteBinding.class);
      if (remoteBindingAnnotation != null)
         homeHandle = new HomeHandleImpl(remoteBindingAnnotation.jndiBinding());
      
      EJBMetaDataImpl metadata = new EJBMetaDataImpl(remote, home, pkClass, true, false, homeHandle);
      
      return metadata;
   }

}
