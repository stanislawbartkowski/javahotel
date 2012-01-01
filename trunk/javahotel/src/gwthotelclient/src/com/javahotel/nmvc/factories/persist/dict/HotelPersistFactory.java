/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.persist.dict;

import javax.inject.Inject;

import com.javahotel.client.IResLocator;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.RType;

/**
 * @author hotel
 * 
 */
public class HotelPersistFactory implements IHotelPersistFactory {

    private final IResLocator rI;

    @Inject
    public HotelPersistFactory(IResLocator rI) {
        this.rI = rI;
    }

    @Override
    public IPersistRecord construct(DataType d, boolean validate) {
        if (d.isAllHotels()) {
            return new PersistHotel(validate);
        }
        if (d.isAllPersons()) {
            return new PersistPerson(validate);
        }
        if (d.isDictType()) {
            switch (d.getdType()) {
            case BookingList:
                return new PersistRecordBooking(rI, validate);
            case InvoiceList:
                return new PersistInvoice(rI, validate);
            default:
                return new PersistRecordDict(rI, d.getdType(), validate);
            } // switch
        }
        if (d.isRType() && d.getrType() == RType.DownPayments) {
            return null;
        }
        if (d.isAddType()) {
            switch (d.getAddType()) {
            case BookRoom:
            case BookNoRoom:
            case RowPaymentElem:
                return null;
            }
        }
        assert false : rI.getMessages().NotSupportedErrorS();
        return null;
    }

}
