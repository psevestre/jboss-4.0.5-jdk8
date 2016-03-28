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
package org.jboss.security.auth.certs;

import java.security.Principal;
import java.security.cert.X509Certificate;
import java.io.Serializable;
import org.jboss.security.CertificatePrincipal;
import org.jboss.security.SimplePrincipal;

/** A CertificatePrincipal implementation that uses the client cert
 * SubjectDN as the principal.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57203 $
 */
public class SubjectDNMapping
   implements CertificatePrincipal
{
   /** Returns the client cert SubjectDN as the principal.
    *
    * @param certs Array of client certificates, with the first one in
    * the array being the certificate of the client itself.
    */
   public Principal toPrinicipal(X509Certificate[] certs)
   {
      Principal subject = certs[0].getSubjectDN();
      // Convert to a serializable principal
      if( (subject instanceof Serializable) == false )
      {
         String name = subject.getName();
         subject = new SimplePrincipal(name);
      }
      return subject;
   }
}
