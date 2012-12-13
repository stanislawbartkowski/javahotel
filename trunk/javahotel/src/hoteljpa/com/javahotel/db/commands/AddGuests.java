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
package com.javahotel.db.commands;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.db.copy.CopyGuests;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import java.util.List;
import java.util.Map;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AddGuests extends CommandAbstract {

    private final String resName;
    private final Map<String, List<GuestP>> col;

    public AddGuests(final SessionT se, final String ho,
            final String resName, Map<String, List<GuestP>> col) {
        super(se, true, new HotelT(ho));
        this.resName = resName;
        this.col = col;
    }

    @Override
    protected void command() {
        Booking b = getBook(resName);
        if (b.getBookingType() == BookingEnumTypes.Reservation) {
            iC.logFatal(IMess.RESERVATIONNOTSTAY, resName);
            return;
        }

        CopyGuests.copyGuests(iC, b, col);
        iC.getJpa().changeRecord(b);
    }
}