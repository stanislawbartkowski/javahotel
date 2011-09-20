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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.CreateJson;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.DataListModelView;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IModifyRowStyle;
import com.gwtmodel.table.view.table.VListHeaderContainer;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final IGwtTableView tableView;
    private final DataListModelView listView;
    private IDataListType dataList;
    private final SlotSignalContextFactory coFactory;

    private class GetHeader implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IGwtTableModel model = tableView.getViewModel();
            VListHeaderContainer header = model.getHeaderList();
            return coFactory.construct(slContext.getSlType(), header);
        }
    }

    private class DrawHeader implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            VListHeaderContainer listHeader = slContext.getListHeader();
            listView.setHeaderList(listHeader);
            tableView.setModel(listView);
            if (listHeader.getJsModifRow() != null) {
                tableView.setModifyRowStyle(new ModifRow(listHeader
                        .getJsModifRow()));
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

    private class RefreshList implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
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
            // order in while predicate evaluation is important !
            while (!found && (++aLine < li.size())) {
                IVModelData v = li.get(aLine);
                found = iOk.OkData(v);
            }
            if (!found) {
                publish(dType, DataActionEnum.NotFoundSignal);
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

    private class ChangeEditRows implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            EditRowsSignal e = (EditRowsSignal) o;
            tableView.setEditable(e);
        }
    }

    /**
     * Delivers IVModelData indentified by position in list
     * 
     * @author hotel
     * 
     */
    private class GetVDataByI implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            GetVDataByIntegerSignal e = (GetVDataByIntegerSignal) o;
            IVModelData v = dataList.getList().get(e.getI());
            return coFactory.construct(slContext.getSlType(), v);

        }
    }

    /**
     * Delivers List of IGetSetVield from table view
     * 
     * @author hotel
     * 
     */
    private class GetVListByI implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            GetVListSignal e = (GetVListSignal) o;
            List<IGetSetVField> vList = tableView.getVList(e.getI());
            e = new GetVListSignal(vList);
            return coFactory.construct(slContext.getSlType(), e);
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
        IVField v = null;
        if (w.isChoosed()) {
            wSize = w.getwSize();
            vData = tableView.getViewModel().getRows().get(w.getChoosedLine());
            v = w.getvField();
        }
        return construct(dType, GetActionEnum.GetListLineChecked, vData, wSize,
                v);
    }

    private class GetListData implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return constructChoosedContext();
        }
    }

    private class GetWholeList implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return coFactory.construct(slContext.getSlType(), dataList);
        }
    }

    private class ClickList implements ICommand {

        @Override
        public void execute() {
            publish(dType, DataActionEnum.TableLineClicked,
                    constructChoosedContext());
        }
    }

    private class ClickColumn implements ICommand {

        @Override
        public void execute() {
            WChoosedLine w = tableView.getClicked();
            SlotType sl = slTypeFactory.construct(dType,
                    DataActionEnum.TableCellClicked);
            publish(sl, w);
        }
    }

    ListDataView(GwtTableFactory gFactory, IDataType dType, IGetCellValue gValue) {
        listView = new DataListModelView();
        this.dType = dType;
        tableView = gFactory.construct(new ClickList(), new ClickColumn(),
                gValue);
        coFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        // subscriber
        registerSubscriber(dType, DataActionEnum.DrawListAction, new DrawList());
        registerSubscriber(dType, DataActionEnum.RefreshListAction,
                new RefreshList());
        registerSubscriber(dType, DataActionEnum.FindRowList, new FindRow(
                false, false));
        registerSubscriber(dType, DataActionEnum.FindRowBeginningList,
                new FindRow(false, true));
        registerSubscriber(dType, DataActionEnum.FindRowNextList, new FindRow(
                true, false));
        registerSubscriber(dType, DataActionEnum.DrawListSetFilter,
                new SetFilter());
        registerSubscriber(dType, DataActionEnum.DrawListRemoveFilter,
                new RemoveFilter());
        registerSubscriber(dType, DataActionEnum.ReadHeaderContainerSignal,
                new DrawHeader());
        registerSubscriber(new CustomStringDataTypeSlot(
                EditRowsSignal.EditSignal, dType), new ChangeEditRows());
        // caller
        registerCaller(new CustomStringDataTypeSlot(
                GetVDataByIntegerSignal.GETINTEGERSLOTSIGNAL, dType),
                new GetVDataByI());
        registerCaller(new CustomStringDataTypeSlot(GetVListSignal.GETVSIGNAL,
                dType), new GetVListByI());
        registerCaller(dType, GetActionEnum.GetListLineChecked,
                new GetListData());
        registerCaller(dType, GetActionEnum.GetListComboField,
                new GetComboField());
        registerCaller(dType, GetActionEnum.GetHeaderList, new GetHeader());
        registerCaller(dType, GetActionEnum.GetListData, new GetWholeList());
    }

    @Override
    public void startPublish(CellId cellId) {
        publish(dType, cellId, tableView);
    }
}
