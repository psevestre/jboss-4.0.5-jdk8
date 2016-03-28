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
package org.jboss.test.jca.adapter;

import javax.sql.PooledConnection;
import javax.sql.StatementEventListener;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.sql.ConnectionEventListener;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Struct;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 57211 $</tt>
 */
public class MockedXADataSource
   implements XADataSource
{
   private static final Map instances = new HashMap();
   
   public Logger getParentLogger()  throws SQLFeatureNotSupportedException {
	   throw new SQLFeatureNotSupportedException();
   }
   

   public static MockedXADataSource getInstance(String url)
   {
      return (MockedXADataSource)instances.get(url);
   }

   public static void stop(String url)
   {
      getInstance(url).stopped = true;
   }

   public static void start(String url)
   {
      getInstance(url).stopped = false;
   }

   public static String[] getUrls()
   {
      return (String[])instances.keySet().toArray(new String[instances.size()]);
   }

   private String url;
   private boolean stopped;
   private int loginTimeout;
   private PrintWriter logWriter;

   public String getURL()
   {
      return url;
   }

   public void setURL(String url)
   {
      this.url = url;
      instances.put(url, this);
   }

   public int getLoginTimeout() throws SQLException
   {
      return loginTimeout;
   }

   public void setLoginTimeout(int seconds) throws SQLException
   {
      this.loginTimeout = seconds;
   }

   public PrintWriter getLogWriter() throws SQLException
   {
      return logWriter;
   }

   public void setLogWriter(PrintWriter out) throws SQLException
   {
      this.logWriter = out;
   }

   public XAConnection getXAConnection() throws SQLException
   {
      return new MockedXAConnection();
   }

   public XAConnection getXAConnection(String user, String password) throws SQLException
   {
      return new MockedXAConnection();
   }

   // Inner

   public class MockedXAConnection
      implements XAConnection
   {
      private boolean closed;
      private Connection con = new MockedConnection();
      private XAResource xaResource = new MockedXAResource();

      public XAResource getXAResource() throws SQLException
      {
         return xaResource;
      }

      public void close() throws SQLException
      {
         closed = true;
      }

      public Connection getConnection() throws SQLException
      {
         return con;
      }

      public void addConnectionEventListener(ConnectionEventListener listener)
      {
      }

      public void removeConnectionEventListener(ConnectionEventListener listener)
      {
      }
      
      public void addStatementEventListener(StatementEventListener listener) {
    	  
      }

      public void removeStatementEventListener(StatementEventListener listener) {
    	  
      }
      
      class MockedConnection
         implements Connection
      {
         private int holdability;
         private int txIsolation;
         private boolean autoCommit;
         private boolean readOnly;
         private String catalog;

         public String getUrl()
         {
            return url;
         }

         public int getHoldability() throws SQLException
         {
            check();
            return holdability;
         }

         public int getTransactionIsolation() throws SQLException
         {
            check();
            return txIsolation;
         }

         public void clearWarnings() throws SQLException
         {
            check();
         }

         public void close() throws SQLException
         {
            check();
            closed = true;
         }

         public void commit() throws SQLException
         {
            check();
         }

         public void rollback() throws SQLException
         {
            check();
         }

         public boolean getAutoCommit() throws SQLException
         {
            check();
            return autoCommit;
         }

         public boolean isClosed() throws SQLException
         {
            check();
            return closed;
         }

         public boolean isReadOnly() throws SQLException
         {
            check();
            return readOnly;
         }

         public void setHoldability(int holdability) throws SQLException
         {
            check();
            this.holdability = holdability;
         }

         public void setTransactionIsolation(int level) throws SQLException
         {
            check();
            this.txIsolation = level;
         }

         public void setAutoCommit(boolean autoCommit) throws SQLException
         {
            check();
            this.autoCommit = autoCommit;
         }

         public void setReadOnly(boolean readOnly) throws SQLException
         {
            check();
            this.readOnly = readOnly;
         }

         public String getCatalog() throws SQLException
         {
            check();
            return catalog;
         }

         public void setCatalog(String catalog) throws SQLException
         {
            check();
            this.catalog = catalog;
         }

         public DatabaseMetaData getMetaData() throws SQLException
         {
            check();
            return (DatabaseMetaData)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
               new Class[]{DatabaseMetaData.class},
               new InvocationHandler()
               {
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                  {
                     if("getURL".equals(method.getName()))
                     {
                        return url;
                     }

                     return new UnsupportedOperationException(
                        "Not implemented: method=" +
                        method.getName() +
                        ", args=" +
                        (args == null ? (Object)"null" : Arrays.asList(args))
                     );
                  }
               }
            );
         }

         public SQLWarning getWarnings() throws SQLException
         {
            check();
            return null;
         }

         public Savepoint setSavepoint() throws SQLException
         {
            check();
            throw new UnsupportedOperationException("setSavepoint() is not implemented.");
         }

         public void releaseSavepoint(Savepoint savepoint) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("releaseSavepoint() is not implemented.");
         }

         public void rollback(Savepoint savepoint) throws SQLException
         {
            check();
         }

         public Statement createStatement() throws SQLException
         {
            check();
            return (Statement)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
               new Class[]{Statement.class},
               new InvocationHandler()
               {
                  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                  {
                     String methodName = method.getName();
                     if("execute".equals(methodName))
                     {
                        // let's suppose it went well!
                        return Boolean.FALSE;
                     }

                     return new UnsupportedOperationException(
                        "Not implemented: method=" +
                        methodName +
                        ", args=" +
                        (args == null ? (Object)"null" : Arrays.asList(args))
                     );
                  }
               }
            );
         }

         public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public Map getTypeMap() throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public void setTypeMap(Map map) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public String nativeSQL(String sql) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public CallableStatement prepareCall(String sql) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public CallableStatement prepareCall(String sql,
                                              int resultSetType,
                                              int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql,
                                                   int resultSetType,
                                                   int resultSetConcurrency,
                                                   int resultSetHoldability) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public Savepoint setSavepoint(String name) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException
         {
            check();
            throw new UnsupportedOperationException("Not implemented.");
         }

         // Private

         private void check() throws SQLException
         {
            if(stopped)
            {
               throw new SQLException("The database is not available: " + url);
            }
         }

		/* (non-Javadoc)
		 * @see java.sql.Wrapper#unwrap(java.lang.Class)
		 */
		public Object unwrap(Class iface) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
		 */
		public boolean isWrapperFor(Class iface) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createClob()
		 */
		public Clob createClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createBlob()
		 */
		public Blob createBlob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createNClob()
		 */
		public NClob createNClob() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createSQLXML()
		 */
		public SQLXML createSQLXML() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isValid(int)
		 */
		public boolean isValid(int timeout) throws SQLException {
			// TODO Auto-generated method stub
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
		 */
		public void setClientInfo(String name, String value)
				throws SQLClientInfoException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setClientInfo(java.util.Properties)
		 */
		public void setClientInfo(Properties properties)
				throws SQLClientInfoException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getClientInfo(java.lang.String)
		 */
		public String getClientInfo(String name) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getClientInfo()
		 */
		public Properties getClientInfo() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
		 */
		public Array createArrayOf(String typeName, Object[] elements)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
		 */
		public Struct createStruct(String typeName, Object[] attributes)
				throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setSchema(java.lang.String)
		 */
		public void setSchema(String schema) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getSchema()
		 */
		public String getSchema() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#abort(java.util.concurrent.Executor)
		 */
		public void abort(Executor executor) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setNetworkTimeout(java.util.concurrent.Executor, int)
		 */
		public void setNetworkTimeout(Executor executor, int milliseconds)
				throws SQLException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getNetworkTimeout()
		 */
		public int getNetworkTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}
         
         
      }
   }

   private static class MockedXAResource
      implements XAResource
   {
      private int txTimeOut;

      public int getTransactionTimeout() throws XAException
      {
         return txTimeOut;
      }

      public boolean setTransactionTimeout(int i) throws XAException
      {
         this.txTimeOut = i;
         return true;
      }

      public boolean isSameRM(XAResource xaResource) throws XAException
      {
         return xaResource instanceof MockedXAResource;
      }

      public Xid[] recover(int i) throws XAException
      {
         throw new UnsupportedOperationException("recover is not implemented.");
      }

      public int prepare(Xid xid) throws XAException
      {
         return XAResource.XA_OK;
      }

      public void forget(Xid xid) throws XAException
      {
      }

      public void rollback(Xid xid) throws XAException
      {
      }

      public void end(Xid xid, int i) throws XAException
      {
      }

      public void start(Xid xid, int i) throws XAException
      {
      }

      public void commit(Xid xid, boolean b) throws XAException
      {
      }
      
      
   }
}
