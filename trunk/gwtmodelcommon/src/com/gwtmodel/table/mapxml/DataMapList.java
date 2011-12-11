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
package com.gwtmodel.table.mapxml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hotel
 *
 */
public class DataMapList {
    
    @SuppressWarnings("serial")
    public static class DContainer extends HashMap<String, Object> {        
    }
    
    private DContainer dFields;
    private List<DContainer> dLines;
    
    public DataMapList() {
        dFields = new DContainer();
        dLines = new ArrayList<DContainer>();        
    }

    /**
     * @return the dFields
     */
    public DContainer getdFields() {
        return dFields;
    }

    /**
     * @return the dLines
     */
    public List<DContainer> getdLines() {
        return dLines;
    }
    

}
