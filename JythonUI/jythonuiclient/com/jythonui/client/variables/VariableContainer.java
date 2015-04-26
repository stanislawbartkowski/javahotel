/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

/**
 * @author hotel
 * 
 */
class VariableContainer implements IVariablesContainer {

    private class FormContainer {
        ISetGetVar l[];
        IBackAction iAction;
        String dialogName;
    }

    private final List<FormContainer> fList = new ArrayList<FormContainer>();

    @Override
    public void addFormVariables(String dialogName, IBackAction iAction,
            ISetGetVar... vars) {
        FormContainer f = new FormContainer();
        f.l = vars;
        f.iAction = iAction;
        f.dialogName = dialogName;
        fList.add(f);

    }

    @Override
    public DialogVariables getVariables(String buttonId) {
        DialogVariables v = new DialogVariables();
        for (int k = 0; k < fList.size(); k++) {
            FormContainer f = fList.get(k);
            String b = null;
            // pass buttonId only for the last container
            if (k == fList.size() - 1)
                b = buttonId;
            for (int i = 0; i < f.l.length; i++)
                f.l[i].addToVar(v, b);
        }
        return v;
    }

    public VariableContainer cloneIt() {
        VariableContainer v = new VariableContainer();
        for (FormContainer f : fList)
            v.fList.add(f);
        return v;
    }

    @Override
    public void setVariablesToForm(final DialogVariables var) {

        ISetGetVar.IReadVarContext iC = new ISetGetVar.IReadVarContext() {

            private final Set<String> se = new HashSet<String>();

            @Override
            public Set<String> getSet() {
                return se;
            }

        };

        for (int f = fList.size() - 1; f >= 0; f--) {
            FormContainer fo = fList.get(f);
            for (int i = 0; i < fo.l.length; i++)
                fo.l[i].readVar(var, iC);
        }
    }

    private void setVariableToForm(ISlotable iSlo, IDataType dType,
            DialogVariables v, IVField vv) {
        FieldValue val = v.getValue(vv.getId());
        if (val == null)
            return;
        IFormLineView i = SlU.getVWidget(dType, iSlo, vv);
        if (i == null)
            return;
        i.setValObj(val.getValue());
    }

    @Override
    public void copyCurrentVariablesToForm(ISlotable iSlo, IDataType dType) {
        DialogVariables var = getVariables(null);
        FormLineContainer fC = SlU.getFormLineContainer(dType, iSlo);
        for (FormField ff : fC.getfList()) {
            IVField v1 = ff.getFie();
            IVField v2 = ff.getFRange();
            setVariableToForm(iSlo, dType, var, v1);
            if (v2 != null)
                setVariableToForm(iSlo, dType, var, v2);
        }

    }

    @Override
    public List<IBackAction> getList() {
        List<IBackAction> bList = new ArrayList<IBackAction>();
        for (FormContainer f : fList)
            bList.add(0, f.iAction);
        return bList;
    }

    @Override
    public String getDialogName() {
        if (fList.isEmpty())
            return null;
        return fList.get(fList.size() - 1).dialogName;
    }
}
