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

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.view.table.PresentationEditCellFactory.IStartEditRow;

/**
 * @author hotel
 *
 */
class PresentationCheckEditFactory extends PresentationEditCellHelper {

    PresentationCheckEditFactory(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol,IStartEditRow iStartEdit) {
        super(errorInfo, table, lostFocus, eCol,iStartEdit);
    }
    
    // Decorator pattern implemented to add "blur" event to the constructor
    // Cannot inherit CheckboxCell directly
    private class EditCheckBoxCell extends
            AbstractEditableCell<Boolean, Boolean> implements IGetField {

        private final IVField v;
        private final CheckboxCell checkBox;

        EditCheckBoxCell(IVField v, boolean handleSelection) {
            super(BrowserEvents.CHANGE, BrowserEvents.KEYDOWN,
                    BrowserEvents.BLUR);
            this.checkBox = new CheckboxCell(false, handleSelection);
            this.v = v;
        }

        @Override
        public boolean dependsOnSelection() {
            return checkBox.dependsOnSelection();
        }

        @Override
        public boolean handlesSelection() {
            return checkBox.handlesSelection();
        }

        @Override
        public Object getValObj(MutableInteger key) {
            Boolean b = checkBox.getViewData(key);
            return b;
        }

        @Override
        public void setValObj(MutableInteger key, Object o) {
            Boolean b = (Boolean) o;
            checkBox.setViewData(key, b);
        }

        @Override
        public boolean isEditing(
                com.google.gwt.cell.client.Cell.Context context,
                Element parent, Boolean value) {
            return checkBox.isEditing(context, parent, value);
        }

        @Override
        public void onBrowserEvent(Context context, Element parent,
                Boolean value, NativeEvent event,
                ValueUpdater<Boolean> valueUpdater) {
            String type = event.getType();
            checkBox.onBrowserEvent(context, parent, value, event, valueUpdater);
            afterChange(type, context, v);
        }

        @Override
        public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
            Object key = context.getKey();
            MutableInteger i = (MutableInteger) key;
            boolean editenabled = eCol.isEditable(i.intValue(), v);
            if (editenabled) {
                checkBox.render(context, value, sb);
                return;
            }

            Boolean viewData = getViewData(key);
            if (viewData != null && viewData.equals(value)) {
                clearViewData(key);
                viewData = null;
            }
            if (value) {
                sb.append(INPUT_CHECKED);
            } else {
                sb.append(INPUT_UNCHECKED);
            }

        }
    }
    
    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v, boolean handleSelection) {
        return new CheckBoxColumn(new EditCheckBoxCell(v, handleSelection), v);
    }



}
