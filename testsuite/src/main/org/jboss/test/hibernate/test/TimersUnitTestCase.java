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
package org.jboss.test.hibernate.test;

import org.jboss.test.JBossTestCase;
import org.jboss.test.hibernate.timers.interfaces.ITimersHome;
import org.jboss.test.hibernate.timers.interfaces.ITimers;
import org.jboss.test.hibernate.timers.interfaces.Key;
import org.jboss.test.hibernate.timers.interfaces.Info;
import org.jboss.test.hibernate.timers.Timers;
import org.jboss.test.hibernate.timers.TimersID;

import java.util.List;
import java.util.Iterator;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import javax.naming.InitialContext;

import junit.framework.Test;

/**
 Test of the ejb timers table mapping

 @author Scott.Stark@jboss.org
 @version $Revision: 57211 $
 */
public class TimersUnitTestCase extends JBossTestCase
{
   public TimersUnitTestCase(String name) throws Exception
   {
      super(name);
   }

   /**
    Setup the test suite.
    */
   public static Test suite() throws Exception
   {
      return getDeploySetup(TimersUnitTestCase.class, "hib-timers.jar");
   }

   public void testCurrentSession() throws Throwable
   {
      InitialContext ctx = super.getInitialContext();
      ITimersHome home = (ITimersHome) ctx.lookup("hib-timers/ITimersHome");
      ITimers bean = null;

      try
      {
         bean = home.create();

         int initialCount = bean.listTimers().size();

         TimersID id = new TimersID("testCurrentSession", "*:ejb=None");
         Timers timer = new Timers(id);
         Date now = new Date();
         Long interval = new Long(5*1000);
         Key key = new Key("key2", 123456789);
         Info info = new Info(System.getProperties());
         timer.setInitialDate(now);
         timer.setInstancePK(serialize(key));
         timer.setTimerInterval(interval);
         timer.setInfo(serialize(info));

         bean.persist(timer);
         log.info("Timers created with id = " + id);

         List timers = bean.listTimers();
         assertNotNull(timers);
         assertEquals("Incorrect result size", initialCount + 1, timers.size());

         Timers found = null;
         Iterator itr = timers.iterator();
         while (itr.hasNext())
         {
            Timers t = (Timers) itr.next();
            if (id.equals(t.getId()))
            {
               found = t;
            }
         }
         assertNotNull("Saved timer found in list", found);
         Date d = found.getInitialDate();
         long t0 = (now.getTime() / 1000) * 1000;
         long t1 = (d.getTime() / 1000) * 1000;
         assertTrue("Timer.InitialDate("+t1+") == now("+t0+")", t0 == t1);
         assertTrue("Timer.Id == id", found.getId().equals(id));
         assertTrue("Timer.TimerInterval == interval", found.getTimerInterval().equals(interval));
         Object tmp = unserialize(found.getInstancePK());
         assertTrue("Timer.InstancePK == key", tmp.equals(key));
         tmp = unserialize(found.getInfo());
         log.info("Info: "+tmp);
         assertTrue("Timer.Info == info", tmp.equals(info));
      }
      finally
      {
         if (bean != null)
         {
            try
            {
               bean.remove();
            }
            catch (Throwable t)
            {
               // ignore
            }
         }
      }
   }

   private byte[] serialize(Serializable data) throws IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(data);
      oos.close();
      return baos.toByteArray();
   }
   private Object unserialize(byte[] data) throws IOException, ClassNotFoundException
   {
      ByteArrayInputStream bais = new ByteArrayInputStream(data);
      ObjectInputStream ois = new ObjectInputStream(bais);
      Object obj = ois.readObject();
      return obj;
   }
}