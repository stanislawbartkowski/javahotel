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

import java.util.List;

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ListEventEnum;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.VListHeaderDesc;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final List<VListHeaderDesc> heList;
    private DataListType dataList;
    private final IGwtTableView tableView;
    private final IDataType dType;

    private class GwtTableView implements IGwtTableModel {

        public List<VListHeaderDesc> getHeaderList() {
            return heList;
        }

        public IVModelData getRow(int row) {
            return dataList.getdList().get(row);
        }

        public int getRowsNum() {
            return dataList.getdList().size();
        }

        public String getTableHeader() {
            return null;
        }

    }

    private class DrawList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            if (!dType.eq(slContext.getdType())) {
                return;
            }
            dataList = slContext.getDataList();
            tableView.refresh();
        }
    }

    ListDataView(IDataType dType, int cellId,
            List<VListHeaderDesc> heList) {
        this.dType = dType;
        this.heList = heList;
        tableView = GwtTableFactory.construct(new GwtTableView());
        // publisher
        createCallBackWidget(cellId);
        // subscriber
        SlotType slType = SlotTypeFactory
                .contruct(ListEventEnum.ReadListSuccess);
        slContainer.addSubscriber(slType, new DrawList());
    }

    public void startPublish() {
        publishCallBack(tableView);
    }

}
