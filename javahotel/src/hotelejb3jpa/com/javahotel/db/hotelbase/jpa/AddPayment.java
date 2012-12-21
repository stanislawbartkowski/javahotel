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


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class AddPayment extends AbstractIID {

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date payDate;
    @Basic(optional=false)
    private BigDecimal offerPrice;
    @Basic(optional=false)
    private BigDecimal customerPrice;
    @Basic(optional=false)
    private BigDecimal customerSum;
    @Basic(optional=false)
    private boolean sumOp;
    @ManyToOne(optional = false)
    @JoinColumn(name = "bill", nullable = false)
    private Bill bill;
    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;
    @Basic
    private String remarks;
    @ManyToOne(optional=false)
    @JoinColumn(name="service", nullable=false, updatable=false)
    private ServiceDictionary rService;
    @Basic(optional=false)
    private BigDecimal noSe;
    @Basic(optional=false)
    private Integer lp;

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate;
    }

    /**
     * @param payDate the payDate to set
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
     * @param offerPrice the offerPrice to set
     */
    public void setOfferRate(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }

    /**
     * @return the customerPrice
     */
    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    /**
     * @param customerPrice the customerPrice to set
     */
    public void setCustomerRate(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    /**
     * @return the bill
     */
    public Bill getBill() {
        return bill;
    }

    /**
     * @param bill the bill to set
     */
    public void setBill(Bill bill) {
        this.bill = bill;
    }

    /**
     * @return the dateOp
     */
    public Date getDateOp() {
        return dateOp;
    }

    /**
     * @param dateOp the dateOp to set
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
     * @param personOp the personOp to set
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
     * @param remarks the remarks to set
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
     * @param rService the rService to set
     */
    public void setRService(ServiceDictionary rService) {
        this.rService = rService;
    }

    /**
     * @return the noSe
     */
    public BigDecimal getNoSe() {
        return noSe;
    }

    /**
     * @param noSe the noSe to set
     */
    public void setNoSe(BigDecimal noSe) {
        this.noSe = noSe;
    }

    /**
     * @return the lp
     */
    public Integer getLp() {
        return lp;
    }

    /**
     * @param lp the lp to set
     */
    public void setLp(Integer lp) {
        this.lp = lp;
    }

    /**
     * @return the customerSum
     */
    public BigDecimal getCustomerSum() {
        return customerSum;
    }

    /**
     * @param customerSum the customerSum to set
     */
    public void setCustomerSum(BigDecimal customerSum) {
        this.customerSum = customerSum;
    }

    /**
     * @return the sumOp
     */
    public boolean isSumOp() {
        return sumOp;
    }

    /**
     * @param sumOp the sumOp to set
     */
    public void setSumOp(boolean sumOp) {
        this.sumOp = sumOp;
    }

}