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

package org.apache.directory.studio.ldapbrowser.core.model;


/**
 * @deprecated This class will be removed in the next version. The Dn/Rdn/RDNPart
 * classes are replaced with the shared-ldap LdapDN/Rdn/ATAV. This class just 
 * remains to provide backward compatibility of the old browserconnections.xml
 * file that stores searches and bookmarks.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class NameException extends Exception
{

    private static final long serialVersionUID = 1L;


    /**
     * Creates a new instance of NameException.
     * 
     * @param message the message
     */
    public NameException( String message )
    {
        super( message );
    }

}
