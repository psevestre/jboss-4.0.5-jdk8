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
package org.jboss.invocation.unified.marshall;

import org.jboss.remoting.marshal.serializable.SerializableUnMarshaller;
import org.jboss.remoting.marshal.UnMarshaller;
import org.jboss.remoting.marshal.http.HTTPUnMarshaller;
import org.jboss.remoting.InvocationRequest;
import org.jboss.invocation.MarshalledInvocation;
import org.jboss.tm.TransactionPropagationContextUtil;
import org.jboss.tm.TransactionPropagationContextImporter;

import java.io.InputStream;
import java.io.IOException;
import java.util.Map;

/**
 * This is a hollow implementation in that it only over rides the DATATYPE
 * value.  All behavior is that of SerializableUnMarshaller.
 *
 * @author <a href="mailto:tom@jboss.org">Tom Elrod</a>
 */
public class HTTPInvocationUnMarshaller extends HTTPUnMarshaller
{
   public final static String DATATYPE = "invocationhttp";

   public Object read(InputStream inputStream, Map metadata) throws IOException, ClassNotFoundException
   {
      Object ret = super.read(inputStream, metadata);

      if(ret instanceof InvocationRequest)
      {
         InvocationRequest remoteInv = (InvocationRequest) ret;
         Object param = remoteInv.getParameter();

         if(param instanceof MarshalledInvocation)
         {
            MarshalledInvocation mi = (MarshalledInvocation) param;
            Object txCxt = mi.getTransactionPropagationContext();
            if(txCxt != null)
            {
               TransactionPropagationContextImporter tpcImporter = TransactionPropagationContextUtil.getTPCImporter();
               mi.setTransaction(tpcImporter.importTransactionPropagationContext(txCxt));
            }
         }
      }

      return ret;
   }

   public UnMarshaller cloneUnMarshaller() throws CloneNotSupportedException
   {
      HTTPInvocationUnMarshaller unmarshaller = new HTTPInvocationUnMarshaller();
      unmarshaller.setClassLoader(this.customClassLoader);
      return unmarshaller;
   }
}