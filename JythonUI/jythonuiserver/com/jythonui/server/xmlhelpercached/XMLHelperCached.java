/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.xmlhelpercached;

import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.table.map.XMap;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMapFactory;

public class XMLHelperCached implements IXMLHelper {

    private final ICommonCache cCache;
    private final IXMLHelper iXML;

    @Inject
    public XMLHelperCached(ICommonCacheFactory cFactory, IXMLHelper iXML) {
        this.cCache = cFactory.construct(ISharedConsts.JYTHONXMLHELPERCACHE);
        this.iXML = iXML;
    }

    @Override
    public List<? extends XMap> getList(String[] constList, String[] tagList,
            IXMapFactory xFactory) {
        Object o = cCache.get(constList[0]);
        if (o != null)
            return (List<? extends XMap>) o;
        List<? extends XMap> tList = iXML.getList(constList, tagList, xFactory);
        cCache.put(constList[0], tList);
        return tList;
    }

}
