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

package com.javahotel.common.toobject;

import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.INumerable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AddPaymentP extends AbstractTo implements INumerable {

    private DateP payDate;
    private DecimalP offerPrice;
    private DecimalP customerPrice;
    private DateP dateOp;
    private String personOp;
    private String remarks;
    private ServiceDictionaryP rService;
    private DecimalP noSe;
    private Integer lp;
    private DecimalP customerSum;
    private boolean sumOp;

    public AddPaymentP() {
        payDate = new DateP();
        offerPrice = new DecimalP();
        customerPrice = new DecimalP();
        dateOp = new DateP();
        noSe = new DecimalP();
        customerSum = new DecimalP();
    }


    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate.getD();
    }

    /**
     * @param payDate the payDate to set
     */
    public void setPayDate(Date payDate) {
        this.payDate.setD(payDate);
    }

    /**
     * @return the offerPrice
     */
    public BigDecimal getOfferPrice() {
        return offerPrice.getDecim();
    }

    /**
     * @param offerPrice the offerPrice to set
     */
    public void setOfferPrice(BigDecimal offerPrice) {
        this.offerPrice.setDecim(offerPrice);
    }

    /**
     * @return the customerPrice
     */
    public BigDecimal getCustomerPrice() {
        return customerPrice.getDecim();
    }

    /**
     * @param customerPrice the customerPrice to set
     */
    public void setCustomerPrice(BigDecimal customerPrice) {
        this.customerPrice.setDecim(customerPrice);
    }

    /**
     * @return the dateOp
     */
    public Date getDateOp() {
        return dateOp.getD();
    }

    /**
     * @param dateOp the dateOp to set
     */
    public void setDateOp(Date dateOp) {
        this.dateOp.setD(dateOp);
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
    public ServiceDictionaryP getRService() {
        return rService;
    }

    /**
     * @param rService the rService to set
     */
    public void setRService(ServiceDictionaryP rService) {
        this.rService = rService;
    }

    /**
     * @return the noSe
     */
    public BigDecimal getNoSe() {
        return noSe.getDecim();
    }

    /**
     * @param noSe the noSe to set
     */
    public void setNoSe(BigDecimal noSe) {
        this.noSe.setDecim(noSe);
    }

    /**
     * @return the customerSum
     */
    public BigDecimal getCustomerSum() {
        return customerSum.getDecim();
    }

    /**
     * @param customerSum the customerSum to set
     */
    public void setCustomerSum(BigDecimal customerSum) {
        this.customerSum.setDecim(customerSum);
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

    public enum F implements IField {

        lp, dateOp,payDate,customerPrice,personOp,remarks,noSe,
        seName,seDesc,sumOp,customerSum;
    };

    @Override
    public Object getF(IField f) {
        F fie = (F) f;
        switch (fie) {
            case lp: return getLp();
            case dateOp: return getDateOp();
            case payDate: return getPayDate();
            case customerPrice: return getCustomerPrice();
            case personOp: return getPersonOp();
            case remarks: return getRemarks();
            case noSe : return getNoSe();
            case seName:
                if (rService == null) { return null; }
                return rService.getName();
            case seDesc:
                if (rService == null) { return null; }
                return rService.getDescription();
            case sumOp:
                return new Boolean(this.isSumOp());
            case customerSum:
                return getCustomerSum();
        }
        return null;
    }

    @Override
    public Class getT(IField f) {
        Class cla = null;
        F fie = (F) f;
        switch (fie) {
            case lp: cla = Integer.class; break;
            case dateOp: cla =  Date.class; break;
            case customerPrice: cla =  BigDecimal.class; break;
            case personOp: cla = String.class; break;
            case remarks: cla = String.class;  break;
            case noSe : cla =  BigDecimal.class; break;
            case payDate: cla = Date.class; break;
            case sumOp: cla = Boolean.class; break;
            case customerSum: cla = BigDecimal.class; break;
            case seName:
            case seDesc:
                cla = String.class; break;
        }
        return cla;
    }

    @Override
    public void setF(IField f, Object o) {
        F fie = (F) f;
        switch (fie) {
            case lp: setLp((Integer) o); break;
            case dateOp: setDateOp((Date) o); break;
            case payDate: setPayDate((Date) o); break;
            case customerPrice: setCustomerPrice((BigDecimal) o); break;
            case personOp: setPersonOp((String) o); break;
            case remarks: setRemarks((String) o);  break;
            case noSe : setNoSe((BigDecimal) o); break;
            case seName:
                if (rService == null) {
                    rService = new ServiceDictionaryP();
                }
                rService.setName((String) o);
                break;
            case seDesc:
                if (rService == null) {
                    rService = new ServiceDictionaryP();
                }
                rService.setDescription((String) o);
                break;
            case sumOp:
                Boolean b = (Boolean) o;
                setSumOp(b.booleanValue());
                break;
            case customerSum:
                setCustomerSum((BigDecimal) o);
                break;
        }
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lP) {
        this.lp = lP;
    }

}
