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
package org.jboss.invocation.unified.interfaces;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.StreamCorruptedException;
import java.net.MalformedURLException;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import org.jboss.ha.framework.interfaces.ClusteringTargetsRepository;
import org.jboss.ha.framework.interfaces.FamilyClusterInfo;
import org.jboss.ha.framework.interfaces.GenericClusteringException;
import org.jboss.ha.framework.interfaces.HARMIResponse;
import org.jboss.ha.framework.interfaces.LoadBalancePolicy;
import org.jboss.invocation.Invocation;
import org.jboss.invocation.PayloadKey;
import org.jboss.invocation.ServiceUnavailableException;
import org.jboss.remoting.CannotConnectException;
import org.jboss.remoting.Client;
import org.jboss.remoting.InvokerLocator;
import org.jboss.remoting.serialization.IMarshalledValue;


/**
 * @author <a href="mailto:tom.elrod@jboss.com">Tom Elrod</a>
 */
public class UnifiedInvokerHAProxy extends UnifiedInvokerProxy
{

   static final long serialVersionUID = -4813929243402349966L;

   private LoadBalancePolicy loadBalancePolicy;
   private String proxyFamilyName = null;

   private FamilyClusterInfo familyClusterInfo = null;

   public static final WeakHashMap txFailoverAuthorizations = new WeakHashMap();


   public UnifiedInvokerHAProxy()
   {
      super();
      log.debug("UnifiedInvokerHAProxy constructor called with no arguments.");
      setSubSystem("invokerha");
   }

   public UnifiedInvokerHAProxy(InvokerLocator locator, boolean isStrictRMIException,
                                ArrayList targets, LoadBalancePolicy policy,
                                String proxyFamilyName, long viewId)
   {
      super(locator, isStrictRMIException);

      this.familyClusterInfo = ClusteringTargetsRepository.initTarget(proxyFamilyName, targets, viewId);
      this.loadBalancePolicy = policy;
      this.proxyFamilyName = proxyFamilyName;

      setSubSystem("invokerha");
   }

   public boolean txContextAllowsFailover(Invocation invocation)
   {
      javax.transaction.Transaction tx = invocation.getTransaction();
      if(tx != null)
      {
         synchronized(tx)
         {
            return ! txFailoverAuthorizations.containsKey(tx);
         }
      }
      else
      {
         return true;
      }
   }

   public void invocationHasReachedAServer(Invocation invocation)
   {
      javax.transaction.Transaction tx = invocation.getTransaction();
      if(tx != null)
      {
         synchronized(tx)
         {
            txFailoverAuthorizations.put(tx, null);
         }
      }
   }

   protected int totalNumberOfTargets()
   {
      if(this.familyClusterInfo != null)
      {
         return this.familyClusterInfo.getTargets().size();
      }
      else
      {
         return 0;
      }
   }

   protected void resetView()
   {
      this.familyClusterInfo.resetView();
   }

   /**
    * Gets the remoting client to call on which is selected by the load balancing policy.
    * If the target InvokerLocator selected is not for the current remoting client, a new one
    * will be initialized.
    *
    * @param invocationBasedRouting
    * @return
    * @throws MalformedURLException
    */
   protected Client getClient(Invocation invocationBasedRouting) throws MalformedURLException
   {
      Object target = loadBalancePolicy.chooseTarget(familyClusterInfo, invocationBasedRouting);
      InvokerLocator targetLocator = (InvokerLocator) target;

      // check if load balancer pick the client invoker we already have
      if(!getLocator().equals(targetLocator))
      {
         init(targetLocator);
      }
      return getClient();
   }


   /**
    * @param invocation A pointer to the invocation object
    * @return Return value of method invocation.
    * @throws Exception Failed to invoke method.
    */
   public Object invoke(Invocation invocation) throws Exception
   {
      // we give the opportunity, to any server interceptor, to know if this a
      // first invocation to a node or if it is a failovered call
      //
      int failoverCounter = 0;
      invocation.setValue("FAILOVER_COUNTER", new Integer(failoverCounter), PayloadKey.AS_IS);

      Object response = null;
      Exception lastException = null;

      boolean failoverAuthorized = true;
      while(familyClusterInfo.getTargets() != null && familyClusterInfo.getTargets().size() > 0 && failoverAuthorized)
      {
         boolean definitivlyRemoveNodeOnFailure = true;

         try
         {
            invocation.setValue("CLUSTER_VIEW_ID", new Long(this.familyClusterInfo.getCurrentViewId()));

            log.debug("Client cluster view id: " + familyClusterInfo.getCurrentViewId());
            log.debug(printPossibleTargets());

            Client clientInstance = getClient(invocation);

            log.debug("Making invocation on " + clientInstance.getInvoker().getLocator());

            response = clientInstance.invoke(invocation, null);

            HARMIResponse haResponse = null;

            if(response instanceof Exception)
            {
               log.debug("Invocation returened exception: " + response);
               if(response instanceof GenericClusteringException)
               {
                  GenericClusteringException gcex = (GenericClusteringException) response;
                  lastException = gcex;
                  // this is a generic clustering exception that contain the
                  // completion status: usefull to determine if we are authorized
                  // to re-issue a query to another node
                  //
                  if(gcex.getCompletionStatus() == GenericClusteringException.COMPLETED_NO)
                  {
                     // we don't want to remove the node from the list of failed
                     // node UNLESS there is a risk to indefinitively loop
                     //
                     if(totalNumberOfTargets() >= failoverCounter)
                     {
                        if(!gcex.isDefinitive())
                        {
                           definitivlyRemoveNodeOnFailure = false;
                        }
                     }
                     removeDeadTarget(getLocator());
                     if(!definitivlyRemoveNodeOnFailure)
                     {
                        resetView();
                     }
                     failoverAuthorized = txContextAllowsFailover(invocation);

                     failoverCounter++;
                     invocation.setValue("FAILOVER_COUNTER", new Integer(failoverCounter), PayloadKey.AS_IS);

                     log.debug("Received GenericClusteringException where request was not completed.  Will retry.");

                     continue;
                  }
                  else
                  {
                     invocationHasReachedAServer(invocation);
                     throw new ServerException("Clustering error", gcex);
                  }
               }
               else
               {
                  throw ((Exception) response);
               }
            }
            if (response instanceof IMarshalledValue)
            {
                haResponse = (HARMIResponse) ((IMarshalledValue) response).get();
            }
            else
            if(response instanceof MarshalledObject)
            {
               haResponse = (HARMIResponse) ((MarshalledObject) response).get();
            }
            else
            {
               haResponse = (HARMIResponse) response;
            }

            // check for clustered targets
            if(haResponse.newReplicants != null)
            {
               updateClusterInfo(haResponse.newReplicants, haResponse.currentViewId);
            }

            response = haResponse.response;
            return response;

         }
         catch(CannotConnectException cncEx)
         {
            log.debug("Invocation failed: CannotConnectException - " + cncEx, cncEx);
            removeDeadTarget(getLocator());
            resetView();
            failoverAuthorized = txContextAllowsFailover(invocation);

            failoverCounter++;
            invocation.setValue("FAILOVER_COUNTER", new Integer(failoverCounter), PayloadKey.AS_IS);
         }
         catch(GenericClusteringException gcex)
         {
            lastException = gcex;
            // this is a generic clustering exception that contain the
            // completion status: usefull to determine if we are authorized
            // to re-issue a query to another node
            //
            if(gcex.getCompletionStatus() == GenericClusteringException.COMPLETED_NO)
            {
               // we don't want to remove the node from the list of failed
               // node UNLESS there is a risk to indefinitively loop
               //
               if(totalNumberOfTargets() >= failoverCounter)
               {
                  if(!gcex.isDefinitive())
                  {
                     definitivlyRemoveNodeOnFailure = false;
                  }
               }
               removeDeadTarget(getLocator());
               if(!definitivlyRemoveNodeOnFailure)
               {
                  resetView();
               }
               failoverAuthorized = txContextAllowsFailover(invocation);

               failoverCounter++;
               invocation.setValue("FAILOVER_COUNTER", new Integer(failoverCounter), PayloadKey.AS_IS);

               log.debug("Received GenericClusteringException where request was not completed.  Will retry.");
            }
            else
            {
               invocationHasReachedAServer(invocation);
               throw new ServerException("Clustering error", gcex);
            }
         }
         catch(RemoteException aex)
         {
            log.debug("Invocation failed: RemoteException - " + aex, aex);

            // per Jira issue JBREM-61
            if(isStrictRMIException())
            {
               throw new ServerException(aex.getMessage(), aex);
            }
            else
            {
               throw aex;
            }
         }
         catch(Throwable throwable)
         {
            log.debug("Invocation failed: " + throwable, throwable);

            // this is somewhat of a hack as remoting throws throwable,
            // so will let Exception types bubble up, but if Throwable type,
            // then have to wrap in new Exception, as this is the signature
            // of this invoke method.
            if(throwable instanceof Exception)
            {
               throw (Exception) throwable;
            }
            throw new Exception(throwable);
         }
      }

      if(failoverAuthorized == false)
      {
         throw new ServiceUnavailableException("Service unavailable (failover not possible inside a user transaction) for " +
                                               invocation.getObjectName() + " calling method " + invocation.getMethod(),
                                               lastException);
      }
      else
      {
         throw new ServiceUnavailableException("Service unavailable for " +
                                               invocation.getObjectName() + " calling method " + invocation.getMethod(),
                                               lastException);
      }
   }

   private Object printPossibleTargets()
   {
      StringBuffer buffer = new StringBuffer();
      if(familyClusterInfo != null)
      {
         List possibleTargets = familyClusterInfo.getTargets();
         if(possibleTargets != null && possibleTargets.size() > 0)
         {
            for(int x = 0; x < possibleTargets.size(); x++)
            {
               buffer.append("\nPossible target " + (x + 1) + ": " + possibleTargets.get(x));
            }
         }
      }
      return buffer.toString();
   }

   private void removeDeadTarget(InvokerLocator locator)
   {
      if(locator != null)
      {
         if(this.familyClusterInfo != null)
         {
            familyClusterInfo.removeDeadTarget(locator);
            log.debug("Removed " + locator + " from target list.");
         }
      }
   }


   private void updateClusterInfo(ArrayList newReplicants, long currentViewId)
   {
      if(familyClusterInfo != null)
      {
         familyClusterInfo.updateClusterInfo(newReplicants, currentViewId);
         log.debug("Updating cluster info.  New view id: " + currentViewId);
         log.debug("New cluster target list is:");
         for(int x = 0; x < newReplicants.size(); x++)
         {
            log.debug(newReplicants.get(x));
         }
      }
   }

   /**
    * Externalize this instance and handle obtaining the remoteInvoker stub
    */
   public void writeExternal(final ObjectOutput out)
         throws IOException
   {
      out.writeInt(CURRENT_VERSION);

      out.writeUTF(getLocator().getOriginalURI());
      out.writeBoolean(isStrictRMIException()); 
      // JBAS-2071 - sync on FCI to ensure targets and vid are consistent
      ArrayList targets = null;
      long vid = 0;
      synchronized (this.familyClusterInfo)
      {
         targets = this.familyClusterInfo.getTargets ();
         vid = this.familyClusterInfo.getCurrentViewId ();
      }
      out.writeObject(targets);
      out.writeObject(this.loadBalancePolicy);
      out.writeObject(this.proxyFamilyName);
      out.writeLong(vid);
   }

   /**
    * Un-externalize this instance.
    */
   public void readExternal(final ObjectInput in)
         throws IOException, ClassNotFoundException
   {
      int version = in.readInt();
      // Read in and map the version of the serialized data seen
      switch(version)
      {
         case VERSION_5_0:
            setLocator(new InvokerLocator(in.readUTF()));
            setStrictRMIException(in.readBoolean());
            init(getLocator());

            ArrayList targets = (ArrayList) in.readObject();
            this.loadBalancePolicy = (LoadBalancePolicy) in.readObject();
            this.proxyFamilyName = (String) in.readObject();
            long vid = in.readLong();

            // keep a reference on our family object
            //
            this.familyClusterInfo = ClusteringTargetsRepository.initTarget(this.proxyFamilyName, targets, vid);

            break;
         default:
            throw new StreamCorruptedException("Unknown version seen: " + version);
      }
   }


}