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
package com.javahotel.client.mvc.persistrecord;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictdata.model.IBookingModel;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordBooking extends APersistRecordDict {

    PersistRecordBooking(IResLocator rI) {
        super(rI, DictType.BookingList);

    }

    @Override
    protected void persistDict(DictType d, DictionaryP dP, CommonCallBack b) {
        GWTGetService.getService().persistResBookingReturn((BookingP) dP, b);
    }

    private class PersistP implements IPersistResult {

        private final int action;
        private final RecordModel mo;
        private final IPersistResult ires;

        PersistP(int action, RecordModel a, IPersistResult ires) {
            this.action = action;
            this.mo = a;
            this.ires = ires;
        }

        public void success(PersistResultContext re) {
            LId id = re.getRet().getId();
            BookingP p = (BookingP) mo.getA();
            setCustId(p, id);
            ipersist(action, mo, ires);
            rI.getR().invalidateResCache();
        }
    }

    private void setCustId(BookingP p, LId custId) {
        p.setCustomer(custId);
        BillP bill = BillUtil.getBill(p);
        bill.setCustomer(custId);
    }

    @Override
    public void persist(int action, RecordModel a, IPersistResult ires) {
        IBookingModel mo = (IBookingModel) a.getAuxData1();
        CustomerP p = mo.getBookCustomer();
        BookingP bp = (BookingP) a.getA();
        boolean runNow = false;
        if (action == IPersistAction.DELACTION) {
            runNow = true;
        }
        if ((p.getId() != null) && !mo.getCustomerModel().IsModifCustomer()) {
            setCustId(bp, p.getId());
            runNow = true;
        }
        if (runNow) {
            ipersist(action, a, ires);
            rI.getR().invalidateResCache();
            return;
        }
        IPersistRecord re = new PersistRecordDict(rI, DictType.CustomerList);
        RecordModel mod = new RecordModel(null, null);
        mod.setA(p);
        re.persist(action, mod, new PersistP(action, a, ires));
    }
}
