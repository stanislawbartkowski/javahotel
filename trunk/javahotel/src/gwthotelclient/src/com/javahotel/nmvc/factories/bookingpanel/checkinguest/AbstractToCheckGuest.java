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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.List;
import java.util.Map;

import com.javahotel.client.abstractto.Compose3AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.nmvc.factories.booking.BookingCustInfo;

/**
 * @author hotel
 * 
 */
@SuppressWarnings("serial")
class AbstractToCheckGuest extends
        Compose3AbstractTo<ResObjectP, CustomerP, GuestP> {

    private final BookElemP bElem;

    private boolean editable = false;
    private boolean waseditable = false;

    private DrawGuest guest = null;

    /**
     * @return the editable
     */
    boolean isEditable() {
        return editable;
    }

    /**
     * @param editable
     *            the editable to set
     */
    void setEditable(boolean editable) {
        this.editable = editable;
        if (editable) {
            waseditable = true;
        }
    }

    /**
     * @return the waseditable
     */
    boolean isWaseditable() {
        return waseditable;
    }

    enum F implements IField {
        ChooseC
    };

    static String chooseCust = "CUSTOMER-CHOOSE-BUTTON";

    @Override
    public Object getF(IField fie) {
        if (fie instanceof F) {
            F f = (F) fie;
            switch (f) {
            case ChooseC:
                return "Wybierz";
            }
        }
        return super.getF(fie);
    }

    AbstractToCheckGuest(BookElemP bElem, ResObjectP o1, CustomerP o2, GuestP o3) {
        super(o1, o2, o3);
        this.bElem = bElem;
    }

    /**
     * @return the guest
     */
    DrawGuest getGuest() {
        return guest;
    }

    /**
     * @param guest
     *            the guest to set
     */
    void setGuest(DrawGuest guest) {
        this.guest = guest;
    }

    BookingCustInfo construct() {
        if (guest != null) {
            return guest.constructCustInfo(getO2());
        }
        GuestP g = getO3();
        boolean newCust = (g.getCustomer() == null);
        return new BookingCustInfo(newCust, waseditable, getO2());
    }

    void addGuestToList(Map<String,List<GuestP>> li) {
//        bElem.getGuests().add(getO3());
        li.get(bElem.getResObject()).add(getO3());
    }
}
