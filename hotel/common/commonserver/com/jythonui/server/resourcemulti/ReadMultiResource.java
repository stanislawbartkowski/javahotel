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
package com.jythonui.server.resourcemulti;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.jythonui.server.UtilHelper;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

class ReadMultiResource extends UtilHelper implements IReadMultiResource {

    private final List<IReadResource> rList = new ArrayList<IReadResource>();

    ReadMultiResource(IReadResourceFactory iFactory, IReadResource... li) {
        for (IReadResource i : li)
            rList.add(i);
        rList.add(iFactory.constructLoader(ReadMultiResource.class
                .getClassLoader()));
    }

    @Override
    public List<IReadResource> getRList() {
        return rList;
    }

    @Override
    public List<URL> getUrlList(String resourcePath) {
        List<URL> uList = new ArrayList<URL>();
        for (IReadResource i : getRList()) {
            URL u = i.getRes(resourcePath);
            if (u != null)
                uList.add(u);
        }
        return uList;
    }

    @Override
    public URL getFirstUrl(String resourcePath) {
        for (URL u : getUrlList(resourcePath)) {
            InputStream i;
            try {
                i = u.openStream();
                i.close();
                return u;
            } catch (FileNotFoundException e) {
                // expected
                continue;
            } catch (IOException e) {
                errorLog(u.getFile(), e);
            }
        }
        return null;
    }
}
