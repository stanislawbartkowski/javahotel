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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final VListHeaderContainer heList;
    private DataListType dataList;
    private final IGwtTableView tableView;
    private final IDataType dType;

    private class GwtTableView implements IGwtTableModel {

        public VListHeaderContainer getHeaderList() {
            return heList;
        }

        public IVModelData getRow(int row) {
            return dataList.getRow(row);
        }

        public int getRowsNum() {
            return dataList.getdList().size();
        }
    }

    private class DrawList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            dataList = slContext.getDataList();
            tableView.refresh();
        }
    }

    private class GetListData implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            WChoosedLine w = tableView.getClicked();
            WSize wSize = null;
            IVModelData vData = null;
            if (w.isChoosed()) {
                wSize = w.getwSize();
                vData = dataList.getRow(w.getChoosedLine());
            }
            return construct(GetActionEnum.GetListLineChecked, dType, vData,
                    wSize);
        }

    }

    ListDataView(GwtTableFactory gFactory, IDataType dType, VListHeaderContainer heList) {
        this.heList = heList;
        this.dType = dType;
        tableView = gFactory.construct(new GwtTableView());
        // subscriber
        registerSubscriber(DataActionEnum.DrawListAction, dType, new DrawList());
        // caller
        registerCaller(GetActionEnum.GetListLineChecked, dType,
                new GetListData());
    }

    public void startPublish(int cellId) {
        publish(cellId, tableView);
    }

}
