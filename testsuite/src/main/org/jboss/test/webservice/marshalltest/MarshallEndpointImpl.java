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

import org.jboss.test.webservice.marshalltest.types.Bean;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class MarshallEndpointImpl implements SessionBean
{
   public String echoString(String v)
   {
      return v;
   }

   public BigInteger echoInteger(BigInteger v)
   {
      return v;
   }

   public int echoInt(int v)
   {
      return v;
   }

   public long echoLong(long v)
   {
      return v;
   }

   public short echoShort(short v)
   {
      return v;
   }

   public BigDecimal echoDecimal(BigDecimal v)
   {
      return v;
   }

   public float echoFloat(float v)
   {
      return v;
   }

   public double echoDouble(double v)
   {
      return v;
   }

   public boolean echoBoolean(boolean v)
   {
      return v;
   }

   public byte echoByte(byte v)
   {
      return v;
   }

   public QName echoQName(QName v)
   {
      return v;
   }

   public Calendar echoDateTimeCalendar(Calendar v)
   {
      return v;
   }

   public Date echoDateTimeDate(Date v)
   {
      return v;
   }

   public Calendar echoDateCalendar(Calendar v)
   {
      return v;
   }

   public Date echoDateDate(Date v)
   {
      return v;
   }

   public byte[] echoBase64Binary(byte[] v)
   {
      return v;
   }

   public byte[] echoHexBinary(byte[] v)
   {
      return v;
   }

   public Bean echoBean(Bean bean)
   {
      return bean;
   }

   //
   // array methods
   //

   public String[] echoStringArray(String[] v)
   {
      return v;
   }

   public BigInteger[] echoIntegerArray(BigInteger[] v)
   {
      return v;
   }

   public int[] echoIntArray(int[] v)
   {
      return v;
   }

   public long[] echoLongArray(long[] v)
   {
      return v;
   }

   public short[] echoShortArray(short[] v)
   {
      return v;
   }

   public BigDecimal[] echoDecimalArray(BigDecimal[] v)
   {
      return v;
   }

   public float[] echoFloatArray(float[] v)
   {
      return v;
   }

   public double[] echoDoubleArray(double[] v)
   {
      return v;
   }

   public boolean[] echoBooleanArray(boolean[] v)
   {
      return v;
   }

   public byte[] echoByteArray(byte[] v)
   {
      return v;
   }

   public QName[] echoQNameArray(QName[] v)
   {
      return v;
   }

   public Calendar[] echoDateTimeArray(Calendar[] v)
   {
      return v;
   }
   
   /*
    public byte[][] echoBase64BinaryArray(byte[][] v)
    {
    return v;
    }
    public byte[][] echoHexBinaryArray(byte[][] v)
    {
    return v;
    }

    public void echoVoid()
    {
    }
    */

   public void ejbCreate()
   {
   }

   public void ejbActivate()
   {
   }

   public void ejbRemove()
   {
   }

   public void ejbPassivate()
   {
   }

   public void setSessionContext(SessionContext sc)
   {
   }
}
