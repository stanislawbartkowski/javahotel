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
package com.jythonui.client.util;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.client.dialog.RunAction;
import com.jythonui.client.listmodel.RowListDataManager;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;

/**
 * @author hotel
 * 
 */
public class PerformVariableAction {

    private PerformVariableAction() {

    }

    public interface VisitList {
        void accept(IDataType da, ListOfRows lRows);
    }

    public static void perform(DialogVariables arg, IVariablesContainer iCon,
            RowListDataManager liManager, VisitList vis) {
        iCon.setVariablesToForm(arg);
        // lists
        for (IDataType da : liManager.getList()) {
            String s = liManager.getLId(da);
            ListOfRows lRows = arg.getList(s);
            vis.accept(da, lRows);
        }
        String mainDialog = arg.getValueS(ICommonConsts.JMAINDIALOG);
        if (!CUtil.EmptyS(mainDialog)) {
            new RunAction().start(mainDialog);
        }
    }

}
