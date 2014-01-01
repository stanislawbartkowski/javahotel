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
package com.jythonui.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class RowVModelData extends AVModelData {

    private final RowIndex rI;
    private final RowContent row;
    private final Set<String> defVal = new HashSet<String>();
    private final Map<String, String> defValS = new HashMap<String, String>();
    private IGetStandardMessage iMess = GwtGiniInjector.getI()
            .getStandardMessage();

    public RowVModelData(RowIndex rI) {
        this(rI, rI.constructRow());
    }

    public RowVModelData(RowIndex rI, RowContent row) {
        this.rI = rI;
        this.row = row;
        for (FieldItem i : rI.getColList())
            if (i.isDefValue()) {
                defVal.add(i.getId());
                defValS.put(i.getId(), iMess.getMessage(i.getDefValue()));
            }

    }

    @Override
    public Object getF(IVField fie) {
        Object o = rI.get(row, fie.getId()).getValue();
        if (o != null)
            return o;
        if (!defVal.contains(fie.getId()))
            return o;
        return FUtils.getValue(fie, defValS.get(fie.getId()));
    }

    @Override
    public void setF(IVField fie, Object o) {
        FieldValue val = new FieldValue();
        FieldItem i = rI.getI(fie.getId());
        val.setValue(i.getFieldType(), o, i.getAfterDot());
        rI.setRowField(row, fie.getId(), val);
    }

    @Override
    public boolean isValid(IVField fie) {
        return rI.isField(fie.getId());
    }

    @Override
    public List<IVField> getF() {
        List<IVField> fList = new ArrayList<IVField>();
        for (int i = 0; i < rI.rowSize(); i++) {
            FieldItem fi = rI.getI(i);
            VField vv = VField.construct(fi);
            fList.add(vv);
        }
        return fList;
    }

}
