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
package com.javahotel.nmvc.factories;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.VSField;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.M;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.gename.IGetFieldName;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.nmvc.factories.advancepayment.AdvancePayment;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ColListFactory {

    String getTitle(DataType d) {
        if (d.isRType()) {
            switch (d.getrType()) {
            case AllHotels:
                return M.L().ListOfHotels();
            case AllPersons:
                return M.L().ListOfUsers();
            case DownPayments:
                return "Lista zaliczek";
            default:
                assert false : M.M().NotSupportedError(d.getrType().name());

            }
        }
        if (d.isDictType()) {
            switch (d.getdType()) {
            case VatDict:
                return "Lista stawek vat";
            case RoomFacility:
                return "Lista wyposażenia";
            case ServiceDict:
                return "Lista usług";
            case RoomStandard:
                return "Wykaz standardów";
            case RoomObjects:
                return "Lista pokojów";
            case CustomerList:
                return "Lista kontrahentów";
            case OffSeasonDict:
                return "List sezonów";
            case PriceListDict:
                return "Lista cenników";
            case BookingList:
                return "Rezerwacje";
            case IssuerInvoiceList:
                return "Dane do wystawienia faktury";
            case InvoiceList:
                return "Lista faktur";
            default:
                assert false : M.M().NotSupportedError(d.getdType().name());
            }
        }
        if (d.isAddType()) {
            switch (d.getAddType()) {
            case BookNoRoom:
            case BookRoom:
            case RowPaymentElem:
                return null;
            }
        }
        assert false : M.M().NotSupportedErrorS();
        return null;
    }

    List<VListHeaderDesc> getColList(DataType d) {
        IField[] fList = null;
        IField[] dList = new IField[] { DictionaryP.F.name,
                DictionaryP.F.description };
        if (d.isAllPersons()) {
            IVField v = new LoginField(LoginField.F.LOGINNAME);
            VListHeaderDesc c = new VListHeaderDesc("Nazwa", v);
            List<VListHeaderDesc> li = new ArrayList<VListHeaderDesc>();
            li.add(c);
            return li;
        }
        if (d.isRType()) {
            switch (d.getrType()) {
            case AllHotels:
                dList = null;
                fList = new IField[] { HotelP.F.name, HotelP.F.description,
                        HotelP.F.database };
                break;
            case DownPayments:
                dList = null;
                VListHeaderDesc bAction = new VListHeaderDesc("Pokaż",
                        VSField.createVString(AdvancePayment.CHOOSE_STRING),
                        false, AdvancePayment.CHOOSE_STRING, false);

                fList = new IField[] { BookingP.F.dateOp,
                        BookingP.F.validationAmount, BookingP.F.validationDate };

                // IGetFieldName i = HInjector.getI().getGetFieldName();
                // String na = i.getName(DownPaymentP.F.sumPayment);
                String na = "Zapłata";
                IVField f = VSField.createVDecimal(AdvancePayment.PAY_STRING);
                VListHeaderDesc bPayment = new VListHeaderDesc(na, f, false,
                        AdvancePayment.PAY_STRING, false);

                List<VListHeaderDesc> li = FFactory.constructH(null, fList);
                li.add(0, bAction);
                li.add(bPayment);
                return li;

            default:
                assert false : M.M().NotSupportedError(d.getrType().name());

            }
        } else if (d.isDictType()) {
            switch (d.getdType()) {
            case VatDict:
                fList = new IField[] { VatDictionaryP.F.vat };
                break;
            case RoomStandard:
            case RoomFacility:
                fList = new IField[] {};
                break;
            case ServiceDict:
                fList = new IField[] { ServiceDictionaryP.F.vat,
                        ServiceDictionaryP.F.servtype };
                break;
            case RoomObjects:
                fList = new IField[] { ResObjectP.F.standard,
                        ResObjectP.F.maxperson };
                break;
            case CustomerList:
                fList = new IField[] { CustomerP.F.name1, CustomerP.F.name2,
                        CustomerP.F.cType };
                break;
            case OffSeasonDict:
                fList = new IField[] { OfferSeasonP.F.startp,
                        OfferSeasonP.F.endp };
                break;
            case PriceListDict:
                fList = new IField[] { OfferPriceP.F.season };
                break;
            case BookingList:
                fList = new IField[] { BookingP.F.checkIn, BookingP.F.checkOut,
                        BookingP.F.season, BookingP.F.noPersons };
                break;
            case IssuerInvoiceList:
                fList = new IField[] { InvoiceIssuerP.F.bankAccount };
                break;
            case InvoiceList:
                fList = new IField[] {};
                break;
            default:
                assert false : M.M().NotSupportedError(d.getdType().name());
                break;
            }
        } else if (d.isAddType()) {
            dList = null;
            switch (d.getAddType()) {
            case BookRoom:
                fList = new IField[] { BookElemP.F.checkIn,
                        BookElemP.F.checkOut, BookElemP.F.resObject,
                        BookElemP.F.service };
                break;
            case BookNoRoom:
                fList = new IField[] { BookElemP.F.checkIn,
                        BookElemP.F.resObject, BookElemP.F.service,
                        PaymentRowP.F.customerPrice };
                break;
            case RowPaymentElem:
                fList = new IField[] { PaymentRowP.F.offerPrice,
                        PaymentRowP.F.customerPrice, PaymentRowP.F.rowFrom,
                        PaymentRowP.F.rowTo };
                break;
            default:
                assert false : M.M().NotSupportedError(d.getAddType().name());
            }
        } else {
            assert false : M.M().NotSupportedError(d.getrType().name());
        }

        return FFactory.constructH(dList, fList);
    }

}
