/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import java.math.BigDecimal;
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
import com.gwtmodel.table.view.grid.GridErrorMess;
import com.gwtmodel.table.view.grid.GridViewFactory;
import com.gwtmodel.table.view.grid.IGridView;
import com.gwtmodel.table.view.grid.IGridViewBoolean;
import com.gwtmodel.table.view.grid.IGridViewDecimal;
import com.jythonui.client.M;
import com.jythonui.client.variables.ISetGetVar;
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

public class FormGridManager implements ISetGetVar {

    private final Map<String, IDataType> sData = new HashMap<String, IDataType>();
    // private final Map<String, IGridViewBoolean> gData = new HashMap<String,
    // IGridViewBoolean>();
    private final Map<String, IGridView> gData = new HashMap<String, IGridView>();
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

        private final IGridView gView;

        GridSlotable(IGridView gView, String id) {
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
        DialogFormat d = dContainer.getD();
        CheckList cCheck = d.findCheckList(id);
        assert cCheck != null;
        IGridView gView;
        if (cCheck.isBoolean())
            gView = gFactory.constructBoolean(true, true, true);
        else
            gView = gFactory.constructDecimal(true, true, true);
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
            IGridView gView = gData.get(s);
            CheckList cCheck = d.findCheckList(s);
            if (cCheck == null) continue;
// 2013/10/29            
//            assert cCheck != null;
            if (gView == null)
                Utils.errAlertB(M.M().NoCheckList(s));
            IGridViewBoolean bView = null;
            IGridViewDecimal dView = null;
            if (cCheck.isBoolean())
                bView = (IGridViewBoolean) gView;
            else
                dView = (IGridViewDecimal) gView;

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
                        if (bView != null)
                            bView.setRowBoolean(r, co, false);
                        else
                            dView.setRowDecimal(r, co, null);
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
                    int colL = rCol.findCol(col);
                    if (bView != null) {
                        Boolean val = rI.get(r, ICommonConsts.CHECKLINEVALUE)
                                .getValueB();
                        bView.setRowBoolean(rowL, colL, val);
                    } else {
                        BigDecimal b = rI.get(r, ICommonConsts.CHECKLINEVALUE)
                                .getValueBD();
                        dView.setRowDecimal(rowL, colL, b);
                    }
                }
            }
            if (cCheck.isReadOnly())
                gView.setReadOnly(true);
            checkError(s, v);
        } // for

    }

    private boolean checkError(String checkId, DialogVariables v) {
        DialogFormat d = dContainer.getD();
        IGridView gView = gData.get(checkId);
        CheckList cCheck = d.findCheckList(checkId);
        RowIndex rI = new RowIndex(cCheck.constructErrLine());
        RowsCols rCol = rMap.get(checkId);
        DialogCheckVariables c = v.getCheckVariables().get(checkId);
        if (c == null)
            return false;
        // 2013/06/04 -- check later, does not work for checkbox ?
        List<GridErrorMess> errList = new ArrayList<GridErrorMess>();
        for (RowContent r : c.getErrors().getRowList()) {
            String row = rI.get(r, ICommonConsts.CHECKERRORROW).getValueS();
            String col = rI.get(r, ICommonConsts.CHECKERRORCOL).getValueS();
            String errmess = rI.get(r, ICommonConsts.CHECKERRORMESS)
                    .getValueS();
            int rowNo = rCol.findLine(row);
            int colNo = rCol.findCol(col);
            GridErrorMess err = new GridErrorMess(rowNo, colNo, errmess);
            errList.add(err);
        }
        if (!errList.isEmpty()) {
            gView.setErrorMess(errList);
            return true;
        }
        return false;
    }

    @Override
    public void addToVar(DialogVariables var, String buttonId) {
        DialogFormat d = dContainer.getD();
        for (String s : gData.keySet()) {
            IGridView gView = gData.get(s);
            assert gView != null;
            CheckList cCheck = DialogFormat.findE(d.getCheckList(), s);
            assert cCheck != null;
            IGridViewBoolean bView = null;
            IGridViewDecimal dView = null;
            if (cCheck.isBoolean())
                bView = (IGridViewBoolean) gView;
            else
                dView = (IGridViewDecimal) gView;
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
                    FieldValue valId = new FieldValue();
                    valId.setValue(col);
                    rI.setRowField(rowC, cCheck.getLines().getId(), valId);
                    valId = new FieldValue();
                    if (bView != null) {
                        Boolean val = bView.getCellBoolean(rowL, colL);
                        valId.setValue(val);
                    } else {
                        BigDecimal val = dView.getCellDecimal(rowL, colL);
                        valId.setValue(val, cCheck.getAfterDot());
                    }
                    rI.setRowField(rowC, ICommonConsts.CHECKLINEVALUE, valId);
                    lRows.addRow(rowC);
                    colL++;
                }
                rowL++;
                c.getVal().put(row, lRows);
            }
            var.getCheckVariables().put(s, c);
            // checkError(s,v);
        }
    }

    void modifAttr(String checkId, String action, FieldValue val) {
        IGridView g = gData.get(checkId);
        if (g == null)
            return;
        if (!action.equals(ICommonConsts.READONLY))
            return;
        g.setReadOnly(val != null);
    }

    boolean okGridErrors(DialogVariables v) {
        boolean ok = true;
        for (String s : gData.keySet()) {
            if (checkError(s, v))
                ok = false;
        }
        return ok;
    }

    @Override
    public void readVar(DialogVariables var) {

    }
}
