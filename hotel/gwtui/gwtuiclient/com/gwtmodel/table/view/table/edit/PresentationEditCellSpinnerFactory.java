/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.table.edit;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.ILostFocusEdit;
import com.gwtmodel.table.view.table.util.EditableCol;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IStartEditRow;

class PresentationEditCellSpinnerFactory extends PresentationEditCellHelper {

    // <label for="age">
    // I am
    // <input type="number" name="age" id="age"
    // min="18" max="120" step="1" value="18">
    // years old
    // </label>

    public interface InputSpinnerCell extends SafeHtmlTemplates {

        @Template("<input type=\"number\" min=\"{0}\" max=\"{1}\" value=\"{2}\" style=\"{3}\" class=\"{4}\"></input>")
        SafeHtml input(String min, String max, String value, String inputStyle,
                String inputClass);
    }

    private final static InputSpinnerCell templateClass = GWT
            .create(InputSpinnerCell.class);

    PresentationEditCellSpinnerFactory(ErrorLineInfo errorInfo,
            CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
            EditableCol eCol, IStartEditRow iStartEdit) {
        super(errorInfo, table, lostFocus, eCol, iStartEdit);
    }

    private class EditNumberSpinnerCell extends EditNumberCell {

        private final IVField v;

        EditNumberSpinnerCell(IVField v, VListHeaderDesc he) {
            super(he);
            this.v = v;
        }

        @Override
        public void render(Context context, String value, SafeHtmlBuilder sb) {
            customRender(context, value, sb, new ICustomEditStringRender() {

                @Override
                public void render(final SafeHtmlBuilder sb, MutableInteger i,
                        final String value) {
                    addInputSbI(i, he, new InsertStyleAndClass() {

                        @Override
                        public void set(String inputStyle, String inputClass) {
                            sb.append(templateClass.input(
                                    Integer.toString(he.getiSpinner().min()),
                                    Integer.toString(he.getiSpinner().max()),
                                    getS(value), inputStyle, inputClass));
                        }
                    });

                }
            });
        }
    }

    @SuppressWarnings("rawtypes")
    Column constructSpinnerCol(VListHeaderDesc he) {
        IVField v = he.getFie();
        Column co = new TColumnEdit(v, new EditNumberSpinnerCell(v, he));
        return co;
    }

}
