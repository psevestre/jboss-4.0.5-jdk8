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
package org.jboss.mx.loading;

import java.util.*;

/**
 * Dataholder class used with MBean file parsers. Contains the information
 * that at minimum should allow the MBean loaded and registered to the MBean
 * server.
 *
 * @see org.jboss.mx.loading.MBeanFileParser
 * @see org.jboss.mx.loading.MLetParser
 * @see org.jboss.mx.loading.XMLMBeanParser
 *
 * @author  <a href="mailto:juha@jboss.org">Juha Lindfors</a>.
 * @version $Revision: 57200 $
 *
 */
public class MBeanElement
{
   // Attributes ----------------------------------------------------

    // Constants -----------------------------------------------------

    /**
     * Property key for setting the boolean value 'delegateToCLR' for MLet
     * MBeans (JMX 1.2)
     */
    public final static String MLET_DELEGATE_TO_CLR = "delegateToCLR";

    /**
     * Additional properties to be provided to the MBean installer/registration
     * process. This map should contain properties that are not necessarily
     * shared between all MBeans (for example, the delegateToCLR property for
     * MLets).
     */
    private Map properties = new HashMap(2);

   /**
    * Fully qualified class name.
    */
   private String code        = null;

   /**
    * Name of serialized MBean representation in the archive.
    */
   private String object      = null;

   /**
    * Object name
    */
   private String name        = null;

   /**
    * Overrides default codebase.
    */
   private String codebase    = null;

   /**
    * MBean jars.
    */
   private ArrayList archives = new ArrayList();

   /**
    * spec only allows one version tag -- doesn't work very well with an archivelist.
    */
   private ArrayList versions = new ArrayList();

   /**
    * MBean constructor argument types.
    */
   private ArrayList argTypes    = new ArrayList();

   /**
    * MBean constructor argument values.
    */
   private ArrayList argValues   = new ArrayList();


   // Public --------------------------------------------------------

   /**
    * Returns fully qualified class name of the MBean.
    *
    * @return class name or <tt>null</tt> if name not set
    */
   public String getCode()
   {
      return code;
   }

   /**
    * Returns the name of a serialized MBean representation in the archive.
    * Note that if the archive contains a file structure then the path to the
    * serialized file is included in this string.
    *
    * @return serial file name or <tt>null</tt> if not set
    */
   public String getObject()
   {
      return object;
   }

   /**
    * Returns the object name of the MBean.
    *
    * @return string representation of object name or <tt>null</tt> if not set
    */
   public String getName()
   {
      return name;
   }

   /**
    * Returns MBean archives.
    *
    * @return a list of MBean Java archives. An empty list if archives is not set.
    */
   public List getArchives()
   {
      return archives;
   }

   /**
    * Returns MBean versions.
    *
    * @return a list of MBean versions. An empty list if versions is not set.
    */
   public List getVersions()
   {
      return versions;
   }

   /**
    * Returns MBean codebase URL.
    *
    * @return codebase or <tt>null</tt> if not set
    */
   public String getCodebase()
   {
      return codebase;
   }

   /**
    * Sets the fully qualified class name of the MBean entry. The name is trimmed
    * of quotes (") and additional equals (=) sign.
    *
    * @param   code     fully qualified class name of the MBean
    */
   public void setCode(String code)
   {
      this.code = trim(code);
      if (this.code.endsWith(".class"))
         this.code = this.code.substring(0, this.code.length() - 6);
   }

   /**
    * Sets the name of the serialized MBean instance. Notice that if the archive
    * contains a file structure its path must be included in the name. Tje name is
    * trimmed of quotes (") and additional equals (=) sign.
    *
    * @param   object   file name and path in the archive
    */
   public void setObject(String object)
   {
      this.object = trim(object);
   }

   /**
    * Sets the object name of the MBean. The name is trimmed of quotes (") and additional
    * equals (=) sign.
    *
    * @param   name  string representation of an MBean object name
    */
   public void setName(String name)
   {
      this.name = trim(name);
   }

   /**
    * Sets the code base for an MLET entry. The codebase is trimmed of quotes (") and
    * additional equals (=) sign.
    *
    * @param   url   url string pointing to the codebase
    */
   public void setCodebase(String url)
   {
      this.codebase = trim(url);
   }

   public void setArchive(String archive)
   {
      archive = trim(archive);
      StringTokenizer tokenizer = new StringTokenizer(archive, " ,");

      while (tokenizer.hasMoreTokens())
         archives.add(tokenizer.nextToken());
   }

   public void setVersion(String version)
   {
      version = trim(version);
      StringTokenizer tokenizer = new StringTokenizer(version, " ,");

      while (tokenizer.hasMoreTokens())
         versions.add(tokenizer.nextToken());
   }

   public void addArg(String type, String value)
   {
      argTypes.add(trim(type));
      argValues.add(trim(value));
   }

   public String[] getConstructorTypes()
   {
      return (String[])argTypes.toArray(new String[0]);
   }

   public String[] getConstructorValues()
   {
      return (String[])argValues.toArray(new String[0]);
   }

   // Private -------------------------------------------------------
   private String trim(String str)
   {
      if (str == null)
         return str;

      // trim values that start with '="someValue"'
      if (str.startsWith("="))
         str = str.substring(1, str.length());

      if (str.startsWith("\"") && str.endsWith("\""))
         return str.substring(1, str.length() - 1);
      else
         return str;
   }

    public void setProperty(String key, Object value)
    {
       if (key == null || key.equals(""))
          throw new IllegalArgumentException("null or empty string keys not allowed");
       if (value == null)
          throw new IllegalArgumentException("null values not allowed");

       properties.put(key, value);
    }

    public Object getProperty(String key)
    {
       return properties.get(key);
    }

}




