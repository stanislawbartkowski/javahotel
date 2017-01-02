/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.tabpanelview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.smessage.IGetStandardMessage;

class TabPanelView extends AbstractSlotContainer implements ITabPanelView {

    private final TabPanel tPanel = new TabPanel();
    private final List<TabElem> tList = new ArrayList<TabElem>();
    private CellId panelId;
    private final String tabId;
    private final IGetStandardMessage iMess = GwtGiniInjector.getI()
            .getStandardMessage();

    class ClickBefore implements BeforeSelectionHandler<java.lang.Integer> {

        @Override
        public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
            CustomStringSlot sl = BeforeTabChange
                    .constructBeforeTabChangeSignal(dType);
            BeforeTabChange cu = new BeforeTabChange(event.getItem(), tabId);
            publish(sl, cu);
        }

    }

    private class TabElem implements Comparable {
        private final int orderNo;
        private final CellId id;
        private final String tabName;
        private final String htmlPanel;
        private Widget w = null;

        TabElem(int orderNo, CellId id, String tabName, String htmlPanel) {
            this.id = id;
            this.orderNo = orderNo;
            this.tabName = tabName;
            this.htmlPanel = htmlPanel;
        }

        @Override
        public int compareTo(Object o) {
            TabElem e = (TabElem) o;
            if (orderNo < e.orderNo)
                return -1;
            if (orderNo == e.orderNo)
                return 0;
            return 1;
        }
    }

    TabPanelView(IDataType dType, CellId id, String tabId) {
        this.dType = dType;
        this.panelId = id;
        tPanel.addBeforeSelectionHandler(new ClickBefore());
        this.tabId = tabId;
    }

    private class SList extends SynchronizeList {

        protected SList(int noSync) {
            super(noSync);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doTask() {
            Collections.sort(tList);
            for (TabElem e : tList)
                tPanel.add(e.w, iMess.getMessage(e.tabName));

            tPanel.selectTab(0);
        }

    }

    private class SetWidget implements ISlotListener {

        private final SList sLi;

        SetWidget(SList sLi) {
            this.sLi = sLi;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            CellId cellId = slContext.getSlType().getCellId();
            assert cellId != null : LogT.getT().CellCannotBeNull();
            IGWidget gwtWidget = slContext.getGwtWidget();
            for (TabElem e : tList)
                // compare references (not content)
                if (e.id == cellId) {
                    e.w = gwtWidget.getGWidget();
                    sLi.signalDone();
                }
        }
    }

    @Override
    public CellId addPanel(int orderNo, String tabName, String htmlpanel) {
        panelId = panelId.constructNext(dType);
        tList.add(new TabElem(orderNo, panelId, tabName, htmlpanel));
        return panelId;
    }

    @Override
    public void createView() {
        SList sLi = new SList(tList.size());
        for (TabElem e : tList)
            registerSubscriber(dType, e.id, new SetWidget(sLi));

    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, new GWidget(tPanel));
    }

}
