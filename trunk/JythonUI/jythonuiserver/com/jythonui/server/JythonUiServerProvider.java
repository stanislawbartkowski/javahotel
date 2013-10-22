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
package com.jythonui.server;

import javax.inject.Inject;
import javax.inject.Provider;

import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.security.ISecurity;

/**
 * @author hotel
 * 
 */
public class JythonUiServerProvider implements Provider<IJythonUIServer> {

    private final IJythonUIServerProperties i;
    private final ICommonCache mCache;
    private final ISecurity iSec;

    @Inject
    public JythonUiServerProvider(IJythonUIServerProperties i,
            ICommonCache mCache, ISecurity iSec) {
        this.i = i;
        this.mCache = mCache;
        this.iSec = iSec;
    }

    @Override
    public IJythonUIServer get() {
        return new JythonUIServer(i, mCache, iSec);
    }

}
