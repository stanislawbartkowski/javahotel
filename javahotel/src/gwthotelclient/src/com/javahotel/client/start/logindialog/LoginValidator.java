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
package com.javahotel.client.start.logindialog;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IBackValidate;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.PersonHotelRoles;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.StringP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class LoginValidator {

    private static class FailureBack implements CommonCallBack.onFailureExt {

        private final IBackValidate cBack;

        FailureBack(IBackValidate cBack) {
            this.cBack = cBack;
        }

        @Override
        public boolean doSth(final Throwable ext) {
            IResLocator rI = HInjector.getI().getI();
            List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
            errMess.add(new InvalidateMess(
                    new LoginField(LoginField.F.PASSWORD), false, rI
                            .getLabels().UserOrPasswordNotValid()));
            cBack.invalid(new InvalidateFormContainer(errMess));
            return true;
        }
    }

    private static class BackLogin extends CommonCallBack<Object> {

        private final CustomLoginData lData;
        private final boolean user;
        private final IBackValidate cBack;
        private final IResLocator rI;

        private void setUserHotel(String hotel, List<StringP> val) {
            String u = lData.getLoginName();
            PersonHotelRoles pRoles = new PersonHotelRoles(u, hotel);
            rI.getR().setpRoles(pRoles);
            IWebPanel w = GwtGiniInjector.getI().getWebPanel();
            w.setUserData(u, hotel);
            if (val != null) {
                for (StringP p : val) {
                    pRoles.getRoles().add(p.getName());
                }
            }
        }

        private class HotelUserLogin implements RData.IVectorList<StringP> {

            @Override
            public void doVList(List<StringP> val) {
                if (val.isEmpty()) {
                    List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
                    errMess.add(new InvalidateMess(new LoginField(
                            LoginField.F.OTHER), false,
                            M.L().YouHaveNoRights()));
                    cBack.invalid(new InvalidateFormContainer(errMess));
                    return;
                }
                String ho = lData.getHotel();
                setUserHotel(ho, val);
                cBack.valid();
            }
        }

        BackLogin(IResLocator rI, CustomLoginData lData, boolean user,
                IBackValidate cBack) {
            super(new FailureBack(cBack));
            this.lData = lData;
            this.user = user;
            this.cBack = cBack;
            this.rI = rI;
        }

        @Override
        public void onMySuccess(final Object arg) {
            if (!user) {
                setUserHotel("", null);
                cBack.valid();
                return;
            }
            CommandParam p = new CommandParam();
            p.setHotel(lData.getHotel());
            p.setPerson(lData.getLoginName());
            rI.getR().getList(RType.PersonHotelRoles, p, new HotelUserLogin());
        }
    }

    static void validateS(CustomLoginData lData, boolean user,
            IBackValidate cBack) {
        IResLocator rI = HInjector.getI().getI();
        if (user) {
            GWTGetService.getService().loginUser(lData.getLoginName(),
                    lData.getPassword(), new BackLogin(rI, lData, user, cBack));
        } else {
            GWTGetService.getService().loginAdmin(lData.getLoginName(),
                    lData.getPassword(), new BackLogin(rI, lData, user, cBack));
        }
    }
}
