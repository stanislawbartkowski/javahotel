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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.view.util.OkDialog;
import com.jythonui.client.M;
import com.jythonui.client.dialog.RunAction;
import com.jythonui.client.listmodel.RowListDataManager;
import com.jythonui.client.util.PerformVariableAction.VisitList.IGetFooter;
import com.jythonui.client.variables.IVariablesContainer;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
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

        public interface IGetFooter {
            String getFie();

            FieldValue getV();
        }

        void accept(IDataType da, ListOfRows lRows);

        void acceptTypes(String typeName, ListOfRows lRows);

        void acceptFooter(IDataType da, List<IGetFooter> fList);
    }

    public static void perform(IYesNoAction iYesno, ISendCloseAction iClose,
            final DialogVariables arg, IVariablesContainer iCon,
            RowListDataManager liManager, final VisitList vis, WSize w) {
        iCon.setVariablesToForm(arg);
        // lists
        for (final IDataType da : liManager.getList()) {
            final String s = liManager.getLId(da);
            ListOfRows lRows = arg.getList(s);
            vis.accept(da, lRows);
            final List<IGetFooter> fList = new ArrayList<IGetFooter>();
            JUtils.IVisitor visDL = new JUtils.IVisitor() {

                @Override
                public void action(final String fie, String field) {
                    FieldValue val = arg.getValue(field);
                    if (val.getType() != TT.BOOLEAN) {
                        String mess = M.M().FooterSetValueShouldBeBoolean(s,
                                field);
                        Utils.errAlert(mess);
                        return;
                    }
                    if (!val.getValueB())
                        return;
                    String vName = ICommonConsts.JFOOTER + s + "_" + fie;
                    final FieldValue v = arg.getValue(vName);
                    if (v == null) {
                        String mess = M.M().FooterSetDefinedButValueBot(field,
                                vName);
                        Utils.errAlert(mess);
                        return;
                    }
                    VisitList.IGetFooter gFooter = new VisitList.IGetFooter() {

                        @Override
                        public String getFie() {
                            return fie;
                        }

                        @Override
                        public FieldValue getV() {
                            return v;
                        }

                    };
                    fList.add(gFooter);
                }
            };
            String jKey = ICommonConsts.JFOOTERCOPY + s + "_";
            JUtils.visitListOfFields(arg, jKey, visDL);
            if (!fList.isEmpty())
                vis.acceptFooter(da, fList);
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
    
    private static boolean checkW(String action, String param,
            String param1, final String param2, WSize w) {
        if (w != null) return true;
        String mess = action + " " + param;
        if (param1 != null) mess += " " + param1;
        if (param2 != null) mess += " " + param2;
        String alert = M.M().CannotCallActionHere(mess);
        Utils.errAlert(alert);
        return false;
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
            if (!checkW(action,param,param1,param2,w)) return;
            new RunAction().upDialog(param, w, iCon);
            return;
        }
        if (action.equals(ICommonConsts.JOKMESSAGE)
                || action.equals(ICommonConsts.JERRORMESSAGE)) {
            if (!checkW(action,param,param1,param2,w)) return;
            OkDialog ok = new OkDialog(param, param1);
            ok.show(w);
            return;
        }
        if (action.equals(ICommonConsts.JYESNOMESSAGE)) {
            if (iYesno != null) {
                if (!checkW(action,param,param1,param2,w)) return;
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
