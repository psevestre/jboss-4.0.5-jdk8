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
package org.jboss.test.cts.keys;

import java.io.Serializable;

/**
 * Class AccountPK
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57211 $
 */
public class AccountPK
   implements Serializable
{
   static final long serialVersionUID = -8235667863328981860L;
   public String key;

   /**
    * Constructor AccountPK
    *
    *
    * @param key
    *
    */
   public AccountPK (String key)
   {
      this.key = key;
   }

   public String getKey ()
   {
      return this.key;
   }
   public void setKey(String key)
   {
      this.key = key;
   }

   /**
    * Method equals
    *
    *
    * @return
    *
    */
   public boolean equals( Object obj )
   { 
       Class cl = obj.getClass( );
       AccountPK pk = (AccountPK)obj;
       return ( (cl.isInstance(this)) && (this.key.trim().compareTo(pk.getKey().trim()) == 0) );
   }

   public String toString()
   {
      return key;
   }

   public int hashCode()
   {
      return key.hashCode();
   }
}
