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
package org.jboss.test.kernel.deployment.jboss.beans.dependspojo;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.naming.Util;

/**
 * Simple service to examine if dependencies are satisfied
 *
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision: 57211 $
 */
public class SimpleService implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   public static final String BASE_JNDI_NAME = "test/kernel/deployment/depends/pojo";
   
   public static final String CONSTRUCTED = "CONSTRUCTED";
   public static final String CREATED     = "CREATED";
   public static final String STARTED     = "STARTED";
   public static final String STOPPED     = "STOPPED";
   public static final String DESTROYED   = "DESTROYED";
   public static final String FAILED      = "FAILED";
   
   // Protected -----------------------------------------------------
   
   protected transient InitialContext ctx;
   
   protected String name;
   protected String[] depends;
   protected String state = CONSTRUCTED;
   
   // Constructor ---------------------------------------------------
   
   public SimpleService(String name) throws NamingException
   {
      // cache it
      ctx = new InitialContext();
      
      this.name = name;
  
      log("CTOR");
   }
   
   // Accessors -----------------------------------------------------

   public String getName()
   {
      return name;
   }
      
   public String[] getDepends()
   {
      return depends;
   }
   
   public String getState()
   {
      return state;
   }
   
   public void setDepends(String[] depends) throws NamingException
   {
      log("setDepends(" + depends + ")");
      this.depends = depends;
      
      if (depends == null)
      {
         Util.unbind(ctx, getJndiName(name));
         log("Destroyed binding: " + name);         
      }
      else
      {
         Util.rebind(ctx, getJndiName(name), this);
         log("Created binding: " + name);         
      }      
   }
   
   // Lifecycle -----------------------------------------------------
   
   public void create() throws Exception
   {
      log("create()");
      state = CREATED;
      checkDependencies(CREATED);
   }
   
   public void start() throws Exception
   {
      log("start()");
      state = STARTED;      
      checkDependencies(STARTED);
   }
   
   public void stop() throws Exception
   {
      log("stop()");
      state = STOPPED;
      checkDependencies(STARTED);
   }
   
   public void destroy() throws Exception
   {
      log("destroy()");
      state = DESTROYED;      
      checkDependencies(STOPPED);
   }
   
   // protected -----------------------------------------------------
   
   protected void log(Object message)
   {
      System.out.println(getName() + " - " + message);
   }
   
   protected String getJndiName(String name)
   {
      return BASE_JNDI_NAME + '/' + name;
   }
   
   protected void checkDependencies(String desiredState)
   {
      if (depends != null)
      {
         for (int i = 0; i < depends.length; i++)
         {
            try
            {
               SimpleService other = (SimpleService)ctx.lookup(getJndiName(depends[i]));
               if (other.getState().equals(desiredState))
               {
                  log(depends[i] + " is " + other.getState());
               }
               else
               {
                  throw new IllegalStateException(depends[i] + " not " + desiredState);
               }
            }
            catch (NamingException e)
            {
               throw new RuntimeException(depends[i] + " not bound\n");
            }
         }
      }
   }
   
   // Overrides -----------------------------------------------------
   
   public String toString()
   {
      StringBuffer sbuf = new StringBuffer();
      sbuf
      .append(getClass().getName())
      .append("[ name=").append(name)
      .append(", state=").append(state);
      if (depends != null)
      {
         sbuf.append(", depends=[");
         for (int i = 0; i < depends.length; i++)
            sbuf.append(' ').append(depends[i]);
         sbuf.append(" ]");
      }
      sbuf.append(" ]");
      
      return sbuf.toString();
   }
}
