/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@ObjectCollection(objectCollectionField = "guests")
public class BookElem implements IId {
	
// =====================	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Key id;
    
    public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();		
	}
// =====================	


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
	@JoinColumn(name = "bookrecord", nullable = false)
	private BookRecord bookrecord;

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

	public BookRecord getBookrecord() {
		return bookrecord;
	}

	public void setBookrecord(BookRecord bookrecord) {
		this.bookrecord = bookrecord;
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

}
