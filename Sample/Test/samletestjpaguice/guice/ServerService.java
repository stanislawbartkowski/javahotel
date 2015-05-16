/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import javax.mail.Session;
import javax.persistence.EntityManagerFactory;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.table.common.dateutil.SetTestTodayProvider;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.gwtmodel.testenhancer.notgae.TestEnhancer;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.jpautil.crudimpl.gensym.JpaObjectGenSymFactoryImpl;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.OObjectAdminInstance;
import com.jython.serversecurity.jpa.OObjectAdminJpa;
import com.jython.ui.ServerProperties;
import com.jython.ui.TestHelper;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.jpanote.JpaNoteStorage;
import com.jython.ui.server.jpastoragekey.BlobEntryJpaHandler;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransactionContext;
import com.jythonui.datastore.DateLineOp;
import com.jythonui.datastore.DateRecordOp;
import com.jythonui.datastore.EntityManagerFactoryProvider;
import com.jythonui.datastore.PersonOp;
import com.jythonui.server.IConsts;
import com.jythonui.server.IConvertJythonTimestamp;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IGetEnvDefaultData;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyGetEnvDefaultData;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.JavaGetMailSessionProvider;
import com.jythonui.server.defa.JavaMailSessionProvider;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.guavacache.GuavaCacheFactory;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.jython.ConvertPython27;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.objectgensymimpl.CrudObjectGenSym;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreSynch;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGenerator;
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
			bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
			bind(IDateLineOp.class).to(DateLineOp.class).in(Singleton.class);
			bind(IDateRecordOp.class).to(DateRecordOp.class)
					.in(Singleton.class);
			bind(IJythonUIServerProperties.class).to(ServerProperties.class)
					.in(Singleton.class);
			bind(ITestEnhancer.class).to(TestEnhancer.class);
			bind(EntityManagerFactory.class).toProvider(
					EntityManagerFactoryProvider.class).in(Singleton.class);
			bind(ICommonCacheFactory.class).to(GuavaCacheFactory.class).in(
					Singleton.class);

			// common
			bind(IStorageJpaRegistryFactory.class).to(
					StorageJpaRegistryFactory.class).in(Singleton.class);
			bind(ISemaphore.class).to(SemaphoreSynch.class).in(Singleton.class);
			bind(IGetConnection.class)
					.toProvider(EmptyConnectionProvider.class).in(
							Singleton.class);
			bind(IBlobHandler.class).to(BlobEntryJpaHandler.class).in(
					Singleton.class);
			bind(ISetTestToday.class).toProvider(SetTestTodayProvider.class)
					.in(Singleton.class);
			bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
					Singleton.class);
			bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(
					Singleton.class);
			bind(IOObjectAdmin.class).to(OObjectAdminJpa.class).in(
					Singleton.class);
			bind(Session.class).annotatedWith(Names.named(IConsts.SENDMAIL))
					.toProvider(JavaMailSessionProvider.class)
					.in(Singleton.class);
			bind(Session.class).annotatedWith(Names.named(IConsts.GETMAIL))
					.toProvider(JavaGetMailSessionProvider.class)
					.in(Singleton.class);
			bind(IJpaObjectGenSymFactory.class).to(
					JpaObjectGenSymFactoryImpl.class).in(Singleton.class);
			bind(INoteStorage.class).to(JpaNoteStorage.class).in(
					Singleton.class);
			bind(IGetEnvDefaultData.class).to(EmptyGetEnvDefaultData.class).in(
					Singleton.class);
			bind(IConvertJythonTimestamp.class).to(ConvertPython27.class).in(
					Singleton.class);
			requestStatic();
			requestStaticInjection(TestHelper.class);

		}

		// common
		@Provides
		@Singleton
		IStorageRealmRegistry getStorageRealmRegistry(
				IStorageJpaRegistryFactory rFactory,
				ITransactionContextFactory iC) {
			return rFactory.construct(iC);
		}

		@Provides
		@Singleton
		ITransactionContextFactory getTransactionContextFactory(
				final EntityManagerFactory eFactory) {
			return new ITransactionContextFactory() {
				@Override
				public ITransactionContext construct() {
					return new JpaTransactionContext(eFactory);
				}
			};
		}

		@Provides
		@Singleton
		ICrudObjectGenSym getCrudObjectGenSym(ISymGenerator iGen,
				@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
			return new CrudObjectGenSym(iGen, lMess);
		}

		// -----

	}

}
