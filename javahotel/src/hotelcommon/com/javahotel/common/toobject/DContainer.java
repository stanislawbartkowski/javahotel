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
package com.javahotel.common.toobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.mapxml.IDataContainer;
import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;

/**
 * @author hotel
 * 
 */
@SuppressWarnings("serial")
public class DContainer implements IDataContainer, Serializable {

    // cannot be final because if not serializable by GWT
    private Map<String, Object> ma = new HashMap<String, Object>();

    public Set<String> getKeys() {
        return ma.keySet();
    }

    public Object get(Object key) {
        Object o = ma.get(key);
        if (o instanceof DateP) {
            DateP d = (DateP) o;
            return d.getD();
        }
        if (o instanceof DecimalP) {
            DecimalP d = (DecimalP) o;
            return d.getDecim();
        }
        return o;
    }

    public Object put(String key, Object val) {
        Object o = ma.get(key);
        if (val instanceof Date) {
            DateP d = null;
            if (o != null) {
                d = (DateP) o;
            }
            if (d == null) {
                d = new DateP();
            }
            d.setD((Date) val);
            return ma.put(key, d);
        }
        if (val instanceof BigDecimal) {
            DecimalP d = null;
            if (o != null) {
                d = (DecimalP) o;
            }
            if (d == null) {
                d = new DecimalP();
            }
            d.setDecim((BigDecimal) val);
            return ma.put(key, d);
        }
        return ma.put(key, val);
    }
}
