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
package org.jboss.test.hello.ejb;

import java.io.Serializable;
import javax.ejb.EJBException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.FinderException;
import javax.naming.InitialContext;

import org.jboss.test.util.ejb.SessionSupport;
import org.jboss.test.hello.interfaces.Hello;
import org.jboss.test.hello.interfaces.HelloData;
import org.jboss.test.hello.interfaces.HelloException;
import org.jboss.test.hello.interfaces.LocalHelloLogHome;
import org.jboss.test.hello.interfaces.LocalHelloLog;
import org.jboss.test.hello.interfaces.NotSerializable;

/**
 *   @author Rickard Oberg
 *   @author Scott.Stark@jboss.org
 *   @version $Revision: 57211 $
 */
public class HelloBean
   extends SessionSupport
{
   public String hello(String name)
   {
      return "Hello "+name+"!";
   }

   public String loggedHello(String name)
   {
      long begin = System.currentTimeMillis();
      LocalHelloLog bean = null;
      LocalHelloLogHome home = null;
      try
      {
         InitialContext ctx = new InitialContext();
         home = (LocalHelloLogHome) ctx.lookup("java:comp/env/ejb/LocalHelloLogHome");
         bean = home.create(name);
         log.info("Created LocalHelloLog with key="+name);
         if( bean != null )
            bean.setStartTime(begin);
      }
      catch(DuplicateKeyException e)
      {
         try
         {
            bean = home.findByPrimaryKey(name);
            log.info("Found LocalHelloLog with key="+name);
            if( bean != null )
               bean.setStartTime(begin);
         }
         catch(FinderException fe)
         {
            log.debug("LocalHelloLog find failed", fe);
         }
      }
      catch(Exception e)
      {
         log.debug("LocalHelloLog create failed", e);
      }
      String reply = "Hello "+name+"!";
      long end = System.currentTimeMillis();
      if( bean != null )
         bean.setEndTime(end);

      return reply;
   }

   public String helloException(String name)
      throws HelloException
   {
      throw new HelloException("Catch me");
   }

   public Hello helloHello(Hello hello)
   {
      return hello;
   }

   public String howdy(HelloData name)
   {
      return "Howdy "+name.getName()+"!";
   }

   public String sleepingHello(String name, long sleepTimeMS)
   {
      if( sleepTimeMS <= 0 )
         sleepTimeMS = 1;
      try
      {
         Thread.sleep(sleepTimeMS);
      }
      catch(InterruptedException ignore)
      {
      }
      return "Hello "+name+"!";
   }

   public Object getCNFEObject()
   {
      return new ServerData();
   }
   public void throwException()
   {
      throw new EJBException("Something went wrong");
   }

   public void setNotSerializable(NotSerializable ignored)
   {
      throw new RuntimeException("Should not get here");
   }
   public NotSerializable getNotSerializable()
   {
      return new NotSerializable();
   }

   static class ServerData implements Serializable
   {
      static final long serialVersionUID = 7590714293644921804L;
   }
}
