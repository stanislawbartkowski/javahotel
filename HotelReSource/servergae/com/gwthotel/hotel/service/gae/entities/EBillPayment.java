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
package com.gwthotel.hotel.service.gae.entities;

import java.math.BigDecimal;
import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class EBillPayment extends EHotelParent {

    private Date dateOfPayment;

    private Double paymentTotal;

    private Ref<ECustomerBill> customerBill;
    @Index
    private String billName;

    private String paymentMethod;

    private String description;

    public ECustomerBill getCustomerBill() {
        return customerBill.get();
    }

    private boolean advancepayment;

    public void setCustomerBill(ECustomerBill b) {
        billName = b.getName();
        customerBill = Ref.create(b);
    }

    public Date getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(Date dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public BigDecimal getPaymentTotal() {
        if (paymentTotal == null)
            return null;
        return new BigDecimal(paymentTotal.doubleValue());
    }

    public void setPaymentTotal(BigDecimal paymentTotal) {
        if (paymentTotal == null) {
            this.paymentTotal = null;
            return;
        }
        this.paymentTotal = paymentTotal.doubleValue();
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAdvancepayment() {
        return advancepayment;
    }

    public void setAdvancepayment(boolean advancepayment) {
        this.advancepayment = advancepayment;
    }

}
