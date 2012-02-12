/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.math.MathUtil;
import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.ILd;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class PaymentRowP extends AbstractTo implements ILd {

    private LId id;
    private DateP rowFrom;
    private DateP rowTo;
    private DecimalP offerRate;
    private DecimalP customerRate;

    public enum F implements IField {

        id, rowFrom, rowTo, offerRate, customerRate, offerPrice, customerPrice
    };

    /**
     * @return the offerRate
     */
    public BigDecimal getOfferRate() {
        return offerRate.getDecim();
    }

    /**
     * @param offerRate
     *            the offerRate to set
     */
    public void setOfferRate(BigDecimal offerRate) {
        this.offerRate.setDecim(offerRate);
    }

    /**
     * @return the customerRate
     */
    public BigDecimal getCustomerRate() {
        return customerRate.getDecim();
    }

    /**
     * @param customerRate
     *            the customerRate to set
     */
    public void setCustomerRate(BigDecimal customerRate) {
        this.customerRate.setDecim(customerRate);
    }

    public Date getRowFrom() {
        return rowFrom.getD();
    }

    public void setRowFrom(final Date rowFrom) {
        this.rowFrom.setD(rowFrom);
    }

    public Date getRowTo() {
        return rowTo.getD();
    }

    public void setRowTo(final Date rowTo) {
        this.rowTo.setD(rowTo);
    }

    private BigDecimal countP(DecimalP price) {
        if (price.getDecim() == null) {
            return null;
        }
        int no = DateUtil.noLodgings(rowFrom.getD(), rowTo.getD());
        return MathUtil.multI(price.getDecim(), no);
    }

    public BigDecimal getCustomerPrice() {
        return countP(customerRate);
    }

    public BigDecimal getOfferPrice() {
        return countP(offerRate);
    }

    public PaymentRowP() {
        rowFrom = new DateP();
        rowTo = new DateP();
        offerRate = new DecimalP();
        customerRate = new DecimalP();
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = String.class;
        F fi = (F) f;
        switch (fi) {
        case id:
            cla = Long.class;
            break;
        case rowFrom:
        case rowTo:
            cla = Date.class;
            break;
        case offerRate:
        case customerRate:
        case customerPrice:
        case offerPrice:
            cla = BigDecimal.class;
            break;

        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return getId();
        case rowFrom:
            return getRowFrom();
        case rowTo:
            return getRowTo();
        case offerPrice:
            return getOfferPrice();
        case customerPrice:
            return getCustomerPrice();
        case offerRate:
            return getOfferRate();
        case customerRate:
            return getCustomerRate();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case id:
            setId((LId) o);
            break;
        case rowFrom:
            setRowFrom((Date) o);
            break;
        case rowTo:
            setRowTo((Date) o);
            break;
        case offerRate:
            setOfferRate((BigDecimal) o);
            break;
        case customerRate:
            setCustomerRate((BigDecimal) o);
            break;
        }
    }

    public LId getId() {
        return id;
    }

    public void setId(LId id) {
        this.id = id;
    }
}
