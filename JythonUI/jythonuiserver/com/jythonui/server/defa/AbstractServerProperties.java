/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;

abstract public class AbstractServerProperties extends UtilHelper implements
        IJythonUIServerProperties {

    protected final IReadResourceFactory iFactory;
    protected final IReadMultiResourceFactory mFactory;

    @Override
    public String getEJBHost() {
        return null;
    }

    @Override
    public String getEJBPort() {
        return null;
    }

    protected AbstractServerProperties(IReadResourceFactory iFactory,
            IReadMultiResourceFactory mFactory) {
        this.iFactory = iFactory;
        this.mFactory = mFactory;
    }

    @Override
    public boolean isSerialized() {
        return false;
    }

    @Override
    public URL getAppPropertiesFile() {
        return null;
    }

    @Override
    public URL getSendMailPropertiesFile() {
        return null;
    }

    @Override
    public URL getGetMailPropertiesFile() {
        return null;
    }

}
