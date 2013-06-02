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
package com.gwthotel.admin.ejblocator;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.ITestHotelAdmin;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.customer.ITestHotelCustomers;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.pricelist.ITestHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.prices.ITestHotelPriceElem;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.rooms.ITestHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.hotel.services.ITestHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.registry.ITestStorageRealmRegistry;
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

    public static IHotelAdmin getHotelAdmin() {
        return construct(IHotelConsts.HOTELADMINEJBJNDI);
    }

    public static IStorageRealmRegistry getStorageRealm() {
        return construct(ISharedConsts.COMMONREGISTRYBEANJNDI);
    }

    public static ITestHotelAdmin getTestHotelAdmin() {
        return construct(IHotelConsts.TESTHOTELADMINEJBJNDI);
    }

    public static ITestStorageRealmRegistry getTestStorageRealm() {
        return construct(ISharedConsts.TESTCOMMONREGISTRYBEANJNDI);
    }

    public static ITestHotelServices getTestHotelServices() {
        return construct(IHotelConsts.TESTHOTELSERVICESJNDI);
    }

    public static IHotelServices getHotelServices() {
        return construct(IHotelConsts.HOTELSERVICESJNDI);
    }

    public static IHotelPriceList getHotelPriceList() {
        return construct(IHotelConsts.HOTELPRICELISTJNDI);
    }

    public static ITestHotelPriceList getTestHotelPriceList() {
        return construct(IHotelConsts.TESTHOTELPRICELISTJNDI);
    }

    public static IHotelPriceElem getHotelPriceElem() {
        return construct(IHotelConsts.HOTELPRICEELEMJNDI);
    }

    public static ITestHotelPriceElem getTestHotelPriceElem() {
        return construct(IHotelConsts.TESTHOTELPRICEELEMJNDI);
    }

    public static IHotelRooms getHotelRooms() {
        return construct(IHotelConsts.HOTELROOMSJNDI);
    }

    public static ITestHotelRooms getTestHotelRooms() {
        return construct(IHotelConsts.TESTHOTELROOMSJNDI);
    }

    public static IHotelCustomers getHotelCustomers() {
        return construct(IHotelConsts.HOTELCUSTOMERSJNDI);
    }

    public static ITestHotelCustomers getTestHotelCustomers() {
        return construct(IHotelConsts.TESTHOTELCUSTOMERSJNDI);
    }

}
