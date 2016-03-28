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
package org.jboss.test.txtimer.test;

import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.transaction.TransactionManager;

import org.jboss.tm.TransactionManagerLocator;

/**
 * @author Thomas.Diesler@jboss.org
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision: 57211 $
 */
public class TransactionalTimerTestCase extends TimerTestBase
{
   protected static TransactionManager txManager = TransactionManagerLocator.getInstance().locate();

   public TransactionalTimerTestCase(String name)
   {
      super(name);
   }

   public void testRollbackBeforeExpire() throws Exception
   {
      txManager.begin();

      TimedMockObject to = new TimedMockObject();
      TimerService service = createTimerService(to);

      service.createTimer(500, null);
      assertEquals("Expected one txtimer", 1, service.getTimers().size());
      txManager.rollback();
      sleep(1000);
      assertEquals("TimedObject called", 0, to.getCallCount());
      assertEquals("Expected no txtimer", 0, service.getTimers().size());
   }

   public void testRollbackAfterCreate() throws Exception
   {
      txManager.begin();

      TimedMockObject to = new TimedMockObject();
      TimerService service = createTimerService(to);
      service.createTimer(500, null);
      txManager.rollback();
      assertEquals("Expected no txtimer", 0, service.getTimers().size());
   }

   public void testRollbackCancel() throws Exception
   {
      TimedMockObject to = new TimedMockObject();
      TimerService service = createTimerService(to);
      Timer timer = service.createTimer(500, null);

      txManager.begin();
      timer.cancel();
      txManager.rollback();

      assertEquals("Expected one txtimer", 1, service.getTimers().size());
      sleep(1000);
      assertEquals("TimedObject not called", 1, to.getCallCount());
   }
   
   public void testExpireBeforeCommit() throws Exception
   {
      TimedMockObject to = new TimedMockObject();
      TimerService service = createTimerService(to);
      
      txManager.begin();
      Timer timer = service.createTimer(500, null);
      sleep(1000);
      assertEquals("TimedObject called", 0, to.getCallCount());
      txManager.commit();
      // On WinXp, immediately after commit the timer will be scheduled
      // with a time in the past, due to sleep(1000) and so it will immediately timeout
      // On Linux, it seems the timer thread has not run at this point, so sleep again!
      sleep(1000);
      assertEquals("TimedObject called", 1, to.getCallCount());
      assertEquals("Expected no txtimer", 0, service.getTimers().size());
   }
}
