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
package com.gwtmodel.table.stringlist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.datalisttype.AbstractLpVModelData;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.ISlotListener;

public class MemoryStringTableFactory {

    private final IVField strField;
    private final IDataType strType;
    private final DataListTypeFactory tFactory;

    public MemoryStringTableFactory() {
        this.strField = Empty.getFieldType();
        this.strType = Empty.getDataType();
        tFactory = GwtGiniInjector.getI().getDataListTypeFactory();

    }

    public IDataListType construct(List<AbstractStringE> mList) {
        List<AbstractLpVModelData> a = new ArrayList<AbstractLpVModelData>();
        a.addAll(mList);
        return tFactory.constructLp(a);
    }

    public IMemoryStringList construct(String fieldName, String title,
            IStringEFactory eFactory, ISlotListener setGwt) {
        return new MemoryStringList(fieldName, title, strType,
                strField, eFactory, setGwt);
    }

    /**
     * @return the strField
     */
    public IVField getStrField() {
        return strField;
    }

    public IDataType getStrType() {
        return strType;

    }
}
