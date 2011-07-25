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
package com.gwtmodel.table.view.table;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.LogT;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author perseus
 */
class PresentationTable implements IGwtTableView {

    private final ICommand iClick;
    private final ICommand iActionColumn;
    private final CellTable<Integer> table = new CellTable<Integer>();
    private IGwtTableModel model = null;
    private boolean columnC = false;
    final SingleSelectionModel<Integer> selectionModel = new SingleSelectionModel<Integer>();
    private WChoosedLine wChoosed;
    private final SimplePager sPager = new SimplePager(TextLocation.CENTER, (Resources) GWT.create(SimplePager.Resources.class), false, 1000,
            true);
    private final VerticalPanel vPanel = new VerticalPanel();
    private final ListDataProvider<Integer> dProvider = new ListDataProvider<Integer>();
    private final List<Integer> dList;
    private boolean whilefind = false;
    private IModifyRowStyle iModRow = null;
    private final IGetCustomValues cValues;

    public void setModifyRowStyle(IModifyRowStyle iMod) {
        this.iModRow = iMod;
        if (iMod != null) {
            table.setRowStyles(new TStyles());
        }
    }

    private class TStyles implements RowStyles<Integer> {

// tr.wait-reply
        public String getStyleNames(Integer row, int rowIndex) {
            IVModelData v = model.getRows().get(row);
            return iModRow.newRowStyle(v);
        }
    }

    private class SelectionChange implements SelectionChangeEvent.Handler {

        @Override
        public void onSelectionChange(SelectionChangeEvent event) {
            Integer sel = selectionModel.getSelectedObject();
            if (sel == null) {
                return;
            }
            wChoosed = pgetClicked(sel, null);
            if (model.getIClicked() != null) {
                model.getIClicked().clicked(wChoosed);
            }
            if ((iClick != null) && !whilefind) {
                iClick.execute();
            }
            // synchronization by global variable to avoid execute while finding
            // not elegant solution but no better for the time being
            whilefind = false;
        }
    }

    PresentationTable(ICommand iClick, ICommand actionColumn) {
        this.iClick = iClick;
        this.iActionColumn = actionColumn;
        selectionModel.addSelectionChangeHandler(new SelectionChange());
        dList = dProvider.getList();
        table.setSelectionModel(selectionModel);
        sPager.setDisplay(table);
        dProvider.addDataDisplay(table);
        setEmpty();
        cValues = GwtGiniInjector.getI().getTableFactoriesContainer().getGetCustomValues();
        dCell = new DateCell(DateTimeFormat.getFormat(cValues.getCustomValue(IGetCustomValues.DATEFORMAT)));
    }

    private class TColumn extends TextColumn<Integer> {

        private final IVField iF;

        TColumn(IVField iF) {
            this.iF = iF;
        }

        @Override
        public String getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            return FUtils.getValueS(v, iF);
        }
    }

    private class TColumnString extends TextColumn<Integer> {

        private final IVField iF;
        private final FieldDataType fType;

        TColumnString(IVField iF, FieldDataType fType) {
            this.iF = iF;
            this.fType = fType;
        }

        @Override
        public String getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            String key = FUtils.getValueS(v, iF);
            return fType.convertToString(key);
        }
    }
    private final NumberCell iCell = new NumberCell(NumberFormat.getFormat("#####"));
    private final NumberCell nCell1 = new NumberCell(NumberFormat.getFormat("###########.#"));
    private final NumberCell nCell2 = new NumberCell(NumberFormat.getFormat("###########.##"));
    private final NumberCell nCell3 = new NumberCell(NumberFormat.getFormat("###########.###"));
    private final NumberCell nCell4 = new NumberCell(NumberFormat.getFormat("###########.####"));
    private final DateCell dCell;

    private class LongColumn extends Column<Integer, Number> {

        private final VListHeaderDesc he;

        LongColumn(VListHeaderDesc he) {
            super(iCell);
            this.he = he;
        }

        @Override
        public Long getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            return FUtils.getValueLong(v, he.getFie());

        }
    }

    private class DateColumn extends Column<Integer, Date> {

        private final VListHeaderDesc he;

        DateColumn(VListHeaderDesc he) {
            super(dCell);
            this.he = he;
        }

        @Override
        public Date getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            return FUtils.getValueDate(v, he.getFie());
        }
    }

    private abstract class NumberColumn extends Column<Integer, Number> {

        private final VListHeaderDesc he;

        NumberColumn(NumberCell n, VListHeaderDesc he) {
            super(n);
            this.he = he;
        }

        @Override
        public Number getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            BigDecimal b = FUtils.getValueBigDecimal(v, he.getFie());
            return b;
        }
    }

    private class NumberColumn1 extends NumberColumn {

        NumberColumn1(VListHeaderDesc he) {
            super(nCell1, he);
        }
    }

    private class NumberColumn2 extends NumberColumn {

        NumberColumn2(VListHeaderDesc he) {
            super(nCell2, he);
        }
    }

    private class NumberColumn3 extends NumberColumn {

        NumberColumn3(VListHeaderDesc he) {
            super(nCell3, he);
        }
    }

    private class NumberColumn4 extends NumberColumn {

        NumberColumn4(VListHeaderDesc he) {
            super(nCell4, he);
        }
    }

    private class CoComparator implements Comparator<Integer> {

        private final VListHeaderDesc he;

        CoComparator(VListHeaderDesc he) {
            this.he = he;
        }

        public int compare(Integer o1, Integer o2) {
            IVModelData v1 = model.getRows().get(o1);
            IVModelData v2 = model.getRows().get(o2);
            return FUtils.compareValue(v1, he.getFie(), v2, he.getFie());
        }
    }

    private class AttachClass implements ActionCell.Delegate<Integer> {

        private final IVField v;

        AttachClass(IVField v) {
            this.v = v;
        }

        @Override
        public void execute(Integer i) {
            wChoosed = pgetClicked(i, v);
            iActionColumn.execute();
        }
    }

    private class ActionButtonCell extends ActionCell<Integer> {

        private final String buttonString;
        private final TColumn tCol;

        ActionButtonCell(String buttonString, IVField iF, FieldDataType fType) {
            super("", new AttachClass(iF));
            this.buttonString = buttonString;
            this.tCol = new TColumn(iF);
        }

        @Override
        public void render(Cell.Context context, Integer value,
                SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<strong>");
            String s = tCol.getValue(value);
            sb.appendEscaped(s);
            sb.appendHtmlConstant("</strong>");
        }
    }

    private class ButtonColumn extends Column<Integer, Integer> {

        public ButtonColumn(ActionButtonCell cell) {
            super(cell);
        }

        @Override
        public Integer getValue(Integer object) {
            return object;
        }
    }

    private void createHeader() {
        if (model == null) {
            return;
        }
        if (columnC) {
            return;
        }
        VListHeaderContainer vo = model.getHeaderList();
        List<VListHeaderDesc> li = vo.getVisHeList();
        Column co = null;
        for (VListHeaderDesc he : li) {
            FieldDataType fType = he.getFie().getType();
            if (he.getButtonAction() != null) {
                co = new ButtonColumn(new ActionButtonCell(he.getButtonAction(), he.getFie(), fType));
            } else if (fType.isConvertableToString()) {
                co = new TColumnString(he.getFie(), fType);
            } else {
                switch (fType.getType()) {
                    case LONG:
                        co = new LongColumn(he);
                        break;
                    case BIGDECIMAL:
                        switch (he.getFie().getType().getAfterdot()) {
                            case 0:
                                co = new LongColumn(he);
                                break;
                            case 1:
                                co = new NumberColumn1(he);
                                break;
                            case 2:
                                co = new NumberColumn2(he);
                                break;
                            case 3:
                                co = new NumberColumn3(he);
                                break;
                            default:
                                co = new NumberColumn4(he);
                                break;
                        }
                        break;
                    case DATE:
                        co = new DateColumn(he);
                        break;
                    default:
                        co = new TColumn(he.getFie());
                        break;
                }
            }
            co.setSortable(true);
            assert !he.isHidden() && he.getHeaderString() != null : LogT.getT().cannotBeNull();
            table.addColumn(co, he.getHeaderString());

            ListHandler<Integer> columnSortHandler = new ListHandler<Integer>(
                    dList);
            columnSortHandler.setComparator(co, new CoComparator(he));
            table.addColumnSortHandler(columnSortHandler);
        }
        columnC = true;
    }

    private void drawRows() {
        if ((model == null) || !model.containsData()) {
            table.setRowCount(0, true);
            return;
        }
        dList.clear();
        for (int i = 0; i < model.getRows().size(); i++) {
            dList.add(new Integer(i));
        }
        int size = model.getHeaderList().getPageSize();
        if (size != 0) {
            table.setPageSize(size);
        }
        table.setPageStart(setSelected().pStart);
        int aNo = vPanel.getWidgetCount();
        int nNo = 1;
        if ((size != 0) && dList.size() > size) {
            nNo = 2;
        }
        if (nNo != aNo) {
            vPanel.clear();
            vPanel.add(table);
            if (nNo > 1) {
                vPanel.add(sPager);
            }
        }
    }

    private void setEmpty() {
        wChoosed = new WChoosedLine();
    }

    private class P {

        private final int pStart;
        private final int pRow;

        P(int pStart, int pRow) {
            this.pStart = pStart;
            this.pRow = pRow;
        }
    }

    private P cPage(int li) {
        int pSize = table.getPageSize();
        int pStart = (li / pSize) * pSize;
        int pRow = li - pStart * pSize;
        return new P(pStart, pRow);
    }

    private P setSelected() {
        if (!wChoosed.isChoosed()) {
            table.setPageStart(0);
            return new P(0, 0);
        }
        int li = wChoosed.getChoosedLine();
        if (li >= dList.size()) {
            table.setPageStart(0);
            setEmpty();
            return new P(0, 0);
        }
        return cPage(li);
    }

    @Override
    public void refresh() {
        createHeader();
        drawRows();
    }

    @Override
    public WChoosedLine getClicked() {
        return wChoosed;
    }

    private WChoosedLine pgetClicked(Integer sel, IVField v) {
        if (sel == null) {
            return new WChoosedLine();
        }
        int i = sel.intValue();
        int sta = sPager.getPageStart();
        int inde = i - sta;
        TableRowElement ro = table.getRowElement(inde);
        WSize w = new WSize(ro.getAbsoluteTop(), ro.getAbsoluteLeft(),
                ro.getClientHeight(), ro.getClientWidth());
        return new WChoosedLine(i, w, v);
    }

    @Override
    public IGwtTableModel getViewModel() {
        return model;
    }

    @Override
    public void setModel(IGwtTableModel model) {
        this.model = model;
        createHeader();
        drawRows();
    }

    @Override
    public Widget getGWidget() {
        return vPanel;
    }

    @Override
    public void setClicked(int clickedno) {
        int pStart = table.getPageStart();
        P p = cPage(clickedno);
        if (p.pStart != pStart) {
            table.setPageStart(p.pStart);
        }
        whilefind = true;
        selectionModel.setSelected(new Integer(clickedno), true);
    }
}
