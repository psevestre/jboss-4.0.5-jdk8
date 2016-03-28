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
package org.jboss.test.webservice.marshalltest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.namespace.QName;

import org.jboss.test.webservice.WebserviceTestBase;
import org.jboss.test.webservice.marshalltest.types.Bean;

/**
 * Tests of the ws4ee rpc/literal marshalling
 *
 * @author Thomas.Diesler@jboss.org
 * @since 09-May-2004
 */
public class MarshallTestBase extends WebserviceTestBase
{
   protected MarshallEndpoint port;

   public MarshallTestBase(String name)
   {
      super(name);
   }

   public void testEchoString() throws Exception
   {
      Object ret = port.echoString("Hello");
      assertEquals("Hello", ret);

      ret = port.echoString("");
      assertEquals("", ret);

      ret = port.echoString(null);
      assertNull(ret);
   }

   public void testEchoInteger() throws Exception
   {
      Object ret = port.echoInteger(new BigInteger("100"));
      assertEquals(new BigInteger("100"), ret);

      ret = port.echoInteger(null);
      assertNull(ret);
   }

   public void testEchoInt() throws Exception
   {
      int ret = port.echoInt(100);
      assertEquals(100, ret);
   }

   public void testEchoLong() throws Exception
   {
      long ret = port.echoLong(100);
      assertEquals(100, ret);
   }

   public void testEchoShort() throws Exception
   {
      short ret = port.echoShort((short)100);
      assertEquals((short)100, ret);
   }

   public void testEchoDecimal() throws Exception
   {
      Object ret = port.echoDecimal(new BigDecimal("100"));
      assertEquals(new BigDecimal("100"), ret);

      ret = port.echoDecimal(null);
      assertNull(ret);
   }

   public void testEchoFloat() throws Exception
   {
      float ret = port.echoFloat((float)100.3);
      assertEquals(100.3, ret, 0.0001);
   }

   public void testEchoDouble() throws Exception
   {
      double ret = port.echoDouble(100.7);
      assertEquals(100.7, ret, 0.0001);
   }

   public void testEchoBoolean() throws Exception
   {
      boolean ret = port.echoBoolean(true);
      assertEquals(true, ret);

      ret = port.echoBoolean(false);
      assertEquals(false, ret);
   }

   public void testEchoByte() throws Exception
   {
      byte ret = port.echoByte((byte)0x6a);
      assertEquals((byte)0x6a, ret);
   }

   public void testEchoQName() throws Exception
   {
      Object ret = port.echoQName(new QName("uri", "local"));
      assertEquals(new QName("uri", "local"), ret);

      ret = port.echoQName(null);
      assertNull(ret);
   }

   public void testEchoDateTimeCalendar() throws Exception
   {
      Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
      Calendar ret = (Calendar)port.echoDateTimeCalendar(cal);
      assertEquals(cal.getTime(), ret.getTime());
      assertEquals(cal.get(Calendar.ZONE_OFFSET), ret.get(Calendar.ZONE_OFFSET));

      ret = port.echoDateTimeCalendar(null);
      assertNull(ret);
   }

   public void testEchoDateTimeDate() throws Exception
   {
      Calendar cal = Calendar.getInstance();
      cal.clear();
      cal.set(2004, 11, 21, 14, 20, 30);
      Date date = cal.getTime();
      
      Date ret = port.echoDateTimeDate(date);
      assertEquals(date.getTime(), ret.getTime());

      ret = port.echoDateTimeDate(null);
      assertNull(ret);
   }

   public void testEchoDateCalendar() throws Exception
   {
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Hong_Kong"));
      cal.clear();
      cal.set(2004, 11, 21);
      Calendar ret = port.echoDateCalendar(cal);
      assertEquals(cal.getTime().getTime(), ret.getTime().getTime());
      assertEquals(cal.get(Calendar.ZONE_OFFSET), ret.get(Calendar.ZONE_OFFSET));

      ret = port.echoDateCalendar(null);
      assertNull(ret);
   }

   public void testEchoDateDate() throws Exception
   {
      Calendar cal = Calendar.getInstance();
      cal.clear();
      cal.set(2004, 11, 21);
      Date ret = port.echoDateDate(cal.getTime());
      assertEquals(cal.getTime().getTime(), ret.getTime());

      ret = port.echoDateDate(null);
      assertNull(ret);
   }

   public void testEchoBase64Binary() throws Exception
   {
      byte[] bytes = "Hello world!".getBytes();
      Object ret = port.echoBase64Binary(bytes);
      assertEquals("Hello world!", new String((byte[])ret));

      ret = port.echoBase64Binary(null);
      assertNull(ret);
   }

   public void testEchoHexBinary() throws Exception
   {
      byte[] bytes = "Hello world!".getBytes();
      Object ret = port.echoHexBinary(bytes);
      assertEquals("Hello world!", new String((byte[])ret));

      ret = port.echoHexBinary(null);
      assertNull(ret);
   }

   public void testEchoBean() throws Exception
   {
      Bean bean = new Bean();
      bean.setX(1);
      bean.setY(2);
      bean.setBase64("base64".getBytes());

      Object ret = port.echoBean(bean);
      assertEquals(bean, ret);

      ret = port.echoBean(null);
      assertNull(ret);
   }

   // See: http://jira.jboss.com/jira/browse/JBWS-30
   public void testEchoBeanNullProperties() throws Exception
   {
      Bean bean = new Bean();

      // Test null byte array
      Object ret = port.echoBean(bean);
      assertEquals(bean, ret);

      // Test empty byte array
      bean.setBase64(new byte[0]);

      ret = port.echoBean(bean);
      assertEquals(bean, ret);
   }

   public void testEchoStringArray() throws Exception
   {
      String[] arr = new String[] { "Hello", "world", "!" };
      Object ret = port.echoStringArray(arr);
      assertEquals(Arrays.asList(arr), Arrays.asList((String[])ret));
   }

   public void testEchoIntegerArray() throws Exception
   {
      BigInteger[] arr = new BigInteger[] { new BigInteger("1"), new BigInteger("0"), new BigInteger("-1") };
      Object ret = port.echoIntegerArray(arr);
      assertEquals(Arrays.asList(arr), Arrays.asList((BigInteger[])ret));
   }

   public void testEchoIntArray() throws Exception
   {
      int[] arr = new int[] { 1, 0, -1 };
      int[] ret = (int[])port.echoIntArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
   }

   public void testEchoLongArray() throws Exception
   {
      long[] arr = new long[] { 1, 0, -1 };
      long[] ret = (long[])port.echoLongArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
   }

   public void testEchoShortArray() throws Exception
   {
      short[] arr = new short[] { 1, 0, -1 };
      short[] ret = (short[])port.echoShortArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
   }

   public void testEchoDecimalArray() throws Exception
   {
      BigDecimal[] arr = new BigDecimal[] { new BigDecimal("1"), new BigDecimal("0"), new BigDecimal("-1") };
      Object ret = port.echoDecimalArray(arr);
      assertEquals(Arrays.asList(arr), Arrays.asList((BigDecimal[])ret));
   }

   public void testEchoFloatArray() throws Exception
   {
      float[] arr = new float[] { 1, 0, -1 };
      float[] ret = (float[])port.echoFloatArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i], 0);
   }

   public void testEchoDoubleArray() throws Exception
   {
      double[] arr = new double[] { 1, 0, -1 };
      double[] ret = (double[])port.echoDoubleArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i], 0);
   }

   public void testEchoBooleanArray() throws Exception
   {
      boolean[] arr = new boolean[] { true, false, true };
      boolean[] ret = (boolean[])port.echoBooleanArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
   }

   public void testEchoByteArray() throws Exception
   {
      byte[] arr = new byte[] { 1, 0, -1 };
      byte[] ret = (byte[])port.echoByteArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
   }

   public void testEchoQNameArray() throws Exception
   {
      QName[] arr = new QName[] { new QName("uri", "loc1"), new QName("uri", "loc2"), new QName("uri", "loc3") };
      QName[] ret = (QName[])port.echoQNameArray(arr);
      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i], ret[i]);
      assertEquals(Arrays.asList(arr), Arrays.asList((QName[])ret));
   }

   public void testEchoDateTimeArray() throws Exception
   {
      Calendar cal1 = Calendar.getInstance();
      cal1.clear(); cal1.set(2004, 5, 10, 14, 23, 30);
      cal1.setTimeZone(TimeZone.getTimeZone("GMT+1"));
      Calendar cal2 = Calendar.getInstance();
      cal2.clear(); cal2.set(2004, 5, 11, 14, 23, 31);
      cal2.setTimeZone(TimeZone.getTimeZone("GMT+1"));
      Calendar cal3 = Calendar.getInstance();
      cal3.clear(); cal3.set(2004, 5, 12, 14, 23, 32);
      cal3.setTimeZone(TimeZone.getTimeZone("GMT+1"));
      Calendar[] arr = new Calendar[] { cal1, cal2, cal3 };
      Calendar[] ret = (Calendar[])port.echoDateTimeArray(arr);

      for (int i = 0; i < arr.length; i++)
         assertEquals(arr[i].getTime(), ret[i].getTime());
   }
}
