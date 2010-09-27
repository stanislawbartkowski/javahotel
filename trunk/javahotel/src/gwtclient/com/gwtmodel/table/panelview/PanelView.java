/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.panelview;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.common.MaxI;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.panel.IGwtPanelView;

class PanelView extends AbstractSlotContainer implements IPanelView {

    private class PanelRowCell {

        private final int rowNo, cellNo;

        PanelRowCell(int rowNo, int cellNo) {
            this.rowNo = rowNo;
            this.cellNo = cellNo;
        }

        public int getRowNo() {
            return rowNo;
        }

        public int getCellNo() {
            return cellNo;
        }
    }

    private final Map<CellId, PanelRowCell> colM = new HashMap<CellId, PanelRowCell>();
    private CellId panelId;
    private IGwtPanelView pView;
    private final GwtPanelViewFactory gFactory;
    private HTMLPanel paHtml;
    private final SlotSignalContextFactory slFactory;

    PanelView(GwtPanelViewFactory gFactory, SlotSignalContextFactory slFactory,
            CellId panelId) {
        this.panelId = panelId;
        this.gFactory = gFactory;
        this.slFactory = slFactory;
        paHtml = null;
        pView = null;
    }

    public CellId addCellPanel(int row, int col) {
        PanelRowCell pa = new PanelRowCell(row, col);
        CellId nextId = panelId.constructNext();
        panelId = nextId;
        colM.put(nextId, pa);
        return nextId;
    }

    private class SetWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            CellId cellId = slContext.getSlType().getCellId();
            PanelRowCell pa = colM.get(cellId);
            IGWidget gwtWidget = slContext.getGwtWidget();
            pView.setWidget(pa.rowNo, pa.cellNo, gwtWidget.getGWidget());
        }
    }

    private IGWidget getHWidget() {
        return new GWidget(paHtml);
    }

    private class GetHtml implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ISlotSignalContext sl = slFactory.construct(slContext.getSlType(),
                    getHWidget());
            return sl;
        }
    }

    public void createView(String html) {
        if (html == null) {
            createView();
            return;
        }
        paHtml = new HTMLPanel(html);
        ISlotCaller c = new GetHtml();
        for (CellId i : colM.keySet()) {
            SlotType sl = slTypeFactory.constructH(i);
            registerCaller(sl, c);
        }
    }

    public void createView() {
        int maxR = 0;
        int maxC = 0;
        for (CellId i : colM.keySet()) {
            PanelRowCell ro = colM.get(i);
            maxR = MaxI.max(maxR, ro.getRowNo());
            maxC = MaxI.max(maxC, ro.getCellNo());
        }
        pView = gFactory.construct(maxR + 1, maxC + 1);
        // create subscribers
        for (CellId ii : colM.keySet()) {
            registerSubscriber(ii, new SetWidget());
        }
        // create publisher
    }

    @Override
    public void startPublish(CellId cellId) {
        if (paHtml != null) {
            publish(cellId, getHWidget());
        } else {
            publish(cellId, pView);
        }
    }
}
