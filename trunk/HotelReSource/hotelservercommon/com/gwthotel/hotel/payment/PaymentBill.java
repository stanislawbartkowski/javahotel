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
package com.gwthotel.hotel.payment;

import java.math.BigDecimal;
import java.util.Date;

import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;

public class PaymentBill extends PropDescription {

    private static final long serialVersionUID = 1L;

    private Date dateOfPayment;

    private BigDecimal paymentTotal;

    public String getBillName() {
        return getAttr(IHotelConsts.PAYMENTBILLNAME);
    }

    public void setBillName(String billName) {
        this.setAttr(IHotelConsts.PAYMENTBILLNAME, billName);
    }

    public void setPaymentMethod(String paymentMethod) {
        setAttr(IHotelConsts.PAYMENTMETHOD, paymentMethod);
    }

    public String getPaymentMethod() {
        return getAttr(IHotelConsts.PAYMENTMETHOD);
    }

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

}
