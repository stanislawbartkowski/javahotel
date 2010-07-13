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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VModelData;
import com.javahotel.types.LId;

public class BookingCustomerContainer extends AbstractSlotContainer {

    private final VerticalPanel vp = new VerticalPanel();
    private final Sych sy = new Sych();
    private final ISlotMediator slMediator;
    private final DataType custType;
    private final IDataModelFactory daFactory;
    private final IResLocator rI;
    private final AddChangeBox cBox = new AddChangeBox();
    private final static String CHANGE_BUTTON = "HOTEL-CHANGE-BUTTON";
    private final static String LIST_BUTTON = "HOTEL-LIST-BUTTON";

    private class Sych extends SynchronizeList {

        CellId cellId;
        Widget but;
        Widget cust;

        Sych() {
            super(3);
        }

        @Override
        protected void doTask() {
            HorizontalPanel hp = new HorizontalPanel();
            hp.add(cBox);
            hp.add(but);
            vp.add(hp);
            vp.add(cust);
            publish(cellId, new GWidget(vp));
        }

    }

    private class SetWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.but = gwtWidget.getGWidget();
            sy.signalDone();
        }

    }

    private class SetWidgetCust implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IGWidget gwtWidget = slContext.getGwtWidget();
            sy.cust = gwtWidget.getGWidget();
            sy.signalDone();
        }

    }

    private void drawCust(IVModelData cust) {
        slMediator.getSlContainer().publish(
                DataActionEnum.DrawViewComposeFormAction, custType, cust);
    }

    private class SetCustomerData implements RData.IOneList<AbstractTo> {

        public void doOne(AbstractTo val) {
            IVModelData cData = new VModelData(val);
            drawCust(cData);
        }
    }

    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            VModelData vData = (VModelData) mData;
            BookingP b = (BookingP) vData.getA();
            LId custI = b.getCustomer();
            if (custI == null) {
                IVModelData cust = daFactory.construct(custType);
                drawCust(cust);
                cBox.setChangeCheck(true);
                cBox.setNewCheck(true);
                return;
            }
            CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
                    custI);
            rI.getR().getOne(RType.ListDict, pa, new SetCustomerData());
        }
    }

    private void register(ISlotable pView, ISlotable cust) {
        pView.getSlContainer().registerSubscriber(IPanelView.CUSTOMID,
                new SetWidget());
        cust.getSlContainer().registerSubscriber(IPanelView.CUSTOMID + 1,
                new SetWidgetCust());
    }

    public BookingCustomerContainer(DataType subType) {
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        ITableCustomFactories fContainer = GwtGiniInjector.getI()
                .getTableFactoriesContainer();
        daFactory = fContainer.getDataModelFactory();
        slMediator = tFactories.getSlotMediatorFactory().construct();
        rI = HInjector.getI().getI();

        ClickButtonType sChange = new ClickButtonType(CHANGE_BUTTON);
        ClickButtonType sChoose = new ClickButtonType(LIST_BUTTON);
        ControlButtonDesc bChange = new ControlButtonDesc("Zmień dane", sChange);
        ControlButtonDesc bChoose = new ControlButtonDesc("Wybierz z listy",
                sChoose);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bChange);
        bList.add(bChoose);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        ControlButtonViewFactory bFactory = GwtGiniInjector.getI()
                .getControlButtonViewFactory();
        IControlButtonView bView = bFactory.construct(cList);
        IGetViewControllerFactory fa = GwtGiniInjector.getI()
                .getTableFactoriesContainer().getGetViewControllerFactory();
        custType = new DataType(DictType.CustomerList);
        CellId bId = new CellId(IPanelView.CUSTOMID);
        CellId cId = new CellId(IPanelView.CUSTOMID + 1);
        IComposeController cust = fa.construct(custType);
        cust.createComposeControle(cId);

        register(bView, cust);
        slMediator.registerSlotContainer(cId, cust);
        slMediator.registerSlotContainer(bId, bView);

        registerSubscriber(DataActionEnum.DrawViewFormAction, subType,
                new DrawModel());

    }

    public void startPublish(CellId cellId) {
        sy.cellId = cellId;
        sy.signalDone();
        slMediator.startPublish(null);
    }

}
