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
package org.jboss.jms.util;

import java.util.Collection;

/**
 *
 * @author <a href="mailto:nathan@jboss.org">Nathan Phelps</a>
 * @version $Revision: 57195 $ $Date: 2006-09-26 08:08:17 -0400 (Tue, 26 Sep 2006) $
 */
public interface PriorityQueue
{
    public void enqueue(Object object);

    public void enqueue(Collection collection);

    public Object dequeue();

    public Collection dequeue(int maximumItems);

    public Object peek();

    public Collection peek(int maximumItems);

    public void purge();

    public int size();

    public boolean isEmpty();
}
