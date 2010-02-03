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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VField;

public class RecordFormDefFactory implements IFormDefFactory {

    private final EditWidgetFactory eFactory;
    private final IResLocator rI;

    @Inject
    public RecordFormDefFactory(IResLocator rI, EditWidgetFactory eFactory) {
        this.rI = rI;
        this.eFactory = eFactory;
    }

    public String getFormTitle(IDataType dType) {
        DataType dd = (DataType) dType;
        DictType d = dd.getdType();
        switch (d) {
        case CustomerList:
            return "Kontrahent";
        }
        return null;

    }

    public FormLineContainer construct(IDataType dType) {
        DataType dd = (DataType) dType;
        DictType d = dd.getdType();
        List<FormField> fList = new ArrayList<FormField>();
        switch (d) {
        case CustomerList:
            IFormLineView name = eFactory.constructTextCheckEdit(true);
            IFormLineView descr = eFactory.constructTextField();
            fList.add(new FormField("Symbol", name, new VField(
                    DictionaryP.F.name), true));
            fList.add(new FormField("Nazwa", descr, new VField(
                    DictionaryP.F.description)));

            IFormLineView name1 = eFactory.constructTextField();
            IFormLineView name2 = eFactory.constructTextField();
            IFormLineView fname = eFactory.constructTextField();
            IFormLineView lname = eFactory.constructTextField();
            IFormLineView country = eFactory.constructTextField();
            IFormLineView zipCode = eFactory.constructTextField();
            IFormLineView address1 = eFactory.constructTextField();
            IFormLineView address2 = eFactory.constructTextField();

            IFormLineView pesel = eFactory.constructTextField();
            // constructListCombo
            IFormLineView pType = eFactory.constructListCombo(rI.getLabels()
                    .PTitles());
            IFormLineView docType = eFactory.constructListCombo(rI.getLabels()
                    .DocTypes());
            IFormLineView docNumber = eFactory.constructTextField();

            IFormLineView city = eFactory.constructTextField();
            IFormLineView cType = eFactory.constructListCombo(rI.getLabels()
                    .CustomerType());
            fList
                    .add(new FormField("Nazwa 1", name1, new VField(CustomerP.F.name1)));
            fList
                    .add(new FormField("Nazwa 2", name2, new VField(CustomerP.F.name2)));
            fList
                    .add(new FormField("Rodzaj", cType, new VField(CustomerP.F.cType)));

            fList.add(new FormField("Pan/Pani", pType, new VField(CustomerP.F.pTitle)));
            fList.add(new FormField("Imię", fname, new VField(CustomerP.F.firstName)));
            fList.add(new FormField("Nazwisko", lname, new VField(CustomerP.F.lastName)));
            fList.add(new FormField("PESEL", pesel, new VField(CustomerP.F.PESEL)));
            fList.add(new FormField("Rodzaj dokumentu", docType,new VField(CustomerP.F.docType)));
            fList.add(new FormField("Numer dokument", docNumber,new VField(CustomerP.F.docNumber)));

            fList.add(new FormField("Kraj", country, new VField(CustomerP.F.country)));
            fList.add(new FormField("Kod pocztowy", zipCode,new VField(CustomerP.F.zipCode)));
            fList.add(new FormField("Miasto", city, new VField(CustomerP.F.city)));
            fList.add(new FormField("Adres 1", address1,new VField(CustomerP.F.address1)));
            fList.add(new FormField("Adres 2", address2,new VField(CustomerP.F.address2)));

            break;
        default:
            return null;
        }

        return new FormLineContainer(fList);
    }

}
