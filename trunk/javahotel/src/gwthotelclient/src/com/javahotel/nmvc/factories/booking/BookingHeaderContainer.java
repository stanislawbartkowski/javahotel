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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.types.AddType;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.common.util.GetMaxUtil;

public class BookingHeaderContainer extends AbstractSlotContainer {

    private final DataType aType;
    private final ISlotMediator slMediator;
    private final IDataModelFactory daFactory;
    private final IDataType publishdType;
    private final CellId cId = new CellId(IPanelView.CUSTOMID);
    private final CellId aId = new CellId(IPanelView.CUSTOMID + 1);
    private final SetVPanelGwt sPanel = new SetVPanelGwt();
    private final SlotSignalContextFactory sFactory;

    private void drawBook(IDataType dType, IVModelData book) {
        slMediator.getSlContainer().publish(
                DataActionEnum.DrawViewComposeFormAction, dType, book);
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            HModelData vData = (HModelData) mData;
            BookingP b = (BookingP) vData.getA();
            BookRecordP p = null;
            if (b.getBookrecords() != null) {
                p = GetMaxUtil.getLastBookRecord(b);
            }
            IVModelData pData;
            if (p == null) {
                pData = daFactory.construct(dType);
            } else {
                pData = VModelDataFactory.construct(p);
            }
            drawBook(dType, pData);

            AdvancePaymentP pa = null;
            if (b.getBill() != null) {
                pa = GetMaxUtil.getLastValidationRecord(b);
            }
            IVModelData aData;
            if (pa != null) {
                aData = VModelDataFactory.construct(pa);
            } else {
                aData = daFactory.construct(aType);
            }
            drawBook(aType, aData);
        }
    }

    private void register(ISlotable book, ISlotable aHeader) {
        book.getSlContainer().registerSubscriber(dType, cId,
                sPanel.constructSetGwt());
        aHeader.getSlContainer().registerSubscriber(aType, aId,
                sPanel.constructSetGwt());
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookingP b = getBook(slContext);

            IVModelData vData = daFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(GetActionEnum.GetViewModelEdited,
                            dType, vData);
            HModelData vv = (HModelData) pData;
            BookRecordP p = (BookRecordP) vv.getA();
            List<BookRecordP> l = new ArrayList<BookRecordP>();
            l.add(p);
            b.setBookrecords(l);

            vData = daFactory.construct(aType);
            pData = slMediator.getSlContainer().getGetterIVModelData(
                    GetActionEnum.GetViewModelEdited, aType, vData);
            vv = (HModelData) pData;
            AdvancePaymentP a = (AdvancePaymentP) vv.getA();
            List<AdvancePaymentP> ll = new ArrayList<AdvancePaymentP>();
            ll.add(a);

            List<BillP> bL = new ArrayList<BillP>();
            BillP bb = BillUtil.createPaymentBill();
            bb.setAdvancePay(ll);
            bL.add(bb);
            b.setBill(bL);
            return slContext;
        }
    }

    private class GetSlot implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            GetSlowC g = new GetSlowC(slMediator);
            return sFactory.construct(slContext.getSlType(), g);
        }

    }

    private BookingP getBook(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        HModelData vData = (HModelData) mData;
        BookingP b = (BookingP) vData.getA();
        return b;
    }

    public BookingHeaderContainer(ICallContext iContext, IDataType subType) {
        publishdType = iContext.getDType();
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        sFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();
        slMediator = tFactories.getSlotMediatorFactory().construct();

        dType = new DataType(AddType.BookRecord);
        aType = new DataType(AddType.AdvanceHeader);
        IGetViewControllerFactory fa = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetViewControllerFactory();
        IComposeController book = fa.construct(iContext.construct(dType));
        book.createComposeControle(cId);
        IComposeController aHeader = fa.construct(iContext.construct(aType));
        aHeader.createComposeControle(aId);
        register(book, aHeader);
        slMediator.registerSlotContainer(cId, book);
        slMediator.registerSlotContainer(aId, aHeader);
        registerSubscriber(DataActionEnum.DrawViewFormAction, subType,
                new DrawModel());
        registerCaller(GetActionEnum.GetViewModelEdited, subType,
                new SetGetter());
        registerCaller(GetSlowC.GETSLOTS, new GetSlot());
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(publishdType, cellId, sPanel.constructGWidget());
        slMediator.startPublish(null);
    }
}