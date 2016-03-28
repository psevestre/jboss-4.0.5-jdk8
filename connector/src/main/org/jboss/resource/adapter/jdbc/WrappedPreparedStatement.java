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
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * A wrapper for a prepared statement.
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 57189 $
 */
public class WrappedPreparedStatement extends WrappedStatement implements
		PreparedStatement {
	private final PreparedStatement ps;

	public WrappedPreparedStatement(final WrappedConnection lc,
			final PreparedStatement ps) {
		super(lc, ps);
		this.ps = ps;
	}

	public Statement getUnderlyingStatement() throws SQLException {
		checkState();
		if (ps instanceof CachedPreparedStatement) {
			return ((CachedPreparedStatement) ps)
					.getUnderlyingPreparedStatement();
		} else {
			return ps;
		}
	}

	public void setBoolean(int parameterIndex, boolean value)
			throws SQLException {
		checkState();
		try {
			ps.setBoolean(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setByte(int parameterIndex, byte value) throws SQLException {
		checkState();
		try {
			ps.setByte(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setShort(int parameterIndex, short value) throws SQLException {
		checkState();
		try {
			ps.setShort(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setInt(int parameterIndex, int value) throws SQLException {
		checkState();
		try {
			ps.setInt(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setLong(int parameterIndex, long value) throws SQLException {
		checkState();
		try {
			ps.setLong(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setFloat(int parameterIndex, float value) throws SQLException {
		checkState();
		try {
			ps.setFloat(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDouble(int parameterIndex, double value) throws SQLException {
		checkState();
		try {
			ps.setDouble(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setURL(int parameterIndex, URL value) throws SQLException {
		checkState();
		try {
			ps.setURL(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTime(int parameterIndex, Time value) throws SQLException {
		checkState();
		try {
			ps.setTime(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTime(int parameterIndex, Time value, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			ps.setTime(parameterIndex, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public boolean execute() throws SQLException {
		checkTransaction();
		try {
			checkConfiguredQueryTimeout();
			return ps.execute();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		checkState();
		try {
			return ps.getMetaData();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public ResultSet executeQuery() throws SQLException {
		checkTransaction();
		try {
			checkConfiguredQueryTimeout();
			ResultSet resultSet = ps.executeQuery();
			return registerResultSet(resultSet);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public int executeUpdate() throws SQLException {
		checkTransaction();
		try {
			checkConfiguredQueryTimeout();
			return ps.executeUpdate();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void addBatch() throws SQLException {
		checkState();
		try {
			ps.addBatch();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		checkState();
		try {
			ps.setNull(parameterIndex, sqlType);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		checkState();
		try {
			ps.setNull(parameterIndex, sqlType, typeName);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBigDecimal(int parameterIndex, BigDecimal value)
			throws SQLException {
		checkState();
		try {
			ps.setBigDecimal(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setString(int parameterIndex, String value) throws SQLException {
		checkState();
		try {
			ps.setString(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBytes(int parameterIndex, byte[] value) throws SQLException {
		checkState();
		try {
			ps.setBytes(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDate(int parameterIndex, Date value) throws SQLException {
		checkState();
		try {
			ps.setDate(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setDate(int parameterIndex, Date value, Calendar calendar)
			throws SQLException {
		checkState();
		try {
			ps.setDate(parameterIndex, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTimestamp(int parameterIndex, Timestamp value)
			throws SQLException {
		checkState();
		try {
			ps.setTimestamp(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setTimestamp(int parameterIndex, Timestamp value,
			Calendar calendar) throws SQLException {
		checkState();
		try {
			ps.setTimestamp(parameterIndex, value, calendar);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	/**
	 * @deprecated
	 */
	public void setAsciiStream(int parameterIndex, InputStream stream,
			int length) throws SQLException {
		checkState();
		try {
			ps.setAsciiStream(parameterIndex, stream, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	/**
	 * @deprecated
	 */
	public void setUnicodeStream(int parameterIndex, InputStream stream,
			int length) throws SQLException {
		checkState();
		try {
			ps.setUnicodeStream(parameterIndex, stream, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBinaryStream(int parameterIndex, InputStream stream,
			int length) throws SQLException {
		checkState();
		try {
			ps.setBinaryStream(parameterIndex, stream, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void clearParameters() throws SQLException {
		checkState();
		try {
			ps.clearParameters();
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(int parameterIndex, Object value, int sqlType,
			int scale) throws SQLException {
		checkState();
		try {
			ps.setObject(parameterIndex, value, sqlType, scale);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(int parameterIndex, Object value, int sqlType)
			throws SQLException {
		checkState();
		try {
			ps.setObject(parameterIndex, value, sqlType);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setObject(int parameterIndex, Object value) throws SQLException {
		checkState();
		try {
			ps.setObject(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		checkState();
		try {
			ps.setCharacterStream(parameterIndex, reader, length);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setRef(int parameterIndex, Ref value) throws SQLException {
		checkState();
		try {
			ps.setRef(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setBlob(int parameterIndex, Blob value) throws SQLException {
		checkState();
		try {
			ps.setBlob(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setClob(int parameterIndex, Clob value) throws SQLException {
		checkState();
		try {
			ps.setClob(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public void setArray(int parameterIndex, Array value) throws SQLException {
		checkState();
		try {
			ps.setArray(parameterIndex, value);
		} catch (Throwable t) {
			throw checkException(t);
		}
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		checkState();
		try {
			return ps.getParameterMetaData();
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
		return ps.unwrap(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return ps.executeQuery(sql);
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		return ps.isWrapperFor(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		return ps.executeUpdate(sql);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		ps.close();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		return ps.getMaxFieldSize();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		ps.setMaxFieldSize(max);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		return ps.getMaxRows();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		ps.setMaxRows(max);
	}

	/**
	 * @param enable
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		ps.setEscapeProcessing(enable);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		return ps.getQueryTimeout();
	}

	/**
	 * @param seconds
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		ps.setQueryTimeout(seconds);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		ps.cancel();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return ps.getWarnings();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		ps.clearWarnings();
	}

	/**
	 * @param name
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		ps.setCursorName(name);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
		return ps.execute(sql);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		return ps.getResultSet();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		return ps.getUpdateCount();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		return ps.getMoreResults();
	}

	/**
	 * @param direction
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		ps.setFetchDirection(direction);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return ps.getFetchDirection();
	}

	/**
	 * @param rows
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		ps.setFetchSize(rows);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return ps.getFetchSize();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		return ps.getResultSetConcurrency();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		return ps.getResultSetType();
	}

	/**
	 * @param sql
	 * @throws SQLException
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		ps.addBatch(sql);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		ps.clearBatch();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		return ps.executeBatch();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return ps.getConnection();
	}

	/**
	 * @param current
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return ps.getMoreResults(current);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return ps.getGeneratedKeys();
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
		return ps.executeUpdate(sql, autoGeneratedKeys);
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
		return ps.executeUpdate(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		ps.setRowId(parameterIndex, x);
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
		return ps.executeUpdate(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		ps.setNString(parameterIndex, value);
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
		ps.setNCharacterStream(parameterIndex, value, length);
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
		return ps.execute(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		ps.setNClob(parameterIndex, value);
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
		ps.setClob(parameterIndex, reader, length);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return ps.execute(sql, columnIndexes);
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
		ps.setBlob(parameterIndex, inputStream, length);
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
		ps.setNClob(parameterIndex, reader, length);
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
		return ps.execute(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		ps.setSQLXML(parameterIndex, xmlObject);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return ps.getResultSetHoldability();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return ps.isClosed();
	}

	/**
	 * @param poolable
	 * @throws SQLException
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	public void setPoolable(boolean poolable) throws SQLException {
		ps.setPoolable(poolable);
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
		ps.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isPoolable()
	 */
	public boolean isPoolable() throws SQLException {
		return ps.isPoolable();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		ps.closeOnCompletion();
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
		ps.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		return ps.isCloseOnCompletion();
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
		ps.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeUpdateCount()
	 */
	public long getLargeUpdateCount() throws SQLException {
		return ps.getLargeUpdateCount();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setLargeMaxRows(long)
	 */
	public void setLargeMaxRows(long max) throws SQLException {
		ps.setLargeMaxRows(max);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		ps.setAsciiStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeMaxRows()
	 */
	public long getLargeMaxRows() throws SQLException {
		return ps.getLargeMaxRows();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		ps.setBinaryStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeBatch()
	 */
	public long[] executeLargeBatch() throws SQLException {
		return ps.executeLargeBatch();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		ps.setCharacterStream(parameterIndex, reader);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		ps.setNCharacterStream(parameterIndex, value);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String)
	 */
	public long executeLargeUpdate(String sql) throws SQLException {
		return ps.executeLargeUpdate(sql);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		ps.setClob(parameterIndex, reader);
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
		return ps.executeLargeUpdate(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		ps.setBlob(parameterIndex, inputStream);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		ps.setNClob(parameterIndex, reader);
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
		return ps.executeLargeUpdate(sql, columnIndexes);
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
		ps.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
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
		return ps.executeLargeUpdate(sql, columnNames);
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
		ps.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeLargeUpdate()
	 */
	public long executeLargeUpdate() throws SQLException {
		return ps.executeLargeUpdate();
	}

	/**
	 * Compatibilidade Java 8
	 */

}
