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
package org.jboss.mx.util;

import javax.management.NotCompliantMBeanException;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * JBossNotCompliantMBeanException.java
 *
 *
 * Created: Tues Feb  18 22:45:03 2003
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version $Revision: 57200 $
 */

public class JBossNotCompliantMBeanException
   extends NotCompliantMBeanException
{
   private static final long serialVersionUID = 7441880585611238427L;

   private final Throwable t;

   public JBossNotCompliantMBeanException()
   {
      super();
      t = null;
   }

   public JBossNotCompliantMBeanException(String message)
   {
      super(message);
      t = null;
   }

   public JBossNotCompliantMBeanException(Throwable t)
   {
      super();
      this.t = t;
   }

   public JBossNotCompliantMBeanException(String message, Throwable t)
   {
      super(message);
      this.t = t;
   }

   // Implementation of org.jboss.util.NestedThrowable

   public Throwable getNested()
   {
      return t;
   }

   public Throwable getCause()
   {
      return t;
   }

   /**
    * Returns the composite throwable message.
    *
    * @return  The composite throwable message.
    */
   public String getMessage()
   {
      //return NestedThrowable.Util.getMessage(super.getMessage(), t);
      return super.getMessage();
   }

   /**
    * Prints the composite message and the embedded stack trace to the
    * specified print stream.
    *
    * @param stream  Stream to print to.
    */
   public void printStackTrace(final PrintStream stream)
   {
      //if (t == null || NestedThrowable.PARENT_TRACE_ENABLED)
      {
         super.printStackTrace(stream);
      }
      //NestedThrowable.Util.print(t, stream);
   }

   /**
    * Prints the composite message and the embedded stack trace to the
    * specified print writer.
    *
    * @param writer  Writer to print to.
    */
   public void printStackTrace(final PrintWriter writer)
   {
      //if (t == null || NestedThrowable.PARENT_TRACE_ENABLED)
      {
         super.printStackTrace(writer);
      }
      //NestedThrowable.Util.print(t, writer);
   }

   /**
    * Prints the composite message and the embedded stack trace to
    * <tt>System.err</tt>.
    */
   public void printStackTrace()
   {
      printStackTrace(System.err);
   }

}// JBossNotCompliantMBeanException

