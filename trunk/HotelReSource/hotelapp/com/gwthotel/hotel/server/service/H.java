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
package com.gwthotel.hotel.server.service;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.hotel.IClearHotel;
//import com.gwthotel.hotel.IHotelGetName;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resbundle.Mess;

public class H {

    private H() {
    }

    @Inject
    @Named(IHotelConsts.MESSNAMED)
    private static IGetLogMess lMess;


    @Inject
    private static Mess mMess;

    @Inject
    private static IHotelServices iServices;


    @Inject
    private static IHotelPriceList iPriceList;

    @Inject
    private static IHotelPriceElem iPriceElem;

    @Inject
    private static IHotelRooms iRooms;

    @Inject
    private static IHotelCustomers iCustomers;

    @Inject
    private static IGetInstanceOObjectIdCache iGet;

    @Inject
    private static IHotelObjectsFactory oFactory;

    @Inject
    private static IReservationOp resOp;

    @Inject
    private static IReservationForm resForm;

    @Inject
    private static ICustomerBills iBills;

    @Inject
    private static IPaymentBillOp iPayment;

    @Inject
    private static IClearHotel iClear;

    public static IGetLogMess getL() {
        return lMess;
    }

    public static Mess getM() {
        return mMess;
    }

    public static IHotelServices getHotelServices() {
        return iServices;
    }

    public static IHotelPriceList getHotelPriceList() {
        return iPriceList;
    }

    public static IHotelPriceElem getHotelPriceElem() {
        return iPriceElem;
    }

    public static IHotelRooms getHotelRooms() {
        return iRooms;
    }

    public static IHotelCustomers getHotelCustomers() {
        return iCustomers;
    }

    public static void invalidateHotelCache() {
        iGet.invalidateCache();
    }

    public static IHotelObjectsFactory getObjectFactory() {
        return oFactory;
    }

    public static IReservationOp getResOp() {
        return resOp;
    }

    public static IReservationForm getResForm() {
        return resForm;
    }

    public static ICustomerBills getCustomerBills() {
        return iBills;
    }

    public static IPaymentBillOp getPaymentsOp() {
        return iPayment;
    }

    public static IClearHotel getClearHotel() {
        return iClear;
    }

    public static IGetInstanceOObjectIdCache getInstanceObjectId() {
        return iGet;
    }

}
