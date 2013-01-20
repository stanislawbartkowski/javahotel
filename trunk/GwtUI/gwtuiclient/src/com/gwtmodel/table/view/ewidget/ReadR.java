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

import com.gwtmodel.table.*;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.LogT;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author perseus
 */
abstract class ReadR implements IGetDataListCallBack {

    private final ITableCustomFactories tFactories;

    ReadR(ITableCustomFactories tFactories) {
        this.tFactories = tFactories;
    }

    abstract void setList(List<String> rList);

    @Override
    public void set(IDataListType dataList) {
        List<String> sList = new ArrayList<String>();
        IVField sym = dataList.comboField();
        if (sym == null) {
          sym = tFactories.getGetCustomValues().getSymForCombo();
        }
        assert sym != null : LogT.getT().cannotBeNull();
        for (IVModelData vv : dataList.getList()) {
            String s = FUtils.getValueS(vv, sym);
            sList.add(s);
        }
        setList(sList);
    }
}
