/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.MUtil;
import com.jythonui.server.ReadUTF8Properties;
import com.jythonui.server.Util;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.ICommonConsts;

public class GetAppProperties extends UtilHelper implements IGetAppProp {

    private final ICommonCache iCache;
    private final IJythonUIServerProperties iProp;
    private final IGetLogMess gMess;

    @Inject
    public GetAppProperties(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            ICommonCacheFactory cFactory, IJythonUIServerProperties iProp) {
        this.iCache = cFactory.construct(ICommonConsts.APP_FILENAME);
        this.iProp = iProp;
        this.gMess = gMess;
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
        if (iProp.getAppPropertiesFile() != null) {
            try {
                prop = ReadUTF8Properties.readProperties(iProp
                        .getAppPropertiesFile().openStream());
            } catch (IOException e) {
                errorMess(gMess, IErrorCode.ERRORCODE105,
                        ILogMess.ERRORWHILEREADINDADDPROPERTIES, e);
                return null;
            }
            if (prop != null)
                MUtil.toElem(m, prop);
        }
        iCache.put(ICommonConsts.APP_FILENAME, m);
        return m;
    }

}
