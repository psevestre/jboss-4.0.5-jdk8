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
package org.jboss.ejb3.test.regression.ejbthree440.session;

import java.util.Date;
import org.jboss.serial.io.MarshalledObject;
import java.io.IOException;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.RemoteBinding;

import org.jboss.ejb3.test.regression.ejbthree440.model.MyResource;
import org.jboss.ejb3.test.regression.ejbthree440.model.Resource;
import org.jboss.ejb3.test.regression.ejbthree440.model.User;
import org.jboss.ejb3.test.regression.ejbthree440.session.i.IInheritanceDemo;

@Stateless
@RemoteBinding(jndiBinding="InheritanceDemo/remote")
@Remote(IInheritanceDemo.class)
public class InheritanceDemo implements IInheritanceDemo {
  @PersistenceContext(unitName="mlog")
  protected EntityManager em;

  public void create() {
    User u = new User();
    u.setName("Test User");
    u.setPassword("acuia.sckln");
    u.setActive(false);
    em.persist(u);
    
    MyResource r = new MyResource();
    r.setUser(u);
    r.setSkills("0");
    r.setActive(false);
    r.setDescription("Inheritance Demo Resource");
    r.setMyField("hello world");
    r.setCreated(new Date());
    r.setUpdated(new Date());
    em.persist(r);
  }
  
  public Resource read() {
    Query q = em.createQuery("SELECT u FROM Resource u WHERE u.description = :d");
    q.setParameter("d", "Inheritance Demo Resource");
    Resource r = (Resource) q.getSingleResult();
    return r;
  }

   public MarshalledObject readFromMO()
   {
      try
      {
         return new MarshalledObject(read());
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }
  
  public void remove() {
    Resource r = read();
    em.remove(r);
  }
}
