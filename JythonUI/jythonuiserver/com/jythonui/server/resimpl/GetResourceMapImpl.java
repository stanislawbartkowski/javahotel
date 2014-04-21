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
package com.jythonui.server.resimpl;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

import javax.inject.Inject;

import com.gwtmodel.commoncache.ICommonCache;
import com.jython.ui.shared.resource.IReadResource;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;
import com.jythonui.server.getbundle.ReadBundle;

public class GetResourceMapImpl implements IGetResourceMap {

    private final ICommonCache iCache;
    private final IJythonUIServerProperties iRes;

    @Inject
    public GetResourceMapImpl(ICommonCache iCache,
            IJythonUIServerProperties iRes) {
        this.iCache = iCache;
        this.iRes = iRes;
    }

    @Override
    public Map<String, String> getResourceMap(IReadResource iRead, String dir,
            String bundle) {
        String loc = Util.getLocale();
        String keyCache = "bundle_" + loc + "_" + dir + "_" + bundle;
        Map<String, String> map = null;
        if (iRes.isCached()) {
            map = (Map<String, String>) iCache.get(keyCache);
        }
        if (map == null) {
//            ReadBundle.ILocaleBundle i = ReadBundle.getLocale(loc, dir, bundle);
            map = ReadBundle.getBundle(iRead, loc, dir, bundle);
//            if (cl != null) {
//                map = ReadBundle.getBundle(cl.getResource(i.getDefa()),
//                        i.getLoc() == null ? null : cl.getResource(i.getLoc()));
//            } else {
//                try {
//                    map = ReadBundle.getBundle(new File(i.getDefa()).toURI()
//                            .toURL(),
//                            i.getLoc() == null ? null : new File(i.getLoc())
//                                    .toURI().toURL());
//                } catch (MalformedURLException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
        if (iRes.isCached())
            iCache.put(keyCache, map);
        return map;
    }
}
