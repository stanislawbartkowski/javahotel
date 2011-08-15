/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.login.LoginData;
import com.gwtmodel.table.login.LoginField;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.view.ValidateUtil;
import com.javahotel.client.M;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.VField;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;

/**
 * @author hotel
 * 
 */
class ValidateEmpty {

    static boolean validateE(SlotListContainer slContainer, DataType da,
            PersistTypeEnum persistTypeEnum, IVModelData mData,
            FormLineContainer fContainer, List<IVField> listE) {
        Set<IVField> ignoreV = new HashSet<IVField>();

        if (da.getdType() == DictType.CustomerList) {
            for (FormField f : fContainer.getfList()) {
                IFormLineView i = f.getELine();
                VField v = (VField) f.getFie();
                if (v.getFie() == DictionaryP.F.name) {
                    if (i.getChooseResult() == IFormLineView.CHOOSECHECKTRUE) {
                        for (IVField vv : listE) {
                            if (vv.eq(v)) {
                                ignoreV.add(vv);
                            }
                        }
                    }
                }
            }
        }
        List<InvalidateMess> errMess = ValidateUtil.checkEmpty(mData, listE,
                ignoreV);
        if (errMess == null) {
            if (da.isAllPersons()) {
                LoginData lo = (LoginData) mData;
                String password = lo.getPassword();
                String repassword = (String) lo.getF(new LoginField(
                        LoginField.F.REPASSWORD));
                if (!CUtil.EmptyS(password)) {
                    if (!CUtil.EqNS(password, repassword)) {
                        errMess = new ArrayList<InvalidateMess>();
                        errMess.add(new InvalidateMess(new LoginField(
                                LoginField.F.PASSWORD),
                                M.L().PasswordDifferent()));
                        return P.publishValidSignalE(slContainer, da, errMess);
                    }
                }
            }
            return true;
        }
        return P.publishValidSignalE(slContainer, da, errMess);
    }

}
