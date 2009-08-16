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

import com.javahotel.db.hotelbase.jpa.CustomerRemark;
import com.javahotel.db.hotelbase.jpa.OfferServicePrice;
import com.javahotel.db.hotelbase.jpa.OfferSpecialPrice;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.SessionT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestGetNumber extends CommandAbstract {

    private final int rType;
    private int res;

    public TestGetNumber(final SessionT se, final int rType,
            final HotelT hotel) {
        super(se, false, hotel);
        this.rType = rType;
    }

    @Override
    protected void command() {

        Class cla = null;

        switch (rType) {
            case IHotelTest.OFFERSEASONSPECIALPRICE:
                cla = OfferSpecialPrice.class;
                break;
            case IHotelTest.OFFERSERVICEPRICE:
                cla = OfferServicePrice.class;
                break;
            case IHotelTest.CUSTOMERREMARKS:
                cla = CustomerRemark.class;
                break;
            case IHotelTest.BOOKINGPAYMENTREGISTER:
                cla = Payment.class;
                break;
        }
//        Long l = iC.getJpa().getNumber(cla);
//        res = l.intValue();
        res = GetQueries.getNumber(iC, cla);
    }

    public int getRes() {
        return res;
    }
}
