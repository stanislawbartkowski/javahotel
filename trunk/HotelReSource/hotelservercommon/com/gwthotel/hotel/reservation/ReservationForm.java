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
package com.gwthotel.hotel.reservation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.shared.PropDescription;

public class ReservationForm extends PropDescription {

    private static final long serialVersionUID = 1L;

    private List<ReservationPaymentDetail> resDetail = new ArrayList<ReservationPaymentDetail>();

    private ResStatus status = ResStatus.OPEN;

    private Date termOfAdvanceDeposit;

    private BigDecimal advanceDeposit;

    private BigDecimal advancePayment;

    private Date dateofadvancePayment;

    public List<ReservationPaymentDetail> getResDetail() {
        return resDetail;
    }

    public void setCustomerName(String name) {
        setAttr(IHotelConsts.RESCUSTOMERPROP, name);
    }

    public String getCustomerName() {
        return getAttr(IHotelConsts.RESCUSTOMERPROP);
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

    public BigDecimal getAdvanceDeposit() {
        return advanceDeposit;
    }

    public void setAdvanceDeposit(BigDecimal advanceDeposit) {
        this.advanceDeposit = advanceDeposit;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    public Date getDateofadvancePayment() {
        return dateofadvancePayment;
    }

    public void setDateofadvancePayment(Date dateofadvancePayment) {
        this.dateofadvancePayment = dateofadvancePayment;
    }

}
