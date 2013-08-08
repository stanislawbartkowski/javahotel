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

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

/**
 *
 * @author perseus
 */
class PresentationEditSelectionCellFactory extends PresentationEditCellHelper {

    PresentationEditSelectionCellFactory(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol, PresentationEditCellFactory.IStartEditRow iStartEdit) {
        super(errorInfo, table, lostFocus, eCol, iStartEdit);
    }

    private class EditSelectionCell extends SelectionCell implements IGetField {

        private final IVField v;
        @SuppressWarnings("unused")
        private final VListHeaderDesc he;
        private final AbstractListT listT;
        private final List<String> helist;

        /**
         * @param options
         */
        public EditSelectionCell(VListHeaderDesc he, List<String> options, AbstractListT listT) {
            super(options);
            this.v = he.getFie();
            this.he = he;
            this.listT = listT;
            this.helist = options;
        }

        @Override
        public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            if (editenabled) {
                if (CUtil.EmptyS(value)) {
                    String pvalue = helist.get(0);
                    this.setViewData(key, pvalue);
                    modifUpdate(false, key, v, null);
                }
                // added 2013/08/09
                String idS = listT.getValueS(value);
                super.render(context, idS, sb);
                return;
            }
            String s = this.getViewData(key);
            if (s == null) {
                s = value;
            }
            sb.append(templateDisplay.input(s));
        }

        @Override
        public Object getValObj(MutableInteger key) {
            String s = this.getViewData(key);
            if (s == null) {
                return null;
            }
            if (listT != null) {
                s = listT.getValue(s);
            }
            return FUtils.getValue(v, s);
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            if (o == null) {
                this.setViewData(key, null);
                return;
            }
            Object oo = o;
            if (listT != null) {
                oo = listT.getValueS((String) o);
            }
            String s = FUtils.getValueOS(oo, v);
            this.setViewData(key, s);
        }

        @Override
        public void onBrowserEvent(Cell.Context context, Element parent,
                String value, NativeEvent event,
                ValueUpdater<String> valueUpdater) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            String eventType = event.getType();
            afterChange(eventType, context, v, new WSize(parent));
        }
    }

    @SuppressWarnings("rawtypes")
    Column constructSelectionCol(VListHeaderDesc he, List<String> lis, AbstractListT listT) {
        assert !lis.isEmpty() : LogT.getT().SelectListCannotBeEmpty();
        final IVField v = he.getFie();
        Column co = new TColumnEdit(v,
                new EditSelectionCell(he, lis, listT));
        return co;
    }
}
