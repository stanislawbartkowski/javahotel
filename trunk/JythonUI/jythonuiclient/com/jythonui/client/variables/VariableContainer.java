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
package com.jythonui.client.variables;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.util.JUtils;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

/**
 * @author hotel
 * 
 */
class VariableContainer implements IVariablesContainer {

    private class FormContainer {
        ISlotable iSlo;
        IDataType dType;
    }

    private final List<FormContainer> fList = new ArrayList<FormContainer>();

    @Override
    public void addFormVariables(ISlotable iSlo, IDataType dType) {
        FormContainer f = new FormContainer();
        f.iSlo = iSlo;
        f.dType = dType;
        fList.add(f);

    }

    private void setVariable(DialogVariables v, IVField vv, FormContainer f) {
        IFormLineView i = SlU.getVWidget(f.dType, f.iSlo, vv);
        Object o = i.getValObj();
        FieldValue fVal = new FieldValue();
        fVal.setValue(vv.getType().getType(), o, fVal.getAfterdot());
        v.setValue(vv.getId(), fVal);
    }

    @Override
    public DialogVariables getVariables() {
        DialogVariables v = new DialogVariables();
        for (FormContainer f : fList) {
            FormLineContainer fC = SlU.getFormLineContainer(f.dType, f.iSlo);
            for (FormField ff : fC.getfList()) {
                IVField v1 = ff.getFie();
                IVField v2 = ff.getFRange();
                setVariable(v, v1, f);
                if (v2 != null) {
                    setVariable(v, v2, f);
                }
            }
        }

        return v;
    }

    public VariableContainer cloneIt() {
        VariableContainer v = new VariableContainer();
        for (FormContainer f : fList) {
            v.fList.add(f);
        }
        return v;
    }

    @Override
    public void setVariablesToForm(final DialogVariables var) {
        for (final FormContainer fo : fList) {
            JUtils.IVisitor vis = new JUtils.IVisitor() {

                @Override
                public void action(String fie, String field) {
                    FieldValue val = var.getValue(fie);
                    if (val == null) {
                        Utils.errAlert(M.M().ErrorNoValue(fie),
                                ICommonConsts.JCOPY + fie);
                        return;
                    }
                    VField v = VField.construct(fie, val.getType());
                    // TODO: check if JCOPY value is define in the form
                    // otherwise throw alert or exception
                    SlU.setVWidgetValue(fo.dType, fo.iSlo, v, val.getValue());

                }
            };
            JUtils.visitListOfFields(var, ICommonConsts.JCOPY, vis);
        }
    }
}
