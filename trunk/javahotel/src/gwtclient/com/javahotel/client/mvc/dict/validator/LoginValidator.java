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
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.DictData.SpecE;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.LoginRecord;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dict.validator.errmess.DictErrorMessage;
import com.javahotel.client.mvc.dict.validator.errmess.InvalidateMess;
import com.javahotel.client.mvc.recordviewdef.DictEmptyFactory;
import com.javahotel.client.mvc.validator.IErrorMessageContext;
import com.javahotel.client.mvc.validator.IRecordValidator;
import com.javahotel.client.mvc.validator.ISignalValidate;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class LoginValidator implements IRecordValidator {

    private final IResLocator rI;
    private final boolean user;
    private final DictData da;
    private IErrorMessageContext iCo;
    private final DictEmptyFactory eFactory;

    LoginValidator(IResLocator rI, DictData da) {
        this.rI = rI;
        this.da = da;
        this.iCo = null;
        if (da.getSE() == SpecE.LoginUser) {
            user = true;
        } else {
            user = false;
        }
        eFactory = HInjector.getI().getDictEmptyFactory();
    }

    public boolean isEmpty(RecordModel a) {
        return false;
    }

    private class FailureBack implements CommonCallBack.onFailureExt {

        private final ISignalValidate sig;

        FailureBack(final ISignalValidate sig) {
            this.sig = sig;
        }

        public boolean doSth(final Throwable ext) {
            List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
            errMess.add(new InvalidateMess(LoginRecord.F.password, false,
                    "User lub Hasło niepoprawne"));
            sig.failue(new DictErrorMessage(errMess, iCo));
            return true;
        }
    }

    private class BackLogin extends CommonCallBack<Object> {

        private final ISignalValidate sig;
        private final LoginRecord re;

        private void setUserHotel(String hotel) {
            rI.getR().setHotel(hotel);
            String user = re.getLogin();
            rI.getR().setUserName(user);
        }

        private class HotelUserLogin implements RData.IVectorList {

            public void doVList(final List<? extends AbstractTo> val) {
                if (val.size() == 0) {
                    List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
                    errMess.add(new InvalidateMess(LoginRecord.F.hotel, false,
                            "Nie masz uprawnień w tym hotelu"));
                    sig.failue(new DictErrorMessage(errMess, iCo));
                    return;
                }
                rI.getUR().setColl(val);
                String ho = re.getHotel();
                setUserHotel(ho);
                sig.success();
            }
        }

        BackLogin(final ISignalValidate sig, final LoginRecord re) {
            super(new FailureBack(sig));
            this.sig = sig;
            this.re = re;
        }

        @Override
        public void onMySuccess(final Object arg) {
            if (!user) {
                setUserHotel("");
                sig.success();
                return;
            }
            CommandParam p = new CommandParam();
            p.setHotel(re.getHotel());
            p.setPerson(re.getLogin());
            rI.getR().getList(RType.PersonHotelRoles, p, new HotelUserLogin());
        }
    }

    public void validateS(int action, RecordModel a, ISignalValidate sig) {
        List<IField> eF = eFactory.getNoEmpty(da);
        List<InvalidateMess> errMess = ValidUtil.checkEmpty(a, eF);
        if (errMess != null) {
            sig.failue(new DictErrorMessage(errMess, iCo));
            return;
        }
        LoginRecord re = (LoginRecord) a.getA();
        if (user) {
            GWTGetService.getService().loginUser(re.getLogin(),
                    re.getPassword(), new BackLogin(sig, re));
        } else {
            GWTGetService.getService().loginAdmin(re.getLogin(),
                    re.getPassword(), new BackLogin(sig, re));
        }
    }

    public void setErrContext(IErrorMessageContext co) {
        this.iCo = co;
    }
}
