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
package com.jythonui.client.variables;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.dialog.FormGridManager;
import com.jythonui.client.dialog.datepanel.DateLineManager;
import com.jythonui.client.listmodel.RowListDataManager;
import com.jythonui.shared.DialogVariables;

/**
 * @author hotel
 * 
 */
public interface IVariablesContainer {

    void addFormVariables(ISlotable iSlo, IDataType dType,
            RowListDataManager liManager, FormGridManager gManager,
            DateLineManager dlManager, DialogVariables addV,IBackAction iAction);

    DialogVariables getVariables();

    void setVariablesToForm(DialogVariables v);

    void copyCurrentVariablesToForm(ISlotable iSlo, IDataType dType);
    
    List<IBackAction> getList();

}
