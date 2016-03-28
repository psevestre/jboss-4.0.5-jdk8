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
package org.jboss.tutorial.entity.security.bean;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.security.SecurityDomain;
import org.jboss.annotation.ejb.AspectDomain;
import org.jboss.tutorial.entity.security.bean.AllEntity;
import org.jboss.tutorial.entity.security.bean.SomeEntity;
import org.jboss.tutorial.entity.security.bean.StarEntity;
import org.jboss.tutorial.entity.security.bean.Stateless;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 57207 $
 */
@javax.ejb.Stateless
@Remote (Stateless.class)
@SecurityDomain ("other")
@AspectDomain("JACC Stateless Bean")
public class StatelessBean implements Stateless
{
   @PersistenceContext
   EntityManager em;

   @PermitAll
   public int unchecked(int i)
   {
      System.out.println("stateless unchecked");
      return i;
   }

   @RolesAllowed ("allowed")
   public int checked(int i)
   {
      System.out.println("stateless checked");
      return i;
   }

   @PermitAll
   public AllEntity insertAllEntity()
   {
      AllEntity e = new AllEntity();
      e.val = "x";
      em.persist(e);
      return e;
   }

   @PermitAll
   public AllEntity readAllEntity(int key)
   {
      AllEntity e = em.find(AllEntity.class, key);
      return e;
   }

   @PermitAll
   public void updateAllEntity(AllEntity e)
   {
      em.merge(e);
   }

   @PermitAll
   public void deleteAllEntity(AllEntity e)
   {
      em.remove(em.find(AllEntity.class, e.id));
   }

   @PermitAll
   public StarEntity insertStarEntity()
   {
      StarEntity e = new StarEntity();
      e.val = "x";
      em.persist(e);
      return e;
   }

   @PermitAll
   public StarEntity readStarEntity(int key)
   {
      StarEntity e = em.find(StarEntity.class, key);
      return e;
   }

   @PermitAll
   public void updateStarEntity(StarEntity e)
   {
      em.merge(e);
   }

   @PermitAll
   public void deleteStarEntity(StarEntity e)
   {
      em.remove(em.find(StarEntity.class, e.id));
   }


   @PermitAll
   public SomeEntity insertSomeEntity()
   {
      SomeEntity e = new SomeEntity();
      e.val = "x";
      em.persist(e);
      return e;
   }

   @PermitAll
   public SomeEntity readSomeEntity(int key)
   {
      SomeEntity e = em.find(SomeEntity.class, key);
      return e;
   }

   @PermitAll
   public void updateSomeEntity(SomeEntity e)
   {
      em.merge(e);
   }

   @PermitAll
   public void deleteSomeEntity(SomeEntity e)
   {
      em.remove(em.find(SomeEntity.class, e.id));
   }
}
