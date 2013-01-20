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
package com.jythonui.client.listmodel;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
class ListUtils {

    private ListUtils() {

    }

    static IDataListType constructList(IDataType da, RowListDataManager rM,
            ListOfRows rL) {
        DataListTypeFactory lFactory = GwtGiniInjector.getI()
                .getDataListTypeFactory();

        List<IVModelData> rList = new ArrayList<IVModelData>();
        RowIndex rI = rM.getR(da);
        if (rL != null)
            for (RowContent t : rL.getRowList()) {
                RowVModelData r = new RowVModelData(rI, t);
                rList.add(r);
            }
        return lFactory.construct(rList);
    }

    static void executeCrudAction(DialogVariables v, ListFormat li,
            String dialogName, String crudAction,
            CommonCallBack<DialogVariables> callBack) {
        v.setValueS(ICommonConsts.JLIST_NAME, li.getId());
        ExecuteAction.action(v, dialogName, crudAction, callBack);
    }

}
