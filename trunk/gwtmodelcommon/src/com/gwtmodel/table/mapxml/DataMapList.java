/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.mapxml;

import java.io.Serializable;
import java.util.List;

/**
 * @author hotel Template class for keeping IDataContainer data.
 */
@SuppressWarnings("serial")
abstract public class DataMapList<T extends IDataContainer> implements
        Serializable {

    /** Main body of data. */
    private T dFields;
    /** List of lines in shape of IDataContainer. */
    private List<T> dLines;

    public DataMapList(T dFields, List<T> dLines) {
        this.dFields = dFields;
        this.dLines = dLines;
    }

    /**
     * @return the dFields
     */
    public T getdFields() {
        return dFields;
    }

    /**
     * @return the dLines
     */
    public List<T> getdLines() {
        return dLines;
    }

    /**
     * Adds next line to the dLines and returns IDataContainer just added
     * 
     * @return IDataContainer added
     */
    abstract public T addToLines();

}
