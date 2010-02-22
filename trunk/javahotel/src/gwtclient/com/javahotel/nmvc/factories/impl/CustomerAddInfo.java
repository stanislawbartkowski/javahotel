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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.stringlist.AbstractStringE;
import com.gwtmodel.table.stringlist.IMemoryStringList;
import com.gwtmodel.table.stringlist.IStringEFactory;
import com.gwtmodel.table.stringlist.MemoryStringTableFactory;
import com.javahotel.common.toobject.AbstractToILd;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.nmvc.common.VModelData;

public class CustomerAddInfo extends AbstractSlotContainer implements ISlotable {

    private final VerticalPanel vPanel = new VerticalPanel();
    private final IMemoryStringList mList;
    private final IMemoryStringList aList;
    private final MemoryStringTableFactory maFactory;

    private class StringE extends AbstractStringE {

        private final AbstractToILd a;

        StringE(AbstractToILd a, IField fie) {
            this.a = a;
            String s = a.getS(fie);
            this.setS(null, s);
        }

        StringE() {
            a = null;
        }

        public IVField[] getF() {
            IVField[] e = { Empty.getFieldType() };
            return e;

        }

    }

    private class SFactory implements IStringEFactory {

        public AbstractStringE construct(IDataType d) {
            return new StringE();
        }

    }

    private class SetGetter implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            CustomerP cust = getCust(slContext);
            IFactory<PhoneNumberP> iP = new IFactory<PhoneNumberP>() {

                public PhoneNumberP construct() {
                    return new PhoneNumberP();
                }
            };
            List<PhoneNumberP> li = getMList(mList, PhoneNumberP.F.phoneNumber,
                    iP);
            cust.setPhones(li);

            IFactory<BankAccountP> iB = new IFactory<BankAccountP>() {

                public BankAccountP construct() {
                    return new BankAccountP();
                }
            };
            List<BankAccountP> lib = getMList(aList,
                    BankAccountP.F.accountNumber, iB);
            cust.setAccounts(lib);
            return slContext;
        }
    }

    private class SetGwt implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            vPanel.add(w);
        }
    }

    private CustomerP getCust(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        VModelData vData = (VModelData) mData;
        CustomerP cust = (CustomerP) vData.getA();
        return cust;
    }

    private void setMList(IMemoryStringList mList,
            List<? extends AbstractToILd> li, IField fie) {
        IDataListType dList = maFactory.construct();
        if (li != null) {
            for (AbstractToILd a : li) {
                StringE e = new StringE(a, fie);
                dList.append(e);
            }
        }
        mList.setMemTable(dList);
    }

    private interface IFactory<T extends AbstractToILd> {
        T construct();
    }

    private <T extends AbstractToILd> List<T> getMList(IMemoryStringList mList,
            IField fie, IFactory<T> fa) {
        List<T> li = new ArrayList<T>();
        IDataListType dList = mList.getMemTable();
        for (int i = 0; i < dList.rowNo(); i++) {
            IVModelData mo = dList.getRow(i);
            StringE e = (StringE) mo;
            AbstractToILd a = e.a;
            if (a == null) {
                a = fa.construct();
            }
            String s = e.getS(null);
            a.setF(fie, s);
            li.add((T) a);
        }
        return li;

    }

    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            CustomerP cust = getCust(slContext);
            setMList(mList, cust.getPhones(), PhoneNumberP.F.phoneNumber);
            setMList(aList, cust.getAccounts(), BankAccountP.F.accountNumber);
        }
    }

    public CustomerAddInfo(IDataType dType) {
        maFactory = GwtGiniInjector.getI().getMemoryStringTableFactory();
        mList = maFactory.construct("Telefon", "Telefony", new SFactory(),
                new SetGwt());
        aList = maFactory.construct("Konto", "Konta", new SFactory(),
                new SetGwt());
        registerCaller(GetActionEnum.GetViewModelEdited, dType, new SetGetter());
        registerCaller(GetActionEnum.GetModelToPersist, dType, new SetGetter());
        registerSubscriber(DataActionEnum.DrawViewFormAction, dType,
                new DrawModel());
    }

    public void startPublish(int cellId) {
        publish(cellId, new GWidget(vPanel));
    }

}