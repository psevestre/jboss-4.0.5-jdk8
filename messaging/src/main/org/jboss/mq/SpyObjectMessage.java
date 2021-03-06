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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.MessageFormatException;
import javax.jms.MessageNotWriteableException;
import javax.jms.ObjectMessage;

import org.jboss.util.Classes;

/**
 * This class implements javax.jms.ObjectMessage
 *
 * @author Norbert Lataille (Norbert.Lataille@m4x.org)
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 57198 $
 */
public class SpyObjectMessage extends SpyMessage implements ObjectMessage, Externalizable
{
   // Constants -----------------------------------------------------

   /** The serialVersionUID */
   private final static long serialVersionUID = 8809953915407712952L;
   
   // Attributes ----------------------------------------------------

   /** Is it a byte array */
   boolean isByteArray = false;
   /** The bytes */
   byte[] objectBytes = null;
   
   // Static --------------------------------------------------------
   
   // Constructors --------------------------------------------------
   
   // Public --------------------------------------------------------
   
   // ObjectMessage implementation ----------------------------------

   public void setObject(Serializable object) throws JMSException
   {
      if (header.msgReadOnly)
        {
         throw new MessageNotWriteableException("setObject");
      }
      if (object == null)
      {
         objectBytes = null;
         return;
      }
      try
      {
         if (object instanceof byte[])
           {
            //cheat for byte arrays
            isByteArray = true;
            objectBytes = new byte[((byte[]) object).length];
            System.arraycopy(object, 0, objectBytes, 0, objectBytes.length);
         }
         else
           {
            isByteArray = false;
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteArray);
            objectOut.writeObject(object);
            objectBytes = byteArray.toByteArray();
            objectOut.close();
         }
      }
      catch (IOException e)
      {
         MessageFormatException mfe = new MessageFormatException("Object cannot be serialized: " + e.getMessage());
         mfe.setLinkedException(e);
         throw mfe;
      }
   }

   public Serializable getObject() throws JMSException
   {

      Serializable retVal = null;
      try
      {
         if (null != objectBytes)
         {
            if (isByteArray)
            {
               retVal = new byte[objectBytes.length];
               System.arraycopy(objectBytes, 0, retVal, 0, objectBytes.length);
            }
            else
            {

               /**
                * Default implementation ObjectInputStream does not work well
                * when running an a micro kernal style app-server like JBoss.
                * We need to look for the Class in the context class loader and
                * not in the System classloader.
                * 
                * Would this be done better by using a MarshaedObject??
                */
               class ObjectInputStreamExt extends ObjectInputStream
               {
                  ObjectInputStreamExt(InputStream is) throws IOException
                  {
                     super(is);
                  }

                  protected Class resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException
                  {
                     return Classes.loadClass(v.getName());
                  }
               }
               ObjectInputStream input = new ObjectInputStreamExt(new ByteArrayInputStream(objectBytes));
               retVal = (Serializable) input.readObject();
               input.close();
            }
         }
      }
      catch (ClassNotFoundException e)
      {
         throw new MessageFormatException("ClassNotFoundException: " + e.getMessage());
      }
      catch (IOException e)
      {
         throw new MessageFormatException("IOException: " + e.getMessage());
      }
      return retVal;
   }
   
   // SpyMessage overrides ------------------------------------------

   public void clearBody() throws JMSException
   {
      objectBytes = null;
      super.clearBody();
   }

   public SpyMessage myClone() throws JMSException
   {
      SpyObjectMessage result = MessagePool.getObjectMessage();
      result.copyProps(this);
      result.isByteArray = this.isByteArray;
      if (objectBytes != null)
        {
         result.objectBytes = new byte[this.objectBytes.length];
         System.arraycopy(this.objectBytes, 0, result.objectBytes, 0, this.objectBytes.length);
      }
      return result;
   }
   
   // Externalizable implementation ---------------------------------

   public void writeExternal(ObjectOutput out) throws IOException
   {
      super.writeExternal(out);
      out.writeBoolean(isByteArray);
      if (objectBytes == null)
      {
         out.writeInt(-1);
      }
      else
      {
         out.writeInt(objectBytes.length);
         out.write(objectBytes);
      }
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      super.readExternal(in);
      isByteArray = in.readBoolean();
      int length = in.readInt();
      if (length < 0)
      {
         objectBytes = null;
      }
      else
      {
         objectBytes = new byte[length];
         in.readFully(objectBytes);
      }
   }
   
   // Package protected ---------------------------------------------
   
   // Protected -----------------------------------------------------
   
   // Private -------------------------------------------------------
   
   // Inner classes -------------------------------------------------
}
