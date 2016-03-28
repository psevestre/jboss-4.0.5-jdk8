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
package org.jboss.ha.framework.interfaces;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.jboss.invocation.MarshalledInvocation;
import org.jboss.logging.Logger;

/**
 *
 *
 *   @author <a href="mailto:sacha.labourey@cogito-info.ch">Sacha Labourey</a>
 *   @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 *   @version $Revision: 57188 $
 */
public class HARMIClient
   implements HARMIProxy, java.lang.reflect.InvocationHandler, java.io.Serializable
{
   // Constants -----------------------------------------------------
   /** The serialVersionUID
    * @since
    */
   private static final long serialVersionUID = -1227816478666532463L;
   private static final Logger log = Logger.getLogger(HARMIClient.class);

   /** {@link Object#toString} method reference. */
   protected static final Method TO_STRING;

   /** {@link Object#hashCode} method reference. */
   protected static final Method HASH_CODE;

   /** {@link Object#equals} method reference. */
   protected static final Method EQUALS;

   static
   {
      try
      {
         final Class[] empty = {};
         final Class type = Object.class;

         TO_STRING = type.getMethod("toString", empty);
         HASH_CODE = type.getMethod("hashCode", empty);
         EQUALS = type.getMethod("equals", new Class[] { type });
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new ExceptionInInitializerError(e);
      }
   }

   // Attributes ----------------------------------------------------

   protected String key = null;
   //protected ArrayList targets = null;
   protected LoadBalancePolicy loadBalancePolicy;
   //protected transient long currentViewId = 0;
   protected transient Object local = null;
   protected transient boolean trace;
   FamilyClusterInfo familyClusterInfo = null;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public HARMIClient() {}

   public HARMIClient(ArrayList targets, LoadBalancePolicy policy, String key)
   {
      this(targets, 0, policy, key, null);
   }

   public HARMIClient(ArrayList targets,
                       long initViewId,
                       LoadBalancePolicy policy,
                       String key,
                       Object local)
   {
      this.familyClusterInfo = ClusteringTargetsRepository.initTarget (key, targets, initViewId);

      //this.targets = targets;
      this.loadBalancePolicy = policy;
      this.loadBalancePolicy.init(this);
      this.key = key;
      this.local = local;
      this.trace = log.isTraceEnabled();
      if( trace )
         log.trace("Init, cluterInfo: "+familyClusterInfo+", policy="+loadBalancePolicy);
   }

   // Public --------------------------------------------------------
   /*
   public ArrayList getTargets()
   {
      return targets;
   }
   
   public void setTargets(ArrayList newTargets)
   {
      synchronized(targets)
      {
         targets.clear();
         targets.addAll(newTargets);
      }
   }
   */
   public void updateClusterInfo (ArrayList targets, long viewId)
   {
      if (familyClusterInfo != null)
         this.familyClusterInfo.updateClusterInfo (targets, viewId);
   }

   public Object getRemoteTarget()
   {
      //      System.out.println("number of targets: " + targets.size());
      return loadBalancePolicy.chooseTarget(this.familyClusterInfo, null); // legacy, no Invocation object in raw HA-RMI
   }

   public void remoteTargetHasFailed(Object target)
   {
      removeDeadTarget(target);
   }


   public Method findLocalMethod(Method method, Object[] args) throws Exception
   {
      return method;
   }


   public Object invokeRemote(Object proxy, Method method, Object[] args) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      HARMIServer target = (HARMIServer)getRemoteTarget();
      while (target != null)
      {
         Exception lastException = null;
         try
         {
            if( trace )
               log.trace("Invoking on target="+target);
            MarshalledInvocation mi = new MarshalledInvocation(null, method, args, null, null, null);
            mi.setObjectName (""); //FIXME: Fake value! Bill's optimisations regarding MI make the hypothesis
                                   // that ObjectName is always here otherwise the writeExternal code of MI
                                   // "out.writeInt(payload.size() - 3);" is wrong
            HARMIResponse rsp = target.invoke(this.familyClusterInfo.getCurrentViewId (), mi);
            if (rsp.newReplicants != null)
            {
               if( trace )
               {
                  log.trace("newReplicants: "+rsp.newReplicants);
               }
               updateClusterInfo (rsp.newReplicants, rsp.currentViewId);
               //setTargets(rsp.newReplicants);
               //currentViewId = rsp.currentViewId;
            }

            return rsp.response;
         }
         catch (java.rmi.ConnectException e)
         {
            lastException = e;
         }
         catch (java.rmi.ConnectIOException e)
         {
            lastException = e;
         }
         catch (java.rmi.NoSuchObjectException e)
         {
            lastException = e;
         }
         catch (java.rmi.UnmarshalException e)
         {
            lastException = e;
         }
         catch (java.rmi.UnknownHostException e)
         {
            lastException = e;
         }
         if( trace )
            log.trace("Invoke failed, target="+target, lastException);
         // If we reach here, this means that we must fail-over
         remoteTargetHasFailed(target);
         target = (HARMIServer)getRemoteTarget();
      }
      // if we get here this means list was exhausted
      throw new java.rmi.RemoteException("Service unavailable.");

   }

   // HARMIProxy implementation ----------------------------------------------

   public boolean isLocal()
   {
      return local != null;
   }

   // InvocationHandler implementation ----------------------------------------------   

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      // The isLocal call is handled by the proxy
      String name = method.getName();
      if (method.equals(TO_STRING))
      {
         StringBuffer tmp = new StringBuffer(super.toString());
         tmp.append('(');
         tmp.append(familyClusterInfo);
         tmp.append(')');
         return tmp.toString();
      }
      else if (name.equals("equals"))
      {
         return method.invoke(this, args);
      }
      else if (name.equals("hashCode"))
      {
         return method.invoke(this, args);
      }
      else if (name.equals("isLocal") && (args == null || args.length == 0))
      {
         return method.invoke(this, args);
      }

      // we try to optimize the call locally first
      //
      if (local != null)
      {
         try
         {
            Method localMethod = findLocalMethod(method, args);
            return localMethod.invoke(local, args);
         }
         catch (java.lang.reflect.InvocationTargetException ite)
         {
            throw ite.getTargetException();
         }
      }
      else
      {
         return invokeRemote(null, method, args);
      }
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   protected void removeDeadTarget(Object target)
   {
      //System.out.println("Size before : " + Integer.toString(targets.length));
      if (this.familyClusterInfo != null)
         this.familyClusterInfo.removeDeadTarget (target);
   }

   // Private -------------------------------------------------------

   private void readObject (ObjectInputStream stream)
      throws IOException, ClassNotFoundException
   {
      this.key = stream.readUTF();
      ArrayList targets = (ArrayList)stream.readObject();
      long vid = stream.readLong ();
      this.loadBalancePolicy = (LoadBalancePolicy)stream.readObject();
      HARMIServer server = (HARMIServer)HARMIServer.rmiServers.get(key);

      // keep a reference on our family object
      //
      this.familyClusterInfo = ClusteringTargetsRepository.initTarget (this.key, targets, vid);

      this.loadBalancePolicy.init(this);

      if (server != null)
      {
         synchronized (targets)
         {
            try
            {
               targets = (ArrayList)server.getReplicants();
               local = server.getLocal();
            }
            catch (Exception ignored)
            {}
         }
      }
      this.trace = log.isTraceEnabled();
      if( trace )
         log.trace("Init, clusterInfo: "+familyClusterInfo+", policy="+loadBalancePolicy);
   }
   private void writeObject (ObjectOutputStream stream)
      throws IOException
   {
      // JBAS-2071 - sync on FCI to ensure targets and vid are consistent
      ArrayList currentTargets = null;
      long vid = 0;
      synchronized (this.familyClusterInfo)
      {
         currentTargets = this.familyClusterInfo.getTargets();
         vid = this.familyClusterInfo.getCurrentViewId ();
      }
      stream.writeUTF(key);
      stream.writeObject(currentTargets);
      stream.writeLong(vid);
      stream.writeObject(loadBalancePolicy);

   }

}
