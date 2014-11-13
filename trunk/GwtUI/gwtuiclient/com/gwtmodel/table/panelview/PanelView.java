/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.common.MaxI;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.*;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.panel.IGwtPanelView;
import com.gwtmodel.table.view.util.CreateFormView;
import java.util.HashMap;
import java.util.Map;

class PanelView extends AbstractSlotContainer implements IPanelView {

    private class PanelRowCell {

        private final int rowNo, cellNo;
        private final String cellId;

        PanelRowCell(int rowNo, int cellNo, String cellId) {
            this.rowNo = rowNo;
            this.cellNo = cellNo;
            this.cellId = cellId;
        }

        int getRowNo() {
            return rowNo;
        }

        int getCellNo() {
            return cellNo;
        }

        /**
         * @return the cellId
         */
        String getCellId() {
            return cellId;
        }
    }

    private final Map<CellId, PanelRowCell> colM = new HashMap<CellId, PanelRowCell>();
    private CellId panelId;
    private IGwtPanelView pView;
    private final GwtPanelViewFactory gFactory;
    private HTMLPanel htmlWidget;
    private final SlotSignalContextFactory slFactory;

    PanelView(GwtPanelViewFactory gFactory, SlotSignalContextFactory slFactory,
            IDataType dType, CellId panelId) {
        assert dType != null : LogT.getT().dTypeCannotBeNull();
        this.panelId = panelId;
        this.gFactory = gFactory;
        this.slFactory = slFactory;
        this.dType = dType;
        htmlWidget = null;
        pView = null;
    }

    @Override
    public CellId addCellPanel(int row, int col) {
        return addCellPanel(null, row, col, null);
    }

    @Override
    public CellId addCellPanel(int row, int col, String cellId) {
        return addCellPanel(null, row, col, cellId);
    }

    @Override
    public CellId addCellPanel(IDataType publishType, int row, int col,
            String cellId) {
        PanelRowCell pa = new PanelRowCell(row, col, cellId);
        CellId nextId = panelId.constructNext(publishType);
        panelId = nextId;
        colM.put(nextId, pa);
        return nextId;
    }

    @Override
    public CellId addCellPanel(IDataType publishType, int row, int col) {
        // return addCellPanel(null, row, col, null);
        // 2013/01/15 -- replace publishType (null previously)
        return addCellPanel(publishType, row, col, null);
    }

    private class SetWidget implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            CellId cellId = slContext.getSlType().getCellId();
            assert cellId != null : LogT.getT().CellCannotBeNull();
            PanelRowCell pa = colM.get(cellId);
            // 2012/01/15 introduced to allow several panels
            if (pa == null) {
                return;
            }
            assert pa != null : LogT.getT().CellShouldBeRegistered();
            IGWidget gwtWidget = slContext.getGwtWidget();
            String id = pa.getCellId();
            if (htmlWidget == null) {
                pView.setWidget(pa.rowNo, pa.cellNo, gwtWidget.getGWidget());
            } else {
                if (id != null) {
                    CreateFormView.replace(htmlWidget, id,
                            gwtWidget.getGWidget());
                }
            }
            if (id != null) {
                CustomStringSlot cSlot = SendPanelElemSignal
                        .constructSlotSendPanelElem(dType);
                SendPanelElemSignal sl = new SendPanelElemSignal(id, gwtWidget);
                publish(cSlot, sl);
            }
        }
    }

    private class GetMainHtml implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ISlotSignalContext sl = slFactory.construct(slContext.getSlType(),
                    new GWidget(htmlWidget));
            return sl;
        }
    }

    @Override
    public void createView() {
        createView(null);
    }

    @Override
    public void createView(String html) {
        ISlotCallerListener c = null;
        if (html == null) {
            int maxR = 0;
            int maxC = 0;
            for (CellId i : colM.keySet()) {
                PanelRowCell ro = colM.get(i);
                maxR = MaxI.max(maxR, ro.getRowNo());
                maxC = MaxI.max(maxC, ro.getCellNo());
            }
            pView = gFactory.construct(maxR + 1, maxC + 1);
        } else {
            htmlWidget = new HTMLPanel(html);
            c = new GetMainHtml();
            SlotType sl = slTypeFactory.constructMainH();
            registerCaller(sl, c);
        }

        for (CellId ii : colM.keySet()) {
            registerSubscriber(dType, ii, new SetWidget());
        }
        // create publisher
    }

    @Override
    public void startPublish(CellId cellId) {
        IDataType publishType;
        if (cellId.getdType() == null) {
            publishType = dType;
        } else {
            publishType = cellId.getdType();
        }
        if (htmlWidget != null) {
            publish(publishType, cellId, new GWidget(htmlWidget));
        } else {
            publish(publishType, cellId, pView);
        }
    }
}
