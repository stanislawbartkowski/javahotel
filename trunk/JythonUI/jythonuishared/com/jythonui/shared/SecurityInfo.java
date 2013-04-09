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
package com.jythonui.shared;

import java.util.HashMap;
import java.util.Map;

public class SecurityInfo extends DialSecurityInfo {

    private Map<String, DialSecurityInfo> lSecur = new HashMap<String, DialSecurityInfo>();
    private Map<String, DialSecurityInfo> listSecur = new HashMap<String, DialSecurityInfo>();

    public Map<String, DialSecurityInfo> getlSecur() {
        return lSecur;
    }
    
    public Map<String, DialSecurityInfo> getListSecur() {
        return listSecur;
    }

    // default
    public SecurityInfo() {
    }

    public SecurityInfo(DialSecurityInfo dInfo) {
        this.buttSec = dInfo.buttSec;
        this.fieldSec = dInfo.fieldSec;
    }

}
