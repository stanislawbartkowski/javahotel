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

import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;

/**
 * 
 * @author hotel
 */
class TableView implements IGwtTableView {

    private final Grid g = new Grid();
    private IGwtTableModel model;
    private final Label header = new Label();
    private final VerticalPanel vp = new VerticalPanel();
    private int clickedNo = -1;
    private WSize wSize;
    private final ICommand iClick;

    public void setModifyRowStyle(IModifyRowStyle iMod) {
    }

    private class ClickRowC implements TableListener {

        public void onCellClicked(final SourcesTableEvents arg0,
                final int arg1, final int arg2) {
            if (arg1 == 0) {
                return;
            }
            clickedNo = arg1 - 1;
            Widget w = g.getWidget(arg1, arg2);
            wSize = new WSize(w);
            if (model.getIClicked() != null) {
                model.getIClicked().clicked(getClicked());
            }
            if (iClick != null) {
                iClick.execute();
            }
        }
    }

    TableView(ICommand iClick) {
        this.iClick = iClick;
        vp.add(header);
        vp.add(g);
        g.addTableListener(new ClickRowC());
    }

    private void draw() {
        if (model == null) {
            return;
        }
        if (!model.containsData()) {
            return;
        }
        List<VListHeaderDesc> co = model.getHeaderList().getVisHeList();
        int cols = co.size();
        int rows = model.getRows().size();
        g.resize(rows + 1, cols);
        int col = 0;
        for (VListHeaderDesc c : co) {
            String s = c.getHeaderString();
            g.setText(0, col, s);
            col++;
        }
        for (int row = 0; row < rows; row++) {
            col = 0;
            IVModelData mo = model.getRows().get(row);
            for (VListHeaderDesc c : co) {
                String s = FUtils.getValueS(mo, c.getFie());
                Label l = new Label(s);
                g.setWidget(row + 1, col, l);
                col++;
            }

        }

    }

    @Override
    public WChoosedLine getClicked() {
        if (clickedNo >= model.getRows().size()) {
            clickedNo = -1;
        }
        return new WChoosedLine(clickedNo, wSize);
    }

    @Override
    public IGwtTableModel getViewModel() {
        return model;
    }

    @Override
    public void refresh() {
        draw();
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
    public Widget getGWidget() {
        return vp;
    }

    @Override
    public void setClicked(int clickedno) {
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

}
