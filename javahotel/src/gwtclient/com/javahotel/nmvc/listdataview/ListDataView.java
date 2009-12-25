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
package com.javahotel.nmvc.listdataview;

import com.javahotel.client.IResLocator;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.common.AbstractSlotContainer;
import com.javahotel.nmvc.common.DataListType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.listheadermodel.ListHeaderData;
import com.javahotel.nmvc.slotmodel.ISlotSignalContext;
import com.javahotel.nmvc.slotmodel.ISlotSignaller;
import com.javahotel.nmvc.slotmodel.ListEventEnum;
import com.javahotel.nmvc.slotmodel.SlotType;
import com.javahotel.nmvc.slotmodel.SlotTypeFactory;
import com.javahotel.view.ngwt.table.GwtTableFactory;
import com.javahotel.view.ngwt.table.IGwtTableModel;
import com.javahotel.view.ngwt.table.IGwtTableView;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final IResLocator rI;
    private final ListHeaderData heList;
    private DataListType dataList;
    private final IGwtTableView tableView;
    private final DataType dType;

    private class GwtTableView implements IGwtTableModel {

        public ListHeaderData getHeaderList() {
            return heList;
        }

        public AbstractTo getRow(int row) {
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

    ListDataView(IResLocator rI, DataType dType, int cellId,
            ListHeaderData heList) {
        this.rI = rI;
        this.dType = dType;
        this.heList = heList;
        tableView = GwtTableFactory.construct(rI, new GwtTableView());
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
