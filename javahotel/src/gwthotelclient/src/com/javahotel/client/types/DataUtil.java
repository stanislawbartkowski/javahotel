/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.types;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.DataListTypeFactory;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;

public class DataUtil {

    private DataUtil() {
    }

    public static ISlotCustom constructValidateAgain(IDataType dType) {
        return new CustomStringDataTypeSlot("HOTEL-VALIDATE-AGAIN", dType);
    }

    public static IDataListType construct(List<? extends AbstractTo> dList) {
        List<IVModelData> dvList = new ArrayList<IVModelData>();
        for (AbstractTo a : dList) {
            dvList.add(VModelDataFactory.construct(a));
        }
        return DataListTypeFactory.construct(dvList);
    }

    public static PersistType persistTo(PersistTypeEnum persisEnumType) {
        switch (persisEnumType) {
        case ADD:
            return PersistType.ADD;
        case MODIF:
            return PersistType.CHANGE;
        case REMOVE:
            return PersistType.REMOVE;
        }
        return null;
    }

    public static List<String> fromDicttoString(
            List<? extends DictionaryP> dList) {
        ArrayList<String> sList = new ArrayList<String>();
        if (dList == null) {
            return sList;
        }
        for (DictionaryP d : dList) {
            String s = d.getName();
            sList.add(s);
        }
        return sList;
    }

    public static <T extends DictionaryP> void fromStringToDict(
            List<String> sList, List<T> dList, List<T> persistList) {
        for (String s : sList) {
            for (T d : dList) {
                if (s.equals(d.getName())) {
                    persistList.add(d);
                }
            }
        }
    }

    public static <T extends AbstractTo> List<T> construct(
            IDataListType dataListType) {
        List<T> dList = new ArrayList<T>();

        for (IVModelData v : dataListType.getList()) {
            HModelData vData = (HModelData) v;
            T t = (T) vData.getA();
            dList.add(t);
        }
        return dList;
    }

    public static <T extends AbstractTo> T getData(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        HModelData vData = (HModelData) mData;
        T cust = (T) vData.getA();
        return cust;
    }

    public static void addToSet(Set<IVField> set, List<FormField> fList,
            IField fi) {
        VField vi = new VField(fi);
        for (FormField f : fList) {
            IVField v = f.getFie();
            if (v.eq(vi)) {
                set.add(v);
            }
        }
    }

    public static Object getO(ISlotable iSlo, IDataType dType, IField f) {
        IFormLineView i = iSlo.getSlContainer().getGetterFormLine(dType,
                new VField(f));
        return i.getValObj();
    }

    public static void setBigDecimal(ISlotable iSlo, IDataType dType,
            IField fie, BigDecimal b) {
        IFormLineView i = iSlo.getSlContainer().getGetterFormLine(dType,
                new VField(fie));
        i.setValObj(b);
    }

}
