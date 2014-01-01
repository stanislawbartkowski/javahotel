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
package com.jythonui.server.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.ui.shared.MUtil;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.Util;
import com.jythonui.shared.ICommonConsts;

public class GetAppProperties implements IGetAppProp {

    private final ICommonCache iCache;

    @Inject
    public GetAppProperties(ICommonCacheFactory cFactory) {
        this.iCache = cFactory.construct(ICommonConsts.APP_FILENAME);
    }

    @Override
    public Map<String, String> get() {
        @SuppressWarnings("unchecked")
        Map<String, String> m = (Map<String, String>) iCache
                .get(ICommonConsts.APP_FILENAME);
        if (m != null)
            return m;
        Properties prop = Util.getProperties(ICommonConsts.APP_FILENAME);
        m = new HashMap<String, String>();
        if (prop != null)
            MUtil.toElem(m, prop);
        iCache.put(ICommonConsts.APP_FILENAME, m);
        return m;
    }

}
