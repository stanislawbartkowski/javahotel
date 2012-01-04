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
import java.util.List;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandUtil;
import com.javahotel.common.util.PUtil;
import com.javahotel.common.util.PUtil.SumP;
import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("serial")
public class BookingP extends DictionaryP {

    private OperationData op;
    private DateP checkIn;
    private DateP checkOut;
    private LId customer;
    private Integer noPersons;
    private String season;
    private String oPrice;

    private List<BookingStateP> state;
    private BookingEnumTypes bookingType;
    private List<BookElemP> booklist;
    /** For stay - symbol of reservation used. */
    private String resName;
    private List<PaymentP> payments;
    private List<AddPaymentP> addpayments;
    private DateP validationDate;
    private DecimalP validationAmount;

    public enum F implements IField {
        checkIn, checkOut, customer, customerPrice, noPersons, season, resName, bookingType, oPrice, validationDate, validationAmount, dateOp
    };

    public BookingEnumTypes getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingEnumTypes bookingType) {
        this.bookingType = bookingType;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    @Override
    protected boolean emptySpecialTrue(final IField f) {
        if (f instanceof BookingP.F) {
            F fie = (F) f;
            switch (fie) {
            case noPersons:
                if (noPersons == null) {
                    break;
                }
                if ((noPersons.intValue() == 0) || (noPersons.intValue() == -1)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    public Class<?> getT(final IField f) {
        Class<?> cla = super.getT(f);
        if (cla != null) {
            return cla;
        }
        F fie = (F) f;
        switch (fie) {
        case checkIn:
        case checkOut:
        case dateOp:
            return Date.class;
        case noPersons:
            return Integer.class;
        case resName:
        case season:
            return String.class;
        case validationDate:
            return Date.class;
        case customerPrice:
        case validationAmount:
            return BigDecimal.class;
        }
        return null;
    }

    @Override
    public IField[] getT() {
        return CommandUtil.addTD(F.values());
    }

    private BigDecimal getCustomerPrice() {
        SumP su = null;
        if (booklist != null) {
            for (BookElemP e : booklist) {
                SumP s = PUtil.getPrice(e.getPaymentrows());
                if (su == null) {
                    su = s;
                } else {
                    PUtil.addS(su, s);
                }
            }
        }
        if (su == null) {
            return null;
        }
        return su.sumCustomer;
    }

    @Override
    public Object getF(IField f) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            return super.getF(pfi);
        }

        F fi = (F) f;
        switch (fi) {
        case dateOp:
            return getDateOp();
        case oPrice:
            return getOPrice();
        case checkIn:
            return getCheckIn();
        case checkOut:
            return getCheckOut();
        case customer:
            return getCustomer();
        case noPersons:
            return getNoPersons();
        case season:
            return getSeason();
        case resName:
            return getResName();
        case bookingType:
            return getBookingType();
        case customerPrice:
            return getCustomerPrice();
        case validationDate:
            return getValidationDate();
        case validationAmount:
            return getValidationAmount();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        DictionaryP.F pfi = CommandUtil.dF(f);
        if (pfi != null) {
            super.setF(pfi, o);
            return;
        }

        F fi = (F) f;
        switch (fi) {
        case oPrice:
            setOPrice((String) o);
            break;
        case checkIn:
            setCheckIn((Date) o);
            break;
        case checkOut:
            setCheckOut((Date) o);
            break;
        case customer:
            setCustomer((LId) o);
            break;
        case noPersons:
            setNoPersons((Integer) o);
            break;
        case season:
            setSeason((String) o);
            break;
        case resName:
            setResName((String) o);
            break;
        case bookingType:
            setBookingType((BookingEnumTypes) o);
            break;
        case validationDate:
            setValidationDate((Date) o);
            break;
        case validationAmount:
            setValidationAmount((BigDecimal) o);
            break;
        case dateOp:
            setDateOp((Date) o);
            break;
        case customerPrice:
            break;
        }
    }

    public BookingP() {
        checkIn = new DateP();
        checkOut = new DateP();
        op = new OperationData();
        validationAmount = new DecimalP();
        validationDate = new DateP();
        // it seems that @Temporal(TemporalType.DATE) is always not-null
        validationDate.setD(new Date());
    }

    public Date getCheckIn() {
        return checkIn.getD();
    }

    public void setCheckIn(final Date checkIn) {
        this.checkIn.setD(checkIn);
    }

    public Date getCheckOut() {
        return checkOut.getD();
    }

    public void setCheckOut(final Date checkOut) {
        this.checkOut.setD(checkOut);
    }

    public LId getCustomer() {
        return customer;
    }

    public void setCustomer(LId customer) {
        this.customer = customer;
    }

    public Integer getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(Integer noPersons) {
        this.noPersons = noPersons;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(final String season) {
        this.season = season;
    }

    public List<BookingStateP> getState() {
        return state;
    }

    public void setState(final List<BookingStateP> state) {
        this.state = state;
    }

    /**
     * @return the oPrice
     */
    public String getOPrice() {
        return oPrice;
    }

    /**
     * @param oPrice
     *            the oPrice to set
     */
    public void setOPrice(String oPrice) {
        this.oPrice = oPrice;
    }

    /**
     * @return the booklist
     */
    public List<BookElemP> getBooklist() {
        return booklist;
    }

    /**
     * @param booklist
     *            the booklist to set
     */
    public void setBooklist(List<BookElemP> booklist) {
        this.booklist = booklist;
    }

    /**
     * @return the payments
     */
    public List<PaymentP> getPayments() {
        return payments;
    }

    /**
     * @param payments
     *            the payments to set
     */
    public void setPayments(List<PaymentP> payments) {
        this.payments = payments;
    }

    /**
     * @return the addpayments
     */
    public List<AddPaymentP> getAddpayments() {
        return addpayments;
    }

    /**
     * @param addpayments
     *            the addpayments to set
     */
    public void setAddpayments(List<AddPaymentP> addpayments) {
        this.addpayments = addpayments;
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
     * @return the validationDate
     */
    public Date getValidationDate() {
        return validationDate.getD();
    }

    /**
     * @param validationDate
     *            the validationDate to set
     */
    public void setValidationDate(Date validationDate) {
        this.validationDate.setD(validationDate);
    }

    /**
     * @return the validationAmount
     */
    public BigDecimal getValidationAmount() {
        return validationAmount.getDecim();
    }

    /**
     * @param validationAmount
     *            the validationAmount to set
     */
    public void setValidationAmount(BigDecimal validationAmount) {
        this.validationAmount.setDecim(validationAmount);
    }

}
