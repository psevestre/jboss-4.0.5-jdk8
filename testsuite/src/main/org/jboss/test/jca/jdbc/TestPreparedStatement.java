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
package org.jboss.test.jca.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * TestPreparedStatement.java
 *
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 */

public class TestPreparedStatement extends TestStatement implements
		PreparedStatement {
	public TestPreparedStatement(final TestDriver driver) {
		super(driver);
	}

	public void addBatch() {
	}

	public void clearParameters() {
	}

	public boolean execute() {
		return true;
	}

	public ResultSet executeQuery() {
		return null;
	}

	public int executeUpdate() {
		return 0;
	}

	public ResultSetMetaData getMetaData() {
		return null;
	}

	public ParameterMetaData getParameterMetaData() {
		return null;
	}

	public void setArray(int i, Array x) {
	}

	public void setAsciiStream(int i, InputStream is, int length) {
	}

	public void setBigDecimal(int i, BigDecimal x) {
	}

	public void setBinaryStream(int i, InputStream is, int length) {
	}

	public void setBlob(int i, Blob x) {
	}

	public void setBoolean(int i, boolean x) {
	}

	public void setByte(int i, byte x) {
	}

	public void setBytes(int i, byte[] x) {
	}

	public void setCharacterStream(int i, Reader r, int length) {
	}

	public void setClob(int i, Clob x) {
	}

	public void setDate(int i, Date x) {
	}

	public void setDate(int i, Date x, Calendar cal) {
	}

	public void setDouble(int i, double x) {
	}

	public void setFloat(int i, float x) {
	}

	public void setInt(int i, int x) {
	}

	public void setLong(int i, long x) {
	}

	public void setNull(int i, int sqlType) {
	}

	public void setNull(int i, int sqlType, String typeName) {
	}

	public void setObject(int i, Object x) {
	}

	public void setObject(int i, Object x, int targetSqlType) {
	}

	public void setObject(int i, Object x, int targetSqlType, int scale) {
	}

	public void setRef(int i, Ref ref) {
	}

	public void setShort(int i, short x) {
	}

	public void setString(int i, String x) {
	}

	public void setTime(int i, Time x) {
	}

	public void setTime(int i, Time x, Calendar cal) {
	}

	public void setTimestamp(int i, Timestamp x) {
	}

	public void setTimestamp(int i, Timestamp x, Calendar cal) {
	}

	public void setUnicodeStream(int i, InputStream x, int length) {
	}

	public void setURL(int i, URL url) {
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object unwrap(Class iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}
}
