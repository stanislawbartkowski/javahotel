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
package com.javahotel.client.gename.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javahotel.client.abstractto.BookPaymentField;
import com.javahotel.client.gename.IGetFieldName;
import com.javahotel.client.gename.ISeasonPriceNames;
import com.javahotel.client.types.DataType;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * @author hotel
 * 
 */
public class GetFieldName implements IGetFieldName {

    private static final Map<IField, String> ma = new HashMap<IField, String>();

    static {
        ma.put(DictionaryP.F.name, "Symbol");
        ma.put(DictionaryP.F.description, "Nazwa");

        ma.put(HotelP.F.name, "Hotel");
        ma.put(HotelP.F.description, "Opis");
        ma.put(HotelP.F.database, "Baza danych");

        ma.put(VatDictionaryP.F.vat, "Procent");

        ma.put(ServiceDictionaryP.F.vat, "Stawka Vat");
        ma.put(ServiceDictionaryP.F.servtype, "Typ");
        ma.put(ServiceDictionaryP.F.noPerson, "Liczba osób");

        ma.put(ResObjectP.F.standard, "Standard");
        ma.put(ResObjectP.F.maxperson, "L osób");
        ma.put(ResObjectP.F.rtype, "Rodzaj obiektu");

        ma.put(CustomerP.F.name1, "Nazwa 1");
        ma.put(CustomerP.F.name2, "Nazwa 2");
        ma.put(CustomerP.F.cType, "Rodzaj");
        ma.put(CustomerP.F.pTitle, "Pan/Pani");
        ma.put(CustomerP.F.firstName, "Imię");
        ma.put(CustomerP.F.lastName, "Nazwisko");
        ma.put(CustomerP.F.PESEL, "Pesel");
        ma.put(CustomerP.F.docType, "Rodzaj dokumentu");
        ma.put(CustomerP.F.docNumber, "Numer dokumentu");
        ma.put(CustomerP.F.country, "Kraj");
        ma.put(CustomerP.F.zipCode, "Kod pocztowy");
        ma.put(CustomerP.F.city, "Miasto");
        ma.put(CustomerP.F.address1, "Adres 1");
        ma.put(CustomerP.F.address2, "Adres 2");
        ma.put(CustomerP.F.mailAddress, "Adres mejlowy");

        ma.put(OfferSeasonP.F.startp, "Okres od");
        ma.put(OfferSeasonP.F.endp, "Okres do");

        ma.put(OfferPriceP.F.season, "Sezon");

        ma.put(BookingP.F.checkIn, "Od");
        ma.put(BookingP.F.checkOut, "Do");
        ma.put(BookingP.F.season, "Sezon");
        ma.put(BookingP.F.noPersons, "Liczba osób");
        ma.put(BookingP.F.resName, "Symbol");

        ma.put(BookElemP.F.checkIn, "Od");
        ma.put(BookElemP.F.checkOut, "Do");
        ma.put(BookElemP.F.service, "Usługa");
        ma.put(BookElemP.F.resObject, "Pokój");

        ma.put(PaymentRowP.F.customerPrice, "Cena klienta");
        ma.put(PaymentRowP.F.offerPrice, "Cena z cennika");
        ma.put(PaymentRowP.F.rowFrom, "Od");
        ma.put(PaymentRowP.F.rowTo, "Do");

        ma.put(BookPaymentField.customerPrice, "Cena klienta");
        ma.put(BookPaymentField.offerPrice, "Cena z cennika");

        ma.put(AdvancePaymentP.F.amount, "Zaliczka");
        ma.put(AdvancePaymentP.F.validationDate, "Zapłacić do");

        ma.put(BookRecordP.F.customerPrice, "Suma");
        ma.put(BookRecordP.F.oPrice, "Cennik");

        ma.put(GuestP.F.checkIn, "Od");
        ma.put(GuestP.F.checkOut, "Do");

        ma.put(DownPaymentP.F.sumPayment, "Wpłata");
        ma.put(AdvancePaymentP.F.amount, "Kwota zaliczki");
        ma.put(AdvancePaymentP.F.dateOp, "Data wystawienia");
        ma.put(AdvancePaymentP.F.validationDate, "Termin płatności");

        ma.put(PaymentP.F.amount, "Kwota wpłaty");
        ma.put(PaymentP.F.datePayment, "Data wpłaty");
        ma.put(PaymentP.F.payMethod, "Metoda wpłaty");
    }

    @Override
    public String getName(IField f) {
        return getName(null, f);
    }

    @Override
    public String getName(DataType d, IField f) {
        return ma.get(f);
    }

    @Override
    public List<String> getStandPriceNames() {
        List<String> pri = new ArrayList<String>();
        for (int i = 0; i <= ISeasonPriceNames.MAXSPECIALNO; i++) {
            pri.add("");
        }
        pri.set(ISeasonPriceNames.HIGHSEASON, "W sezonie");
        pri.set(ISeasonPriceNames.HIGHSEASONWEEKEND, "W sezonie weekend");
        pri.set(ISeasonPriceNames.LOWSEASON, "Poza sezonem");
        pri.set(ISeasonPriceNames.LOWSEASONWEEKEND, "Poza sezonem weekend");
        return pri;
    }

}
