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
package com.gwthotel.adminejb.guice;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
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
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.mess.HotelMessProvider;
import com.gwthotel.shared.IHotelConsts;
import com.jython.dateutil.ISetTestToday;
import com.jython.dateutil.SetTestTodayProvider;
import com.jython.jpautil.crudimpl.gensym.IJpaObjectGenSymFactory;
import com.jython.jpautil.crudimpl.gensym.JpaObjectGenSymFactoryImpl;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.OObjectAdminInstance;
import com.jython.serversecurity.jpa.OObjectAdminJpa;
import com.jython.ui.server.jpajournal.JpaJournal;
import com.jython.ui.server.jpanote.JpaNoteStorage;
import com.jython.ui.server.jpastoragekey.BlobEntryJpaHandler;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaNonTransactionContext;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.journal.IJournal;
import com.jythonui.server.logmess.MessProvider;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.newblob.impl.AddNewBlob;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resource.ReadResourceFactory;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGeneratorFactory;
import com.jythonui.server.storage.gensymimpl.SymGeneratorFactory;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.storage.seq.ISequenceRealmGenFactory;
import com.jythonui.server.storage.seqimpl.SequenceRealmGenFactory;

public class GuiceService {

	public static class ServiceModule extends AbstractModule {
		@Override
		protected void configure() {

			bind(IHotelServices.class).to(HotelJpaServices.class).in(Singleton.class);

			bind(IHotelPriceList.class).to(HotelJpaPriceList.class).in(Singleton.class);

			bind(IHotelPriceElem.class).to(HotelJpaPrices.class).in(Singleton.class);

			bind(IHotelRooms.class).to(HotelJpaRooms.class).in(Singleton.class);

			bind(IHotelCustomers.class).to(HotelJpaCustomers.class).in(Singleton.class);

			bind(IGetLogMess.class).annotatedWith(Names.named(IHotelConsts.MESSNAMED))
					.toProvider(HotelMessProvider.class).in(Singleton.class);

			bind(IGetLogMess.class).annotatedWith(Names.named(ISharedConsts.JYTHONMESSSERVER))
					.toProvider(MessProvider.class).in(Singleton.class);

			bind(IReservationForm.class).to(HotelReservations.class).in(Singleton.class);

			bind(IReservationOp.class).to(ReservationOp.class).in(Singleton.class);
			bind(IClearHotel.class).to(ClearObjects.class).in(Singleton.class);
			bind(ICustomerBills.class).to(CustomerBillJpa.class).in(Singleton.class);
			bind(IPaymentBillOp.class).to(PaymentOp.class).in(Singleton.class);
			// common
			bind(IStorageJpaRegistryFactory.class).to(StorageJpaRegistryFactory.class).in(Singleton.class);
			bind(ISequenceRealmGenFactory.class).to(SequenceRealmGenFactory.class).in(Singleton.class);
			bind(IBlobHandler.class).to(BlobEntryJpaHandler.class).in(Singleton.class);

			bind(ISymGeneratorFactory.class).to(SymGeneratorFactory.class).in(Singleton.class);
			bind(ISemaphore.class).to(SemaphoreRegistry.class).in(Singleton.class);
			bind(ISetTestToday.class).toProvider(SetTestTodayProvider.class).in(Singleton.class);
			bind(IReadResourceFactory.class).to(ReadResourceFactory.class).in(Singleton.class);
			bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(Singleton.class);
			bind(IOObjectAdmin.class).to(OObjectAdminJpa.class).in(Singleton.class);
			bind(INoteStorage.class).to(JpaNoteStorage.class).in(Singleton.class);
			bind(IJpaObjectGenSymFactory.class).to(JpaObjectGenSymFactoryImpl.class).in(Singleton.class);
			bind(IAddNewBlob.class).to(AddNewBlob.class).in(Singleton.class);
			bind(IHotelMailList.class).to(HotelMailing.class).in(Singleton.class);
			bind(IJournal.class).to(JpaJournal.class).in(Singleton.class);

			// -----

		}

		// common
		@Provides
		@Singleton
		IStorageRealmRegistry getStorageRealmRegistry(IStorageJpaRegistryFactory rFactory,
				ITransactionContextFactory iC) {
			return rFactory.construct(iC);
		}

		@Provides
		@Singleton
		ITransactionContextFactory getTransactionContextFactory() {
			return new ITransactionContextFactory() {
				@Override
				public ITransactionContext construct() {
					return new JpaNonTransactionContext(EMHolder.getEm());
				}
			};
		}

		@Provides
		@Singleton
		ISequenceRealmGen getSequenceRealmGen(ISequenceRealmGenFactory seqFactory, IStorageRealmRegistry iReg,
				ISemaphore iSem) {
			return seqFactory.construct(iReg, iSem);
		}

	}
}
