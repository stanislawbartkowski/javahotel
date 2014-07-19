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
package com.jython.serversecurity.impl;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class GetInstanceObjectId extends UtilHelper implements IGetInstanceOObjectIdCache {

    private final IAppInstanceOObject iApp;
    private final ICommonCache iCache;
    private final IGetLogMess lMess;

    @Inject
    public GetInstanceObjectId(IAppInstanceOObject iApp,
            ICommonCacheFactory cFactory,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
        this.iApp = iApp;
        iCache = cFactory.construct(ISharedConsts.CACHEREALMOBJECTINSTANCE);
        this.lMess = lMess;
    }

    @Override
    public AppInstanceId getInstance(String instanceName, String userName) {
        Object o = null;
        o = iCache.get(instanceName);
        if (o != null)
            return (AppInstanceId) o;
        AppInstanceId a = iApp.getInstanceId(instanceName, userName);
        if (a.getId() == null) {
            String mess = lMess.getMess(IErrorCode.ERRORCODE86,
                    ILogMess.INSTANCEIDCANNOTNENULLHERE);
            errorLog(mess);
        }
        iCache.put(instanceName, a);
        return a;
    }

    @Override
    public OObjectId getOObject(String instanceName, String objectName,
            String userName) {
        String key = instanceName + " " + objectName;
        Object o = null;
        o = iCache.get(key);
        if (o != null)
            return (OObjectId) o;
        AppInstanceId a = getInstance(instanceName,userName);
        OObjectId h = iApp.getOObjectId(a, objectName, userName);
        iCache.put(key, h);
        return h;
    }

    @Override
    public void invalidateCache() {
        iCache.invalidate();
    }

}
