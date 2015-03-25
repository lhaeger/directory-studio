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
package org.apache.directory.studio.openldap.syncrepl;


import java.text.ParseException;


/**
 * This enum implements all the possible values for the bind method value.
 */
public enum BindMethod
{
    /** The 'simple' bind method value */
    SIMPLE("simple"),

    /** The 'sash' bind method value */
    SASL("sasl");

    /** The value */
    private String value;


    /**
     * Parses a bind method string.
     *
     * @param s the string
     * @return a bind method
     * @throws ParseException if an error occurs during parsing
     */
    public static BindMethod parse( String s ) throws ParseException
    {
        // SIMPLE
        if ( SIMPLE.value.equalsIgnoreCase( s ) )
        {
            return SIMPLE;
        }
        // SASL
        else if ( SASL.value.equalsIgnoreCase( s ) )
        {
            return SASL;
        }
        else
        {
            throw new ParseException( "Unable to parse string '" + s + "' as a valid bind method.", 0 );
        }
    }


    /**
     * Creates a new instance of BindMethod.
     *
     * @param value the value
     */
    private BindMethod( String value )
    {
        this.value = value;
    }


    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return value;
    }
}
