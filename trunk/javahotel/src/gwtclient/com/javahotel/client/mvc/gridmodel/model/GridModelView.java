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
package com.javahotel.client.mvc.gridmodel.model;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;
import com.javahotel.client.mvc.gridmodel.model.view.IGridView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class GridModelView implements IGridModelView {

    private List<String> rows = null;
    private List<ColsHeader> cols = null;
    private int tNo;
    private List<C> q = new ArrayList<C>();
    private boolean enabled = true;
    private final IGridView iGrid;

    public void setEnable(final boolean enable) {
        iGrid.setEnable(RowNo(), ColNo(), enable);
    }

    public List<String> getSRow() {
        return rows;
    }

    public List<ColsHeader> getSCol() {
        return cols;
    }

    public void show() {
    }

    public void hide() {
    }

    public void reset() {
        tNo = 0;
    }

    public IMvcWidget getMWidget() {
        return iGrid.getMWidget();
    }

    private class C {

        private final int no;
        private final boolean rows;
        private final List<? extends Object> a;

        C(int no, boolean rows, List<? extends Object> a) {
            this.no = no;
            this.rows = rows;
            this.a = a;
        }
    }

    GridModelView(IGridView iGrid) {
        this.iGrid = iGrid;
        reset();
    }

    private void nextT() {
        tNo++;
        if (tNo >= 2) {
            for (int r = 0; r < RowNo(); r++) {
                iGrid.setRowWidget(r, ColNo());
            }
            for (C c : q) {
                drawA(c);
            }
            q.clear();
            if (tNo == 2) {
                setEnable(enabled);
            }
        }
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
        iGrid.setRowBeginning(rows);
        nextT();
    }

    public void setCols(ColsHeader rowTitle, List<ColsHeader> cols) {
        this.cols = cols;
        iGrid.setCols(rowTitle, cols);
        nextT();
    }

    private void walk(boolean rows, int no, IGridModelViewVisitor vi) {
        if (rows) {
            for (int c = 0; c < ColNo(); c++) {
                Object val = iGrid.getCell(no, c);
                vi.visit(c, val);
            }
        } else {
            for (int r = 0; r < RowNo(); r++) {
                Object val = iGrid.getCell(r, no);
                vi.visit(r, val);
            }

        }
    }

    private void setA(C aa) {
        final List<? extends Object> a = aa.a;
        final boolean rows = aa.rows;
        final int no = aa.no;
        IGridModelViewVisitor v = new IGridModelViewVisitor() {

            public void visit(int i, Object val) {
                Object b = null;
                if (i < a.size()) {
                    b = a.get(i);
                }
                if (rows) {
                    iGrid.setRowVal(no, i, b);
                } else {
                    iGrid.setRowVal(i, no, b);
                }
            }
        };
        walk(rows, no, v);
    }

    public void drawA(C c) {
        if (tNo < 2) {
            q.add(c);
        } else {
            setA(c);
        }
    }

    public void setRowVal(int row, List<? extends Object> vals) {
        drawA(new C(row, true, vals));
    }

    public void setColVal(int col, List<? extends Object> vals) {
        drawA(new C(col, false, vals));
    }

    private List<Object> getVal(boolean rows, int c) {
        final List<Object> a = new ArrayList<Object>();
        IGridModelViewVisitor v = new IGridModelViewVisitor() {

            public void visit(int i, Object val) {
                a.add(val);
            }
        };
        walk(rows, c, v);
        return a;
    }

    public List<? extends Object> getRows(int row) {
        return getVal(true, row);
    }

    public List<? extends Object> getCols(int col) {
        return getVal(false, col);
    }

    private int tNo(List a) {
        if (a == null) {
            return -1;
        }
        return a.size();
    }

    public int RowNo() {
        return tNo(rows);
    }

    public int ColNo() {
        return tNo(cols);
    }
}
