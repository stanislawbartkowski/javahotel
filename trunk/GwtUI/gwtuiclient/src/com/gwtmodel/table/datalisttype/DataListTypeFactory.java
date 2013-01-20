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
package com.gwtmodel.table.datalisttype;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author stanislaw.bartkowski@gmail.com
 */
public class DataListTypeFactory {

    public IDataListType construct(List<IVModelData> dList) {
        return new DataListType(dList, null, null);
    }

    public IDataListType constructLp(List<AbstractLpVModelData> dList) {
        IDataListType d = new DataListTypeLp(new ArrayList<IVModelData>(), null);
        int inde = 0;
        for (AbstractLpVModelData a : dList) {
            d.add(inde, a);
            inde++;
        }
        return d;
    }

    public IDataListType construct(List<IVModelData> dList, IVField comboFie) {
        return new DataListType(dList, comboFie, null);
    }

    public IDataListType construct(List<IVModelData> dList, IVField comboFie,
            IVField treeLevel) {
        return new DataListType(dList, comboFie, treeLevel);
    }
}
