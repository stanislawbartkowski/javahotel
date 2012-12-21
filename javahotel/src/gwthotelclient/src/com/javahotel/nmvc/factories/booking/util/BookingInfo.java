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
package com.javahotel.nmvc.factories.booking.util;

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
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentRowP;
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

    private void setB(BookingP p) {
        if (resId != null) {
            BookElemP e = null;
            for (BookElemP ee : p.getBooklist()) {
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
        BigDecimal c = (BigDecimal) p.getF(BookingP.F.customerPrice);
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

    }

    private class R implements BackAbstract.IRunAction<BookingP> {

        @Override
        public void action(BookingP t) {
            setB(t);
        }

    }

    private BookingInfo() {
        this.rI = HInjector.getI().getI();

        this.bState = null;
        this.resId = null;
        initWidget(vp);
    }

    public BookingInfo(final String resName) {
        this();
        new BackAbstract<BookingP>().readAbstract(DictType.BookingList,
                resName, new R());
    }

    public BookingInfo(final BookingP p) {
        this();
        setB(p);
    }
}
