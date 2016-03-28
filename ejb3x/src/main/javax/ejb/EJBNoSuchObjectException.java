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
package javax.ejb;

/**
 * A NoSuchObjectLocalException is thrown if an attempt is made to invoke
 * a method on an object that no longer exists.
 * @version $Revision: 57192 $
 */
public class EJBNoSuchObjectException extends EJBException {

  /**
   * Constructs a NoSuchObjectLocalException with no detail message.
   */
  public EJBNoSuchObjectException() {
    super();
  }

  /**
   * Constructs a NoSuchObjectLocalException with the specified detailed message.
   *
   * @param message - The detailed message.
   */
  public EJBNoSuchObjectException(String message) {
    super(message);
  }

  /**
   * Constructs a NoSuchObjectLocalException with the specified detail message and a
   * nested exception.
   *
   * @param message - The detailed message.
   * @param ex - The originally thrown exception.
   */
  public EJBNoSuchObjectException(String message, Exception ex) {
    super(message,ex);
  }
}
