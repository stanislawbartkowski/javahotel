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
package com.gwtmodel.table.login;

import com.gwtmodel.table.AVModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import java.util.List;

public class LoginData extends AVModelData {

    public String getRepassword() {
        return repassword;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }

    private String loginName;
    private String password;
    private String repassword;

    @Override
    public Object getF(IVField fie) {
        LoginField f = (LoginField) fie;
        if (f.isPassword()) {
            return password;
        }
        if (f.isLogin()) {
            return loginName;
        }
        if (f.isRePassword()) {
            return repassword;
        }
        return null;
    }

    @Override
    public void setF(IVField fie, Object val) {
        String s = (String) val;
        LoginField f = (LoginField) fie;
        if (f.isLogin()) {
            loginName = s;
            return;
        }
        if (f.isPassword()) {
            password = s;
        }
        if (f.isRePassword()) {
            repassword = s;
        }
    }

    @Override
    public List<IVField> getF() {
        IVField[] e = { new LoginField(LoginField.F.LOGINNAME),
                new LoginField(LoginField.F.PASSWORD),
                new LoginField(LoginField.F.REPASSWORD),
                new LoginField(LoginField.F.OTHER) };
        return Utils.toList(e);
    }

    @Override
    public boolean isValid(IVField fie) {
        return true;
    }
}
