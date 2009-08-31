/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.hotelbase.impl;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.db.commands.AddDownPaymentState;
import com.javahotel.db.commands.AddGuests;
import com.javahotel.db.commands.AddPayment;
import com.javahotel.db.commands.ChangeBookingToStay;
import com.javahotel.db.commands.DictNumberRecord;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.SessionT;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Stateless(mappedName = "hotelOpEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class HotelOp implements IHotelOp {

    public void hotelOp(final SessionT sessionId, final HotelOpType op,
            final CommandParam p) {
        hotelopret(sessionId, op, p);
    }

    public ReturnPersist hotelOpRet(final SessionT sessionId,
            final HotelOpType op, final CommandParam p) {
        return hotelopret(sessionId, op, p);
    }

    private static ReturnPersist hotelopret(final SessionT sessionId,
            final HotelOpType op, final CommandParam p) {
        String ho = p.getHotel();
        ReturnPersist ret = null;
        switch (op) {
            case NumberOfDictRecords:
                DictNumberRecord nu = new DictNumberRecord(sessionId,
                        p.getDict(null), new HotelT( p.getHotel()));
                nu.run();
                ret = nu.getRes();
                break;
                
            case ChangeBookingToStay:
                String resName = p.getReservName();
                ChangeBookingToStay sta = new ChangeBookingToStay(sessionId,
                        ho, resName);
                sta.run();
                ret = sta.getRet();
                break;
            case payDownPaymentState:
            case payDownPaymentStateNoChange:
                PaymentP pa = p.getDownPayment();
                BookingStateP st = p.getStateP();
                resName = p.getReservName();
                AddDownPaymentState dcom = new AddDownPaymentState(sessionId,
                        ho, pa, st, resName,
                        op == HotelOpType.payDownPaymentStateNoChange);
                dcom.run();
                break;
            case PersistGuests:
                resName = p.getReservName();
                Map<String, List<GuestP>> c = p.getGuests();
                AddGuests a = new AddGuests(sessionId, ho, resName, c);
                a.run();
                break;
            case PersistAddPayment:
                resName = p.getReservName();
                BillP bi = p.getBill();
                List<AddPaymentP> add = p.getAddpayment();
                AddPayment addCo = new AddPayment(sessionId, ho, resName,
                        bi, add);
                addCo.run();
                ret = addCo.getRet();
                break;
            default:

        }
        return ret;
    }

    public ReturnPersist hotelOpRet(SessionT sessionID, CommandParam p) {
        return hotelOpRet(sessionID, p.getoP(), p);
    }

    public List<ReturnPersist> hotelOpRet(SessionT sessionID, List<CommandParam> p) {
        List<ReturnPersist> ret = new ArrayList<ReturnPersist>();
        for (CommandParam pa : p) {
            ReturnPersist re = hotelOpRet(sessionID, pa);
            ret.add(re);
        }
        return ret;
    }
}
