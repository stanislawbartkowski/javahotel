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

package com.javahotel.common.toobject;

import java.math.BigDecimal;
import java.util.Date;

import com.gwtmodel.table.common.dateutil.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.INumerable;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class AddPaymentP extends AbstractTo implements INumerable {

    private DateP payDate;
    private DecimalP offerPrice;
    private DecimalP customerPrice;
    private ServiceDictionaryP rService;
    private Integer lp;
    private OperationData op;

    public AddPaymentP() {
        payDate = new DateP();
        offerPrice = new DecimalP();
        customerPrice = new DecimalP();
        op = new OperationData();
    }

    /**
     * @return the payDate
     */
    public Date getPayDate() {
        return payDate.getD();
    }

    /**
     * @param payDate
     *            the payDate to set
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
     * @param offerPrice
     *            the offerPrice to set
     */
    public void setOfferRate(BigDecimal offerPrice) {
        this.offerPrice.setDecim(offerPrice);
    }

    /**
     * @return the customerPrice
     */
    public BigDecimal getCustomerPrice() {
        return customerPrice.getDecim();
    }

    /**
     * @param customerPrice
     *            the customerPrice to set
     */
    public void setCustomerRate(BigDecimal customerPrice) {
        this.customerPrice.setDecim(customerPrice);
    }

    /**
     * @return the dateOp
     */
    public Date getDateOp() {
        return op.getDateOp();
    }

    /**
     * @param dateOp
     *            the dateOp to set
     */
    public void setDateOp(Date dateOp) {
        op.setDateOp(dateOp);
    }

    /**
     * @return the personOp
     */
    public String getPersonOp() {
        return op.getPersonOp();
    }

    /**
     * @param personOp
     *            the personOp to set
     */
    public void setPersonOp(String personOp) {
        op.setPersonOp(personOp);
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return op.getRemarks();
    }

    /**
     * @param remarks
     *            the remarks to set
     */
    public void setRemarks(String remarks) {
        op.setRemarks(remarks);
    }

    /**
     * @return the rService
     */
    public ServiceDictionaryP getRService() {
        return rService;
    }

    /**
     * @param rService
     *            the rService to set
     */
    public void setRService(ServiceDictionaryP rService) {
        this.rService = rService;
    }



    public enum F implements IField {

        lp, dateOp, payDate, customerPrice, personOp, remarks, seName, seDesc;
    };

    @Override
    public Object getF(IField f) {
        F fie = (F) f;
        switch (fie) {
        case lp:
            return getLp();
        case dateOp:
            return getDateOp();
        case payDate:
            return getPayDate();
        case customerPrice:
            return getCustomerPrice();
        case personOp:
            return getPersonOp();
        case remarks:
            return getRemarks();
        case seName:
            if (rService == null) {
                return null;
            }
            return rService.getName();
        case seDesc:
            if (rService == null) {
                return null;
            }
            return rService.getDescription();
        }
        return null;
    }

    @Override
    public Class<?> getT(IField f) {
        Class<?> cla = null;
        F fie = (F) f;
        switch (fie) {
        case lp:
            cla = Integer.class;
            break;
        case dateOp:
            cla = Date.class;
            break;
        case customerPrice:
            cla = BigDecimal.class;
            break;
        case personOp:
            cla = String.class;
            break;
        case remarks:
            cla = String.class;
            break;
        case payDate:
            cla = Date.class;
            break;
        case seName:
        case seDesc:
            cla = String.class;
            break;
        }
        return cla;
    }

    @Override
    public void setF(IField f, Object o) {
        F fie = (F) f;
        switch (fie) {
        case lp:
            setLp((Integer) o);
            break;
        case dateOp:
            setDateOp((Date) o);
            break;
        case payDate:
            setPayDate((Date) o);
            break;
        case customerPrice:
            setCustomerRate((BigDecimal) o);
            break;
        case personOp:
            setPersonOp((String) o);
            break;
        case remarks:
            setRemarks((String) o);
            break;
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
