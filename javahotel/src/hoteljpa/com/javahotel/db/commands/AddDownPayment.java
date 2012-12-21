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
package com.javahotel.db.commands;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.common.toobject.PaymentP;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddDownPayment extends CommandAbstract {

    private final String resName;
    private final List<PaymentP> payP;

    public AddDownPayment(final SessionT se, final String ho,
            final List<PaymentP> payP, final String resName) {
        super(se, true, new HotelT(ho));
        this.resName = resName;
        this.payP = payP;
    }

    @Override
    protected void command() {
        Booking b = getBook(resName);
        b.setPayments(new ArrayList<Payment>());
        for (PaymentP p : payP) {
            setCol(b, b.getPayments(), p, Payment.class, "booking",
                    Booking.class);
        }
        iC.getJpa().changeRecord(b);
    }
}