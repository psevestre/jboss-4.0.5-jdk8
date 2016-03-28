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
package org.jboss.ejb3.test.stateful.unit;

import javax.ejb.EJBNoSuchObjectException;
import javax.naming.InitialContext;

import org.jboss.ejb3.test.stateful.ConcurrentStateful;
import org.jboss.ejb3.test.stateful.SmallCacheStateful;
import org.jboss.ejb3.test.stateful.Stateful;
import org.jboss.ejb3.test.stateful.StatefulInvoker;
import org.jboss.ejb3.test.stateful.StatefulLocal;
import org.jboss.ejb3.test.stateful.StatefulTx;
import org.jboss.ejb3.test.stateful.ProxyFactoryInterface;
import org.jboss.ejb3.test.stateful.RemoteBindingInterceptor;
import org.jboss.ejb3.test.stateful.State;
import org.jboss.ejb3.test.stateful.StatefulHome;
import org.jboss.ejb3.test.stateful.ExtendedState;
import org.jboss.ejb3.test.stateful.Tester;
import org.jboss.logging.Logger;
import org.jboss.test.JBossTestCase;
import junit.framework.Test;

import org.jboss.security.SimplePrincipal;
import org.jboss.security.SecurityAssociation;

/**
 * Sample client for the jboss container.
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Id: RemoteUnitTestCase.java 57207 2006-09-26 12:06:13Z dimitris@jboss.org $
 */

public class RemoteUnitTestCase
extends JBossTestCase
{
   private static final Logger log = Logger.getLogger(RemoteUnitTestCase.class);

   static boolean deployed = false;
   static int test = 0;

   public RemoteUnitTestCase(String name)
   {

      super(name);

   }
   
   private class ConcurrentInvocation extends Thread
   {
      SmallCacheStateful small = null;
      public Exception ex;
      private int id;
      
      public ConcurrentInvocation(int id)
      {
         this.id = id;
         try
         {
            small = (SmallCacheStateful)getInitialContext().lookup("SmallCacheStatefulBean/remote");
            small.setId(id);
         }
         catch (Exception e)
         {
         }
      }
      
      public void run()
      {
         for (int i = 0; i < 5; i++)
         {
            try
            {
               assertEquals(small.doit(id),i);
            }
            catch (Exception e)
            {
               ex = e;
            }
            catch (Error er)
            {
               ex = new RuntimeException("Failed assert: " + id, er);
            }
         }
      }
   }
   
   public void testSmallCache() throws Exception
   {
      ConcurrentInvocation[] threads = new ConcurrentInvocation[5];
      for (int i = 0; i < 5; i++) threads[i] = new ConcurrentInvocation(i);
      for (int i = 0; i < 5; i++) threads[i].start();
      for (int i = 0; i < 5; i++) threads[i].join();
      for (int i = 0; i < 5; i++)
      {
         if (threads[i].ex != null)
         {
            throw new Exception(threads[i].ex);
         }
      }
   }
   
   

   public void testStatefulSynchronization() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      Tester test = (Tester) getInitialContext().lookup("TesterBean/remote");
      test.testSessionSynchronization();

   }
   
   public void testEJBObject() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      StatefulHome home = (StatefulHome)getInitialContext().lookup("StatefulHome");
      assertNotNull(home);
      javax.ejb.EJBObject stateful = (javax.ejb.EJBObject)home.create(); 
      assertNotNull(stateful);
      stateful = (javax.ejb.EJBObject)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
   }
   
   public void testStatefulTx() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      StatefulTx stateful = (StatefulTx)getInitialContext().lookup("StatefulTx");
      assertNotNull(stateful);
      
      boolean transacted = stateful.isLocalTransacted();
      assertTrue(transacted);
      transacted = stateful.isGlobalTransacted();
      assertFalse(transacted);
      
      try
      {
         stateful.testTxRollback();
         fail("should have caught exception");
      }
      catch (javax.ejb.EJBException e)
      {
      }
   }
   
   public void testTemplateInterfaceTx() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      StatefulTx stateful = (StatefulTx)getInitialContext().lookup("StatefulTx");
      assertNotNull(stateful);
      
      try
      {
         stateful.testMandatoryTx(new State("test"));
         fail("should have caught exception");
      }
      catch (javax.ejb.EJBTransactionRequiredException e)
      {
      }
   }
   
   public void testLocalSFSB() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      try
      {
         StatefulLocal stateful = (StatefulLocal)getInitialContext().lookup("StatefulBean/local");
         assertNotNull(stateful);
         
         stateful.getState();
         fail("EJBException should be thrown");
      }
      catch (Exception e)
      {
         if (e.getCause() == null || !(e.getCause() instanceof javax.ejb.EJBException))
            fail("EJBException should be thrown as cause");
      }
   }
   
   public void testNotSerialableSFSB() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
      stateful.setState("state");
      String state = stateful.getState();
      assertEquals("state", state);
   }
   
   public void testSFSBInit() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      StatefulHome home = (StatefulHome)getInitialContext().lookup("StatefulHome");
      assertNotNull(home);
      ExtendedState state = new ExtendedState("init");
      Stateful stateful = home.create(state);
      assertNotNull(stateful);
      String s = stateful.getState();
      assertEquals("Extended_init", s);
   }
   
   public void testStackTrace() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
      
      try
      {
         stateful.testThrownException();
         this.fail("no exception caught");
      } 
      catch (Exception e)
      {
         StackTraceElement[] stackTrace = e.getStackTrace();
         assertTrue(stackTrace[stackTrace.length - 1].getClassName().startsWith("org.apache.tools.ant.taskdefs.optional.junit.JUnitTestRunner"));
      }
   }
   
   public void testExceptionCase() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
      
      try
      {
         stateful.testExceptionCause();
         this.fail("no exception caught");
      } 
      catch (Exception e)
      {
         Throwable cause = e.getCause();
         assertNotNull(cause);
         assertEquals(NullPointerException.class.getName(), cause.getClass().getName());
      }
   }
   
   public void testRemoteBindingProxyFactory() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      ProxyFactoryInterface stateful = (ProxyFactoryInterface)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
   }
   
   public void testRemoteBindingInterceptorStack() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
      assertFalse(stateful.interceptorAccessed());
      assertTrue(RemoteBindingInterceptor.accessed);
   }

   public void testPassivation() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      System.out.println("testPassivation");
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
      stateful.setState("state");
      assertEquals("state", stateful.getState());
      stateful.testSerializedState("state");
      stateful.clearPassivated();
      assertEquals(null, stateful.getInterceptorState());
      stateful.setInterceptorState("hello world");
      assertFalse(stateful.testSessionContext());
      Thread.sleep(5 * 1000);
      assertTrue(stateful.wasPassivated());
      
      assertEquals("state", stateful.getState());
      assertEquals("hello world", stateful.getInterceptorState());

      Stateful another = (Stateful)getInitialContext().lookup("Stateful");
      assertEquals(null, another.getInterceptorState());
      another.setInterceptorState("foo");
      assertEquals("foo", another.getInterceptorState());
      assertEquals("hello world", stateful.getInterceptorState());
      
      assertFalse(stateful.testSessionContext());
      
      stateful.testResources();
   }
   
   public void testRemove() throws Exception
   {
      SecurityAssociation.setPrincipal(new SimplePrincipal("somebody"));
      SecurityAssociation.setCredential("password".toCharArray());
      
      System.out.println("testPassivation");
      Stateful stateful = (Stateful)getInitialContext().lookup("Stateful");
      assertNotNull(stateful);
 //     stateful.setState("state");
      
      stateful.removeBean();
      
      try
      {
         stateful.getState();
         fail("Bean should have been removed");
      } catch (EJBNoSuchObjectException e)
      {
         
      }
   }

   public void testRemoveWithRollback() throws Exception
   {
      Tester test = (Tester) getInitialContext().lookup("TesterBean/remote");
      test.testRollback1();
      test.testRollback2();
   }
   
   public void testConcurrentAccess() throws Exception
   {
      ConcurrentStateful stateful = (ConcurrentStateful) new InitialContext().lookup("ConcurrentStateful");
      stateful.getState();
      
      StatefulInvoker[] invokers = new StatefulInvoker[2];
      for (int i = 0; i < 2 ; ++i)
      {
         invokers[i] = new StatefulInvoker(stateful);
      }
      
      for (StatefulInvoker invoker: invokers)
      {
         invoker.start();
      }
      
      Thread.sleep(10000);
      
      for (StatefulInvoker invoker: invokers)
      {
         if (invoker.getException() != null)
            throw invoker.getException();
      }
      
      stateful = (ConcurrentStateful) new InitialContext().lookup("Stateful");
      
      invokers = new StatefulInvoker[2];
      for (int i = 0; i < 2 ; ++i)
      {
         invokers[i] = new StatefulInvoker(stateful);
      }
      
      for (StatefulInvoker invoker: invokers)
      {
         invoker.start();
      }
      
      Thread.sleep(10000);
      
      boolean wasConcurrentException = false;
      for (StatefulInvoker invoker: invokers)
      {
         if (invoker.getException() != null)
            wasConcurrentException = true;
      }
      
      assertTrue(wasConcurrentException);
   }
   
   public void testOverrideConcurrentAccess() throws Exception
   {
      ConcurrentStateful stateful = (ConcurrentStateful) new InitialContext().lookup("OverrideConcurrentStateful");
      stateful.getState();
      
      StatefulInvoker[] invokers = new StatefulInvoker[2];
      for (int i = 0; i < 2 ; ++i)
      {
         invokers[i] = new StatefulInvoker(stateful);
      }
      
      for (StatefulInvoker invoker: invokers)
      {
         invoker.start();
      }
      
      Thread.sleep(5000);
      
      boolean wasConcurrentException = false;
      for (StatefulInvoker invoker: invokers)
      {
         if (invoker.getException() != null)
            wasConcurrentException = true;
      }

      assertTrue(wasConcurrentException);
   }

   public static Test suite() throws Exception
   {
      return getDeploySetup(RemoteUnitTestCase.class, "stateful-test.jar");
   }

}
