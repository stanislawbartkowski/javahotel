/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist.dict;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.abstractto.LoginRecord;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.PersonP;

class PersistPerson extends APersonHotelPersist {

    private final boolean validate;

    PersistPerson(boolean validate) {
        this.validate = validate;
    }

    @Override
    public void persist(PersistTypeEnum persistTypeEnum, HModelData h,
            IPersistResult iRes) {
        LoginRecord lo = (LoginRecord) h.getA();
        PersonP pe = new PersonP();
        String loginName = lo.getLogin();
        String password = lo.getPassword();
        pe.setName(loginName);
        CallBack ca = new CallBack(iRes, persistTypeEnum, pe);
        if (validate) { 
            GWTGetService.getService().validatePersistPerson(DataUtil.persistTo(persistTypeEnum), pe, ca);
            return;
        }
        
        switch (persistTypeEnum) {
        case ADD:
        case MODIF:
            if (CUtil.EmptyS(password)) {
                ca.onSuccess(new ReturnPersist());
                break;
            }
            GWTGetService.getService().addPerson(pe, password, ca);
            break;
        case REMOVE:
            GWTGetService.getService().removePerson(pe, ca);
            break;
        }

    }
}
