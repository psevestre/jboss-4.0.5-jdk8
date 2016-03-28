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
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJBObject;
import javax.xml.namespace.QName;

import org.jboss.test.webservice.marshalltest.types.Bean;

public interface MarshallEndpointRemote extends EJBObject
{
   public String echoString(String v) throws RemoteException;

   public BigInteger echoInteger(BigInteger v) throws RemoteException;

   public int echoInt(int v) throws RemoteException;

   public long echoLong(long v) throws RemoteException;

   public short echoShort(short v) throws RemoteException;

   public BigDecimal echoDecimal(BigDecimal v) throws RemoteException;

   public float echoFloat(float v) throws RemoteException;

   public double echoDouble(double v) throws RemoteException;

   public boolean echoBoolean(boolean v) throws RemoteException;

   public byte echoByte(byte v) throws RemoteException;

   public QName echoQName(QName v) throws RemoteException;

   public Calendar echoDateTimeCalendar(Calendar v) throws RemoteException;

   public Date echoDateTimeDate(Date v) throws RemoteException;

   public Calendar echoDateCalendar(Calendar v) throws RemoteException;

   public Date echoDateDate(Date v) throws RemoteException;

   public byte[] echoBase64Binary(byte[] v) throws RemoteException;

   public byte[] echoHexBinary(byte[] v) throws RemoteException;

   public Bean echoBean(Bean bean) throws RemoteException;

   public String[] echoStringArray(String[] v) throws RemoteException;

   public BigInteger[] echoIntegerArray(BigInteger[] v) throws RemoteException;

   public int[] echoIntArray(int[] v) throws RemoteException;

   public long[] echoLongArray(long[] v) throws RemoteException;

   public short[] echoShortArray(short[] v) throws RemoteException;

   public BigDecimal[] echoDecimalArray(BigDecimal[] v) throws RemoteException;

   public float[] echoFloatArray(float[] v) throws RemoteException;

   public double[] echoDoubleArray(double[] v) throws RemoteException;

   public boolean[] echoBooleanArray(boolean[] v) throws RemoteException;

   public byte[] echoByteArray(byte[] v) throws RemoteException;

   public QName[] echoQNameArray(QName[] v) throws RemoteException;

   public Calendar[] echoDateTimeArray(Calendar[] v) throws RemoteException;
}
