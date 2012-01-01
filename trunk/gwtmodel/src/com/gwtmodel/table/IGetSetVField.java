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
package com.gwtmodel.table;

/**
 * @author hotel Interface for getting and setting IVField objects
 */
public interface IGetSetVField {

    /**
     * Get IVField connected with that setter/getter
     * 
     * @return IVFIeld
     */
    IVField getV();

    /**
     * Getter
     * 
     * @return Object
     */
    Object getValObj();

    /**
     * Setter
     * 
     * @param o
     *            Object to set
     */
    void setValObj(Object o);

}
