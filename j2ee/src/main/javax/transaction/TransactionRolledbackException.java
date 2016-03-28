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
package javax.transaction;

import java.rmi.RemoteException;

/**
 *  This exception indicates that the transaction context carried in the
 *  remote invocation has been rolled back, or was marked for roll back only.
 *
 *  This indicates to the client that further computation on behalf of this
 *  transaction would be fruitless.
 *
 * @version $Revision: 57196 $
 */
public class TransactionRolledbackException extends RemoteException
{

    /**
     *  Creates a new <code>TransactionRolledbackException</code> without
     *  a detail message.
     */
    public TransactionRolledbackException()
    {
    }

    /**
     *  Constructs an <code>TransactionRolledbackException</code> with the
     *  specified detail message.
     *
     *  @param msg the detail message.
     */
    public TransactionRolledbackException(String msg)
    {
        super(msg);
    }
}
