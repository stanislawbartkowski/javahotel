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

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IDataFormConstructor;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.util.CreateFormView;
import com.gwtmodel.table.view.util.FormUtil;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VField;

public class CustomerForm implements IDataFormConstructor {

    private void setTab(TabPanel tab, String ro) {
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

    private void changeC(TabPanel tab, IFormLineView i) {
        setTab(tab, i.getVal());
    }

    private class CList implements IFormChangeListener {

        private final TabPanel tab;

        CList(TabPanel tab) {
            this.tab = tab;
        }

        @Override
        public void onChange(IFormLineView i) {
            changeC(tab, i);
        }
    }

    @Override
    public Widget construct(ICallContext iContext, FormLineContainer model) {
        Set<IVField> fi = new HashSet<IVField>();
        DataUtil.addToSet(fi, model.getfList(), DictionaryP.F.name);
        DataUtil.addToSet(fi, model.getfList(), DictionaryP.F.description);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.cType);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.country);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.zipCode);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.city);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.address1);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.address2);
        Widget w = CreateFormView.construct(model.getfList(), fi);

        fi = new HashSet<IVField>();
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.name1);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.name2);
        Widget w1 = CreateFormView.construct(model.getfList(), fi);

        fi = new HashSet<IVField>();
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.pTitle);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.firstName);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.lastName);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.docType);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.docNumber);
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.PESEL);
        Widget w2 = CreateFormView.construct(model.getfList(), fi);

        VerticalPanel vpm = new VerticalPanel();
        vpm.add(w);
        TabPanel tab = new TabPanel();
        vpm.add(tab);
        tab.add(w1, "Firma", true);
        tab.add(w2, "Osoba", true);
        FormField eC = FormUtil.findI(model.getfList(), new VField(
                CustomerP.F.cType));
        eC.getELine().addChangeListener(new CList(tab));
        changeC(tab, eC.getELine());

        return vpm;
    }
}
