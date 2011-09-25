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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IChangeObject;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.types.LId;

public class BookingCustomerContainer extends AbstractSlotContainer {

    private final ISlotMediator slMediator;
    private final IDataModelFactory daFactory;
    private final IResLocator rI;
    private final IEditChooseRecordContainer cContainer;
    /* Initial CustomerP being displayed. */
    private IVModelData custP = null;

    private void drawCust() {
        slMediator.getSlContainer().publish(dType,
                DataActionEnum.DrawViewComposeFormAction, custP);
    }

    private class SetCustomerData implements RData.IOneList<CustomerP> {

        @Override
        public void doOne(CustomerP val) {
            custP = VModelDataFactory.construct(val);
            drawCust();
        }
    }

    private class DrawModel implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            BookingP b = getBook(slContext);
            LId custI = b.getCustomer();
            if (custI == null) {
                custP = daFactory.construct(dType);
                drawCust();
                cContainer.SetNewChange(true, true);
                return;
            }
            CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
                    custI);
            rI.getR().getOne(RType.ListDict, pa, new SetCustomerData());
        }
    }

    private BookingP getBook(ISlotSignalContext slContext) {
        BookingP b = DataUtil.getData(slContext);
        return b;
    }

    private class SetGetter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            HModelData vData = (HModelData) mData;

            // Use initial CustomerP modified by data being displayed 
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(dType,
                            GetActionEnum.GetViewModelEdited, custP);
            CustomerP custP = DataUtil.getData(pData);
            BookingCustInfo bInfo = new BookingCustInfo(cContainer,
                    custP);
            vData.setCustomData(bInfo);
            return slContext;
        }
    }

    class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            cContainer.ChangeViewForm(slContext.getPersistType());
            slMediator.getSlContainer().publish(dType,
                    DataActionEnum.ChangeViewComposeFormModeAction,
                    slContext.getPersistType());
        }

    }

    public BookingCustomerContainer(ICallContext iContext, IDataType subType) {
        dType = new DataType(DictType.CustomerList);
        ICallContext ii = iContext.construct(dType);
        ii.setiSlo(this);
        cContainer = EditChooseRecordFactory.constructEditChooseRecord(ii,
                iContext.getDType(), subType);
        cContainer.getSlContainer().registerSubscriber(
                IChangeObject.signalString, new ReceiveChange(cContainer));
        TablesFactories tFactories = iContext.getT();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();
        slMediator = tFactories.getSlotMediatorFactory().construct();
        rI = HInjector.getI().getI();

        slMediator.registerSlotContainer(cContainer);
        registerSubscriber(subType, DataActionEnum.DrawViewFormAction,
                new DrawModel());
        registerSubscriber(subType, DataActionEnum.ChangeViewFormModeAction,
                new ChangeMode());

        registerCaller(subType, GetActionEnum.GetViewModelEdited,
                new SetGetter());
        registerCaller(subType, GetActionEnum.GetModelToPersist,
                new SetGetter());

    }

    @Override
    public void startPublish(CellId cellId) {
        cContainer.startPublish(cellId);
    }
}
