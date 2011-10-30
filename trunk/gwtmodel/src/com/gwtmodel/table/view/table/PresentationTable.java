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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;
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
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.table.PresentationCellFactory.IGetField;

/**
 * 
 * @author perseus
 */
class PresentationTable implements IGwtTableView {

    /** Call back when line is clicked. */
    private final ICommand iClick;
    /** Call back when column is clicked. */
    private final ICommand iActionColumn;
    /** Call back function for raw cell columns. */
    private final IGetCellValue gValue;
    /** The widget table in flesh. */
    private final CellTable<Integer> table = new CellTable<Integer>();
    private IGwtTableModel model = null;
    /** If column definition has been provided. */
    private boolean columnC = false;
    final SingleSelectionModel<Integer> selectionModel = new SingleSelectionModel<Integer>();
    private WChoosedLine wChoosed;
    private final SimplePager sPager = new SimplePager(TextLocation.CENTER,
            (Resources) GWT.create(SimplePager.Resources.class), false, 1000,
            true);
    private final VerticalPanel vPanel = new VerticalPanel();
    private final ListDataProvider<Integer> dProvider = new ListDataProvider<Integer>();
    private final List<Integer> dList;
    private boolean whilefind = false;
    private IModifyRowStyle iModRow = null;
    private final PresentationCellFactory fa;

    public void setModifyRowStyle(IModifyRowStyle iMod) {
        this.iModRow = iMod;
        if (iMod != null) {
            table.setRowStyles(new TStyles());
        }
    }

    /**
     * Custom function for additional style for rows. Uses java script function.
     * 
     * @author hotel
     * 
     */
    private class TStyles implements RowStyles<Integer> {

        // tr.wait-reply
        public String getStyleNames(Integer row, int rowIndex) {
            IVModelData v = model.getRows().get(row);
            return iModRow.newRowStyle(v);
        }
    }

    /**
     * Raised when the whole row was selected
     * 
     * @author hotel
     * 
     */

    private class SelectionChange implements SelectionChangeEvent.Handler {

        @Override
        public void onSelectionChange(SelectionChangeEvent event) {
            Integer sel = selectionModel.getSelectedObject();
            if (sel == null) {
                return;
            }
            // neither cell position nor column are available
            wChoosed = pgetClicked(sel, null, null);
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

    PresentationTable(ICommand iClick, ICommand actionColumn,
            IGetCellValue gValue) {
        this.iClick = iClick;
        this.iActionColumn = actionColumn;
        this.gValue = gValue;
        selectionModel.addSelectionChangeHandler(new SelectionChange());
        dList = dProvider.getList();
        table.setSelectionModel(selectionModel);
        sPager.setDisplay(table);
        dProvider.addDataDisplay(table);
        setEmpty();
        fa = new PresentationCellFactory(gValue);
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

    /**
     * Implementation of AbstractCell. The only purpose is to take over
     * "clicked" event
     * 
     * @author hotel
     * 
     */
    private class A extends AbstractCell<SafeHtml> {

        A(SafeHtml a) {
            // activates onBrowserEvent
            super("click");
        }

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context,
                SafeHtml value, SafeHtmlBuilder sb) {
            sb.append(value);
        }

    }

    private SafeHtml getEmpty() {
        return new SafeHtmlBuilder().appendEscaped("").toSafeHtml();
    }

    /**
     * Display raw cell column. Call back function provides html (safe)
     * 
     * @author hotel
     * 
     */
    private class RawColumn extends Column<Integer, SafeHtml> {

        private final IVField fie;

        RawColumn(IVField fie) {
            super(new A(getEmpty()));
            this.fie = fie;
        }

        @Override
        public SafeHtml getValue(Integer object) {
            IVModelData v = model.getRows().get(object);
            return gValue.getValue(v, fie);
        }

        @Override
        public void onBrowserEvent(Context context, Element elem,
                final Integer i, NativeEvent event) {
            // do not check event type, only clicked is raised here
            WSize wSize = new WSize(elem);
            // it is possible for cell to provide position of the single cell
            // not the whole row
            wChoosed = pgetClicked(i, fie, wSize);
            iActionColumn.execute();
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
            wChoosed = pgetClicked(i, v, null);
            iActionColumn.execute();
        }
    }

    @SuppressWarnings("unchecked")
    private void createHeader() {
        if (model == null) {
            return;
        }
        if (columnC) {
            return;
        }
        VListHeaderContainer vo = model.getHeaderList();
        List<VListHeaderDesc> li = vo.getVisHeList();
        @SuppressWarnings("rawtypes")
        Column co = null;
        for (VListHeaderDesc he : li) {
            boolean editable = he.isEditable();
            if (he.getgHeader() != null) {
                @SuppressWarnings("rawtypes")
                Header header = he.getgHeader().getHeader();
                co = he.getgHeader().getColumn();
                if (co == null) {
                    co = new RawColumn(he.getFie());
                }
                table.addColumn(co, header);
                continue;
            }
            FieldDataType fType = he.getFie().getType();
            if (he.getButtonAction() != null) {
                co = fa.constructActionButtonCell(he.getButtonAction(),
                        he.getFie(), fType, new AttachClass(he.getFie()));
            } else if (fType.isConvertableToString()) {
                co = new TColumnString(he.getFie(), fType);
            } else {
                switch (fType.getType()) {
                case LONG:
                case BIGDECIMAL:
                    co = fa.constructNumberCol(he.getFie(), editable);
                    break;
                case DATE:
                    co = fa.constructDateEditCol(he.getFie(), editable);
                    break;
                default:
                    co = fa.constructEditCol(he.getFie(), editable);
                    break;
                }
            }
            co.setSortable(true);
            if (he.isHidden() || he.getHeaderString() == null) {
                // Important: for some reason the assert violation cause breaking without Exception throwing
                // So additional error alert is displayed to avoid confusion
                Utils.errAlert(he.getFie().getId(), LogT.getT().HeaderNull());
            }
            assert !he.isHidden() && he.getHeaderString() != null : LogT.getT()
                    .cannotBeNull();

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
        @SuppressWarnings("unused")
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

    /**
     * Creates WChoosedLine for selected/clicked. It can be later retrieved.
     * Only one can be retrieved, next overwrite the previous
     * 
     * @param sel
     *            Row (Integer) position
     * @param v
     *            Column to be clicked (if available)
     * @param wSize
     *            Cell position (if not null)
     * @return
     */
    private WChoosedLine pgetClicked(Integer sel, IVField v, WSize wSize) {
        if (sel == null) {
            return new WChoosedLine();
        }
        int i = sel.intValue();
        int sta = sPager.getPageStart();
        int inde = i - sta;
        TableRowElement ro = table.getRowElement(inde);
        WSize w = wSize;
        // cell position not defined, take whe position of the whole row
        if (wSize == null) {
            w = new WSize(ro.getAbsoluteTop(), ro.getAbsoluteLeft(),
                    ro.getClientHeight(), ro.getClientWidth());
        }
        return new WChoosedLine(i, w, v);
    }

    @Override
    public IGwtTableModel getViewModel() {
        return model;
    }

    @Override
    public void setModel(IGwtTableModel model) {
        this.model = model;
        fa.setModel(model);
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

    @Override
    public void setEditable(ChangeEditableRowsParam eParam) {
        fa.setEditable(eParam);
        drawRows();
    }

    private class GetSet implements IGetSetVField {

        private final int i;
        private final int rowno;

        private GetSet(int i, int rowno) {
            this.i = i;
            this.rowno = rowno;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.gwtmodel.table.IGetSetVField#getV()
         */
        @Override
        public IVField getV() {
            VListHeaderDesc v = model.getHeaderList().getVisHeList().get(i);
            return v.getFie();
        }

        private IGetField getI() {
            Column<?, ?> co = table.getColumn(i);
            Cell<?> ce = co.getCell();
            IGetField iGet = (IGetField) ce;
            return iGet;
        }

        @Override
        public Object getValObj() {
            IGetField iGet = getI();
            Object o = iGet.getValObj(new Integer(rowno));
            if (o == null) {
                IVModelData v = model.getRows().get(rowno);
                o = FUtils.getValue(v, getV());
            }
            return o;
        }

        @Override
        public void setValObj(Object o) {
            IGetField iGet = getI();
            iGet.setValObj(new Integer(rowno), o);
            table.redraw();
        }

    }

    @Override
    public List<IGetSetVField> getVList(int rowno) {
        int no = 0;
        List<IGetSetVField> vList = new ArrayList<IGetSetVField>();
        for (VListHeaderDesc v : model.getHeaderList().getVisHeList()) {
            if (v.isEditable()) {
                vList.add(new GetSet(no, rowno));
            }
            no++;
        }
        return vList;
    }
}
