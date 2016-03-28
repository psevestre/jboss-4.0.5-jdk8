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
package org.jboss.test.web.test;

//import java.net.HttpURLConnection;
import java.net.URL;
//import javax.management.ObjectName;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.JBossTestCase;
import org.jboss.test.JBossTestSetup;
import org.jboss.test.util.web.HttpUtils;
//import org.apache.commons.httpclient.HttpMethodBase;
//import org.apache.commons.httpclient.Header;

/** Tests of JSF integration into the JBoss server. This test
 requires than a web container and JSF implementation be integrated 
 into the JBoss server. The tests currently do NOT use the 
 java.net.HttpURLConnection and associated http client and these do 
 not return valid HTTP error codes so if a failure occurs it is best 
 to connect the webserver using a browser to look for additional error
 info. 
 
 @author Stan.Silvert@jboss.org
 @version $Revision: 57211 $
 */
public class JSFIntegrationUnitTestCase extends JBossTestCase
{
   private static String REALM = "JBossTest Servlets";
   private String baseURL = HttpUtils.getBaseURL(); 
   private String baseURLNoAuth = HttpUtils.getBaseURLNoAuth(); 
   
   public JSFIntegrationUnitTestCase(String name)
   {
      super(name);
   }
   
   /** Access the http://localhost/jbosstest/index.faces to make
    * sure that JSF and its tag libraries are integrated.
    */
   public void testJSFIntegrated() throws Exception
   {
      URL url = new URL(baseURL+"jbosstest-jsf/index.faces");
      HttpUtils.accessURL(url);
   }   

   public static Test suite() throws Exception
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(JSFIntegrationUnitTestCase.class));

      // Create an initializer for the test suite
      Test wrapper = new JBossTestSetup(suite)
      {
         protected void setUp() throws Exception
         {
            super.setUp();
            deploy("jbosstest-jsf.war");           
         }
         protected void tearDown() throws Exception
         {
            undeploy("jbosstest-jsf.war");
            super.tearDown();            
         }
      };
      return wrapper;
   }
   

}
