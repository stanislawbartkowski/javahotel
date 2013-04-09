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
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.JythonUiServerProvider;
import com.jythonui.server.defa.SecurityCache;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityResolver;
import com.jythonui.server.security.ISessionCache;
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
            bind(ISessionCache.class).to(SecurityCache.class).in(Singleton.class);
            bind(ISecurityResolver.class).to(SecurityResolver.class).in(Singleton.class);
        }
    }

}
