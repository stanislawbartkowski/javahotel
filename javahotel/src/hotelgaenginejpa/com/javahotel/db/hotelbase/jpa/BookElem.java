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
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.javahotel.db.jtypes.IId;
 
/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@ObjectCollection(objectCollectionField = "guests")
public class BookElem extends AbstractIId implements IId {
	
	@Basic(optional = false)
	@Temporal(TemporalType.DATE)
	private Date checkIn;

	@Basic(optional = false)
	@Temporal(TemporalType.DATE)
	private Date checkOut;

	@Basic(optional=false)
	private String resObject;

	public String getResObject() {
		return resObject;
	}

	public void setResObject(String resObject) {
		this.resObject = resObject;
	}


	@Basic(optional=false)
	private String service;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookelem")
	private List<PaymentRow> paymentrows;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookelem")
	private List<Guest> guests;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "booking", nullable = false)
	private Booking booking;

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

    public List<PaymentRow> getPaymentrows() {
		return paymentrows;
	}

    public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @param paymentrows
	 *            the paymentrows to set
	 */
	public void setPaymentrows(List<PaymentRow> paymentrows) {
		this.paymentrows = paymentrows;
	}

	/**
	 * @return the guests
	 */
	public List<Guest> getGuests() {
		return guests;
	}

	/**
	 * @param guests
	 *            the guests to set
	 */
	public void setGuests(List<Guest> guests) {
		this.guests = guests;
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
