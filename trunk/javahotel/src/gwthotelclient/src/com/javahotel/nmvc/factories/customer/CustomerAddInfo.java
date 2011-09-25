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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.stringlist.AbstractStringE;
import com.gwtmodel.table.stringlist.IMemoryStringList;
import com.gwtmodel.table.stringlist.IStringEFactory;
import com.gwtmodel.table.stringlist.MemoryStringTableFactory;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.AbstractToILd;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.PhoneNumberP;

public class CustomerAddInfo extends AbstractSlotContainer {

    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final IMemoryStringList mList;
    private final IMemoryStringList aList;
    private final MemoryStringTableFactory maFactory;
    private final IDataType publishType;
    public static final String setTelString = "CUSTOM-SET-TEL";
    public static final String setAccString = "CUSTOM-SET-ACC";
    private final IResLocator rI;

    private class StringE extends AbstractStringE {

        private final AbstractToILd a;

        StringE(AbstractToILd a, IField fie) {
            this.a = a;
            String s = (String) a.getF(fie);
            this.setF(null, s);
        }

        StringE() {
            a = null;
        }

        @Override
        public List<IVField> getF() {
            return super.createF(maFactory.getStrField());
        }
    }

    private class SFactory implements IStringEFactory {

        @Override
        public AbstractStringE construct(IDataType d) {
            return new StringE();
        }
    }

    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            CustomerP cust = getCust(slContext);
            IFactory<PhoneNumberP> iP = new IFactory<PhoneNumberP>() {

                @Override
                public PhoneNumberP construct() {
                    return new PhoneNumberP();
                }
            };
            List<PhoneNumberP> li = getMList(mList, PhoneNumberP.F.phoneNumber,
                    iP);
            cust.setPhones(li);

            IFactory<BankAccountP> iB = new IFactory<BankAccountP>() {

                @Override
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

    private CustomerP getCust(ISlotSignalContext slContext) {
        CustomerP cust = DataUtil.getData(slContext);
        return cust;
    }

    private void setMList(IMemoryStringList mList,
            List<? extends AbstractToILd> li, IField fie) {
        List<AbstractStringE> sList = new ArrayList<AbstractStringE>();
        if (li != null) {
            for (AbstractToILd a : li) {
                StringE e = new StringE(a, fie);
                sList.add(e);
            }
        }
        IDataListType dList = maFactory.construct(sList);
        mList.setMemTable(dList);
    }

    private interface IFactory<T extends AbstractToILd> {

        T construct();
    }

    private <T extends AbstractToILd> List<T> getMList(IMemoryStringList mList,
            IField fie, IFactory<T> fa) {
        List<T> li = new ArrayList<T>();
        IDataListType dList = mList.getMemTable();
        for (IVModelData mo : dList.getList()) {
            StringE e = (StringE) mo;
            AbstractToILd a = e.a;
            if (a == null) {
                a = fa.construct();
            }
            String s = FUtils.getValueS(mo, maFactory.getStrField());
            a.setF(fie, s);
            li.add((T) a);
        }
        return li;

    }

    private class DrawModel implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            CustomerP cust = getCust(slContext);
            setMList(mList, cust.getPhones(), PhoneNumberP.F.phoneNumber);
            setMList(aList, cust.getAccounts(), BankAccountP.F.accountNumber);
        }
    }

    private class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            mList.getSlContainer().publish(maFactory.getStrType(), slContext);
            aList.getSlContainer().publish(maFactory.getStrType(), slContext);
        }
    }

    public CustomerAddInfo(IDataType publishType, IDataType dType) {
        this(publishType, dType, true);
    }

    private class SetGWT implements ISlotListener {

        private final String stringButt;

        SetGWT(String stringButt) {
            this.stringButt = stringButt;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            publish(stringButt, slContext.getGwtWidget());
        }
    }

    public CustomerAddInfo(IDataType publishType, IDataType dType, boolean setW) {
        rI = HInjector.getI().getI();
        this.publishType = publishType;
        this.dType = dType;
        maFactory = GwtGiniInjector.getI().getMemoryStringTableFactory();
        if (setW) {
            mList = maFactory.construct("Telefon", rI.getLabels().Phones(),
                    new SFactory(), sPanel.constructSetGwt());
            aList = maFactory.construct("Konto", rI.getLabels().Account(),
                    new SFactory(), sPanel.constructSetGwt());
        } else {
            mList = maFactory.construct("Telefon", rI.getLabels().Phones(),
                    new SFactory(), new SetGWT(setTelString));
            aList = maFactory.construct("Konto", rI.getLabels().Account(),
                    new SFactory(), new SetGWT(setAccString));
        }
        registerCaller(dType, GetActionEnum.GetViewModelEdited, new SetGetter());
        registerCaller(dType, GetActionEnum.GetModelToPersist, new SetGetter());
        registerSubscriber(dType, DataActionEnum.DrawViewFormAction,
                new DrawModel());
        registerSubscriber(dType, DataActionEnum.ChangeViewFormModeAction,
                new ChangeMode());

    }

    @Override
    public void startPublish(CellId cellId) {
        if (cellId != null) {
            publish(publishType, cellId, sPanel.constructGWidget());
        }
    }
}
