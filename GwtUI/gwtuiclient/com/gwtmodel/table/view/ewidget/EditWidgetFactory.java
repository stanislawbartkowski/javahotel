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
package com.gwtmodel.table.view.ewidget;

import com.google.inject.Inject;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.rdef.IFormLineView;
import java.util.ArrayList;
import java.util.List;

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
                false, false, false, false, false, null));
    }

    public IFormLineView constructCheckField(IVField v, String text) {
        return new FieldCheckField(tFactories, v, text);
    }

    public IFormLineView constructListValuesCombo(IVField v, IDataType dType) {
        GetValueLB lB = new GetValueLB(tFactories, v);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    public IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet, boolean addEmpty) {
        GetValueLB lB = new GetValueLB(tFactories, v, addEmpty);
        AddBoxValues.addValues(v, iGet, lB);
        return lB;
    }

    public void setComboList(IFormLineView i, IGetDataList iGet) {
        GetValueLB lB = (GetValueLB) i;
        AddBoxValues.addValues(i.getV(), iGet, lB);
    }

    private ExtendTextBox.EParam newE(boolean password, boolean area) {
        return new ExtendTextBox.EParam(password, area, false, false, false,
                null);
    }

    public IFormLineView constructPasswordField(IVField v) {
        return new ExtendTextBox(tFactories, v, newE(true, false));
    }

    public IFormLineView constructTextField(IVField v) {
        return constructTextField(v, null);
    }

    public IFormLineView constructTextField(IVField v, IGetDataList iGet) {
        ExtendTextBox.EParam e;
        if (iGet == null) {
            e = newE(false, false);
        } else {
            e = new ExtendTextBox.EParam(false, false, true, iGet, null);
        }
        return new ExtendTextBox(tFactories, v, e);
    }

    public IFormLineView constructLabelTextEdit(IVField v, String la) {
        return new LabelEdit(tFactories, v, newE(false, false), la);
    }

    public IFormLineView constructTextArea(IVField v) {
        return constructTextArea(v, null);
    }

    public IFormLineView constructTextArea(IVField v, IGetDataList iGet) {
        ExtendTextBox.EParam e;
        if (iGet == null) {
            e = newE(false, true);
        } else {
            e = new ExtendTextBox.EParam(true, false, true, iGet, null);
        }
        return new ExtendTextBox(tFactories, v, e);
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
        return new ListFieldWithHelp(
                tFactories,
                v,
                dType,
                new ExtendTextBox.EParam(false, false, true, false, false, null));
    }

    public IFormLineView constructListComboValuesHelp(IVField v, IDataType dType) {
        GetValueLB lB = new ListBoxWithHelp(tFactories, v, dType);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    private ExtendTextBox.EParam newC(boolean cEnable) {
        return new ExtendTextBox.EParam(false, false, false, true, cEnable,
                null);
    }

    public IFormLineView constructTextCheckEdit(IVField v, boolean checkenable) {
        return new ExtendTextBox(tFactories, v, newC(checkenable));
    }

    public IFormLineView constructEditFileName(IVField v) {
        return new FileChooser(tFactories, v);
    }

    private List<ComboVal> createVals(List<String> ma) {
        List<ComboVal> vals = new ArrayList<ComboVal>();
        for (String s : ma) {
            vals.add(new ComboVal(s));
        }
        return vals;
    }

    public IFormLineView constructListCombo(IVField v, List<String> ma) {
        return new ComboBoxField(tFactories, v, createVals(ma));
    }

    public IFormLineView constructListCombo(IVField v, List<String> ma,
            boolean addEmpty) {
        return new ComboBoxField(tFactories, v, createVals(ma), addEmpty);
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
            return constructCheckField(v, null);
        default:
            if (v.getType().getLi() != null) {
                return constructListCombo(v);
            }
            return constructTextField(v);
        }

    }
}
