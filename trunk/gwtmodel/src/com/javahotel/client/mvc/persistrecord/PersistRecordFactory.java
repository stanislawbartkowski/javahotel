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
package com.javahotel.client.mvc.persistrecord;

import com.google.inject.Inject;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PersistRecordFactory {

    private final IResLocator rI;
    
    @Inject
    public PersistRecordFactory(IResLocator rI) {
        this.rI = rI;
    }
    
    public IPersistRecord getPersistDict(final DictData da) {
        if (da.isSe()) {
            switch (da.getSE()) {
              case SpecialPeriod:
                  return new PersistRecordPeriod();
                case CustomerPhone:
                case CustomerAccount:
                case BookingElem:
                case AddPaymentList:
                    return new PersistRecordPhoneBank();
                case AddPayment:
                    return new PaymentPersist(rI);
                case BillsList:
                    return new PersistBill(rI);

                default:
                    return null;
            }
        }
        if (da.getD() != null) {
            switch (da.getD()) {
                case RoomStandard:
                    return new PersistRecordRoomStandard(rI);
                case RoomObjects:
                    return new PersistRecordResRoom(rI);
                case PriceListDict:
                    return new PersistRecordPrice(rI);
                case BookingList:
                    return new PersistRecordBooking(rI);
                default:
                    break;
            }
            return new PersistRecordDict(rI, da.getD());
        }
        return new PersistHotelOsoba(rI, da.getRt());
    }

    public IPersistRecord getPersistList(final DictData da) {
        return new PersistGuestList(rI);
    }
}
