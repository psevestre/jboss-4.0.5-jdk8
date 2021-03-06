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
package javax.management.relation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.mx.util.Serialization;

/**
 * A role is a role name and an ordered list of object names to
 * the MBeans in the role.
 *
 * @author <a href="mailto:Adrian.Brock@HappeningTimes.com">Adrian Brock</a>
 * @version $Revision: 57200 $
 *
 * <p><b>Revisions:</b>
 * <p><b>20020716 Adrian Brock:</b>
 * <ul>
 * <li> Serialization
 * </ul>
 */
public class Role
  implements Serializable
{
   // Attributes ----------------------------------------------------

   /**
    * The role name
    */
   private String name;

   /**
    * An ordered list of MBean object names.
    */
   private List objectNameList;

   // Static --------------------------------------------------------

   private static final long serialVersionUID;
   private static final ObjectStreamField[] serialPersistentFields;

   static
   {
      switch (Serialization.version)
      {
      case Serialization.V1R0:
         serialVersionUID = -1959486389343113026L;
         serialPersistentFields = new ObjectStreamField[]
         {
            new ObjectStreamField("myName",  String.class),
            new ObjectStreamField("myObjNameList", List.class)
         };
         break;
      default:
         serialVersionUID = -279985518429862552L;
         serialPersistentFields = new ObjectStreamField[]
         {
            new ObjectStreamField("name",  String.class),
            new ObjectStreamField("objectNameList", List.class)
         };
      }
   }

   /**
    * Formats the role value for output.<p>
    *
    * The spec says it should be a comma separated list of object names.
    * But the RI uses new lines which makes more sense for object names.
    *
    * @param roleValue the role value to print
    * @return the string representation
    * @exception IllegalArgumentException for null value.
    */
   public static String roleValueToString(List roleValue)
     throws IllegalArgumentException
   {
     if (roleValue == null)
       throw new IllegalArgumentException("null roleValue");
     StringBuffer buffer = new StringBuffer();
     Iterator iterator = roleValue.iterator(); 
     while (iterator.hasNext())
     {
       buffer.append(iterator.next());
       if (iterator.hasNext())
         buffer.append("\n");
     }
     return buffer.toString();
   }

   // Constructors --------------------------------------------------

   /**
    * Construct a new role.<p>
    *
    * No validation is performed until the role is set of in a
    * relation. Passed parameters must not be null.<p>
    * 
    * The passed list must be an ArrayList.
    *
    * @param roleName the role name
    * @param roleValue the MBean object names in the role
    * @exception IllegalArgumentException for null values.
    */
   public Role(String roleName, List roleValue)
     throws IllegalArgumentException
   {
     setRoleName(roleName);
     setRoleValue(roleValue); 
   }

   // Public ---------------------------------------------------------

   /**
    * Retrieve the role name.
    * 
    * @return the role name.
    */
   public String getRoleName()
   {
     return name;
   }

   /**
    * Retrieve the role value.
    * 
    * @return a list of MBean object names.
    */
   public List getRoleValue()
   {
     return new ArrayList(objectNameList);
   }

   /**
    * Set the role name.
    * 
    * @param roleName the role name.
    * @exception IllegalArgumentException for a null value
    */
   public void setRoleName(String roleName)
     throws IllegalArgumentException
   {
     if (roleName == null)
       throw new IllegalArgumentException("Null roleName");
     name = roleName;
   }

   /**
    * Set the role value it must be an ArrayList.
    * A list of mbean object names.
    * 
    * @param roleValue the role value.
    * @exception IllegalArgumentException for a null value or not an
    *            array list
    */
   public void setRoleValue(List roleValue)
     throws IllegalArgumentException
   {
     if (roleValue == null)
       throw new IllegalArgumentException("Null roleValue");
     objectNameList = new ArrayList(roleValue);
   }

   // Object Overrides -------------------------------------------------

   /**
    * Clones the object.
    *
    * @todo fix this not to use the copy constructor
    *
    * @return a copy of the role
    * @throws CloneNotSupportedException
    */
   public synchronized Object clone()
   {
      return new Role(name, objectNameList);
/*      try
      {
         Role clone = (Role) super.clone();
         clone.name = this.name;
         clone.objectNameList = new ArrayList(this.objectNameList);
         return clone;
      }
      catch (CloneNotSupportedException e)
      {
         throw new RuntimeException(e.toString());
      }
*/  }

   /**
    * Formats the role for output.
    *
    * @return a human readable string
    */
   public synchronized String toString()
   {
     StringBuffer buffer = new StringBuffer();
     buffer.append("Role@");
     buffer.append(System.identityHashCode(this));
     buffer.append(" RoleName(");
     buffer.append(name);
     buffer.append(") ObjectNames (");
     Iterator iterator = objectNameList.iterator(); 
     while (iterator.hasNext())
     {
       buffer.append(iterator.next());
       if (iterator.hasNext())
         buffer.append(" & ");
     }
     buffer.append(")");
     return buffer.toString();
   }

   // Private -----------------------------------------------------

   private void readObject(ObjectInputStream ois)
      throws IOException, ClassNotFoundException
   {
      switch (Serialization.version)
      {
      case Serialization.V1R0:
         ObjectInputStream.GetField getField = ois.readFields();
         name = (String) getField.get("myName", null);
         objectNameList = (List) getField.get("myObjNameList", null);
         break;
      default:
         ois.defaultReadObject();
      }
   }

   private void writeObject(ObjectOutputStream oos)
      throws IOException
   {
      switch (Serialization.version)
      {
      case Serialization.V1R0:
         ObjectOutputStream.PutField putField = oos.putFields();
         putField.put("myName", name);
         putField.put("myObjNameList", objectNameList);
         oos.writeFields();
         break;
      default:
         oos.defaultWriteObject();
      }
   }
}

