// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.wsrp.core;

import java.io.Serializable;


public class RuntimeContext implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -8571602653678634933L;
   protected java.lang.String userAuthentication;
   protected java.lang.String portletInstanceKey;
   protected java.lang.String namespacePrefix;
   protected Templates templates;
   protected java.lang.String sessionID;
   protected Extension[] extensions;

   public RuntimeContext()
   {
   }

   public RuntimeContext(java.lang.String userAuthentication, java.lang.String portletInstanceKey, java.lang.String namespacePrefix, Templates templates, java.lang.String sessionID, Extension[] extensions)
   {
      this.userAuthentication = userAuthentication;
      this.portletInstanceKey = portletInstanceKey;
      this.namespacePrefix = namespacePrefix;
      this.templates = templates;
      this.sessionID = sessionID;
      this.extensions = extensions;
   }

   public java.lang.String getUserAuthentication()
   {
      return userAuthentication;
   }

   public void setUserAuthentication(java.lang.String userAuthentication)
   {
      this.userAuthentication = userAuthentication;
   }

   public java.lang.String getPortletInstanceKey()
   {
      return portletInstanceKey;
   }

   public void setPortletInstanceKey(java.lang.String portletInstanceKey)
   {
      this.portletInstanceKey = portletInstanceKey;
   }

   public java.lang.String getNamespacePrefix()
   {
      return namespacePrefix;
   }

   public void setNamespacePrefix(java.lang.String namespacePrefix)
   {
      this.namespacePrefix = namespacePrefix;
   }

   public Templates getTemplates()
   {
      return templates;
   }

   public void setTemplates(Templates templates)
   {
      this.templates = templates;
   }

   public java.lang.String getSessionID()
   {
      return sessionID;
   }

   public void setSessionID(java.lang.String sessionID)
   {
      this.sessionID = sessionID;
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
