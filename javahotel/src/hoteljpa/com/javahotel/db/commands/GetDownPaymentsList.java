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
package com.javahotel.db.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.jpa.AdvancePayment;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class GetDownPaymentsList extends CommandAbstract {

    private List<DownPaymentP> col;
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
        List<AdvancePayment> c = GetQueries.getValidationForHotel(iC);
        PeriodT pe = new PeriodT(dFrom, dTo);
        Map<Long, AdvancePayment> ma = new HashMap<Long, AdvancePayment>();
        for (AdvancePayment va : c) {
            HId id = va.getBill().getBooking().getId();
            AdvancePayment va1 = ma.get(id.getL());
            if ((va1 == null) ||
                    (va1.getLp().compareTo(va.getLp()) == -1)) {
                ma.put(id.getL(), va);
            }
        }
        col = new ArrayList<DownPaymentP>();
        for (AdvancePayment va : ma.values()) {
            int cc = DateUtil.compPeriod(va.getValidationDate(), pe);
            if (cc != 0) {
                continue;
            }
            DownPaymentP dp = new DownPaymentP();
            CommonCopyBean.copyB(iC, va, dp);
            dp.setResId(va.getBill().getBooking().getName());
            dp.setCustomerId(ToLD.toLId(va.getBill().getBooking().getCustomer().getId()));
            BigDecimal sum = HotelHelper.sumPayment(va.getBill().getPayments());
            dp.setSumPayment(sum);
            col.add(dp);
        }
    }
}

