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
package org.jboss.test.jaxr.scout.query;

/** Tests Jaxr capability to do business queries
 *  @author <mailto:Anil.Saldhana@jboss.org>Anil Saldhana
 *  @since  Dec 29, 2004
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.test.jaxr.scout.JaxrBaseTestCase;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.Organization;

public class JaxrBusinessQueryTestCase extends JaxrBaseTestCase
{
    protected String querystr = "JBOSS";
    
    private Key orgKey = null;
    
    protected void setUp() throws Exception
    {
       super.setUp();
       String keyid = "";
       login();
       try
       {
           getJAXREssentials();
           Collection orgs = new ArrayList();
           Organization org = createOrganization("JBOSS");

           orgs.add(org);
           BulkResponse br = blm.saveOrganizations(orgs);
           if (br.getStatus() == JAXRResponse.STATUS_SUCCESS)
           { 
               Collection coll = br.getCollection();
               Iterator iter = coll.iterator();
               while (iter.hasNext())
               {
                   Key key = (Key) iter.next();
                   keyid = key.getId(); 
                   assertNotNull(keyid);
                   orgKey = key;
               }//end while
           } else
           { 
               Collection exceptions = br.getExceptions();
               Iterator iter = exceptions.iterator();
               while (iter.hasNext())
               {
                   Exception e = (Exception) iter.next(); 
                   fail(e.toString());
               }
           }
       } catch (JAXRException e)
       { 
          e.printStackTrace();
          fail(e.getMessage());
       } 
    }
    
    protected void tearDown() throws Exception
    {
       if(orgKey != null)
         this.deleteOrganization(this.orgKey); 
       super.tearDown();
    }

    public void testBusinessQuery() throws JAXRException
    {
        searchBusiness(querystr); 
    } 
}
