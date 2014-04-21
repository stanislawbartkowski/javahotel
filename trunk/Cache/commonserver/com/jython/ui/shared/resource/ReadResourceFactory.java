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
package com.jython.ui.shared.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.jython.ui.shared.BUtil;
import com.jython.ui.shared.UtilHelper;

public class ReadResourceFactory extends UtilHelper implements
        IReadResourceFactory {

    public IReadResource constructLoader(final ClassLoader cl) {
        return new IReadResource() {

            @Override
            public URL getRes(String resourcePath) {
                return cl.getResource(BUtil.addNameToPath(
                        IReadResource.RESOURCES, resourcePath));
            }

        };
    }

    public IReadResource constructDir(final String baseDir) {
        return new IReadResource() {

            @Override
            public URL getRes(String resourcePath) {
                String fName = BUtil.addNameToPath(baseDir,
                        IReadResource.RESOURCES, resourcePath);
                try {
                    return new File(fName).toURI().toURL();
                } catch (MalformedURLException e) {
                    errorLog(fName, e);
                    return null;
                }
            }
        };
    }

}
