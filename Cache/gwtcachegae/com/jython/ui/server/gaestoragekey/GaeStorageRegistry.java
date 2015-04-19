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
package com.jython.ui.server.gaestoragekey;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class GaeStorageRegistry extends AbstractStorageRegistry {

    static {
        ObjectifyService.register(RegistryEntry.class);
    }

    @Inject
    public GaeStorageRegistry(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        super(gMess,RegistryEntry.class);
    }

    @Override
    AbstractRegistryEntry construct() {
        return new RegistryEntry();
    }

}
