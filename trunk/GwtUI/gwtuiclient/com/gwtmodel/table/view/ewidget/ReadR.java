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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.LogT;

/**
 * 
 * @author perseus
 */
abstract class ReadR implements IGetDataListCallBack {

    private final IGetCustomValues cValues;

    ReadR(IGetCustomValues cValues) {
        this.cValues = cValues;
    }

    abstract void setList(List<String> rList);

    @Override
    public void set(IDataListType dataList) {
        List<String> sList = new ArrayList<String>();
        IVField sym = dataList.comboField();
        if (sym == null) {
            sym = cValues.getSymForCombo();
        }
        assert sym != null : LogT.getT().cannotBeNull();
        for (IVModelData vv : dataList.getList()) {
            String s = FUtils.getValueS(vv, sym);
            sList.add(s);
        }
        setList(sList);
    }
}
