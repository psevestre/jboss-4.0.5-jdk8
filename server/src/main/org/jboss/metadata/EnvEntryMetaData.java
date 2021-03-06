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
package org.jboss.metadata;

import javax.naming.Context;
import javax.naming.NamingException;

import org.w3c.dom.Element;

import org.jboss.deployment.DeploymentException;
import org.jboss.util.naming.Util;

/**
 * <description>
 * 
 * @see <related>
 * @author <a href="mailto:sebastien.alborini@m4x.org">Sebastien Alborini </a>
 * @version $Revision: 57209 $
 */
public class EnvEntryMetaData extends MetaData
{
	// Constants -----------------------------------------------------

	// Attributes ----------------------------------------------------
	private String name;

	private String type;

	private String value;

	// Static --------------------------------------------------------

	// Constructors --------------------------------------------------
	public EnvEntryMetaData()
	{
	}

	// Public --------------------------------------------------------

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public String getValue()
	{
		return value;
	}

	public void importEjbJarXml(Element element) throws DeploymentException
	{
		name = getElementContent(getUniqueChild(element, "env-entry-name"));
		type = getElementContent(getUniqueChild(element, "env-entry-type"));
		// Strip any surrounding spaces
		type = type.trim();
		value = getElementContent(getUniqueChild(element, "env-entry-value"));
	}

	public static void bindEnvEntry(Context ctx, EnvEntryMetaData entry) throws ClassNotFoundException, NamingException
	{
		ClassLoader loader = EnvEntryMetaData.class.getClassLoader();
		Class type = loader.loadClass(entry.getType());
		if (type == String.class)
		{
			Util.bind(ctx, entry.getName(), entry.getValue());
		}
		else if (type == Integer.class)
		{
			Util.bind(ctx, entry.getName(), new Integer(entry.getValue()));
		}
		else if (type == Long.class)
		{
			Util.bind(ctx, entry.getName(), new Long(entry.getValue()));
		}
		else if (type == Double.class)
		{
			Util.bind(ctx, entry.getName(), new Double(entry.getValue()));
		}
		else if (type == Float.class)
		{
			Util.bind(ctx, entry.getName(), new Float(entry.getValue()));
		}
		else if (type == Byte.class)
		{
			Util.bind(ctx, entry.getName(), new Byte(entry.getValue()));
		}
		else if (type == Character.class)
		{
			Object value = null;
			String input = entry.getValue();
			if (input == null || input.length() == 0)
			{
				value = new Character((char) 0);
			}
			else
			{
				if (input.length() > 1)
					// TODO: Add deployment context
					log.warn("Warning character env-entry is too long: binding=" + entry.getName() + " value=" + input);
				value = new Character(input.charAt(0));
			}
			Util.bind(ctx, entry.getName(), value);
		}
		else if (type == Short.class)
		{
			Util.bind(ctx, entry.getName(), new Short(entry.getValue()));
		}
		else if (type == Boolean.class)
		{
			Util.bind(ctx, entry.getName(), new Boolean(entry.getValue()));
		}
		else
		{
			// Default to a String type
			Util.bind(ctx, entry.getName(), entry.getValue());
		}
	}

}
