/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
import javax.persistence.Transient;

import com.javahotel.db.jtypes.IId;

/**
 * Entity: advance (forward) payment
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField = "serviceKey", objectField = "rService")
public class AddPayment extends AbstractIId implements IId {

    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;
    @Basic
    private String remarks;
    
    @Transient
    private ServiceDictionary rService;

    @Basic(optional = false)
    private Long serviceKey;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date payDate;
    @Basic(optional = false)
    private BigDecimal offerPrice;
    @Basic(optional = false)
    private BigDecimal customerPrice;
    @ManyToOne(optional = false)
    @JoinColumn(name = "booking", nullable = false)
    private Booking booking;
    @Basic(optional = false)
    private Integer lp;

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * @param payDate
     *            the payDate to set
     */
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    /**
     * @return the offerPrice
     */
    public BigDecimal getOfferPrice() {
        return offerPrice;
    }

    /**
     * @param offerPrice
     *            the offerPrice to set
     */
    public void setOfferPrice(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }

    /**
     * @return the customerPrice
     */
    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    /**
     * @param customerPrice
     *            the customerPrice to set
     */
    public void setCustomerPrice(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    /**
     * @return the dateOp
     */
    public Date getDateOp() {
        return dateOp;
    }

    /**
     * @param dateOp
     *            the dateOp to set
     */
    public void setDateOp(Date dateOp) {
        this.dateOp = dateOp;
    }

    /**
     * @return the personOp
     */
    public String getPersonOp() {
        return personOp;
    }

    /**
     * @param personOp
     *            the personOp to set
     */
    public void setPersonOp(String personOp) {
        this.personOp = personOp;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the rService
     */
    public ServiceDictionary getRService() {
        return rService;
    }

    /**
     * @param rService
     *            the rService to set
     */
    public void setRService(ServiceDictionary rService) {
        this.rService = rService;
    }

    /**
     * @return the lp
     */
    public Integer getLp() {
        return lp;
    }

    /**
     * @param lp
     *            the lp to set
     */
    public void setLp(Integer lp) {
        this.lp = lp;
    }

    public Long getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(Long serviceKey) {
        this.serviceKey = serviceKey;
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