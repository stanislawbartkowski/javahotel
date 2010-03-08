/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormLineView;

public class EditWidgetFactory {

    private final ITableCustomFactories tFactories;

    @Inject
    public EditWidgetFactory(ITableCustomFactories tFactories) {
        this.tFactories = tFactories;
    }

    public RadioBoxString constructRadioBoxString(IGetDataList iGet,
            final boolean enable) {
        return new RadioBoxString(tFactories, iGet, enable);
    }

    public IFormLineView contructCalculatorNumber() {
        return new NumberCalculator(tFactories, 2);
    }

    public IFormLineView contructCalculatorNumber(int afterdot) {
        return new NumberCalculator(tFactories, afterdot);
    }

    public IFormLineView constructCheckField() {
        return new FieldCheckField(tFactories);
    }

    public IFormLineView constructListValuesCombo(IDataType dType) {
        GetValueLB lB = new GetValueLB(tFactories);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    public IFormLineView constructListValuesCombo(IGetDataList iGet) {
        GetValueLB lB = new GetValueLB(tFactories);
        AddBoxValues.addValues(iGet, lB);
        return lB;
    }

    public IFormLineView constructPasswordField() {
        return new FieldTextField(tFactories, true);
    }

    public IFormLineView constructTextField() {
        return new FieldTextField(tFactories, false);
    }

    public IFormLineView construcDateBoxCalendar() {
        return new DateBoxCalendar(tFactories);
    }

    public IFormLineView constructBoxSelectField(List<ComboVal> wy) {
        return new ComboBoxField(tFactories, wy);
    }

    public IFormLineView constructRadioSelectField(String zName,
            List<ComboVal> wy) {
        return new RadioBoxField(tFactories, zName, wy);
    }

    public IFormLineView constructListValuesHelp(IDataType dType) {
        return new ListFieldWithHelp(tFactories, dType);
    }

    public IFormLineView constructListComboValuesHelp(IDataType dType) {
        GetValueLB lB = new ListBoxWithHelp(tFactories, dType);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    private class GetValueCheck extends ExtendTextBox {

        GetValueCheck(ITableCustomFactories tFactories, boolean checkenable) {
            super(tFactories, false, checkenable);
        }
    }

    public IFormLineView constructTextCheckEdit(boolean checkenable) {
        return new GetValueCheck(tFactories, checkenable);
    }

    public IFormLineView constructListCombo(Map<String, String> ma) {
        List<ComboVal> vals = new ArrayList<ComboVal>();
        for (String s : ma.keySet()) {
            String val = ma.get(s);
            vals.add(new ComboVal(s, val));
        }
        return new ComboBoxField(tFactories, vals);
    }
}
