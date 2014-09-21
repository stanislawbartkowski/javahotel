/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.service.gae.entities;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.gwthotel.hotel.mailing.HotelMailElem;
import com.jython.ui.server.gae.security.entities.EObjectDict;

@Entity
public class EHotelMail extends EObjectDict {

    private HotelMailElem.MailType mType;

    private Ref<EHotelCustomer> customer;

    private Ref<EHotelReservation> reservation;

    public HotelMailElem.MailType getmType() {
        return mType;
    }

    public void setmType(HotelMailElem.MailType mType) {
        this.mType = mType;
    }

    public EHotelCustomer getCustomer() {
        return customer.get();
    }

    public void setCustomer(EHotelCustomer customer) {
        this.customer = Ref.create(customer);
    }

    public EHotelReservation getReservation() {
        if (reservation == null) return null;
        return reservation.get();
    }

    public void setReservation(EHotelReservation reservation) {
        this.reservation = Ref.create(reservation);
    }

}
