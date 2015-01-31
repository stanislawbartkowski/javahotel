/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.jpa.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gwthotel.hotel.mailing.HotelMailElem;
import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findAllHotelMails", query = "SELECT x FROM EHotelMail x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllHotelmails", query = "DELETE FROM EHotelMail x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneHotelMail", query = "SELECT x FROM EHotelMail x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelMail extends EObjectDict {

    @Column(nullable = false)
    private HotelMailElem.MailType mType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private EHotelCustomer customer;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = true)
    private EHotelReservation reservation;

    public HotelMailElem.MailType getmType() {
        return mType;
    }

    public void setmType(HotelMailElem.MailType mType) {
        this.mType = mType;
    }

    public EHotelCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(EHotelCustomer customer) {
        this.customer = customer;
    }

    public EHotelReservation getReservation() {
        return reservation;
    }

    public void setReservation(EHotelReservation reservation) {
        this.reservation = reservation;
    }

}
