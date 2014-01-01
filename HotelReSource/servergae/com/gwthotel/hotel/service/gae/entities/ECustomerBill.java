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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ECustomerBill extends EHotelDict {

    private Ref<EHotelCustomer> payer;
    @Index
    private String payerName;

    private Ref<EHotelReservation> reservation;

    @Index
    private String resName;

    private Date issueDate;

    private Date dateOfPayment;

    private List<Long> resDetails = new ArrayList<Long>();

    public EHotelCustomer getPayer() {
        return payer.get();
    }

    public void setPayer(EHotelCustomer payer) {
        this.payerName = payer.getName();
        this.payer = Ref.create(payer);
    }
    
    public EHotelReservation getReservation() {
        return reservation.get();
    }

    public void setReservation(EHotelReservation reservation) {
        this.resName = reservation.getName();
        this.reservation = Ref.create(reservation);
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public List<Long> getResDetails() {
        return resDetails;
    }
    
}
