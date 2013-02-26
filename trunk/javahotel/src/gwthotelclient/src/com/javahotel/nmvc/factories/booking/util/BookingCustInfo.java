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

import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.javahotel.common.toobject.CustomerP;

public class BookingCustInfo {

    private final boolean addCust;
    private final boolean changeCust;
    private final CustomerP cust;

    public boolean isAddCust() {
        return addCust;
    }

    public boolean isChangeCust() {
        return changeCust;
    }

    public CustomerP getCust() {
        return cust;
    }

    public BookingCustInfo(IEditChooseRecordContainer eCust, CustomerP cust) {
        addCust = eCust.getNewCheck();
        changeCust = eCust.getChangeCheck();
        this.cust = cust;
    }

    public BookingCustInfo(boolean addCust, boolean changeCust, CustomerP cust) {
        this.addCust = addCust;
        this.changeCust = changeCust;
        this.cust = cust;
    }

}