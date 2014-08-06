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
package com.jython.ui.server.guice;

import javax.mail.Session;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwtmodel.commoncache.CommonCacheFactory;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.ui.server.Cached;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.datastore.gae.DateLineOp;
import com.jython.ui.server.datastore.gae.DateRecordOp;
import com.jython.ui.server.datastore.gae.PersonOp;
import com.jython.ui.server.gae.security.impl.ObjectAdminGae;
import com.jython.ui.server.gae.security.impl.ObjectInstanceImpl;
import com.jython.ui.server.gaestoragekey.BlobStorage;
import com.jython.ui.server.gaestoragekey.GaeStorageRegistry;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.GetClientProperties;
import com.jythonui.server.defa.IsCached;
import com.jythonui.server.defa.JavaMailSessionProvider;
import com.jythonui.server.defa.ServerProperties;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

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
            bind(IDateLineOp.class).to(DateLineOp.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(
                    GaeSampleAppServerProperties.class).in(Singleton.class);
            bind(IJythonClientRes.class).to(GetClientProperties.class).in(
                    Singleton.class);
            bind(ICommonCacheFactory.class).to(CommonCacheFactory.class).in(
                    Singleton.class);
            bind(IDateRecordOp.class).to(DateRecordOp.class)
                    .in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(IBlobHandler.class).to(BlobStorage.class).in(Singleton.class);
            bind(IStorageRealmRegistry.class).to(GaeStorageRegistry.class).in(
                    Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            bind(IAppInstanceOObject.class).to(ObjectInstanceImpl.class).in(
                    Singleton.class);
            bind(IOObjectAdmin.class).to(ObjectAdminGae.class).in(
                    Singleton.class);
            bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
                    Singleton.class);
            bind(Session.class).annotatedWith(Names.named(IConsts.SENDMAIL))
                    .toProvider(JavaMailSessionProvider.class)
                    .in(Singleton.class);
            requestStatic();
        }

        @Provides
        @Named(IConsts.GETMAIL)
        @Singleton
        Session getGetSession() {
            return null;
        }

        // @Provides
        // @Named(IConsts.SENDMAIL)
        // @Singleton
        // Session getSendSession() {
        // return null;
        // }

    }

}
