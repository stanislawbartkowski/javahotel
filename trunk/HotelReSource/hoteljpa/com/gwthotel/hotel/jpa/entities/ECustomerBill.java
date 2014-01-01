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
package com.gwthotel.hotel.jpa.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "removeAllBills", query = "DELETE FROM ECustomerBill x WHERE x.hotel = ?1"),
        @NamedQuery(name = "removeBillsForReservation", query = "DELETE FROM ECustomerBill x WHERE x.reservation = ?1"),
        @NamedQuery(name = "findBillsForReservation", query = "SELECT x FROM ECustomerBill x WHERE x.reservation = ?1"),

        @NamedQuery(name = "findAllBills", query = "SELECT x FROM ECustomerBill x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllBills", query = "DELETE FROM ECustomerBill x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneBill", query = "SELECT x FROM ECustomerBill x WHERE x.hotel = ?1 AND x.name = ?2") })
public class ECustomerBill extends EHotelDict {

    @JoinColumn(name = "customer_id", nullable = false)
    private EHotelCustomer customer;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private EHotelReservation reservation;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    private Date dateOfPayment;

    @ElementCollection
    private List<Long> resDetails;

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

    public List<Long> getResDetails() {
        return resDetails;
    }

    public void setResDetails(List<Long> resDetails) {
        this.resDetails = resDetails;
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

}
