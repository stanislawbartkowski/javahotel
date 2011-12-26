/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwtmodel.mapxml;

/**
 * @author hotel Factory interface providing user specific data
 */
public interface IXMLTypeFactory {

    /** Common types. */
    String DATE = "date";
    String DECIMAL = "decimal";
    String INT = "int";
    String INTEGER = "integer";
    String LONG = "long";

    /** Name of type attribute. */
    String TYPE = "type";

    /** Returns name of 'lines' element. */
    String getLinesTag();

    /** Returns name of single line in 'lines' section. */
    String getLineTag();

    /**
     * Creates object from string (element text). Object will be inserted into
     * IDataContainer map.
     * 
     * @param xType
     *            Type string (or null). Values is taken from 'type' attribute
     * @param s
     *            String value
     * @return Object
     */
    Object contruct(String xType, String s);

    /**
     * Opposite to construct. Transforms object to string
     * 
     * @param xType
     *            Type string (or null).
     * @param o
     *            Object taken from IDataContainer map
     * @return String to be inserted into XML string
     */
    String toS(String xType, Object o);

}
