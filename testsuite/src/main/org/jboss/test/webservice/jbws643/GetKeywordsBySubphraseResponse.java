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
package org.jboss.test.webservice.jbws643;

public class GetKeywordsBySubphraseResponse
{

   protected org.jboss.test.webservice.jbws643.KeywordType inputKeyword;

   protected org.jboss.test.webservice.jbws643.KeywordDetailType[] relatedKeyword;

   public GetKeywordsBySubphraseResponse()
   {
   }

   public GetKeywordsBySubphraseResponse(org.jboss.test.webservice.jbws643.KeywordType inputKeyword,
         org.jboss.test.webservice.jbws643.KeywordDetailType[] relatedKeyword)
   {
      this.inputKeyword = inputKeyword;
      this.relatedKeyword = relatedKeyword;
   }

   public org.jboss.test.webservice.jbws643.KeywordType getInputKeyword()
   {
      return inputKeyword;
   }

   public void setInputKeyword(org.jboss.test.webservice.jbws643.KeywordType inputKeyword)
   {
      this.inputKeyword = inputKeyword;
   }

   public org.jboss.test.webservice.jbws643.KeywordDetailType[] getRelatedKeyword()
   {
      return relatedKeyword;
   }

   public void setRelatedKeyword(org.jboss.test.webservice.jbws643.KeywordDetailType[] relatedKeyword)
   {
      this.relatedKeyword = relatedKeyword;
   }

}
