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
package com.javahotel.client.mvc.dict.validator;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.DictErrorMessage;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.validator.IErrorMessageContext;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class ValidUtil {

    private ValidUtil() {
    }

    public static List<InvalidateMess> checkEmpty(RecordModel mo,
            List<IField> eF) {
        AbstractTo a = mo.getA();
        List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
        boolean ok = true;
        List<RecordField> co = mo.getRDef().getFields();
        for (IField f : eF) {
            if (a.emptyS(f)) {
                boolean o = false;
                for (RecordField re : co) {
                    if (re.getFie() == f) {
                        if (re.getELine().getChooseResult() == ILineField.CHOOSECHECKTRUE) {
                            o = true;
                            break;
                        }
                    }
                }
                if (o) {
                    continue;
                }
                ok = false;
                errMess.add(new InvalidateMess(f, true, null));
            }
        }
        if (ok) {
            return null;
        }
        return errMess;
    }

    static void callSig(List<InvalidateMess> col, ISignalValidate sig,
            IErrorMessageContext iCo) {
        if (col == null) {
            sig.success();
        } else {
            sig.failue(new DictErrorMessage(col, iCo));
        }
    }

    static List<InvalidateMess> createErr(IField f, String errmess) {
        List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
        errMess = new ArrayList<InvalidateMess>();
        errMess.add(new InvalidateMess(f, errmess));
        return errMess;
    }

    static List<InvalidateMess> validateEmpty(final DictData d,
            final int action, final RecordModel a) {
        List<IField> eF = HInjector.getI().getDictEmptyFactory().getNoEmpty(
                action, d);
        List<InvalidateMess> errMess = ValidUtil.checkEmpty(a, eF);
        return errMess;
    }

    static class ValidCallBack extends CommonCallBack<ReturnPersist> {

        private final ISignalValidate sig;
        private final IErrorMessageContext iCo;
        private final AbstractTo a;

        ValidCallBack(IResLocator rI, ISignalValidate sig,
                IErrorMessageContext iCo, AbstractTo a) {
            this.sig = sig;
            this.iCo = iCo;
            this.a = a;
        }

        @Override
        public void onMySuccess(ReturnPersist arg) {
            List<InvalidateMess> errMess = null;
            if (arg.getErrorMessage() != null) {
                IField f1 = ValidUtil.getIField(a, arg.getViewName());
                errMess = createErr(f1, arg.getErrorMessage());
            }
            callSig(errMess, sig, iCo);
        }

    }

    static IField getIField(AbstractTo a, String name) {
        IField i = a.getF(name);
        return i;
    }
}
