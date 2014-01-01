/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.jythonui.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DialogCheckVariables implements Serializable {

    private static final long serialVersionUID = 1L;
    private ListOfRows lines = new ListOfRows();
    private ListOfRows columns = new ListOfRows();
    private ListOfRows errors = new ListOfRows();

    private Map<String, ListOfRows> val = new HashMap<String, ListOfRows>();

    public ListOfRows getLines() {
        return lines;
    }

    public ListOfRows getColumns() {
        return columns;
    }

    public Map<String, ListOfRows> getVal() {
        return val;
    }

    public ListOfRows getErrors() {
        return errors;
    }
    
}
