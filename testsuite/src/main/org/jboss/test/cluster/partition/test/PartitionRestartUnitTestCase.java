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
package org.jboss.test.cluster.partition.test;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.jboss.test.JBossClusteredTestCase;
import org.jboss.test.testbean.interfaces.StatefulSessionHome;
import org.jboss.test.testbeancluster.interfaces.NodeAnswer;
import org.jboss.test.testbeancluster.interfaces.StatefulSession;

/**
 * A PartitionRestartUnitTestCase.
 * 
 * @author <a href="mailto://brian.stansberry@jboss.com">Brian Stansberry</a>
 * @version $Revision$
 */
public class PartitionRestartUnitTestCase extends JBossClusteredTestCase
{
   /**
    * Create a new PartitionRestartUnitTestCase.
    * 
    * @param name
    */
   public PartitionRestartUnitTestCase(String name)
   {
      super(name);
   }         

   public static Test suite() throws Exception
   {
      Test t1 = JBossClusteredTestCase.getDeploySetup(PartitionRestartUnitTestCase.class, "testbeancluster.jar");
      return t1;
   }   
   
   public void testStatefulBeanFailover() 
   throws Exception
   {       
      getLog().debug("testStatefulBeanFailover");
      
      RMIAdaptor[] adaptors = this.getAdaptors(); 
      
      Context ctx = new InitialContext();
      getLog().debug("OK");
      
      getLog().debug("");
      getLog().debug("Looking up the home nextgen.StatefulSession...");
      StatefulSessionHome  statefulSessionHome =
      (StatefulSessionHome) ctx.lookup("nextgen_StatefulSession");
      if (statefulSessionHome!= null ) getLog().debug("ok");
         getLog().debug("Calling create on StatefulSessionHome...");
      StatefulSession statefulSession =
      (StatefulSession)statefulSessionHome.create("Bupple-Dupple");
      assertTrue("statefulSessionHome.create() != null", statefulSession != null);
      getLog().debug("ok");
      
      NodeAnswer node1 = statefulSession.getNodeState ();
      getLog ().debug (node1);
      
      // Now we switch to the other node, simulating a failure on node 1
      //
      System.setProperty ("JBossCluster-DoFail", "once");
      NodeAnswer node2 = statefulSession.getNodeState ();      
      getLog ().debug (node2);
      
      assertTrue ("Failover has occured", !node1.nodeId.equals (node2.nodeId));
      
      assertTrue ("Value is identical on replicated node", 
                  "Bupple-Dupple".equals (node1.answer) &&
                  node1.answer.equals (node2.answer) );

      // Stop and restart the ClusterPartition on node1
      restartPartition(adaptors[0]);
      
      // Let the cluster stabilize
      sleep(2000);
      
      // we change our name to see if it replicates to node 1
      //
      statefulSession.setName ("Changed");
      
      // now we travel back to node 1
      System.setProperty ("JBossCluster-DoFail", "once");
      node1 = statefulSession.getNodeState ();
      getLog ().debug (node1);
      
      assertTrue ("Failover has occured", !node1.nodeId.equals (node2.nodeId));
      
      assertTrue ("Value is identical on replicated node", "Changed".equals (node1.answer) );      
      
      statefulSession.remove();
      getLog().debug("ok");
   }
   
   
   protected void restartPartition(RMIAdaptor adaptor) throws Exception
   {
      ObjectName partition = new ObjectName("jboss:service=DefaultPartition");
      
      Object[] params = new Object[0];
      String[] types = new String[0];
      adaptor.invoke(partition, "stop", params, types);
      adaptor.invoke(partition, "start", params, types);
   }

}
