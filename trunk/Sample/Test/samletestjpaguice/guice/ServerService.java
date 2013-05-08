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
package guice;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.gwtmodel.testenhancer.notgae.TestEnhancer;
import com.jython.ui.ServerProperties;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.jpastoragekey.StorageRegistryFactory;
import com.jythonui.datastore.EntityManagerFactoryProvider;
import com.jythonui.datastore.PersonOp;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
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
            bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ITestEnhancer.class).to(TestEnhancer.class);
            bind(EntityManagerFactory.class)
                    .annotatedWith(
                            Names.named(IConsts.STORAGEREGISTRYENTITYMANAGERFACTORY))
                    .toProvider(EntityManagerFactoryProvider.class)
                    .in(Singleton.class);
            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class)
                    .to(StorageRegistryFactory.class).in(Singleton.class);
            requestStatic();
        }
    }

}