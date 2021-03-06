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
package org.jboss.mq;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;
import javax.jms.Session;

import org.jboss.util.Primitives;
import org.jboss.util.Strings;

/**
 * This class implements javax.jms.Message
 * 
 * @author Norbert Lataille (Norbert.Lataille@m4x.org)
 * @author Hiram Chirino (Cojonudo14@hotmail.com)
 * @author David Maplesden (David.Maplesden@orion.co.nz)
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 57198 $
 */
public class SpyMessage implements Serializable, Message, Comparable, Cloneable, Externalizable
{
   // Constants -----------------------------------------------------

   /** The serialVersionUID */
   private final static long serialVersionUID = 467206190892964404L;

   /**
    * Standard property for delivery count
    */
   public static final String PROPERTY_DELIVERY_COUNT = "JMSXDeliveryCount";

   /**
	 * JBoss-vendor specific property for scheduling a JMS message. In
	 * milliseconds since January 1, 1970.
	 */
   public static final String PROPERTY_SCHEDULED_DELIVERY = "JMS_JBOSS_SCHEDULED_DELIVERY";

   /**
	 * JBoss-vendor specific property specifying redelivery delay of a message.
	 * The message will be rescheduled for delivery from the time at which it
	 * was unacknowledged, plus the given period.
	 */
   public static final String PROPERTY_REDELIVERY_DELAY = "JMS_JBOSS_REDELIVERY_DELAY";

   /**
	 * JBoss-vendor specific property for getting the count of redelivery
	 * attempts of a message.
	 */
   public static final String PROPERTY_REDELIVERY_COUNT = "JMS_JBOSS_REDELIVERY_COUNT";

   /**
	 * JBoss-vendor specific property specifying the limit of redelivery
	 * attempts of a message. The message will be redelivered a given number of
	 * times. If not set, the container default is used.
	 */
   public static final String PROPERTY_REDELIVERY_LIMIT = "JMS_JBOSS_REDELIVERY_LIMIT";

   /** 
   * JBoss-vendor property name holding original destination.
   */
   public static final String PROPERTY_ORIG_DESTINATION = "JBOSS_ORIG_DESTINATION";

   /** 
   * JBoss-vendor property name holding original expiration value.
   */
   public static final String PROPERTY_ORIG_EXPIRATION = "JBOSS_ORIG_EXPIRATION";

   /** 
   * JBoss-vendor property name holding original message ID value.
   */
   public static final String PROPERTY_ORIG_MESSAGEID = "JBOSS_ORIG_MESSAGEID";

   /** An object message */
   protected static final byte OBJECT_MESS = 1;
   /** An bytes message */
   protected static final byte BYTES_MESS = 2;
   /** A map message */
   protected static final byte MAP_MESS = 3;
   /** A text message */
   protected static final byte TEXT_MESS = 4;
   /** A stream message */
   protected static final byte STREAM_MESS = 5;
   /** An encapsulated message */
   protected static final byte ENCAP_MESS = 6;
   /** A plain message */
   protected static final byte SPY_MESS = 7;

   /** A byte property */
   protected static final int BYTE = 0;
   /** A short property */
   protected static final int SHORT = 1;
   /** An integer property */
   protected static final int INT = 2;
   /** A long property */
   protected static final int LONG = 3;
   /** A float property */
   protected static final int FLOAT = 4;
   /** A double property */
   protected static final int DOUBLE = 5;
   /** A boolean property */
   protected static final int BOOLEAN = 6;
   /** A string property */
   protected static final int STRING = 7;
   /** An object property */
   protected static final int OBJECT = 8;
   /** A null property */
   protected static final int NULL = 9;

   /** Reserved identifiers */
   private static final HashSet reservedIdentifiers = new HashSet();

   // Attributes ----------------------------------------------------

   /** The message header */
   public Header header = new Header();

   /** The acknowledgement request for this message */
   public transient AcknowledgementRequest ack;

   /** The session for this message */
   public transient SpySession session;

   // Static --------------------------------------------------------

   static
   {
      reservedIdentifiers.add("NULL");
      reservedIdentifiers.add("TRUE");
      reservedIdentifiers.add("FALSE");
      reservedIdentifiers.add("NOT");
      reservedIdentifiers.add("AND");
      reservedIdentifiers.add("OR");
      reservedIdentifiers.add("BETWEEN");
      reservedIdentifiers.add("LIKE");
      reservedIdentifiers.add("IN");
      reservedIdentifiers.add("IS");
      reservedIdentifiers.add("ESCAPE");
   }

   /**
	 * Write a message
	 * 
	 * @param message the message
	 * @param out the output
	 * @throws IOException for any error
	 */
   public static void writeMessage(SpyMessage message, ObjectOutput out) throws IOException
   {
      if (message instanceof SpyEncapsulatedMessage)
         out.writeByte(ENCAP_MESS);
      else if (message instanceof SpyObjectMessage)
         out.writeByte(OBJECT_MESS);
      else if (message instanceof SpyBytesMessage)
         out.writeByte(BYTES_MESS);
      else if (message instanceof SpyMapMessage)
         out.writeByte(MAP_MESS);
      else if (message instanceof SpyTextMessage)
         out.writeByte(TEXT_MESS);
      else if (message instanceof SpyStreamMessage)
         out.writeByte(STREAM_MESS);
      else
         out.writeByte(SPY_MESS);
      message.writeExternal(out);
   }

   /**
	 * Read a message
	 * 
	 * @param in the input
	 * @return the message
	 * @throws IOException for any error
	 */
   public static SpyMessage readMessage(ObjectInput in) throws IOException
   {
      SpyMessage message = null;
      byte type = in.readByte();
      switch (type)
      {
         case OBJECT_MESS :
            message = MessagePool.getObjectMessage();
            break;
         case BYTES_MESS :
            message = MessagePool.getBytesMessage();
            break;
         case MAP_MESS :
            message = MessagePool.getMapMessage();
            break;
         case STREAM_MESS :
            message = MessagePool.getStreamMessage();
            break;
         case TEXT_MESS :
            message = MessagePool.getTextMessage();
            break;
         case ENCAP_MESS :
            message = MessagePool.getEncapsulatedMessage();
            break;
         default :
            message = MessagePool.getMessage();
      }
      try
      {
         message.readExternal(in);
      }
      catch (ClassNotFoundException cnf)
      {
         throw new IOException("Class not found when reading in spy message.");
      }
      return message;
   }

   // Constructors --------------------------------------------------

   // Public --------------------------------------------------------

   /**
    * Clear the message body
    * 
    * @throws JMSException for any error
    */
   public void clearBody() throws JMSException
   {
      //Inherited classes clear their content here
      header.msgReadOnly = false;
   }

   /**
    * Acknowledge a message
    * 
    * @throws JMSException for any error
    */
   public void acknowledge() throws JMSException
   {
      if (session == null)
         throw new JMSException("This message was not recieved from the provider");

      if (session.acknowledgeMode == Session.CLIENT_ACKNOWLEDGE)
         doAcknowledge();
   }

   /**
    * Set the message to read only
    */
   public void setReadOnlyMode()
   {
      header.jmsPropertiesReadWrite = false;
      header.msgReadOnly = true;
   }

   /**
    * Clone the message
    *
    * @return the cloned message
    * @throws JMSException for any error
    */
   public SpyMessage myClone() throws JMSException
   {
      SpyMessage result = MessagePool.getMessage();
      result.copyProps(this);
      return result;
   }

   /**
    * Copy the properties
    *
    * @param original the message with original properties
    * @throws JMSException for any error
    */
   public void copyProps(SpyMessage original) throws JMSException
   {
      try
      {
         this.setJMSCorrelationID(original.getJMSCorrelationID());
      }
      catch (JMSException e)
      {
         //must be as bytes
         this.setJMSCorrelationIDAsBytes(original.getJMSCorrelationIDAsBytes());
      }
      this.setJMSDeliveryMode(original.getJMSDeliveryMode());
      this.setJMSDestination(original.getJMSDestination());
      this.setJMSExpiration(original.getJMSExpiration());
      this.setJMSMessageID(original.getJMSMessageID());
      this.setJMSPriority(original.getJMSPriority());
      this.setJMSRedelivered(original.getJMSRedelivered());
      this.setJMSReplyTo(original.getJMSReplyTo());
      this.setJMSTimestamp(original.getJMSTimestamp());
      this.setJMSType(original.getJMSType());
      this.header.jmsProperties.putAll(original.header.jmsProperties);

      //Spy Message special header.jmsPropertiess
      this.header.jmsPropertiesReadWrite = original.header.jmsPropertiesReadWrite;
      this.header.msgReadOnly = original.header.msgReadOnly;
      this.header.producerClientId = original.header.producerClientId;
      if (original.header.durableSubscriberID != null)
         this.header.durableSubscriberID = new DurableSubscriptionID(original.header.durableSubscriberID.clientID,
               original.header.durableSubscriberID.subscriptionName, original.header.durableSubscriberID.selector);
   }

   /**
	 * Test whether a message has expired
	 * 
	 * @return true when expired false otherwise
	 */
   public boolean isOutdated()
   {
      if (header.jmsExpiration == 0)
         return false;
      long ts = System.currentTimeMillis();
      return header.jmsExpiration < ts;
   }

   /**
    * Actually acknowledge a message
    *
    * @throws JMSException for any error
    */
   public void doAcknowledge() throws JMSException
   {
      session.doAcknowledge(this, getAcknowledgementRequest(true));
   }

   /**
    * Create an acknowledgement request for the message
    */
   public void createAcknowledgementRequest(int subscriptionId)
   {
      ack = new AcknowledgementRequest();
      ack.destination = header.jmsDestination;
      ack.messageID = header.jmsMessageID;
      ack.subscriberId = subscriptionId;
   }

   /**
    * Get an acknowledgement request for the message
    * 
    * @param isAck true for an ack, false for a nack
    * @throws JMSException for any error
    */
   public AcknowledgementRequest getAcknowledgementRequest(boolean isAck) throws JMSException
   {
      //don't know if we have to copy but to be on safe side...
      AcknowledgementRequest item = new AcknowledgementRequest(isAck);
      item.destination = ack.destination;
      item.messageID = ack.messageID;
      item.subscriberId = ack.subscriberId;
      return item;
   }

   // Comparable implementation -------------------------------------

   public int compareTo(Object o)
   {
      SpyMessage sm = (SpyMessage) o;

      if (header.jmsPriority > sm.header.jmsPriority)
      {
         return -1;
      }
      if (header.jmsPriority < sm.header.jmsPriority)
      {
         return 1;
      }
      return (int) (header.messageId - sm.header.messageId);
   }

   // Message implementation ----------------------------------------

   public String getJMSMessageID()
   {
      return header.jmsMessageID;
   }

   public void setJMSMessageID(String id) throws JMSException
   {
      header.jmsMessageID = id;
   }

   public long getJMSTimestamp()
   {
      return header.jmsTimeStamp;
   }

   public void setJMSTimestamp(long timestamp) throws JMSException
   {
      header.jmsTimeStamp = timestamp;
   }

   public byte[] getJMSCorrelationIDAsBytes() throws JMSException
   {
      if (header.jmsCorrelationID)
         throw new JMSException("JMSCorrelationID is a string");
      return header.jmsCorrelationIDbyte;
   }

   public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException
   {
      header.jmsCorrelationID = false;
      header.jmsCorrelationIDbyte = (byte[]) correlationID.clone();
      header.jmsCorrelationIDString = null;
   }

   public void setJMSCorrelationID(String correlationID) throws JMSException
   {
      header.jmsCorrelationID = true;
      header.jmsCorrelationIDString = correlationID;
      header.jmsCorrelationIDbyte = null;
   }

   public String getJMSCorrelationID() throws JMSException
   {
      if (!header.jmsCorrelationID)
         throw new JMSException("JMSCorrelationID is an array");
      return header.jmsCorrelationIDString;
   }

   public Destination getJMSReplyTo()
   {
      return header.jmsReplyTo;
   }

   public void setJMSReplyTo(Destination replyTo) throws JMSException
   {
      header.jmsReplyTo = replyTo;
   }

   public Destination getJMSDestination()
   {
      return header.jmsDestination;
   }

   public void setJMSDestination(Destination destination) throws JMSException
   {
      header.jmsDestination = destination;
   }

   public int getJMSDeliveryMode()
   {
      return header.jmsDeliveryMode;
   }

   public void setJMSDeliveryMode(int deliveryMode) throws JMSException
   {
      header.jmsDeliveryMode = deliveryMode;
   }

   public boolean getJMSRedelivered()
   {
      return header.jmsRedelivered;
   }

   public void setJMSRedelivered(boolean redelivered) throws JMSException
   {
      header.jmsRedelivered = redelivered;
   }

   public String getJMSType()
   {
      return header.jmsType;
   }

   public void setJMSType(String type) throws JMSException
   {
      header.jmsType = type;
   }

   public long getJMSExpiration()
   {
      return header.jmsExpiration;
   }

   public void setJMSExpiration(long expiration) throws JMSException
   {
      header.jmsExpiration = expiration;
   }

   public int getJMSPriority()
   {
      return header.jmsPriority;
   }

   public void setJMSPriority(int priority) throws JMSException
   {
      if (priority < 0 || priority > 10)
         throw new JMSException("Unsupported priority '" + priority + "': priority must be from 0-10");
      header.jmsPriority = priority;
   }

   public void clearProperties() throws JMSException
   {
      header.jmsProperties.clear();
      header.jmsPropertiesReadWrite = true;
   }

   public boolean propertyExists(String name) throws JMSException
   {
      return header.jmsProperties.containsKey(name);
   }

   public boolean getBooleanProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         return Boolean.valueOf(null).booleanValue();

      if (value instanceof Boolean)
         return ((Boolean) value).booleanValue();
      else if (value instanceof String)
         return Boolean.valueOf((String) value).booleanValue();
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public byte getByteProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         throw new NumberFormatException("Message property '" + name + "' not set.");

      if (value instanceof Byte)
         return ((Byte) value).byteValue();
      else if (value instanceof String)
         return Byte.parseByte((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public short getShortProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         throw new NumberFormatException("Message property '" + name + "' not set.");

      if (value instanceof Byte)
         return ((Byte) value).shortValue();
      else if (value instanceof Short)
         return ((Short) value).shortValue();
      else if (value instanceof String)
         return Short.parseShort((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public int getIntProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         throw new NumberFormatException("Message property '" + name + "' not set.");

      if (value instanceof Byte)
         return ((Byte) value).intValue();
      else if (value instanceof Short)
         return ((Short) value).intValue();
      else if (value instanceof Integer)
         return ((Integer) value).intValue();
      else if (value instanceof String)
         return Integer.parseInt((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public long getLongProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         throw new NumberFormatException("Message property '" + name + "' not set.");

      if (value instanceof Byte)
         return ((Byte) value).longValue();
      else if (value instanceof Short)
         return ((Short) value).longValue();
      else if (value instanceof Integer)
         return ((Integer) value).longValue();
      else if (value instanceof Long)
         return ((Long) value).longValue();
      else if (value instanceof String)
         return Long.parseLong((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public float getFloatProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         return Float.valueOf(null).floatValue();

      if (value instanceof Float)
         return ((Float) value).floatValue();
      else if (value instanceof String)
         return Float.parseFloat((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public double getDoubleProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         return Double.valueOf(null).doubleValue();

      if (value instanceof Float)
         return ((Float) value).doubleValue();
      else if (value instanceof Double)
         return ((Double) value).doubleValue();
      else if (value instanceof String)
         return Double.parseDouble((String) value);
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public String getStringProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      if (value == null)
         return null;

      if (value instanceof Boolean)
         return ((Boolean) value).toString();
      else if (value instanceof Byte)
         return ((Byte) value).toString();
      else if (value instanceof Short)
         return ((Short) value).toString();
      else if (value instanceof Integer)
         return ((Integer) value).toString();
      else if (value instanceof Long)
         return ((Long) value).toString();
      else if (value instanceof Float)
         return ((Float) value).toString();
      else if (value instanceof Double)
         return ((Double) value).toString();
      else if (value instanceof String)
         return (String) value;
      else
         throw new MessageFormatException("Invalid conversion");
   }

   public Object getObjectProperty(String name) throws JMSException
   {
      Object value = header.jmsProperties.get(name);
      return value;
   }

   public Enumeration getPropertyNames() throws JMSException
   {
      Enumeration names = Collections.enumeration(header.jmsProperties.keySet());
      return names;
   }

   public void setBooleanProperty(String name, boolean value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Boolean b = Primitives.valueOf(value);
      checkProperty(name, b);
      header.jmsProperties.put(name, b);
   }

   public void setByteProperty(String name, byte value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Byte b = new Byte(value);
      checkProperty(name, b);
      header.jmsProperties.put(name, b);
   }

   public void setShortProperty(String name, short value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Short s = new Short(value);
      checkProperty(name, s);
      header.jmsProperties.put(name, s);
   }

   public void setIntProperty(String name, int value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Integer i = new Integer(value);
      checkProperty(name, i);
      header.jmsProperties.put(name, i);
   }

   public void setLongProperty(String name, long value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Long l = new Long(value);
      checkProperty(name, l);
      header.jmsProperties.put(name, l);
   }

   public void setFloatProperty(String name, float value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Float f = new Float(value);
      checkProperty(name, f);
      header.jmsProperties.put(name, f);
   }

   public void setDoubleProperty(String name, double value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      Double d = new Double(value);
      checkProperty(name, d);
      header.jmsProperties.put(name, d);
   }

   public void setStringProperty(String name, String value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      checkProperty(name, value);
      header.jmsProperties.put(name, value);
   }

   public void setObjectProperty(String name, Object value) throws JMSException
   {
      if (!header.jmsPropertiesReadWrite)
         throw new MessageNotWriteableException("Properties are read-only");
      checkProperty(name, value);
      if (value instanceof Boolean)
         header.jmsProperties.put(name, value);
      else if (value instanceof Byte)
         header.jmsProperties.put(name, value);
      else if (value instanceof Short)
         header.jmsProperties.put(name, value);
      else if (value instanceof Integer)
         header.jmsProperties.put(name, value);
      else if (value instanceof Long)
         header.jmsProperties.put(name, value);
      else if (value instanceof Float)
         header.jmsProperties.put(name, value);
      else if (value instanceof Double)
         header.jmsProperties.put(name, value);
      else if (value instanceof String)
         header.jmsProperties.put(name, value);
      else if (value == null)
         header.jmsProperties.put(name, null);
      else
         throw new MessageFormatException("Invalid object type");
   }

   // Externalizable implementation ---------------------------------

   public void writeExternal(ObjectOutput out) throws IOException
   {
      SpyDestination.writeDest(out, header.jmsDestination);
      out.writeInt(header.jmsDeliveryMode);
      out.writeLong(header.jmsExpiration);
      out.writeInt(header.jmsPriority);
      writeString(out, header.jmsMessageID);
      out.writeLong(header.jmsTimeStamp);
      out.writeBoolean(header.jmsCorrelationID);
      writeString(out, header.jmsCorrelationIDString);
      if (header.jmsCorrelationIDbyte == null)
         out.writeInt(-1);
      else
      {
         out.writeInt(header.jmsCorrelationIDbyte.length);
         out.write(header.jmsCorrelationIDbyte);
      }
      SpyDestination.writeDest(out, header.jmsReplyTo);
      writeString(out, header.jmsType);
      out.writeBoolean(header.jmsRedelivered);
      out.writeBoolean(header.jmsPropertiesReadWrite);
      out.writeBoolean(header.msgReadOnly);
      writeString(out, header.producerClientId);
      //write out header.jmsPropertiess
      java.util.Set entrySet = header.jmsProperties.entrySet();
      out.writeInt(entrySet.size());
      for (java.util.Iterator it = entrySet.iterator(); it.hasNext(); )
      {
         Map.Entry me = (Map.Entry)it.next();
         out.writeUTF((String)me.getKey());
         Object value = me.getValue();
         if (value == null)
         {
            out.writeByte(OBJECT);
            out.writeObject(value);
         }
         else if (value instanceof String)
         {
            out.writeByte(STRING);
            out.writeUTF((String) value);
         }
         else if (value instanceof Integer)
         {
            out.writeByte(INT);
            out.writeInt(((Integer) value).intValue());
         }
         else if (value instanceof Boolean)
         {
            out.writeByte(BOOLEAN);
            out.writeBoolean(((Boolean) value).booleanValue());
         }
         else if (value instanceof Byte)
         {
            out.writeByte(BYTE);
            out.writeByte(((Byte) value).byteValue());
         }
         else if (value instanceof Short)
         {
            out.writeByte(SHORT);
            out.writeShort(((Short) value).shortValue());
         }
         else if (value instanceof Long)
         {
            out.writeByte(LONG);
            out.writeLong(((Long) value).longValue());
         }
         else if (value instanceof Float)
         {
            out.writeByte(FLOAT);
            out.writeFloat(((Float) value).floatValue());
         }
         else if (value instanceof Double)
         {
            out.writeByte(DOUBLE);
            out.writeDouble(((Double) value).doubleValue());
         }
         else
         {
            out.writeByte(OBJECT);
            out.writeObject(value);
         }
      }
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      header.jmsDestination = SpyDestination.readDest(in);
      header.jmsDeliveryMode = in.readInt();
      header.jmsExpiration = in.readLong();
      header.jmsPriority = in.readInt();
      header.jmsMessageID = readString(in);
      header.jmsTimeStamp = in.readLong();
      header.jmsCorrelationID = in.readBoolean();
      header.jmsCorrelationIDString = readString(in);
      int length = in.readInt();
      if (length < 0)
         header.jmsCorrelationIDbyte = null;
      else
      {
         header.jmsCorrelationIDbyte = new byte[length];
         in.readFully(header.jmsCorrelationIDbyte);
      }
      header.jmsReplyTo = SpyDestination.readDest(in);
      header.jmsType = readString(in);
      header.jmsRedelivered = in.readBoolean();
      header.jmsPropertiesReadWrite = in.readBoolean();
      header.msgReadOnly = in.readBoolean();
      header.producerClientId = readString(in);
      //read in header.jmsPropertiess
      header.jmsProperties = new HashMap();
      int size = in.readInt();
      for (int i = 0; i < size; i++)
      {
         String key = in.readUTF();
         byte type = in.readByte();
         Object value = null;
         switch (type)
         {
            case BYTE :
               value = new Byte(in.readByte());
               break;
            case SHORT :
               value = new Short(in.readShort());
               break;
            case INT :
               value = new Integer(in.readInt());
               break;
            case LONG :
               value = new Long(in.readLong());
               break;
            case FLOAT :
               value = new Float(in.readFloat());
               break;
            case DOUBLE :
               value = new Double(in.readDouble());
               break;
            case BOOLEAN :
               value = Primitives.valueOf(in.readBoolean());
               break;
            case STRING :
               value = in.readUTF();
               break;
            default :
               value = in.readObject();
         }
         header.jmsProperties.put(key, value);
      }
   }

   // Object overrides ----------------------------------------------

   public String toString()
   {
      return getClass().getName() + " {\n" + header + "\n" + "}";
   }

   // Package protected ---------------------------------------------

   /**
	 * Check a property is valid
	 * 
	 * @param name the name
	 * @param value the value
	 * @throws JMSException for any error
	 */
   void checkProperty(String name, Object value) throws JMSException
   {
      if (name == null)
         throw new IllegalArgumentException("The name of a property must not be null.");

      if (name.equals(""))
         throw new IllegalArgumentException("The name of a property must not be an empty String.");

      if (reservedIdentifiers.contains(name))
         throw new IllegalArgumentException("The property name '" + name + "' is reserved due to selector syntax.");

      if (name.regionMatches(false, 0, "JMS_", 0, 4))
      {
         if (name.equals(PROPERTY_SCHEDULED_DELIVERY))
         {
            if (!(value instanceof Long))
               throw new JMSException(name + " must be Long: " + value);
         }
         else if (name.equals(PROPERTY_REDELIVERY_DELAY))
         {
            if (!(value instanceof Number))
               throw new JMSException(name + " must be Number: " + value);
         }
         else if (name.equals(PROPERTY_REDELIVERY_COUNT))
         {
            if (!(value instanceof Number))
               throw new JMSException(name + " must be Number: " + value);
         }
         else if (name.equals(PROPERTY_REDELIVERY_LIMIT))
         {
            if (!(value instanceof Number))
               throw new JMSException(name + " must be Number: " + value);
         }
         else if (name.equals(PROPERTY_ORIG_EXPIRATION))
         {
            if (!(value instanceof Long))
               throw new JMSException(name + " must be Long: " + value);
         }
         else if (name.equals(PROPERTY_ORIG_DESTINATION))
         {
            // no validation
         }
         else if (name.equals(PROPERTY_ORIG_MESSAGEID))
         {
            // no validation
         }
         else
         {
            throw new JMSException("Illegal property name: " + name);
         }
      }

      if (name.regionMatches(false, 0, "JMSX", 0, 4))
      {
         if (name.equals("JMSXGroupID"))
            return;
         if (name.equals("JMSXGroupSeq"))
            return;
         throw new JMSException("Illegal property name: " + name);
      }

      if (Strings.isValidJavaIdentifier(name) == false)
         throw new IllegalArgumentException("The property name '" + name + "' is not a valid java identifier.");

   }

   /**
	 * Clear a message
	 * 
	 * @throws JMSException for any error
	 */
   void clearMessage() throws JMSException
   {
      clearBody();
      this.ack = null;
      this.session = null;
      //Set by send() method
      this.header.jmsDestination = null;
      this.header.jmsDeliveryMode = -1;
      this.header.jmsExpiration = 0;
      this.header.jmsPriority = -1;
      this.header.jmsMessageID = null;
      this.header.jmsTimeStamp = 0;
      //Set by the client
      this.header.jmsCorrelationID = true;
      this.header.jmsCorrelationIDString = null;
      this.header.jmsCorrelationIDbyte = null;
      this.header.jmsReplyTo = null;
      this.header.jmsType = null;
      //Set by the provider
      this.header.jmsRedelivered = false;
      //Properties
      this.header.jmsProperties.clear();
      this.header.jmsPropertiesReadWrite = true;
      //Message body
      this.header.msgReadOnly = false;
      //For noLocal to be able to tell if this was a locally produced message
      this.header.producerClientId = null;
      //For durable subscriptions
      this.header.durableSubscriberID = null;
      //For ordering in the JMSServerQueue (set on the server side)
      this.header.messageId = 0;
   }

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   /**
	 * Write a string
	 * 
	 * @param out the output
	 * @param s the string
	 * @throws IOException for any error
	 */
   private static void writeString(ObjectOutput out, String s) throws IOException
   {
      if (s == null)
         out.writeByte(NULL);
      else
      {
         out.writeByte(STRING);
         out.writeUTF(s);
      }
   }

   /**
	 * Read a string
	 * 
	 * @param in the input
	 * @return the string
	 * @throws IOException for any error
	 */
   private static String readString(ObjectInput in) throws IOException
   {
      byte b = in.readByte();
      if (b == NULL)
         return null;
      else
         return in.readUTF();
   }

   // Inner classes -------------------------------------------------

   /**
	 * The message headers
	 */
   public static class Header
   {
      /** The destination */
      public Destination jmsDestination = null;
      /** The delivery mode */
      public int jmsDeliveryMode = -1;
      /** The expiration time */
      public long jmsExpiration = 0;
      /** The message priority */
      public int jmsPriority = -1;
      /** The message id */
      public String jmsMessageID = null;
      /** The send timestamp */
      public long jmsTimeStamp = 0;
      /** Whether the correlation is a string */
      public boolean jmsCorrelationID = true;
      /** The correlation string */
      public String jmsCorrelationIDString = null;
      /** The correlation in bytes */
      public byte[] jmsCorrelationIDbyte = null;
      /** The reply to destination */
      public Destination jmsReplyTo = null;
      /** The message type */
      public String jmsType = null;
      /** Set by the provider */
      public boolean jmsRedelivered = false;
      /** Properties */
      public HashMap jmsProperties = new HashMap();
      /** Whether the properties are writable */
      public boolean jmsPropertiesReadWrite = true;
      /** Message body */
      public boolean msgReadOnly = false;
      /** For noLocal to be able to tell if this was a locally produced message */
      public String producerClientId;
      /** For durable subscriptions */
      public DurableSubscriptionID durableSubscriberID = null;
      /** For ordering in the JMSServerQueue (set on the server side) */
      public transient long messageId;

      public String toString()
      {
         StringBuffer buffer = new StringBuffer(100);
         buffer.append("Header { \n");
         buffer.append("   jmsDestination  : ").append(jmsDestination).append('\n');
         buffer.append("   jmsDeliveryMode : ").append(jmsDeliveryMode).append('\n');
         buffer.append("   jmsExpiration   : ").append(jmsExpiration).append('\n');
         buffer.append("   jmsPriority     : ").append(jmsPriority).append('\n');
         buffer.append("   jmsMessageID    : ").append(jmsMessageID).append('\n');
         buffer.append("   jmsTimeStamp    : ").append(jmsTimeStamp).append('\n');
         buffer.append("   jmsCorrelationID: ").append(jmsCorrelationIDString).append('\n');
         buffer.append("   jmsReplyTo      : ").append(jmsReplyTo).append('\n');
         buffer.append("   jmsType         : ").append(jmsType).append('\n');
         buffer.append("   jmsRedelivered  : ").append(jmsRedelivered).append('\n');
         buffer.append("   jmsProperties   : ").append(jmsProperties).append('\n');
         buffer.append("   jmsPropReadWrite: ").append(jmsPropertiesReadWrite).append('\n');
         buffer.append("   msgReadOnly     : ").append(msgReadOnly).append('\n');
         buffer.append("   producerClientId: ").append(producerClientId).append('\n');
         buffer.append('}');
         return buffer.toString();
      }
   }
}
