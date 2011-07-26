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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.client.abstractto.BookElemWithPayment;
import com.javahotel.client.gename.FFactory;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.StringP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

class RecordFormDefFactory implements IFormDefFactory {

    private final EditWidgetFactory eFactory;
    private final EWidgetFactory heFactory;
    private final IResLocator rI;

    RecordFormDefFactory(IResLocator rI, EditWidgetFactory eFactory,
            EWidgetFactory heFactory) {
        this.rI = rI;
        this.eFactory = eFactory;
        this.heFactory = heFactory;
    }

    private List<FormField> getDict(boolean symchecked) {
        List<FormField> fList = new ArrayList<FormField>();
        IFormLineView name;
        IVField vSymbol = new VField(DictionaryP.F.name);
        if (symchecked) {
            name = eFactory.constructTextCheckEdit(vSymbol, true);
        } else {
            name = eFactory.constructTextField(vSymbol);
        }
        fList.add(FFactory.constructRM(DictionaryP.F.name, name));
        fList.add(FFactory.construct(DictionaryP.F.description));

        return fList;
    }

    @Override
    public FormLineContainer construct(ICallContext iContext) {
        DataType dd = (DataType) iContext.getDType();
        List<FormField> fList = new ArrayList<FormField>();
        IField[] fL;
        if (dd.isRType()) {
            switch (dd.getrType()) {
            case AllPersons:
                IVField passwordP = new LoginField(LoginField.F.PASSWORD);
                IVField repasswordP = new LoginField(LoginField.F.REPASSWORD);
                IFormLineView password = eFactory
                        .constructPasswordField(passwordP);
                IFormLineView repassword = eFactory
                        .constructPasswordField(repasswordP);
                fList.add(new FormField("Symbol", null, new LoginField(
                        LoginField.F.LOGINNAME), true, false));
                fList.add(new FormField("Hasło", password, passwordP));
                fList.add(new FormField("Hasło do sprawdzenia", repassword,
                        repasswordP));
                break;

            case AllHotels:
                IVField vDataBase = new VField(HotelP.F.database);
                IFormLineView hDataBase = heFactory.getListValuesBox(vDataBase,
                        RType.DataBases, new CommandParam(), StringP.F.name);
                fList.add(FFactory.constructRM(HotelP.F.name));
                fList.add(FFactory.construct(HotelP.F.description));
                fList.add(FFactory.constructRM(HotelP.F.database, hDataBase));
                break;
            default:
                assert false : rI.getMessages().NotSupportedError(
                        dd.getrType().name());
            }
        }

        CommandParam p;

        if (dd.isAddType()) {
            switch (dd.getAddType()) {
            case BookElem:
                fL = new IField[] { BookElemP.F.checkIn, BookElemP.F.checkOut,
                        BookElemP.F.resObject, BookElemP.F.service };
                FFactory.add(fList, fL);
                fList.add(FFactory
                        .constructReadOnly(BookElemWithPayment.F.customerPrice));
                fList.add(FFactory
                        .constructReadOnly(BookElemWithPayment.F.offerPrice));

                break;
            case BookRecord:
                p = rI.getR().getHotelCommandParam();
                fList.add(new FormField("Suma", new VField(
                        BookRecordP.F.customerPrice)));
                p.setDict(DictType.PriceListDict);
                IVField priceV = new VField(BookRecordP.F.oPrice);
                IFormLineView oPrice = heFactory.getListValuesBox(priceV, p);
                fList.add(new FormField("Cennik", oPrice, priceV));
                break;
            case AdvanceHeader:
                fL = new IField[] { AdvancePaymentP.F.amount,
                        AdvancePaymentP.F.amount };
                FFactory.add(fList, fL);
                break;
            default:
                assert false : rI.getMessages().NotSupportedError(
                        dd.getAddType().name());
                return null;
            }
        }
        if (dd.isDictType()) {
            DictType d = dd.getdType();
            switch (d) {
            case RoomFacility:
            case RoomStandard:
                fList = getDict(false);
                break;

            case VatDict:
                fList = getDict(false);
                fList.add(FFactory.construct(VatDictionaryP.F.vat));
                break;

            case PriceListDict:
                fList = getDict(false);
                fL = new IField[] { OfferPriceP.F.season };
                FFactory.add(fList, fL);
                break;

            case ServiceDict:
                fList = getDict(false);
                FFactory.add(fList, new IField[] { ServiceDictionaryP.F.vat,
                        ServiceDictionaryP.F.servtype });
                break;

            case RoomObjects:
                fList = getDict(false);
                FFactory.add(fList, new IField[] { ResObjectP.F.standard,
                        ResObjectP.F.maxperson });
                break;

            case OffSeasonDict:
                fList = getDict(false);
                fL = new IField[] { OfferSeasonP.F.startp, OfferSeasonP.F.endp };
                FFactory.add(fList, fL);
                break;

            case CustomerList:
                fList = getDict(true);
                fL = new IField[] { CustomerP.F.name1, CustomerP.F.name2,
                        CustomerP.F.cType, CustomerP.F.pTitle,
                        CustomerP.F.firstName, CustomerP.F.lastName,
                        CustomerP.F.PESEL, CustomerP.F.docType,
                        CustomerP.F.docNumber, CustomerP.F.country,
                        CustomerP.F.zipCode, CustomerP.F.city,
                        CustomerP.F.address1, CustomerP.F.address2 };
                FFactory.add(fList, fL);
                break;
            case BookingList:
                fList = getDict(false);
                fL = new IField[] { BookingP.F.checkIn, BookingP.F.checkOut,
                        BookingP.F.season, BookingP.F.noPersons };
                FFactory.add(fList, fL);
                break;
            default:
                assert false : rI.getMessages().NotSupportedError(d.name());
            }

        }
        return new FormLineContainer(fList);
    }
}
