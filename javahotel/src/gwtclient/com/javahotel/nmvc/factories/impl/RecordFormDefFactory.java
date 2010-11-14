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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VField;
import com.javahotel.nmvc.ewidget.EWidgetFactory;

public class RecordFormDefFactory implements IFormTitleFactory,IFormDefFactory {

    private final EditWidgetFactory eFactory;
    private final EWidgetFactory heFactory;
    private final IResLocator rI;

    @Inject
    public RecordFormDefFactory(IResLocator rI, EditWidgetFactory eFactory,
            EWidgetFactory heFactory) {
        this.rI = rI;
        this.eFactory = eFactory;
        this.heFactory = heFactory;
    }

    private List<FormField> getDict(boolean symchecked) {
        List<FormField> fList = new ArrayList<FormField>();
        IFormLineView name;
        if (symchecked) {
            name = eFactory.constructTextCheckEdit(true,
                    DictionaryP.F.name.toString());
        } else {
            name = eFactory.constructTextField(DictionaryP.F.name.toString());
        }
        IFormLineView descr = eFactory.constructTextField(DictionaryP.F.description.toString());
        fList.add(new FormField("Symbol", name, new VField(DictionaryP.F.name),
                true));
        fList.add(new FormField("Nazwa", descr, new VField(
                DictionaryP.F.description)));
        return fList;
    }

    @Override
    public String getFormTitle(IDataType dType) {
        DataType dd = (DataType) dType;
        DictType d = dd.getdType();
        switch (d) {
            case CustomerList:
                return "Kontrahent";
            case OffSeasonDict:
                return "Sezony";
        }
        return null;

    }

    @Override
    public FormLineContainer construct(IDataType dType) {
        DataType dd = (DataType) dType;
        List<FormField> fList = new ArrayList<FormField>();
        if (dd.isRType()) {
            switch (dd.getrType()) {
                case AllPersons:
                    IFormLineView loginname = eFactory.constructTextField(LoginField.F.LOGINNAME.toString());
                    IFormLineView password = eFactory.constructPasswordField(LoginField.F.PASSWORD.toString());
                    IFormLineView repassword = eFactory.constructPasswordField(LoginField.F.REPASSWORD.toString());
                    fList.add(new FormField("Symbol", loginname, new LoginField(
                            LoginField.F.LOGINNAME), true));
                    fList.add(new FormField("Hasło", password, new LoginField(
                            LoginField.F.PASSWORD)));
                    fList.add(new FormField("Hasło do sprawdzenia", repassword,
                            new LoginField(LoginField.F.REPASSWORD)));
                    return new FormLineContainer(fList);
            }
            return null;
        }
        if (dd.isAddType()) {
            switch (dd.getAddType()) {
                case BookRecord:
                    CommandParam p = rI.getR().getHotelCommandParam();
                    p.setDict(DictType.PriceListDict);
                    IFormLineView oPrice = heFactory.getListValuesBox(p);
                    IFormLineView cAmount = eFactory.contructCalculatorNumber(2,
                            BookRecordP.F.customerPrice.toString());
                    fList.add(new FormField("Suma", cAmount, new VField(
                            BookRecordP.F.customerPrice)));
                    fList.add(new FormField("Cennik", oPrice, new VField(
                            BookRecordP.F.oPrice)));
                    break;
                case AdvanceHeader:
                    cAmount = eFactory.contructCalculatorNumber(2,
                            AdvancePaymentP.F.amount.toString());
                    IFormLineView dateTo = eFactory.construcDateBoxCalendar();
                    fList.add(new FormField("Zaliczka", cAmount, new VField(
                            AdvancePaymentP.F.amount)));
                    fList.add(new FormField("Zapłacić do", dateTo, new VField(
                            AdvancePaymentP.F.validationDate)));
                    break;
            }
        }
        if (dd.isDictType()) {
            DictType d = dd.getdType();
            switch (d) {
                case OffSeasonDict:
                    fList = getDict(false);
                    IFormLineView fromP = eFactory.construcDateBoxCalendar();
                    IFormLineView toP = eFactory.construcDateBoxCalendar();
                    fList.add(new FormField("Od", fromP, new VField(
                            OfferSeasonP.F.startp)));
                    fList.add(new FormField("Do", toP, new VField(
                            OfferSeasonP.F.endp)));
                    break;

                case CustomerList:
                    fList = getDict(true);
                    IFormLineView name1 = eFactory.constructTextField(CustomerP.F.name1.toString());
                    IFormLineView name2 = eFactory.constructTextField(CustomerP.F.name2.toString());
                    IFormLineView fname = eFactory.constructTextField(CustomerP.F.firstName.toString());
                    IFormLineView lname = eFactory.constructTextField(CustomerP.F.lastName.toString());
                    IFormLineView country = eFactory.constructTextField(CustomerP.F.country.toString());
                    IFormLineView zipCode = eFactory.constructTextField(CustomerP.F.zipCode.toString());
                    IFormLineView address1 = eFactory.constructTextField(CustomerP.F.address1.toString());
                    IFormLineView address2 = eFactory.constructTextField(CustomerP.F.address2.toString());

                    IFormLineView pesel = eFactory.constructTextField(CustomerP.F.PESEL.toString());
                    // constructListCombo
                    IFormLineView pType = eFactory.constructListCombo(rI.getLabels().PTitles());
                    IFormLineView docType = eFactory.constructListCombo(rI.getLabels().DocTypes());
                    IFormLineView docNumber = eFactory.constructTextField(CustomerP.F.docType.toString());

                    IFormLineView city = eFactory.constructTextField(CustomerP.F.city.toString());
                    IFormLineView cType = eFactory.constructListCombo(rI.getLabels().CustomerType());
                    fList.add(new FormField("Nazwa 1", name1, new VField(
                            CustomerP.F.name1)));
                    fList.add(new FormField("Nazwa 2", name2, new VField(
                            CustomerP.F.name2)));
                    fList.add(new FormField("Rodzaj", cType, new VField(
                            CustomerP.F.cType)));

                    fList.add(new FormField("Pan/Pani", pType, new VField(
                            CustomerP.F.pTitle)));
                    fList.add(new FormField("Imię", fname, new VField(
                            CustomerP.F.firstName)));
                    fList.add(new FormField("Nazwisko", lname, new VField(
                            CustomerP.F.lastName)));
                    fList.add(new FormField("PESEL", pesel, new VField(
                            CustomerP.F.PESEL)));
                    fList.add(new FormField("Rodzaj dokumentu", docType,
                            new VField(CustomerP.F.docType)));
                    fList.add(new FormField("Numer dokument", docNumber,
                            new VField(CustomerP.F.docNumber)));

                    fList.add(new FormField("Kraj", country, new VField(
                            CustomerP.F.country)));
                    fList.add(new FormField("Kod pocztowy", zipCode, new VField(
                            CustomerP.F.zipCode)));
                    fList.add(new FormField("Miasto", city, new VField(
                            CustomerP.F.city)));
                    fList.add(new FormField("Adres 1", address1, new VField(
                            CustomerP.F.address1)));
                    fList.add(new FormField("Adres 2", address2, new VField(
                            CustomerP.F.address2)));

                    break;
                default:
                    return null;
            }
        }
        return new FormLineContainer(fList);
    }
}
