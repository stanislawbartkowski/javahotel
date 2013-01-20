/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.commoncache;


/**
 * @author hotel
 * 
 */
public interface ICommonCache {

    Object get(String key);

    // TODO: remove
    Object getE(String key);

    void put(String key, Object o);

    void remove(String key);

    // TODO: remove
    long inc(String iKey, boolean plus);
    
    
    // TODO: remove
    void clearAll();

}
