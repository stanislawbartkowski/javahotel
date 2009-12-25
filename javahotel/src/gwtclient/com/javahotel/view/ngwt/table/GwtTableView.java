package com.javahotel.view.ngwt.table;

import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.listheadermodel.ListHeaderDesc;

class GwtTableView implements IGwtTableView {

    private final IResLocator rI;
    private final IGwtTableModel model;
    private int clickedNo;
    private final Label header = new Label();
    private final Table ta;
    private DataTable data;

    GwtTableView(final IResLocator rI, IGwtTableModel model) {
        this.model = model;
        this.rI = rI;
        if (model.getTableHeader() != null) {
            header.setText(model.getTableHeader());
        }
        ta = new Table(null, Table.Options.create());
        ta.addSelectHandler(new H(ta));
    }

    private void drawrow(int row) {
        AbstractTo ii = model.getRow(row);
        List<ListHeaderDesc> co = model.getHeaderList().getHeader();
        for (int c = 0; c < co.size(); c++) {
            ListHeaderDesc cl = co.get(c);
            String val = ii.getS(cl.getFie());
            data.setValue(row, c, val);
        }
    }

    private void getTable() {
        data = DataTable.create();
        List<ListHeaderDesc> co = model.getHeaderList().getHeader();
        for (ListHeaderDesc c : co) {
            data.addColumn(ColumnType.STRING, c.getHeaderString());
        }
        data.addRows(model.getRowsNum());
        for (int i = 0; i < model.getRowsNum(); i++) {
            drawrow(i);
        }
    }

    // private IWidgetSize getS(int row, int col) {
    // Element etd = ta.getElement(); // TD
    // // search first TR
    // while ((etd != null) && (!etd.getTagName().equals("TR"))) {
    // etd = etd.getFirstChildElement(); // TABLE
    // }
    //
    // int no = 0;
    // int top = 0;
    // int left = 0;
    // int height = 0;
    // int width = 0;
    // Element etr = etd;
    // while (etr != null) {
    // etr = etr.getNextSiblingElement();
    // if (no == row) {
    // break;
    // }
    // no++;
    // }
    // if (etr != null) {
    // Element echild = etr.getFirstChildElement();
    // int cno = 0;
    // while ((echild != null) && (cno != col)) {
    // echild = echild.getNextSiblingElement();
    // cno++;
    // }
    // if (echild != null) {
    // top = echild.getAbsoluteTop();
    // left = echild.getAbsoluteLeft();
    // height = echild.getOffsetHeight();
    // width = echild.getOffsetWidth();
    // }
    // }
    // return WidgetSizeFactory.getW(top, left, height, width);
    // }

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
        }
    }

    public void refresh() {
        getTable();
        ta.draw(data);
    }

    public int getClicked() {
        return clickedNo;
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(ta);
    }
}