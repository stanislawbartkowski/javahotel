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
package com.jythonui.server.dict;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.dict.IDictOfLocalEntries.DictEntry;

public class ReadDict {

    private static final String dictName = ISharedConsts.RESOURCES + "/dict/";

    public static DictEntry[] getList(IGetResourceMap iGet, String resName) {
        List<DictEntry> cList = new ArrayList<DictEntry>();
        URL u = ReadDict.class.getClassLoader().getResource(dictName);
        // Map<String, String> mess = ReadBundle.getBundle(lang, u.getFile(),
        // resName);
        Map<String, String> mess = iGet.getResourceMap(u.getFile(), resName);
        for (Entry e : mess.entrySet()) {
            String key = (String) e.getKey();
            String name = (String) e.getValue();
            DictEntry c = new DictEntry();
            c.setKey(key);
            c.setName(name);
            cList.add(c);
        }
        return (DictEntry[]) cList.toArray(new DictEntry[cList.size()]);
    }
}
