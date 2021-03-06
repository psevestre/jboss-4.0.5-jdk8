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
package org.jboss.ha.framework.server;

import org.jboss.logging.Logger;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;

/**
 * Utility class that accepts objects into a queue and maintains a separate
 * thread that reads them off the queue and passes them to a registered
 * "processor".
 * 
 * @todo find a better home for this than the cluster module
 * 
 * @author <a href="mailto://brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
class AsynchEventHandler implements Runnable
{
   /**
    * Interface implemented by classes able to process the objects
    * placed into an AsynchEventHandler's queue.
    */
   public static interface AsynchEventProcessor
   {
      public void processEvent(Object event);
   }
   
   private String name;
   /** The LinkedQueue of events to pass to our processor */
   private LinkedQueue events = new LinkedQueue();
   /** Whether we're blocking on the queue */
   private boolean blocking;
   private AsynchEventProcessor processor;
   private boolean stopped = true;
   private Thread  handlerThread;
   private Logger log;
   
   /**
    * Create a new AsynchEventHandler.
    * 
    * @param processor  object to which objects placed in the queue should
    *                   be handed when dequeued
    * @param name       name for this instance.  Appended to the processor's
    *                   class name to create a log category, and used
    *                   to name to handler thread
    */
   public AsynchEventHandler(AsynchEventProcessor processor, String name)
   {
      super();
      this.processor = processor;
      if (name == null)
         name = "AsynchEventHandler";
      this.name = name;
      this.log = Logger.getLogger(processor.getClass().getName() + "." + name);
   }
   
   /**
    * Place the given object in the queue.
    * 
    * @param event  the object to asynchronously pass to the 
    *               AsynchEventHandler.
    *               
    * @throws InterruptedException  if the thread is interrupted while blocking
    *                               on the queue.
    */
   public void queueEvent(Object event) throws InterruptedException
   {
      if (event != null)
         events.put(event);
   }
   
   public void run()
   {
      log.debug("Begin " + name + " Thread");
      stopped = false;
      while( !stopped )
      {
         try
         {
            blocking = true;
            Object event = events.take();
            blocking = false;
            
            if (!stopped) 
            {
               processor.processEvent(event);
            }
         }
         catch(InterruptedException e)
         {
            blocking = false;
            log.debug(name + " Thread interrupted", e);               
            if (stopped)
               break;
         }
         catch (Throwable t)
         {
            log.error("Caught Throwable handling asynch events", t);
         }
      }
      log.debug("End " + name + " Thread");
   }
   
   /**
    * Starts the handler thread.
    */
   public void start()
   {
      handlerThread = new Thread(this, name + " Thread");
      handlerThread.start();
   }
   
   /**
    * Stops the handler thread.
    */
   public void stop()
   {
      stopped = true;
      if (blocking)
         handlerThread.interrupt(); // it's just waiting on the LinkedQueue
      
      if (handlerThread.isAlive()) {
         // Give it up to 100ms to finish whatever it's doing
         try
         {
            handlerThread.join(100);
         }
         catch (Exception ignored) {}
      }
      
      if (handlerThread.isAlive())
         handlerThread.interrupt(); // kill it
   }
   
   public boolean isStopped()
   {
      return stopped;
   }

}
