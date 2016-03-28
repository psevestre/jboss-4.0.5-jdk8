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
package org.jboss.resource.adapter.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * WrappedCallableStatement
 * 
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 57189 $
 */
public class WrappedCallableStatement extends WrappedPreparedStatement
		implements CallableStatement {
	private final CallableStatement cs;

	public WrappedCallableStatement(final WrappedConnection lc,
			final CallableStatement cs) {
		super(lc, cs);
		this.cs = cs;
	}

	public Object getObject(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getObject(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Object getObject(int parameterIndex, Map typeMap)
			throws SQLException {
		checkState();
		try {
			return cs.getObject(parameterIndex, typeMap);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Object getObject(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getObject(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Object getObject(String parameterName, Map typeMap)
			throws SQLException {
		checkState();
		try {
			return cs.getObject(parameterName, typeMap);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getBoolean(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public boolean getBoolean(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getBoolean(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public byte getByte(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getByte(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public byte getByte(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getByte(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public short getShort(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getShort(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public short getShort(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getShort(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public int getInt(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getInt(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public int getInt(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getInt(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public long getLong(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getLong(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public long getLong(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getLong(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public float getFloat(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getFloat(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public float getFloat(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getFloat(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public double getDouble(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getDouble(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public double getDouble(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getDouble(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getBytes(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public byte[] getBytes(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getBytes(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public URL getURL(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getURL(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public URL getURL(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getURL(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public String getString(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getString(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public String getString(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getString(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Ref getRef(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getRef(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Ref getRef(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getRef(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Time getTime(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getTime(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Time getTime(int parameterIndex, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getTime(parameterIndex, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Time getTime(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getTime(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Time getTime(String parameterName, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getTime(parameterName, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Date getDate(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getDate(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Date getDate(int parameterIndex, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getDate(parameterIndex, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Date getDate(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getDate(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Date getDate(String parameterName, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getDate(parameterName, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterIndex, sqlType);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterIndex, sqlType, scale);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(int parameterIndex, int sqlType,
			String typeName) throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterIndex, sqlType, typeName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterName, sqlType);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterName, sqlType, scale);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		checkState();
		try {
			cs.registerOutParameter(parameterName, sqlType, typeName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public boolean wasNull() throws SQLException {
		checkState();
		try {
			return cs.wasNull();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	/**
	 * @deprecated
	 */
	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		checkState();
		try {
			return cs.getBigDecimal(parameterIndex, scale);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getBigDecimal(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getBigDecimal(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getTimestamp(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getTimestamp(parameterIndex, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getTimestamp(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Timestamp getTimestamp(String parameterName, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			return cs.getTimestamp(parameterName, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Blob getBlob(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getBlob(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Blob getBlob(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getBlob(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Clob getClob(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getClob(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Clob getClob(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getClob(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Array getArray(int parameterIndex) throws SQLException {
		checkState();
		try {
			return cs.getArray(parameterIndex);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public Array getArray(String parameterName) throws SQLException {
		checkState();
		try {
			return cs.getArray(parameterName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBoolean(String parameterName, boolean value)
			throws SQLException {
		checkState();
		try {
			cs.setBoolean(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setByte(String parameterName, byte value) throws SQLException {
		checkState();
		try {
			cs.setByte(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setShort(String parameterName, short value) throws SQLException {
		checkState();
		try {
			cs.setShort(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setInt(String parameterName, int value) throws SQLException {
		checkState();
		try {
			cs.setInt(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setLong(String parameterName, long value) throws SQLException {
		checkState();
		try {
			cs.setLong(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setFloat(String parameterName, float value) throws SQLException {
		checkState();
		try {
			cs.setFloat(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDouble(String parameterName, double value)
			throws SQLException {
		checkState();
		try {
			cs.setDouble(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setURL(String parameterName, URL value) throws SQLException {
		checkState();
		try {
			cs.setURL(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTime(String parameterName, Time value) throws SQLException {
		checkState();
		try {
			cs.setTime(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTime(String parameterName, Time value, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			cs.setTime(parameterName, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setNull(String parameterName, int value) throws SQLException {
		checkState();
		try {
			cs.setNull(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		checkState();
		try {
			cs.setNull(parameterName, sqlType, typeName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBigDecimal(String parameterName, BigDecimal value)
			throws SQLException {
		checkState();
		try {
			cs.setBigDecimal(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setString(String parameterName, String value)
			throws SQLException {
		checkState();
		try {
			cs.setString(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBytes(String parameterName, byte[] value)
			throws SQLException {
		checkState();
		try {
			cs.setBytes(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDate(String parameterName, Date value) throws SQLException {
		checkState();
		try {
			cs.setDate(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDate(String parameterName, Date value, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			cs.setDate(parameterName, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTimestamp(String parameterName, Timestamp value)
			throws SQLException {
		checkState();
		try {
			cs.setTimestamp(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTimestamp(String parameterName, Timestamp value,
			Calendar calendar) throws SQLException {
		checkState();
		try {
			cs.setTimestamp(parameterName, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setAsciiStream(String parameterName, InputStream stream,
			int length) throws SQLException {
		checkState();
		try {
			cs.setAsciiStream(parameterName, stream, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBinaryStream(String parameterName, InputStream stream,
			int length) throws SQLException {
		checkState();
		try {
			cs.setBinaryStream(parameterName, stream, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(String parameterName, Object value, int sqlType,
			int scale) throws SQLException {
		checkState();
		try {
			cs.setObject(parameterName, value, sqlType, scale);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(String parameterName, Object value, int sqlType)
			throws SQLException {
		checkState();
		try {
			cs.setObject(parameterName, value, sqlType);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(String parameterName, Object value)
			throws SQLException {
		checkState();
		try {
			cs.setObject(parameterName, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		checkState();
		try {
			cs.setCharacterStream(parameterName, reader, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class iface) throws SQLException {
		return cs.unwrap(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return cs.executeQuery(sql);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		return cs.executeQuery();
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		return cs.isWrapperFor(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		return cs.executeUpdate(sql);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	public int executeUpdate() throws SQLException {
		return cs.executeUpdate();
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		cs.setNull(parameterIndex, sqlType);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		cs.close();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		return cs.getMaxFieldSize();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		cs.setBoolean(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	public void setByte(int parameterIndex, byte x) throws SQLException {
		cs.setByte(parameterIndex, x);
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		cs.setMaxFieldSize(max);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	public void setShort(int parameterIndex, short x) throws SQLException {
		cs.setShort(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		return cs.getMaxRows();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	public void setInt(int parameterIndex, int x) throws SQLException {
		cs.setInt(parameterIndex, x);
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		cs.setMaxRows(max);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	public void setLong(int parameterIndex, long x) throws SQLException {
		cs.setLong(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	public void setFloat(int parameterIndex, float x) throws SQLException {
		cs.setFloat(parameterIndex, x);
	}

	/**
	 * @param enable
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		cs.setEscapeProcessing(enable);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	public void setDouble(int parameterIndex, double x) throws SQLException {
		cs.setDouble(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		return cs.getQueryTimeout();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		cs.setBigDecimal(parameterIndex, x);
	}

	/**
	 * @param seconds
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		cs.setQueryTimeout(seconds);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	public void setString(int parameterIndex, String x) throws SQLException {
		cs.setString(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		cs.setBytes(parameterIndex, x);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		cs.cancel();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	public void setDate(int parameterIndex, Date x) throws SQLException {
		cs.setDate(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return cs.getWarnings();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	public void setTime(int parameterIndex, Time x) throws SQLException {
		cs.setTime(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		cs.setTimestamp(parameterIndex, x);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		cs.clearWarnings();
	}

	/**
	 * @param name
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		cs.setCursorName(name);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 *      int)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		cs.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @deprecated
	 * @see java.sql.PreparedStatement#setUnicodeStream(int,
	 *      java.io.InputStream, int)
	 */
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		cs.setUnicodeStream(parameterIndex, x, length);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
		return cs.execute(sql);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 *      int)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		cs.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		return cs.getResultSet();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		return cs.getUpdateCount();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	public void clearParameters() throws SQLException {
		cs.clearParameters();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		return cs.getMoreResults();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		cs.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @param direction
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		cs.setFetchDirection(direction);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	public void setObject(int parameterIndex, Object x) throws SQLException {
		cs.setObject(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return cs.getFetchDirection();
	}

	/**
	 * @param rows
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		cs.setFetchSize(rows);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return cs.getFetchSize();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return cs.execute();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		return cs.getResultSetConcurrency();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		return cs.getResultSetType();
	}

	/**
	 * @param sql
	 * @throws SQLException
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		cs.addBatch(sql);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException {
		cs.addBatch();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 *      int)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		cs.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		cs.clearBatch();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		return cs.executeBatch();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		cs.setRef(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		cs.setBlob(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		cs.setClob(parameterIndex, x);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	public void setArray(int parameterIndex, Array x) throws SQLException {
		cs.setArray(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return cs.getConnection();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return cs.getMetaData();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date,
	 *      java.util.Calendar)
	 */
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		cs.setDate(parameterIndex, x, cal);
	}

	/**
	 * @param current
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return cs.getMoreResults(current);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time,
	 *      java.util.Calendar)
	 */
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		cs.setTime(parameterIndex, x, cal);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return cs.getGeneratedKeys();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param cal
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp,
	 *      java.util.Calendar)
	 */
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		cs.setTimestamp(parameterIndex, x, cal);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return cs.executeUpdate(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		cs.setNull(parameterIndex, sqlType, typeName);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return cs.executeUpdate(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	public void setURL(int parameterIndex, URL x) throws SQLException {
		cs.setURL(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return cs.getParameterMetaData();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		cs.setRowId(parameterIndex, x);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String,
	 *      java.lang.String[])
	 */
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return cs.executeUpdate(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		cs.setNString(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader,
	 *      long)
	 */
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		cs.setNCharacterStream(parameterIndex, value, length);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return cs.execute(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		cs.setNClob(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		cs.setClob(parameterIndex, reader, length);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return cs.execute(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		cs.setBlob(parameterIndex, inputStream, length);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		cs.setNClob(parameterIndex, reader, length);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		return cs.execute(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		cs.setSQLXML(parameterIndex, xmlObject);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int,
	 *      int)
	 */
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		cs.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return cs.getResultSetHoldability();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return cs.isClosed();
	}

	/**
	 * @param poolable
	 * @throws SQLException
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	public void setPoolable(boolean poolable) throws SQLException {
		cs.setPoolable(poolable);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream,
	 *      long)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		cs.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isPoolable()
	 */
	public boolean isPoolable() throws SQLException {
		return cs.isPoolable();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		cs.closeOnCompletion();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream,
	 *      long)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		cs.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		return cs.isCloseOnCompletion();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader,
	 *      long)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		cs.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeUpdateCount()
	 */
	public long getLargeUpdateCount() throws SQLException {
		return cs.getLargeUpdateCount();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setLargeMaxRows(long)
	 */
	public void setLargeMaxRows(long max) throws SQLException {
		cs.setLargeMaxRows(max);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		cs.setAsciiStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeMaxRows()
	 */
	public long getLargeMaxRows() throws SQLException {
		return cs.getLargeMaxRows();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		cs.setBinaryStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeBatch()
	 */
	public long[] executeLargeBatch() throws SQLException {
		return cs.executeLargeBatch();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		cs.setCharacterStream(parameterIndex, reader);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		cs.setNCharacterStream(parameterIndex, value);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String)
	 */
	public long executeLargeUpdate(String sql) throws SQLException {
		return cs.executeLargeUpdate(sql);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		cs.setClob(parameterIndex, reader);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, int)
	 */
	public long executeLargeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return cs.executeLargeUpdate(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		cs.setBlob(parameterIndex, inputStream);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		cs.setNClob(parameterIndex, reader);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, int[])
	 */
	public long executeLargeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return cs.executeLargeUpdate(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object,
	 *      java.sql.SQLType, int)
	 */
	public void setObject(int parameterIndex, Object x, SQLType targetSqlType,
			int scaleOrLength) throws SQLException {
		cs.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String,
	 *      java.lang.String[])
	 */
	public long executeLargeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return cs.executeLargeUpdate(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object,
	 *      java.sql.SQLType)
	 */
	public void setObject(int parameterIndex, Object x, SQLType targetSqlType)
			throws SQLException {
		cs.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeLargeUpdate()
	 */
	public long executeLargeUpdate() throws SQLException {
		return cs.executeLargeUpdate();
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRowId(int)
	 */
	public RowId getRowId(int parameterIndex) throws SQLException {
		return cs.getRowId(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getRowId(java.lang.String)
	 */
	public RowId getRowId(String parameterName) throws SQLException {
		return cs.getRowId(parameterName);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setRowId(java.lang.String,
	 *      java.sql.RowId)
	 */
	public void setRowId(String parameterName, RowId x) throws SQLException {
		cs.setRowId(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNString(java.lang.String,
	 *      java.lang.String)
	 */
	public void setNString(String parameterName, String value)
			throws SQLException {
		cs.setNString(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		cs.setNCharacterStream(parameterName, value, length);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String,
	 *      java.sql.NClob)
	 */
	public void setNClob(String parameterName, NClob value) throws SQLException {
		cs.setNClob(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader,
	 *      long)
	 */
	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		cs.setClob(parameterName, reader, length);
	}

	/**
	 * @param parameterName
	 * @param inputStream
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		cs.setBlob(parameterName, inputStream, length);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String,
	 *      java.io.Reader, long)
	 */
	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		cs.setNClob(parameterName, reader, length);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNClob(int)
	 */
	public NClob getNClob(int parameterIndex) throws SQLException {
		return cs.getNClob(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNClob(java.lang.String)
	 */
	public NClob getNClob(String parameterName) throws SQLException {
		return cs.getNClob(parameterName);
	}

	/**
	 * @param parameterName
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setSQLXML(java.lang.String,
	 *      java.sql.SQLXML)
	 */
	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		cs.setSQLXML(parameterName, xmlObject);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getSQLXML(int)
	 */
	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		return cs.getSQLXML(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getSQLXML(java.lang.String)
	 */
	public SQLXML getSQLXML(String parameterName) throws SQLException {
		return cs.getSQLXML(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNString(int)
	 */
	public String getNString(int parameterIndex) throws SQLException {
		return cs.getNString(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNString(java.lang.String)
	 */
	public String getNString(String parameterName) throws SQLException {
		return cs.getNString(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNCharacterStream(int)
	 */
	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		return cs.getNCharacterStream(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getNCharacterStream(java.lang.String)
	 */
	public Reader getNCharacterStream(String parameterName) throws SQLException {
		return cs.getNCharacterStream(parameterName);
	}

	/**
	 * @param parameterIndex
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getCharacterStream(int)
	 */
	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		return cs.getCharacterStream(parameterIndex);
	}

	/**
	 * @param parameterName
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getCharacterStream(java.lang.String)
	 */
	public Reader getCharacterStream(String parameterName) throws SQLException {
		return cs.getCharacterStream(parameterName);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String, java.sql.Blob)
	 */
	public void setBlob(String parameterName, Blob x) throws SQLException {
		cs.setBlob(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.sql.Clob)
	 */
	public void setClob(String parameterName, Clob x) throws SQLException {
		cs.setClob(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		cs.setAsciiStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream, long)
	 */
	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		cs.setBinaryStream(parameterName, x, length);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader, long)
	 */
	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		cs.setCharacterStream(parameterName, reader, length);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setAsciiStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		cs.setAsciiStream(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBinaryStream(java.lang.String,
	 *      java.io.InputStream)
	 */
	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		cs.setBinaryStream(parameterName, x);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		cs.setCharacterStream(parameterName, reader);
	}

	/**
	 * @param parameterName
	 * @param value
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNCharacterStream(java.lang.String,
	 *      java.io.Reader)
	 */
	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		cs.setNCharacterStream(parameterName, value);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setClob(java.lang.String, java.io.Reader)
	 */
	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		cs.setClob(parameterName, reader);
	}

	/**
	 * @param parameterName
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setBlob(java.lang.String,
	 *      java.io.InputStream)
	 */
	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		cs.setBlob(parameterName, inputStream);
	}

	/**
	 * @param parameterName
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setNClob(java.lang.String,
	 *      java.io.Reader)
	 */
	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		cs.setNClob(parameterName, reader);
	}

	/**
	 * @param parameterIndex
	 * @param type
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(int, java.lang.Class)
	 */
	public Object getObject(int parameterIndex, Class type)
			throws SQLException {
		return cs.getObject(parameterIndex, type);
	}

	/**
	 * @param parameterName
	 * @param type
	 * @return
	 * @throws SQLException
	 * @see java.sql.CallableStatement#getObject(java.lang.String,
	 *      java.lang.Class)
	 */
	public Object getObject(String parameterName, Class type)
			throws SQLException {
		return cs.getObject(parameterName, type);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, java.sql.SQLType, int)
	 */
	public void setObject(String parameterName, Object x,
			SQLType targetSqlType, int scaleOrLength) throws SQLException {
		cs.setObject(parameterName, x, targetSqlType, scaleOrLength);
	}

	/**
	 * @param parameterName
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#setObject(java.lang.String,
	 *      java.lang.Object, java.sql.SQLType)
	 */
	public void setObject(String parameterName, Object x, SQLType targetSqlType)
			throws SQLException {
		cs.setObject(parameterName, x, targetSqlType);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int,
	 *      java.sql.SQLType)
	 */
	public void registerOutParameter(int parameterIndex, SQLType sqlType)
			throws SQLException {
		cs.registerOutParameter(parameterIndex, sqlType);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param scale
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int,
	 *      java.sql.SQLType, int)
	 */
	public void registerOutParameter(int parameterIndex, SQLType sqlType,
			int scale) throws SQLException {
		cs.registerOutParameter(parameterIndex, sqlType, scale);
	}

	/**
	 * @param parameterIndex
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(int,
	 *      java.sql.SQLType, java.lang.String)
	 */
	public void registerOutParameter(int parameterIndex, SQLType sqlType,
			String typeName) throws SQLException {
		cs.registerOutParameter(parameterIndex, sqlType, typeName);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      java.sql.SQLType)
	 */
	public void registerOutParameter(String parameterName, SQLType sqlType)
			throws SQLException {
		cs.registerOutParameter(parameterName, sqlType);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @param scale
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      java.sql.SQLType, int)
	 */
	public void registerOutParameter(String parameterName, SQLType sqlType,
			int scale) throws SQLException {
		cs.registerOutParameter(parameterName, sqlType, scale);
	}

	/**
	 * @param parameterName
	 * @param sqlType
	 * @param typeName
	 * @throws SQLException
	 * @see java.sql.CallableStatement#registerOutParameter(java.lang.String,
	 *      java.sql.SQLType, java.lang.String)
	 */
	public void registerOutParameter(String parameterName, SQLType sqlType,
			String typeName) throws SQLException {
		cs.registerOutParameter(parameterName, sqlType, typeName);
	}

	// ======================================================= Java 7 & 8

}
