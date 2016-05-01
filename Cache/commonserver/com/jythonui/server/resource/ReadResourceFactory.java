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
package com.jythonui.server.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.jythonui.server.BUtil;
import com.jythonui.server.UtilHelper;

public class ReadResourceFactory extends UtilHelper implements
        IReadResourceFactory {

    private class ClassL implements IReadResource {
        private final ClassLoader cl;

        ClassL(ClassLoader cl) {
            this.cl = cl;
        }

        @Override
        public URL getRes(String resourcePath) {
            return cl.getResource(BUtil.addNameToPath(IReadResource.RESOURCES,
                    resourcePath));
        }

    }

    @Override
    public IReadResource constructLoader(final ClassLoader cl) {
        return new ClassL(cl);
    }

    @Override
    public IReadResource constructDirLoader(final String baseDir) {

        return new IReadResource() {

            @Override
            public URL getRes(String resourcePath) {
                String fName = BUtil.addNameToPath(baseDir,
                        IReadResource.RESOURCES, resourcePath);
                try {
                    URL u = new File(fName).toURI().toURL();
                    return u;
                } catch (MalformedURLException e) {
                    errorLog(fName, e);
                }
                return null;
            }

        };
    }

}
