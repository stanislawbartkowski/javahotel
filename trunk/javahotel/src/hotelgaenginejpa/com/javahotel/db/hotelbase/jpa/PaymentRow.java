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
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;
 
/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class PaymentRow implements IId {

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
	private Long lrowFrom;
	
	@Basic(optional = false)
	private Long lrowTo;
	
	@Basic(optional = false)
	private BigDecimal offerPrice;
	@Basic(optional = false)
	private BigDecimal customerPrice;
    @ManyToOne(optional = false)
    @JoinColumn(name = "bookelem", nullable = false)
	private BookElem bookelem;

	public Date getRowFrom() {
		return new Date(lrowFrom);
	}

	public void setRowFrom(final Date rowFrom) {
		this.lrowFrom = new Long(rowFrom.getTime());
	}

	public Long getLrowFrom() {
		return lrowFrom;
	}

	public void setLrowFrom(Long lrowFrom) {
		this.lrowFrom = lrowFrom;
	}

	public Date getRowTo() {
		return new Date(lrowTo.longValue());
	}

	public void setRowTo(final Date rowTo) {
		this.lrowTo = new Long(rowTo.getTime());
	}

	public Long getLrowTo() {
		return lrowTo;
	}

	public void setLrowTo(Long lrowTo) {
		this.lrowTo = lrowTo;
	}

	public BigDecimal getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(BigDecimal offerPrice) {
		this.offerPrice = offerPrice;
	}

	public BigDecimal getCustomerPrice() {
		return customerPrice;
	}

	public void setCustomerPrice(BigDecimal customerPrice) {
		this.customerPrice = customerPrice;
	}

	public BookElem getBookelem() {
		return bookelem;
	}

	public void setBookelem(BookElem bookelem) {
		this.bookelem = bookelem;
	}

}
