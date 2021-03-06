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
package org.jboss.test.cluster.test;

import junit.framework.Test;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.test.JBossClusteredTestCase;

import java.util.Random;

import EDU.oswego.cs.dl.util.concurrent.Semaphore;

/**
 * Simple clustering test case of get/set. It is attribute based granularity with concurrent access.
 *
 * @author Ben Wang
 * @version $Revision: 1.0
 */
public class AttributeBasedConcurrentTestCase
      extends SessionBasedConcurrentTestCase
{
   public AttributeBasedConcurrentTestCase(String name)
   {
      super(name);
      setURLName_ = "/http-scoped-attr/testsessionreplication.jsp";
      getURLName_ = "/http-scoped-attr/getattribute.jsp";
   }

   // Use different war file
   public static Test suite() throws Exception
   {
      Test t1 = JBossClusteredTestCase.getDeploySetup(AttributeBasedConcurrentTestCase.class,
            "http-scoped-attr.war");
      return t1;
   }
}
