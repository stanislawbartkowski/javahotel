package com.gwtmodel.table.view.table;

import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;

class GwtTableView implements IGwtTableView {

    private IGwtTableModel model;
    private int clickedNo = -1;
    private WSize wSize;
    private final Label header = new Label();
    private final Table ta;
    private DataTable data;
    private WSize startW;

    GwtTableView() {
        ta = new Table(null, Table.Options.create());
        ta.addSelectHandler(new H(ta));
        model = null;
        startW = null;
    }

    private void drawrow(int row) {
        IVModelData ii = model.getRow(row);
        List<VListHeaderDesc> co = model.getHeaderList().getHeList();
        for (int c = 0; c < co.size(); c++) {
            VListHeaderDesc cl = co.get(c);
            String val = ii.getS(cl.getFie());
            data.setValue(row, c, val);
        }
    }

    private void getTable() {
        data = DataTable.create();
        List<VListHeaderDesc> co = model.getHeaderList().getHeList();
        for (VListHeaderDesc c : co) {
            data.addColumn(ColumnType.STRING, c.getHeaderString());
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
