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
package org.jboss.ejb.plugins.cmp.ejbql;

import org.jboss.ejb.plugins.cmp.jdbc.JDBCUtil;
import org.jboss.ejb.plugins.cmp.jdbc.JDBCResultSetReader;
import org.jboss.logging.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 57209 $</tt>
 */
public abstract class AggregateFunction
   extends SimpleNode
   implements SelectFunction
{
   private final Logger log;
   private JDBCResultSetReader resultReader;
   private Class resultType;

   public String distinct = "";

   public AggregateFunction(int i)
   {
      super(i);
      log = Logger.getLogger(getClass());
   }

   public void setResultType(Class type)
   {
      if(Collection.class.isAssignableFrom(type))
      {
         resultType = getDefaultResultType();
      }
      else
      {
         this.resultType = type;
      }
      this.resultReader = JDBCUtil.getResultReaderByType(resultType);
   }

   protected Class getDefaultResultType()
   {
      return Double.class;
   }

   // SelectFunction implementation

   public Object readResult(ResultSet rs) throws SQLException
   {
      return resultReader.get(rs, 1, resultType, log);
   }
}
