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
package org.jboss.test.aop.annotated.declare;

import org.jboss.aop.pointcut.Pointcut;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 57183 $
 * @@org.jboss.aop.Aspect (scope=org.jboss.aop.advice.Scope.PER_VM)
 */
public class DeclareAspect
{
   /**
    * @@org.jboss.aop.DeclareError (expr="call(* org.jboss.test.aop.annotated.declare.POJO->someMethod()) AND withincode(* org.jboss.test.aop.annotated.declare.POJO->otherMethod())", msg="Should not happen")
    */
   Pointcut error;

   /**
    * @@org.jboss.aop.DeclareWarning (expr="call(* org.jboss.test.aop.annotated.declare.POJO->otherMethod()) AND withincode(* org.jboss.test.aop.annotated.declare.POJO->someMethod())", msg="Expected annotated warning")
    */
   Pointcut warning;
   
}
