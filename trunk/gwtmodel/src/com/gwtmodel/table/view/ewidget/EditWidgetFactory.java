/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.inject.Inject;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormLineView;

public class EditWidgetFactory {

    private final ITableCustomFactories tFactories;

    @Inject
    public EditWidgetFactory(ITableCustomFactories tFactories) {
        this.tFactories = tFactories;
    }

    public RadioBoxString constructRadioBoxString(IVField v, IGetDataList iGet,
            final boolean enable) {
        return new RadioBoxString(tFactories, v, iGet, enable);
    }

    public IFormLineView contructCalculatorNumber(IVField v) {
        return new NumberCalculator(tFactories, v, new ExtendTextBox.EParam(
                false, false, false, false, false));
    }

    public IFormLineView constructCheckField(IVField v) {
        return new FieldCheckField(tFactories, v);
    }

    public IFormLineView constructListValuesCombo(IVField v, IDataType dType) {
        GetValueLB lB = new GetValueLB(tFactories, v);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    public IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet) {
        GetValueLB lB = new GetValueLB(tFactories, v);
        AddBoxValues.addValues(iGet, lB);
        return lB;
    }

    private ExtendTextBox.EParam newE(boolean password, boolean area) {
        return new ExtendTextBox.EParam(password, area, false, false, false);
    }

    public IFormLineView constructPasswordField(IVField v) {
        return new ExtendTextBox(tFactories, v, newE(true, false));
    }

    public IFormLineView constructTextField(IVField v) {
        return new ExtendTextBox(tFactories, v, newE(false, false));
    }

    public IFormLineView constructLabelTextEdit(IVField v, String la) {
        return new LabelEdit(tFactories, v, newE(false, false), la);
    }

    public IFormLineView constructTextArea(IVField v) {
        return new ExtendTextBox(tFactories, v, newE(false, true));
    }

    public IFormLineView constructRichTextArea(IVField v) {
        return new ExtendTextBox(tFactories, v, new ExtendTextBox.EParam(false,
                false));
    }

    public IFormLineView construcDateBoxCalendar(IVField v) {
        return new DateBoxCalendar(tFactories, v);
    }

    public IFormLineView constructBoxSelectField(IVField v, List<ComboVal> wy) {
        return new ComboBoxField(tFactories, v, wy);
    }

    public IFormLineView constructRadioSelectField(IVField v) {
        return new RadioBoxField(tFactories, v);
    }

    public IFormLineView constructListValuesHelp(IVField v, IDataType dType) {
        return new ListFieldWithHelp(tFactories, v, dType,
                new ExtendTextBox.EParam(false, false, true, false, false));
    }

    public IFormLineView constructListComboValuesHelp(IVField v, IDataType dType) {
        GetValueLB lB = new ListBoxWithHelp(tFactories, v, dType);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    private ExtendTextBox.EParam newC(boolean cEnable) {
        return new ExtendTextBox.EParam(false, false, false, true, cEnable);
    }

    public IFormLineView constructTextCheckEdit(IVField v, boolean checkenable) {
        return new ExtendTextBox(tFactories, v, newC(checkenable));
    }

    public IFormLineView constructListCombo(IVField v, List<String> ma) {
        List<ComboVal> vals = new ArrayList<ComboVal>();
        for (String s : ma) {
            vals.add(new ComboVal(s));
        }
        return new ComboBoxField(tFactories, v, vals);
    }

    public IFormLineView constructListCombo(IVField v) {
        return new ComboListBoxField(tFactories, v);
    }

    public IFormLineView constructListComboEnum(IVField v) {
        List<String> la = new ArrayList<String>();
        la.addAll(v.getType().getE().getValues());
        return constructListCombo(v, la);
    }

    public IFormLineView constructEditWidget(IVField v) {
        FieldDataType.IFormLineViewFactory fa = v.getType().getiFactory();
        if (fa != null) {
            return fa.construct(v);
        }
        switch (v.getType().getType()) {
        case DATE:
            return construcDateBoxCalendar(v);
        case INT:
        case LONG:
        case BIGDECIMAL:
            if (v.getType().getLi() != null) {
                return constructListCombo(v);
            }
            return contructCalculatorNumber(v);
        case ENUM:
            return constructListComboEnum(v);
        case BOOLEAN:
            return constructCheckField(v);
        default:
            if (v.getType().getLi() != null) {
                return constructListCombo(v);
            }
            return constructTextField(v);
        }

    }
}
