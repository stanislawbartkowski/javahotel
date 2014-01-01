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
package com.gwtmodel.table.login;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;

public class LoginField implements IVField {

    @Override
    public FieldDataType getType() {
        return FieldDataType.constructString();
    }

    @Override
    public String getId() {
        return f.toString();
    }

    public enum F {

        LOGINNAME, PASSWORD, REPASSWORD, OTHER
    };

    public F getF() {
        return f;
    }
    private final F f;

    public LoginField(F f) {
        this.f = f;
    }

    @Override
    public boolean eq(IVField o) {
        LoginField l = (LoginField) o;
        return f == l.f;
    }

    public boolean isLogin() {
        return f == F.LOGINNAME;
    }

    public boolean isPassword() {
        return f == F.PASSWORD;
    }

    public boolean isRePassword() {
        return f == F.REPASSWORD;
    }
}
