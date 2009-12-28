/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.common;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.common.toobject.IField;

public class EmptyListFactory {

    private EmptyListFactory() {
    }

    public static List<IVField> constructEmptyList(IDataType dType) {
        DataType dd = (DataType) dType;
        DictData dicData = new DictData(dd.getdType());
        List<IVField> list = new ArrayList<IVField>();
        DictEmptyFactory eFactory = HInjector.getI().getDictEmptyFactory();
        List<IField> eList = eFactory.getNoEmpty(dicData);
        for (IField f : eList) {
            list.add(new VField(f));
        }
        return list;
    }

}
