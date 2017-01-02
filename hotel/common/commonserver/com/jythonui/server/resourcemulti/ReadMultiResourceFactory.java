/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.server.resourcemulti;

import com.google.inject.Inject;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

public class ReadMultiResourceFactory extends UtilHelper implements
        IReadMultiResourceFactory {

    private final IReadResourceFactory iFactory;

    @Inject
    public ReadMultiResourceFactory(IReadResourceFactory iFactory) {
        this.iFactory = iFactory;
    }

    @Override
    public IReadMultiResource construct(IReadResource... li) {
        return new ReadMultiResource(iFactory, li);
    }

    @Override
    public IReadMultiResource construct(String listDir) {
        String[] b = listDir.split(",");
        IReadResource[] r = new IReadResource[b.length];
        for (int i = 0; i < b.length; i++)
            r[i] = iFactory.constructDirLoader(b[i]);
        return new ReadMultiResource(iFactory, r);

    }

}
