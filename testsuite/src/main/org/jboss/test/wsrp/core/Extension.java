// This class was generated by the JAXRPC SI, do not edit.
// Contents subject to change without notice.
// JAX-RPC Standard Implementation (1.1.3, build R1)
// Generated source version: 1.1.3

package org.jboss.test.wsrp.core;

import java.io.Serializable;


public class Extension implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -1197474564664405762L;
   protected javax.xml.soap.SOAPElement _any;

   public Extension()
   {
   }

   public Extension(javax.xml.soap.SOAPElement _any)
   {
      this._any = _any;
   }

   public javax.xml.soap.SOAPElement get_any()
   {
      return _any;
   }

   public void set_any(javax.xml.soap.SOAPElement _any)
   {
      this._any = _any;
   }
}
