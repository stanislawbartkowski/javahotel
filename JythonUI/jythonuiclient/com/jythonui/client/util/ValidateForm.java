/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.jythonui.client.M;
import com.jythonui.client.dialog.VField;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ValidateRule;

public class ValidateForm {

    private ValidateForm() {

    }

    private enum Op {
        lt, eq, gt, ge, le
    }

    private static String getAttr(ValidateRule va, String aid) {
        String id = va.getAttr(aid);
        if (CUtil.EmptyS(id)) {
            Utils.errAlert(M.M().ValidateAttributeNotDefined(aid));
            return null;
        }
        return id;
    }

    private static FieldItem findId(ValidateRule va, String aid,
            List<FieldItem> fList) {
        String id = getAttr(va, aid);
        if (id == null) {
            return null;
        }
        FieldItem f = DialogFormat.findE(fList, id);
        if (f == null) {
            Utils.errAlert(M.M().ValidateFieldNotFound(aid, id));
        }
        return f;
    }

    public static List<InvalidateMess> createErrList(IVModelData v,
            List<FieldItem> fList, List<ValidateRule> rulList) {
        List<InvalidateMess> err = VerifyEmpty.checkEmpty(v, fList);
        if (err != null)
            return err;
        if (rulList == null)
            return null;
        err = new ArrayList<InvalidateMess>();

        for (ValidateRule va : rulList) {
            String op = getAttr(va, ICommonConsts.VALIDATEOP);
            if (op == null) {
                return null;
            }
            String displayN = getAttr(va, ICommonConsts.DISPLAYNAME);
            if (displayN == null)
                return null;
            if (CUtil.EmptyS(op)) {
                Utils.errAlert(M.M().ValidateAttributeNotDefined(
                        ICommonConsts.VALIDATEOP));
                return null;
            }
            Op o = Op.valueOf(op);
            FieldItem f1 = findId(va, ICommonConsts.ID, fList);
            FieldItem f2 = findId(va, ICommonConsts.VALIDATEID1, fList);
            if (f1 == null || f2 == null) {
                return null;
            }
            IVField v1 = VField.construct(f1);
            IVField v2 = VField.construct(f2);
            if (FUtils.isNullValue(v, v1)) {
                continue;
            }
            if (FUtils.isNullValue(v, v2)) {
                continue;
            }
            int comp = FUtils.compareValue(v, v2, v, v1, false, false);
            switch (o) {
            case lt:
                if (comp == -1)
                    continue;
                break;
            case le:
                if (comp != 1)
                    continue;
                break;
            case eq:
                if (comp == 0)
                    continue;
                break;
            case ge:
                if (comp != -1)
                    continue;
                break;
            case gt:
                if (comp == 1)
                    continue;
                break;
            } // switch
            err.add(new InvalidateMess(v1, displayN));
        }
        if (err.isEmpty())
            return null;
        return err;
    }

    public static boolean validateV(IDataType dType, ISlotable iSlo,
            DialogFormat d, DataActionEnum errSig) {
        IVModelData v = new VModelData();
        v = iSlo.getSlContainer().getGetterIVModelData(dType,
                GetActionEnum.GetViewModelEdited, v);
        List<InvalidateMess> errMess = createErrList(v, d.getFieldList(),
                d.getValList());
        if (errMess == null)
            return true;
        iSlo.getSlContainer().publish(dType, errSig,
                new InvalidateFormContainer(errMess));
        return false;

    }

}
