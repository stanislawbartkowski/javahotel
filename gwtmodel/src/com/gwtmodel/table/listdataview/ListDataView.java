/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.CreateJson;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.DataListModelView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IModifyRowStyle;
import com.gwtmodel.table.view.table.VListHeaderContainer;
import java.util.ArrayList;
import java.util.List;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final IGwtTableView tableView;
    private final DataListModelView listView;
    private IDataListType dataList;

    private class DrawHeader implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            VListHeaderContainer listHeader = slContext.getListHeader();
            listView.setHeaderList(listHeader);
            tableView.setModel(listView);
            if (listHeader.getJsModifRow() != null) {
                tableView.setModifyRowStyle(new ModifRow(listHeader.getJsModifRow()));
            }
        }
    }

    private class ModifRow implements IModifyRowStyle {

        private final String jsFun;

        ModifRow(String jsFun) {
            this.jsFun = jsFun;
        }

        public String newRowStyle(IVModelData line) {
            CreateJson s = new CreateJson("row");
            for (IVField f : line.getF()) {
                boolean number = false;
                switch (f.getType().getType()) {
                    case BIGDECIMAL:
                    case LONG:
                    case INT:
                        number = true;
                        break;
                }
                String name = f.getId();
                String val = FUtils.getValueS(line, f);
                s.addElem(name, val, number);
            }
            String param = s.createJsonString();
            String m = Utils.callJsStringFun(jsFun, param);
            return m;
        }
    }

    private class FilterDataListType implements IDataListType {

        private final IOkModelData iOk;

        FilterDataListType(IOkModelData iOk) {
            this.iOk = iOk;
        }

        @Override
        public List<IVModelData> getList() {
            List<IVModelData> li = new ArrayList<IVModelData>();
            for (IVModelData v : dataList.getList()) {
                if (iOk.OkData(v)) {
                    li.add(v);
                }
            }
            return li;
        }

        @Override
        public IVField comboField() {
            return dataList.comboField();
        }

        @Override
        public void append(IVModelData vData) {
            dataList.append(vData);
        }

        @Override
        public void remove(int row) {
            dataList.remove(row);
        }
    }

    private class DrawList implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            dataList = slContext.getDataList();
            listView.setDataList(dataList);
            tableView.refresh();
        }
    }

    private class SetFilter implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IOkModelData iOk = slContext.getIOkModelData();
            assert iOk != null : LogT.getT().FilterCannotbeNull();
            IDataListType dList = new FilterDataListType(iOk);
            listView.setDataList(dList);
            tableView.refresh();
        }
    }

    private class FindRow implements ISlotSignaller {

        private final boolean next;
        private final boolean begin;

        FindRow(boolean next, boolean begin) {
            this.next = next;
            this.begin = begin;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            IOkModelData iOk = slContext.getIOkModelData();
            assert iOk != null : LogT.getT().FilterCannotbeNull();
            WChoosedLine w = tableView.getClicked();
            int aLine = -1;
            if (w.isChoosed() && !begin) {
                aLine = w.getChoosedLine() - 1;
            }
            List<IVModelData> li = tableView.getViewModel().getRows();
            if (next) {
                aLine++;
            }
            boolean found = false;
            // order in while predicat evaluation is important !
            while (!found && (++aLine < li.size())) {
                IVModelData v = li.get(aLine);
                found = iOk.OkData(v);
            }
            if (!found) {
                publish(DataActionEnum.NotFoundSignal, dType);
                return;
            }
            tableView.setClicked(aLine);
        }
    }

    private class RemoveFilter implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            listView.setDataList(dataList);
            tableView.refresh();
        }
    }

    private class GetComboField implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVField comboF = listView.getComboField();
            return construct(dType, comboF);
        }
    }

    private ISlotSignalContext constructChoosedContext() {
        WChoosedLine w = tableView.getClicked();
        WSize wSize = null;
        IVModelData vData = null;
        if (w.isChoosed()) {
            wSize = w.getwSize();
            vData = tableView.getViewModel().getRows().get(w.getChoosedLine());
        }
        return construct(GetActionEnum.GetListLineChecked, dType, vData,
                wSize);
    }

    private class GetListData implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return constructChoosedContext();
        }
    }

    private class ClickList implements ICommand {

        @Override
        public void execute() {
            publish(DataActionEnum.TableLineClicked, dType, constructChoosedContext());
        }
    }

    ListDataView(GwtTableFactory gFactory, IDataType dType) {
        listView = new DataListModelView();
        this.dType = dType;
        tableView = gFactory.construct(new ClickList());
        // subscriber
        registerSubscriber(DataActionEnum.DrawListAction, dType, new DrawList());
        registerSubscriber(DataActionEnum.FindRowList, dType, new FindRow(false, false));
        registerSubscriber(DataActionEnum.FindRowBeginningList, dType, new FindRow(false, true));
        registerSubscriber(DataActionEnum.FindRowNextList, dType, new FindRow(true, false));
        registerSubscriber(DataActionEnum.DrawListSetFilter, dType, new SetFilter());
        registerSubscriber(DataActionEnum.DrawListRemoveFilter, dType, new RemoveFilter());
        registerSubscriber(DataActionEnum.ReadHeaderContainerSignal, dType,
                new DrawHeader());
        // caller
        registerCaller(GetActionEnum.GetListLineChecked, dType,
                new GetListData());
        registerCaller(GetActionEnum.GetListComboField, dType, new GetComboField());
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, tableView);
    }
}
