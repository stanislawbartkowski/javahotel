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
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.reservation.ResStatus;
import com.jython.ui.server.gae.security.entities.EObjectDict;

@Entity
public class EHotelReservation extends EObjectDict {

    private Ref<EHotelCustomer> customer;

    private ResStatus status = ResStatus.OPEN;

    private Date termOfAdvanceDeposit;

    private Double advanceDeposit;

    private Double advancePayment;

    private Date dateofadvancePayment;

    public EHotelCustomer getCustomer() {
        return customer.get();
    }

    public void setCustomer(EHotelCustomer customer) {
        this.customer = Ref.create(customer);
    }

    public ResStatus getStatus() {
        return status;
    }

    public void setStatus(ResStatus status) {
        this.status = status;
    }

    public Date getTermOfAdvanceDeposit() {
        return termOfAdvanceDeposit;
    }

    public void setTermOfAdvanceDeposit(Date termOfAdvanceDeposit) {
        this.termOfAdvanceDeposit = termOfAdvanceDeposit;
    }

    public Date getDateofadvancePayment() {
        return dateofadvancePayment;
    }

    public void setDateofadvancePayment(Date dateofadvancePayment) {
        this.dateofadvancePayment = dateofadvancePayment;
    }

    public BigDecimal getAdvanceDeposit() {
        if (advanceDeposit == null)
            return null;
        return new BigDecimal(advanceDeposit);
    }

    public void setAdvanceDeposit(BigDecimal advanceDeposit) {
        this.advanceDeposit = DictUtil.toDouble(advanceDeposit, true);
    }

    public BigDecimal getAdvancePayment() {
        if (advancePayment == null)
            return null;
        return new BigDecimal(advancePayment);
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = DictUtil.toDouble(advancePayment, true);
    }

}
