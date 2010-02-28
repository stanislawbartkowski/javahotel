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
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IGetViewControllerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.common.command.DictType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataTypeSubEnum;

public class BookingRegisterContainer {

    private BookingRegisterContainer() {
    }

    private static class CheckButt extends Composite {

        private final VerticalPanel hp = new VerticalPanel();
        private final CheckBox addNew = new CheckBox("Dodaj nowego");
        private final CheckBox changeD = new CheckBox("Zmień dane");

        CheckButt() {
            initWidget(hp);
            hp.add(addNew);
            hp.add(changeD);
        }

    }

    private static class ComposeV extends AbstractSlotContainer {

        private final VerticalPanel vp = new VerticalPanel();
        private final Sych sy = new Sych();

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

        ComposeV(ISlotable pView, ISlotable cust) {
            pView.getSlContainer().registerSubscriber(IPanelView.CUSTOMID,
                    new SetWidget());
            cust.getSlContainer().registerSubscriber(IPanelView.CUSTOMID + 1,
                    new SetWidgetCust());
        }

        public void startPublish(CellId cellId) {
            sy.cellId = cellId;
            sy.signalDone();
        }

    }

    static void registerButtons(IComposeController iCon) {
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
        IComposeController cust = fa.construct(new DataType(
                DictType.CustomerList));

        DataType subType = new DataType(DictType.BookingList,
                DataTypeSubEnum.Sub1);
        ComposeV v = new ComposeV(bView, cust);
        ComposeControllerType cType = new ComposeControllerType(v, subType, 0,
                1);
        ComposeControllerType c1Type = new ComposeControllerType(bView,
                new CellId(IPanelView.CUSTOMID));
        ComposeControllerType c2Type = new ComposeControllerType(cust,
                new CellId(IPanelView.CUSTOMID + 1));
        iCon.registerController(cType);
        iCon.registerController(c1Type);
        iCon.registerController(c2Type);
    }

    public static void register(IComposeController iCon) {
        registerButtons(iCon);

    }

}
