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

import com.gwtmodel.table.AbstractListT;
import com.javahotel.common.toobject.InvoiceP;

/**
 * @author hotel
 * 
 */
class MapFields {

    private MapFields() {

    }

    static final MapStringField mapS = new MapString();

    static {
        AbstractListT i = MapInvoiceP.getM();
        mapS.createMap(i.getListKeys());
    }

    private static class MapString extends MapStringField {

        @Override
        SType GetType(String s) {

            if (s.equals(InvoiceP.INVOICE_DATE)) {
                return SType.DATE;
            }
            if (s.equals(InvoiceP.DATE_OF_DSALE)) {
                return SType.DATE;
            }
            if (s.equals(InvoiceP.PAYMENT_DATE)) {
                return SType.DATE;
            }
            if (s.equals(InvoiceP.NUMBER_OF_DAYS)) {
                return SType.INT;
            }
            if (s.equals(InvoiceP.PAYMENT_METHOD)) {
                return SType.PAYMENTMETHOD;
            }
            return SType.STRING;
        }

    }

}
