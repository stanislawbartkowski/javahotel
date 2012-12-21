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
package com.javahotel.nmvc.factories.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateMess;
import com.javahotel.client.M;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.StringU;

/**
 * @author hotel
 * 
 */
class BookingValidate {

    static List<InvalidateMess> check(IVModelData pData) {
        BookingP p = DataUtil.getData(pData);
        if (!StringU.isEmpty(p.getBooklist())) {
            return null;
        }
        List<InvalidateMess> errMess = new ArrayList<InvalidateMess>();
        InvalidateMess e = new InvalidateMess(new VField(
                BookingP.F.customerPrice), M.L().NothingReserved());
        errMess.add(e);
        return errMess;
    }

}
