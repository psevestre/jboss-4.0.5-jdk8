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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.util.Map;
import java.sql.SQLWarning;
import java.sql.Savepoint;


/**
 * TestConnection.java
 *
 *
 * Created: Fri Feb 14 13:19:39 2003
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version
 */

public class TestConnection implements Connection {

   private TestDriver driver;

   private boolean autocommit;

   private boolean closed;

   public TestConnection(TestDriver driver)
   {
      this.driver = driver;
   }

   public void setFail(boolean fail)
   {
      driver.setFail(fail);
   }

   public int getClosedCount()
   {
      return driver.getClosedCount();
   }

   // Implementation of java.sql.Connection

   public Statement createStatement(int n, int n1, int n2) throws SQLException {
      return null;
   }

   public void clearWarnings()
   {
   }

   public void close()
   {
      closed = true;
      driver.connectionClosed();
   }

   public void commit()
   {
   }

   public Statement createStatement() throws SQLException
   {
      return new TestStatement(driver);
   }

   public Statement createStatement(int rst, int rsc) throws SQLException
   {
      return null;
   }

   public boolean getAutoCommit()
   {
      return autocommit;
   }

   public void setAutoCommit(boolean autocommit)
   {
      this.autocommit = autocommit;
   }

   public String getCatalog()
   {
      return null;
   }

   public DatabaseMetaData getMetaData()
   {
      return null;
   }

   public int getTransactionIsolation()
   {
      return 0;
   }

   public Map getTypeMap()
   {
      return null;
   }

   public SQLWarning getWarnings()
   {
      return null;
   }

   public boolean isClosed()
   {
      return closed;
   }

   public boolean isReadOnly()
   {
      return false;
   }

   public String nativeSQL(String sql)
   {
      return sql;
   }

   public CallableStatement prepareCall(String sql)
   {
      return null;
   }

   public CallableStatement prepareCall(String sql, int rst)
   {
      return null;
   }

   public CallableStatement prepareCall(String sql, int[] rst)
   {
      return null;
   }

   public CallableStatement prepareCall(String sql, int rst, int rsc)
   {
      return null;
   }

   public CallableStatement prepareCall(String sql, String[] rst)
   {
      return null;
   }

   public CallableStatement prepareCall(String sql, int rst, int rsc, int i)
   {
      return null;
   }

   public PreparedStatement prepareStatement(String sql)
   {
      return new TestPreparedStatement(driver);
   }

   public PreparedStatement prepareStatement(String sql, int rst, int rsc)
   {
      return new TestPreparedStatement(driver);
   }

   public PreparedStatement prepareStatement(String sql, int rst)
   {
      return null;
   }

   public PreparedStatement prepareStatement(String sql, int[] rst)
   {
      return null;
   }

   public PreparedStatement prepareStatement(String sql, String[] rst)
   {
      return null;
   }

   public PreparedStatement prepareStatement(String sql, int rst, int rsc, int i)
   {
      return null;
   }

   public void rollback()
   {
   }

   public void setCatalog(String cat)
   {
   }

   public void setReadOnly(boolean r0)
   {
   }

   public void setTransactionIsolation(int level)
   {
   }

   public void setTypeMap(Map map)
   {
   }

   public void setHoldability(int h)
   {
   }

   public int getHoldability()
   {
      return 0;
   }

   public Savepoint setSavepoint()
   {
      return null;
   }

   public Savepoint setSavepoint(String name)
   {
      return null;
   }

   public void rollback(Savepoint s)
   {
   }
   public void commit(Savepoint s)
   {
   }

   public void releaseSavepoint(Savepoint s)
   {
   }

}// TestConnection
