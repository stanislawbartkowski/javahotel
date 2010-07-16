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
package com.javahotel.nmvc.factories.booking;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
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
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.nmvc.common.AddType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VModelData;

public class BookingElemContainer extends AbstractSlotContainer {

    private final DataType subType;
    private final DataType dType;
    private final DataType aType;
    private final ISlotMediator slMediator;
    private final IDataModelFactory daFactory;
    private final IResLocator rI;
    private final VerticalPanel vp = new VerticalPanel();
    private final Sych sy = new Sych();

    private class Sych extends SynchronizeList {

        CellId cellId;
        Widget hW;
        Widget aW;

        Sych() {
            super(3);
        }

        @Override
        protected void doTask() {
            vp.add(hW);
            vp.add(aW);
            publish(cellId, new GWidget(vp));
        }

    }

    private class SetWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            Widget w = gwtWidget.getGWidget();
            sy.aW = w;
            sy.signalDone();
        }
    }

    private class SetWidgetHeader implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            Widget w = gwtWidget.getGWidget();
            sy.hW = w;
            sy.signalDone();
        }
    }

    private void drawBook(DataType dType, IVModelData book) {
        slMediator.getSlContainer().publish(
                DataActionEnum.DrawViewComposeFormAction, dType, book);
    }

    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;
            BookingP b = (BookingP) vData.getA();
            BookRecordP p = null;
            if (b.getBookrecords() != null) {
                p = GetMaxUtil.getLastBookRecord(b);
            }
            IVModelData pData;
            if (p == null) {
                pData = daFactory.construct(dType);
            } else {
                pData = new VModelData(p);
            }
            drawBook(dType, pData);

            AdvancePaymentP pa = null;
            if (b.getBill() != null) {
                pa = GetMaxUtil.getLastValidationRecord(b);
            }
            IVModelData aData;
            if (pa != null) {
                aData = new VModelData(pa);
            } else {
                aData = daFactory.construct(aType);
            }
            drawBook(aType, aData);
        }
    }

    private void register(ISlotable book, ISlotable aHeader) {
        book.getSlContainer().registerSubscriber(IPanelView.CUSTOMID,
                new SetWidget());
        aHeader.getSlContainer().registerSubscriber(IPanelView.CUSTOMID + 1,
                new SetWidgetHeader());
    }
    
    private class SetGetter implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            BookingP b = getBook(slContext);
            
            IVModelData vData = daFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer().getGetterIVModelData(GetActionEnum.GetViewModelEdited, dType, vData);
            VModelData vv = (VModelData) pData;
            BookRecordP p = (BookRecordP) vv.getA();
            List<BookRecordP> l = new ArrayList<BookRecordP>();
            l.add(p);
            b.setBookrecords(l);

            vData = daFactory.construct(aType);
            pData = slMediator.getSlContainer().getGetterIVModelData(GetActionEnum.GetViewModelEdited, aType, vData);
            vv = (VModelData) pData;
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
    
    private BookingP getBook(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        VModelData vData = (VModelData) mData;
        BookingP b = (BookingP) vData.getA();
        return b;
    }
    
    public BookingElemContainer(DataType subType) {
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();
        slMediator = tFactories.getSlotMediatorFactory().construct();
        rI = HInjector.getI().getI();

        dType = new DataType(AddType.BookRecord);
        aType = new DataType(AddType.AdvanceHeader);
        this.subType = subType;
        CellId cId = new CellId(IPanelView.CUSTOMID);
        CellId aId = new CellId(IPanelView.CUSTOMID + 1);
        IGetViewControllerFactory fa = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetViewControllerFactory();
        IComposeController book = fa.construct(dType);
        book.createComposeControle(cId);
        IComposeController aHeader = fa.construct(aType);
        aHeader.createComposeControle(aId);
        register(book, aHeader);
        slMediator.registerSlotContainer(cId, book);
        slMediator.registerSlotContainer(aId, aHeader);
        registerSubscriber(DataActionEnum.DrawViewFormAction, subType,
                new DrawModel());
        registerCaller(GetActionEnum.GetViewModelEdited, subType,
                new SetGetter());
    }

    public void startPublish(CellId cellId) {
        sy.cellId = cellId;
        sy.signalDone();
        slMediator.startPublish(null);
    }
}