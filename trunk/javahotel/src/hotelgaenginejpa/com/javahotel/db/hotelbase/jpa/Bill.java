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

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;
import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.db.jtypes.HId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField = "customerId", objectField = "customer")
@ObjectCollection(objectCollectionField = "addpayments")
public class Bill implements IPureDictionary {

	// ===============================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	@Basic(optional = false)
	private String name;
	@Basic
	private String description;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	// ====================================

	@Transient
	private Customer customer;
	@Basic(optional = false)
	private Long customerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking", nullable = false)
	private Booking booking;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
	private Collection<AdvancePayment> advancePay;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
	private Collection<Payment> payments;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
	private Collection<AddPayment> addpayments;
	@Basic(optional = false)
	private BillEnumTypes billType;

	@Basic(optional=false)
	private String oPrice;

	public Bill() {
		payments = new ArrayList<Payment>();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Collection<AdvancePayment> getAdvancePay() {
		return advancePay;
	}

	public void setAdvancePay(Collection<AdvancePayment> advancePay) {
		this.advancePay = advancePay;
	}

	public Collection<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Collection<Payment> payments) {
		this.payments = payments;
	}

	public BillEnumTypes getBillType() {
		return billType;
	}

	public void setBillType(BillEnumTypes billType) {
		this.billType = billType;
	}

	/**
	 * @return the addpayments
	 */
	public Collection<AddPayment> getAddpayments() {
		return addpayments;
	}

	/**
	 * @param addpayments
	 *            the addpayments to set
	 */
	public void setAddpayments(Collection<AddPayment> addpayments) {
		this.addpayments = addpayments;
	}

	public String getOPrice() {
		return oPrice;
	}

	public void setOPrice(String price) {
		oPrice = price;
	}

}
