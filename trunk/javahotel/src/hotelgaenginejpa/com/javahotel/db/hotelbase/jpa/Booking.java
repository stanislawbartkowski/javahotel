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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObjects(keyFields = { "hotelId", "customerId" }, objectFields = { "hotel",
        "customer" })
@ObjectCollections(objectCollectionField = { "booklist", "addpayments" })
public class Booking extends AbstractDictionary implements IHotelDictionary {

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date checkIn;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date checkOut;

    @Basic(optional = false)
    private Long customerId;
    @Transient
    private Customer customer;

    @Basic
    private Integer noPersons;

    @Basic(optional = false)
    private String season;

    @Basic(optional = false)
    private String oPrice;

    @Basic(optional = false)
    private BigDecimal customerPrice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookElem> booklist;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<Payment> payments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<AddPayment> addpayments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookingState> state;
    @Basic
    private String resName;
    @Basic(optional = false)
    private BookingEnumTypes bookingType;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date validationDate;
    @Basic
    private BigDecimal validationAmount;

    public Booking() {
        state = new ArrayList<BookingState>();
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(final Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(final Date checkOut) {
        this.checkOut = checkOut;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(Integer noPersons) {
        this.noPersons = noPersons;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<BookingState> getState() {
        return state;
    }

    public void setState(final List<BookingState> state) {
        this.state = state;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public BookingEnumTypes getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingEnumTypes bookingType) {
        this.bookingType = bookingType;
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
     * @return the booklist
     */
    public List<BookElem> getBooklist() {
        return booklist;
    }

    /**
     * @param booklist
     *            the booklist to set
     */
    public void setBooklist(List<BookElem> booklist) {
        this.booklist = booklist;
    }

    /**
     * @return the addpayments
     */
    public List<AddPayment> getAddpayments() {
        return addpayments;
    }

    /**
     * @param addpayments
     *            the addpayments to set
     */
    public void setAddpayments(List<AddPayment> addpayments) {
        this.addpayments = addpayments;
    }

    /**
     * @return the payments
     */
    public List<Payment> getPayments() {
        return payments;
    }

    /**
     * @param payments
     *            the payments to set
     */
    public void setPayments(List<Payment> payments) {
        this.payments = payments;
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
     * @return the oPrice
     */
    public String getoPrice() {
        return oPrice;
    }

    /**
     * @param oPrice
     *            the oPrice to set
     */
    public void setoPrice(String oPrice) {
        this.oPrice = oPrice;
    }

    /**
     * @return the validationDate
     */
    public Date getValidationDate() {
        return validationDate;
    }

    /**
     * @param validationDate
     *            the validationDate to set
     */
    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }

    /**
     * @return the amount
     */
    public BigDecimal getValidationAmount() {
        return validationAmount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setValidationAmount(BigDecimal amount) {
        this.validationAmount = amount;
    }

}
