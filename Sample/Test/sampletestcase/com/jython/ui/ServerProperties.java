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
package com.jython.ui;

import java.net.URL;

import javax.inject.Inject;

import com.jythonui.server.defa.AbstractServerProperties;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resourcemulti.IReadMultiResource;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;

/**
 * @author hotel
 * 
 */
public class ServerProperties extends AbstractServerProperties {

    @Inject
    public ServerProperties(IReadResourceFactory iFactory,
            IReadMultiResourceFactory mFactory) {
        super(iFactory, mFactory);
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public IReadMultiResource getResource() {
        if (M.getAddPath() == null)
            return mFactory.construct(iFactory.constructLoader(TestHelper.class
                    .getClassLoader()));
        IReadResource res = iFactory.constructDirLoader(M.getAddPath());
        return mFactory.construct(res,
                iFactory.constructLoader(TestHelper.class.getClassLoader()));
    }

    @Override
    public boolean isSerialized() {
        return M.isJythonSerialized();
    }

    @Override
    public URL getSendMailPropertiesFile() {
        return getResource().getFirstUrl("mail/mailbox.properties");
    }

    @Override
    public URL getGetMailPropertiesFile() {
        return getResource().getFirstUrl("mail/imapmailbox.properties");
    }

}
