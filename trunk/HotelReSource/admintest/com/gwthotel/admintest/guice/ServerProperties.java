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
package com.gwthotel.admintest.guice;

import javax.inject.Inject;

import com.jython.ui.shared.resource.IReadResource;
import com.jython.ui.shared.resource.IReadResourceFactory;
import com.jythonui.server.defa.AbstractServerProperties;

public class ServerProperties extends AbstractServerProperties {

    @Inject
    public ServerProperties(IReadResourceFactory iFactory) {
        super(iFactory);
    }

    @Override
    public String getEJBHost() {
        return "think";
    }

    @Override
    public IReadResource getResource() {
        return iFactory.constructLoader(ServerProperties.class.getClassLoader());
    }


    @Override
    public boolean isCached() {
        return false;
    }
}
