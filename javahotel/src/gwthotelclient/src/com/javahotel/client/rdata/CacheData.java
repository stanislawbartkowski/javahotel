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
package com.javahotel.client.rdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;

/**
 * Keeps (as cache) some data
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CacheData {

    /**
     * One piece of data (parameter for keeping)
     * 
     * @author hotel
     * 
     */
    class RetData {

        RetData() {
            col = null;
        }

        /** keyId . */
        String keyId;
        /** CommandParam which was used to read this data. */
        /** (not used now) */
        CommandParam p;
        /** Type of data. */
        RType r;
        /** List of data cached. */
        List<AbstractTo> col;
    }

    /**
     * One piece of data being cached.
     * 
     * @author hotel
     * 
     */
    private class CData {

        /** Type of data. */
        RType r;
        /** CommandParam used fo reading this data. */
        /** For instace: RType but read as different interval. */
        /** Not used now (feature) */
        CommandParam p;
        /** List of data being cached. */
        List<AbstractTo> col;
    }

    /** Cache for data */
    private final Map<String, CData> hList = new HashMap<String, CData>();

    /**
     * Clear cache, remove all data from cache.
     */
    void clear() {
        hList.clear();
    }

    /**
     * Get piece of data from cache
     * 
     * @param r
     *            Type of data
     * @param p
     *            CommandParam used for reading
     * @return Return data (col field can be null, no data in cache)
     */
    RetData getCol(RType r, CommandParam p) {
        RetData re = new RetData();
        re.keyId = CommandUtil.getHash(r, p);
        re.r = r;
        re.p = p;
        CData co = hList.get(re.keyId);
        if (co != null) {
            re.col = co.col;
        }
        return re;
    }

    /**
     * Add data to cache
     * 
     * @param re
     *            RetData to be stored
     */
    void putData(RetData re) {
        CData c = new CData();
        c.col = re.col;
        c.p = re.p;
        c.r = re.r;
        hList.put(re.keyId, c);
    }

    /**
     * Removes (invalidates) all data related to type RType
     * 
     * @param r
     *            RType to be invalidated
     */
    void invalidateCache(final RType r) {
        List<String> de = new ArrayList<String>();
        for (String s : hList.keySet()) {
            CData co = hList.get(s);
            if (co.r == r) {
                de.add(s);
            }
        }
        for (String s : de) {
            hList.remove(s);
        }
    }
}
