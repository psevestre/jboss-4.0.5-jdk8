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

/**
 *  This interface allows an application to explicitly manage transactions.
 *
 *  @version $Revision: 57196 $
 */
public interface UserTransaction
{
    /**
     *  Starts a new transaction, and associate it with the calling thread.
     *
     *  @throws NotSupportedException If the calling thread is already
     *          associated with a transaction, and nested transactions are
     *          not supported.
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     */
    public void begin() throws NotSupportedException, SystemException;

    /**
     *  Commit the transaction associated with the calling thread.
     *
     *  @throws RollbackException If the transaction was marked for rollback
     *          only, the transaction is rolled back and this exception is
     *          thrown.
     *  @throws IllegalStateException If the calling thread is not associated
     *          with a transaction.
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     *  @throws HeuristicMixedException If a heuristic decision was made and
     *          some some parts of the transaction have been committed while
     *          other parts have been rolled back.
     *  @throws HeuristicRollbackException If a heuristic decision to roll
     *          back the transaction was made.
     *  @throws SecurityException If the caller is not allowed to commit this
     *          transaction.
     */
    public void commit() throws RollbackException, HeuristicMixedException,
                   HeuristicRollbackException, SecurityException,
                   IllegalStateException, SystemException;

    /**
     *  Rolls back the transaction associated with the calling thread.
     * 
     *  @throws IllegalStateException If the transaction is in a state
     *          where it cannot be rolled back. This could be because the
     *          calling thread is not associated with a transaction, or
     *          because it is in the
     *          {@link Status#STATUS_PREPARED prepared state}.
     *  @throws SecurityException If the caller is not allowed to roll back
     *          this transaction.
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     */
    public void rollback() throws IllegalStateException, SecurityException,
                   SystemException;

    /**
     *  Mark the transaction associated with the calling thread for rollback
     *  only.
     * 
     *  @throws IllegalStateException If the transaction is in a state
     *          where it cannot be rolled back. This could be because the
     *          calling thread is not associated with a transaction, or
     *          because it is in the
     *          {@link Status#STATUS_PREPARED prepared state}.
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     */
    public void setRollbackOnly() throws IllegalStateException, SystemException;

    /**
     *  Get the status of the transaction associated with the calling thread.
     *
     *  @return The status of the transaction. This is one of the
     *          {@link Status} constants. If no transaction is associated
     *          with the calling thread,
     *          {@link Status#STATUS_NO_TRANSACTION} is returned. 
     *
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     */
    public int getStatus() throws SystemException;

    /**
     *  Change the transaction timeout for transactions started by the calling
     *  thread with the {@link #begin()} method.
     *
     *  @param seconds The new timeout value, in seconds. If this parameter
     *         is <code>0</code>, the timeout value is reset to the default
     *         value.
     *
     *  @throws SystemException If the transaction service fails in an
     *          unexpected way.
     */
    public void setTransactionTimeout(int seconds) throws SystemException;
}
