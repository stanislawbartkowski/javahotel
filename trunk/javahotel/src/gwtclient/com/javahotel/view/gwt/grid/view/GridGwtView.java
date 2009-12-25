/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.view.gwt.grid.view;

import java.math.BigDecimal;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.GridCellType;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.idialog.GetIEditFactory;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.ifield.ISetWidget;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.gridmodel.model.view.IGridView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class GridGwtView implements IGridView {

    private class NumerW extends Composite {

        private final ILineField iF;

        NumerW(ILineField iF) {
            this.iF = iF;
            initWidget(iF.getMWidget().getWidget());
        }
    }
    private final Grid g = new Grid();
    private final GridCellType cType;

    private final IResLocator rI;

    GridGwtView(IResLocator rI, GridCellType cType) {
        this.rI = rI;
        this.cType = cType;
    }

    public Object getCell(int row, int c) {
        Widget w = getW(row, c);
        switch (cType) {
            case BOOLEAN:
                CheckBox ce = (CheckBox) w;
                boolean b = ce.isChecked();
                return new Boolean(b);
            case NUMERIC:
                NumerW nw = (NumerW) w;
                ILineField i = nw.iF;
                BigDecimal bi = i.getDecimal();
                return bi;
        }
        return null;
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(g);
    }

    private Widget getW(int r, int c) {
        Widget w = g.getWidget(r + 1, c + 1);
        if (w == null) {
            w = null;
            switch (cType) {
                case BOOLEAN:
                    CheckBox ce = new CheckBox();
                    w = ce;
                    break;
                case NUMERIC:
                    ILineField eLine = GetIEditFactory.getNumberCalculator(rI);
                    NumerW nw = new NumerW(eLine);
                    ISetWidget i = new ISetWidget() {

                        public void setW(Widget w) {
                            w.setWidth("6em");
                        }
                    };
                    eLine.setWidget(i);
                    w = nw;
                    break;
            }
            g.setWidget(r + 1, c + 1, w);
        }
        return w;
    }

    public Widget getWidget() {
        return g;
    }

    public void setCols(ColsHeader rowTitle, List<ColsHeader> cols) {
        if (cols == null) {
            return;
        }
        int m = cols.size();
        g.resizeColumns(m + 1);
        if (g.getRowCount() == 0) {
            g.resizeRows(1);
        }
        if (rowTitle != null) {
            g.setText(0, 0, rowTitle.getHName());
        }
        for (int i = 0; i < m; i++) {
            g.setText(0, i + 1, cols.get(i).getHName());
        }
    }

    public void setEnable(int rowno, int colno, boolean enable) {
        for (int r = 0; r < rowno; r++) {
            for (int c = 0; c < colno; c++) {
                Widget w = getW(r, c);
                switch (cType) {
                    case BOOLEAN:
                        CheckBox ce = (CheckBox) w;
                        ce.setEnabled(enable);
                        break;
                    case NUMERIC:
                        NumerW nw = (NumerW) w;
                        ILineField i = nw.iF;
                        i.setReadOnly(!enable);
                        break;
                }
            }
        }
    }

    public void setRowBeginning(List<String> rows) {
        if (rows == null) {
            return;
        }
        int m = rows.size();
        g.resizeRows(m + 1);
        if (g.getColumnCount() == 0) {
            g.resizeColumns(1);
        }
        for (int i = 0; i < m; i++) {
            g.setText(i + 1, 0, rows.get(i));
        }
    }

    public void setRowVal(int row, int c, Object o) {
        Widget w = getW(row, c);
        switch (cType) {
            case BOOLEAN:
                Boolean b = (Boolean) o;
                CheckBox ce = (CheckBox) w;
                ce.setChecked(b.booleanValue());
                break;
            case NUMERIC:
                NumerW nw = (NumerW) w;
                ILineField eLine = nw.iF;
                BigDecimal bi = (BigDecimal) o;
                eLine.setDecimal(bi);
                break;
        }
    }

    public void setRowWidget(int row, int colNo) {
        for (int c = 0; c < colNo; c++) {
            Widget w = getW(row, c);
            c++;
        }
    }
}
