// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.wsrp.core;

import java.io.Serializable;


public class SessionContext implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 3270889220340919064L;
   protected java.lang.String sessionID;
   protected int expires;
   protected Extension[] extensions;

   public SessionContext()
   {
   }

   public SessionContext(java.lang.String sessionID, int expires, Extension[] extensions)
   {
      this.sessionID = sessionID;
      this.expires = expires;
      this.extensions = extensions;
   }

   public java.lang.String getSessionID()
   {
      return sessionID;
   }

   public void setSessionID(java.lang.String sessionID)
   {
      this.sessionID = sessionID;
   }

   public int getExpires()
   {
      return expires;
   }

   public void setExpires(int expires)
   {
      this.expires = expires;
   }

   public Extension[] getExtensions()
   {
      return extensions;
   }

   public void setExtensions(Extension[] extensions)
   {
      this.extensions = extensions;
   }
}
