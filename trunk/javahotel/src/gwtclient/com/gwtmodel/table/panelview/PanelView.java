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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.panel.GwtPanelViewFactory;
import com.gwtmodel.table.view.panel.IGwtPanelView;
import com.gwtmodel.table.view.panel.PanelRowDesc;
import com.javahotel.common.util.MaxI;

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

    private final Map<Integer, PanelRowCell> colM = new HashMap<Integer, PanelRowCell>();
    private int nextToUse;
    private IGwtPanelView pView;

    PanelView(int panelCellId, int firstToUse) {
        this.nextToUse = firstToUse;
        // create publisher
        createCallBackWidget(panelCellId);
    }

    public int addCellPanel(int row, int col) {
        PanelRowCell pa = new PanelRowCell(row, col);
        int r = nextToUse;
        colM.put(nextToUse++, pa);
        return r;
    }

    private class SetWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            int cellId = slContext.getSlType().getCellId();
            PanelRowCell pa = colM.get(cellId);
            IGWidget gwtWidget = slContext.getGwtWidget();
            pView.setWidget(pa.rowNo, pa.cellNo, gwtWidget.getWidget());
        }

    }

    public void createView() {
        List<PanelRowDesc> rowDesc = new ArrayList<PanelRowDesc>();
        int maxR = 0;
        for (Integer i : colM.keySet()) {
            PanelRowCell ro = colM.get(i);
            maxR = MaxI.max(maxR, ro.getRowNo());
        }
        for (int i = 0; i <= maxR; i++) {
            int col = 0;
            for (Integer ii : colM.keySet()) {
                PanelRowCell ro = colM.get(ii);
                if (ro.getRowNo() == i) {
                    col = MaxI.max(col, ro.getCellNo());
                }
                rowDesc.add(new PanelRowDesc(col+1));
            }
        }
        pView = GwtPanelViewFactory.construct(rowDesc);
        // create subscribers
        for (Integer ii : colM.keySet()) {
            // PanelRowCell ro = colM.get(ii);
            SlotType slType = SlotTypeFactory.constructCallBackWidget(ii);
            slContainer.addSubscriber(slType, new SetWidget());
        }
        // create publisher
    }

    public void startPublish() {
        publishCallBack(pView);
    }

}
