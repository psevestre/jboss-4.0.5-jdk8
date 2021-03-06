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
package org.jnp.test;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.ServerSocket;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Properties;
import javax.naming.InitialContext;

import junit.framework.TestSuite;
import junit.framework.TestCase;

import org.jnp.server.Main;

/** A test of RMI custom sockets with the jnp JNDI provider.

@author Scott_Stark@displayscape.com
@version $Revision: 57199 $
*/
public class TestJNPSockets extends TestCase
{
    static Main server;
    static int serverPort;

    public TestJNPSockets(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        if( server != null )
            return;

        server = new Main();
        server.setPort(0);
        server.setBindAddress("localhost");
        server.setClientSocketFactory(ClientSocketFactory.class.getName());
        server.setServerSocketFactory(ServerSocketFactory.class.getName());
        server.start();
        serverPort = server.getPort();
    }

    public void testAccess() throws Exception
    {
        Properties env = new Properties();
        env.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        env.setProperty("java.naming.provider.url", "localhost:"+serverPort);
        env.setProperty("java.naming.factory.url.pkgs", "org.jnp.interfaces");
        InitialContext ctx = new InitialContext(env);
        System.out.println("Connected to jnp service");
        ctx.list("");
        ctx.close();
        if( ClientSocketFactory.created == false )
            fail("No ClientSocketFactory was created");
        if( ServerSocketFactory.created == false )
            fail("No ServerSocketFactory was created");
        server.stop();
    }

    public static void main(String[] args) throws Exception
    {
        System.setErr(System.out);
        org.apache.log4j.BasicConfigurator.configure();
        TestSuite suite = new TestSuite(TestJNPSockets.class);
        junit.textui.TestRunner.run(suite);
    }

    public static class ClientSocketFactory implements RMIClientSocketFactory, Serializable
    {
       static final long serialVersionUID = -3951228824124738736L;
       static boolean created;
        public Socket createSocket(String host, int port) throws IOException
        {
            Socket clientSocket = new Socket(host, port);
            System.out.println("createSocket -> "+clientSocket);
            created = true;
            return clientSocket;
        }
    }

    public static class ServerSocketFactory implements RMIServerSocketFactory, Serializable
    {
       static final long serialVersionUID = -2632886892871952872L;
       static boolean created;
        public ServerSocket createServerSocket(int port) throws IOException
        {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("createServerSocket -> "+serverSocket);
            created = true;
            return serverSocket;
        }
    }
}
