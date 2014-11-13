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
package com.jythonui.server.defa;

import java.net.URL;

import javax.inject.Inject;

import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resourcemulti.IReadMultiResource;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;

/**
 * @author hotel
 * 
 */
public class ServerProperties extends AbstractServerProperties {

    private final IsCached isC;

    @Inject
    public ServerProperties(IsCached isC, IReadResourceFactory iFactory,
            IReadMultiResourceFactory mFactory) {
        super(iFactory, mFactory);
        this.isC = isC;
    }

    @Override
    public boolean isCached() {
        if (isC == null)
            return false;
        return isC.isCached();
    }

    @Override
    public IReadMultiResource getResource() {
        return mFactory.construct();
    }

    @Override
    public URL getSendMailPropertiesFile() {
        // IReadResource r = getResource();
        // return r.getRes("gaemail/mailbox.properties");
        return getResource().getFirstUrl("gaemail/mailbox.properties");
    }

}
