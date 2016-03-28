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
package org.jboss.ejb3.test.clusteredentity;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jboss.cache.TreeCacheMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jgroups.View;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57207 $
 */
@Stateless
@Remote(EntityTest.class)
public class EntityTestBean implements EntityTest
{
   @PersistenceContext
   private EntityManager manager;
   
   static MyListener listener;

   public EntityTestBean()
   {
      try
      {
         //Just to initialise the cache with a listener
         TreeCache cache = getCache();
         if (listener == null)
         {
            listener = new MyListener();
            cache.addTreeCacheListener(listener);
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public Customer createCustomer()
   {
      System.out.println("CREATE CUSTOMER");
      try
      {
         listener.clear();
         
         Customer customer = new Customer();
         customer.setId(1);
         customer.setName("JBoss");
         Set<Contact> contacts = new HashSet<Contact>();
         
         Contact kabir = new Contact();
         kabir.setId(1);
         kabir.setCustomer(customer);
         kabir.setName("Kabir");
         kabir.setTlf("1111");
         contacts.add(kabir);
         
         Contact bill = new Contact();
         bill.setId(2);
         bill.setCustomer(customer);
         bill.setName("Bill");
         bill.setTlf("2222");
         contacts.add(bill);

         customer.setContacts(contacts);

         manager.persist(customer);
         return customer;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e); // TODO Auto-generated catch block
      }
      finally
      {
         System.out.println("CREATE CUSTOMER -  END");         
      }
   }

   public Customer findByCustomerId(Integer id)
   {
      System.out.println("FIND CUSTOMER");         
      listener.clear();
      try
      {
         Customer customer = manager.find(Customer.class, id);
         
         return customer;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e); // TODO Auto-generated catch block
      }
      finally
      {
         System.out.println("FIND CUSTOMER -  END");         
      }
   }
   
   public void loadedFromCache()
   {
      System.out.println("CHECK CACHE");         
      try
      {
         System.out.println("Visited: " + listener.visited);
         if (!listener.visited.contains("Customer#1"))
            throw new RuntimeException("Customer#1 was not in cache");
         if (!listener.visited.contains("Contact#1"))
            throw new RuntimeException("Contact#1 was not in cache");
         if (!listener.visited.contains("Contact#2"))
            throw new RuntimeException("Contact2#1 was not in cache");
         if (!listener.visited.contains("Customer.contacts#1"))
            throw new RuntimeException("Customer.contacts#1 was not in cache");
      }
      finally
      {
         System.out.println("CHECK CACHE -  END");         
      }
      
   }

   private TreeCache getCache() throws Exception
   {
      MBeanServer server = MBeanServerLocator.locateJBoss();
      TreeCacheMBean proxy = (TreeCacheMBean)MBeanProxyExt.create(TreeCacheMBean.class, new ObjectName("jboss.cache:service=EJB3EntityTreeCache"), server);
      TreeCache cache = proxy.getInstance();
      
      return cache;
   }

   class MyListener implements TreeCacheListener
   {
      HashSet visited = new HashSet(); 
      
      public void clear()
      {
         visited.clear();
      }
      
      public void cacheStarted(TreeCache cache)
      {
      }

      public void cacheStopped(TreeCache cache)
      {
      }

      public void nodeCreated(Fqn fqn)
      {
      }

      public void nodeEvicted(Fqn fqn)
      {
      }

      public void nodeLoaded(Fqn fqn)
      {
      }

      public void nodeModified(Fqn fqn)
      {
      }

      public void nodeRemoved(Fqn fqn)
      {
      }

      public void nodeVisited(Fqn fqn)
      {
         System.out.println("MyListener - Visiting node " + fqn.toString());
         String name = fqn.toString();
         String token = ".clusteredentity.";
         int index = name.indexOf(token);
         index += token.length();
         name = name.substring(index);
         System.out.println(name);
         visited.add(name);
      }

      public void viewChange(View new_view)
      {
      }
   }
}
