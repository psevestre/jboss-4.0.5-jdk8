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
package org.jboss.naming;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.jboss.invocation.Invocation;
import org.jboss.invocation.MarshalledInvocation;
import org.jboss.invocation.jrmp.server.JRMPProxyFactoryMBean;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.util.threadpool.ThreadPool;
import org.jboss.util.threadpool.BasicThreadPoolMBean;
import org.jnp.interfaces.Naming;
import org.jnp.interfaces.MarshalledValuePair;
import org.jnp.server.Main;

/**
 * A JBoss service that starts the jnp JNDI server.
 *
 * @author <a href="mailto:rickard.oberg@telkel.com">Rickard �berg</a>
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @author <a href="mailto:andreas@jboss.org">Andreas Schaefer</a>.
 * @version $Revision: 57220 $
 *
 * @jmx:mbean name="jboss:service=Naming"
 *  extends="org.jboss.system.ServiceMBean, org.jnp.server.MainMBean"
 */
public class NamingService
   extends ServiceMBeanSupport
   implements NamingServiceMBean
{
   /** The actual naming service impl from the legacy jndi service */
   private Main naming;
   /** The hash mappings of the Naming interface methods */
   private Map marshalledInvocationMapping = new HashMap();
   /** An optional proxy factory for externalizing the Naming proxy transport */
   private JRMPProxyFactoryMBean proxyFactory;

   public NamingService()
   {
      naming = new Main(this.getClass().getName());
   }

   /** Set the thread pool used for the bootstrap lookups
    *
    * @jmx:managed-attribute
    *
    * @param poolMBean 
    */
   public void setLookupPool(BasicThreadPoolMBean poolMBean)
   {
      ThreadPool lookupPool = poolMBean.getInstance();
      naming.setLookupPool(lookupPool);
   }

   /** Get the call by value flag for jndi lookups.
    * 
    * @jmx:managed-attribute
    * @return true if all lookups are unmarshalled using the caller's TCL,
    *    false if in VM lookups return the value by reference.
    */ 
   public boolean getCallByValue()
   {
      return MarshalledValuePair.getEnableCallByReference() == false;
   }
   /** Set the call by value flag for jndi lookups.
    *
    * @jmx:managed-attribute
    * @param flag - true if all lookups are unmarshalled using the caller's TCL,
    *    false if in VM lookups return the value by reference.
    */
   public void setCallByValue(boolean flag)
   {
      boolean callByValue = ! flag;
      MarshalledValuePair.setEnableCallByReference(callByValue);
   }

   public void setPort(int port)
   {
      naming.setPort(port);
   }

   public int getPort()
   {
      return naming.getPort();
   }

   public void setRmiPort(int port)
   {
      naming.setRmiPort(port);
   }

   public int getRmiPort()
   {
      return naming.getRmiPort();
   }

   public String getBindAddress()
   {
      return naming.getBindAddress();
   }

   public void setBindAddress(String host) throws UnknownHostException
   {
      naming.setBindAddress(host);
   }

   public String getRmiBindAddress()
   {
      return naming.getRmiBindAddress();
   }

   public void setRmiBindAddress(String host) throws UnknownHostException
   {
      naming.setRmiBindAddress(host);
   }

   public int getBacklog()
   {
      return naming.getBacklog();
   }

   public void setBacklog(int backlog)
   {
      naming.setBacklog(backlog);
   }

   public boolean getInstallGlobalService()
   {
      return naming.getInstallGlobalService();
   }
   public void setInstallGlobalService(boolean flag)
   {
      naming.setInstallGlobalService(flag);
   }
   public boolean getUseGlobalService()
   {
      return naming.getUseGlobalService();
   }
   public void setUseGlobalService(boolean flag)
   {
      naming.setUseGlobalService(flag);
   }
   public String getClientSocketFactory()
   {
      return naming.getClientSocketFactory();
   }

   public void setClientSocketFactory(String factoryClassName)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException
   {
      naming.setClientSocketFactory(factoryClassName);
   }

   public String getServerSocketFactory()
   {
      return naming.getServerSocketFactory();
   }

   public void setServerSocketFactory(String factoryClassName)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException
   {
      naming.setServerSocketFactory(factoryClassName);
   }

   public void setJNPServerSocketFactory(String factoryClassName)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException
   {
      naming.setJNPServerSocketFactory(factoryClassName);
   }

   public void setInvokerProxyFactory(JRMPProxyFactoryMBean proxyFactory)
   {
      this.proxyFactory = proxyFactory;
   }

   protected void startService()
      throws Exception
   {
      boolean debug = log.isDebugEnabled();

      // Read jndi.properties into system properties
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      InputStream is = loader.getResourceAsStream("jndi.properties");
      if (is == null)
         throw new RuntimeException("Cannot find jndi.properties, it should be at conf/jndi.properties by default.");
      Properties props = new Properties();
      try
      {
         props.load(is);
      }
      finally
      {
         is.close();
      }

      for (Enumeration keys = props.propertyNames(); keys.hasMoreElements(); )
      {
         String key = (String) keys.nextElement();
         String value = props.getProperty(key);
         if (debug)
         {
            log.debug("System.setProperty, key="+key+", value="+value);
         }
         System.setProperty(key, value);
      }
      if( proxyFactory != null )
         naming.setNamingProxy(proxyFactory.getProxy());
      naming.start();

      /* Create a default InitialContext and dump out its env to show what properties
         were used in its creation. If we find a Context.PROVIDER_URL property
         issue a warning as this means JNDI lookups are going through RMI.
      */
      InitialContext iniCtx = new InitialContext();
      Hashtable env = iniCtx.getEnvironment();
      log.debug("InitialContext Environment: ");
      Object providerURL = null;
      for (Enumeration keys = env.keys(); keys.hasMoreElements(); )
      {
         Object key = keys.nextElement();
         Object value = env.get(key);
         String type = value == null ? "" : value.getClass().getName();
         log.debug("key="+key+", value("+type+")="+value);
         if( key.equals(Context.PROVIDER_URL) )
            providerURL = value;
      }
      // Warn if there was a Context.PROVIDER_URL
      if( providerURL != null )
         log.warn("Context.PROVIDER_URL in server jndi.properties, url="+providerURL);

      /* Bind an ObjectFactory to "java:comp" so that "java:comp/env" lookups
         produce a unique context for each thread contexxt ClassLoader that
         performs the lookup.
      */
      ClassLoader topLoader = Thread.currentThread().getContextClassLoader();
      ENCFactory.setTopClassLoader(topLoader);
      RefAddr refAddr = new StringRefAddr("nns", "ENC");
      Reference envRef = new Reference("javax.naming.Context", refAddr, ENCFactory.class.getName(), null);
      Context ctx = (Context)iniCtx.lookup("java:");
      ctx.rebind("comp", envRef);
      log.debug("Listening on port "+naming.getPort());
      ctx.close();
      iniCtx.close();

      // Build the Naming interface method map
      HashMap tmpMap = new HashMap(13);
      Method[] methods = Naming.class.getMethods();
      for(int m = 0; m < methods.length; m ++)
      {
         Method method = methods[m];
         Long hash = new Long(MarshalledInvocation.calculateHash(method));
         tmpMap.put(hash, method);
      }
      marshalledInvocationMapping = Collections.unmodifiableMap(tmpMap);
   }

   protected void stopService()
      throws Exception
   {
      naming.stop();
      log.debug("JNP server stopped");
   }

   /**
    * The <code>getNamingServer</code> method makes this class
    * extensible, but it is a hack.  The NamingServer should be
    * exposed directly as an xmbean, and the startup logic put in
    * either an interceptor, the main class itself, or an auxilliary
    * mbean (for the enc).
    *
    * @return a <code>Main</code> value
    */
   protected Main getNamingServer()
   {
      return naming;
   } // end of main()


   /** Expose the Naming service interface mapping as a read-only attribute
    *
    * @jmx:managed-attribute
    *
    * @return A Map<Long hash, Method> of the Naming interface
    */
   public Map getMethodMap()
   {
      return marshalledInvocationMapping;
   }
   
   public void createAlias(String fromName, String toName) throws Exception
   {
      Util.createLinkRef(fromName, toName);
      log.info("Created alias " + fromName + "->" + toName);
   }
   
   public void removeAlias(String name) throws Exception
   {
      log.info("Removing alias " + name);
      Util.removeLinkRef(name);
   }

   /** Expose the Naming service via JMX to invokers.
    *
    * @jmx:managed-operation
    *
    * @param invocation    A pointer to the invocation object
    * @return              Return value of method invocation.
    *
    * @throws Exception    Failed to invoke method.
    */
   public Object invoke(Invocation invocation) throws Exception
   {
      Naming theServer = naming.getServer();
      // Set the method hash to Method mapping
      if (invocation instanceof MarshalledInvocation)
      {
         MarshalledInvocation mi = (MarshalledInvocation) invocation;
         mi.setMethodMap(marshalledInvocationMapping);
      }
      // Invoke the Naming method via reflection
      Method method = invocation.getMethod();
      Object[] args = invocation.getArguments();
      Object value = null;
      try
      {
         value = method.invoke(theServer, args);
      }
      catch(InvocationTargetException e)
      {
         Throwable t = e.getTargetException();
         if( t instanceof Exception )
            throw (Exception) t;
         else
            throw new UndeclaredThrowableException(t, method.toString());
      }

      return value;
   }
}
