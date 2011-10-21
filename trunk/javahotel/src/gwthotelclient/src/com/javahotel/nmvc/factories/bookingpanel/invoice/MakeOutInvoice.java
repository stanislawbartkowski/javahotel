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

import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.readres.ISetResText;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.client.types.BackAbstract;
import com.javahotel.client.types.BackAbstract.IRunAction;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.InvoiceIssuerP;

/**
 * @author hotel
 * 
 */
public class MakeOutInvoice {

    private final IResLocator rI;

    private static final String INVOICEHTML = "invoice/invoice1.html";

    private class Synch extends SynchronizeList {

        List<InvoiceIssuerP> val;
        String html;
        WSize w;
        BookingP p;
        CustomerP buyer;

        Synch() {
            super(3);
        }

        @Override
        protected void doTask() {
            new MakeOutInvoiceWidget(p, val, buyer, html, w);
        }

    }

    public MakeOutInvoice() {
        rI = HInjector.getI().getI();
    }

    private class ReadL implements IVectorList<InvoiceIssuerP> {

        private final Synch sy;

        ReadL(Synch sy) {
            this.sy = sy;
        }

        @Override
        public void doVList(List<InvoiceIssuerP> val) {
            sy.val = val;
            sy.signalDone();
        }

    }

    private class SetHtml implements ISetResText {

        private final Synch sy;

        SetHtml(Synch sy) {
            this.sy = sy;
        }

        @Override
        public void setResText(String s) {
            sy.html = s;
            sy.signalDone();
        }

    }

    public void doInvoice(BookingP p, WSize w) {

        final Synch sy = new Synch();
        sy.w = w;
        sy.p = p;

        CommandParam pa = rI.getR().getHotelCommandParam();
        pa.setDict(DictType.IssuerInvoiceList);
        rI.getR().getList(RType.ListDict, pa, new ReadL(sy));
        rI.readRes().readRes(new SetHtml(sy), INVOICEHTML);

        IRunAction<CustomerP> iCustomer = new IRunAction<CustomerP>() {

            @Override
            public void action(CustomerP t) {
                sy.buyer = t;
                sy.signalDone();
            }
        };
        new BackAbstract<CustomerP>().readAbstract(DictType.CustomerList,
                p.getCustomer(), iCustomer);
    }

}
