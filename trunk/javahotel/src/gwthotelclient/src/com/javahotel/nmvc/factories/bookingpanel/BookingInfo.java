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
package com.javahotel.nmvc.factories.bookingpanel;

import java.math.BigDecimal;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.Utils;
import com.javahotel.client.DUtil;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.ButtonClickHandler;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookingInfo extends Composite {

    private final IResLocator rI;
    private final VerticalPanel vp = new VerticalPanel();
    private LId resId;
    private final BookingStateP bState;

    // private class CC implements RData.IOneList<CustomerP> {
    //
    // public void doOne(CustomerP p) {
    // CustomerPopInfo po = new CustomerPopInfo(p);
    // vp.add(po);
    // }
    // }

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
            assert e != null : "Reservation " + resId.getId().longValue()
                    + " not found";
            String st = DUtil.getBookingS(e.getCheckIn(), e.getCheckOut());
            vp.add(new Label(st));
            st = DUtil.getLodgingS(e.getCheckIn(), e.getCheckOut());
            vp.add(new Label(st));
            if (bState != null) {
                String na = bState.getBState().toString();
                String name = (String) rI.getLabels().BookingStateType()
                        .get(na);
                if (name != null) {
                    vp.add(new Label(name));
                }
            }

        }
        BigDecimal c = bR.getCustomerPrice();
        String price;
        if (c != null) {
            price = Utils.DecimalToS(c);
        } else {
            price = "";
        }
        vp.add(new Label("Cena " + price));
        vp.add(new Label("Liczba osób " + p.getNoPersons()));
        Button b = new Button("Zobacz");
        vp.add(b);
        b.addClickHandler(new ButtonClickHandler<BookingP>(p,
                DictType.BookingList));

        // LId custId = p.getCustomer();
        // assert custId != null : "Customer id" + custId.getId().longValue() +
        // " cannot be null";
        // CommandParam pa = rI.getR().getHotelDictId(DictType.CustomerList,
        // custId);
        // rI.getR().getOne(RType.ListDict, pa, new CC());
    }

    private class R implements BackAbstract.IRunAction<BookingP> {

        @Override
        public void action(BookingP t) {
            setB(t);
        }

    }

    public BookingInfo(final String resName) {
        this.rI = HInjector.getI().getI();

        this.bState = null;
        this.resId = null;
        initWidget(vp);
        vp.add(new Label(resName));
        new BackAbstract<BookingP>().readAbstract(DictType.BookingList,
                resName, new R());
    }
}
