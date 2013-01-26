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
package com.jythonui.client.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowIndex;
import com.jythonui.shared.TypedefDescr;

public class EnumTypesList {

    private final DialogFormat d;

    public EnumTypesList(DialogFormat d) {
        this.d = d;
    }

    private Map<IVField, String> vMap = new HashMap<IVField, String>();
    private Map<String, EnumSynch> syMap = new HashMap<String, EnumSynch>();

    void add(IVField v, String customType) {
        vMap.put(v, customType);
        TypedefDescr t = d.findCustomType(customType);
        if (t == null)
            Utils.errAlert(customType, M.M().CustomTypeNotDefine());
        if (t == null)
            Utils.errAlert(customType, M.M().CustomTypeNotDefine());
        EnumSynch sy = new EnumSynch();
        sy.type = t;
        syMap.put(customType, sy);
    }

    private class EnumSynch extends SynchronizeList {

        IGetDataListCallBack iCallBack;
        ListOfRows lRows;
        TypedefDescr type;

        EnumSynch() {
            super(2);
        }

        @Override
        protected void doTask() {
            List<FieldItem> fList = type.construct();
            RowIndex rI = new RowIndex(fList);
            String comboField = type.getAttr(ICommonConsts.COMBOID);
            IVField v = VField.construct(comboField);
            IVField displayV = null;
            if (type.getDisplayName() != null) {
                displayV = VField.construct(type.getDisplayName());
            }
            IDataListType dList = JUtils.constructList(rI, lRows, v, displayV);
            iCallBack.set(dList);
        }
    }

    public void add(IVField v, IGetDataListCallBack iCallBack) {
        String customType = vMap.get(v);
        EnumSynch sy = syMap.get(customType);
        sy.iCallBack = iCallBack;
        sy.signalDone();
    }

    public void add(String customType, ListOfRows lRows) {
        EnumSynch sy = syMap.get(customType);
        sy.lRows = lRows;
        sy.signalDone();
    }
}
