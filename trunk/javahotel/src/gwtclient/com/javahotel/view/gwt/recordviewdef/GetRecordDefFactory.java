/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.recordviewdef;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.MvcWindowSize;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.StringP;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetRecordDefFactory {

    private GetRecordDefFactory() {
    }

    private static List<RecordField> getStandDict(final IResLocator rI,
            String symName, boolean enable) {
        List<RecordField> dict = new ArrayList<RecordField>();
        ILineField name = GetIEditFactory.getTextEditI(rI);
        ILineField descr = GetIEditFactory.getTextEditI(rI);
        if (enable) {
            dict.add(new RecordField(symName, name, DictionaryP.F.name, false));
        } else {
            dict.add(new RecordField(symName, name, DictionaryP.F.name));
        }
        dict.add(new RecordField("Nazwa", descr, DictionaryP.F.description,
                true));
        return dict;
    }

    private static List<RecordField> getStandDict(final IResLocator rI,
            String symName) {
        return getStandDict(rI, symName, true);
    }

    private static List<RecordField> getDef(final IResLocator rI, final RType rt) {
        List<RecordField> dict = new ArrayList<RecordField>();
        ILineField name = GetIEditFactory.getTextEditI(rI);
        switch (rt) {
        case AllPersons:
            ILineField password = GetIEditFactory.getPasswordEditI(rI);
            ILineField confpassword = GetIEditFactory.getPasswordEditI(rI);
            dict.add(new RecordField("Identyfikator", name,
                    LoginRecord.F.login, false));
            dict.add(new RecordField("Hasło", password, LoginRecord.F.password,
                    true));
            dict.add(new RecordField("Potwierdź hasło", confpassword,
                    LoginRecord.F.confpassword, true));
            break;
        case AllHotels:
            ILineField descr = GetIEditFactory.getTextEditI(rI);
            ILineField database = GetIEditFactory.getListValuesBox(rI,
                    RType.DataBases, new CommandParam(), StringP.F.name);
            dict.add(new RecordField("Nazwa", name, HotelP.F.name, false));
            dict
                    .add(new RecordField("Opis", descr, HotelP.F.description,
                            true));
            dict.add(new RecordField("Baza danych", database,
                    HotelP.F.database, true));
            break;
        default:
            assert false : rt + " invalid value here";
        }
        return dict;
    }

    private static List<RecordField> getDef(final IResLocator rI,
            final DictData.SpecE e) {
        List<RecordField> dict = null;
        switch (e) {
        case SpecialPeriod:
            dict = new ArrayList<RecordField>();
            ILineField descr = GetIEditFactory.getTextEditI(rI);
            ILineField fromP = GetIEditFactory.getTextCalendard(rI);
            ILineField endP = GetIEditFactory.getTextCalendard(rI);
            dict.add(new RecordField("Opis", descr,
                    OfferSeasonPeriodP.F.description, true));
            dict.add(new RecordField("Od", fromP, OfferSeasonPeriodP.F.startP,
                    true));
            dict.add(new RecordField("Do", endP, OfferSeasonPeriodP.F.endP,
                    true));
            break;
        case CustomerAccount:
            dict = new ArrayList<RecordField>();
            ILineField acc = GetIEditFactory.getTextEditI(rI);
            dict.add(new RecordField("Konto bankowe", acc,
                    BankAccountP.F.accountNumber, true));
            break;
        case CustomerPhone:
            dict = new ArrayList<RecordField>();
            ILineField acc1 = GetIEditFactory.getTextEditI(rI);
            dict.add(new RecordField("Telefon", acc1,
                    PhoneNumberP.F.phoneNumber, true));
            break;
        case BookingHeader:
            dict = new ArrayList<RecordField>();
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.PriceListDict);
            ILineField oPrice = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name, null);
            ILineField cAmount = GetIEditFactory.getNumEditI(rI);
            dict.add(new RecordField("Suma", cAmount,
                    BookRecordP.F.customerPrice, true));
            dict.add(new RecordField("Cennik", oPrice, BookRecordP.F.oPrice,
                    true));
            break;
        case ValidationHeader:
            dict = new ArrayList<RecordField>();
            cAmount = GetIEditFactory.getNumEditI(rI);
            ILineField dateTo = GetIEditFactory.getTextCalendard(rI);
            dict.add(new RecordField("Zaliczka", cAmount,
                    AdvancePaymentP.F.amount, true));
            dict.add(new RecordField("Zapłacić do", dateTo,
                    AdvancePaymentP.F.validationDate, true));
            break;
        case BookingElem:
            ILineField checkIn = GetIEditFactory.getTextCalendard(rI);
            ILineField checkOut = GetIEditFactory.getTextCalendard(rI);
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomObjects);
            ILineField resObject = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name, null);
            ILineField service = GetIEditFactory.getLBEditI(rI);
            dict = new ArrayList<RecordField>();
            dict.add(new RecordField("Rezerwacja od", checkIn,
                    BookElemP.F.checkIn, true));
            dict.add(new RecordField("Rezerwacja do", checkOut,
                    BookElemP.F.checkOut, true));
            dict.add(new RecordField("Pokój", resObject, BookElemP.F.resObject,
                    true));
            dict.add(new RecordField("Usługa", service, BookElemP.F.service,
                    true));
            break;
        case RowPaymentElem:
            dict = new ArrayList<RecordField>(); // empty
            break;
        case AddPayment:
            ILineField amount = GetIEditFactory.getNumberCalculator(rI);
            ILineField resType = GetIEditFactory.getEnumMap(rI, rI.getLabels()
                    .BookingStateType());
            ILineField payMethod = GetIEditFactory.getEnumMap(rI, rI
                    .getLabels().PaymentMethod());
            ILineField dateAmount = GetIEditFactory.getTextCalendard(rI);
            dict = new ArrayList<RecordField>();
            dict.add(new RecordField("Zaliczka", amount, PaymentP.F.amount,
                    true));
            dict.add(new RecordField("Data zapłaty", dateAmount,
                    PaymentP.F.datePayment, true));
            dict.add(new RecordField("Forma płatności", payMethod,
                    PaymentP.F.payMethod, true));
            dict.add(new RecordField("Stan rezerwacji", resType,
                    BookingStateP.F.bState, true));
            break;
        case ResGuestList:
            List<RecordField> cList = getDef(rI, new DictData(
                    DictType.CustomerList));
            checkIn = GetIEditFactory.getTextCalendard(rI);
            checkOut = GetIEditFactory.getTextCalendard(rI);
            cList.add(new RecordField("Od", checkIn, GuestP.F.checkIn, true));
            cList.add(new RecordField("Do", checkOut, GuestP.F.checkOut, true));
            descr = GetIEditFactory.getTextEditI(rI);
            ILineField numb = GetIEditFactory.getTextEditI(rI);
            cList
                    .add(new RecordField("Numer", numb, ResRoomGuest.F.name,
                            true));
            cList
                    .add(new RecordField("Opis", descr, ResRoomGuest.F.desc,
                            true));
            dict = cList;
            break;
        case BillsList:
            dict = new ArrayList<RecordField>();
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.PriceListDict);
            oPrice = GetIEditFactory.getListValuesBox(rI, RType.ListDict, p,
                    DictionaryP.F.name, null);
            dict.add(new RecordField("Cennik", oPrice, BillsCustomer.F.oPrice,
                    true));
            ILineField name = GetIEditFactory.getTextCheckEditI(rI, true);
            dict
                    .add(new RecordField("Symbol", name, BillsCustomer.F.name,
                            true));
            descr = GetIEditFactory.getTextEditI(rI);
            dict.add(new RecordField("Symbol", descr, BillsCustomer.F.descr,
                    true));
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.CustomerList);
            ILineField custName = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name);
            dict.add(new RecordField("Klient", custName, DictionaryP.F.name,
                    true));
            break;
        case AddPaymentList:
            dict = new ArrayList<RecordField>();
            ILineField dOp = GetIEditFactory.getTextCalendard(rI);
            dict.add(new RecordField("Data usługi", dOp, AddPaymentP.F.payDate,
                    true));
            descr = GetIEditFactory.getTextEditI(rI);
            dict
                    .add(new RecordField("Opis", descr, AddPaymentP.F.remarks,
                            true));
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.ServiceDict);
            ILineField seName = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name);
            dict.add(new RecordField("Usługa", seName, AddPaymentP.F.seName,
                    true));
            amount = GetIEditFactory.getNumberCalculator(rI);
            dict
                    .add(new RecordField("Ilość", amount, AddPaymentP.F.noSe,
                            true));
            amount = GetIEditFactory.getNumberCalculator(rI);
            dict.add(new RecordField("Cena", amount,
                    AddPaymentP.F.customerPrice, true));
            amount = GetIEditFactory.getNumberCalculator(rI);
            dict.add(new RecordField("Wartość", amount,
                    AddPaymentP.F.customerSum, true));
            break;
        case FromReservHeader:
            ILineField dFrom = GetIEditFactory.getTextCalendard(rI);
            ILineField dTo = GetIEditFactory.getTextCalendard(rI);
            ILineField noPerson = GetIEditFactory.getNumEditI(rI);
            dict = new ArrayList<RecordField>();
            ILineField rname = GetIEditFactory.getTextEditI(rI);
            dict.add(new RecordField("Nazwa", rname, DictionaryP.F.name));
            dict.add(new RecordField("Od", dFrom, BookingP.F.checkIn, true));
            dict.add(new RecordField("Do", dTo, BookingP.F.checkOut, true));
            dict.add(new RecordField("Liczba osób", noPerson,
                    BookingP.F.noPersons, true));
            return dict;
        case LoginUser:
            return getLoginDef(rI, true);
        case LoginAdmin:
            return getLoginDef(rI, false);
        }
        return dict;
    }

    public static List<RecordField> getDef(final IResLocator rI,
            final DictData da) {
        if (da.getSE() != null) {
            return getDef(rI, da.getSE());
        }
        if (da.getD() == null) {
            return getDef(rI, da.getRt());
        }
        List<RecordField> dict = null;
        switch (da.getD()) {
        case RoomFacility:
        case RoomStandard:
            dict = getStandDict(rI, "Symbol");
            break;
        case RoomObjects:
            dict = getStandDict(rI, "Symbol");
            CommandParam p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.RoomStandard);
            ILineField standardB = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name);

            ILineField nopersonE = GetIEditFactory.getNumEditI(rI);
            dict.add(new RecordField("Standard", standardB,
                    ResObjectP.F.standard, true));
            dict.add(new RecordField("Liczba osób", nopersonE,
                    ResObjectP.F.noperson, true));
            break;
        case VatDict:
            dict = getStandDict(rI, "Symbol");
            ILineField percent = GetIEditFactory.getNumberCalculator(rI);
            dict.add(new RecordField("Procent", percent, VatDictionaryP.F.vat,
                    true));
            break;
        case ServiceDict:
            dict = getStandDict(rI, "Symbol");
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.VatDict);
            ILineField vatServ = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name);
            ILineField servType = GetIEditFactory.getEnumMap(rI, rI.getLabels()
                    .Services());
            dict.add(new RecordField("Vat", vatServ, ServiceDictionaryP.F.vat,
                    true));
            dict.add(new RecordField("Typ", servType,
                    ServiceDictionaryP.F.servtype, true));
            break;
        case OffSeasonDict:
            dict = getStandDict(rI, "Symbol");
            ILineField fromP = GetIEditFactory.getTextCalendard(rI);
            ILineField toP = GetIEditFactory.getTextCalendard(rI);

            dict.add(new RecordField("Od", fromP, OfferSeasonP.F.startp, true));

            dict.add(new RecordField("Do", toP, OfferSeasonP.F.endp, true));
            break;
        case PriceListDict:
            dict = getStandDict(rI, "Symbol");
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.OffSeasonDict);
            ILineField season = GetIEditFactory.getListValuesBox(rI,
                    RType.ListDict, p, DictionaryP.F.name);
            dict.add(new RecordField("Sezon", season, OfferPriceP.F.season,
                    true));
            break;
        case CustomerList:
            dict = new ArrayList<RecordField>();
            ILineField name = GetIEditFactory.getTextCheckEditI(rI, true);
            ILineField descr = GetIEditFactory.getTextEditI(rI);
            dict
                    .add(new RecordField("Symbol", name, DictionaryP.F.name,
                            false));
            dict.add(new RecordField("Nazwa", descr, DictionaryP.F.description,
                    true));

            ILineField name1 = GetIEditFactory.getTextEditI(rI);
            ILineField name2 = GetIEditFactory.getTextEditI(rI);
            ILineField fname = GetIEditFactory.getTextEditI(rI);
            ILineField lname = GetIEditFactory.getTextEditI(rI);
            ILineField country = GetIEditFactory.getTextEditI(rI);
            ILineField zipCode = GetIEditFactory.getTextEditI(rI);
            ILineField address1 = GetIEditFactory.getTextEditI(rI);
            ILineField address2 = GetIEditFactory.getTextEditI(rI);

            ILineField pesel = GetIEditFactory.getTextEditI(rI);
            ILineField pType = GetIEditFactory.getEnumMap(rI, rI.getLabels()
                    .PTitles());
            ILineField docType = GetIEditFactory.getEnumMap(rI, rI.getLabels()
                    .DocTypes());
            ILineField docNumber = GetIEditFactory.getTextEditI(rI);

            ILineField city = GetIEditFactory.getTextEditI(rI);
            ILineField cType = GetIEditFactory.getEnumMap(rI, rI.getLabels()
                    .CustomerType());
            dict
                    .add(new RecordField("Nazwa 1", name1, CustomerP.F.name1,
                            true));
            dict
                    .add(new RecordField("Nazwa 2", name2, CustomerP.F.name2,
                            true));
            dict.add(new RecordField("Rodzaj", cType, CustomerP.F.cType, true));

            dict.add(new RecordField("Pan/Pani", pType, CustomerP.F.pTitle,
                    true));
            dict
                    .add(new RecordField("Imię", fname, CustomerP.F.firstName,
                            true));
            dict.add(new RecordField("Nazwisko", lname, CustomerP.F.lastName,
                    true));
            dict.add(new RecordField("PESEL", pesel, CustomerP.F.PESEL, true));
            dict.add(new RecordField("Rodzaj dokumentu", docType,
                    CustomerP.F.docType, true));
            dict.add(new RecordField("Numer dokument", docNumber,
                    CustomerP.F.docNumber, true));

            dict
                    .add(new RecordField("Kraj", country, CustomerP.F.country,
                            true));
            dict.add(new RecordField("Kod pocztowy", zipCode,
                    CustomerP.F.zipCode, true));
            dict.add(new RecordField("Miasto", city, CustomerP.F.city, true));
            dict.add(new RecordField("Adres 1", address1, CustomerP.F.address1,
                    true));
            dict.add(new RecordField("Adres 2", address2, CustomerP.F.address2,
                    true));
            break;

        case BookingList:
            dict = getStandDict(rI, "Symbol", false);
            ILineField dFrom = GetIEditFactory.getTextCalendard(rI);
            ILineField dTo = GetIEditFactory.getTextCalendard(rI);
            ILineField noPerson = GetIEditFactory.getNumEditI(rI);
            p = rI.getR().getHotelCommandParam();
            p.setDict(DictType.OffSeasonDict);
            season = GetIEditFactory.getListValuesBox(rI, RType.ListDict, p,
                    DictionaryP.F.name, null);
            dict.add(new RecordField("Od", dFrom, BookingP.F.checkIn, true));
            dict.add(new RecordField("Do", dTo, BookingP.F.checkOut, true));
            dict.add(new RecordField("Sezon", season, BookingP.F.season, true));
            dict.add(new RecordField("Liczba osób", noPerson,
                    BookingP.F.noPersons, true));
            break;
        }

        return dict;
    }

    public static MvcWindowSize getSize(DictData da) {
        return null;
    }

    private static List<RecordField> getLoginDef(final IResLocator rI,
            final boolean user) {
        List<RecordField> dict = new ArrayList<RecordField>();
        ILineField name = GetIEditFactory.getTextEditI(rI);
        ILineField pass = GetIEditFactory.getPasswordEditI(rI);
        dict.add(new RecordField("Symbol", name, LoginRecord.F.login, false));
        dict.add(new RecordField("Hasło", pass, LoginRecord.F.password, false));
        if (user) {
            ILineField hotel = GetIEditFactory.getListValuesBox(rI,
                    RType.AllHotels, new CommandParam(), HotelP.F.name);
            dict
                    .add(new RecordField("Hotel", hotel, LoginRecord.F.hotel,
                            false));
        }
        return dict;
    }

    public static List<String> getStandPriceNames() {
        List<String> pri = new ArrayList<String>();
        for (int i = 0; i <= ISeasonPriceModel.MAXSPECIALNO; i++) {
            pri.add("");
        }
        pri.set(ISeasonPriceModel.HIGHSEASON, "W sezonie");
        pri.set(ISeasonPriceModel.HIGHSEASONWEEKEND, "W sezonie weekend");
        pri.set(ISeasonPriceModel.LOWSEASON, "Poza sezonem");
        pri.set(ISeasonPriceModel.LOWSEASONWEEKEND, "Poza sezonem weekend");
        return pri;
    }
}
