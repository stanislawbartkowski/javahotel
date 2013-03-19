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
package com.jythonui.db2.scheduler.injector;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.SimpleMapCache;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.Cached;
import com.jythonui.server.defa.IsCached;
import com.jythonui.server.defa.ServerProperties;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.service.Holder;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends JythonServiceModule {
        @Override
        protected void configure() {
            configureJythonUi();
            bind(boolean.class).annotatedWith(Names.named("CachedNow"))
                    .toInstance(false);
            bind(IsCached.class).to(Cached.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ICommonCache.class).to(SimpleMapCache.class).in(
                    Singleton.class);
            requestStaticInjection(Holder.class);
        }
    }

}
