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

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.jythonui.client.util.JUtils;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.MapDialogVariable;
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
        RowIndex rI = rM.getR(da);
        return JUtils.constructList(rI, rL, null, null);
    }

    static void addListName(MapDialogVariable v, ListFormat li) {
        v.setValueS(ICommonConsts.JLIST_NAME, li.getId());
    }

}
