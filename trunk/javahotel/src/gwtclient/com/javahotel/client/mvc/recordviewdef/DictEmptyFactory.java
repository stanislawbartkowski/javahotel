/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.recordviewdef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DictEmptyFactory {

    private final IResLocator rI;

    @Inject
    public DictEmptyFactory(IResLocator rI) {
        this.rI = rI;
    }
    private static final List<IField> dictE;
    private static final List<IField> loginE;
    private static final List<IField> adminE;
    private static final List<IField> personE;
    private static final List<IField> hotelE;
    private static final List<IField> personERemove;
    private static final List<IField> roomObjectE;
    private static final List<IField> vatE;
    private static final List<IField> servE;
    private static final List<IField> seasonE;
    private static final List<IField> specE;
    private static final List<IField> priceE;
    private static final List<IField> bankE;
    private static final List<IField> phoneE;
    private static final List<IField> bookE;
    private static final List<IField> bookH;
    private static final List<IField> validH;
    private static final List<IField> bookEH;
    private static final List<IField> addPayE;
    private static final List<IField> roomGuestE;
    private static final List<IField> billE;
    private static final List<IField> addE;

    static {
        roomObjectE = new ArrayList<IField>();
        roomObjectE.add(DictionaryP.F.name);
        roomObjectE.add(ResObjectP.F.standard);
        roomObjectE.add(ResObjectP.F.noperson);

        dictE = new ArrayList<IField>();
        dictE.add(DictionaryP.F.name);

        loginE = new ArrayList<IField>();
        loginE.add(LoginRecord.F.login);
        loginE.add(LoginRecord.F.password);
        loginE.add(LoginRecord.F.hotel);

        adminE = new ArrayList<IField>();
        adminE.add(LoginRecord.F.login);
        adminE.add(LoginRecord.F.password);

        personE = new ArrayList<IField>();
        personE.add(LoginRecord.F.login);
        personE.add(LoginRecord.F.password);
        personE.add(LoginRecord.F.confpassword);

        personERemove = new ArrayList<IField>();
        personERemove.add(LoginRecord.F.login);

        hotelE = new ArrayList<IField>();
        hotelE.add(HotelP.F.name);
        hotelE.add(HotelP.F.database);

        vatE = new ArrayList<IField>();
        vatE.add(DictionaryP.F.name);
        vatE.add(VatDictionaryP.F.vat);

        servE = new ArrayList<IField>();
        servE.add(DictionaryP.F.name);
        servE.add(ServiceDictionaryP.F.servtype);
        servE.add(ServiceDictionaryP.F.vat);

        seasonE = new ArrayList<IField>();
        seasonE.add(DictionaryP.F.name);
        seasonE.add(OfferSeasonP.F.startp);
        seasonE.add(OfferSeasonP.F.endp);

        specE = new ArrayList<IField>();
        specE.add(OfferSeasonPeriodP.F.startP);
        specE.add(OfferSeasonPeriodP.F.endP);

        priceE = new ArrayList<IField>();
        priceE.add(DictionaryP.F.name);
        priceE.add(OfferPriceP.F.season);

        bankE = new ArrayList<IField>();
        bankE.add(BankAccountP.F.accountNumber);

        phoneE = new ArrayList<IField>();
        phoneE.add(PhoneNumberP.F.phoneNumber);

        bookE = new ArrayList<IField>();
        bookE.add(BookingP.F.checkIn);
        bookE.add(BookingP.F.checkOut);
        bookE.add(BookingP.F.noPersons);
        bookE.add(BookingP.F.season);

        bookH = new ArrayList<IField>();
        bookH.add(BookRecordP.F.customerPrice);
        bookH.add(BookRecordP.F.oPrice);

        validH = new ArrayList<IField>();

        bookEH = new ArrayList<IField>();
        bookEH.add(BookElemP.F.checkIn);
        bookEH.add(BookElemP.F.checkOut);
        bookEH.add(BookElemP.F.resObject);
        bookEH.add(BookElemP.F.service);

        addPayE = new ArrayList<IField>();
        addPayE.add(PaymentP.F.amount);
        addPayE.add(PaymentP.F.datePayment);
        addPayE.add(PaymentP.F.payMethod);
        addPayE.add(BookingStateP.F.bState);

        roomGuestE = new ArrayList<IField>();
        roomGuestE.add(GuestP.F.checkIn);
        roomGuestE.add(CustomerP.F.firstName);
        roomGuestE.add(CustomerP.F.lastName);
        roomGuestE.add(CustomerP.F.pTitle);

        billE = new ArrayList<IField>();
        billE.add(BillsCustomer.F.name);
        billE.add(DictionaryP.F.name);
        billE.add(BillsCustomer.F.oPrice);

        addE = new ArrayList<IField>();
        addE.add(AddPaymentP.F.payDate);
        addE.add(AddPaymentP.F.noSe);
        addE.add(AddPaymentP.F.seName);
        addE.add(AddPaymentP.F.customerPrice);
        addE.add(AddPaymentP.F.customerSum);
    }

    private List<IField> getNoEmpty(final DictData.SpecE d) {
        switch (d) {
            case SpecialPeriod:
                return specE;
            case CustomerPhone:
                return phoneE;
            case CustomerAccount:
                return bankE;
            case BookingHeader:
                return bookH;
            case ValidationHeader:
                return validH;
            case BookingElem:
                return bookEH;
            case AddPayment:
                return addPayE;
            case ResGuestList:
                return roomGuestE;
            case BillsList:
                return billE;
            case AddPaymentList:
                return addE;
            case LoginUser:
                return loginE;
            case LoginAdmin:
                return adminE;
        }
        return null;
    }

    private List<IField> getNoEmpty(final DictType d) {
        switch (d) {
            case RoomObjects:
                return roomObjectE;
            case VatDict:
                return vatE;
            case ServiceDict:
                return servE;
            case OffSeasonDict:
                return seasonE;
            case PriceListDict:
                return priceE;
            case BookingList:
                return bookE;
            default:
                break;
        }
        return dictE;
    }

    private List<IField> getNoEmpty(final int action,
            final RType rt) {
        switch (rt) {
            case AllPersons:
                if (action == IPersistAction.DELACTION) {
                    return personERemove;
                }
                return personE;
            case AllHotels:
                return hotelE;
        }
        return null;
    }

    public List<IField> getNoEmpty(final int action,
            final DictData d) {
        List<IField> c = null;
        if (d.getRt() != null) {
            c = getNoEmpty(action, d.getRt());
            if (c != null) {
                return c;
            }
        }
        if (d.isSe()) {
            return getNoEmpty(d.getSE());
        }
        return getNoEmpty(d.getD());
    }

    public List<IField> getNoEmpty(final DictData d) {
        return getNoEmpty(IPersistAction.MODIFACTION, d);
    }

    public Set<IField> getNoEmptyS(final DictData d) {
        List<IField> co = getNoEmpty(d);
        Set<IField> se = new HashSet<IField>();
        se.addAll(co);
        return se;
    }

    public List<IField> getEmptyPriceList() {

        List<IField> e = new ArrayList<IField>();
        e.add(OfferServicePriceP.F.highseasonprice);
        e.add(OfferServicePriceP.F.highseasonweekendprice);
        e.add(OfferServicePriceP.F.lowseasonprice);
        e.add(OfferServicePriceP.F.lowseasonweekendprice);
        e.add(OfferServicePriceP.F.service);
        return e;
    }
}
