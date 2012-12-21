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
package com.javahotel.nmvc.factories.persist;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.login.LoginData;
import com.javahotel.client.abstractto.LoginRecord;

/**
 * @author hotel
 * 
 */
public class ConvertP {

    private ConvertP() {
    }

    public static LoginRecord toLoginP(IVModelData ma) {
        LoginData lo = (LoginData) ma;
        LoginRecord pe = new LoginRecord();
        String loginName = lo.getLoginName();
        String password = lo.getPassword();
        pe.setLogin(loginName);
        pe.setPassword(password);
        return pe;
    }

}
