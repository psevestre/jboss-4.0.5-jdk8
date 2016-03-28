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
package org.jboss.resource.adapter.jdbc.xa;

import org.jboss.resource.adapter.jdbc.BaseWrapperManagedConnectionFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditor;
import javax.sql.XADataSource;
import javax.sql.XAConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.security.auth.Subject;
import org.jboss.resource.JBossResourceException;

/**
 * XAManagedConnectionFactory
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @author <a href="weston.price@jboss.com">Weston Price</a>
 * 
 * @version $Revision: 57189 $
 */
public class XAManagedConnectionFactory extends BaseWrapperManagedConnectionFactory
{
   private static final long serialVersionUID = 1647927657609573729L;

   private String xaDataSourceClass;

   private String xaDataSourceProperties;

   protected final Properties xaProps = new Properties();

   private Boolean isSameRMOverrideValue;

   private XADataSource xads;

   public XAManagedConnectionFactory()
   {
   }

   /**
    * Get the XaDataSourceClass value.
    * 
    * @return the XaDataSourceClass value.
    */
   public String getXADataSourceClass()
   {
      return xaDataSourceClass;
   }

   /**
    * Set the XaDataSourceClass value.
    * 
    * @param xaDataSourceClass The new XaDataSourceClass value.
    */
   public void setXADataSourceClass(String xaDataSourceClass)
   {
      this.xaDataSourceClass = xaDataSourceClass;
   }

   /**
    * Get the XADataSourceProperties value.
    * 
    * @return the XADataSourceProperties value.
    */
   public String getXADataSourceProperties()
   {
      return xaDataSourceProperties;
   }

   /**
    * Set the XADataSourceProperties value.
    * 
    * @param xaDataSourceProperties The new XADataSourceProperties value.
    */
   public void setXADataSourceProperties(String xaDataSourceProperties) throws ResourceException
   {
      this.xaDataSourceProperties = xaDataSourceProperties;
      xaProps.clear();
      if (xaDataSourceProperties != null)
      {
         // Map any \ to \\
         xaDataSourceProperties = xaDataSourceProperties.replaceAll("\\\\", "\\\\\\\\");

         InputStream is = new ByteArrayInputStream(xaDataSourceProperties.getBytes());
         try
         {
            xaProps.load(is);
         }
         catch (IOException ioe)
         {
            throw new JBossResourceException("Could not load connection properties", ioe);
         }
      }
   }

   /**
    * Get the IsSameRMOverrideValue value.
    * 
    * @return the IsSameRMOverrideValue value.
    */
   public Boolean getIsSameRMOverrideValue()
   {
      return isSameRMOverrideValue;
   }

   /**
    * Set the IsSameRMOverrideValue value.
    * 
    * @param isSameRMOverrideValue The new IsSameRMOverrideValue value.
    */
   public void setIsSameRMOverrideValue(Boolean isSameRMOverrideValue)
   {
      this.isSameRMOverrideValue = isSameRMOverrideValue;
   }

   public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cri)
         throws javax.resource.ResourceException
   {
      Properties props = getConnectionProperties(subject, cri);
      try
      {
         final String user = props.getProperty("user");
         final String password = props.getProperty("password");

         XAConnection xaConnection = (user != null)
               ? getXADataSource().getXAConnection(user, password)
               : getXADataSource().getXAConnection();

         return newXAManagedConnection(props, xaConnection);
      }
      catch (Exception e)
      {
         throw new JBossResourceException("Could not create connection", e);
      }
   }

   /**
    * This method can be overwritten by sublcasses to provide rm specific
    * implementation of XAManagedConnection
    */
   protected ManagedConnection newXAManagedConnection(Properties props, XAConnection xaConnection) throws SQLException
   {
      return new XAManagedConnection(this, xaConnection, props, transactionIsolation, preparedStatementCacheSize);
   }

   public ManagedConnection matchManagedConnections(Set mcs, Subject subject, ConnectionRequestInfo cri)
         throws ResourceException
   {
      Properties newProps = getConnectionProperties(subject, cri);
      for (Iterator i = mcs.iterator(); i.hasNext();)
      {
         Object o = i.next();
         if (o instanceof XAManagedConnection)
         {
            XAManagedConnection mc = (XAManagedConnection) o;
          
            if (mc.getProps().equals(newProps))
            {
               //Next check to see if we are validating on matchManagedConnections
               if((getValidateOnMatch() && mc.checkValid()) || !getValidateOnMatch())
               {
                
                  return mc;

               }            
               
            }
        
         }
      }
      return null;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 37 + ((xaDataSourceClass == null) ? 0 : xaDataSourceClass.hashCode());
      result = result * 37 + xaProps.hashCode();
      result = result * 37 + ((userName == null) ? 0 : userName.hashCode());
      result = result * 37 + ((password == null) ? 0 : password.hashCode());
      result = result * 37 + transactionIsolation;
      return result;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
      if (getClass() != other.getClass())
         return false;
      XAManagedConnectionFactory otherMcf = (XAManagedConnectionFactory) other;
      return this.xaDataSourceClass.equals(otherMcf.xaDataSourceClass) && this.xaProps.equals(otherMcf.xaProps)
            && ((this.userName == null) ? otherMcf.userName == null : this.userName.equals(otherMcf.userName))
            && ((this.password == null) ? otherMcf.password == null : this.password.equals(otherMcf.password))
            && this.transactionIsolation == otherMcf.transactionIsolation;

   }

   protected synchronized XADataSource getXADataSource() throws ResourceException
   {
      if (xads == null)
      {
         if (xaDataSourceClass == null)
            throw new JBossResourceException("No XADataSourceClass supplied!");
         try
         {
            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(xaDataSourceClass);
            xads = (XADataSource) clazz.newInstance();
            Class[] NOCLASSES = new Class[] {};
            for (Iterator i = xaProps.keySet().iterator(); i.hasNext();)
            {
               String name = (String) i.next();
               String value = xaProps.getProperty(name);
               //This is a bad solution.  On the other hand the only known example
               // of a setter with no getter is for Oracle with password.
               //Anyway, each xadatasource implementation should get its
               //own subclass of this that explicitly sets the
               //properties individually.
               Class type = null;
               try
               {
                  Method getter = clazz.getMethod("get" + name, NOCLASSES);
                  type = getter.getReturnType();
               }
               catch (NoSuchMethodException e)
               {
                  type = String.class;
               }

               Method setter = clazz.getMethod("set" + name, new Class[] { type });
               PropertyEditor editor = PropertyEditorManager.findEditor(type);
               if (editor == null)
                  throw new JBossResourceException("No property editor found for type: " + type);
               editor.setAsText(value);
               setter.invoke(xads, new Object[] { editor.getValue() });
            }
         }
         catch (ClassNotFoundException cnfe)
         {
            throw new JBossResourceException("Class not found for XADataSource " + xaDataSourceClass, cnfe);
         }
         catch (InstantiationException ie)
         {
            throw new JBossResourceException("Could not create an XADataSource: ", ie);
         }
         catch (IllegalAccessException iae)
         {
            throw new JBossResourceException("Could not set a property: ", iae);
         }
         catch (IllegalArgumentException iae)
         {
            throw new JBossResourceException("Could not set a property: ", iae);
         }
         catch (InvocationTargetException ite)
         {
            throw new JBossResourceException("Could not invoke setter on XADataSource: ", ite);
         }
         catch (NoSuchMethodException nsme)
         {
            throw new JBossResourceException("Could not find accessor on XADataSource: ", nsme);
         }
      }
      return xads;
   }

   protected Properties getXaProps()
   {
      return xaProps;
   }
}
