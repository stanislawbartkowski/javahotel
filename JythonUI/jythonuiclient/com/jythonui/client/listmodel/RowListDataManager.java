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
package com.jythonui.client.listmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class RowListDataManager {

    // private final DataListTypeFactory lFactory;
    private final Map<IDataType, String> listMap = new HashMap<IDataType, String>();
    private final Map<IDataType, ListFormat> lMap = new HashMap<IDataType, ListFormat>();
    private final Map<IDataType, RowIndex> rMap = new HashMap<IDataType, RowIndex>();
    private final String dialogName;

    public RowListDataManager(String dialogName) {
        this.dialogName = dialogName;
    }

    /**
     * @return the dialogName
     */
    String getDialogName() {
        return dialogName;
    }

    public void addList(IDataType di, String lId, ListFormat fo) {
        listMap.put(di, lId);
        lMap.put(di, fo);
        rMap.put(di, new RowIndex(fo.getColumns()));
    }

    public ListFormat getFormat(IDataType da) {
        return lMap.get(da);
    }

    public void publishBeforeForm(ISlotable sl, IDataType d, ListOfRows l) {
        FormBeforeCompletedSignal signal = new FormBeforeCompletedSignal(l);
        CustomStringSlot slot = FormBeforeCompletedSignal.constructSignal(d);
        sl.getSlContainer().publish(slot, signal);
    }

    public List<IDataType> getList() {
        Iterator<IDataType> i = lMap.keySet().iterator();
        List<IDataType> l = new ArrayList<IDataType>();
        while (i.hasNext()) {
            l.add(i.next());
        }
        return l;
    }

    public String getLId(IDataType f) {
        return listMap.get(f);
    }

    RowIndex getR(IDataType d) {
        return rMap.get(d);
    }

    public ISlotable constructListControler(IDataType da, CellId panelId,
            IVariablesContainer iCon, IPerformClickAction iAction) {
        return ListControler.contruct(this, da, panelId, iCon, iAction);
    }

    IVModelData contructE(IDataType da) {
        return new RowVModelData(rMap.get(da));
    }

}
