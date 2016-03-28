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
package org.jboss.jms.destination;

import javax.jms.JMSException;
import javax.jms.TemporaryTopic;

import org.jboss.jms.client.ConnectionDelegate;

/**
 * A temporary topic
 * 
 * @author <a href="mailto:adrian@jboss.org>Adrian Brock</a>
 * @version $Revision: 57195 $
 */
public class JBossTemporaryTopic
   extends JBossTopic
   implements TemporaryTopic, JBossTemporaryDestination
{
   // Constants -----------------------------------------------------

   // Attributes ----------------------------------------------------

   /** The connection */
   private ConnectionDelegate delegate;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   /**
    * Construct a new temporary topic
    * 
    * @param delegate the connection
    * @param name the name
    */
   public JBossTemporaryTopic(ConnectionDelegate delegate, String name)
   {
      super(name);
      this.delegate = delegate;
   }

   // Public --------------------------------------------------------

   // TemporaryTopic implementation ---------------------------------

   public void delete()
      throws JMSException
   {
      delegate.deleteTempDestination(this);
   }

   // Protected ------------------------------------------------------

   // Package Private ------------------------------------------------

   // Private --------------------------------------------------------

   // Inner Classes --------------------------------------------------

}
