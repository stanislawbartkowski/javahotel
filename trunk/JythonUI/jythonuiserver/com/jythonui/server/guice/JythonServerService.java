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

import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.impl.GetInstanceObjectId;
import com.jython.serversecurity.persons.SecurityForPersons;
import com.jythonui.server.IConsts;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IExecuteJython;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.IGetDialog;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.IStorageMemContainerFactory;
import com.jythonui.server.IUserCacheHandler;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.defa.GetClientProperties;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.defadata.DefaDataImpl;
import com.jythonui.server.dialog.GetDialog;
import com.jythonui.server.dict.DictEntry;
import com.jythonui.server.dict.GetVatTaxes;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.dict.ListOfCountries;
import com.jythonui.server.dict.ReadDict;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.impl.GetAppProperties;
import com.jythonui.server.impl.JythonUIServer;
import com.jythonui.server.jython.RunJython;
import com.jythonui.server.jython.DecoratorRunJython;
import com.jythonui.server.logmess.MessProvider;
import com.jythonui.server.memstorage.MemStorageCacheFactory;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.newblob.impl.AddNewBlob;
import com.jythonui.server.objectauth.PersonSecurityConverter;
import com.jythonui.server.objectauth.SecurityConverter;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.registry.object.ObjectRegistryFactory;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.server.resbundle.Mess;
import com.jythonui.server.resimpl.GetResourceMapImpl;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resource.ReadResourceFactory;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityConvert;
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
import com.jythonui.server.usercacheimpl.UserCacheHandler;
import com.jythonui.server.xml.IXMLHelper;
import com.jythonui.server.xml.IXMLToXMap;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.server.xml.XMLTransformer;
import com.jythonui.server.xmlhelper.XMLHelper;
import com.jythonui.server.xmlhelpercached.XMLHelperCached;
import com.jythonui.server.xmlmap.XMLMap;
import com.jythonui.server.xmltoxmap.XMLToXMap;

/**
 * @author hotel
 * 
 */
public class JythonServerService {

    public abstract static class JythonServiceModule extends AbstractModule {

        protected void configureJythonUi() {
            bind(IJythonUIServer.class).to(JythonUIServer.class).in(
                    Singleton.class);
            bind(IExecuteJython.class)
                    .annotatedWith(Names.named(IConsts.GENERICRUNJYTHON))
                    .to(RunJython.class).in(Singleton.class);
            bind(IExecuteJython.class)
                    .to(DecoratorRunJython.class).in(Singleton.class);
            bind(ISecurity.class).to(SecurityJython.class).in(Singleton.class);
            bind(ISecurityResolver.class).to(SecurityResolver.class).in(
                    Singleton.class);
            bind(ObjectRegistryFactory.class).in(Singleton.class);
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
            bind(IUserCacheHandler.class).to(UserCacheHandler.class).in(
                    Singleton.class);

            bind(ISymGeneratorFactory.class).to(SymGeneratorFactory.class).in(
                    Singleton.class);
            bind(IXMLTransformer.class).to(XMLTransformer.class).in(
                    Singleton.class);
            bind(IXMLToMap.class).to(XMLMap.class).in(Singleton.class);
            bind(IXMLHelper.class).to(XMLHelper.class).in(Singleton.class);
            bind(IXMLHelper.class)
                    .annotatedWith(Names.named(ISharedConsts.XMLHELPERCACHED))
                    .to(XMLHelperCached.class).in(Singleton.class);
            bind(IGetLocalizedDict.class)
                    .annotatedWith(Names.named(IConsts.COUNTRIESDICT))
                    .to(ListOfCountries.class).in(Singleton.class);
            bind(IOObjectAdmin.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.PERSONSONLYSECURITY))
                    .to(SecurityForPersons.class).in(Singleton.class);
            bind(IGetLocalizedDict.class)
                    .annotatedWith(Names.named(IConsts.VATDICT))
                    .to(GetVatTaxes.class).in(Singleton.class);
            bind(IStorageMemContainerFactory.class).to(
                    MemStorageCacheFactory.class).in(Singleton.class);
            bind(IDefaultData.class).to(DefaDataImpl.class).in(Singleton.class);
            bind(IGetAppProp.class).to(GetAppProperties.class).in(
                    Singleton.class);
            bind(IReadResourceFactory.class).to(ReadResourceFactory.class).in(
                    Singleton.class);
            bind(IGetDialog.class).to(GetDialog.class).in(Singleton.class);
            bind(IXMLToXMap.class).to(XMLToXMap.class).in(Singleton.class);
            // common
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IAddNewBlob.class).to(AddNewBlob.class).in(Singleton.class);
            bind(IGetInstanceOObjectIdCache.class)
                    .to(GetInstanceObjectId.class).in(Singleton.class);
            bind(ISecurityConvert.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.PERSONSONLYSECURITY))
                    .to(PersonSecurityConverter.class).in(Singleton.class);
            bind(ISecurityConvert.class).to(SecurityConverter.class).in(
                    Singleton.class);
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
        @Named(IConsts.TITLESDICT)
        @Singleton
        IGetLocalizedDict getListOfTitles(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public List<DictEntry> getList() {
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
                public List<DictEntry> getList() {
                    return ReadDict.getList(iGet, IConsts.IDTYPEDICT);
                }

            };
        }

        @Provides
        @Named(IConsts.ROLES)
        @Singleton
        IGetLocalizedDict getListOfDefaultRoles(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public List<DictEntry> getList() {
                    return ReadDict.getList(iGet, IConsts.ROLES);
                }
            };
        }

        @Provides
        @Named(IConsts.PAYMENTDICT)
        @Singleton
        IGetLocalizedDict getListOfPayment(final IGetResourceMap iGet) {
            return new IGetLocalizedDict() {

                @Override
                public List<DictEntry> getList() {
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

        @Provides
        @Singleton
        ICommonCache getCommonCache(ICommonCacheFactory cFactory) {
            return cFactory.construct(IConsts.COMMONCACHENAME);

        }

    }

}
