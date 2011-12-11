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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import com.google.appengine.api.datastore.Text;
import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 * @author hotel
 * 
 */
@Entity
@KeyObjects(keyFields = { "hotelId", "customerId", "bookingId" }, objectFields = {
        "hotel", "customer", "booking" })
public class Invoice extends AbstractDictionary implements IHotelDictionary {

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date dateOp;
    @Basic(optional = false)
    private String personOp;
    @Basic
    private String remarks;

    @Basic(optional = false)
    private Long customerId;

    @Transient
    private Customer customer;

    @Basic(optional = false)
    private Long bookingId;

    @Transient
    private Booking booking;

    @Basic(optional = false)
    private Text invoiceXML;

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     *            the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the bookingId
     */
    public Long getBookingId() {
        return bookingId;
    }

    /**
     * @param bookingId
     *            the bookingId to set
     */
    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * @return the booking
     */
    public Booking getBooking() {
        return booking;
    }

    /**
     * @param booking
     *            the booking to set
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    /**
     * @return the invoiceXML
     */
    public String getInvoiceXML() {
        return invoiceXML.getValue();
    }

    /**
     * @param invoiceXML
     *            the invoiceXML to set
     */
    public void setInvoiceXML(String invoiceXML) {
        this.invoiceXML = new Text(invoiceXML);
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

}
