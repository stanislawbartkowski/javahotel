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

import com.javahotel.common.command.BookingEnumTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class Booking extends AbstractDictionary {

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date checkIn;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date checkOut;
    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;
    @Basic
    private Integer noPersons;
    @Basic(optional=false)
    private String season;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookRecord> bookrecords;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<BookingState> state;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<Bill> bill;
    @Basic
    private String resName;
    @Basic(optional=false)
    private BookingEnumTypes bookingType;
    
    
    public Booking() {
        state = new ArrayList<BookingState>();
        bill = new ArrayList<Bill>();
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

    public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public List<BookRecord> getBookrecords() {
        return bookrecords;
    }

    public void setBookrecords(final List<BookRecord> bookrecords) {
        this.bookrecords = bookrecords;
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

    public List<Bill> getBill() {
        return bill;
    }

    public void setBill(List<Bill> bill) {
        this.bill = bill;
    }

}
