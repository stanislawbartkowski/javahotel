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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetDownPaymentsList extends CommandAbstract {

    private List<BookingP> col;
    private final Date dFrom;
    private final Date dTo;

    public GetDownPaymentsList(final SessionT se, final HotelT hotel,
            final Date dFrom, final Date dTo) {
        super(se, false, hotel);
        this.dFrom = dFrom;
        this.dTo = dTo;
    }

    public List<? extends AbstractTo> getRes() {
        return col;
    }

    @Override
    protected void command() {
        List<Booking> c = GetQueries.getValidationForHotel(iC);
        PeriodT pe = new PeriodT(dFrom, dTo);
        col = new ArrayList<BookingP>();
        for (Booking va : c) {
            int cc = DateUtil.compPeriod(va.getValidationDate(), pe);
            if (cc != 0) {
                continue;
            }
            BookingP dp = new BookingP();
            CommonCopyBean.copyB(iC, va, dp);
            col.add(dp);
        }
    }
}
