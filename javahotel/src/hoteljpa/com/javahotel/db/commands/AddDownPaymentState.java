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
package com.javahotel.db.commands;

import java.util.List;

import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.copy.CopyHelper;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.dbjpa.copybean.GetFieldHelper;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.types.INumerable;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AddDownPaymentState extends CommandAbstract {

    private final String resName;
    private final PaymentP payP;
    private final BookingStateP stateP;
    private final boolean stateNoChange;

    public AddDownPaymentState(final SessionT se, final String ho,
            final PaymentP p, final BookingStateP s, final String resName,
            final boolean stateNoChange) {
        super(se, true, new HotelT(ho));
        this.payP = p;
        this.stateP = s;
        this.resName = resName;
        this.stateNoChange = stateNoChange;
    }

    private void setCol(final Object p,
            final List<? extends INumerable> col, final INumerable sou,
            final Class cla, final String name, final Class mecl) {

        INumerable dest;
        try {
            dest = (INumerable) cla.newInstance();
        } catch (InstantiationException ex) {
            throw new HotelException(ex);
        } catch (IllegalAccessException ex) {
            throw new HotelException(ex);
        }
        CommonCopyBean.copyB(iC, sou, dest);
//        dest.setLp(lp);
        GetMaxUtil.setNextLp(col, dest);
        GetFieldHelper.setterVal(dest, p, name, mecl,
                iC.getLog());
        CopyHelper.checkPersonDateOp(iC, dest);
        ((List) col).add(dest);
    }

    @Override
    protected void command() {
        Booking b = getBook(resName);
        Bill bi = HotelHelper.getBill(b);
        if (payP != null) {
            setCol(bi, bi.getPayments(), payP,
                    Payment.class, "bill", Bill.class);
        }
        if (stateP != null) {
            boolean changeS = true;
            if (stateNoChange) {
                BookingState sta = GetMaxUtil.getLast(b.getState());
                if ((sta != null) && sta.getBState() == stateP.getBState()) {
                    changeS = false;
                }
            }
            if (changeS) {
                setCol(b, b.getState(), stateP, BookingState.class, "booking", Booking.class);
            }
        }
        iC.getJpa().changeRecord(b);
    }
}