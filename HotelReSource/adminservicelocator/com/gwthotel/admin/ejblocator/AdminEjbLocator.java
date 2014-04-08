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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.IHotelAdmin;
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
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.shared.JythonUIFatal;

public class AdminEjbLocator {

    private AdminEjbLocator() {

    }

    static final private Logger log = Logger.getLogger(AdminEjbLocator.class
            .getName());

    static private void error(String mess, Throwable e) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess, e);
    }

    private static <T> T construct(String name) {
        try {
            Object remoteObj = new InitialContext().lookup(name);
            T inter = (T) remoteObj;
            return inter;
        } catch (NamingException e) {
            error("Cannot load service " + name, e);
        }
        return null;
    }

    static IHotelAdmin getHotelAdmin() {
        return construct(IHotelConsts.HOTELADMINEJBJNDI);
    }

    static IStorageRealmRegistry getStorageRealm() {
        return construct(ISharedConsts.COMMONREGISTRYBEANJNDI);
    }

    static public IBlobHandler getBlobHandler() {
        return construct(ISharedConsts.COMMONBEANBLOBJNDI);
    }

    static IHotelServices getHotelServices() {
        return construct(IHotelConsts.HOTELSERVICESJNDI);
    }

    static IHotelPriceList getHotelPriceList() {
        return construct(IHotelConsts.HOTELPRICELISTJNDI);
    }

    static IHotelPriceElem getHotelPriceElem() {
        return construct(IHotelConsts.HOTELPRICEELEMJNDI);
    }

    static IHotelRooms getHotelRooms() {
        return construct(IHotelConsts.HOTELROOMSJNDI);
    }

    static IHotelCustomers getHotelCustomers() {
        return construct(IHotelConsts.HOTELCUSTOMERSJNDI);
    }

    static IAppInstanceHotel getAppInstanceHotel() {
        return construct(IHotelConsts.HOTELADMININSTANCEEJBJNDI);
    }

    static ISequenceRealmGen getSequenceRealmGen() {
        return construct(ISharedConsts.COMMONSEQGENJNDI);
    }

    public static IReservationForm getReservationForm() {
        return construct(IHotelConsts.HOTELRESERVATIONJNDI);
    }

    public static IReservationOp getReservationOp() {
        return construct(IHotelConsts.HOTELRESERVATIONOPJNDI);
    }

    public static IClearHotel getClearHotel() {
        return construct(IHotelConsts.HOTELCLEAROPJNDI);
    }

    public static ICustomerBills getCustomerBills() {
        return construct(IHotelConsts.HOTELBILLJNDI);
    }

    public static IPaymentBillOp getBillPaymentOp() {
        return construct(IHotelConsts.HOTELPAYMENTOPJNDI);
    }

}
