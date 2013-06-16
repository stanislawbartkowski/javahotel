/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.registry.object;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.commoncache.ICommonCache;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.registry.IStorageRegistry;

public class ObjectRegistryFactory {

    private final IGetLogMess gMess;

    @Inject
    public ObjectRegistryFactory(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        this.gMess = gMess;
    }

    public ICommonCache construct(IStorageRegistry iRegistry) {
        return new ObjectRegistry(iRegistry, gMess);
    }

}
