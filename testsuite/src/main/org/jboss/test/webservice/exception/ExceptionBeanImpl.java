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
package org.jboss.test.webservice.exception;

import org.jboss.logging.Logger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

/** A SLSB that throws custom exceptions
 */
public class ExceptionBeanImpl implements SessionBean
{
   public void throwException() throws UserException
   {
      throw new UserException();
   }

   public void throwExceptionWithMessage(String message) throws UserMessageException
   {
      throw new UserMessageException(message);
   }

   public void throwComplexUserException(String message, int code) throws ComplexUserException
   {
      throw new ComplexUserException(message, code);
   }

   public void throwComplexUserArrayException(String message, int[] codes) throws ComplexUserArrayException
   {
      ComplexUserArrayException arrEx = new ComplexUserArrayException(message, codes);
      throw arrEx;
   }

   // EJB life cycle methods *******************************************************************************************

   public void ejbCreate()
   {
   }

   public void ejbRemove()
   {
   }

   public void ejbActivate()
   {
   }

   public void ejbPassivate()
   {
   }

   public void setSessionContext(SessionContext sc)
   {
   }
}
