/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.ReadDictList;
import com.gwtmodel.table.ReadDictList.IListCallBack;
import com.gwtmodel.table.TOptional;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class AddBoxValues {

    private static void setValue(IDataListType dList, IValueLB e) {
        String firstS = null;
        List<String> li = new ArrayList<String>();
        for (int i = 0; i < FUtils.getRowNumber(dList); i++) {
            IVModelData vData = FUtils.getRow(dList, i);
            String s = FUtils.getValueS(vData, dList.comboField());
            li.add(s);
            if (firstS == null) {
                firstS = s;
            }
        }
        List<String> displayLi = null;
        IVField displayV = dList.displayComboField();
        if (displayV != null) {
            if (e.addEmpty()) {
                li.add(0, "");
            }
            displayLi = new ArrayList<String>();
            for (int i = 0; i < FUtils.getRowNumber(dList); i++) {
                IVModelData vData = FUtils.getRow(dList, i);
                String s = FUtils.getValueS(vData, displayV);
                displayLi.add(s);
            }
        }

        TOptional<String> be = e.getBeforeVal();
        Object o = e.getValObj();
        String av = FUtils.getValueOS(o, e.getV());
        if (displayLi != null) {
            e.setList(displayLi);
            e.setIdList(li);
        } else
            e.setList(li);
        if (be != null) {
            e.setValObj(be.orNull());
        } else {
            if ((av == null) && (firstS != null)) {
                e.setValObj(firstS);
            }
        }

    }

    private static class R implements IListCallBack<IDataListType> {

        private final IValueLB e;

        R(final IValueLB e) {
            this.e = e;
        }

        @Override
        public void setList(IDataListType dList) {
            setValue(dList, e);
        }
    }

    static void addValues(IDataType dType, final IValueLB e) {
        new ReadDictList<IDataListType>().readList(dType, new R(e));
    }

    private static class RR implements IGetDataListCallBack {

        private final IValueLB e;

        RR(final IValueLB e) {
            this.e = e;
        }

        @Override
        public void set(IDataListType dataList) {
            setValue(dataList, e);
        }
    }

    static void addValues(IVField v, IGetDataList iGet, final IValueLB e) {
        iGet.call(v, new RR(e));
    }
}
