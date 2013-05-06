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

import com.google.inject.Singleton;
import com.gwtmodel.commoncache.CommonCacheFactory;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.ui.server.Cached;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.datastore.gae.PersonOp;
import com.jython.ui.server.gaestoragekey.StorageRegistryFactory;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.GetClientProperties;
import com.jythonui.server.defa.IsCached;
import com.jythonui.server.defa.ServerProperties;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.registry.IStorageRegistryFactory;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends JythonServiceModule {

        @Override
        protected void configure() {
            configureJythonUi();
            bind(IsCached.class).to(Cached.class).in(Singleton.class);
            bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(IJythonClientRes.class).to(GetClientProperties.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class)
                    .to(StorageRegistryFactory.class).in(Singleton.class);
            bind(ICommonCacheFactory.class).to(CommonCacheFactory.class).in(
                    Singleton.class);
            requestStatic();
        }
    }

}
