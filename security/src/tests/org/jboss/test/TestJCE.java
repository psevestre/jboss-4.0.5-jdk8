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
package org.jboss.test;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Iterator;
import java.lang.reflect.Constructor;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/** Tests of the Java Cryptography Extension framework
 @author Scott.Stark@jboss.org
 @version $Revision: 37390 $
*/
public class TestJCE
{
   static void showProviders() throws Exception
   {
      Provider[] providers = Security.getProviders();
      for(int p = 0; p < providers.length; p ++)
      {
         Iterator iter = providers[p].keySet().iterator();
         System.out.println("Provider: "+providers[p].getInfo());
         while( iter.hasNext() )
         {
            String key = (String) iter.next();
            System.out.println("  key="+key+", value="+providers[p].getProperty(key));
         }
      }
   }

   static void testBlowfish() throws Exception
   {
      KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
      Cipher cipher = Cipher.getInstance("Blowfish");
      SecretKey key = null;
      int minKeyBits = -1, maxKeyBits = 0;
      int minCipherBits = -1, maxCipherBits = 0;
      for(int size = 1; size <= 448/8; size ++)
      {
         int bits = size * 8;
         try
         {
            kgen.init(bits);
            key = kgen.generateKey();
            if( minKeyBits == -1 )
               minKeyBits = bits;
            maxKeyBits = bits;
         }
         catch(Exception e)
         {
            System.out.println("Failed to create key with bits="+bits);
            e.printStackTrace();
            continue;
         }

         try
         {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            if( minCipherBits == -1 )
               minCipherBits = bits;
            maxCipherBits = bits;
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }
      }
      System.out.println("Key range: "+minKeyBits+".."+maxKeyBits);
      System.out.println("Cipher range: "+minCipherBits+".."+maxCipherBits);
   }

   static void testKey() throws Exception
   {
      int size = 8 * 24;
      KeyGenerator kgen = KeyGenerator.getInstance("Blowfish");
      kgen.init(size);
      SecretKey key = kgen.generateKey();
      byte[] kbytes = key.getEncoded();
      System.out.println("key.Algorithm = "+key.getAlgorithm());
      System.out.println("key.Format = "+key.getFormat());
      System.out.println("key.Encoded Size = "+kbytes.length);
      
      Cipher cipher = Cipher.getInstance("Blowfish");
      AlgorithmParameters params = cipher.getParameters();
      System.out.println("Blowfish.params = "+params);
      cipher.init(Cipher.ENCRYPT_MODE, key);
      SealedObject msg = new SealedObject("This is a secret", cipher);
      
      SecretKeySpec serverKey = new SecretKeySpec(kbytes, "Blowfish");
      Cipher scipher = Cipher.getInstance("Blowfish");
      scipher.init(Cipher.DECRYPT_MODE, serverKey);
      String theMsg = (String) msg.getObject(scipher);
      System.out.println("Decrypted: "+theMsg);
      
      SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
      BigInteger bi = new BigInteger(320, rnd);
      byte[] k2bytes = bi.toByteArray();
      SecretKeySpec keySpec = new SecretKeySpec(k2bytes, "Blowfish");
      System.out.println("key2.Algorithm = "+key.getAlgorithm());
      System.out.println("key2.Format = "+key.getFormat());
      System.out.println("key2.Encoded Size = "+kbytes.length);
   }

   static void testKey2() throws Exception
   {
      byte[] key = new byte[40];
      for(int n = 0; n < 40; n ++)
         key[n] = (byte) (n+100);
      String cipherAlgorithm = "Blowfish";
      Class[] signature = {key.getClass(), String.class};
      Object[] args = {key, cipherAlgorithm};
      Object secretKey = null;
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class secretKeySpecClass = loader.loadClass("javax.crypto.spec.SecretKeySpec");
      Constructor ctor = secretKeySpecClass.getDeclaredConstructor(signature);
      secretKey = ctor.newInstance(args);
      System.out.println("SecretKey: "+secretKey);
   }
   public static void main(String[] args)
   {
      try
      {
         System.setOut(System.err);
         TestJCE tst = new TestJCE();
         //tst.showProviders();
         tst.testKey2();
         //tst.testBlowfish();
      }
      catch(Throwable t)
      {
         t.printStackTrace();
      }
   }
}
