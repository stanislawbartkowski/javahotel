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

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.view.table.VListHeaderDesc;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.DictErrorMessage;
import com.javahotel.client.mvc.recordviewdef.ColListFactory;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.IField;

public class DataUtil {

    private DataUtil() {
    }

    public static int vTypetoAction(PersistTypeEnum persisEnumType) {
        int action = -1;
        switch (persisEnumType) {
        case ADD:
            action = IPersistAction.ADDACION;
            break;
        case MODIF:
            action = IPersistAction.MODIFACTION;
            break;
        case REMOVE:
            action = IPersistAction.DELACTION;
            break;
        }
        return action;
    }

    public static List<VListHeaderDesc> constructList(DataType dType) {
        DictData dicData = new DictData(dType.getdType());
        ColListFactory cFactory = HInjector.getI().getColListFactory();
        List<ColTitle> coList = cFactory.getColList(dicData);
        List<VListHeaderDesc> heList = new ArrayList<VListHeaderDesc>();
        for (ColTitle co : coList) {
            VListHeaderDesc he = new VListHeaderDesc(co.getCTitle(),
                    new VField(co.getF()));
            heList.add(he);
        }
        return heList;
    }

    public static List<IVField> constructEmptyList(IDataType dType, int action) {
        DataType dd = (DataType) dType;
        DictData dicData = new DictData(dd.getdType());
        List<IVField> list = new ArrayList<IVField>();
        DictEmptyFactory eFactory = HInjector.getI().getDictEmptyFactory();
        List<IField> eList = eFactory.getNoEmpty(action, dicData);
        for (IField f : eList) {
            list.add(new VField(f));
        }
        return list;
    }

    public static DataListType construct(List<? extends AbstractTo> dList) {
        List<IVModelData> dvList = new ArrayList<IVModelData>();
        for (AbstractTo a : dList) {
            dvList.add(new VModelData(a));
        }
        return new DataListType(dvList);
    }

    public static <T extends AbstractTo> List<T> construct(
            DataListType dataListType) {
        List<T> dList = new ArrayList<T>();
        for (IVModelData v : dataListType.getdList()) {
            VModelData vData = (VModelData) v;
            T t = (T) vData.getA();
            dList.add(t);
        }
        return dList;
    }

    public static InvalidateFormContainer convert(IErrorMessage errmess) {
        List<InvalidateMess> eList = new ArrayList<InvalidateMess>();
        DictErrorMessage dictM = (DictErrorMessage) errmess;
        List<com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess> col = dictM
                .getErrmess();
        for (com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess mess : col) {
            InvalidateMess mm = new InvalidateMess(new VField(mess.getFie()),
                    mess.isEmpty(), mess.getErrmess());
            eList.add(mm);
        }
        return new InvalidateFormContainer(eList);
    }

    public static RecordModel toRecordModel(IVModelData mData) {
        VModelData va = (VModelData) mData;
        RecordModel mo = new RecordModel(null, null);
        mo.setA(va.getA());
        return mo;
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

}
