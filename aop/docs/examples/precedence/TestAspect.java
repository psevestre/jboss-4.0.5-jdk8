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
import org.jboss.aop.joinpoint.Invocation;

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 57185 $
 */
public class TestAspect
{
   public Object advice(Invocation invocation)throws Throwable
   {
      try
      {
         System.out.println("<<< Entering TestAspect.advice type:" + invocation);
         return invocation.invokeNext();
      }
      finally
      {
         System.out.println("<<< Leaving TestAspect.advice type:" + invocation);
      }
   }

   public Object otherAdvice(Invocation invocation)throws Throwable
   {
      try
      {
         System.out.println("<<< Entering TestAspect.otherAdvice type:" + invocation);
         return invocation.invokeNext();
      }
      finally
      {
         System.out.println("<<< Leaving TestAspect.otherAdvice type:" + invocation);
      }
   }

}
