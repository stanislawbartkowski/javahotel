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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import com.gwtmodel.table.IDataListType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
class UtilCust {

    static class ECountParam {
        int guestNo;
        boolean wasEdited;

        ECountParam() {
            guestNo = 0;
            wasEdited = false;
        }
    }

    static ECountParam countGuests(IDataListType dList) {
        ECountParam p = new ECountParam();
        Iterable<AbstractToCheckGuest> i = DataUtil.getI(dList);
        for (AbstractToCheckGuest a : i) {
            CustomerP c = a.getO2();
            if (!UtilCust.EmptyC(c)) {
                p.guestNo++;
            }
            if (a.isWaseditable()) {
                p.wasEdited = true;
            }
        }
        return p;

    }

    static boolean EmptyC(CustomerP p) {
        IField[] fEmpty = { CustomerP.F.firstName, CustomerP.F.lastName,
                CustomerP.F.address1 };
        return DataUtil.isEmpty(p, fEmpty);
    }

}
