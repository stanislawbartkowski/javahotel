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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.dict.IDictOfLocalEntries.DictEntry;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.ReadResourceFactory;

public class ReadDict {

    private static final String dictName = "dict";
    private static IReadResource iRead = new ReadResourceFactory()
            .constructLoader(ReadDict.class.getClassLoader());

    public static DictEntry[] getList(IGetResourceMap iGet, String resName) {
        List<DictEntry> cList = new ArrayList<DictEntry>();
        Map<String, String> mess = iGet
                .getResourceMap(iRead, dictName, resName);
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
