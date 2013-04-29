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
package com.jythonui.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.JythonUiServerProvider;
import com.jythonui.server.defa.CommonCacheProvider;
import com.jythonui.server.defa.GetClientProperties;
import com.jythonui.server.defa.SecurityMemCacheProvider;
import com.jythonui.server.defa.SecurityPersistentStorageProvider;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.MessProvider;
import com.jythonui.server.registry.object.ObjectRegistryFactory;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityResolver;
import com.jythonui.server.security.ISecuritySessionCache;
import com.jythonui.server.security.cache.ISecuritySessionMemCache;
import com.jythonui.server.security.cache.ISecuritySessionPersistent;
import com.jythonui.server.security.cache.impl.SecuritySessionStore;
import com.jythonui.server.security.impl.SecurityJython;
import com.jythonui.server.security.resolver.SecurityResolver;

/**
 * @author hotel
 * 
 */
public class JythonServerService {

    public abstract static class JythonServiceModule extends AbstractModule {

        protected void configureJythonUi() {
            bind(IJythonUIServer.class)
                    .toProvider(JythonUiServerProvider.class).in(
                            Singleton.class);
            bind(ISecurity.class).to(SecurityJython.class).in(Singleton.class);
            bind(ISecuritySessionCache.class).to(SecuritySessionStore.class)
                    .in(Singleton.class);
            bind(ISecurityResolver.class).to(SecurityResolver.class).in(
                    Singleton.class);
            bind(ObjectRegistryFactory.class).in(Singleton.class);
            bind(ISecuritySessionPersistent.class).toProvider(
                    SecurityPersistentStorageProvider.class)
                    .in(Singleton.class);
            bind(ICommonCache.class).toProvider(CommonCacheProvider.class).in(
                    Singleton.class);
            bind(ISecuritySessionMemCache.class).toProvider(
                    SecurityMemCacheProvider.class).in(Singleton.class);
            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(IConsts.JYTHONMESSSERVER))
                    .toProvider(MessProvider.class).in(Singleton.class);
            bind(IJythonClientRes.class).to(GetClientProperties.class).in(Singleton.class);
        }
        
        protected void requestStatic() {
            requestStaticInjection(Holder.class);
        }
    }

}
