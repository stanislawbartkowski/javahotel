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
package com.gwtmodel.table.view.table;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.PresentationEditCellFactory.IStartEditRow;

/**
 * @author hotel
 * 
 */
class PresentationNumbEditFactory extends PresentationEditCellHelper {

    PresentationNumbEditFactory(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol, IStartEditRow iStartEdit) {
        super(errorInfo, table, lostFocus, eCol, iStartEdit);
    }

    private class EditNumberCell extends EditStringCell {

        EditNumberCell(VListHeaderDesc he) {
            super(he);
        }

        @Override
        public Object getValObj(MutableInteger key) {
            ViewData viewData = getViewData(key);
            if (viewData == null) {
                return null;
            }
            return FUtils.getValue(v, viewData.getCurrentValue());
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            String s = FUtils.getValueOS(o, v);
            super.setValObj(key, s);
        }

    }

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(VListHeaderDesc he) {
        IVField v = he.getFie();
        EditNumberCell cce = new EditNumberCell(he);
        return new TColumnEdit(v, cce);
    }

}
