/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.studio.test.integration.junit5;


import static org.apache.directory.studio.test.integration.junit5.Constants.LOCALHOST;

import java.io.File;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.ldap.handlers.extended.PwdModifyHandler;
import org.apache.directory.server.ldap.handlers.extended.WhoAmIHandler;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.protocol.shared.transport.Transport;
import org.apache.mina.util.AvailablePortFinder;


/**
 * An ApacheDS implementation of a test LDAP server.
 * 
 * This implementation starts an embedded LdapServer and adds a partition dc=example,dc=org.
 */
public class ApacheDirectoryServer extends TestLdapServer
{

    private static DirectoryService service;
    private static LdapServer server;

    public static ApacheDirectoryServer getInstance()
    {
        if ( server == null )
        {
            startServer();
        }
        return new ApacheDirectoryServer();
    }


    private static void startServer()
    {
        try
        {
            DefaultDirectoryServiceFactory factory = new DefaultDirectoryServiceFactory();
            factory.init( "test" );
            service = factory.getDirectoryService();
            Partition partition = factory.getPartitionFactory().createPartition( service.getSchemaManager(),
                service.getDnFactory(), "example.org", "dc=example,dc=org", 1024,
                new File( service.getInstanceLayout().getPartitionsDirectory(), "example.org" ) );
            partition.initialize();
            service.addPartition( partition );

            server = new LdapServer();
            server.setDirectoryService( service );
            int port = AvailablePortFinder.getNextAvailable( 1024 );
            Transport ldap = new TcpTransport( port );
            server.addTransports( ldap );
            server.addExtendedOperationHandler( new PwdModifyHandler() );
            server.addExtendedOperationHandler( new WhoAmIHandler() );

            server.start();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }


    private ApacheDirectoryServer()
    {
        super( LdapServerType.ApacheDS, LOCALHOST, server.getPort(), "uid=admin,ou=system", "secret" );
    }

}
