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
package org.jboss.ejb3.dd;

import java.util.List;
import java.util.ArrayList;

import org.jboss.logging.Logger;

/**
 * Represents a activation-config-property element of the ejb-jar.xml deployment descriptor for
 * the 1.4 schema
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version <tt>$Revision: 57207 $</tt>
 */
public class ActivationConfigProperty
{
   private static final Logger log = Logger.getLogger(ActivationConfigProperty.class);

   // ejb-jar.xml
   private String name;
   private String value;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }
   
   public String getValue()
   {
      return value;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append('[');
      sb.append("name=").append(name);
      sb.append(", value=").append(value);
      sb.append(']');
      return sb.toString();
   }
}
