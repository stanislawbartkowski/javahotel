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
package com.javahotel.client.dialog.user.downpayment;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.dialog.user.addpayment.AddPayment;
import com.javahotel.client.dialog.user.booking.BookingInfo;
import com.javahotel.client.mvc.auxabstract.AdvancePaymentCustomer;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.crud.controler.ICrudPersistSignal;
import com.javahotel.client.widgets.disclosure.DisclosureOptions;
import com.javahotel.client.widgets.popup.PopUpWithClose;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DrawResInfo {

    DrawResInfo(final IResLocator rI, final AdvancePaymentCustomer dr,
            final IField f, final IWidgetSize w, final ICrudPersistSignal pSig) {
        String resName = dr.getResId();
        PopUpWithClose aPanel = new PopUpWithClose(w);
        VerticalPanel vp = aPanel.getVP();

        List<ContrButton> dButton = new ArrayList<ContrButton>();;
        dButton.add(new ContrButton(null, "Wprowadź zaliczkę", 0));
        dButton.add(new ContrButton(null, "Skasuj rezerwację", 1));

        IControlClick cont = new IControlClick() {

            public void click(ContrButton co, Widget w) {
                new AddPayment(rI, w, dr.getResId(),pSig);
            }
        };

        DisclosureOptions op = new DisclosureOptions(rI, dButton, cont);
        vp.add(op);
        vp.add(new BookingInfo(rI, resName));

    }
}
