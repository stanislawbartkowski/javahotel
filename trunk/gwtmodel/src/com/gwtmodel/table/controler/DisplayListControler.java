/*
 f * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.factories.IHeaderListContainer;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.*;

class DisplayListControler extends AbstractSlotMediatorContainer implements
        IDataControler {

    private final CellId cellTableId;
    private final CellId controlId;
    private final boolean startM;
    private final DisplayListControlerParam cParam;

    private class GetHTML implements ISlotCallerListener {

        private String htmlButton;

        GetHTML(String htmlButton) {
            this.htmlButton = htmlButton;
        }

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            HTMLPanel ha = new HTMLPanel("div", htmlButton);
            return slContextFactory.construct(slContext.getSlType(),
                    new GWidget(ha));
        }
    }

    DisplayListControler(DisplayListControlerParam cParam) {
        this.cParam = cParam;
        IDataPersistListAction persistA = cParam.getListParam().getPersistA();
        IHeaderListContainer heList = cParam.getListParam().getHeList();
        // create panel View
        IPanelView pView = pViewFactory.construct(cParam.getdType(),
                cParam.getPanelId());
        controlId = pView.addCellPanel(0, 0);
        cellTableId = pView.addCellPanel(1, 0);
        pView.createView();
        // persist layer
        // header list
        ListDataViewFactory lDataFactory = cParam.gettFactories()
                .getlDataFactory();
        IListDataView daView = lDataFactory.construct(cParam.getdType(),
                cParam.getGetCell(), true, false, cParam.isTreeView());
        ControlButtonViewFactory bFactory = cParam.gettFactories()
                .getbViewFactory();
        IControlButtonView bView = bFactory.construct(cParam.getdType(),
                cParam.getListButton());
        if (cParam.getMe() == null) {
            startM = true;
        } else {
            slMediator = cParam.getMe();
            startM = false;
        }
        slMediator.registerSlotContainer(cParam.getPanelId(), pView);
        if (persistA != null) {
            slMediator.registerSlotContainer(-1, persistA);
        }
        slMediator.registerSlotContainer(cellTableId, daView);
        slMediator.registerSlotContainer(controlId, bView);
        if (cParam.getcControler() != null) {
            slMediator.registerSlotContainer(-1, cParam.getcControler());
        }
        if (heList != null) {
            slMediator.registerSlotContainer(-1, heList);
        }
        if (cParam.getListButton().getHtmlFormat() != null) {
            ISlotCallerListener l = new GetHTML(cParam.getListButton()
                    .getHtmlFormat());
            SlotType slType = slTypeFactory.constructH(controlId);
            slMediator.getSlContainer().registerCaller(slType, l);
        }
    }

    private class DrawListAction implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            slMediator.getSlContainer().publish(cParam.getdType(),
                    DataActionEnum.DrawListAction, slContext.getDataList(),
                    cParam.getwSize());
        }
    }

    @Override
    public void startPublish(CellId cellId) {
        if (startM) {
            slMediator.startPublish(cellId);
        }
        slMediator.getSlContainer().registerSubscriber(cParam.getdType(),
                DataActionEnum.ListReadSuccessSignal, new DrawListAction());
        slMediator
                .getSlContainer()
                .registerRedirector(
                        cParam.gettFactories()
                                .getSlTypeFactory()
                                .construct(
                                        cParam.getdType(),
                                        DataActionEnum.RefreshAfterPersistActionSignal),
                        cParam.gettFactories()
                                .getSlTypeFactory()
                                .construct(cParam.getdType(),
                                        DataActionEnum.ReadListAction));

        // secondly publish
        slMediator.getSlContainer().publish(cParam.getdType(),
                DataActionEnum.ReadListAction, cParam.getwSize());
        slMediator.getSlContainer().publish(cParam.getdType(),
                DataActionEnum.ReadHeaderContainer);
    }
}
