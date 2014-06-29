/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import com.gwtmodel.table.view.util.OkDialog;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.dialog.run.RunAction;
import com.jythonui.client.listmodel.IRowListDataManager;
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

        public class EditListMode {
            private ChangeEditableRowsParam.ModifMode mode = ChangeEditableRowsParam.ModifMode.ADDCHANGEDELETEMODE;
            private List<IVField> eList = new ArrayList<IVField>();

            public ChangeEditableRowsParam.ModifMode getMode() {
                return mode;
            }

            public List<IVField> geteList() {
                return eList;
            }

        }

        void accept(IDataType da, ListOfRows lRows);

        void acceptTypes(String typeName, ListOfRows lRows);

        void acceptFooter(IDataType da, List<IGetFooter> fList);

        void acceptEditListMode(IDataType da, EditListMode e);
    }

    public static void perform(IYesNoAction iYesno, ISendCloseAction iClose,
            final DialogVariables arg, IVariablesContainer iCon,
            IRowListDataManager liManager, final VisitList vis, WSize w,
            IExecuteAfterModalDialog iEx) {
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

            // check edit list
            final VisitList.EditListMode listMode = new VisitList.EditListMode();
            JUtils.IVisitor visEdit = new JUtils.IVisitor() {

                @Override
                public void action(String fie, String field) {
                    if (fie.equals(ICommonConsts.JLISTEDITMODE)) {
                        String mode = arg.getValue(field).getValueS();
                        try {
                            listMode.mode = ChangeEditableRowsParam.ModifMode
                                    .valueOf(mode);
                        } catch (java.lang.IllegalArgumentException e) {
                            Utils.errAlert(mode, e);
                        }

                    }
                    IVField e = VField.construct(fie);
                    listMode.eList.add(e);
                }

            };
            String eKey = ICommonConsts.JLISTEDIT + s + "_";
            JUtils.visitListOfFields(arg, eKey, visEdit);
            if (!listMode.eList.isEmpty())
                vis.acceptEditListMode(da, listMode);

        } // forICreateBackActionFactory

        for (Entry<String, ListOfRows> e : arg.getEnumList().entrySet()) {
            vis.acceptTypes(e.getKey(), e.getValue());
        }

        // cookie
        JUtils.IVisitor cookSet = new JUtils.IVisitor() {

            @Override
            public void action(String fie, String field) {
                FieldValue val = arg.getValue(field);
                if (val.getType() != TT.BOOLEAN) {
                    String mess = M.M().SetCookieValueShoulbBool(field);
                    Utils.errAlertB(mess);
                    return;
                }
                String valName = ICommonConsts.JCOOKIE + fie;
                String s = arg.getValueS(valName);
                if (CUtil.EmptyS(s)) {
                    Utils.RemoveCookie(fie);
                    return;
                }
                Utils.SetCookie(fie, s);
            }
        };
        JUtils.visitListOfFields(arg, ICommonConsts.JCOOKIESET, cookSet);

        // it a little tricky but this way allows code reuse with performAction
        String[] kom = { ICommonConsts.JMAINDIALOG, ICommonConsts.JUPDIALOG,
                ICommonConsts.JOKMESSAGE, ICommonConsts.JERRORMESSAGE,
                ICommonConsts.JYESNOMESSAGE, ICommonConsts.JSUBMIT };
        String[] param = { null, ICommonConsts.JBUTTONDIALOGSTART,
                ICommonConsts.JMESSAGE_TITLE, ICommonConsts.JMESSAGE_TITLE,
                ICommonConsts.JMESSAGE_TITLE, null };
        String[] param2 = { null, ICommonConsts.JAFTERDIALOGACTION, null, null,
                ICommonConsts.JAFTERDIALOGACTION, null };
        for (int i = 0; i < kom.length; i++) {
            FieldValue val = arg.getValue(kom[i]);
            if (val == null)
                continue;
            String p = null;
            if (val.getType() == TT.STRING)
                p = val.getValueS();
            String par = null;
            String par2 = null;
            if (param[i] != null) {
                par = arg.getValueS(param[i]);
            }
            if (param2[i] != null) {
                par2 = arg.getValueS(param2[i]);
            }
            performAction(iYesno, iClose, kom[i], p, par, par2, w, iCon, iEx);
        }
        if (arg.getValue(ICommonConsts.JLOGOUTACTION) != null) {
            performAction(null, iClose, ICommonConsts.JLOGOUTACTION, null,
                    null, null, w, iCon, iEx);
            return;
        }
        if (arg.getValue(ICommonConsts.JCLOSEDIALOG) != null) {
            String resString = null;
            String closeButton = null;
            FieldValue val = arg.getValue(ICommonConsts.JCLOSEDIALOG);
            if (val.getType() == TT.STRING)
                resString = val.getValueS();
            val = arg.getValue(ICommonConsts.JCLOSEBUTTON);
            if (val != null)
                closeButton = val.getValueS();

            performAction(null, iClose, ICommonConsts.JCLOSEDIALOG, resString,
                    closeButton, null, w, iCon, iEx);
        }
    }

    private static boolean checkW(String action, String param, String param1,
            final String param2, WSize w) {
        if (w != null)
            return true;
        String mess = action + " " + param;
        if (param1 != null)
            mess += " " + param1;
        if (param2 != null)
            mess += " " + param2;
        String alert = M.M().CannotCallActionHere(mess);
        Utils.errAlert(alert);
        return false;
    }

    public static void performAction(final IYesNoAction iYesno,
            ISendCloseAction iClose, String action, String param,
            String param1, final String param2, WSize w,
            IVariablesContainer iCon, IExecuteAfterModalDialog iEx) {
        if (action.equals(ICommonConsts.JLOGOUTACTION)) {
            IWebPanel i = GwtGiniInjector.getI().getWebPanel();
            i.logOut();
            return;
        }
        if (action.equals(ICommonConsts.JMAINDIALOG)) {
            if (M.getMainD() != null) M.getMainD().close();
            M.setMainD(null);
            new RunAction().start(param);
            return;
        }
        if (action.equals(ICommonConsts.JUPDIALOG)) {
            if (!checkW(action, param, param1, param2, w))
                return;
            if (iEx != null)
                iEx.setAction(param2);
            new RunAction().upDialog(param, w, iCon, iEx, param1);
            return;
        }
        if (action.equals(ICommonConsts.JOKMESSAGE)
                || action.equals(ICommonConsts.JERRORMESSAGE)) {
            if (!checkW(action, param, param1, param2, w))
                return;
            OkDialog ok = new OkDialog(param, param1);
            ok.show(w);
            return;
        }
        if (action.equals(ICommonConsts.JYESNOMESSAGE)) {
            if (iYesno != null) {
                if (!checkW(action, param, param1, param2, w))
                    return;
                iYesno.answer(param, param1, param2, w);
            }
            return;
        }
        // JCloseDialog: param : resString, param1: resButton
        if (action.equals(ICommonConsts.JCLOSEDIALOG)) {
            if (iClose != null)
                iClose.closeAction(param, param1);
            return;
        }
        if (action.equals(ICommonConsts.JCLOSEDIALOG)) {
            if (iClose != null)
                iClose.closeAction(param, param1);
            return;
        }
        if (action.equals(ICommonConsts.JSUBMIT)) {
            if (iClose != null)
                iClose.submitAction();
            return;
        }
        Utils.errAlert(M.M().UnknownAction(action, param));
    }

}
