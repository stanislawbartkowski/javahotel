/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.jythonui.client.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.grid.IGridViewBoolean;
import com.jythonui.client.M;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.CheckListElem;
import com.jythonui.shared.DialogCheckVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class FormGridManager {

    private final Map<String, IDataType> sData = new HashMap<String, IDataType>();
    private final Map<String, IGridViewBoolean> gData = new HashMap<String, IGridViewBoolean>();
    private final GridViewFactory gFactory = GwtGiniInjector.getI()
            .getGridViewFactory();
    private final DialogContainer dContainer;
    private final Map<String, RowsCols> rMap = new HashMap<String, RowsCols>();
    private final IDataType publishType;

    private class RowsCols {
        final List<String> lines = new ArrayList<String>();
        final List<String> cols = new ArrayList<String>();
        final String checkS;

        RowsCols(String checkS) {
            this.checkS = checkS;
        }

        private int findR(List<String> li, String s) {
            int i = 0;
            for (String ss : li) {
                if (ss.equals(s))
                    return i;
                i++;
            }
            return -1;

        }

        int findLine(String row) {
            int l = findR(lines, row);
            if (l == -1)
                Utils.errAlertB(M.M().CannotFindRowInCheckRows(checkS, row));
            return l;

        }

        int findCol(String col) {
            int l = findR(cols, col);
            if (l == -1)
                Utils.errAlertB(M.M().CannotFindColInCheckColumns(checkS, col));
            return l;
        }
    }

    FormGridManager(DialogContainer dContainer, IDataType publishType) {
        this.dContainer = dContainer;
        this.publishType = publishType;
    }

    void addDataType(String id, IDataType dType) {
        sData.put(id, dType);
    }

    private class GridSlotable extends AbstractSlotContainer {

        private final IGridViewBoolean gView;

        GridSlotable(IGridViewBoolean gView, String id) {
            this.gView = gView;
            dType = sData.get(id);
            gData.put(id, gView);
        }

        @Override
        public void startPublish(CellId cellId) {
            publish(publishType, cellId, gView);
            //
            // SlU.publishWidget(dType, this, gView);
        }

    }

    ISlotable constructSlotable(String id) {
        IGridViewBoolean gView = gFactory.constructBoolean(true, true, true);
        return new GridSlotable(gView, id);
    }

    private void readLines(List<String> idS, List<String> dispS,
            CheckListElem cElem, ListOfRows lRows) {
        RowIndex rI = new RowIndex(cElem.constructCol());
        for (RowContent r : lRows.getRowList()) {
            String idC = rI.get(r, cElem.getId()).getValueS();
            String idName = rI.get(r, cElem.getDisplayName()).getValueS();
            idS.add(idC);
            dispS.add(idName);
        }
    }

    void addLinesAndColumns(String id, DialogVariables v) {
        DialogFormat d = dContainer.getD();
        for (String s : v.getCheckVariables().keySet()) {
            // DialogCheckVariables c
            IGridViewBoolean gView = gData.get(s);
            if (gView == null)
                Utils.errAlertB(M.M().NoCheckList(s));

            CheckList cCheck = DialogFormat.findE(d.getCheckList(), s);
            assert cCheck != null;
            DialogCheckVariables c = v.getCheckVariables().get(s);
            if (!c.getLines().getRowList().isEmpty()
                    && !c.getColumns().getRowList().isEmpty()) {
                RowsCols rCols = new RowsCols(s);
                List<String> linesNames = new ArrayList<String>();
                List<String> colsNames = new ArrayList<String>();
                readLines(rCols.lines, linesNames, cCheck.getLines(),
                        c.getLines());
                readLines(rCols.cols, colsNames, cCheck.getColumns(),
                        c.getColumns());
                rMap.put(s, rCols);
                gView.setCols(cCheck.getDisplayName(), colsNames);
                gView.setRowBeginning(linesNames);
                gView.setColNo(colsNames.size());
                gView.setRowNo(linesNames.size());
                for (int r = 0; r < linesNames.size(); r++)
                    for (int co = 0; co < colsNames.size(); co++)
                        gView.setRowBoolean(r, co, false);
            }
            for (String row : c.getVal().keySet()) {
                ListOfRows lRows = c.getVal().get(row);
                RowIndex rI = new RowIndex(cCheck.constructValLine());
                RowsCols rCol = rMap.get(s);
                if (rCol == null)
                    Utils.errAlertB(M.M().CheckListWithoutLinesOrColumns(s));

                int rowL = rCol.findLine(row);
                for (RowContent r : lRows.getRowList()) {
                    String col = rI.get(r, cCheck.getLines().getId())
                            .getValueS();
                    Boolean val = rI.get(r, ICommonConsts.CHECKLINEVALUE)
                            .getValueB();
                    int colL = rCol.findCol(col);
                    gView.setRowBoolean(rowL, colL, val);
                }
            }
            if (cCheck.isReadOnly())
                gView.setReadOnly(true);
        }

    }

    public void addValues(DialogVariables v) {
        DialogFormat d = dContainer.getD();
        for (String s : gData.keySet()) {
            IGridViewBoolean gView = gData.get(s);
            assert gView != null;
            CheckList cCheck = DialogFormat.findE(d.getCheckList(), s);
            assert cCheck != null;
            RowsCols rCol = rMap.get(s);
            if (rCol == null)
                continue;
            DialogCheckVariables c = new DialogCheckVariables();
            int rowL = 0;
            RowIndex rI = new RowIndex(cCheck.constructValLine());
            for (String row : rCol.lines) {
                int colL = 0;
                ListOfRows lRows = new ListOfRows();
                for (String col : rCol.cols) {
                    RowContent rowC = rI.constructRow();
                    Boolean val = gView.getCellBoolean(rowL, colL);
                    FieldValue valId = new FieldValue();
                    valId.setValue(col);
                    rI.setRowField(rowC, cCheck.getLines().getId(), valId);
                    valId = new FieldValue();
                    valId.setValue(val);
                    rI.setRowField(rowC, ICommonConsts.CHECKLINEVALUE, valId);
                    lRows.addRow(rowC);
                    colL++;
                }
                rowL++;
                c.getVal().put(row, lRows);
            }
            v.getCheckVariables().put(s, c);
        }
    }

    void modifAttr(String checkId, String action, FieldValue val) {
        IGridViewBoolean g = gData.get(checkId);
        if (g == null)
            return;
        if (!action.equals(ICommonConsts.READONLY))
            return;
        g.setReadOnly(val != null);
    }
}
