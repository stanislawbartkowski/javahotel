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
package com.javahotel.db.jtypes;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.javahotel.types.LId;

public class ToLD {

    public static LId toLId(HId id) {
        Key k = id.getId();
        Long l = id.getL();
        if (k == null) {
            return new LId(l);
        }
        return new LId(l, KeyFactory.keyToString(k));
    }

    public static boolean eq(LId id1, HId id2) {
        if (id1 == null) {
            return id2 == null;
        }
        if (id2 == null) {
            return false;
        }
        if (id2.getL() == null) {
            return false;
        }
        return id1.getId().equals(id2.getL());
    }

}