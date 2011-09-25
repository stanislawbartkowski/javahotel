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
package com.javahotel.nmvc.factories.customer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IDataFormConstructor;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.view.util.CreateFormView;
import com.gwtmodel.table.view.util.FormUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;

public class CustomerForm implements IDataFormConstructor {

    private final IResLocator rI;
    private final TabPanel tab = new TabPanel();

    public CustomerForm() {
        rI = HInjector.getI().getI();
    }

    private void setTab(Object e) {
        if (e == null) {
            tab.selectTab(0);
            return;
        }
        CustomerType cType = (CustomerType) e;
        if (cType == CustomerType.Person) {
            tab.selectTab(1);
        } else {
            tab.selectTab(0);
        }
    }

    private void changeC(IFormLineView i) {
        setTab(i.getValObj());
    }

    private class CList implements IFormChangeListener {

        @Override
        public void onChange(IFormLineView i, boolean afterFocus) {
            changeC(i);
        }
    }

    private class GetGWT implements ISlotListener {

        private final String tabName;

        GetGWT(String tabName) {
            this.tabName = tabName;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            LogT.getLS().info(
                    LogT.getT().receivedSignalLog(
                            slContext.getSlType().toString()));
            IGWidget g = slContext.getGwtWidget();
            assert g != null : LogT.getT().cannotBeNull();
            tab.add(g.getGWidget(), tabName);
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
        DataUtil.addToSet(fi, model.getfList(), CustomerP.F.mailAddress);
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
        vpm.add(tab);
        Map<String, String> ma = rI.getLabels().CustomerType();
        tab.add(w1, ma.get(CustomerType.Company.toString()), true);
        tab.add(w2, ma.get(CustomerType.Person.toString()), true);
        FormField eC = FormUtil.findI(model.getfList(), new VField(
                CustomerP.F.cType));
        eC.getELine().addChangeListener(new CList());
        changeC(eC.getELine());

        iContext.iSlo()
                .getSlContainer()
                .registerSubscriber(CustomerAddInfo.setAccString,
                        new GetGWT("Konta"));
        iContext.iSlo()
                .getSlContainer()
                .registerSubscriber(CustomerAddInfo.setTelString,
                        new GetGWT("Telefony"));

        return vpm;
    }
}
