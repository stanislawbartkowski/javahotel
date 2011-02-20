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
package com.javahotel.nmvc.factories.booking;

import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IChangeObject;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.util.YesNoDialog;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.types.LId;

public class BookingCustomerContainer extends AbstractSlotContainer {

    private final ISlotMediator slMediator;
    private final IDataModelFactory daFactory;
    private final IResLocator rI;
    private final IEditChooseRecordContainer cContainer;

    private void drawCust(IVModelData cust) {
        slMediator.getSlContainer().publish(
                DataActionEnum.DrawViewComposeFormAction, dType, cust);
    }

    private class SetCustomerData implements RData.IOneList<AbstractTo> {

        @Override
        public void doOne(AbstractTo val) {
            IVModelData cData = new VModelData(val);
            drawCust(cData);
        }
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            BookingP b = getBook(slContext);
            LId custI = b.getCustomer();
            if (custI == null) {
                IVModelData cust = daFactory.construct(dType);
                drawCust(cust);
                cContainer.SetNewChange(true, true);
                return;
            }
            CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
                    custI);
            rI.getR().getOne(RType.ListDict, pa, new SetCustomerData());
        }
    }

    private BookingP getBook(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        VModelData vData = (VModelData) mData;
        BookingP b = (BookingP) vData.getA();
        return b;
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;

            IVModelData cust = daFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(GetActionEnum.GetViewModelEdited,
                            dType, cust);
            boolean addCust = cContainer.getNewCheck();
            boolean changeCust = cContainer.getChangeCheck();
            VModelData vvData = (VModelData) pData;
            BookingCustInfo bInfo = new BookingCustInfo(addCust, changeCust,
                    (CustomerP) vvData.getA());
            vData.setCustomData(bInfo);
            return slContext;
        }
    }

    private class ReceiveChange implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IChangeObject i = (IChangeObject) slContext.getCustom();
            LogT.getLS().info(
                    LogT.getT().receivedSignalLogParam(
                            slContext.getSlType().toString(), i.toString()));
            String mess = null;
            String ask = null;
            final boolean newset;
            final boolean changeset;
            if (i.getWhat() == IChangeObject.NEW) {
                newset = !i.getSet();
                changeset = cContainer.getChangeCheck();
                if (i.getSet()) {
                    ask = rI.getLabels().NextCustomerSymbol();
                } else {
                    mess = rI.getLabels().CannotChangeNoNewCustomer();
                }
            } else {
                newset = cContainer.getNewCheck();
                changeset = !i.getSet();
                if (i.getSet()) {
                    ask = rI.getLabels().CustomerDataWillBeChanged();
                } else {
                    mess = rI.getLabels().CannotChangeCustomerToNotChange();
                }
            }

            if (mess != null) {
                cContainer.SetNewChange(newset, changeset);
                OkDialog ok = new OkDialog(mess, null);
                ok.show(i.getW().getGWidget());
            }
            if (ask != null) {
                IClickYesNo c = new IClickYesNo() {

                    @Override
                    public void click(boolean yes) {
                        if (!yes) {
                            cContainer.SetNewChange(newset, changeset);
                        } else {
                            cContainer.ModifForm();
                        }

                    }
                };
                YesNoDialog y = new YesNoDialog(ask + " "
                        + rI.getLabels().Confirm(), c);
                y.show(i.getW().getGWidget());
            }
        }
    }

    public BookingCustomerContainer(ICallContext iContext, IDataType subType) {
        dType = new DataType(DictType.CustomerList);
        ICallContext ii = iContext.construct(dType);
        ii.setiSlo(this);
        cContainer = EditChooseRecordFactory.constructEditChooseRecord(ii,
                iContext.getDType(), subType);
        cContainer.getSlContainer().registerSubscriber(
                IChangeObject.signalString, new ReceiveChange());
        TablesFactories tFactories = iContext.getT();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();
        slMediator = tFactories.getSlotMediatorFactory().construct();
        rI = HInjector.getI().getI();

        slMediator.registerSlotContainer(cContainer);
        registerSubscriber(DataActionEnum.DrawViewFormAction, subType,
                new DrawModel());

        registerCaller(GetActionEnum.GetViewModelEdited, subType,
                new SetGetter());

    }

    @Override
    public void startPublish(CellId cellId) {
        cContainer.startPublish(cellId);
    }
}
