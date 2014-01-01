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
package com.jythonui.server.memstorage;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.IConsts;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.IStorageMemContainerFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.registry.object.ObjectRegistryFactory;

public class MemStorageCacheFactory implements IStorageMemContainerFactory {

    private final ICommonCacheFactory cFactory;
    private final IStorageRegistryFactory iStorage;
    private final IGetLogMess gMess;
    private final ObjectRegistryFactory oProvider;

    @Inject
    public MemStorageCacheFactory(ICommonCacheFactory cFactory,
            IStorageRegistryFactory iStorage,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            ObjectRegistryFactory oProvider) {
        this.cFactory = cFactory;
        this.iStorage = iStorage;
        this.gMess = gMess;
        this.oProvider = oProvider;
    }

    @Override
    public IStorageMemCache construct(String realm) {
        ICommonCache cCache = cFactory.construct(IConsts.SECURITYREALM);
        IStorageRegistry sRegistry = iStorage.construct(IConsts.SECURITYREALM);
        ICommonCache rCache = oProvider.construct(sRegistry);
        return new StorageCache(cCache, rCache, gMess);

    }

}
