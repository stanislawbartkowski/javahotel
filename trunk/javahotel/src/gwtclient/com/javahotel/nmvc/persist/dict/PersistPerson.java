/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.persist.dict;

import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.login.LoginData;
import com.javahotel.client.GWTGetService;
import com.javahotel.common.toobject.PersonP;

public class PersistPerson extends APersonHotelPersist {

    @Override
    public void persist(PersistTypeEnum persistTypeEnum, IVModelData mData,
             ISuccess iRes) {
        LoginData lo = (LoginData) mData;
        PersonP pe = new PersonP();
        String loginName = lo.getLoginName();
        String password = lo.getPassword();
        pe.setName(loginName);
        switch (persistTypeEnum) {
        case ADD:
        case MODIF:
              if (CUtil.EmptyS(password)) {
                  iRes.success();
                  break;
              }
              GWTGetService.getService().addPerson(pe, password,
                      new CallBack(iRes));
              break;
        case REMOVE :
            GWTGetService.getService().removePerson(pe,new CallBack(iRes));
            break;     
        }
        
    }
}
