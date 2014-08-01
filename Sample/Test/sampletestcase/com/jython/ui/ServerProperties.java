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
package com.jython.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import com.jythonui.server.defa.AbstractServerProperties;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

/**
 * @author hotel
 * 
 */
public class ServerProperties extends AbstractServerProperties {

    @Inject
    public ServerProperties(IReadResourceFactory iFactory) {
        super(iFactory);
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public IReadResource getResource() {
        return iFactory.constructLoader(TestHelper.class.getClassLoader());
    }

    @Override
    public boolean isSerialized() {
        return M.isJythonSerialized();
    }

    @Override
    public URL getSendMailPropertiesFile() {
        IReadResource r = getResource();
        return r.getRes("mail/mailbox.properties");
    }
    
    @Override
    public URL getGetMailPropertiesFile() {
        IReadResource r = getResource();
        return r.getRes("mail/pop3mailbox.properties");
    }

}
