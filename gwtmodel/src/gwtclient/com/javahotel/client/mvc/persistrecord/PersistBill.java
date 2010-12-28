/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.view.callback.CommonCallBack;
import com.javahotel.client.GWTGetService;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.mvc.auxabstract.ABillsCustomer;
import com.javahotel.client.mvc.auxabstract.BillsCustomer;
import com.javahotel.client.mvc.auxabstract.NumAddPaymentP;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictdata.model.IGetAddPaymentList;
import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.CustomerP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PersistBill implements IPersistRecord {

    private final IResLocator rI;

    PersistBill(IResLocator rI) {
        this.rI = rI;
    }

    private class CC extends CommonCallBack<Object> {

        private final IPersistResult pR;
        private final int action;

        CC(IPersistResult p, int action) {
            this.pR = p;
            this.action = action;
        }

        @Override
        public void onMySuccess(Object arg) {
            rI.getR().invalidateCacheList();
            CallSuccess.callI(pR, action, null, null);
        }

    }

    public void persist(int action, RecordModel a, IPersistResult ires) {
        if (action == IPersistAction.DELACTION) {
            return;
        }
        IGetAddPaymentList iG = (IGetAddPaymentList) a.getAuxData1();
        String resName = iG.getResName();
        List<NumAddPaymentP> aList = iG.getList();
        ABillsCustomer aB = (ABillsCustomer) a.getA();
        BillsCustomer bb = (BillsCustomer) aB.getO();

        ABillsCustomer aB1 = (ABillsCustomer) a.getBeforea();
        BillsCustomer bb1 = (BillsCustomer) aB1.getO();
        CustomerP cu = bb1.getCust();
        CommandParam pa = rI.getR().getHotelCommandParam();
        BillP bi = new BillP();
        bi.setBillType(BillEnumTypes.IndividualBill);
        bi.setCustomer(cu.getId());
        // a.getBeforea();
        bi.setName(bb.getName());
        bi.setDescription(bb.getDesc());
        bi.setOPrice(bb.getOPrice());
        ArrayList<AddPaymentP> aL = new ArrayList<AddPaymentP>();
        for (NumAddPaymentP aa : aList) {
            AddPaymentP ap = (AddPaymentP) aa.getO();
            aL.add(ap);
        }
        pa.setAddpayment(aL);
        pa.setReservName(resName);
        pa.setBill(bi);
        GWTGetService.getService().hotelOp(HotelOpType.PersistAddPayment, pa,
                new CC(ires, action));
    }

}
