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
package org.jboss.test.hibernate.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Association class for the many-to-many assocition from User to Role.
 * @author Gavin King
 */
public class UserRole implements Serializable
{
   static final long serialVersionUID = -1953830525461941982L;
   private User user;
   private Role role;
   private Calendar timeOfCreation;

   private UserRole()
   {
   }

   public UserRole(User user, Role role)
   {
      this.user = user;
      this.role = role;
      this.timeOfCreation = Calendar.getInstance();
   }

   public Role getRole()
   {
      return role;
   }

   public void setRole(Role role)
   {
      this.role = role;
   }

   public Calendar getTimeOfCreation()
   {
      return timeOfCreation;
   }

   public void setTimeOfCreation(Calendar timeOfCreation)
   {
      this.timeOfCreation = timeOfCreation;
   }

   public User getUser()
   {
      return user;
   }

   public void setUser(User user)
   {
      this.user = user;
   }

}

