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

import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.BookingState;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * @author hotel
 * 
 */
public class SetNewBookingState extends CommandAbstract {

    private final String resName;
    private final BookingStateP stateP;

    public SetNewBookingState(final SessionT se, final String ho,
            final BookingStateP s, final String resName) {
        super(se, true, new HotelT(ho));
        this.stateP = s;
        this.resName = resName;
    }

    @Override
    protected void command() {
        Booking b = getBook(resName);
        boolean changeS = true;
        BookingState sta = GetMaxUtil.getLast(b.getState());
        if ((sta != null) && sta.getBState() == stateP.getBState()) {
            changeS = false;
        }
        if (changeS) {
            setCol(b, b.getState(), stateP, BookingState.class, "booking",
                    Booking.class);
        }
        iC.getJpa().changeRecord(b);
    }

}
