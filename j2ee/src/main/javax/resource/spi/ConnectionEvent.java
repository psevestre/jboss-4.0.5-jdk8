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
package javax.resource.spi;

import java.io.Serializable;
import java.io.ObjectStreamField;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.EventObject;

import org.jboss.util.id.SerialVersion;

/**
 * The ConnectionEvent class provides information about the source of a
 * Connection related event. A ConnectionEvent contains:
 * <ul>
 * 
 * <li>Type of connection event.</li>
 * <li>Managed connection instance that generated the event.</li>
 * <li>Connection handle associated with the managed connection.</li>
 * <li>Optionally an exception indicating an error.</li>
 * </ul>
 * <p>
 * This class is used for the following types of notifications:
 * <ul>
 * 
 * <li>Connection closed</li>
 * <li>Local transaction started</li>
 * <li>Local transaction commited</li>
 * <li>Local transaction rolled back</li>
 * <li>Connection error occurred</li>
 * </ul>
 * @version $Revision: 57196 $
 */
public class ConnectionEvent extends EventObject
{
   /** @since 4.0.2 */
   static final long serialVersionUID;

   // Constants -----------------------------------------------------
   /**
    * These field names match the j2ee 1.4.1 ri version names
    */
   private static final ObjectStreamField[] serialPersistentFields;
   private static final int ID_IDX = 0;
   private static final int EXCEPTION_IDX = 1;
   private static final int CONN_HANDLE_IDX = 2;

   static
   {
      if (SerialVersion.version == SerialVersion.LEGACY)
      {
         serialVersionUID = 2776168349823367611L;
         serialPersistentFields = new ObjectStreamField[] {
         /** @serialField id int */
         new ObjectStreamField("id", int.class),
         /** @serialField e Exception */
         new ObjectStreamField("e", Exception.class),
         /** @serialField connectionHandle Object */
         new ObjectStreamField("connectionHandle", Object.class)
         };
      }
      else
      {
         // j2ee 1.4.1 RI values
         serialVersionUID = 5611772461379563249L;
         serialPersistentFields = new ObjectStreamField[] {
            /** @serialField id int */
            new ObjectStreamField("id", int.class),
            /** @serialField exception Exception */
            new ObjectStreamField("exception", Exception.class),
            /** @serialField connectionHandle Object */
            new ObjectStreamField("connectionHandle", Object.class)
         };         
      }
   }

   /**
	 * Connection has been closed
	 */
   public static final int CONNECTION_CLOSED = 1;

   /**
	 * Local transaction has been started
	 */
   public static final int LOCAL_TRANSACTION_STARTED = 2;

   /**
	 * Local transaction has been committed
	 */
   public static final int LOCAL_TRANSACTION_COMMITTED = 3;

   /**
	 * Local transaction has been rolled back
	 */
   public static final int LOCAL_TRANSACTION_ROLLEDBACK = 4;

   /**
	 * Connection error has occurred
	 */
   public static final int CONNECTION_ERROR_OCCURRED = 5;

   /** Type of event */
   protected int id;

   /** The exception */
   private Exception e = null;
   /** The connectionHandle */
   private Object connectionHandle = null;

   /**
    * Create a new ConnectionEvent
    *
    * @param source the source of the event
    * @param eid the event id
    */
   public ConnectionEvent(ManagedConnection source, int eid)
   {
      super(source);
      id = eid;
   }

   /**
    * Create a new ConnectionEvent
    *
    * @param source the source of the event
    * @param eid the event id
    * @param exception the exception associated with the event
    */
   public ConnectionEvent(ManagedConnection source, int eid, Exception exception)
   {
      super(source);
      id = eid;
      e = exception;
   }

   /**
	 * Get the event type
    * 
    * @return the event id
	 */
   public int getId()
   {
      return id;
   }

   /**
	 * Get the exception
    * 
    * @return the exception
	 */
   public Exception getException()
   {
      return e;
   }

   /**
	 * Set the ConnectionHandle
    * 
    * @param connectionHandle the connection handle
	 */
   public void setConnectionHandle(Object connectionHandle)
   {
      this.connectionHandle = connectionHandle;
   }

   /**
	 * Get the ConnectionHandle
    * 
    * @return the connection handle
	 */
   public Object getConnectionHandle()
   {
      return connectionHandle;
   }

   // Private -------------------------------------------------------
   private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException
   {
      ObjectInputStream.GetField fields = ois.readFields();
      String name = serialPersistentFields[ID_IDX].getName();
      this.id = fields.get(name, CONNECTION_ERROR_OCCURRED);
      name = serialPersistentFields[EXCEPTION_IDX].getName();
      this.e = (Exception) fields.get(name, null);
      name = serialPersistentFields[CONN_HANDLE_IDX].getName();
      this.connectionHandle = fields.get(name, null);
   }

   private void writeObject(ObjectOutputStream oos)
      throws IOException
   {
      // Write j2ee 1.4.1 RI field names
      ObjectOutputStream.PutField fields =  oos.putFields();
      String name = serialPersistentFields[ID_IDX].getName();
      fields.put(name, id);
      name = serialPersistentFields[EXCEPTION_IDX].getName();
      fields.put(name, e);
      name = serialPersistentFields[CONN_HANDLE_IDX].getName();
      fields.put(name, connectionHandle);
      oos.writeFields();
   }

}