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
package org.jboss.ejb.txtimer;

// $Id: DatabasePersistencePolicy.java 57209 2006-09-26 12:21:57Z dimitris@jboss.org $

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TimerService;
import javax.management.ObjectName;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.ejb.ContainerMBean;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.tm.TransactionManagerLocator;

/**
 * This service implements a PersistencePolicy that persistes the timer to a
 * database.
 *
 * @author Thomas.Diesler@jboss.org
 * @author Scott.Stark@jboss.org
 * @author Dimitris.Andreadis@jboss.org
 * @version $Revision: 57209 $
 * @since 09-Sep-2004
 */
public class DatabasePersistencePolicy extends ServiceMBeanSupport
   implements DatabasePersistencePolicyMBean
{
   // logging support
   private static Logger log = Logger.getLogger(DatabasePersistencePolicy.class);

   // The persistence plugin
   private DatabasePersistencePlugin dbpPlugin;

   // The service attributes
   private ObjectName dataSource;
   private String dbpPluginClassName;

   // The transaction manager, to suspend the current Tx during delete
   private TransactionManager tm;
   
   // The persisted timers seen on startup
   private List timersToRestore;

   /**
    * Initializes this service.
    */
   public void startService() throws Exception
   {
      tm = TransactionManagerLocator.getInstance().locate();

      // Get the persistence plugin
      if (dbpPluginClassName != null)
      {
         Class dbpPolicyClass = Thread.currentThread().getContextClassLoader().loadClass(dbpPluginClassName);
         dbpPlugin = (DatabasePersistencePlugin)dbpPolicyClass.newInstance();
      }
      else
      {
         dbpPlugin = new GeneralPurposeDatabasePersistencePlugin();
      }

      // init the plugin
      dbpPlugin.init(server, dataSource);

      // create the table if needed
      dbpPlugin.createTableIfNotExists();
   }

   /**
    * Creates the timer in  persistent storage.
    *
    * @param timerId          The timer id
    * @param timedObjectId    The timed object id
    * @param firstEvent       The point in time at which the first txtimer expiration must occur.
    * @param intervalDuration The number of milliseconds that must elapse between txtimer expiration notifications.
    * @param info             A serializable handback object.
    */
   public void insertTimer(String timerId, TimedObjectId timedObjectId, Date firstEvent, long intervalDuration, Serializable info)
   {
      try
      {
         dbpPlugin.insertTimer(timerId, timedObjectId, firstEvent, intervalDuration, info);
      }
      catch (SQLException e)
      {
         RuntimeException ex = new IllegalStateException("Unable to persist timer");
         ex.initCause(e);
         throw ex;
      }
   }

   /**
    * Removes the timer from persistent storage.
    *
    * @param timerId The timer id
    */
   public void deleteTimer(String timerId, TimedObjectId timedObjectId)
   {
      // suspend the Tx before we get the con, because you cannot get a connection on an already commited Tx
      Transaction threadTx = suspendTransaction();

      try
      {
         dbpPlugin.deleteTimer(timerId, timedObjectId);
      }
      catch (SQLException e)
      {
         log.warn("Unable to delete timer", e);
      }
      finally
      {
         // resume the Tx
         resumeTransaction(threadTx);
      }
   }

   /**
    * List the persisted timer handles for a particular container
    *
    * @param containerId The Container ObjectName
    * @param loader The ClassLoader to use for loading the handles
    * @return a list of TimerHandleImpl objects
    */
   public List listTimerHandles(ObjectName containerId, ClassLoader loader)
   {
      List list = new ArrayList();

      ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
      try
      {
         if (loader != null)
         {
            Thread.currentThread().setContextClassLoader(loader);
         }
         list.addAll(dbpPlugin.selectTimers(containerId));
      }
      catch (SQLException e)
      {
         log.warn("Unable to get timer handles for containerId: " + containerId, e);         
      }
      finally
      {
         // restore class loader
         Thread.currentThread().setContextClassLoader(oldCl);
      }
      return list;
   }
   
   /**
    * Return a List of TimerHandle objects.
    */
   public List listTimerHandles()
   {
      List list = new ArrayList();
      try
      {
         list.addAll(dbpPlugin.selectTimers(null));
      }
      catch (SQLException e)
      {
         log.warn("Unable to get timer handles", e);
      }
      return list;
   }
   
   /**
    * Restore the persistent timers seen during service startup
    */
   public void restoreTimers()
   {
      if (timersToRestore != null && timersToRestore.size() > 0)
      {
         log.debug("Restoring " + timersToRestore.size() + " timer(s)");

         // recreate the timers
         for (int i = 0; i < timersToRestore.size(); i++)
         {
            TimerHandleImpl handle = (TimerHandleImpl)timersToRestore.get(i);

            try
            {
               TimedObjectId targetId = handle.getTimedObjectId();
               ObjectName containerName = targetId.getContainerId();
               ContainerMBean container = (ContainerMBean)MBeanProxyExt.create(ContainerMBean.class, containerName, server);
               TimerService timerService = container.getTimerService(targetId.getInstancePk());
               timerService.createTimer(handle.getFirstTime(), handle.getPeriode(), handle.getInfo());
            }
            catch (Exception e)
            {
               log.warn("Unable to restore timer record: " + handle);
            }
         }
         timersToRestore.clear();
      }
   }

   /**
    * Delete all persisted timers
    */
   public void clearTimers()
   {
      try
      {
         dbpPlugin.clearTimers();
      }
      catch (SQLException e)
      {
         log.warn("Unable to clear timers", e);
      }
   }

   /** Re-read the current persistent timers list, clear the db of timers,
    * and restore the timers.
    * 
    * @jmx.managed-operation 
    */ 
   public void resetAndRestoreTimers() throws SQLException
   {
      timersToRestore = dbpPlugin.selectTimers(null);
      log.debug("Found " + timersToRestore.size() + " timer(s)");
      if (timersToRestore.size() > 0)
      {
         // delete all timers
         clearTimers();
      }
      restoreTimers();
   }

   // MBean attributes *************************************************************************************************\

   /**
    * @jmx.managed-attribute
    */
   public ObjectName getDataSource()
   {
      return dataSource;
   }

   /**
    * @jmx.managed-attribute
    */
   public void setDataSource(ObjectName dataSource)
   {
      this.dataSource = dataSource;
   }

   /**
    * @jmx.managed-attribute
    */
   public String getDatabasePersistencePlugin()
   {
      return dbpPluginClassName;
   }

   /**
    * @jmx.managed-attribute
    */
   public void setDatabasePersistencePlugin(String dbpPluginClass)
   {
      this.dbpPluginClassName = dbpPluginClass;
   }
   // private **********************************************************************************************************

   private Transaction suspendTransaction()
   {
      Transaction threadTx = null;
      try
      {
         threadTx = tm.suspend();
      }
      catch (SystemException e)
      {
         log.warn("Cannot suspend Tx: " + e.toString());
      }
      return threadTx;
   }

   private void resumeTransaction(Transaction threadTx)
   {
      try
      {
         if (threadTx != null)
            tm.resume(threadTx);
      }
      catch (Exception e)
      {
         log.warn("Cannot resume Tx: " + e.toString());
      }
   }
}

