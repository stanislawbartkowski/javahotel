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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "countAllPayments", query = "SELECT COUNT(x) FROM EBillPayment x,ECustomerBill c WHERE x.customerBill = c AND c.hotel = ?1"),
        @NamedQuery(name = "removeAllPayments", query = "DELETE FROM EBillPayment x WHERE x.customerBill IN (SELECT c FROM ECustomerBill c WHERE c.hotel = ?1)"),
        @NamedQuery(name = "removePaymentsforBill", query = "DELETE FROM EBillPayment x WHERE x.customerBill = ?1"),
        @NamedQuery(name = "removeAllPaymentsforReservation", query = "DELETE FROM EBillPayment x WHERE x.customerBill IN (SELECT c FROM ECustomerBill c WHERE c.reservation = ?1)"),
        @NamedQuery(name = "findAllPaymentsForBill", query = "SELECT x FROM EBillPayment x WHERE x.customerBill = ?1") })
public class EBillPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfPayment;

    @Column(nullable = false)
    private BigDecimal paymentTotal;

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerbill_id", nullable = false)
    private ECustomerBill customerBill;

    @Column(nullable = false, length = 16)
    private String paymentMethod;

    private boolean advancepayment;

    @Column(length = 16)
    private String creationPerson;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    private String description;

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public BigDecimal getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(BigDecimal paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public ECustomerBill getCustomerBill() {
        return customerBill;
    }

    public void setCustomerBill(ECustomerBill customerBill) {
        this.customerBill = customerBill;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationPerson() {
        return creationPerson;
    }

    public void setCreationPerson(String creationPerson) {
        this.creationPerson = creationPerson;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isAdvancepayment() {
        return advancepayment;
    }

    public void setAdvancepayment(boolean advancepayment) {
        this.advancepayment = advancepayment;
    }

}
