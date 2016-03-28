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
package org.jboss.test.webservice.attachmentstepbystep;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.RemoteException;
import javax.activation.DataHandler;



/**
 * Implementation des méthodes du service
 */
public class MyServiceImpl implements MyService  
{
    
    // CTOR
    public MyServiceImpl() {
        super();
    }
    
    public DataHandler myService(DataHandler mimepart) throws RemoteException
    {
        URL url = null;
        String result="";
        try {
            // print the recieved file
            InputStream is = mimepart.getInputStream();
            if (is != null) {
                BufferedReader in =  new BufferedReader(new InputStreamReader(is));
                
                String line="";
                line = in.readLine();
                result = "" + line;
                while (line != null)
                {
                    System.out.println(line);
                    line = in.readLine();
                    result = result + line;
                }
                in.close();
            }
            // the file to return if size of received file == 192
            if (result.length() == 190)
            {
                url = new URL("http://" + System.getProperty("jboss.bind.address", "localhost") + ":8080/myservice/resources/attachment_server2client.txt");
            }
            else
            {
                throw new Exception("The received file isn't 190 bytes length as expected. Length = " + result.length());
            }
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
        return new DataHandler(url);
    }

    
}
