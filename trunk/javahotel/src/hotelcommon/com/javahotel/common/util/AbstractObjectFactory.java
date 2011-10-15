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
package com.javahotel.common.util;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.common.toobject.InvoiceIssuerP;

/**
 * Factory of dictionary objects
 * @author stanislawbartkowski@gmail.com
 */
public class AbstractObjectFactory {

    private AbstractObjectFactory() {
    }

    /**
     * Constructs dictionary object
     * @param d DictType
     * @return Object created
     */
    public static AbstractTo constructAbstract(final DictType d) {
        switch (d) {
            case RoomStandard:
                return new RoomStandardP();
            case RoomFacility:
                return new DictionaryP();
            case RoomObjects:
                return new ResObjectP();
            case VatDict:
                return new VatDictionaryP();
            case ServiceDict:
                return new ServiceDictionaryP();
            case OffSeasonDict:
                return new OfferSeasonP();
            case PriceListDict:
                return new OfferPriceP();
            case CustomerList:
                return new CustomerP();
            case BookingList:
                return new BookingP();
            case PaymentRowList:
                return new PaymentRowP();
            case IssuerInvoiceList:
                return new InvoiceIssuerP();

        }
        return null;
    }
}
