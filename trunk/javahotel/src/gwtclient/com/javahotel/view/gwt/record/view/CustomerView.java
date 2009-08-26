/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.record.view;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.ifield.IChangeListener;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.util.GetELine;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CustomerView extends VRecordView {

    private final TabPanel tab = new TabPanel();
    private ILineField eC;

    private void setTab(String ro) {
        if (ro == null) {
            tab.selectTab(0);
            return;
        }
        CustomerType cType = CustomerType.valueOf(ro);
        if (cType == CustomerType.Person) {
            tab.selectTab(1);
        } else {
            tab.selectTab(0);
        }
    }

    private void changeC() {
        setTab(eC.getVal());
    }

    CustomerView(final IResLocator rI, final DictData da, final IRecordDef model,
            final IContrPanel contr, final IControlClick co,
            final IAuxRecordPanel auxV) {
        super(rI, da, model, contr, co, auxV);
    }

    private class CList implements IChangeListener {

        public void onChange(ILineField sender) {
            changeC();
        }
    }

    @Override
    public void initW(final IPanel vPP, final Widget iW) {
        IField f = CustomerP.F.cType;
        eC = GetELine.getE(getModel(), f);
        VerticalPanel vpm = new VerticalPanel();
        Set<IField> fi = new HashSet<IField>();
        fi.add(DictionaryP.F.name);
        fi.add(DictionaryP.F.description);
        fi.add(CustomerP.F.cType);
        fi.add(CustomerP.F.country);
        fi.add(CustomerP.F.zipCode);
        fi.add(CustomerP.F.city);
        fi.add(CustomerP.F.address1);
        fi.add(CustomerP.F.address2);
        createWDialog(vpm, fi);

        //                        zipCode, name1, name2, city, stateUS, country, address1, address2,
//        cType, NIP, PESEL, docType, docNumber,
//        firstName, lastName, pTitle


        VerticalPanel vp1 = new VerticalPanel();
        fi = new HashSet<IField>();
        fi.add(CustomerP.F.name1);
        fi.add(CustomerP.F.name2);
        createWDialog(vp1, fi);

        VerticalPanel vp2 = new VerticalPanel();
        fi = new HashSet<IField>();
        fi.add(CustomerP.F.pTitle);
        fi.add(CustomerP.F.firstName);
        fi.add(CustomerP.F.lastName);
        fi.add(CustomerP.F.docType);
        fi.add(CustomerP.F.docNumber);
        fi.add(CustomerP.F.PESEL);
        createWDialog(vp2, fi);

        tab.add(vp1, "Firma", true);
        tab.add(vp2, "Osoba", true);
        vpm.add(tab);

        super.initW(vPP, vpm);
        eC.setChangeListener(new CList());
        changeC();
    }
}
