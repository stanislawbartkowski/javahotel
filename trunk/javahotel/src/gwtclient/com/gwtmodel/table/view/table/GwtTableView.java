/*
 * Copyright 2010 stanislawbartkowski@gmail.com
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

import java.util.List;

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
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormatUtil;
import java.math.BigDecimal;
import java.util.Date;

class GwtTableView implements IGwtTableView {

    private IGwtTableModel model;
    private int clickedNo = -1;
    private WSize wSize;
    private final Label header = new Label();
    private final Table ta;
    private DataTable data;
    private WSize startW;
    private final ICommand iClick;
    private final DateFormat df;

    GwtTableView(ICommand iClick) {
        ta = new Table(null, Table.Options.create());
        ta.addSelectHandler(new H(ta));
        this.iClick = iClick;
        model = null;
        startW = null;
        DateFormat.Options o = DateFormat.Options.create();
        o.setPattern("yyyy'.'MM'.'dd");
        df = DateFormat.create(o);
    }

    private void drawrow(int row) {
        IVModelData ii = model.getRow(row);
        List<VListHeaderDesc> co = model.getHeaderList().getVisHeList();
        for (int c = 0; c < co.size(); c++) {
            VListHeaderDesc cl = co.get(c);
            Object val = ii.getF(cl.getFie());
            String sval = ii.getS(cl.getFie());
            switch (cl.getColType()) {
            case NUMBER:
                BigDecimal b;
                if (val == null) {
                    b = Utils.toBig(sval);
                } else {
                    b = (BigDecimal) val;
                }
                if (b != null) {
                    Double d = Utils.toDouble(b);
                    data.setValue(row, c, d.doubleValue());
                }
                break;
            case INTEGER:
                int ival;
                if (val == null) {
                    ival = CUtil.getNumb(sval);
                } else {
                    Integer i = (Integer) val;
                    ival = i.intValue();
                }
                data.setValue(row, c, ival);
                break;
            case DATE:
                Date dd;
                if (val == null) {
                    dd = DateFormatUtil.toD(sval);
                } else {
                    dd = (Date) val;
                }
                data.setValue(row, c, dd);
                break;
            case BOOLEAN:
                boolean log;
                if (val == null) {
                    log = Utils.TrueL(sval);
                } else {
                    Boolean bl = (Boolean) val;
                    log = bl.booleanValue();
                }
                data.setValue(row, c, log);
                break;
            default:
            case STRING:
                data.setValue(row, c, sval);
                break;
            }
        }
    }

    private ColumnType getCType(VListHeaderDesc c) {
        switch (c.getColType()) {
        case BOOLEAN:
            return ColumnType.BOOLEAN;
        case STRING:
            return ColumnType.STRING;
        case NUMBER:
        case INTEGER:
            return ColumnType.NUMBER;
        case DATE:
            return ColumnType.DATE;
        case DATETIME:
            return ColumnType.DATETIME;
        case TIMEOFDAY:
            return ColumnType.TIMEOFDAY;
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
        int colNo = 1;
        for (VListHeaderDesc c : co) {
            if (c.getColType() == ColumnDataType.DATE) {
                df.format(data, colNo);
            }
            colNo++;
        }
        data.addRows(model.getRowsNum());
        for (int i = 0; i < model.getRowsNum(); i++) {
            drawrow(i);
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
        if (startW != null) {
            int size = Utils.CalculateNOfRows(startW);
            if (size > 0) {
                tao.setPageSize(size);
                tao.setPage(Table.Options.Policy.ENABLE);
            }
        }
        ta.draw(data, tao);
    }

    public void setModel(IGwtTableModel model) {
        this.model = model;
        String title = model.getHeaderList().getListTitle();
        if (title != null) {
            header.setText(title);
        }
        draw();
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

    public void refresh(WSize startW) {
        this.startW = startW;
        draw();
    }

    public WChoosedLine getClicked() {
        return new WChoosedLine(clickedNo, wSize);
    }

    public Widget getGWidget() {
        return ta;

    }
}
