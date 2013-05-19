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
package com.gwthotel.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

abstract public class PropDescription implements Serializable {

    // important: not final !
    private Map<String, String> attr = new HashMap<String, String>();

    public Map<String, String> getMap() {
        return attr;
    }

    public void setAttr(String key, String value) {
        attr.put(key, value);
    }

    public String getAttr(String key) {
        return attr.get(key);
    }

    public String getId() {
        return getAttr(IHotelConsts.ID);
    }

    public String getDescription() {
        return getAttr(IHotelConsts.DESCRIPTION);
    }

    public String getName() {
        return getAttr(IHotelConsts.NAME);
    }

    public void setName(String name) {
        setAttr(IHotelConsts.NAME, name);
    }

    public void setDescription(String de) {
        setAttr(IHotelConsts.DESCRIPTION, de);
    }
}
