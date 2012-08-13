/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.FlowPanel;
import com.gwtmodel.table.CreateJson;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.GWidget;
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
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGetCellValue;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IModifyRowStyle;

class ListDataView extends AbstractSlotContainer implements IListDataView {

    private final FlowPanel vp = new FlowPanel();
    private IGwtTableView tableView;
    private final DataListModelView listView;
    private IDataListType dataList;
    private final SlotSignalContextFactory coFactory;
    private boolean isFilter = false;
    private boolean isTreeNow = false;
    private final GwtTableFactory gFactory;
    private final IGetCellValue gValue;
    private final boolean selectedRow;

    private class GetHeader implements ISlotCallerListener {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IGwtTableModel model = tableView.getViewModel();
            VListHeaderContainer header = model.getHeaderList();
            return coFactory.construct(slContext.getSlType(), header);
        }
    }

    private class DrawHeader implements ISlotListener {

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
                default:
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

        @Override
        public int treeLevel(int row) {
            return dataList.treeLevel(row);
        }

        @Override
        public boolean isTreeEnabled() {
            return dataList.isTreeEnabled();
        }
    }

    private class DrawList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            dataList = slContext.getDataList();
            listView.setDataList(dataList);
            tableView.refresh();
        }
    }

    private class RefreshList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            tableView.refresh();
        }
    }

    private class SetFilter implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            isFilter = true;
            IOkModelData iOk = slContext.getIOkModelData();
            assert iOk != null : LogT.getT().FilterCannotbeNull();
            IDataListType dList = new FilterDataListType(iOk);
            listView.setDataList(dList);
            tableView.refresh();
        }
    }

    private class FindRow implements ISlotListener {

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

    private class RemoveFilter implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            if (!isFilter) {
                return;
            }
            listView.setDataList(dataList);
            tableView.refresh();
            isFilter = false;
        }
    }

    private class ChangeEditRows implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            EditRowsSignal e = (EditRowsSignal) o;
            tableView.setEditable(e);
        }
    }

    private abstract class ModifListener implements ISlotListener {

        abstract void modif(ISlotSignalContext slContext);

        @Override
        public void signal(ISlotSignalContext slContext) {
            WChoosedLine w = tableView.getClicked();
            modif(slContext);
            if (w != null && w.isChoosed()) {
                tableView.setClicked(w.getChoosedLine());
            }

        }

    }

    private class ToTableTree extends ModifListener {

        private final boolean totree;

        ToTableTree(boolean totree) {
            this.totree = totree;
        }

        @Override
        void modif(ISlotSignalContext slContext) {
            constructView(totree);
            vp.clear();
            vp.add(tableView.getGWidget());
            tableView.setModel(listView);
            tableView.refresh();
            isTreeNow = totree;
        }

    }
    
    private class ChangeTableSize extends ModifListener {

        @Override
        void modif(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            DataIntegerSignal si = (DataIntegerSignal) o;
            tableView.setPageSize(si.getValue());
        }

    }

    private class RemoveSort extends ModifListener {

        @Override
        void modif(ISlotSignalContext slContext) {
            tableView.removeSort();
            tableView.refresh();
        }

    }

    /**
     * Delivers IVModelData indentified by position in list
     * 
     * @author hotel
     * 
     */
    private class GetVDataByI implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            DataIntegerSignal e = (DataIntegerSignal) o;
            IVModelData v = dataList.getList().get(e.getValue());
            return coFactory.construct(slContext.getSlType(), v);

        }
    }

    /**
     * Delivers List of IGetSetVield from table view
     * 
     * @author hotel
     * 
     */
    private class GetVListByI implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            GetVListSignal e = (GetVListSignal) o;
            List<IGetSetVField> vList = tableView.getVList(e.getValue());
            e = new GetVListSignal(vList);
            return coFactory.construct(slContext.getSlType(), e);
        }
    }

    private class GetTreeViewNow implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IsBooleanSignalNow si = new IsBooleanSignalNow(isTreeNow);
            return coFactory.construct(slContext.getSlType(), si);
        }

    }

    private class GetTableTreeEnabled implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            boolean enabled = false;
            if (dataList != null && dataList.isTreeEnabled()) {
                enabled = true;
            }
            IsBooleanSignalNow si = new IsBooleanSignalNow(enabled);
            return coFactory.construct(slContext.getSlType(), si);
        }
    }

    private class GetTableIsFilter implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IsBooleanSignalNow si = new IsBooleanSignalNow(isFilter);
            return coFactory.construct(slContext.getSlType(), si);
        }
    }

    private class GetTableIsSorted implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IsBooleanSignalNow si = new IsBooleanSignalNow(tableView.isSorted());
            return coFactory.construct(slContext.getSlType(), si);
        }
    }

    private class GetTablePageSize implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            DataIntegerSignal si = new DataIntegerSignal(
                    tableView.getPageSize());
            return coFactory.construct(slContext.getSlType(), si);
        }
    }

    private class GetComboField implements ISlotCallerListener {

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

    private class GetListData implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return constructChoosedContext();
        }
    }

    private class GetWholeList implements ISlotCallerListener {

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

    private void constructView(boolean treeView) {
        if (treeView) {
            tableView = gFactory.constructTree(selectedRow ? new ClickList()
                    : null);
            isTreeNow = true;
        } else {
            tableView = gFactory.construct(
                    selectedRow ? new ClickList() : null, new ClickColumn(),
                    gValue);
        }
    }

    ListDataView(GwtTableFactory gFactory, IDataType dType,
            IGetCellValue gValue, boolean selectedRow, boolean unSelectAtOnce,
            boolean treeView) {
        listView = new DataListModelView();
        listView.setUnSelectAtOnce(unSelectAtOnce);
        this.dType = dType;
        this.gFactory = gFactory;
        this.gValue = gValue;
        this.selectedRow = selectedRow;
        constructView(treeView);
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
//        registerSubscriber(new CustomStringDataTypeSlot(
//                EditRowsSignal.EditSignal, dType), new ChangeEditRows());
        registerSubscriber(ActionTableSignal.constructEditRowSignal(dType), new ChangeEditRows());
        registerSubscriber(ActionTableSignal.constructToTableSignal(dType),
                new ToTableTree(false));
        registerSubscriber(ActionTableSignal.constructToTreeSignal(dType),
                new ToTableTree(true));
        registerSubscriber(ActionTableSignal.constructRemoveSortSignal(dType),
                new RemoveSort());
        registerSubscriber(ActionTableSignal.constructSetPageSizeSignal(dType),
                new ChangeTableSize());
        // caller
        registerCaller(ActionTableSignal.constructSlot(dType),
                new GetVDataByI());
        registerCaller(ActionTableSignal.constructSlotGetVSignal(dType),
                new GetVListByI());
        registerCaller(dType, GetActionEnum.GetListLineChecked,
                new GetListData());
        registerCaller(dType, GetActionEnum.GetListComboField,
                new GetComboField());
        registerCaller(dType, GetActionEnum.GetHeaderList, new GetHeader());
        registerCaller(dType, GetActionEnum.GetListData, new GetWholeList());
        // GetTableIsFilter
        registerCaller(ActionTableSignal.constructSlotGetTreeView(dType),
                new GetTreeViewNow());
        registerCaller(
                ActionTableSignal.constructSlotGetTableTreeEnabled(dType),
                new GetTableTreeEnabled());
        registerCaller(ActionTableSignal.constructSlotGetTableIsFilter(dType),
                new GetTableIsFilter());
        registerCaller(ActionTableSignal.constructSlotGetTableIsSorted(dType),
                new GetTableIsSorted());
        registerCaller(ActionTableSignal.constructGetPageSizeSignal(dType),
                new GetTablePageSize());
    }

    @Override
    public void startPublish(CellId cellId) {
        vp.add(tableView.getGWidget());
        publish(dType, cellId, new GWidget(vp));
    }
}
