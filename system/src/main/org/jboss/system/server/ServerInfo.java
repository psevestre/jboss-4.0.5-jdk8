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
package org.jboss.system.server;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.logging.Logger;
import org.jboss.util.platform.Java;

/**
 * An MBean that provides a rich view of system information for the JBoss
 * server in which it is deployed.
 *
 * @author <a href="mailto:rickard.oberg@telkel.com">Rickard Oberg</a>
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>
 * @author <a href="mailto:hiram.chirino@jboss.org">Hiram Chirino</a>
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author <a href="mailto:marc.fleury@jboss.org">Marc Fleury</a>
 * @author <a href="mailto:dimitris@jboss.org">Dimitris Andreadis</a>
 * @version $Revision: 57205 $
 */
public class ServerInfo
   implements ServerInfoMBean, MBeanRegistration
{
   /** Class logger. */
   private static final Logger log = Logger.getLogger(ServerInfo.class);

   /** Zero */
   private static final Integer ZERO = new Integer(0);

   /** The MBeanServer we are registered to */
   private MBeanServer server;
   
   /** The cached host name for the server. */
   private String hostName;
   
   /** The cached host address for the server. */
   private String hostAddress;

   /** The cached jdk5+ ThreadMXBean instance */
   private Object threadMXBean;
   
   /** The cached jdk5+ ManagementFactory.getMemoryPoolMXBeans() method */
   private Method getMemoryPoolMXBeans;
   
   /** The cached jdk5+ MemoryPoolMXBean methods */
   private Method getName;
   private Method getType;
   private Method getUsage;
   private Method getPeakUsage;
   
   /** The cached jdk5+ MemoryUsage methods */
   private Method getInit;
   private Method getUsed;
   private Method getCommitted;
   private Method getMax;
   
   /** The cached jdk5+ ThreadMXBean.getThreadInfo() method */
   private Method getThreadInfo;
   private Method getAllThreadIds;
   private Method getThreadCpuTime;

   /** The cached jdk5+ ThreadInfo methods */
   private Method getThreadName;
   private Method getThreadState;
   private Method getLockName;
   private Method getStackTrace;
   
   /** The cached jdk5+ Thread.getId() method */
   private Method getThreadId;
   
   ///////////////////////////////////////////////////////////////////////////
   //                               JMX Hooks                               //
   ///////////////////////////////////////////////////////////////////////////
   
   public ObjectName preRegister(MBeanServer server, ObjectName name)
      throws Exception
   {
      this.server = server;
      // Dump out basic JVM & OS info as INFO priority msgs
      log.info("Java version: " +
      System.getProperty("java.version") + "," +
      System.getProperty("java.vendor"));
      
      log.info("Java VM: " +
      System.getProperty("java.vm.name") + " " +
      System.getProperty("java.vm.version") + "," +
      System.getProperty("java.vm.vendor"));
      
      log.info("OS-System: " +
      System.getProperty("os.name") + " " +
      System.getProperty("os.version") + "," +
      System.getProperty("os.arch"));
      
      // Dump out the entire system properties
      log.debug("Full System Properties Dump");
      Enumeration names = System.getProperties().propertyNames();
      while (names.hasMoreElements())
      {
         String pname = (String)names.nextElement();
            log.debug("    " + pname + ": " + System.getProperty(pname));
      }
      
      // cache a reference to the platform ThreadMXBean
      // and related Thread/ThreadInfo methods, if available
      if (Java.isCompatible(Java.VERSION_1_5))
      {
         try
         {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();            
            Class clazz = cl.loadClass("java.lang.management.ManagementFactory");

            // cache ThreadMXBean instance
            Method method = clazz.getMethod("getThreadMXBean", null);
            this.threadMXBean = method.invoke(null, null);
            
            // cache ManagementFactory.getMemoryPoolMXBeans() method
            this.getMemoryPoolMXBeans = clazz.getMethod("getMemoryPoolMXBeans", null);
            
            // cache MemoryPoolMXBean methods
            clazz = cl.loadClass("java.lang.management.MemoryPoolMXBean");
            this.getName = clazz.getMethod("getName", null);
            this.getType = clazz.getMethod("getType", null);
            this.getUsage = clazz.getMethod("getUsage", null);
            this.getPeakUsage = clazz.getMethod("getPeakUsage", null);
            
            // cache MemoryUsage methods
            clazz = cl.loadClass("java.lang.management.MemoryUsage");
            this.getInit = clazz.getMethod("getInit", null);
            this.getUsed = clazz.getMethod("getUsed", null);
            this.getCommitted = clazz.getMethod("getCommitted", null);
            this.getMax = clazz.getMethod("getMax", null);
            
            // cache ThreadMXBean.getThreadInfo() method
            clazz = cl.loadClass("java.lang.management.ThreadMXBean");
            this.getThreadInfo = clazz.getMethod("getThreadInfo", new Class[] { Long.TYPE, Integer.TYPE } );
            this.getAllThreadIds = clazz.getMethod("getAllThreadIds", null );
            this.getThreadCpuTime = clazz.getMethod("getThreadCpuTime", new Class[] { Long.TYPE } );

            // cache ThreadInfo methods
            clazz = cl.loadClass("java.lang.management.ThreadInfo");
            this.getThreadName = clazz.getMethod("getThreadName", null);
            this.getThreadState = clazz.getMethod("getThreadState", null);
            this.getLockName = clazz.getMethod("getLockName", null);
            this.getStackTrace = clazz.getMethod("getStackTrace", null);
            
            // cache Thread.getId() method
            clazz = Thread.class;
            this.getThreadId = clazz.getMethod("getId", null);
         }
         catch (Exception e)
         {
            log.debug("Cannot access platform ThreadMXBean", e);
         }
      }
      
      return name == null ? OBJECT_NAME : name;
   }
   
   public void postRegister(Boolean registrationDone)
   {
      // empty
   }
   
   public void preDeregister() throws Exception
   {
      // empty
   }
   
   public void postDeregister()
   {
      // empty
   }
   
   
   ///////////////////////////////////////////////////////////////////////////
   //                            Server Information                         //
   ///////////////////////////////////////////////////////////////////////////

   /**
    * @jmx:managed-attribute
    */
   public String getJavaVersion()
   {
      return System.getProperty("java.version");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getJavaVendor()
   {
      return System.getProperty("java.vendor");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getJavaVMName()
   {
      return System.getProperty("java.vm.name");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getJavaVMVersion()
   {
      return System.getProperty("java.vm.version");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getJavaVMVendor()
   {
      return System.getProperty("java.vm.vendor");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getOSName()
   {
      return System.getProperty("os.name");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getOSVersion()
   {
      return System.getProperty("os.version");
   }

   /**
    * @jmx:managed-attribute
    */
   public String getOSArch()
   {
      return System.getProperty("os.arch");
   }
   
   /**
    * @jmx:managed-attribute
    */
   public Long getTotalMemory()
   {
      return new Long(Runtime.getRuntime().totalMemory());
   }
   
   /**
    * @jmx:managed-attribute
    */
   public Long getFreeMemory()
   {
      return new Long(Runtime.getRuntime().freeMemory());
   }
   
   /**
    * Returns <tt>Runtime.getRuntime().maxMemory()<tt> on 
    * JDK 1.4 vms or -1 on previous versions.
    * 
    * @jmx:managed-attribute
    */
   public Long getMaxMemory()
   {
      if (Java.isCompatible(Java.VERSION_1_4)) {
         // Uncomment when JDK 1.4 is the base JVM
         // return new Long(Runtime.getRuntime().maxMemory());

         // until then use reflection to do the job
         try {
            Runtime rt = Runtime.getRuntime();
            Method m = rt.getClass().getMethod("maxMemory", new Class[0]);
            return (Long)m.invoke(rt, new Object[0]);
         }
         catch (Exception e) {
            log.error("Operation failed", e);
         }
      }

      return new Long(-1);
   }

   /**
    * Returns <tt>Runtime.getRuntime().availableProcessors()</tt> on 
    * JDK 1.4 vms or -1 on previous versions.
    * 
    * @jmx:managed-attribute
    */
   public Integer getAvailableProcessors()
   {
      if (Java.isCompatible(Java.VERSION_1_4)) {
         // Uncomment when JDK 1.4 is the base JVM
         // return new Integer(Runtime.getRuntime().availableProcessors());

         // until then use reflection to do the job
         try {
            Runtime rt = Runtime.getRuntime();
            Method m = rt.getClass().getMethod("availableProcessors", new Class[0]);
            return (Integer)m.invoke(rt, new Object[0]);
         }
         catch (Exception e) {
            log.error("Operation failed", e);
         }
      }

      return new Integer(-1);
   }

   /**
    * Returns InetAddress.getLocalHost().getHostName();
    *
    * @jmx:managed-attribute
    */
   public String getHostName()
   {
      if (hostName == null)
      {
         try
         {
            hostName = java.net.InetAddress.getLocalHost().getHostName();
         }
         catch (java.net.UnknownHostException e)
         {
            log.error("Error looking up local hostname", e);
            hostName = "<unknown>";
         }
      }
      
      return hostName;
   }
   
   /**
    * Returns InetAddress.getLocalHost().getHostAddress();
    *
    * @jmx:managed-attribute
    */
   public String getHostAddress()
   {
      if (hostAddress == null)
      {
         try
         {
            hostAddress = java.net.InetAddress.getLocalHost().getHostAddress();
         }
         catch (java.net.UnknownHostException e)
         {
            log.error("Error looking up local address", e);
            hostAddress = "<unknown>";
         }
      }
      
      return hostAddress;
   }

   /**
    * Return a listing of the thread pools on jdk5+.
    * 
    * @jmx:managed-operation
    * 
    * @param fancy produce a text-based graph when true
    */
   public String listMemoryPools(boolean fancy)
   {
      if (getMemoryPoolMXBeans != null)
      {
         // running under jdk5+
         StringBuffer sbuf = new StringBuffer(4196);
         try
         {
            // get the pools
            List poolList = (List)getMemoryPoolMXBeans.invoke(null, null);
            sbuf.append("<b>Total Memory Pools:</b> ").append(poolList.size());
            sbuf.append("<blockquote>");
            for (Iterator i = poolList.iterator(); i.hasNext(); )
            {
               // MemoryPoolMXBean instance
               Object pool = i.next();
               String name = (String)getName.invoke(pool, null);
               // enum MemoryType
               Object type = getType.invoke(pool, null);
               sbuf.append("<b>Pool: ").append(name);
               sbuf.append("</b> (").append(type).append(")");
               
               // PeakUsage/CurrentUsage
               Object peakUsage = getPeakUsage.invoke(pool, null);
               Object usage = getUsage.invoke(pool, null);
               
               sbuf.append("<blockquote>");
               if (usage != null && peakUsage != null)
               {
                  Long init = (Long)getInit.invoke(peakUsage, null);
                  Long used = (Long)getUsed.invoke(peakUsage, null);
                  Long committed = (Long)getCommitted.invoke(peakUsage, null);
                  Long max = (Long)getMax.invoke(peakUsage, null);

                  sbuf.append("Peak Usage    : ");
                  sbuf.append("init:").append(init);
                  sbuf.append(", used:").append(used);
                  sbuf.append(", committed:").append(committed);
                  sbuf.append(", max:").append(max);
                  sbuf.append("<br>");
                  
                  init = (Long)getInit.invoke(usage, null);
                  used = (Long)getUsed.invoke(usage, null);
                  committed = (Long)getCommitted.invoke(usage, null);
                  max = (Long)getMax.invoke(usage, null);

                  sbuf.append("Current Usage : ");
                  sbuf.append("init:").append(init);
                  sbuf.append(", used:").append(used);
                  sbuf.append(", committed:").append(committed);
                  sbuf.append(", max:").append(max);
                  
                  if (fancy)
                  {
                     TextGraphHelper.poolUsage(sbuf, used.longValue(), committed.longValue(), max.longValue());
                  }
               }
               else
               {
                  sbuf.append("Memory pool NOT valid!");
               }
               sbuf.append("</blockquote><br>");
            }
            sbuf.append("</blockquote>");
         }
         catch (Exception e)
         {
            // ignore
         }
         return sbuf.toString();
      }
      else
      {
         return "<b>Memory pool information available only under a JDK5+ compatible JVM!</b>";
      }
   }
   
   /**
    * @jmx:managed-operation
    */
   public Integer getActiveThreadCount()
   {
      return new Integer(getRootThreadGroup().activeCount());
   }

   /**
    * @jmx:managed-operation
    */
   public Integer getActiveThreadGroupCount()
   {
      return new Integer(getRootThreadGroup().activeGroupCount());
   }
   
   /**
    * Return a listing of the active threads and thread groups.
    *
    * @jmx:managed-operation
    */
   public String listThreadDump()
   {
      ThreadGroup root = getRootThreadGroup();
      
      // Count the threads/groups during our traversal
      // rather than use the often inaccurate ThreadGroup
      // activeCount() and activeGroupCount()
      ThreadGroupCount count = new ThreadGroupCount();

      // traverse
      String threadGroupInfo = getThreadGroupInfo(root, count);
      // attach counters
      String threadDump =
         "<b>Total Threads:</b> " + count.threads + "<br>" +
         "<b>Total Thread Groups:</b> " + count.groups + "<br>" +
         threadGroupInfo;
      
      return threadDump;
   }
   
   /**
    * Return a listing of the active threads and thread groups.
    *
    * @jmx:managed-operation
    */
   public String listThreadCpuUtilization()
   {
      Set threads = getThreadCpuUtilization(); 

      if (threads == null)
      {
         return("Thread cpu utilization requires J2SE5+");
      }
      else
      {
         long totalCPU = 0;
         StringBuffer buffer = new StringBuffer();
         buffer.append("<table><tr><th>Thread Name</th><th>CPU (milliseconds)</th></tr>");
         for (Iterator i = threads.iterator(); i.hasNext();)
         {
            ThreadCPU thread = (ThreadCPU) i.next();
            buffer.append("<tr><td>").append(thread.name).append("</td><td>");
            buffer.append(thread.cpuTime).append("</td></tr>");
            totalCPU += thread.cpuTime;
         }
         buffer.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr><tr><td>Total</td><td>");
         buffer.append(totalCPU).append("</td></tr></table>");
         return buffer.toString();
      }
   }
   
   ///////////////////////////////////////////////////////////////////////////
   //                               Private                                 //
   ///////////////////////////////////////////////////////////////////////////
   
   /**
    * Get the Thread cpu utilization
    * 
    * @return an ordered 
    */
   private Set getThreadCpuUtilization()
   {
      if (threadMXBean == null)
         return null;
      
      try
      {
         TreeSet result = new TreeSet();
         long[] threads = (long[]) getAllThreadIds.invoke(threadMXBean, null);
         for (int i = 0; i < threads.length; ++i)
         {
            Long id = new Long(threads[i]);
            Long cpuTime = (Long) getThreadCpuTime.invoke(threadMXBean, new Object[] { id });
            Object threadInfo = getThreadInfo.invoke(threadMXBean, new Object[] { id, ZERO });
            String name = (String) getThreadName.invoke(threadInfo, null);
            result.add(new ThreadCPU(name, cpuTime.longValue()));
         }
         return result;
      }
      catch (Exception e)
      {
         log.warn("Error retrieving thread cpu utiliation", e);
         return null;
      }
   }
   
   /*
    * Traverse to the root thread group
    */
   private ThreadGroup getRootThreadGroup()
   {
      ThreadGroup group = Thread.currentThread().getThreadGroup();
      while (group.getParent() != null)
      {
         group = group.getParent();
      }

      return group;
   }
   
   /*
    * Recurse inside ThreadGroups to create the thread dump
    */
   private String getThreadGroupInfo(ThreadGroup group, ThreadGroupCount count)
   {
      StringBuffer rc = new StringBuffer();
      
      // Visit one more group
      count.groups++;
      
      rc.append("<br><b>");
      rc.append("Thread Group: " + group.getName());
      rc.append("</b> : ");
      rc.append("max priority:" + group.getMaxPriority() +
                ", demon:" + group.isDaemon());
      
      rc.append("<blockquote>");
      Thread threads[]= new Thread[group.activeCount()];
      group.enumerate(threads, false);
      for (int i= 0; i < threads.length && threads[i] != null; i++)
      {
         // Visit one more thread
         count.threads++;
         
         rc.append("<b>");
         rc.append("Thread: " + threads[i].getName());
         rc.append("</b> : ");
         rc.append("priority:" + threads[i].getPriority() +
         ", demon:" + threads[i].isDaemon());
         // Output extra info with jdk5+, or just <br>
         outputJdk5ThreadMXBeanInfo(rc, threads[i]);
      }
      
      ThreadGroup groups[]= new ThreadGroup[group.activeGroupCount()];
      group.enumerate(groups, false);
      for (int i= 0; i < groups.length && groups[i] != null; i++)
      {
         rc.append(getThreadGroupInfo(groups[i], count));
      }
      rc.append("</blockquote>");
      
      return rc.toString();
   }

   /*
    * Complete the output of thread info, with optional stuff
    * when running under jdk5+, or just change line.
    */
   private void outputJdk5ThreadMXBeanInfo(StringBuffer sbuf, Thread thread)
   {
      // if ThreadMXBean has been found, we run under jdk5+
      if (threadMXBean != null)
      {
         // use reflection all the way, until we base on jdk5
         try
         {
            // get the threadId
            Long threadId = (Long)getThreadId.invoke(thread, null);
            // get the ThreadInfo object for that threadId, max StackTraceElement depth
            Object threadInfo = getThreadInfo.invoke(threadMXBean,
                  new Object[] { threadId, new Integer(Integer.MAX_VALUE) });
            // get misc info from ThreadInfo
            Object threadState = getThreadState.invoke(threadInfo, null); // enum
            String threadLockName = (String)getLockName.invoke(threadInfo, null);
            Object[] stackTrace = (Object[])getStackTrace.invoke(threadInfo, null);
            
            sbuf.append(", threadId:").append(threadId);
            sbuf.append(", threadState:").append(threadState);
            sbuf.append(", threadLockName:").append(threadLockName);
            sbuf.append("<br>");
            if (stackTrace.length > 0)
            {
               sbuf.append("<blockquote>");
               for (int i = 0; i < stackTrace.length; i++)
               {
                  sbuf.append(stackTrace[i]).append("<br>");
               }
               sbuf.append("</blockquote>");
            }
         }
         catch (Exception ignore)
         {
            // empty
         }
      }
      else
      {
         // no jdk5+ info to add, just change line
         sbuf.append("<br>");
      }
   }
   
   /**
    * Display the java.lang.Package info for the pkgName
    *
    * @jmx:managed-operation
    */
   public String displayPackageInfo(String pkgName)
   {
      Package pkg = Package.getPackage(pkgName);
      if( pkg == null )
         return "<h2>Package:"+pkgName+" Not Found!</h2>";

      StringBuffer info = new StringBuffer("<h2>Package: "+pkgName+"</h2>");
      displayPackageInfo(pkg, info);
      return info.toString();
   }

   private void displayPackageInfo(Package pkg, StringBuffer info)
   {
      info.append("<pre>\n");
      info.append("SpecificationTitle: "+pkg.getSpecificationTitle());
      info.append("\nSpecificationVersion: "+pkg.getSpecificationVersion());
      info.append("\nSpecificationVendor: "+pkg.getSpecificationVendor());
      info.append("\nImplementationTitle: "+pkg.getImplementationTitle());
      info.append("\nImplementationVersion: "+pkg.getImplementationVersion());
      info.append("\nImplementationVendor: "+pkg.getImplementationVendor());
      info.append("\nisSealed: "+pkg.isSealed());
      info.append("</pre>\n");
   }
   
   ///////////////////////////////////////////////////////////////////////////
   //                               Inner                                   //
   ///////////////////////////////////////////////////////////////////////////
   
   /*
    * Inner Helper class for fancy text graphs
    * 
    * @author dimitris@jboss.org
    */
   private static class TextGraphHelper
   {
      // number conversions
      static final DecimalFormat formatter = new DecimalFormat("#.##");      
      static final long KILO = 1024;
      static final long MEGA = 1024 * 1024;
      static final long GIGA = 1024 * 1024 * 1024;
      
      // how many dashes+pipe is 100%
      static final int factor = 70;
      static char[] fixedline;
      static char[] baseline;
      static char[] barline;
      static char[] spaces;
      static
      {
         // cache a couple of Strings
         StringBuffer sbuf0 = new StringBuffer();
         StringBuffer sbuf1 = new StringBuffer();
         StringBuffer sbuf2 = new StringBuffer();
         StringBuffer sbuf3 = new StringBuffer();
         sbuf0.append('+');
         sbuf1.append('|');
         sbuf2.append('|');
         for (int i = 1; i < factor; i++)
         {
            sbuf0.append('-');
            sbuf1.append('-');
            sbuf2.append('/');
            sbuf3.append(' ');
         }
         sbuf0.append('+');
         fixedline = sbuf0.toString().toCharArray();
         baseline = sbuf1.toString().toCharArray();
         barline = sbuf2.toString().toCharArray();
         spaces = sbuf3.toString().toCharArray();
      }
      
      private TextGraphHelper()
      {
         // do not instantiate
      }
      
      /*
       * Make a text graph of a memory pool usage:
       * 
       * +---------------------------| committed:10Mb
       * +-------------------------------------------------+
       * |////////////////           |                     | max:20Mb
       * +-------------------------------------------------+
       * +---------------| used:3Mb
       *
       * When max is unknown assume max == committed
       * 
       * |-------------------------------------------------| committed:10Mb
       * +-------------------------------------------------+
       * |////////////////                                 | max:-1
       * +-------------------------------------------------+
       * |---------------| used:3Mb
       */      
      public static void poolUsage(StringBuffer sbuf, long used, long committed, long max)
      {
         // there is a chance that max is not provided (-1)
         long assumedMax = (max == -1) ? committed : max;
         // find out bar lengths
         int localUsed = (int)(factor * used / assumedMax);
         int localCommitted = (int)(factor * committed / assumedMax);
         int localMax = factor;

         sbuf.append("<blockquote><br>");
         sbuf.append(baseline, 0, localCommitted).append("| committed:").append(outputNumber(committed)).append("<br>");
         sbuf.append(fixedline).append("<br>");
         
         // the difficult part
         sbuf.append(barline, 0, localUsed);
         if (localUsed < localCommitted)
         {
            sbuf.append(localUsed > 0 ? '/' : '|');
            sbuf.append(spaces, 0, localCommitted - localUsed - 1);            
         }
         sbuf.append('|');
         if (localCommitted < localMax)
         {
            sbuf.append(spaces, 0, localMax - localCommitted - 1);            
            sbuf.append('|');
         }
         sbuf.append(" max:").append(outputNumber(max)).append("<br>");
         
         sbuf.append(fixedline).append("<br>");
         sbuf.append(baseline, 0, localUsed).append("| used:").append(outputNumber(used));
         sbuf.append("</blockquote>");
      }
      
      private static String outputNumber(long value)
      {     
         if (value >= GIGA)
         {
            return formatter.format((double)value / GIGA) + "Gb";
         }
         else if (value >= MEGA)
         {
            return formatter.format((double)value / MEGA) + "Mb";
         }
         else if (value >= KILO)
         {
            return formatter.format((double)value / KILO) + "Kb";
         }
         else if (value >= 0)
         {
            return value + "b";
         }
         else
         {
            return Long.toString(value);
         }
      }
   }
   
   private static class ThreadCPU implements Comparable
   {
      public String name;
      public long cpuTime;

      public ThreadCPU(String name, long cpuTime)
      {
         this.name = name;
         this.cpuTime = cpuTime / 1000000; // convert to millis
      }
      
      public int compareTo(Object o)
      {
         ThreadCPU other = (ThreadCPU) o;
         long value = cpuTime - other.cpuTime;
         if (value > 0)
            return -1;
         else if (value < 0)
            return +1;
         else
            return name.compareTo(other.name);
      }
   }
   
   /*
    * Simple data holder
    */
   private static class ThreadGroupCount
   {
      public int threads;
      public int groups;
   }
}