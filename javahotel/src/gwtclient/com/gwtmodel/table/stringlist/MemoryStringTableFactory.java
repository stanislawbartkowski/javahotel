/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stringlist;

import com.gwtmodel.table.AbstractLpVModelData;
import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import java.util.ArrayList;
import java.util.List;

public class MemoryStringTableFactory {

    public IDataListType construct(List<AbstractStringE> mList) {
        List<AbstractLpVModelData> a = new ArrayList<AbstractLpVModelData>();
        a.addAll(mList);
        return DataListTypeFactory.constructLp(a);
    }

    public IMemoryStringList construct(String fieldName, String title,
            IStringEFactory eFactory, ISlotSignaller setGwt) {
        return new MemoryStringList(fieldName, title, eFactory, setGwt);
    }

}
