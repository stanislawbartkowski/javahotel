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
package com.gwthotel.admin.ejblocator;

import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

public interface IBeanLocator {

    IOObjectAdmin getObjectAdmin();

    IAppInstanceOObject getAppInstanceObject();

    IStorageRealmRegistry getStorageRealm();

    IBlobHandler getBlobHandler();

    IHotelServices getHotelServices();

    IHotelPriceList getHotelPriceList();

    IHotelPriceElem getHotelPriceElem();

    IHotelRooms getHotelRooms();

    IHotelCustomers getHotelCustomers();

    ISequenceRealmGen getSequenceRealmGen();

    IReservationForm getReservationForm();

    IReservationOp getReservationOp();

    IClearHotel getClearHotel();

    ICustomerBills getCustomerBills();

    IPaymentBillOp getBillPaymentOp();

}
