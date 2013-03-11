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

import java.util.Map.Entry;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.view.util.OkDialog;
import com.jythonui.client.M;
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

        void acceptTypes(String typeName, ListOfRows lRows);
    }

    public static void perform(IYesNoAction iYesno, ISendCloseAction iClose,
            DialogVariables arg, IVariablesContainer iCon,
            RowListDataManager liManager, VisitList vis, WSize w) {
        iCon.setVariablesToForm(arg);
        // lists
        for (IDataType da : liManager.getList()) {
            String s = liManager.getLId(da);
            ListOfRows lRows = arg.getList(s);
            vis.accept(da, lRows);
        }
        for (Entry<String, ListOfRows> e : arg.getEnumList().entrySet()) {
            vis.acceptTypes(e.getKey(), e.getValue());
        }

        // it a little tricky but this way allows code reuse with performAction
        String[] kom = { ICommonConsts.JMAINDIALOG, ICommonConsts.JUPDIALOG,
                ICommonConsts.JOKMESSAGE, ICommonConsts.JERRORMESSAGE,
                ICommonConsts.JYESNOMESSAGE };
        String[] param = { null, null, ICommonConsts.JMESSAGE_TITLE,
                ICommonConsts.JMESSAGE_TITLE, ICommonConsts.JMESSAGE_TITLE };
        String[] param2 = { null, null, null, null,
                ICommonConsts.JAFTERDIALOGACTION };
        for (int i = 0; i < kom.length; i++) {
            String p = arg.getValueS(kom[i]);
            String par = null;
            String par2 = null;
            if (param[i] != null) {
                par = arg.getValueS(param[i]);
            }
            if (param2[i] != null) {
                par2 = arg.getValueS(param[i]);
            }
            if (!CUtil.EmptyS(p))
                performAction(iYesno, iClose, kom[i], p, par, par2, w, iCon);
        }
        if (arg.getValue(ICommonConsts.JCLOSEDIALOG) != null) {
            performAction(null, iClose, ICommonConsts.JCLOSEDIALOG, null, null,
                    null, w, iCon);
        }
    }

    public static void performAction(final IYesNoAction iYesno,
            ISendCloseAction iClose, String action, String param,
            String param1, final String param2, WSize w,
            IVariablesContainer iCon) {
        if (action.equals(ICommonConsts.JMAINDIALOG)) {
            new RunAction().start(param);
            return;
        }
        if (action.equals(ICommonConsts.JUPDIALOG)) {
            new RunAction().upDialog(param, w, iCon);
            return;
        }
        if (action.equals(ICommonConsts.JOKMESSAGE)
                || action.equals(ICommonConsts.JERRORMESSAGE)) {
            OkDialog ok = new OkDialog(param, param1);
            ok.show(w);
            return;
        }
        if (action.equals(ICommonConsts.JYESNOMESSAGE)) {
            if (iYesno != null) {
                iYesno.answer(param, param1, param2, w);
            }
            return;
        }
        if (action.equals(ICommonConsts.JCLOSEDIALOG)) {
            if (iClose != null)
                iClose.closeAction();
            return;
        }
        Utils.errAlert(M.M().UnknownAction(action, param));
    }

}
