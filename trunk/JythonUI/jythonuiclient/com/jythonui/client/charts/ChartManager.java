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
package com.jythonui.client.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.dialog.IDialogContainer;

class ChartManager implements IChartManager {

    private final IDialogContainer iDialog;
    private final IDataType publishType;

    private final Map<IDataType, String> dMap = new HashMap<IDataType, String>();

    ChartManager(IDialogContainer iDialog, IDataType publishType) {
        this.iDialog = iDialog;
        this.publishType = publishType;
    }

    @Override
    public ISlotable constructSlotable(String id, IDataType dType) {
        dMap.put(dType, id);
        return new CharContainer(iDialog, id, publishType, dType);
    }

    @Override
    public List<IDataType> getList() {
        List<IDataType> l = new ArrayList<IDataType>();
        for (IDataType d : dMap.keySet())
            l.add(d);
        return l;
    }

    @Override
    public String getId(IDataType da) {
        return dMap.get(da);
    }

}
