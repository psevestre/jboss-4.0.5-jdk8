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
package org.jboss.varia.stats;

import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.ServiceMBean;
import org.jboss.naming.NonSerializableFactory;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.management.ObjectName;
import javax.naming.NamingException;
import javax.naming.Name;
import javax.naming.InitialContext;

import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSetMetaData;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Savepoint;
import java.sql.ParameterMetaData;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import java.math.BigDecimal;
import java.net.URL;

/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 57210 $</tt>
 * @@jmx:mbean name="jboss.stats:name=DataSourceInterceptor"
 * extends="org.jboss.system.ServiceMBean"
 */
public class DataSourceInterceptor
   extends ServiceMBeanSupport
   implements DataSource, DataSourceInterceptorMBean
{
   /**
    * JNDI name the service will be bound under
    */
   private String bindName;
   /**
    * target DataSource JNDI name
    */
   private String targetName;
   /**
    * target DataSource
    */
   private DataSource target;

   private ObjectName statsCollector;

   // MBean implementation

   /**
    * @jmx.managed-attribute
    */
   public ObjectName getStatsCollector()
   {
      return statsCollector;
   }

   /**
    * @jmx.managed-attribute
    */
   public void setStatsCollector(ObjectName statsCollector)
   {
      this.statsCollector = statsCollector;
   }

   /**
    * @jmx.managed-attribute
    */
   public String getBindName()
   {
      return bindName;
   }

   /**
    * @jmx.managed-attribute
    */
   public void setBindName(String bindName) throws NamingException
   {
      this.bindName = bindName;
      if(getState() == ServiceMBean.STARTED)
      {
         bind();
      }
   }

   /**
    * @jmx.managed-attribute
    */
   public String getTargetName()
   {
      return targetName;
   }

   /**
    * @jmx.managed-attribute
    */
   public void setTargetName(String targetName) throws NamingException
   {
      this.targetName = targetName;
      if(getState() == ServiceMBean.STARTED)
      {
         updateTarget();
      }
   }

   public void startService()
      throws Exception
   {
      updateTarget();
      bind();
   }

   public void stopService()
      throws Exception
   {
      unbind();
   }

   // DataSource implementation

   public int getLoginTimeout() throws SQLException
   {
      return target.getLoginTimeout();
   }

   public void setLoginTimeout(int seconds) throws SQLException
   {
      target.setLoginTimeout(seconds);
   }

   public PrintWriter getLogWriter() throws SQLException
   {
      return target.getLogWriter();
   }

   public void setLogWriter(PrintWriter out) throws SQLException
   {
      target.setLogWriter(out);
   }

   public Connection getConnection() throws SQLException
   {
      return new ConnectionInterceptor(target.getConnection());
   }

   public Connection getConnection(String username, String password) throws SQLException
   {
      return new ConnectionInterceptor(target.getConnection());
   }

   // Inner

   public class ConnectionInterceptor
      implements Connection
   {
      private final Connection target;

      public ConnectionInterceptor(Connection target)
      {
         this.target = target;
      }

      
      public int getHoldability() throws SQLException
      {
         return target.getHoldability();
      }
      

      public int getTransactionIsolation() throws SQLException
      {
         return target.getTransactionIsolation();
      }

      public void clearWarnings() throws SQLException
      {
         target.clearWarnings();
      }

      public void close() throws SQLException
      {
         target.close();
      }

      public void commit() throws SQLException
      {
         target.commit();
      }

      public void rollback() throws SQLException
      {
         target.rollback();
      }

      public boolean getAutoCommit() throws SQLException
      {
         return target.getAutoCommit();
      }

      public boolean isClosed() throws SQLException
      {
         return target.isClosed();
      }

      public boolean isReadOnly() throws SQLException
      {
         return target.isReadOnly();
      }

      
      public void setHoldability(int holdability) throws SQLException
      {
         target.setHoldability(holdability);
      }
      

      public void setTransactionIsolation(int level) throws SQLException
      {
         target.setTransactionIsolation(level);
      }

      public void setAutoCommit(boolean autoCommit) throws SQLException
      {
         target.setAutoCommit(autoCommit);
      }

      public void setReadOnly(boolean readOnly) throws SQLException
      {
         target.setReadOnly(readOnly);
      }

      public String getCatalog() throws SQLException
      {
         return target.getCatalog();
      }

      public void setCatalog(String catalog) throws SQLException
      {
         target.setCatalog(catalog);
      }

      public DatabaseMetaData getMetaData() throws SQLException
      {
         return target.getMetaData();
      }

      public SQLWarning getWarnings() throws SQLException
      {
         return target.getWarnings();
      }

      
      public Savepoint setSavepoint() throws SQLException
      {
         return target.setSavepoint();
      }
      

      
      public void releaseSavepoint(Savepoint savepoint) throws SQLException
      {
         target.releaseSavepoint(savepoint);
      }
      

      
      public void rollback(Savepoint savepoint) throws SQLException
      {
         target.rollback(savepoint);
      }
      

      public Statement createStatement() throws SQLException
      {
         return new StatementInterceptor(this, target.createStatement());
      }

      public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
      {
         return new StatementInterceptor(this, target.createStatement(resultSetType, resultSetConcurrency));
      }

      
      public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
         throws SQLException
      {
         return new StatementInterceptor(this,
            target.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
      }
      

      public Map getTypeMap() throws SQLException
      {
         return target.getTypeMap();
      }

      public void setTypeMap(Map map) throws SQLException
      {
         target.setTypeMap(map);
      }

      public String nativeSQL(String sql) throws SQLException
      {
         return target.nativeSQL(sql);
      }

      public CallableStatement prepareCall(String sql) throws SQLException
      {
         return target.prepareCall(sql);
      }

      public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
         throws SQLException
      {
         return target.prepareCall(sql, resultSetType, resultSetConcurrency);
      }

      
      public CallableStatement prepareCall(String sql,
                                           int resultSetType,
                                           int resultSetConcurrency,
                                           int resultSetHoldability) throws SQLException
      {
         return target.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
      }
      

      public PreparedStatement prepareStatement(String sql) throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this, target.prepareStatement(sql));
      }

      
      public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this, target.prepareStatement(sql, autoGeneratedKeys));
      }
      

      public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
         throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this,
            target.prepareStatement(sql, resultSetType, resultSetConcurrency));
      }

      
      public PreparedStatement prepareStatement(String sql,
                                                int resultSetType,
                                                int resultSetConcurrency,
                                                int resultSetHoldability) throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this,
            target.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
      }
      

      
      public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this, target.prepareStatement(sql, columnIndexes));
      }
      

      
      public Savepoint setSavepoint(String name) throws SQLException
      {
         return target.setSavepoint(name);
      }
      

      
      public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException
      {
         logSql(sql);
         return new PreparedStatementInterceptor(this, target.prepareStatement(sql, columnNames));
      }


	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class iface) throws SQLException {
		return target.unwrap(iface);
	}


	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		return target.isWrapperFor(iface);
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createClob()
	 */
	public Clob createClob() throws SQLException {
		return target.createClob();
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createBlob()
	 */
	public Blob createBlob() throws SQLException {
		return target.createBlob();
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createNClob()
	 */
	public NClob createNClob() throws SQLException {
		return target.createNClob();
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createSQLXML()
	 */
	public SQLXML createSQLXML() throws SQLException {
		return target.createSQLXML();
	}


	/**
	 * @param timeout
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#isValid(int)
	 */
	public boolean isValid(int timeout) throws SQLException {
		return target.isValid(timeout);
	}


	/**
	 * @param name
	 * @param value
	 * @throws SQLClientInfoException
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		target.setClientInfo(name, value);
	}


	/**
	 * @param properties
	 * @throws SQLClientInfoException
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		target.setClientInfo(properties);
	}


	/**
	 * @param name
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
	public String getClientInfo(String name) throws SQLException {
		return target.getClientInfo(name);
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getClientInfo()
	 */
	public Properties getClientInfo() throws SQLException {
		return target.getClientInfo();
	}


	/**
	 * @param typeName
	 * @param elements
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
	 */
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return target.createArrayOf(typeName, elements);
	}


	/**
	 * @param typeName
	 * @param attributes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
	 */
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return target.createStruct(typeName, attributes);
	}


	/**
	 * @param schema
	 * @throws SQLException
	 * @see java.sql.Connection#setSchema(java.lang.String)
	 */
	public void setSchema(String schema) throws SQLException {
		target.setSchema(schema);
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getSchema()
	 */
	public String getSchema() throws SQLException {
		return target.getSchema();
	}


	/**
	 * @param executor
	 * @throws SQLException
	 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
	 */
	public void abort(Executor executor) throws SQLException {
		target.abort(executor);
	}


	/**
	 * @param executor
	 * @param milliseconds
	 * @throws SQLException
	 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor, int)
	 */
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		target.setNetworkTimeout(executor, milliseconds);
	}


	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Connection#getNetworkTimeout()
	 */
	public int getNetworkTimeout() throws SQLException {
		return target.getNetworkTimeout();
	}
      
      
      
   }

   public class StatementInterceptor
      implements Statement
   {
      private final Connection con;
      private final Statement target;

      public StatementInterceptor(Connection con, Statement target)
      {
         this.con = con;
         this.target = target;
      }

      public int getFetchDirection() throws SQLException
      {
         return target.getFetchDirection();
      }

      public int getFetchSize() throws SQLException
      {
         return target.getFetchSize();
      }

      public int getMaxFieldSize() throws SQLException
      {
         return target.getMaxFieldSize();
      }

      public int getMaxRows() throws SQLException
      {
         return target.getMaxRows();
      }

      public int getQueryTimeout() throws SQLException
      {
         return target.getQueryTimeout();
      }

      public int getResultSetConcurrency() throws SQLException
      {
         return target.getResultSetConcurrency();
      }

      
      public int getResultSetHoldability() throws SQLException
      {
         return target.getResultSetHoldability();
      }
      

      public int getResultSetType() throws SQLException
      {
         return target.getResultSetType();
      }

      public int getUpdateCount() throws SQLException
      {
         return target.getUpdateCount();
      }

      public void cancel() throws SQLException
      {
         target.cancel();
      }

      public void clearBatch() throws SQLException
      {
         target.clearBatch();
      }

      public void clearWarnings() throws SQLException
      {
         target.clearWarnings();
      }

      public void close() throws SQLException
      {
         target.close();
      }

      public boolean getMoreResults() throws SQLException
      {
         return target.getMoreResults();
      }

      public int[] executeBatch() throws SQLException
      {
         return target.executeBatch();
      }

      public void setFetchDirection(int direction) throws SQLException
      {
         target.setFetchDirection(direction);
      }

      public void setFetchSize(int rows) throws SQLException
      {
         target.setFetchSize(rows);
      }

      public void setMaxFieldSize(int max) throws SQLException
      {
         target.setMaxFieldSize(max);
      }

      public void setMaxRows(int max) throws SQLException
      {
         target.setMaxRows(max);
      }

      public void setQueryTimeout(int seconds) throws SQLException
      {
         target.setQueryTimeout(seconds);
      }

      
      public boolean getMoreResults(int current) throws SQLException
      {
         return target.getMoreResults(current);
      }
      

      public void setEscapeProcessing(boolean enable) throws SQLException
      {
         target.setEscapeProcessing(enable);
      }

      public int executeUpdate(String sql) throws SQLException
      {
         logSql(sql);
         return target.executeUpdate(sql);
      }

      public void addBatch(String sql) throws SQLException
      {
         logSql(sql);
         target.addBatch(sql);
      }

      public void setCursorName(String name) throws SQLException
      {
         target.setCursorName(name);
      }

      public boolean execute(String sql) throws SQLException
      {
         logSql(sql);
         return target.execute(sql);
      }

      
      public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
      {
         logSql(sql);
         return target.executeUpdate(sql, autoGeneratedKeys);
      }
      

      
      public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
      {
         logSql(sql);
         return target.execute(sql, autoGeneratedKeys);
      }
      

      
      public int executeUpdate(String sql, int columnIndexes[]) throws SQLException
      {
         logSql(sql);
         return target.executeUpdate(sql, columnIndexes);
      }
      

      
      public boolean execute(String sql, int columnIndexes[]) throws SQLException
      {
         logSql(sql);
         return target.execute(sql, columnIndexes);
      }
      

      public Connection getConnection() throws SQLException
      {
         return con;
      }

      
      public ResultSet getGeneratedKeys() throws SQLException
      {
         return target.getGeneratedKeys();
      }
      

      public ResultSet getResultSet() throws SQLException
      {
         return target.getResultSet();
      }

      public SQLWarning getWarnings() throws SQLException
      {
         return target.getWarnings();
      }

      
      public int executeUpdate(String sql, String columnNames[]) throws SQLException
      {
         logSql(sql);
         return target.executeUpdate(sql, columnNames);
      }
      

      
      public boolean execute(String sql, String columnNames[]) throws SQLException
      {
         logSql(sql);
         return target.execute(sql, columnNames);
      }
      

      public ResultSet executeQuery(String sql) throws SQLException
      {
         logSql(sql);
         return target.executeQuery(sql);
      }

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class iface) throws SQLException {
		return target.unwrap(iface);
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		return target.isWrapperFor(iface);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return target.isClosed();
	}

	/**
	 * @param poolable
	 * @throws SQLException
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	public void setPoolable(boolean poolable) throws SQLException {
		target.setPoolable(poolable);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isPoolable()
	 */
	public boolean isPoolable() throws SQLException {
		return target.isPoolable();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		target.closeOnCompletion();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		return target.isCloseOnCompletion();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeUpdateCount()
	 */
	public long getLargeUpdateCount() throws SQLException {
		return target.getLargeUpdateCount();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setLargeMaxRows(long)
	 */
	public void setLargeMaxRows(long max) throws SQLException {
		target.setLargeMaxRows(max);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeMaxRows()
	 */
	public long getLargeMaxRows() throws SQLException {
		return target.getLargeMaxRows();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeBatch()
	 */
	public long[] executeLargeBatch() throws SQLException {
		return target.executeLargeBatch();
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String)
	 */
	public long executeLargeUpdate(String sql) throws SQLException {
		return target.executeLargeUpdate(sql);
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
		return target.executeLargeUpdate(sql, autoGeneratedKeys);
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
		return target.executeLargeUpdate(sql, columnIndexes);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, java.lang.String[])
	 */
	public long executeLargeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return target.executeLargeUpdate(sql, columnNames);
	}
      
      
      
   }

   public class PreparedStatementInterceptor
      extends StatementInterceptor
      implements PreparedStatement
   {
      private final PreparedStatement target;

      public PreparedStatementInterceptor(Connection con, PreparedStatement target)
      {
         super(con, target);
         this.target = target;
      }

      public int executeUpdate() throws SQLException
      {
         return target.executeUpdate();
      }

      public void addBatch() throws SQLException
      {
         target.addBatch();
      }

      public void clearParameters() throws SQLException
      {
         target.clearParameters();
      }

      public boolean execute() throws SQLException
      {
         return target.execute();
      }

      public void setByte(int parameterIndex, byte x) throws SQLException
      {
         target.setByte(parameterIndex, x);
      }

      public void setDouble(int parameterIndex, double x) throws SQLException
      {
         target.setDouble(parameterIndex, x);
      }

      public void setFloat(int parameterIndex, float x) throws SQLException
      {
         target.setFloat(parameterIndex, x);
      }

      public void setInt(int parameterIndex, int x) throws SQLException
      {
         target.setInt(parameterIndex, x);
      }

      public void setNull(int parameterIndex, int sqlType) throws SQLException
      {
         target.setNull(parameterIndex, sqlType);
      }

      public void setLong(int parameterIndex, long x) throws SQLException
      {
         target.setLong(parameterIndex, x);
      }

      public void setShort(int parameterIndex, short x) throws SQLException
      {
         target.setShort(parameterIndex, x);
      }

      public void setBoolean(int parameterIndex, boolean x) throws SQLException
      {
         target.setBoolean(parameterIndex, x);
      }

      public void setBytes(int parameterIndex, byte x[]) throws SQLException
      {
         target.setBytes(parameterIndex, x);
      }

      public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
      {
         target.setAsciiStream(parameterIndex, x, length);
      }

      public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
      {
         target.setBinaryStream(parameterIndex, x, length);
      }

      public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
      {
         target.setUnicodeStream(parameterIndex, x, length);
      }

      public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
      {
         target.setCharacterStream(parameterIndex, reader, length);
      }

      public void setObject(int parameterIndex, Object x) throws SQLException
      {
         target.setObject(parameterIndex, x);
      }

      public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
      {
         target.setObject(parameterIndex, x, targetSqlType);
      }

      public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException
      {
         target.setObject(parameterIndex, x, targetSqlType, scale);
      }

      public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException
      {
         target.setNull(paramIndex, sqlType, typeName);
      }

      public void setString(int parameterIndex, String x) throws SQLException
      {
         target.setString(parameterIndex, x);
      }

      public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
      {
         target.setBigDecimal(parameterIndex, x);
      }

      
      public void setURL(int parameterIndex, URL x) throws SQLException
      {
         target.setURL(parameterIndex, x);
      }
      

      public void setArray(int i, Array x) throws SQLException
      {
         target.setArray(i, x);
      }

      public void setBlob(int i, Blob x) throws SQLException
      {
         target.setBlob(i, x);
      }

      public void setClob(int i, Clob x) throws SQLException
      {
         target.setClob(i, x);
      }

      public void setDate(int parameterIndex, Date x) throws SQLException
      {
         target.setDate(parameterIndex, x);
      }

      
      public ParameterMetaData getParameterMetaData() throws SQLException
      {
         return target.getParameterMetaData();
      }
      

      public void setRef(int i, Ref x) throws SQLException
      {
         target.setRef(i, x);
      }

      public ResultSet executeQuery() throws SQLException
      {
         return target.executeQuery();
      }

      public ResultSetMetaData getMetaData() throws SQLException
      {
         return target.getMetaData();
      }

      public void setTime(int parameterIndex, Time x) throws SQLException
      {
         target.setTime(parameterIndex, x);
      }

      public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
      {
         target.setTimestamp(parameterIndex, x);
      }

      public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
      {
         target.setDate(parameterIndex, x, cal);
      }

      public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
      {
         target.setTime(parameterIndex, x, cal);
      }

      public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
      {
         target.setTimestamp(parameterIndex, x, cal);
      }

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class iface) throws SQLException {
		return target.unwrap(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		return target.executeQuery(sql);
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		return target.isWrapperFor(iface);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	public int executeUpdate(String sql) throws SQLException {
		return target.executeUpdate(sql);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#close()
	 */
	public void close() throws SQLException {
		target.close();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	public int getMaxFieldSize() throws SQLException {
		return target.getMaxFieldSize();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	public void setMaxFieldSize(int max) throws SQLException {
		target.setMaxFieldSize(max);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMaxRows()
	 */
	public int getMaxRows() throws SQLException {
		return target.getMaxRows();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	public void setMaxRows(int max) throws SQLException {
		target.setMaxRows(max);
	}

	/**
	 * @param enable
	 * @throws SQLException
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	public void setEscapeProcessing(boolean enable) throws SQLException {
		target.setEscapeProcessing(enable);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	public int getQueryTimeout() throws SQLException {
		return target.getQueryTimeout();
	}

	/**
	 * @param seconds
	 * @throws SQLException
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	public void setQueryTimeout(int seconds) throws SQLException {
		target.setQueryTimeout(seconds);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#cancel()
	 */
	public void cancel() throws SQLException {
		target.cancel();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException {
		return target.getWarnings();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		target.clearWarnings();
	}

	/**
	 * @param name
	 * @throws SQLException
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	public void setCursorName(String name) throws SQLException {
		target.setCursorName(name);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	public boolean execute(String sql) throws SQLException {
		return target.execute(sql);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSet()
	 */
	public ResultSet getResultSet() throws SQLException {
		return target.getResultSet();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getUpdateCount()
	 */
	public int getUpdateCount() throws SQLException {
		return target.getUpdateCount();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults()
	 */
	public boolean getMoreResults() throws SQLException {
		return target.getMoreResults();
	}

	/**
	 * @param direction
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	public void setFetchDirection(int direction) throws SQLException {
		target.setFetchDirection(direction);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchDirection()
	 */
	public int getFetchDirection() throws SQLException {
		return target.getFetchDirection();
	}

	/**
	 * @param rows
	 * @throws SQLException
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	public void setFetchSize(int rows) throws SQLException {
		target.setFetchSize(rows);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getFetchSize()
	 */
	public int getFetchSize() throws SQLException {
		return target.getFetchSize();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	public int getResultSetConcurrency() throws SQLException {
		return target.getResultSetConcurrency();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetType()
	 */
	public int getResultSetType() throws SQLException {
		return target.getResultSetType();
	}

	/**
	 * @param sql
	 * @throws SQLException
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	public void addBatch(String sql) throws SQLException {
		target.addBatch(sql);
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		target.clearBatch();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeBatch()
	 */
	public int[] executeBatch() throws SQLException {
		return target.executeBatch();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return target.getConnection();
	}

	/**
	 * @param current
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	public boolean getMoreResults(int current) throws SQLException {
		return target.getMoreResults(current);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return target.getGeneratedKeys();
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
		return target.executeUpdate(sql, autoGeneratedKeys);
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
		return target.executeUpdate(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		target.setRowId(parameterIndex, x);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return target.executeUpdate(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		target.setNString(parameterIndex, value);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
	 */
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		target.setNCharacterStream(parameterIndex, value, length);
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
		return target.execute(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		target.setNClob(parameterIndex, value);
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
		target.setClob(parameterIndex, reader, length);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return target.execute(sql, columnIndexes);
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
		target.setBlob(parameterIndex, inputStream, length);
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
		target.setNClob(parameterIndex, reader, length);
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
		return target.execute(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param xmlObject
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		target.setSQLXML(parameterIndex, xmlObject);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	public int getResultSetHoldability() throws SQLException {
		return target.getResultSetHoldability();
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isClosed()
	 */
	public boolean isClosed() throws SQLException {
		return target.isClosed();
	}

	/**
	 * @param poolable
	 * @throws SQLException
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	public void setPoolable(boolean poolable) throws SQLException {
		target.setPoolable(poolable);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		target.setAsciiStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isPoolable()
	 */
	public boolean isPoolable() throws SQLException {
		return target.isPoolable();
	}

	/**
	 * @throws SQLException
	 * @see java.sql.Statement#closeOnCompletion()
	 */
	public void closeOnCompletion() throws SQLException {
		target.closeOnCompletion();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		target.setBinaryStream(parameterIndex, x, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#isCloseOnCompletion()
	 */
	public boolean isCloseOnCompletion() throws SQLException {
		return target.isCloseOnCompletion();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @param length
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		target.setCharacterStream(parameterIndex, reader, length);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeUpdateCount()
	 */
	public  long getLargeUpdateCount() throws SQLException {
		return target.getLargeUpdateCount();
	}

	/**
	 * @param max
	 * @throws SQLException
	 * @see java.sql.Statement#setLargeMaxRows(long)
	 */
	public  void setLargeMaxRows(long max) throws SQLException {
		target.setLargeMaxRows(max);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		target.setAsciiStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#getLargeMaxRows()
	 */
	public  long getLargeMaxRows() throws SQLException {
		return target.getLargeMaxRows();
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		target.setBinaryStream(parameterIndex, x);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeBatch()
	 */
	public  long[] executeLargeBatch() throws SQLException {
		return target.executeLargeBatch();
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		target.setCharacterStream(parameterIndex, reader);
	}

	/**
	 * @param parameterIndex
	 * @param value
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		target.setNCharacterStream(parameterIndex, value);
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String)
	 */
	public  long executeLargeUpdate(String sql) throws SQLException {
		return target.executeLargeUpdate(sql);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		target.setClob(parameterIndex, reader);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, int)
	 */
	public  long executeLargeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return target.executeLargeUpdate(sql, autoGeneratedKeys);
	}

	/**
	 * @param parameterIndex
	 * @param inputStream
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		target.setBlob(parameterIndex, inputStream);
	}

	/**
	 * @param parameterIndex
	 * @param reader
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		target.setNClob(parameterIndex, reader);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, int[])
	 */
	public  long executeLargeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return target.executeLargeUpdate(sql, columnIndexes);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @param scaleOrLength
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, java.sql.SQLType, int)
	 */
	public  void setObject(int parameterIndex, Object x,
			SQLType targetSqlType, int scaleOrLength) throws SQLException {
		target.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws SQLException
	 * @see java.sql.Statement#executeLargeUpdate(java.lang.String, java.lang.String[])
	 */
	public  long executeLargeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return target.executeLargeUpdate(sql, columnNames);
	}

	/**
	 * @param parameterIndex
	 * @param x
	 * @param targetSqlType
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, java.sql.SQLType)
	 */
	public  void setObject(int parameterIndex, Object x,
			SQLType targetSqlType) throws SQLException {
		target.setObject(parameterIndex, x, targetSqlType);
	}

	/**
	 * @return
	 * @throws SQLException
	 * @see java.sql.PreparedStatement#executeLargeUpdate()
	 */
	public  long executeLargeUpdate() throws SQLException {
		return target.executeLargeUpdate();
	}
      
      
   }

   // Private

   private void logSql(String sql)
   {
      try
      {
         StatisticalItem item = new TxReport.SqlStats(sql);
         server.invoke(statsCollector, "addStatisticalItem",
            new Object[]{item},
            new String[]{StatisticalItem.class.getName()});
      }
      catch(Exception e)
      {
         log.error("Failed to add invocation.", e);
      }
   }

   private void bind()
      throws NamingException
   {
      InitialContext ic = null;
      try
      {
         ic = new InitialContext();
         Name name = ic.getNameParser("").parse(bindName);
         NonSerializableFactory.rebind(name, this, true);
         log.debug("bound to JNDI name " + bindName);
      }
      finally
      {
         if(ic != null)
         {
            ic.close();
         }
      }
   }

   private void unbind()
      throws NamingException
   {
      InitialContext ic = null;
      try
      {
         ic = new InitialContext();
         ic.unbind(bindName);
         NonSerializableFactory.unbind(bindName);
      }
      finally
      {
         if(ic != null)
         {
            ic.close();
         }
      }
   }

   private void updateTarget()
      throws NamingException
   {
      InitialContext ic = null;
      try
      {
         ic = new InitialContext();
         target = (DataSource) ic.lookup(targetName);
         log.debug("target updated to " + targetName);
      }
      finally
      {
         if(ic != null)
         {
            ic.close();
         }
      }
   }
   
   public Logger getParentLogger()  throws SQLFeatureNotSupportedException {
	   return ((CommonDataSource)target).getParentLogger();
   }

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public Object unwrap(Class iface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	/**
	 * @param iface
	 * @return
	 * @throws SQLException
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class iface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
   
}
