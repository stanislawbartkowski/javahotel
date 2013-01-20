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
package com.jython.ui.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.SimpleMapCache;
import com.jython.ui.server.ServerProperties;
import com.jython.ui.server.datastore.IPersonOp;
import com.jythonui.datastore.PersonOp;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.JythonUiServerProvider;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(IJythonUIServer.class)
                    .toProvider(JythonUiServerProvider.class).in(
                            Singleton.class);
            bind(ICommonCache.class).to(SimpleMapCache.class).in(
                    Singleton.class);
        }
    }

}
