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
package org.jboss.tm;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57208 $
 */
public class TransactionPropagationContextUtil
{
   private static TransactionPropagationContextFactory tpcFactory;
   private static TransactionPropagationContextImporter tpcImporter;

   public static TransactionPropagationContextFactory getTPCFactoryClientSide()
   {
      return tpcFactory;
   }

   public static TransactionPropagationContextFactory getTPCFactory()
   {
      if (tpcFactory == null)
      {
         try
         {
            InitialContext ctx = new InitialContext();
            // Get the transaction propagation context factory
            tpcFactory = (TransactionPropagationContextFactory)
            ctx.lookup("java:/TransactionPropagationContextExporter");
         }
         catch (NamingException e)
         {
            throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
         }
      }
      return tpcFactory;
   }

   public static void setTPCFactory(TransactionPropagationContextFactory tpcFactory)
   {
      TransactionPropagationContextUtil.tpcFactory = tpcFactory;
   }

   public static TransactionPropagationContextImporter getTPCImporter()
   {
      if (tpcImporter == null)
      {
         try
         {
            InitialContext ctx = new InitialContext();
            // and the transaction propagation context importer
            tpcImporter = (TransactionPropagationContextImporter)
            ctx.lookup("java:/TransactionPropagationContextImporter");
         }
         catch (NamingException e)
         {
            throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
         }
      }
      return tpcImporter;
   }

   public static void setTPCImporter(TransactionPropagationContextImporter importer)
   {
      tpcImporter = importer;
   }
}
