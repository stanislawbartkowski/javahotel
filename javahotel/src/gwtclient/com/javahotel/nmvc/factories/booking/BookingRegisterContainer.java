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

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
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
import com.javahotel.common.command.DictType;
import com.javahotel.nmvc.common.DataType;

public class BookingRegisterContainer extends AbstractSlotContainer {

    private class CheckButt extends Composite {

        private final VerticalPanel hp = new VerticalPanel();
        private final CheckBox addNew = new CheckBox("Dodaj nowego");
        private final CheckBox changeD = new CheckBox("Zmień dane");

        CheckButt() {
            initWidget(hp);
            hp.add(addNew);
            hp.add(changeD);
        }

    }

    private final VerticalPanel vp = new VerticalPanel();
    private final Sych sy = new Sych();
    private final ISlotMediator slMediator;
    private final DataType custType;
    

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
            hp.add(new CheckButt());
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
    
    private class DrawModel implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            slMediator.getSlContainer().publish(DataActionEnum.DrawViewComposeFormAction,custType);
        }

        
    }

    private void register(ISlotable pView, ISlotable cust) {
        pView.getSlContainer().registerSubscriber(IPanelView.CUSTOMID,
                new SetWidget());
        cust.getSlContainer().registerSubscriber(IPanelView.CUSTOMID + 1,
                new SetWidgetCust());
    }

    public BookingRegisterContainer(DataType subType) {
        TablesFactories tFactories = GwtGiniInjector.getI().getTablesFactories();
        slMediator = tFactories.getSlotMediatorFactory().construct();

        ClickButtonType sChange = new ClickButtonType(0);
        ClickButtonType sChoose = new ClickButtonType(1);
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
