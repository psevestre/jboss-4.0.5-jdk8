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
package org.jboss.test.jbossmx.compliance.objectname;

import org.jboss.test.jbossmx.compliance.TestCase;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

public class BasicTestCase
   extends TestCase
{
   public static final String STD_DOMAIN = "domain";
   public static final String WHITESPACE_DOMAIN = " ";
   public static final String STD_KEYPROP_STRING = "key1=val1,key2=val2";
   public static final String REV_KEYPROP_STRING = "key2=val2,key1=val1";
   public static final String KEY1 = "key1";
   public static final String KEY2 = "key2";
   public static final String VAL1 = "val1";
   public static final String VAL2 = "val2";

   public BasicTestCase(String s)
   {
      super(s);
   }

   public void testStringNoDomain()
   {
      String nameArg = ":" + STD_KEYPROP_STRING;
      try
      {
         ObjectName name = new ObjectName(nameArg);
         String domain = name.getDomain();
         if (null == domain)
         {
            fail("getDomain() should return empty string rather than null");
         }
         assertTrue("domain should have been zero size", domain.length() == 0);
         assertEquals("value for key: " + KEY1 + " should be: " + VAL1, VAL1, name.getKeyProperty(KEY1));
         assertEquals("value for key: " + KEY2 + " should be: " + VAL2, VAL2, name.getKeyProperty(KEY2));
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg + "')");
      }
   }

   public void testStringWithDomain()
   {
      String nameArg = STD_DOMAIN + ":" + STD_KEYPROP_STRING;
      try
      {
         ObjectName name = new ObjectName(nameArg);
         assertEquals("domain should be equivalent", STD_DOMAIN, name.getDomain());
         assertEquals("value for key: " + KEY1 + " should be: " + VAL1, VAL1, name.getKeyProperty(KEY1));
         assertEquals("value for key: " + KEY2 + " should be: " + VAL2, VAL2, name.getKeyProperty(KEY2));
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg + "')");
      }
   }

   public void testSingleKVP()
   {
      try
      {
         ObjectName name = new ObjectName(STD_DOMAIN, KEY1, VAL1);
         assertEquals("domain should be equivalent", STD_DOMAIN, name.getDomain());
         assertEquals("value for key: " + KEY1 + " should be: " + VAL1, VAL1, name.getKeyProperty(KEY1));
         assertNull("should return NULL key property for: " + KEY2, name.getKeyProperty(KEY2));

         String kplistString = name.getKeyPropertyListString();
         if (null == kplistString)
         {
            fail("key property list string was null;");
         }
         assertTrue("KeyPropertyListString should match",
                    kplistString.equals("key1=val1"));

      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + STD_DOMAIN + "','" + KEY1 + "','" + VAL1 + "')");
      }
   }

   public void testHashtable()
   {
      try
      {
         Hashtable properties = new Hashtable();
         properties.put(KEY1, VAL1);
         properties.put(KEY2, VAL2);
         ObjectName name = new ObjectName(STD_DOMAIN, properties);
         assertEquals("domain should be equivalent", STD_DOMAIN, name.getDomain());
         assertEquals("value for key: " + KEY1 + " should be: " + VAL1, VAL1, name.getKeyProperty(KEY1));
         assertEquals("value for key: " + KEY2 + " should be: " + VAL2, VAL2, name.getKeyProperty(KEY2));

         String kplistString = name.getKeyPropertyListString();
         if (null == kplistString)
         {
            fail("key property list string was null;");
         }
         assertTrue("KeyPropertyListString should match",
                    (kplistString.equals(STD_KEYPROP_STRING) || kplistString.equals(REV_KEYPROP_STRING)));
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + STD_DOMAIN + "','" + KEY1 + "','" + VAL1 + "')");
      }
   }

   public void testWhitespaceDomain()
   {
      String wsDomain = " ";
      String nameArg = wsDomain + ":" + STD_KEYPROP_STRING;
      try
      {
         ObjectName name = new ObjectName(nameArg);
         assertEquals("domain should be equivalent", wsDomain, name.getDomain());
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg + "')");
      }
   }

   public void testKeyPropertyList()
   {
      String nameArg = ":" + STD_KEYPROP_STRING;
      try
      {
         ObjectName name = new ObjectName(nameArg);
         String kplistString = name.getKeyPropertyListString();
         if (null == kplistString)
         {
            fail("key property list string was null;");
         }
         assertTrue("KeyPropertyListString should match",
                    (kplistString.equals(STD_KEYPROP_STRING) || kplistString.equals(REV_KEYPROP_STRING)));

      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg + "')");
      }
   }

   public void testToString()
   {
      String nameArg1 = ":key1=val1";
      String nameArg2 = "domain:key1=val1";

      try
      {
         ObjectName name1 = new ObjectName(nameArg1);
         assertEquals("toString should match", nameArg1, name1.toString());
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg1 + "')");
      }

      try
      {
         ObjectName name2 = new ObjectName(nameArg2);
         assertEquals("toString should match", nameArg2, name2.toString());
      }
      catch (MalformedObjectNameException e)
      {
         fail("spurious MalformedObjectNameException on ('" + nameArg2 + "')");
      }
   }
}
