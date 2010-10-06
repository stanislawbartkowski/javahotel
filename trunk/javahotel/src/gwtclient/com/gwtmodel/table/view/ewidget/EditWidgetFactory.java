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

    public IFormLineView contructCalculatorNumber(String fName) {
        return contructCalculatorNumber(2, fName);
    }

    public IFormLineView contructCalculatorNumber(int afterdot, String fName) {
        return new NumberCalculator(tFactories,
                new ExtendTextBox.EParam(false, false, false, false, false, fName),
                afterdot);
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

    private ExtendTextBox.EParam newE(boolean password, boolean area, String fName) {
        return new ExtendTextBox.EParam(password, area, false, false, false, fName);
    }

    public IFormLineView constructPasswordField(String fName) {
//        return new FieldTextField(tFactories, true, false, fName);
        return new ExtendTextBox(tFactories, newE(true, false, fName));
    }

    public IFormLineView constructTextField(String fName) {
//        return new FieldTextField(tFactories, false, false, fName);
        return new ExtendTextBox(tFactories, newE(false, false, fName));
    }

    public IFormLineView constructTextArea(String fName) {
        return new ExtendTextBox(tFactories, newE(false, true, fName));
//        return new FieldTextField(tFactories, false, true, fName);
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

    public IFormLineView constructListValuesHelp(IDataType dType, String fName) {
//        return new ListFieldWithHelp(tFactories, dType, fName);
        return new ListFieldWithHelp(tFactories, dType, new ExtendTextBox.EParam(false, false, true, false, false, fName));
    }

    public IFormLineView constructListComboValuesHelp(IDataType dType) {
        GetValueLB lB = new ListBoxWithHelp(tFactories, dType);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    private ExtendTextBox.EParam newC(boolean cEnable, String fName) {
        return new ExtendTextBox.EParam(false, false, false, true, cEnable, fName);
    }

//    private class GetValueCheck extends ExtendTextBox {
//
//        GetValueCheck(ITableCustomFactories tFactories, boolean checkenable,
//                String fName) {
//            super(tFactories, false, checkenable, fName);
//        }
//    }
    public IFormLineView constructTextCheckEdit(boolean checkenable,
            String fName) {
//        return new GetValueCheck(tFactories, checkenable, fName);
        return new ExtendTextBox(tFactories, newC(checkenable, fName));
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
