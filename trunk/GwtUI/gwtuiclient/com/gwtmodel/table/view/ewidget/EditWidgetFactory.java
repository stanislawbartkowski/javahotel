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
package com.gwtmodel.table.view.ewidget;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.rdef.IFormLineView;

public class EditWidgetFactory {

    private final IGetCustomValues cValues;

    @Inject
    public EditWidgetFactory(IGetCustomValues cValues) {
        this.cValues = cValues;
    }

    public IFormLineView constructLabelField(IVField v, String displayName) {
        return new VLabel(v, displayName);
    }

    public IFormLineView constructAnchorField(IVField v) {
        return new AnchorField(v);
    }

    // used
    public RadioBoxString constructRadioBoxString(IVField v, IGetDataList iGet,
            final boolean enable, String htmlName) {
        return new RadioBoxString(cValues, v, iGet, enable, htmlName);
    }

    // used
    public IFormLineView contructCalculatorNumber(IVField v, String htmlName) {
        return new NumberCalculator(cValues, v, new ExtendTextBox.EParam(false,
                false, false, false, false, false, false, null, null), htmlName);
    }

    // used
    public IFormLineView constructCheckField(IVField v, String text,
            String htmlName) {
        return new FieldCheckField(cValues, v, text, htmlName);
    }

    public IFormLineView constructListValuesCombo(IVField v, IDataType dType,
            String htmlName) {
        GetValueLB lB = new GetValueLB(cValues, v, htmlName);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    // used
    public IFormLineView constructListValuesCombo(IVField v, IGetDataList iGet,
            boolean addEmpty, String htmlName) {
        GetValueLB lB = new GetValueLB(cValues, v, addEmpty, htmlName);
        AddBoxValues.addValues(v, iGet, lB);
        return lB;
    }

    @SuppressWarnings("unused")
    private void setComboList(IFormLineView i, IGetDataList iGet) {
        GetValueLB lB = (GetValueLB) i;
        AddBoxValues.addValues(i.getV(), iGet, lB);
    }

    private ExtendTextBox.EParam newE(boolean password, boolean area) {
        return new ExtendTextBox.EParam(password, false, false, area, false,
                false, false, null, null);
    }

    public IFormLineView constructHelperList(IVField v, IDataType dType,
            boolean refreshAlways, String htmlName) {
        ExtendTextBox.EParam e = new ExtendTextBox.EParam(false, true, false,
                false, false, false, false, null, null);
        return new ListFieldWithHelp(cValues, v, dType, e, refreshAlways,
                htmlName);

    }

    // used
    public IFormLineView constructPasswordField(IVField v, String htmlName) {
        return new ExtendTextBox(cValues, v, newE(true, false), htmlName);
    }

    // used
    public IFormLineView constructTextField(IVField v, String htmlName) {
        return constructTextField(v, null, null, false, false, false, htmlName);
    }

    public IFormLineView constructTextField(IVField v, IGetDataList iGet,
            IRequestForGWidget iHelper, boolean textarea, boolean richtext,
            boolean refreshAlways, String htmlName) {
        ExtendTextBox.EParam e;
        boolean panel = (iGet != null || iHelper != null);
        e = new ExtendTextBox.EParam(false, panel, false, textarea, false,
                richtext, false, iGet, null);
        if (iHelper == null)
            return new ExtendTextBox(cValues, v, e, htmlName);
        return new EditTextFieldWithHelper(cValues, v, e, iHelper,
                refreshAlways, htmlName);
    }

    @SuppressWarnings("unused")
    private IFormLineView constructLabelTextEdit(IVField v, String la,
            String htmlName) {
        return new LabelEdit(cValues, v, newE(false, false), la, htmlName);
    }

    // used
    public IFormLineView construcDateBoxCalendar(IVField v, String htmlName) {
        return new DateBoxCalendar(cValues, v, htmlName);
    }

    public IFormLineView constructDateBoxCalendarWithHelper(IVField v,
            IRequestForGWidget i, boolean refreshAlways, String htmlName) {
        return new DateBoxWithHelper(cValues, v, i, refreshAlways, htmlName);
    }

    @SuppressWarnings("unused")
    private IFormLineView constructBoxSelectField(IVField v, List<ComboVal> wy,
            String htmlName) {
        return new ComboBoxField(cValues, v, wy, htmlName);
    }

    public IFormLineView constructRadioSelectField(IVField v, String htmlName) {
        return new RadioBoxField(cValues, v, htmlName);
    }

    public IFormLineView constructListComboValuesHelp(IVField v,
            IDataType dType, String htmlName) {
        GetValueLB lB = new ListBoxWithHelp(cValues, v, dType, htmlName);
        AddBoxValues.addValues(dType, lB);
        return lB;
    }

    // used
    public IFormLineView constructEditFileName(IVField v, String htmlName) {
        return new FileChooser(cValues, v, htmlName);
    }

    private List<ComboVal> createVals(List<String> ma) {
        List<ComboVal> vals = new ArrayList<ComboVal>();
        for (String s : ma) {
            vals.add(new ComboVal(s));
        }
        return vals;
    }

    // used
    public IFormLineView constructListCombo(IVField v, List<String> ma,
            String htmlName) {
        return new ComboBoxField(cValues, v, createVals(ma), htmlName);
    }

    public IFormLineView constructListCombo(IVField v, List<String> ma,
            boolean addEmpty, String htmlName) {
        return new ComboBoxField(cValues, v, createVals(ma), addEmpty, htmlName);
    }

    public IFormLineView constructListCombo(IVField v, String htmlName) {
        return new ComboListBoxField(cValues, v, htmlName);
    }

    private IFormLineView constructListComboEnum(IVField v, String htmlName) {
        List<String> la = new ArrayList<String>();
        la.addAll(v.getType().getE().getValues());
        return constructListCombo(v, la, htmlName);
    }

    public IFormLineView constructEditWidget(IVField v, String htmlName) {
        FieldDataType.IFormLineViewFactory fa = v.getType().getiFactory();
        if (fa != null) {
            return fa.construct(v);
        }
        switch (v.getType().getType()) {
        case DATETIME:
            return new DateTimePicker(cValues, v, htmlName);
        case DATE:
            return construcDateBoxCalendar(v, htmlName);
        case INT:
        case LONG:
        case BIGDECIMAL:
            if (v.getType().getLi() != null) {
                return constructListCombo(v, htmlName);
            }
            return contructCalculatorNumber(v, htmlName);
        case ENUM:
            return constructListComboEnum(v, htmlName);
        case BOOLEAN:
            return constructCheckField(v, null, htmlName);
        default:
            if (v.getType().getLi() != null) {
                return constructListCombo(v, htmlName);
            }
            return constructTextField(v, htmlName);
        }

    }
}
