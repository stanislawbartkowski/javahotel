/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.appengine.api.datastore.Key;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;
import com.javahotel.types.INumerable;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class Payment implements INumerable,IId {
	
	// =====================	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Key id;
    
    public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();		
	}
// =====================	

    @Basic(optional=false)
    private PaymentMethod payMethod;
    @Basic(optional=false)
    private boolean sumOp;
    @Basic(optional=false)
    private BigDecimal amount;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date datePayment;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;
    @Basic
    private String remarks;
	@ManyToOne(optional = false)
	@JoinColumn(name = "bill", nullable = false)
    private Bill bill;
    @Basic(optional=false)
    private Integer lp;

    public PaymentMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PaymentMethod payMethod) {
        this.payMethod = payMethod;
    }

    public boolean isSumOp() {
        return sumOp;
    }

    public void setSumOp(boolean sumOp) {
        this.sumOp = sumOp;
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

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

}
