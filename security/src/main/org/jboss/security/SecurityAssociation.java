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

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import javax.security.auth.Subject;

import org.jboss.logging.Logger;

/**
 * The SecurityAssociation class maintains the security principal and
 * credentials. This can be done on either a singleton basis or a thread local
 * basis depending on the server property. When the server property has been set
 * to true, the security information is maintained in thread local storage. The
 * type of thread local storage depends on the org.jboss.security.SecurityAssociation.ThreadLocal
 * property. If this property is true, then the thread local storage object is
 * of type java.lang.ThreadLocal which results in the current thread's security
 * information NOT being propagated to child threads.
 *
 * When the property is false or does not exist, the thread local storage object
 * is of type java.lang.InheritableThreadLocal, and any threads spawned by the
 * current thread will inherit the security information of the current thread.
 * Subseqent changes to the current thread's security information are NOT
 * propagated to any previously spawned child threads.
 *
 * When the server property is false, security information is maintained in
 * class variables which makes the information available to all threads within
 * the current VM.
 * 
 * Note that this is not a public API class. Its an implementation detail that
 * is subject to change without notice.
 * 
 * @author Daniel O'Connor (docodan@nycap.rr.com)
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
public final class SecurityAssociation
{
   private static Logger log = Logger.getLogger(SecurityAssociation.class);
   /**
    * A flag indicating if trace level logging should be performed
    */
   private static boolean trace;
   /**
    * A flag indicating if security information is global or thread local
    */
   private static boolean server;
   /**
    * The SecurityAssociation principal used when the server flag is false
    */
   private static Principal principal;
   /**
    * The SecurityAssociation credential used when the server flag is false
    */
   private static Object credential;

   /**
    * The SecurityAssociation principal used when the server flag is true
    */
   private static ThreadLocal threadPrincipal;
   /**
    * The SecurityAssociation credential used when the server flag is true
    */
   private static ThreadLocal threadCredential;
   /**
    * The SecurityAssociation HashMap<String, Object>
    */
   private static ThreadLocal threadContextMap;

   /**
    * Thread local stacks of run-as principal roles used to implement J2EE
    * run-as identity propagation
    */
   private static RunAsThreadLocalStack threadRunAsStacks;
   /**
    * Thread local stacks of authenticated subject used to control the current
    * caller security context
    */ 
   private static SubjectThreadLocalStack threadSubjectStacks;

   /**
    * The permission required to access getPrincpal, getCredential
    */
   private static final RuntimePermission getPrincipalInfoPermission =
      new RuntimePermission("org.jboss.security.SecurityAssociation.getPrincipalInfo");
   /**
    * The permission required to access getSubject
    */
   private static final RuntimePermission getSubjectPermission =
      new RuntimePermission("org.jboss.security.SecurityAssociation.getSubject");
   /**
    * The permission required to access setPrincpal, setCredential, setSubject
    * pushSubjectContext, popSubjectContext
    */
   private static final RuntimePermission setPrincipalInfoPermission =
      new RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo");
   /**
    * The permission required to access setServer
    */
   private static final RuntimePermission setServerPermission =
      new RuntimePermission("org.jboss.security.SecurityAssociation.setServer");
   /**
    * The permission required to access pushRunAsIdentity/popRunAsIdentity
    */
   private static final RuntimePermission setRunAsIdentity =
      new RuntimePermission("org.jboss.security.SecurityAssociation.setRunAsRole");
   /**
    * The permission required to get the current security context info
    */
   private static final RuntimePermission getContextInfo =
      new RuntimePermission("org.jboss.security.SecurityAssociation.accessContextInfo", "get");
   /**
    * The permission required to set the current security context info
    */
   private static final RuntimePermission setContextInfo =
      new RuntimePermission("org.jboss.security.SecurityAssociation.accessContextInfo", "set");

   static
   {
      String flag = SecurityActions.getProperty("org.jboss.security.SecurityAssociation.ThreadLocal", "false");
      boolean useThreadLocal = Boolean.valueOf(flag).booleanValue();
      log.debug("Using ThreadLocal: "+useThreadLocal);

      trace = log.isTraceEnabled();
      if (useThreadLocal)
      {
         threadPrincipal = new ThreadLocal();
         threadCredential = new ThreadLocal();
         threadContextMap = new ThreadLocal()
         {
            protected Object initialValue()
            {
               return new HashMap();
            }
         };
      }
      else
      {
         threadPrincipal = new InheritableThreadLocal();
         threadCredential = new InheritableThreadLocal();
         threadContextMap = new HashMapInheritableLocal();
      }
      threadRunAsStacks = new RunAsThreadLocalStack(useThreadLocal);
      threadSubjectStacks = new SubjectThreadLocalStack(useThreadLocal);
   }

   /**
    * Get the current authentication principal information. If a security
    * manager is present, then this method calls the security manager's
    * <code>checkPermission</code> method with a
    * <code>RuntimePermission("org.jboss.security.SecurityAssociation.getPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * @return Principal, the current principal identity.
    */
   public static Principal getPrincipal()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getPrincipalInfoPermission);

      Principal thePrincipal = principal;
      
      
      if (server)
         thePrincipal = (Principal) threadPrincipal.get();
      
      if( trace )
         log.trace("getPrincipal, principal="+thePrincipal);
         
      return thePrincipal;
   }

   /**
    * Get the caller's principal. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.getPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * 
    * @return Principal, the current principal identity.
    */
   public static Principal getCallerPrincipal()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getPrincipalInfoPermission);

      Principal thePrincipal = peekRunAsIdentity(1);
      if( thePrincipal == null )
      {
         if (server)
            thePrincipal = (Principal) threadPrincipal.get();
         else
            thePrincipal = principal;
      }
      if( trace )
         log.trace("getCallerPrincipal, principal="+thePrincipal);
      return thePrincipal;
   }

   /**
    * Get the current authentication credential information. This can be of any type
    * including: a String password, a char[] password, an X509 cert, etc. If a
    * security manager is present, then this method calls the security manager's
    * <code>checkPermission</code> method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.getPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * @return Object, the credential that proves the principal identity.
    */
   public static Object getCredential()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getPrincipalInfoPermission);

      if (server)
         return threadCredential.get();
      else
         return credential;
   }

   /**
    * Get the current Subject information. If a security manager is present,
    * then this method calls the security manager's checkPermission method with
    * a  RuntimePermission("org.jboss.security.SecurityAssociation.getSubject")
    * permission to ensure it's ok to access principal information. If not, a
    * SecurityException will be thrown. Note that this method does not consider
    * whether or not a run-as identity exists. For access to this information
    * see the JACC PolicyContextHandler registered under the key
    * "javax.security.auth.Subject.container"
    * @return Subject, the current Subject identity.
    * @see javax.security.jacc.PolicyContext#getContext(String)
    */
   public static Subject getSubject()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getSubjectPermission);

      SubjectContext sc = threadSubjectStacks.peek();
      if( trace )
         log.trace("getSubject, sc="+sc);
      Subject subject = null;
      if( sc != null )
         subject = sc.getSubject();
      return subject;
   }

   /**
    * Set the current principal information. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * @param principal - the current principal identity.
    */
   public static void setPrincipal(Principal principal)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      if (trace)
         log.trace("setPrincipal, p=" + principal + ", server=" + server);
      if (server)
      {
         threadPrincipal.set(principal);
      }
      else
         SecurityAssociation.principal = principal;
      // Integrate with the new SubjectContext 
      SubjectContext sc = threadSubjectStacks.peek();
      if( sc == null )
      {
         // There is no active security context
         sc = new SubjectContext();
         threadSubjectStacks.push(sc);
      }
      else if( (sc.getFlags() & SubjectContext.PRINCIPAL_WAS_SET) != 0 )
      {
         // The current security context has its principal set
         sc = new SubjectContext();
         threadSubjectStacks.push(sc);    
      }
      sc.setPrincipal(principal);
      if (trace)
         log.trace("setPrincipal, sc="+sc);
   }

   /**
    * Set the current principal credential information. This can be of any type
    * including: a String password, a char[] password, an X509 cert, etc.
    *
    * If a security manager is present, then this method calls the security
    * manager's <code>checkPermission</code> method with a <code>
    * RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * @param credential - the credential that proves the principal identity.
    */
   public static void setCredential(Object credential)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      if (server)
         threadCredential.set(credential);
      else
         SecurityAssociation.credential = credential;
      // Integrate with the new SubjectContext 
      SubjectContext sc = threadSubjectStacks.peek();
      if( sc == null )
      {
         // There is no active security context
         sc = new SubjectContext();
         threadSubjectStacks.push(sc);
      }
      else if( (sc.getFlags() & SubjectContext.CREDENTIAL_WAS_SET) != 0 )
      {
         // The current security context has its principal set
         sc = new SubjectContext();
         threadSubjectStacks.push(sc);   
      }
      sc.setCredential(credential);
      if (trace)
         log.trace("setCredential, sc="+sc);
   }

   /**
    * Set the current Subject information. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    * @param subject - the current identity.
    */
   public static void setSubject(Subject subject)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      if (trace)
         log.trace("setSubject, s=" + subject + ", server=" + server);
      // Integrate with the new SubjectContext 
      SubjectContext sc = threadSubjectStacks.peek();
      if( sc == null )
      {
         // There is no active security context
         sc = new SubjectContext();
         threadSubjectStacks.push(sc);
      }
      else if( (sc.getFlags() & SubjectContext.SUBJECT_WAS_SET) != 0 )
      {
         // The current security context has its subject set
         sc = new SubjectContext();
         threadSubjectStacks.push(sc); 
      }
      sc.setSubject(subject);
      if (trace)
         log.trace("setSubject, sc="+sc);
   }

   /**
    * Get the current thread context info. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.accessContextInfo",
    * "get") </code> permission to ensure it's ok to access context information.
    * If not, a <code>SecurityException</code> will be thrown.
    * @param key - the context key
    * @return the mapping for the key in the current thread context
    */
   public static Object getContextInfo(Object key)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getContextInfo);

      HashMap contextInfo = (HashMap) threadContextMap.get();
      return contextInfo.get(key);
   }

   /**
    * Set the current thread context info. If a security manager is present,
    * then this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.accessContextInfo",
    * "set") </code> permission to ensure it's ok to access context information.
    * If not, a <code>SecurityException</code> will be thrown.
    * @param key - the context key
    * @param value - the context value to associate under key
    * @return the previous mapping for the key if one exists
    */
   public static Object setContextInfo(Object key, Object value)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setContextInfo);

      HashMap contextInfo = (HashMap) threadContextMap.get();
      return contextInfo.put(key, value);
   }

   /**
    * Push the current authenticated context. This sets the authenticated subject
    * along with the principal and proof of identity that was used to validate
    * the subject. This context is used for authorization checks. Typically
    * just the subject as seen by getSubject() is input into the authorization.
    * When run under a security manager this requires the
    * RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * permission.
    * @param subject - the authenticated subject
    * @param principal - the principal that was input into the authentication
    * @param credential - the credential that was input into the authentication
    */ 
   public static void pushSubjectContext(Subject subject,
      Principal principal, Object credential)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      // Set the legacy single-value access points
      if (server)
      {
         threadPrincipal.set(principal);
         threadCredential.set(credential);
      }
      else
      {
         SecurityAssociation.principal = principal;
         SecurityAssociation.credential = credential;
      }
      // Push the subject context
      SubjectContext sc = new SubjectContext(subject, principal, credential);
      threadSubjectStacks.push(sc);
      if (trace)
         log.trace("pushSubjectContext, subject=" + subject + ", sc="+sc);
   }
   /**
    * Push a duplicate of the current SubjectContext if one exists.
    * When run under a security manager this requires the
    * RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * permission.
    */
   public static void dupSubjectContext()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      SubjectContext sc = threadSubjectStacks.dup();
      if (trace)
         log.trace("dupSubjectContext, sc="+sc);
   }

   /**
    * Pop the current SubjectContext from the previous pushSubjectContext call
    * and return the pushed SubjectContext ig there was one.
    * When run under a security manager this requires the
    * RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * permission.
    * @return the SubjectContext pushed previously by a pushSubjectContext call
    */ 
   public static SubjectContext popSubjectContext()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      SubjectContext sc = threadSubjectStacks.pop();
      if (trace)
      {
         log.trace("popSubjectContext, sc="+sc);
      }
      
      Principal principal = null;
      Object credential = null;
      
      SubjectContext top = threadSubjectStacks.peek();
      
      if (top != null)
      {
         principal = top.getPrincipal();
         credential = top.getCredential();
      }
      
      if (server)
      {
         threadPrincipal.set(principal);
         threadCredential.set(credential);
      }
      else
      {
         SecurityAssociation.principal = principal;
         SecurityAssociation.credential = credential;
      }
      
      return sc;
   }
   
   /**
    * Look at the current thread of control's authenticated identity on the top
    * of the stack.
    * When run under a security manager this requires the
    * RuntimePermission("org.jboss.security.SecurityAssociation.getPrincipalInfo")
    * permission.
    * @return the SubjectContext pushed previously by a pushSubjectContext call
    */
   public static SubjectContext peekSubjectContext()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(getPrincipalInfoPermission);

      return threadSubjectStacks.peek();
   }

   /**
    * Clear all principal information. If a security manager is present, then
    * this method calls the security manager's <code>checkPermission</code>
    * method with a <code> RuntimePermission("org.jboss.security.SecurityAssociation.setPrincipalInfo")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    */
   public static void clear()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setPrincipalInfoPermission);

      if (trace)
         log.trace("clear, server=" + server);
      if (server == true)
      {
         threadPrincipal.set(null);
         threadCredential.set(null);
      }
      else
      {
         SecurityAssociation.principal = null;
         SecurityAssociation.credential = null;
      }
      // Remove all subject contexts
      threadSubjectStacks.clear();
   }

   /**
    * Push the current thread of control's run-as identity.
    */
   public static void pushRunAsIdentity(RunAsIdentity runAs)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setRunAsIdentity);
      if (trace)
         log.trace("pushRunAsIdentity, runAs=" + runAs);
      
      threadRunAsStacks.push(runAs);
   }

   /**
    * Pop the current thread of control's run-as identity.
    */
   public static RunAsIdentity popRunAsIdentity()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setRunAsIdentity);
      RunAsIdentity runAs = threadRunAsStacks.pop();
      if (trace)
         log.trace("popRunAsIdentity, runAs=" + runAs);
      return runAs;
   }

   /**
    * Look at the current thread of control's run-as identity on the top of the
    * stack.
    */
   public static RunAsIdentity peekRunAsIdentity()
   {
      return peekRunAsIdentity(0);
   }

   /**
    * Look at the current thread of control's run-as identity at the indicated
    * depth. Typically depth is either 0 for the identity the current caller
    * run-as that will be assumed, or 1 for the active run-as the previous
    * caller has assumed.
    * @return RunAsIdentity depth frames up.
    */
   public static RunAsIdentity peekRunAsIdentity(int depth)
   {
      RunAsIdentity runAs = threadRunAsStacks.peek(depth);
      return runAs;
   }

   /**
    * Set the server mode of operation. When the server property has been set to
    * true, the security information is maintained in thread local storage. This
    * should be called to enable property security semantics in any
    * multi-threaded environment where more than one thread requires that
    * security information be restricted to the thread's flow of control.
    *
    * If a security manager is present, then this method calls the security
    * manager's <code>checkPermission</code> method with a <code>
    * RuntimePermission("org.jboss.security.SecurityAssociation.setServer")
    * </code> permission to ensure it's ok to access principal information. If
    * not, a <code>SecurityException</code> will be thrown.
    */
   public static void setServer()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(setServerPermission);

      server = true;
   }

   /**
    * A subclass of ThreadLocal that implements a value stack using an ArrayList
    * and implements push, pop and peek stack operations on the thread local
    * ArrayList.
    */
   private static class RunAsThreadLocalStack
   {
      ThreadLocal local;

      RunAsThreadLocalStack(boolean threadLocal)
      {
         if( threadLocal == true )
            local = new ArrayListLocal();
         else
            local = new ArrayListInheritableLocal();
      }
      
      int size()
      {
         ArrayList stack = (ArrayList) local.get();
         return stack.size();
      }

      void push(RunAsIdentity runAs)
      {
         ArrayList stack = (ArrayList) local.get();
         stack.add(runAs);
      }

      RunAsIdentity pop()
      {
         ArrayList stack = (ArrayList) local.get();
         RunAsIdentity runAs = null;
         int lastIndex = stack.size() - 1;
         if (lastIndex >= 0)
            runAs = (RunAsIdentity) stack.remove(lastIndex);
         return runAs;
      }

      /**
       * Look for the first non-null run-as identity on the stack starting
       * with the value at depth.
       * @return The run-as identity if one exists, null otherwise.
       */
      RunAsIdentity peek(int depth)
      {
         ArrayList stack = (ArrayList) local.get();
         RunAsIdentity runAs = null;
         final int stackSize = stack.size();
         do
         {
            int index = stackSize - 1 - depth;
            if( index >= 0 )
               runAs = (RunAsIdentity) stack.get(index);
            depth ++;
         }
         while (runAs == null && depth <= stackSize - 1);
         return runAs;
      }
   }

   /**
    * The encapsulation of the authenticated subject
    */ 
   public static class SubjectContext
   {
      public static final int SUBJECT_WAS_SET = 1;
      public static final int PRINCIPAL_WAS_SET = 2;
      public static final int CREDENTIAL_WAS_SET = 4;

      private Subject subject;
      private Principal principal;
      private Object credential;
      private int flags;

      public SubjectContext()
      {
         this.flags = 0;
      }
      public SubjectContext(Subject s, Principal p, Object cred)
      {
         this.subject = s;
         this.principal = p;
         this.credential = cred;
         this.flags = SUBJECT_WAS_SET | PRINCIPAL_WAS_SET | CREDENTIAL_WAS_SET;
      }

      public Subject getSubject()
      {
         return subject;
      }
      public void setSubject(Subject subject)
      {
         this.subject = subject;
         this.flags |= SUBJECT_WAS_SET;
      }

      public Principal getPrincipal()
      {
         return principal;
      }
      public void setPrincipal(Principal principal)
      {
         this.principal = principal;
         this.flags |= PRINCIPAL_WAS_SET;
      }

      public Object getCredential()
      {
         return credential;
      }
      public void setCredential(Object credential)
      {
         this.credential = credential;
         this.flags |= CREDENTIAL_WAS_SET;
      }

      public int getFlags()
      {
         return this.flags;
      }

      public String toString()
      {
         StringBuffer tmp = new StringBuffer(super.toString());
         tmp.append("{principal=");
         tmp.append(principal);
         tmp.append(",subject=");
         if( subject != null )
            tmp.append(System.identityHashCode(subject));
         else
            tmp.append("null");
         tmp.append("}");
         return tmp.toString();
      }
   }

   private static class SubjectThreadLocalStack
   {
      ThreadLocal local;

      SubjectThreadLocalStack(boolean threadLocal)
      {
         if( threadLocal == true )
            local = new ArrayListLocal();
         else
            local = new ArrayListInheritableLocal();
      }
      
      int size()
      {
         ArrayList stack = (ArrayList) local.get();
         return stack.size();
      }

      void push(SubjectContext context)
      {
         ArrayList stack = (ArrayList) local.get();
         stack.add(context);
      }

      SubjectContext dup()
      {
         ArrayList stack = (ArrayList) local.get();
         SubjectContext context = null;
         int lastIndex = stack.size() - 1;
         if (lastIndex >= 0)
         {
            context = (SubjectContext) stack.get(lastIndex);
            stack.add(context);
         }
         return context;
      }

      SubjectContext pop()
      {
         ArrayList stack = (ArrayList) local.get();
         SubjectContext context = null;
         int lastIndex = stack.size() - 1;
         if (lastIndex >= 0)
            context = (SubjectContext) stack.remove(lastIndex);
         return context;
      }

      /**
       * Look for the first non-null run-as identity on the stack starting
       * with the value at depth.
       * @return The run-as identity if one exists, null otherwise.
       */
      SubjectContext peek()
      {
         ArrayList stack = (ArrayList) local.get();
         SubjectContext context = null;
         int lastIndex = stack.size() - 1;
         if (lastIndex >= 0)
            context = (SubjectContext) stack.get(lastIndex);
         return context;
      }
      /**
       * Remove all SubjectContext from the current thread stack
       */ 
      void clear()
      {
         ArrayList stack = (ArrayList) local.get();
         stack.clear();
      }
   }

   private static class ArrayListLocal extends ThreadLocal
   {
      protected Object initialValue()
      {
         return new ArrayList();
      }
      
   }

   private static class ArrayListInheritableLocal extends InheritableThreadLocal
   {
      /**
       * Override to make a copy of the parent as not doing so results in multiple
       * threads sharing the unsynchronized list of the parent thread.
       * @param parentValue - the parent ArrayList
       * @return a copy of the parent thread list
       */
      protected Object childValue(Object parentValue)
      {
         ArrayList list = (ArrayList) parentValue;
         /* It seems there are scenarios where the size can change during the copy so there is
         a fallback to an empty list here.
         */
         ArrayList copy = null;
         try
         {
            copy = new ArrayList(list);
         }
         catch(Throwable t)
         {
            log.debug("Failed to copy parent list, using new list");
            copy = new ArrayList();
         }
         return copy;
      }

      protected Object initialValue()
      {
         return new ArrayList();
      }
      
   }
   private static class HashMapInheritableLocal extends InheritableThreadLocal
   {
      /**
       * Override to make a copy of the parent as not doing so results in multiple
       * threads sharing the unsynchronized map of the parent thread.
       * @param parentValue - the parent HashMap
       * @return a copy of the parent thread map
       */
      protected Object childValue(Object parentValue)
      {
         HashMap map = (HashMap) parentValue;
         /* It seems there are scenarios where the size can change during the copy so there is
         a fallback to an empty map here.
         */
         HashMap copy = null;
         try
         {
            copy = new HashMap(map);
         }
         catch(Throwable t)
         {
            log.debug("Failed to copy parent map, using new map");
            copy = new HashMap();
         }
         return copy;
      }

      protected Object initialValue()
      {
         return new HashMap();
      }
      
   }
}