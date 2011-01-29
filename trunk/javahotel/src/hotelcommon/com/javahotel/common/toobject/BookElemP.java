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
package com.javahotel.common.toobject;

import java.util.Date;
import java.util.List;

import com.javahotel.types.DateP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class BookElemP extends AbstractToILd {

	private LId id;
	private DateP checkIn;
	private DateP checkOut;
	private String resObject;
	private String service;
	private List<PaymentRowP> paymentrows;
	private List<GuestP> guests;

	public BookElemP() {
		this.checkIn = new DateP();
		this.checkOut = new DateP();
	}

	public String getResObject() {
		return resObject;
	}

	public void setResObject(final String resObject) {
		this.resObject = resObject;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the guests
	 */
	public List<GuestP> getGuests() {
		return guests;
	}

	/**
	 * @param guests
	 *            the guests to set
	 */
	public void setGuests(List<GuestP> guests) {
		this.guests = guests;
	}

	public enum F implements IField {

		id, checkIn, checkOut, resObject, service, paymentrows
		// paymentrows - necessary
	};

	@Override
	public Class getT(IField f) {
		F fi = (F) f;
		Class cla = String.class;
		switch (fi) {
		case id:
			cla = Long.class;
			break;
		case checkIn:
		case checkOut:
			cla = Date.class;
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
		case checkIn:
			return getCheckIn();
		case checkOut:
			return getCheckOut();
		case resObject:
			return getResObject();
		case service:
			return getService();
		case paymentrows:
			return getPaymentrows();
		}
		return null;
	}

	@Override
	public void setF(IField f, Object o) {
		F fi = (F) f;
		switch (fi) {
		case id:
			setId((LId) id);
			break;
		case checkIn:
			setCheckIn((Date) o);
			break;
		case checkOut:
			setCheckOut((Date) o);
			break;
		case resObject:
			setResObject((String) o);
			break;
		case service:
			setService((String) o);
			break;
		case paymentrows:
			setPaymentrows((List<PaymentRowP>) o);
			break;
		}
	}

	public LId getId() {
		return id;
	}

	public void setId(LId id) {
		this.id = id;
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

	public List<PaymentRowP> getPaymentrows() {
		return paymentrows;
	}

	public void setPaymentrows(final List<PaymentRowP> paymentrows) {
		this.paymentrows = paymentrows;
	}
}
