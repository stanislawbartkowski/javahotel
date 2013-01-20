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

import java.util.List;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.db.copy.AddPayments;
import com.javahotel.db.copy.BeanPrepareKeys;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AddPayment extends CommandAbstract {

    private final String resName;
    private final List<AddPaymentP> col;

    public AddPayment(final SessionT se, final String ho, String resName,
            List<AddPaymentP> col) {
        super(se, true, new HotelT(ho)); // defer start transaction
        this.resName = resName;
        this.col = col;
    }

    @Override
    protected void command() {
        Booking b = getBook(resName);
        BeanPrepareKeys.prepareA(iC, col);
        startTra();
        if (b.getBookingType() == BookingEnumTypes.Reservation) {
            iC.logFatal(IMess.RESERVATIONNOTSTAY, resName);
            return;
        }
        AddPayments.addPayment(iC, b, col);
        iC.getJpa().changeRecord(b);
    }

}