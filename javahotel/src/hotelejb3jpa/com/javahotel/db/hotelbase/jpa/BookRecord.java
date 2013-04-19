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

import com.javahotel.types.INumerable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class BookRecord extends AbstractIID implements INumerable {

	@Basic(optional = false)
	@Temporal(TemporalType.DATE)
	private Date dataFrom;
	@Basic(optional = false)
	private BigDecimal customerPrice;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookrecord")
	private List<BookElem> booklist;
	@ManyToOne(optional = false)
	@JoinColumn(name = "booking", nullable = false)
	private Booking booking;
	@Basic(optional = false)
	private Integer lp;
	@Basic(optional = false)
	private String oPrice;
    @Basic(optional=false)
    private Integer seqId;


	public Integer getSeqId() {
		return seqId;
	}

	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}

	public Date getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(final Date dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getOPrice() {
		return oPrice;
	}

	public void setOPrice(String price) {
		oPrice = price;
	}

	public BigDecimal getCustomerPrice() {
		return customerPrice;
	}

	public void setCustomerRate(BigDecimal customerPrice) {
		this.customerPrice = customerPrice;
	}

	public List<BookElem> getBooklist() {
		return booklist;
	}

	public void setBooklist(final List<BookElem> booklist) {
		this.booklist = booklist;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Integer getLp() {
		return lp;
	}

	public void setLp(Integer lp) {
		this.lp = lp;
	}

	public int getBookLId() {
		Long l = getId().getL();
		return l.intValue();
	}

	// public int getBookLId() {
	// return (int)id.getId();
	// }

}