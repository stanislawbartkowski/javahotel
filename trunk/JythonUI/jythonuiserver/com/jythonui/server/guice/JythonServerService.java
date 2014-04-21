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
package com.jythonui.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwtmodel.commoncache.ICommonCache;
import com.jython.ui.shared.ISharedConsts;
import com.jython.ui.shared.resource.IReadResourceFactory;
import com.jython.ui.shared.resource.ReadResourceFactory;
import com.jythonui.server.IConsts;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.IStorageMemContainerFactory;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.Util;
import com.jythonui.server.defa.CommonCacheProvider;
import com.jythonui.server.defa.GetClientProperties;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.defadata.DefaDataImpl;
import com.jythonui.server.dict.IDictOfLocalEntries;
import com.jythonui.server.dict.IDictOfLocalEntries.DictEntry;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.dict.ListOfCountries;
import com.jythonui.server.dict.ReadDict;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.impl.GetAppProperties;
import com.jythonui.server.impl.JythonUiServerProvider;
import com.jythonui.server.logmess.MessProvider;
import com.jythonui.server.memstorage.MemStorageCacheFactory;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.newblob.impl.AddNewBlob;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.registry.object.ObjectRegistryFactory;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.server.resbundle.Mess;
import com.jythonui.server.resimpl.GetResourceMapImpl;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityResolver;
import com.jythonui.server.security.impl.SecurityJython;
import com.jythonui.server.security.resolver.SecurityResolver;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.gensym.ISymGeneratorFactory;
import com.jythonui.server.storage.gensymimpl.SymGeneratorFactory;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.storage.seq.ISequenceRealmGenFactory;
import com.jythonui.server.storage.seqimpl.SequenceRealmGenFactory;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.server.xml.XMLTransformer;
import com.jythonui.server.xmlmap.XMLMap;

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
            bind(ISecurityResolver.class).to(SecurityResolver.class).in(
                    Singleton.class);
            bind(ObjectRegistryFactory.class).in(Singleton.class);
            bind(ICommonCache.class).toProvider(CommonCacheProvider.class).in(
                    Singleton.class);
            bind(IAppMess.class).annotatedWith(Names.named(IConsts.APPMESS))
                    .to(Mess.class).in(Singleton.class);
            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(ISharedConsts.JYTHONMESSSERVER))
                    .toProvider(MessProvider.class).in(Singleton.class);
            bind(IJythonClientRes.class).to(GetClientProperties.class).in(
                    Singleton.class);
            bind(ISequenceRealmGenFactory.class).to(
                    SequenceRealmGenFactory.class).in(Singleton.class);
            bind(IGetResourceMap.class).to(GetResourceMapImpl.class).in(
                    Singleton.class);

            bind(ISymGeneratorFactory.class).to(SymGeneratorFactory.class).in(
                    Singleton.class);
            bind(IXMLTransformer.class).to(XMLTransformer.class).in(
                    Singleton.class);
            bind(IXMLToMap.class).to(XMLMap.class).in(Singleton.class);
            bind(IDictOfLocalEntries.class)
                    .annotatedWith(Names.named(IConsts.COUNTRIESDICT))
                    .to(ListOfCountries.class).in(Singleton.class);
            bind(IStorageMemContainerFactory.class).to(
                    MemStorageCacheFactory.class).in(Singleton.class);
            bind(IDefaultData.class).to(DefaDataImpl.class).in(Singleton.class);
            bind(IGetAppProp.class).to(GetAppProperties.class).in(
                    Singleton.class);
            bind(IReadResourceFactory.class).to(ReadResourceFactory.class).in(
                    Singleton.class);
            // common
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IAddNewBlob.class).to(AddNewBlob.class).in(Singleton.class);
        }

        @Provides
        @Singleton
        ISequenceRealmGen getSequenceRealmGen(
                ISequenceRealmGenFactory seqFactory,
                IStorageRealmRegistry iReg, ISemaphore iSem) {
            return seqFactory.construct(iReg, iSem);
        }

        @Provides
        @Singleton
        ISymGenerator getSymGenerator(ISymGeneratorFactory sFactory,
                ISequenceRealmGen iSeq) {
            return sFactory.construct(iSeq);
        }

        protected void requestStatic() {
            requestStaticInjection(Holder.class);
            requestStaticInjection(SHolder.class);
        }

        @Provides
        @Named(IConsts.COUNTRIESDICT)
        @Singleton
        IGetLocalizedDict getListOfCountries(
                @Named(IConsts.COUNTRIESDICT) final IDictOfLocalEntries iList) {
            return new IGetLocalizedDict() {

                @Override
                public DictEntry[] getList() {
                    return iList.getList(Util.getLocale());
                }

            };
        }

        @Provides
        @Named(IConsts.TITLESDICT)
        @Singleton
        IGetLocalizedDict getListOfTitles(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public DictEntry[] getList() {
                    return ReadDict.getList(iGet, IConsts.TITLESDICT);
                }

            };
        }

        @Provides
        @Named(IConsts.IDTYPEDICT)
        @Singleton
        IGetLocalizedDict getListOfIdTypes(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public DictEntry[] getList() {
                    return ReadDict.getList(iGet, IConsts.IDTYPEDICT);
                }

            };
        }

        @Provides
        @Named(IConsts.PAYMENTDICT)
        @Singleton
        IGetLocalizedDict getListOfPayment(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public DictEntry[] getList() {
                    return ReadDict.getList(iGet, IConsts.PAYMENTDICT);
                }

            };
        }

        @Provides
        @Named(IConsts.SECURITYREALM)
        @Singleton
        IStorageMemCache getSecurityCache(
                final IStorageMemContainerFactory cFactory) {
            return cFactory.construct(IConsts.SECURITYREALM);
        }

        @Provides
        @Named(IConsts.DEFADATAREALM)
        @Singleton
        IStorageMemCache getDefaDataCache(
                final IStorageMemContainerFactory cFactory) {
            return cFactory.construct(IConsts.DEFADATAREALM);
        }

    }

}
