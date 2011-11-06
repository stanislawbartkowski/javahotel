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
package com.javahotel.nmvc.factories.persist.dict;

import com.gwtmodel.table.PersistTypeEnum;
import com.javahotel.client.IResLocator;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.nmvc.factories.booking.util.BookingCustInfo;
import com.javahotel.nmvc.factories.persist.PersistCustomer;
import com.javahotel.types.LId;

/**
 * @author hotel
 * 
 */
class PersistRecordBooking implements IPersistRecord {

    private final IResLocator rI;
    private final boolean validate;

    PersistRecordBooking(final IResLocator rI, boolean validate) {
        this.rI = rI;
        this.validate = validate;
    }

    private class PersistP implements PersistCustomer.ISetCustomerId {

        private final HModelData mo;
        private final IPersistResult ires;
        private final PersistTypeEnum action;
        private final IPersistRecord bPersist;

        PersistP(PersistTypeEnum action, HModelData a, IPersistResult ires,
                IPersistRecord bPersist) {
            this.action = action;
            this.mo = a;
            this.ires = ires;
            this.bPersist = bPersist;
        }

        @Override
        public void setC(LId custId) {
            BookingP p = (BookingP) mo.getA();
            setCustId(p, custId);
            bPersist.persist(action, mo, ires);
        }
    }

    private void setCustId(BookingP p, LId custId) {
        p.setCustomer(custId);
    }

    @Override
    public void persist(PersistTypeEnum action, HModelData ho,
            IPersistResult ires) {

        IPersistRecord bPersist = new PersistRecordDict(rI,
                DictType.BookingList, validate);
        BookingCustInfo bCust = (BookingCustInfo) ho.getCustomData();
        if (action == PersistTypeEnum.REMOVE || (bCust == null)) {
            bPersist.persist(action, ho, ires);
            return;
        }
        PersistCustomer pC = new PersistCustomer();
        pC.persistCustomer(action, bCust, new PersistP(action, ho, ires,
                bPersist));
    }

}
