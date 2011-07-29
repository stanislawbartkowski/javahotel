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
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.nmvc.factories.booking.BookingCustInfo;
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

    private class PersistP implements IPersistResult {

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

        public void success(PersistResultContext re) {
            LId id = re.getRet().getId();
            BookingP p = (BookingP) mo.getA();
            setCustId(p, id);
            bPersist.persist(action, mo, ires);
        }
    }

    private void setCustId(BookingP p, LId custId) {
        p.setCustomer(custId);
        BillP bill = BillUtil.getBill(p);
        bill.setCustomer(custId);
    }

    @Override
    public void persist(PersistTypeEnum action, HModelData ho,
            IPersistResult ires) {

        IPersistRecord bPersist = new PersistRecordDict(rI,
                DictType.BookingList, validate);
        BookingP b = (BookingP) ho.getA();
        BookingCustInfo bCust = (BookingCustInfo) ho.getCustomData();
        boolean runNow = false;
        if (action == PersistTypeEnum.REMOVE) {
            runNow = true;
        }
        if ((b.getId() != null) && !bCust.isChangeCust()) {
            b.setCustomer(bCust.getCust().getLId());
            runNow = true;
        }
        if (runNow) {
            bPersist.persist(action, ho, ires);
            return;
        }
        IPersistRecord re = new PersistRecordDict(rI, DictType.CustomerList,
                validate);
        HModelData custH = VModelDataFactory.construct(bCust.getCust());
        re.persist(action, custH, new PersistP(action, ho, ires, bPersist));
    }

}
