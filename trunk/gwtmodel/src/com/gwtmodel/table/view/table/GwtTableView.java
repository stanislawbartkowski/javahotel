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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.formatters.DateFormat;
import com.google.gwt.visualization.client.visualizations.Table;
import com.gwtmodel.table.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

class GwtTableView implements IGwtTableView {

    private IGwtTableModel model;
    private int clickedNo = -1;
    private WSize wSize;
    private final Label header = new Label();
    private final Table ta;
    private DataTable data;
    private final ICommand iClick;
    private final DateFormat df;

    GwtTableView(ICommand iClick) {
        ta = new Table(null, Table.Options.create());
        ta.addSelectHandler(new H(ta));
        this.iClick = iClick;
        model = null;
        DateFormat.Options o = DateFormat.Options.create();
        o.setPattern("yyyy.MM.dd");
        df = DateFormat.create(o);
    }

    private void drawrow(int row) {
        IVModelData ii = model.getRows().get(row);
        List<VListHeaderDesc> co = model.getHeaderList().getVisHeList();
        for (int c = 0; c < co.size(); c++) {
            VListHeaderDesc cl = co.get(c);
            switch (cl.getFie().getType().getType()) {
                case BIGDECIMAL:
                    BigDecimal b = FUtils.getValueBigDecimal(ii, cl.getFie());
                    if (b != null) {
                        Double d = Utils.toDouble(b);
                        data.setValue(row, c, d.doubleValue());
                    }
                    break;
                case LONG:
                    Long l = FUtils.getValueLong(ii, cl.getFie());
                    if (l != null) {
                        int ival = l.intValue();
                        data.setValue(row, c, ival);
                    }
                    break;
                case DATE:
                    Date dd = FUtils.getValueDate(ii, cl.getFie());
                    if (dd != null) {
                        data.setValue(row, c, dd);
                    }
                    break;
                case BOOLEAN:
                    Boolean bb = FUtils.getValueBoolean(ii, cl.getFie());
                    if (bb != null) {
                        boolean log = bb.booleanValue();
                        data.setValue(row, c, log);
                    }
                    break;
                default:
                    data.setValue(row, c, FUtils.getValueString(ii, cl.getFie()));
                    break;
            }
        }
    }

    private ColumnType getCType(VListHeaderDesc c) {
        switch (c.getFie().getType().getType()) {
            case BOOLEAN:
                return ColumnType.BOOLEAN;
            case STRING:
                return ColumnType.STRING;
            case BIGDECIMAL:
            case LONG:
                return ColumnType.NUMBER;
            case DATE:
                return ColumnType.DATE;
            case DATETIME:
                return ColumnType.DATETIME;
        }
        assert false : "Unrecognized column type";
        return null;
    }

    private void getTable() {
        data = DataTable.create();
        List<VListHeaderDesc> co = model.getHeaderList().getVisHeList();
        for (VListHeaderDesc c : co) {
            data.addColumn(getCType(c), c.getHeaderString());
        }
        int rowNo = model.getRows().size();
        data.addRows(rowNo);
        for (int i = 0; i < rowNo; i++) {
            drawrow(i);
        }
        // important: should be set after drawing rows, not before
        int colNo = 0;
        for (VListHeaderDesc c : co) {
            if (c.getFie().getType().getType() == FieldDataType.T.DATE) {
                df.format(data, colNo);
            }
            colNo++;
        }
    }

    private WSize getS(int row, int col) {
        Element etd = ta.getElement(); // TD
        // search first TR
        while ((etd != null) && (!etd.getTagName().equals("TR"))) {
            etd = etd.getFirstChildElement(); // TABLE
        }
        int no = 0;
        int top = 0;
        int left = 0;
        int height = 0;
        int width = 0;
        Element etr = etd;

        while (etr != null) {
            etr = etr.getNextSiblingElement();
            if (no == row) {
                break;
            }
            no++;
        }
        if (etr != null) {
            Element echild = etr.getFirstChildElement();
            int cno = 0;
            while ((echild != null) && (cno != col)) {
                echild = echild.getNextSiblingElement();
                cno++;
            }
            if (echild != null) {
                top = echild.getAbsoluteTop();
                left = echild.getAbsoluteLeft();
                height = echild.getOffsetHeight();
                width = echild.getOffsetWidth();
            }
        }
        return new WSize(top, left, height, width);
    }

    @Override
    public IGwtTableModel getViewModel() {
        return model;
    }

    private void draw() {
        if (model == null) {
            return;
        }
        if (!model.containsData()) {
            return;
        }
        getTable();
        Table.Options tao = Table.Options.create();
        int size = model.getHeaderList().getPageSize();
        if (size == 0) {
            size = Utils.CalculateNOfRows(null);
        }
        if (size > 0) {
            tao.setPageSize(size);
            tao.setPage(Table.Options.Policy.ENABLE);
        }
        ta.draw(data, tao);
    }

    @Override
    public void setModel(IGwtTableModel model) {
        this.model = model;
        String title = model.getHeaderList().getListTitle();
        if (title != null) {
            header.setText(title);
        }
        draw();
    }

    @Override
    public void setClicked(int clickedno) {
        Selection selr = Selection.createRowSelection(clickedno);
        JsArray<Selection> sel = ta.getSelections();
        sel.setLength(0);
        sel.push(selr);
        ta.setSelections(sel);
        this.clickedNo = clickedno;
        wSize = getS(clickedno, 1);
    }

    public void setModifyRowStyle(IModifyRowStyle iMod) {
    }

    private class H extends SelectHandler {

        private final Table ta;

        H(Table ta) {
            this.ta = ta;
        }

        @Override
        public void onSelect(SelectEvent event) {
            JsArray<Selection> sel = ta.getSelections();
            Selection se = null;
            for (int i = 0; i < sel.length(); i++) {
                se = sel.get(i);
            }
            if (se == null) {
                return;
            }
            clickedNo = -1;
            int row = 0;
            if (se.isRow() || se.isCell()) {
                row = se.getRow();
                clickedNo = row;
            }
            int col = 0;
            if (se.isColumn() || se.isCell()) {
                col = se.getColumn();
            }
            wSize = getS(row, col);
            if (model.getIClicked() != null) {
                model.getIClicked().clicked(getClicked());
            }
            if (iClick != null) {
                iClick.execute();
            }

        }
    }

    @Override
    public void refresh() {
        draw();
    }

    @Override
    public WChoosedLine getClicked() {
        return new WChoosedLine(clickedNo, wSize);
    }

    @Override
    public Widget getGWidget() {
        return ta;
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#setEditable(com.gwtmodel.table.view.table.ChangeEditableRows)
     */
    @Override
    public void setEditable(ChangeEditableRowsParam eParam) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#getVList(int)
     */
    @Override
    public List<IGetSetVField> getVList(int rowno) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#removeSort()
     */
    @Override
    public void removeSort() {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#isSorted()
     */
    @Override
    public boolean isSorted() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#getPageSize()
     */
    @Override
    public int getPageSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.view.table.IGwtTableView#setPageSize(int)
     */
    @Override
    public void setPageSize(int pageSize) {
        // TODO Auto-generated method stub
        
    }

}
