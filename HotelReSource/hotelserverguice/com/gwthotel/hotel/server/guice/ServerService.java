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
package com.gwthotel.hotel.server.guice;

import java.util.Date;

import javax.mail.Session;
import javax.persistence.EntityManagerFactory;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.guice.HotelCommonGuice.HotelServiceModule;
import com.gwthotel.hotel.jpa.bill.CustomerBillJpa;
import com.gwthotel.hotel.jpa.clearobjects.ClearObjects;
import com.gwthotel.hotel.jpa.customers.HotelJpaCustomers;
import com.gwthotel.hotel.jpa.hotelmail.HotelMailing;
import com.gwthotel.hotel.jpa.payment.PaymentOp;
import com.gwthotel.hotel.jpa.pricelist.HotelJpaPriceList;
import com.gwthotel.hotel.jpa.prices.HotelJpaPrices;
import com.gwthotel.hotel.jpa.reservation.HotelReservations;
import com.gwthotel.hotel.jpa.reservationop.ReservationOp;
import com.gwthotel.hotel.jpa.rooms.HotelJpaRooms;
import com.gwthotel.hotel.jpa.services.HotelJpaServices;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.server.autompatt.GetAutomPatterns;
import com.gwthotel.hotel.server.provider.EntityManagerFactoryProvider;
import com.gwthotel.hotel.server.service.H;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.resource.GetResourceJNDI;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.jpautil.crudimpl.gensym.JpaObjectGenSymFactoryImpl;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.OObjectAdminInstance;
import com.jython.serversecurity.jpa.OObjectAdminJpa;
import com.jython.ui.server.jpanote.JpaNoteStorage;
import com.jython.ui.server.jpastoragekey.BlobEntryJpaHandler;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransactionContext;
import com.jythonui.server.IConsts;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IGetEnvDefaultData;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.envvar.IGetResourceJNDI;
import com.jythonui.server.envvar.defa.GetEnvDefaultData;
import com.jythonui.server.envvar.impl.GetEnvVariables;
import com.jythonui.server.envvar.impl.ServerPropertiesEnv;
import com.jythonui.server.guavacache.GuavaCacheFactory;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.Mess;
import com.jythonui.server.ressession.ResGetMailSessionProvider;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreSynch;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends HotelServiceModule {
        @Override
        protected void configure() {
            configureHotel();
            bind(IJythonUIServerProperties.class).to(ServerPropertiesEnv.class)
                    .in(Singleton.class);
            bind(ICommonCacheFactory.class).to(GuavaCacheFactory.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(Mess.class).in(Singleton.class);
            bind(IGetResourceJNDI.class).to(GetResourceJNDI.class).in(
                    Singleton.class);
            bind(IHotelServices.class).to(HotelJpaServices.class).in(
                    Singleton.class);
            bind(IHotelPriceList.class).to(HotelJpaPriceList.class).in(
                    Singleton.class);
            bind(IHotelPriceElem.class).to(HotelJpaPrices.class).in(
                    Singleton.class);
            bind(IHotelCustomers.class).to(HotelJpaCustomers.class).in(
                    Singleton.class);
            bind(IHotelRooms.class).to(HotelJpaRooms.class).in(Singleton.class);
            bind(ICustomerBills.class).to(CustomerBillJpa.class).in(
                    Singleton.class);
            bind(IGetAutomPatterns.class).to(GetAutomPatterns.class).in(
                    Singleton.class);

            bind(IReservationForm.class).to(HotelReservations.class).in(
                    Singleton.class);

            bind(IPaymentBillOp.class).to(PaymentOp.class).in(Singleton.class);

            bind(IReservationOp.class).to(ReservationOp.class).in(
                    Singleton.class);

            bind(IClearHotel.class).to(ClearObjects.class).in(Singleton.class);
            bind(IGetEnvVariable.class).to(GetEnvVariables.class).in(
                    Singleton.class);
            bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(
                    Singleton.class);
            bind(IGetEnvDefaultData.class).to(GetEnvDefaultData.class).in(
                    Singleton.class);
            bind(IHotelMailList.class).to(HotelMailing.class).in(
                    Singleton.class);

            // common
            bind(IStorageJpaRegistryFactory.class).to(
                    StorageJpaRegistryFactory.class).in(Singleton.class);
            bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(
                    Singleton.class);
            bind(IOObjectAdmin.class).to(OObjectAdminJpa.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreSynch.class).in(Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            bind(IBlobHandler.class).to(BlobEntryJpaHandler.class).in(
                    Singleton.class);
            bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
                    Singleton.class);
            bind(IJpaObjectGenSymFactory.class).to(
                    JpaObjectGenSymFactoryImpl.class).in(Singleton.class);
            bind(INoteStorage.class).to(JpaNoteStorage.class).in(
                    Singleton.class);
            // common
            bind(Session.class).annotatedWith(Names.named(IConsts.SENDMAIL))
                    .toProvider(ResGetMailSessionProvider.class)
                    .in(Singleton.class);

            requestStatic();
            requestStaticInjection(H.class);
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
        ISetTestToday getSetTestToday() {
            return new ISetTestToday() {

                @Override
                public void setToday(Date p) {

                }

            };
        }

        // -----

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
