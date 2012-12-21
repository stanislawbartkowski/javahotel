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
package com.javahotel.db.copy;

import com.javahotel.common.toobject.InvoiceP;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.Invoice;
import com.javahotel.db.jtypes.ToLD;
import com.javahotel.db.util.HotelChangeXMLToMap;
import com.javahotel.db.util.HotelCreateXML;
import com.javahotel.db.util.HotelVerifyXML;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.messid.IMessId;
import com.javahotel.dbres.resources.IMess;

/**
 * @author hotel
 * 
 */
class CopyInvoice {

    static void copy(final ICommandContext iC, InvoiceP sou, Invoice dest) {
        if (sou.getBooking() == null) {
            iC.logFatal(IMessId.NULLBOOKINGINVOICE);
        }
        if (sou.getCustomer() == null) {
            iC.logFatal(IMessId.NULLCUSTOMERINVOICE, sou.getBooking());
        }
        CopyHelper.setPattName(iC, sou, IMess.INVOICEPATTID, IMess.INVOICEPATT);
        CopyHelper.copyDict1(iC, sou, dest, FieldList.InvoiceList);
        CopyHelper.checkPersonDateOp(iC, dest);
        CopyHelper.copyCustomer(iC, sou, dest);
        CopyHelper.copyBooking(iC, sou, dest);
        sou.getInvoiceD().getdFields()
                .put(InvoiceP.INVOICENUMBER, sou.getName());
        String xml = HotelCreateXML.constructXMLFile(iC, IMess.INVOICEPATTERN,
                sou.getInvoiceD());
        boolean ok = false;
        if (xml != null && HotelVerifyXML.verify(iC, xml, IMess.INVOICEXSD)) {
            ok = true;
        }
        if (!ok) {
            iC.getLog().getL().info(xml);
            String mess = iC.logEvent(IMessId.INPROPERINVOICEXML,
                    dest.getName(), IMess.INVOICEXSD);
            throw new HotelException(mess);
        }
        dest.setInvoiceXML(xml);
    }

    static void copy(final ICommandContext iC, Invoice sou, InvoiceP dest) {
        CopyHelper.copyDict2(iC, sou, dest, FieldList.InvoiceList);
        HotelChangeXMLToMap.constructMapFromXML(iC, dest.getInvoiceD(),
                sou.getInvoiceXML());
        String i = (String) dest.getInvoiceD().getdFields()
                .get(InvoiceP.INVOICENUMBER);
        dest.setName(i);
        dest.setCustomer(ToLD.toLId(sou.getCustomer().getId()));
        dest.setBooking(ToLD.toLId(sou.getBooking().getId()));
    }

}
