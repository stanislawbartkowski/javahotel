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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.nmvc.factories.booking.util.IsServiceBooking;
import com.javahotel.nmvc.factories.booking.util.RunCompose;
import com.javahotel.nmvc.factories.booking.util.RunCompose.IRunComposeFactory;

/**
 * @author hotel
 * 
 */
class MakeOutInvoiceWidget {

    private final List<InvoiceIssuerP> val;
    private final String html;
    private final RunCompose rCompose;
    private final CustomerP buyer;
    private final IsServiceBooking iService;

    private class Run implements IRunComposeFactory {

        @Override
        public IFormTitleFactory getTitle() {
            return new IFormTitleFactory() {

                @Override
                public String getFormTitle(ICallContext iContext) {
                    return "Wystawienie faktury";
                }

            };
        }

        @Override
        public ISlotable constructS(ICallContext iContext, IDataType dType,
                BookingP p, BoxActionMenuOptions bOptions, SlotType slType) {
            ISlotable iSlo = new MakeOutInvoiceDialog(dType,
                    Empty.getDataType1(), val.get(0), html, buyer, p, iService);
            return iSlo;
        }

    }

    MakeOutInvoiceWidget(BookingP p, List<InvoiceIssuerP> val, CustomerP buyer,
            String html, WSize w, IsServiceBooking iService) {
        this.val = val;
        this.html = html;
        this.buyer = buyer;
        this.iService = iService;
        this.rCompose = new RunCompose(new Run());
        rCompose.runDialog(Empty.getDataType(), p, w, false, null);

    }

}
