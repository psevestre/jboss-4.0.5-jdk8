// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.wsrp.core;

import java.io.Serializable;


public class MarkupContext implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 7593781629145052222L;
   protected java.lang.Boolean useCachedMarkup;
   protected java.lang.String mimeType;
   protected java.lang.String markupString;
   protected byte[] markupBinary;
   protected java.lang.String locale;
   protected java.lang.Boolean requiresUrlRewriting;
   protected CacheControl cacheControl;
   protected java.lang.String preferredTitle;
   protected Extension[] extensions;

   public MarkupContext()
   {
   }

   public MarkupContext(java.lang.Boolean useCachedMarkup, java.lang.String mimeType, java.lang.String markupString, byte[] markupBinary, java.lang.String locale, java.lang.Boolean requiresUrlRewriting, CacheControl cacheControl, java.lang.String preferredTitle, Extension[] extensions)
   {
      this.useCachedMarkup = useCachedMarkup;
      this.mimeType = mimeType;
      this.markupString = markupString;
      this.markupBinary = markupBinary;
      this.locale = locale;
      this.requiresUrlRewriting = requiresUrlRewriting;
      this.cacheControl = cacheControl;
      this.preferredTitle = preferredTitle;
      this.extensions = extensions;
   }

   public java.lang.Boolean getUseCachedMarkup()
   {
      return useCachedMarkup;
   }

   public void setUseCachedMarkup(java.lang.Boolean useCachedMarkup)
   {
      this.useCachedMarkup = useCachedMarkup;
   }

   public java.lang.String getMimeType()
   {
      return mimeType;
   }

   public void setMimeType(java.lang.String mimeType)
   {
      this.mimeType = mimeType;
   }

   public java.lang.String getMarkupString()
   {
      return markupString;
   }

   public void setMarkupString(java.lang.String markupString)
   {
      this.markupString = markupString;
   }

   public byte[] getMarkupBinary()
   {
      return markupBinary;
   }

   public void setMarkupBinary(byte[] markupBinary)
   {
      this.markupBinary = markupBinary;
   }

   public java.lang.String getLocale()
   {
      return locale;
   }

   public void setLocale(java.lang.String locale)
   {
      this.locale = locale;
   }

   public java.lang.Boolean getRequiresUrlRewriting()
   {
      return requiresUrlRewriting;
   }

   public void setRequiresUrlRewriting(java.lang.Boolean requiresUrlRewriting)
   {
      this.requiresUrlRewriting = requiresUrlRewriting;
   }

   public CacheControl getCacheControl()
   {
      return cacheControl;
   }

   public void setCacheControl(CacheControl cacheControl)
   {
      this.cacheControl = cacheControl;
   }

   public java.lang.String getPreferredTitle()
   {
      return preferredTitle;
   }

   public void setPreferredTitle(java.lang.String preferredTitle)
   {
      this.preferredTitle = preferredTitle;
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