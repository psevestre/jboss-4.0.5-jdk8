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
package org.jboss.web.tomcat.tc5.session;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map;


/**
 * Implementation of a clustered session for the JBossCacheManager. The replication granularity
 * level is session based; that is, we replicate per whole session object.
 * We use JBossCache for our internal replicated data store.
 * The internal structure in JBossCache is as follows:
 * <pre>
 * /JSESSION
 *    /hostname
 *       /web_app_path    (path + session id is unique)
 *          /id    Map(id, session)
 *                    (VERSION_KEY, version)  // Used for version tracking. version is an Integer.
 * </pre>
 * <p/>
 * Note that the isolation level of the cache dictates the
 * concurrency behavior.</p>
 *
 * @author Ben Wang
 * @author Brian Stansberry
 * 
 * @version $Revision: 57206 $
 */
class SessionBasedClusteredSession
   extends JBossCacheClusteredSession
{
   static final long serialVersionUID = 3200976125245487256L;
   /**
    * Descriptive information describing this Session implementation.
    */
   protected static final String info = "SessionBasedClusteredSession/1.0";

   public SessionBasedClusteredSession(JBossCacheManager manager)
   {
      super(manager);
   }

   // ---------------------------------------------- Overridden Public Methods

   /**
    * Return a string representation of this object.
    */
   public String toString()
   {
      StringBuffer sb = new StringBuffer();
      sb.append("SessionBasedClusteredSession[");
      sb.append(super.toString());
      sb.append("]");
      return (sb.toString());

   }

   public void removeMyself()
   {
      proxy_.removeSession(realId);
   }

   public void removeMyselfLocal()
   {
      proxy_.removeSessionLocal(realId);
   }

   // ----------------------------------------------HttpSession Public Methods
   
   /**
    * Does nothing -- all attributes are populated already
    */
   protected void populateAttributes()
   {
      // no-op
   }

   protected Object getJBossInternalAttribute(String name)
   {
      Object result = attributes.get(name);

      // Do dirty check even if result is null, as w/ SET_AND_GET null
      // still makes us dirty (ensures timely replication w/o using ACCESS)
      if (isGetDirty(result))
      {
         sessionAttributesDirty();
      }

      return result;

   }

   protected Object removeJBossInternalAttribute(String name, 
                                                 boolean localCall, 
                                                 boolean localOnly)
   {
      if (localCall)
         sessionAttributesDirty();
      return attributes.remove(name);
   }

   protected Map getJBossInternalAttributes()
   {
      return attributes;
   }

   protected Object setJBossInternalAttribute(String name, Object value)
   {
      sessionAttributesDirty();
      return attributes.put(name, value);
   }

   /**
    * Overrides the superclass version by additionally reading the 
    * attributes map.
    * 
    * <p>
    * This method is deliberately public so it can be used to reset
    * the internal state of a session object using serialized
    * contents replicated from another JVM via JBossCache.
    * </p>
    * 
    * @see org.jboss.web.tomcat.tc5.session.ClusteredSession#readExternal(java.io.ObjectInput)
    */
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      synchronized (this)
      {
         // Let superclass read in everything but the attribute map
         super.readExternal(in);
      
         attributes = (Map) in.readObject();      
      }
   }

   /**
    * Overrides the superclass version by appending the attributes map. Does
    * not write any attributes whose names are found in 
    * {@link ClusteredSession#excludedAttributes}.
    * 
    * @see org.jboss.web.tomcat.tc5.session.ClusteredSession#writeExternal(java.io.ObjectOutput)
    */
   public void writeExternal(ObjectOutput out) throws IOException
   {
      synchronized (this)
      {
         // Let superclass write out everything but the attribute map
         super.writeExternal(out);
         
         // Don't replicate any excluded attributes
         Map excluded = removeExcludedAttributes(attributes);
        
         out.writeObject(attributes);
        
         // Restore any excluded attributes
         if (excluded != null)
            attributes.putAll(excluded);
      }   
   
   }
   

   /**
    * Overrides the superclass version to return <code>true</code> if either
    * the metadata or the attributes are dirty.
    */
   public boolean getReplicateSessionBody()
   {
      return isSessionDirty() || getExceedsMaxUnreplicatedInterval();
   }

}
