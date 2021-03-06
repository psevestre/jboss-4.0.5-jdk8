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
package org.jboss.test.cts.test;


import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;

import EDU.oswego.cs.dl.util.concurrent.CountDown;
import junit.framework.Test;
import org.jboss.test.JBossTestCase;

/** Basic conformance tests for MDBs
 *
 *  @author Scott.Stark@jboss.org
 *  @version $Revision: 57211 $
 */
public class MDBUnitTestCase
   extends JBossTestCase
{
   static final int MAX_SIZE = 20;
   static String QUEUE_FACTORY = "ConnectionFactory";

   public MDBUnitTestCase (String name)
   {
      super(name);
   }

   public void testPooling() throws Exception
   {
      CountDown done = new CountDown(MAX_SIZE);
      InitialContext ctx = new InitialContext();
      QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup(QUEUE_FACTORY);
      QueueConnection queConn = factory.createQueueConnection();
      QueueSession session = queConn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
      Queue queueA = (Queue) ctx.lookup("queue/A");
      Queue queueB = (Queue) ctx.lookup("queue/B");
      queConn.start();
      MDBInvoker[] threads = new MDBInvoker[MAX_SIZE];
      for(int n = 0; n < MAX_SIZE; n ++)
      {
         MDBInvoker t = new MDBInvoker(session, queueA, queueB, n, done, getLog());
         threads[n] = t;
         t.start();
      }
      assertTrue("Acquired done", done.attempt(1500 * MAX_SIZE));
      session.close();
      queConn.close();

      for(int n = 0; n < MAX_SIZE; n ++)
      {
         MDBInvoker t = threads[n];
         if( t.runEx != null )
         {
            t.runEx.printStackTrace();
            fail("Inovker.runEx != null, msg="+t.runEx.getMessage());
         }
      }
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(MDBUnitTestCase.class, "cts.jar");
   }

}
