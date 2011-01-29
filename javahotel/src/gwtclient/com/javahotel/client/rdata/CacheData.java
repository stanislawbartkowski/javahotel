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
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CacheData {

    class RetData {

        RetData() {
            col = null;
        }

        String keyId;
        CommandParam p;
        RType r;
        List<AbstractTo> col;
    }

    private class CData {

        RType r;
        CommandParam p;
        List<AbstractTo> col;
    }

    private final Map<String, CData> hList = new HashMap<String, CData>();

    void clear() {
        hList.clear();
    }

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

    void putData(RetData re) {
        CData c = new CData();
        c.col = re.col;
        c.p = re.p;
        c.r = re.r;
        hList.put(re.keyId, c);
    }

    void invalidateCache(final RType r) {
        List<String> de = new ArrayList<String>();
        for (final String s : hList.keySet()) {
            CData co = hList.get(s);
            if (co.r == r) {
                de.add(s);
            }
        }
        for (final String s : de) {
            hList.remove(s);
        }
    }
}
