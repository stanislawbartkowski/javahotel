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

package com.javahotel.db.hotelbase.jpa;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.javahotel.common.command.PaymentMethod;
import com.javahotel.db.jtypes.IId;
import com.javahotel.types.INumerable;
 
/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class Payment extends AbstractIId implements INumerable,IId {
	
    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;
    @Basic
    private String remarks;
    
    @Basic(optional=false)
    private PaymentMethod payMethod;
    @Basic(optional=false)
    private BigDecimal amount;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date datePayment;
	@ManyToOne(optional = false)
	@JoinColumn(name = "booking", nullable = false)
    private Booking booking;
    @Basic(optional=false)
    private Integer lp;

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDateOp() {
        return dateOp;
    }

    public void setDateOp(final Date dateOp) {
        this.dateOp = dateOp;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
    }

    public Date getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(Date datePayment) {
        this.datePayment = datePayment;
    }

    public String getPersonOp() {
        return personOp;
    }

    public void setPersonOp(String personOp) {
        this.personOp = personOp;
    }

    /**
     * @return the booking
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * @param booking the booking to set
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    

}
