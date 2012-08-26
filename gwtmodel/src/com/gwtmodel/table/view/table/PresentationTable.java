/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, softwarre 
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
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
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
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.PresentationEditCellFactory.IGetField;
import com.gwtmodel.table.view.util.PopUpHint;

/**
 * 
 * @author perseus
 */
class PresentationTable implements IGwtTableView {

    /**
     * Call back when line is clicked.
     */
    private final IRowClick iClick;
    /**
     * Call back when column is clicked.
     */
    private final ICommand iActionColumn;
    /**
     * Call back function for raw cell columns.
     */
    private final IGetCellValue gValue;
    /**
     * The widget table in flesh.
     */
    private final CellTable<MutableInteger> table = new CellTable<MutableInteger>();
    private IGwtTableModel model = null;
    /**
     * If column definition has been provided.
     */
    private boolean columnC = false;
    final SingleSelectionModel<MutableInteger> selectionModel = new SingleSelectionModel<MutableInteger>();
    private WChoosedLine wChoosed;
    private final SimplePager sPager = new SimplePager(TextLocation.CENTER,
            (Resources) GWT.create(SimplePager.Resources.class), false, 1000,
            true);
    private final VerticalPanel vPanel = new VerticalPanel();
    private final ListDataProvider<MutableInteger> dProvider = new ListDataProvider<MutableInteger>();
    private final List<MutableInteger> dList;
    private boolean whilefind = false;
    private IModifyRowStyle iModRow = null;
    private final PresentationCellFactory fa;
    private final PresentationEditCellFactory faEdit;

    private class CurrentHoverTip {

        MutableInteger key;
        int col;
        boolean on = false;

        PopUpHint pHint = new PopUpHint();
    }

    private final CurrentHoverTip currentH = new CurrentHoverTip();

    public void setModifyRowStyle(IModifyRowStyle iMod) {
        this.iModRow = iMod;
        if (iMod != null) {
            table.setRowStyles(new TStyles());
        }
    }

    private boolean selectEnabled() {
        return iClick != null;
    }

    /**
     * Custom function for additional style for rows. Uses java script function.
     * 
     * @author hotel
     * 
     */
    private class TStyles implements RowStyles<MutableInteger> {

        // tr.wait-reply
        public String getStyleNames(MutableInteger row, int rowIndex) {
            IVModelData v = model.getRows().get(row.intValue());
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
            MutableInteger sel = selectionModel.getSelectedObject();
            if (sel == null) {
                return;
            }
            // neither cell position nor column are available
            wChoosed = pgetClicked(sel, null, null);
            if (model.getIClicked() != null) {
                model.getIClicked().clicked(wChoosed);
            }
            if (selectEnabled()) {
                iClick.execute(whilefind);
            }
            if (model.unSelectAtOnce()) {
                selectionModel.setSelected(sel, false);
            }
            // synchronization by global variable to avoid execute while finding
            // not elegant solution but no better for the time being
            whilefind = false;
        }
    }

    private class PersistInTable implements ILostFocusEdit {

        @Override
        public void action(int row, IVField v) {
            List<IGetSetVField> l = getVList(row);
            for (IGetSetVField i : l) {
                if (i.getV().eq(v)) {
                    Object o = i.getValObj();
                    IVModelData vD = model.getRows().get(row);
                    vD.setF(v, o);
                }
            }

        }
    }

    private void hovercell(MutableInteger row, int col, WSize w, boolean focuson) {

        if (!focuson) {
            if (currentH.on) {
                currentH.on = false;
                currentH.pHint.actionOut();
            }
            return;
        }
        if (currentH.on) {
            currentH.pHint.actionOut();
        }
        // String s = "row=" + row.intValue() + " col = " + col;
        int i = toVColNo(col);
        if (i == -1) {
            return;
        }
        PresentationEditCellFactory.ErrorLineInfo errInfo = faEdit
                .getErrorInfo();
        GetSet g = new GetSet(i, row.intValue());
        Object o = g.getValObj();
        IVField v = g.getV();
        String s = FUtils.getValueOS(o, v);
        if (!errInfo.active) {
            if (row.intValue() != errInfo.rowno) {
                faEdit.setErrorLineInfo(errInfo.rowno, null);
            } else {
                InvalidateMess me = errInfo.errContainer.findV(v);
                if (me != null) {
                    s = me.getErrmess();
                }
            }
        }
        currentH.pHint.setMessage(s);
        currentH.key = row;
        currentH.col = col;
        currentH.on = true;
        currentH.pHint.actionOver(w);
    }

    private class RowHover implements RowHoverEvent.Handler {

        @Override
        public void onRowHover(RowHoverEvent e) {
            Event event = e.getBrowserEvent();
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }
            final Element target = event.getEventTarget().cast();
            TableCellElement targetTableCell = target.cast();
            try {
                int col = targetTableCell.getCellIndex();
                boolean focuson = !e.isUnHover();
                WSize w = new WSize(target);
                int row = e.getHoveringRow().getSectionRowIndex();
                MutableInteger i = table.getVisibleItem(row);
                hovercell(i, col, w, focuson);
            } catch (Exception ee) {
                // do not know reason, sometimes it happens
                // HostedModeException
                LogT.getL().severe(ee.getLocalizedMessage());
            }
        }

    }

    PresentationTable(IRowClick iClick, ICommand actionColumn,
            IGetCellValue gValue) {
        this.iClick = iClick;
        this.iActionColumn = actionColumn;
        this.gValue = gValue;
        selectionModel.addSelectionChangeHandler(new SelectionChange());
        dList = dProvider.getList();
        // set selection only if iClick defined
        // thus avoid changing color while clicking
        // TODO: consider later
        // if (selectEnabled()) {
        table.setSelectionModel(selectionModel);
        // }
        sPager.setDisplay(table);
        dProvider.addDataDisplay(table);
        setEmpty();
        ILostFocusEdit e = null;
        // if (!selectEnabled()) {
        // TODO: all the time move data from cells to table
        e = new PersistInTable();
        // }
        fa = new PresentationCellFactory(gValue);
        faEdit = new PresentationEditCellFactory(e, table);
        table.addRowHoverHandler(new RowHover());
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
    private class RawColumn extends Column<MutableInteger, SafeHtml> {

        private final IVField fie;

        RawColumn(IVField fie) {
            super(new A(getEmpty()));
            this.fie = fie;
        }

        @Override
        public String getCellStyleNames(Context context, MutableInteger object) {
            IVModelData v = model.getRows().get(object.intValue());
            return gValue.getCellStyleNames(v, fie);
        }

        @Override
        public SafeHtml getValue(MutableInteger object) {
            IVModelData v = model.getRows().get(object.intValue());
            return gValue.getValue(v, fie);
        }

        @Override
        public void onBrowserEvent(Context context, Element elem,
                final MutableInteger i, NativeEvent event) {
            // do not check event type, only clicked is raised here
            WSize wSize = new WSize(elem);
            // it is possible for cell to provide position of the single cell
            // not the whole row
            wChoosed = pgetClicked(i, fie, wSize);
            iActionColumn.execute();
        }
    }

    private class CoComparator implements Comparator<MutableInteger> {

        private final VListHeaderDesc he;

        CoComparator(VListHeaderDesc he) {
            this.he = he;
        }

        public int compare(MutableInteger o1, MutableInteger o2) {
            IVModelData v1 = model.getRows().get(o1.intValue());
            IVModelData v2 = model.getRows().get(o2.intValue());
            return FUtils.compareValue(v1, he.getFie(), v2, he.getFie());
        }
    }

    private class AttachClass implements ActionCell.Delegate<MutableInteger> {

        private final IVField v;

        AttachClass(IVField v) {
            this.v = v;
        }

        @Override
        public void execute(MutableInteger i) {
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
        if (!CUtil.EmptyS(vo.getWidthDef())) {
            table.setWidth(vo.getWidthDef(), true);
        }
        List<VListHeaderDesc> li = vo.getVisHeList();
        @SuppressWarnings("rawtypes")
        Column co;
        for (VListHeaderDesc he : li) {
            boolean editable = he.isEditable();
            VListHeaderDesc.ColAlign align = he.getAlign();
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
                case INT:
                    if (editable) {
                        co = faEdit.constructNumberCol(he);
                    } else {
                        co = fa.constructNumberCol(he.getFie());
                    }
                    if (align == null) {
                        co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                    }
                    break;
                case DATE:
                    if (editable) {
                        co = faEdit.constructDateEditCol(he);
                    } else {
                        co = fa.constructDateEditCol(he.getFie());
                    }
                    break;
                case BOOLEAN:
                    if (editable) {
                        co = faEdit.contructBooleanCol(he.getFie(),
                                !selectEnabled());
                    } else {
                        co = fa.contructBooleanCol(he.getFie());
                    }
                    break;
                default:
                    if (editable) {
                        co = faEdit.constructEditTextCol(he);
                    } else {
                        co = fa.constructTextCol(he.getFie());
                    }
                    break;
                }
            }
            co.setSortable(true);
            // align
            if (align != null) {
                switch (align) {
                case LEFT:
                    co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
                    break;
                case RIGHT:
                    co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
                    break;
                case CENTER:
                    co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                    break;
                }
            }
            // col width
            String width = he.getColWidth();
            if (!CUtil.EmptyS(width)) {
                String[] pa = width.split(";");
                if (pa.length == 1) {
                    table.setColumnWidth(co, pa[0]);
                } else {
                    double wi;
                    try {
                        wi = Utils.toDouble(pa[0]);
                    } catch (NumberFormatException e) {
                        String info = LogT.getT().InproperWidthInColumn(
                                he.getHeaderString(), pa[0]);
                        LogT.errorLogE(info, e);
                        return;
                    }
                    Style.Unit u;
                    try {
                        u = Style.Unit.valueOf(pa[1]);
                    } catch (IllegalArgumentException e) {
                        String info = LogT.getT().InproperColumnUnit(
                                he.getHeaderString(), pa[1]);
                        LogT.errorLogE(info, e);
                        return;
                    }
                    table.setColumnWidth(co, wi, u);
                }
            }
            //
            if (he.isHidden() || he.getHeaderString() == null) {
                // Important: for some reason the assert violation cause
                // breaking without Exception throwing
                // So additional error alert is displayed to avoid confusion
                Utils.errAlert(he.getFie().getId(), LogT.getT().HeaderNull());
            }
            assert !he.isHidden() && he.getHeaderString() != null : LogT.getT()
                    .cannotBeNull();

            table.addColumn(co, he.getHeaderString());

            ListHandler<MutableInteger> columnSortHandler = new ListHandler<MutableInteger>(
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
            dList.add(new MutableInteger(i));
        }
        int size = model.getHeaderList().getPageSize();
        if (size != 0) {
            table.setPageSize(size);
        }
        table.setPageStart(setSelected().pStart);
        int aNo = vPanel.getWidgetCount();
        String title = model.getHeaderList().getListTitle();
        Label lTitle = null;
        if (title != null) {
            lTitle = new Label(title);
        }
        int nNo = 1;
        if (lTitle != null) {
            nNo = 2;
        }
        boolean addPager = false;
        if ((size != 0) && dList.size() > size) {
            nNo++;
            addPager = true;
        }
        if (nNo != aNo) {
            vPanel.clear();
            if (lTitle != null) {
                vPanel.add(lTitle);
            }
            vPanel.add(table);
            if (addPager) {
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
    private WChoosedLine pgetClicked(MutableInteger sel, IVField v, WSize wSize) {
        if (sel == null) {
            return new WChoosedLine();
        }
        int i = sel.intValue();
        WSize w = wSize;
        // cell position not defined, take whe position of the whole row
        if (wSize == null) {
            w = getRowWidget(i);
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
        faEdit.setModel(model);
        createHeader();
        drawRows();
    }

    @Override
    public Widget getGWidget() {
        return vPanel;
    }

    @Override
    public void setClicked(int clickedno, boolean whileFind) {
        int pStart = table.getPageStart();
        P p = cPage(clickedno);
        if (p.pStart != pStart) {
            table.setPageStart(p.pStart);
        }
        whilefind = whileFind;
        selectionModel.setSelected(new MutableInteger(clickedno), true);
        // added 2012/08/20
        whilefind = false;
    }

    @Override
    public void setEditable(ChangeEditableRowsParam eParam) {
        ChangeEditableRowsParam actParam = faEdit.geteParam();
        ChangeEditableRowsParam.ModifMode actMode = null;
        if (actParam != null) {
            actMode = actParam.getMode();
        }
        if (eParam.fullEdit()) {
            if (actMode == null
                    || actMode == ChangeEditableRowsParam.ModifMode.NORMALMODE) {
                Column imColumn = faEdit.constructControlColumn();
                table.insertColumn(0, imColumn);
            }
        }
        faEdit.setEditable(eParam);
        drawRows();
    }

    private int toVColNo(int col) {
        if (faEdit.geteParam() != null && faEdit.geteParam().fullEdit()) {
            return col - 1;
        }
        return col;

    }

    private class GetSet implements IGetSetVField {

        private final int i;
        private final int rowno;

        private GetSet(int i, int rowno) {
            this.i = i;
            this.rowno = rowno;
        }

        @Override
        public IVField getV() {
            VListHeaderDesc v = model.getHeaderList().getVisHeList().get(i);
            return v.getFie();
        }

        private int getColNo() {
            // omit first column, contains edit controls
            if (faEdit.geteParam() != null && faEdit.geteParam().fullEdit()) {
                return i + 1;
            }
            return i;

        }

        private IGetField getI() {
            Column<?, ?> co = table.getColumn(getColNo());
            Cell<?> ce = co.getCell();
            if (ce instanceof IGetField) {
                IGetField iGet = (IGetField) ce;
                return iGet;
            }
            return null;
        }

        @Override
        public Object getValObj() {
            IGetField iGet = getI();
            Object o = null;
            if (iGet != null) {
                o = iGet.getValObj(new MutableInteger(rowno));
            }
            if (o == null) {
                IVModelData v = model.getRows().get(rowno);
                o = FUtils.getValue(v, getV());
            }
            return o;
        }

        @Override
        public void setValObj(Object o) {
            IGetField iGet = getI();
            iGet.setValObj(new MutableInteger(rowno), o);
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

    @Override
    public void removeSort() {
        if (table == null) {
            return;
        }
        ColumnSortList sortList = table.getColumnSortList();
        if (sortList == null) {
            return;
        }
        while (sortList.size() > 0) {
            ColumnSortInfo i = sortList.get(0);
            sortList.remove(i);
        }

    }

    @Override
    public boolean isSorted() {
        if (table == null) {
            return false;
        }
        ColumnSortList sortList = table.getColumnSortList();
        return sortList.size() > 0;
    }

    @Override
    public int getPageSize() {
        if (table == null) {
            return -1;
        }
        return table.getPageSize();
    }

    @Override
    public void setPageSize(int pageSize) {
        table.setPageSize(pageSize);
    }

    @Override
    public WSize getRowWidget(int rowno) {
        MutableInteger sel = new MutableInteger(rowno);
        // cell position not defined, take the position of the whole row
        List<MutableInteger> vList = table.getVisibleItems();
        int inde = -1;
        boolean found = false;
        for (MutableInteger ine : vList) {
            inde++;
            if (ine.equals(sel)) {
                found = true;
                break;
            }
        }
        assert found : LogT.getT().RowSelectedNotFound();
        TableRowElement ro = table.getRowElement(inde);
        WSize w = new WSize(ro.getAbsoluteTop(), ro.getAbsoluteLeft(),
                ro.getClientHeight(), ro.getClientWidth());
        return w;
    }

    @Override
    public void removeRow(int rownum) {
        List<MutableInteger> iList = dProvider.getList();
        for (MutableInteger i : iList) {
            if (i.intValue() == rownum) {
                iList.remove(i);
                break;
            }
        }
        for (MutableInteger i : iList) {
            if (i.intValue() > rownum) {
                i.dec();
            }
        }
    }

    @Override
    public void addRow(int rownum) {
        List<MutableInteger> iList = dProvider.getList();
        if (rownum == -1) {
            iList.add(new MutableInteger(iList.size()));
            return;
        }
        for (MutableInteger i : iList) {
            if (i.intValue() >= rownum) {
                i.inc();
            }
        }
        iList.add(rownum, new MutableInteger(rownum));
    }

    @Override
    public void showInvalidate(int rowno, InvalidateFormContainer errContainer) {
        faEdit.setErrorLineInfo(rowno, errContainer);

    }
}
