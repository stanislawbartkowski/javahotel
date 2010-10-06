/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.dialog.user.booking;

import java.math.BigDecimal;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.user.custinfo.CustomerPopInfo;
import com.javahotel.client.rdata.RData;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookingInfo extends Composite {

    private final IResLocator rI;
    private final String resName;
    private final VerticalPanel vp = new VerticalPanel();
    private LId resId;
    private final BookingStateP bState;

    private class CC implements RData.IOneList<CustomerP> {

        public void doOne(CustomerP p) {
            CustomerPopInfo po = new CustomerPopInfo(p);
            vp.add(po);
        }
    }

    private void setB(BookingP p) {
        BookRecordP bR = GetMaxUtil.getLastBookRecord(p);
        if (resId != null) {
            BookElemP e = null;
            for (BookElemP ee : bR.getBooklist()) {
                for (PaymentRowP pr : ee.getPaymentrows()) {
                    if (pr.getId().equals(resId)) {
                        e = ee;
                    }
                }
            }
            assert e != null : "Reservation " + resId.getId().longValue() + " not found";
            String st = CommonUtil.getBookingS(e.getCheckIn(), e.getCheckOut());
            vp.add(new Label(st));
            st = CommonUtil.noLodgings(e.getCheckIn(), e.getCheckOut());
            vp.add(new Label(st));
            if (bState != null) {
                String na = bState.getBState().toString();
                String name = (String) rI.getLabels().BookingStateType().get(na);
                if (name != null) {
                    vp.add(new Label(name));
                }
            }

        }
        BigDecimal c = bR.getCustomerPrice();
        String price;
        if (c != null) {
            price = CommonUtil.DecimalToS(c);
        } else {
            price = "";
        }
        vp.add(new Label("Cena " + price));
        LId custId = p.getCustomer();
        assert custId != null : "Customer id" + custId.getId().longValue() + " cannot be null";
        CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
                custId);
        rI.getR().getOne(RType.ListDict, pa, new CC());
    }

    private class R implements RData.IOneList<BookingP> {

        public void doOne(BookingP p) {
            setB(p);
        }
    }

    public BookingInfo(final IResLocator rI, final BookingP p,
            final LId BookId, final BookingStateP bState) {
        this.rI = rI;
        this.bState = bState;
        this.resName = p.getName();
        this.resId = BookId;
        initWidget(vp);
        vp.add(new Label(resName));
        setB(p);
    }

    public BookingInfo(final IResLocator rI, final String resName) {
        this.rI = rI;
        this.bState = null;
        this.resName = resName;
        this.resId = null;
        initWidget(vp);
        vp.add(new Label(resName));
        CommandParam p = rI.getR().getHotelDictName(DictType.BookingList,
                resName);
        rI.getR().getOne(RType.ListDict, p, new R());
    }
}
