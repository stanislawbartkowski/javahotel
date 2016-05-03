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
package com.jythonui.client.listmodel;

import java.util.List;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.dialog.ICreateBackActionFactory;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.util.PerformVariableAction.VisitList;
import com.jythonui.client.util.PerformVariableAction.VisitList.IGetFooter;
import com.jythonui.client.variables.ISetGetVar;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;

public interface IRowListDataManager extends ISetGetVar {

    ListFormat getFormat(IDataType da);

    IDataType getLType(String fId);

    void addList(IDataType di, String lId, ListFormat fo);

    ISlotable constructListControler(IDataType da, CellId panelId,
            IVariablesContainer iCon, IPerformClickAction iAction,
            ICreateBackActionFactory bFactory, IPerformClickAction custAction);

    List<IDataType> getList();

    void publishBeforeForm(IDataType d, ListOfRows l);

    void publishBeforeFooter(IDataType d, List<IGetFooter> value);

    void publishBeforeListEdit(IDataType d, VisitList.EditListMode eModel);

    void sendEnum(String customT, IDataListType dList);

    String getLId(IDataType f);

    void enableButton(String buttid, boolean enable);

    void hideButton(String buttid, boolean hide);
}
