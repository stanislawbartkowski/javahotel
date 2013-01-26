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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.listmodel.RowListDataManager;
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
        RowListDataManager liManager;
        DialogVariables addVar;
    }

    private final List<FormContainer> fList = new ArrayList<FormContainer>();

    @Override
    public void addFormVariables(ISlotable iSlo, IDataType dType,
            RowListDataManager liManager, DialogVariables addVar) {
        FormContainer f = new FormContainer();
        f.iSlo = iSlo;
        f.dType = dType;
        f.liManager = liManager;
        f.addVar = addVar;
        fList.add(f);

    }

    private static void setVariables(DialogVariables v, IVModelData vData) {
        if (vData == null) {
            return;
        }
        for (IVField fie : vData.getF()) {
            Object o = vData.getF(fie);
            // pass empty as null (None)
            // if (o == null) {
            // continue;
            // }
            FieldValue fVal = new FieldValue();
            fVal.setValue(fie.getType().getType(), o, fie.getType()
                    .getAfterdot());
            v.setValue(fie.getId(), fVal);
        }
    }

    @Override
    public DialogVariables getVariables() {
        DialogVariables v = new DialogVariables();
        for (FormContainer f : fList) {
            IVModelData vData = new VModelData();
            vData = f.iSlo.getSlContainer().getGetterIVModelData(f.dType,
                    GetActionEnum.GetViewModelEdited, vData);
            setVariables(v, vData);
            for (IDataType dType : f.liManager.getList()) {
                vData = f.iSlo.getSlContainer().getGetterIVModelData(dType,
                        GetActionEnum.GetListLineChecked);
                boolean setLine = vData != null;
                FieldValue val = new FieldValue();
                val.setValue(setLine);
                v.setValue(f.liManager.getLId(dType) + ICommonConsts.LINESET,
                        val);
                setVariables(v, vData);
            }
            if (f.addVar != null) {
                for (String fie : f.addVar.getFields()) {
                    FieldValue val = f.addVar.getValue(fie);
                    v.setValue(fie, val);
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
                    IFormLineView i = SlU.getVWidget(fo.dType, fo.iSlo, v);
                    if (i == null) {
                        return;
                    }
                    i.setValObj(val.getValue());
                }
            };
            JUtils.visitListOfFields(var, ICommonConsts.JCOPY, vis);
        }
    }

    private void setVariableToForm(ISlotable iSlo, IDataType dType,
            DialogVariables v, IVField vv) {
        FieldValue val = v.getValue(vv.getId());
        if (val == null) {
            return;
        }
        IFormLineView i = SlU.getVWidget(dType, iSlo, vv);
        if (i == null) {
            return;
        }
        i.setValObj(val.getValue());
    }

    @Override
    public void copyCurrentVariablesToForm(ISlotable iSlo, IDataType dType) {
        DialogVariables var = getVariables();
        FormLineContainer fC = SlU.getFormLineContainer(dType, iSlo);
        for (FormField ff : fC.getfList()) {
            IVField v1 = ff.getFie();
            IVField v2 = ff.getFRange();
            setVariableToForm(iSlo, dType, var, v1);
            if (v2 != null) {
                setVariableToForm(iSlo, dType, var, v2);
            }

        }

    }
}
