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
package com.javahotel.common.toobject;

import com.javahotel.types.DateP;
import com.javahotel.types.DecimalP;
import com.javahotel.types.INumerable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookRecordP extends AbstractTo implements INumerable {

	private Long id;
	private DateP dataFrom;
	private String oPrice;
	private DecimalP customerPrice;
	private List<BookElemP> booklist;
	private Integer lp;
	private Integer seqId;

	public Integer getSeqId() {
		return seqId;
	}

	public void setSeqId(Integer seqId) {
		this.seqId = seqId;
	}

	public BookRecordP() {
		dataFrom = new DateP();
		customerPrice = new DecimalP();
	}

	public Integer getLp() {
		return lp;
	}

	public void setLp(Integer lp) {
		this.lp = lp;
	}

	public String getOPrice() {
		return oPrice;
	}

	public void setOPrice(final String oPrice) {
		this.oPrice = oPrice;
	}

	public enum F implements IField {

		lp, id, dataFrom, oPrice, customerPrice, seqId,
	};

	@Override
	public Class getT(IField f) {
		F fi = (F) f;
		Class<?> cla = String.class;
		switch (fi) {
		case seqId:
			cla = Integer.class;
			break;
		case id:
			cla = Long.class;
			break;
		case dataFrom:
			cla = Date.class;
			break;
		case lp:
			cla = Integer.class;
			break;
		case customerPrice:
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
	public Object getF(final IField f) {
		F fi = (F) f;
		switch (fi) {
		case seqId:
			return getSeqId();
		case lp:
			return getLp();
		case id:
			return getId();
		case dataFrom:
			return getDataFrom();
		case oPrice:
			return getOPrice();
		case customerPrice:
			return getCustomerPrice();
		}
		return null;

	}

	@Override
	public void setF(final IField f, final Object o) {
		F fi = (F) f;
		switch (fi) {
		case id:
			setId((Long) o);
			break;
		case dataFrom:
			setDataFrom((Date) o);
			break;
		case oPrice:
			setOPrice((String) o);
			break;
		case customerPrice:
			setCustomerPrice((BigDecimal) o);
			break;
		case lp:
			setLp((Integer) o);
			break;
		case seqId:
			setSeqId((Integer) o);
			break;
		default:
			break;
		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	public Date getDataFrom() {
		return dataFrom.getD();
	}

	public void setDataFrom(final Date dataFrom) {
		this.dataFrom.setD(dataFrom);
	}

	public BigDecimal getCustomerPrice() {
		return customerPrice.getDecim();
	}

	public void setCustomerPrice(BigDecimal customerPrice) {
		this.customerPrice.setDecim(customerPrice);
	}

	public List<BookElemP> getBooklist() {
		return booklist;
	}

	public void setBooklist(final List<BookElemP> booklist) {
		this.booklist = booklist;
	}
}
