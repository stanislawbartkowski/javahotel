/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
import java.util.Set;

import com.gwtmodel.table.view.table.ColumnDataType;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ColListFactory {

    private static final ArrayList<ColTitle> dictCol;
    private static final ArrayList<ColTitle> hotelCol;
    private static final ArrayList<ColTitle> personCol;
    private static final ArrayList<ColTitle> resRoomCol;
    private static final ArrayList<ColTitle> vatCol;
    private static final ArrayList<ColTitle> servCol;
    private static final ArrayList<ColTitle> seasCol;
    private static final ArrayList<ColTitle> specPe;
    private static final ArrayList<ColTitle> priceCol;
    private static final ArrayList<ColTitle> custCol;
    private static final ArrayList<ColTitle> editcustCol;
    private static final ArrayList<ColTitle> bankCol;
    private static final ArrayList<ColTitle> phoneCol;
    private static final ArrayList<ColTitle> bookCol;
    private static final ArrayList<ColTitle> bookECol;
    private static final ArrayList<ColTitle> rowPCol;
    private static final ArrayList<ColTitle> conflictRowCol;
    private static final ArrayList<ColTitle> advanceCol;
    private static final ArrayList<ColTitle> resPanelCol;
    private static final ArrayList<ColTitle> billsCol;
    private static final ArrayList<ColTitle> aPay;

    private static ArrayList<ColTitle> createA(String sy) {
        ArrayList<ColTitle> a = new ArrayList<ColTitle>();
        a.add(new ColTitle(DictionaryP.F.name, sy));
        a.add(new ColTitle(DictionaryP.F.description, "Opis"));
        return a;
    }

    private static ArrayList<ColTitle> createA() {
        return createA("Symbol");
    }

    static {
        specPe = new ArrayList<ColTitle>();
        specPe.add(new ColTitle(OfferSeasonPeriodP.F.description, "Opis"));
        specPe.add(new ColTitle(OfferSeasonPeriodP.F.startP, "Od",
                ColumnDataType.DATE));
        specPe.add(new ColTitle(OfferSeasonPeriodP.F.endP, "Do",
                ColumnDataType.DATE));

        dictCol = createA();

        hotelCol = new ArrayList<ColTitle>();
        hotelCol.add(new ColTitle(HotelP.F.name, "Nazwa"));
        hotelCol.add(new ColTitle(HotelP.F.description, "Opis"));
        hotelCol.add(new ColTitle(HotelP.F.database, "Baza"));

        personCol = new ArrayList<ColTitle>();
        personCol.add(new ColTitle(LoginRecord.F.login, "Nazwa"));

        resRoomCol = createA("Numer");
        resRoomCol.add(new ColTitle(ResObjectP.F.standard, "Standard"));
        resRoomCol.add(new ColTitle(ResObjectP.F.noperson, "L osób",
                ColumnDataType.INTEGER));

        vatCol = createA();
        vatCol.add(new ColTitle(VatDictionaryP.F.vat, "Procent",
                ColumnDataType.NUMBER));

        servCol = createA();
        servCol.add(new ColTitle(ServiceDictionaryP.F.servtype, "Rodzaj"));
        servCol.add(new ColTitle(ServiceDictionaryP.F.vat, "Vat"));

        seasCol = createA();
        seasCol.add(new ColTitle(OfferSeasonP.F.startp, "Okres od",
                ColumnDataType.DATE));
        seasCol.add(new ColTitle(OfferSeasonP.F.endp, "Okres do",
                ColumnDataType.DATE));

        priceCol = createA();
        priceCol.add(new ColTitle(OfferPriceP.F.season, "Sezon"));

        custCol = createA();
        custCol.add(new ColTitle(CustomerP.F.name1, "Nazwa 1"));
        custCol.add(new ColTitle(CustomerP.F.name2, "Nazwa 2"));
        custCol.add(new ColTitle(CustomerP.F.cType, "Typ"));

        editcustCol = new ArrayList<ColTitle>();
        editcustCol.add(new ColTitle(ResRoomGuest.F.name, "Numer"));
        editcustCol.add(new ColTitle(ResRoomGuest.F.desc, "Opis"));
        editcustCol.add(new ColTitle(GuestP.F.checkIn, "Od",
                ColumnDataType.DATE));
        editcustCol.add(new ColTitle(GuestP.F.checkOut, "Do",
                ColumnDataType.DATE));
        editcustCol.add(new ColTitle(ResRoomGuest.F.choosebutt, "Wybierz"));
        editcustCol.add(new ColTitle(CustomerP.F.pTitle, "Pan/Pani"));
        editcustCol.add(new ColTitle(CustomerP.F.firstName, "Imię"));
        editcustCol.add(new ColTitle(CustomerP.F.lastName, "Nazwisko"));

        bankCol = new ArrayList<ColTitle>();
        bankCol
                .add(new ColTitle(BankAccountP.F.accountNumber,
                        "Numer rachunku"));

        phoneCol = new ArrayList<ColTitle>();
        phoneCol
                .add(new ColTitle(PhoneNumberP.F.phoneNumber, "Numer telefonu"));

        bookCol = createA();
        bookCol.add(new ColTitle(BookingP.F.checkIn, "Rezerwacja od",
                ColumnDataType.DATE));
        bookCol.add(new ColTitle(BookingP.F.checkOut, "Rezerwacja do",
                ColumnDataType.DATE));
        bookCol.add(new ColTitle(BookingP.F.noPersons, "Liczba osób",
                ColumnDataType.INTEGER));

        bookECol = new ArrayList<ColTitle>();
        bookECol.add(new ColTitle(BookElemP.F.checkIn, "Od",
                ColumnDataType.DATE));
        bookECol.add(new ColTitle(BookElemP.F.checkOut, "Do",
                ColumnDataType.DATE));
        bookECol.add(new ColTitle(BookElemP.F.resObject, "Pokój"));
        bookECol.add(new ColTitle(BookElemP.F.service, "Usługa"));

        rowPCol = new ArrayList<ColTitle>();
        rowPCol.add(new ColTitle(PaymentRowP.F.rowFrom, "Od",
                ColumnDataType.DATE));
        rowPCol
                .add(new ColTitle(PaymentRowP.F.rowTo, "Do",
                        ColumnDataType.DATE));
        rowPCol.add(new ColTitle(PaymentRowP.F.offerPrice, "Cennik"));
        rowPCol.add(new ColTitle(PaymentRowP.F.customerPrice, "Cena klienta",
                ColumnDataType.NUMBER));

        conflictRowCol = new ArrayList<ColTitle>();
        conflictRowCol.add(new ColTitle(ResDayObjectStateP.F.bookName,
                "Nazwa rezerwacji"));
        conflictRowCol.add(new ColTitle(ResDayObjectStateP.F.d, "Data",
                ColumnDataType.DATE));
        conflictRowCol
                .add(new ColTitle(ResDayObjectStateP.F.resObject, "Pokój"));

        advanceCol = new ArrayList<ColTitle>();
        advanceCol.add(new ColTitle(DictionaryP.F.name, "Klient"));
        advanceCol.add(new ColTitle(DictionaryP.F.description, "Opis"));
        advanceCol.add(new ColTitle(DownPaymentP.F.resId, "Rezerwacja"));
        advanceCol.add(new ColTitle(AdvancePaymentP.F.amount, "Kwota zaliczki",
                ColumnDataType.NUMBER));
        advanceCol.add(new ColTitle(AdvancePaymentP.F.validationDate, "Termin",
                ColumnDataType.DATE));
        advanceCol.add(new ColTitle(DownPaymentP.F.sumPayment, "Zapłata",
                ColumnDataType.NUMBER));

        resPanelCol = createA("Numer");

        billsCol = new ArrayList<ColTitle>();
        billsCol.add(new ColTitle(BillsCustomer.F.name, "Symbol"));
        billsCol.add(new ColTitle(BillsCustomer.F.descr, "Nazwa"));
        billsCol.add(new ColTitle(DictionaryP.F.name, "Klient"));
        billsCol.add(new ColTitle(DictionaryP.F.description, "Nazwa"));

        aPay = new ArrayList<ColTitle>();
        aPay.add(new ColTitle(AddPaymentP.F.payDate, "Data usługi",
                ColumnDataType.DATE));
        aPay.add(new ColTitle(AddPaymentP.F.remarks, "Opis"));
        aPay.add(new ColTitle(AddPaymentP.F.seName, "Usługa"));
        aPay.add(new ColTitle(AddPaymentP.F.seDesc, "Opis"));
        aPay.add(new ColTitle(AddPaymentP.F.noSe, "Ilość",
                ColumnDataType.NUMBER));
    }

    private ArrayList<ColTitle> getColList(DictData.SpecE e) {
        switch (e) {
        case SpecialPeriod:
            return specPe;
        case CustomerPhone:
            return phoneCol;
        case CustomerAccount:
            return bankCol;
        case BookingHeader:
        case ValidationHeader:
        case AddPayment:
            // empty, to avoid null
            return new ArrayList<ColTitle>();
        case BookingElem:
            return bookECol;
        case RowPaymentElem:
            return rowPCol;
        case ObjectResConflict:
            return conflictRowCol;
        case ResTablePanel:
            return resPanelCol;
        case BillsList:
            return billsCol;
        case AddPaymentList:
            return aPay;
        }
        return null;
    }

    private static ArrayList<ColTitle> getColList(final DictType dict) {
        switch (dict) {
        case RoomFacility:
        case RoomStandard:
            return dictCol;
        case RoomObjects:
            return resRoomCol;
        case VatDict:
            return vatCol;
        case ServiceDict:
            return servCol;
        case OffSeasonDict:
            return seasCol;
        case PriceListDict:
            return priceCol;
        case CustomerList:
            return custCol;
        case BookingList:
            return bookCol;
        default:
            break;
        }
        return null;
    }

    private static ArrayList<ColTitle> getColList(final RType r) {
        switch (r) {
        case AllHotels:
            return hotelCol;
        case AllPersons:
            return personCol;
        case DownPayments:
            return advanceCol;
        default:
            break;
        }
        return null;
    }

    public ArrayList<ColTitle> getColList(final DictData da) {
        if (da.isSe()) {
            return getColList(da.getSE());
        }
        if (da.getD() != null) {
            return getColList(da.getD());
        }
        return getColList(da.getRt());
    }

    public String getHeader(final DictData da) {
        if (da.getD() != null) {
            switch (da.getD()) {
            case CustomerList:
                return "Lista klientów";
            default:
                break;
            }
        }
        if (da.getSE() == null) {
            return null;
        }
        switch (da.getSE()) {
        case SpecialPeriod:
            switch (da.getSpeT()) {
            case LOW:
                return "Poza sezonem";
            case SPECIAL:
                return "Okresy specjalne";
            default:
                break;
            }
        case CustomerAccount:
            return "Konta bankowe";
        case CustomerPhone:
            return "Telefony";
        case BillsList:
            return "Lista rachunków do pobytu";
        default:
            break;
        }
        return null;
    }

    public ArrayList<ColTitle> getEditColList(final DictData da) {
        return editcustCol;
    }

    public Set<IField> getNoEditableColList(final DictData da) {
        Set<IField> se = new HashSet<IField>();
        se.add(ResRoomGuest.F.name);
        se.add(ResRoomGuest.F.desc);
        return se;
    }

}
