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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.IdentDocType;
import com.javahotel.common.command.PersonTitle;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.HId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField = "hotelId", objectField = "hotel")
public class Customer implements IHotelDictionary {

	// =============================================
	// Abstract Dictionary
	// =============================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	@Basic
	private Long hotelId;

	@Transient
	private RHotel hotel;

	public RHotel getHotel() {
		return hotel;
	}

	public void setHotel(RHotel hotel) {
		this.hotel = hotel;
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

	// ===================================================

	@Basic
	private String zipCode;
	@Basic
	private String name1;
	@Basic
	private String name2;
	@Basic
	private String city;
	@Basic
	private String stateUS;
	@Basic
	private String country;
	@Basic
	private String address1;
	@Basic
	private String address2;
	@Basic(optional = false)
	private CustomerType cType;
	@Basic
	private String NIP;
	@Basic
	private String PESEL;
	@Basic
	private IdentDocType docType;
	@Basic
	private String docNumber;

	@Basic
	private String firstName;
	@Basic
	private String lastName;
	@Basic(optional = false)
	private PersonTitle pTitle;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<CustomerRemark> remarks;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<CustomerPhoneNumber> phones;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
	private List<CustomerBankAccount> accounts;

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(final String zipCode) {
		this.zipCode = zipCode;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(final String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(final String name2) {
		this.name2 = name2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(final String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(final String address2) {
		this.address2 = address2;
	}

	public CustomerType getCType() {
		return cType;
	}

	public void setCType(CustomerType cType) {
		this.cType = cType;
	}

	public String getNIP() {
		return NIP;
	}

	public void setNIP(final String NIP) {
		this.NIP = NIP;
	}

	public String getPESEL() {
		return PESEL;
	}

	public void setPESEL(final String PESEL) {
		this.PESEL = PESEL;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public String getStateUS() {
		return stateUS;
	}

	public void setStateUS(final String stateUS) {
		this.stateUS = stateUS;
	}

	public List<CustomerRemark> getRemarks() {
		return remarks;
	}

	public void setRemarks(final List<CustomerRemark> remarks) {
		this.remarks = remarks;
	}

	public List<CustomerPhoneNumber> getPhones() {
		return phones;
	}

	public void setPhones(final List<CustomerPhoneNumber> phones) {
		this.phones = phones;
	}

	public List<CustomerBankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(final List<CustomerBankAccount> accounts) {
		this.accounts = accounts;
	}

	public IdentDocType getDocType() {
		return docType;
	}

	public void setDocType(IdentDocType docType) {
		this.docType = docType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public PersonTitle getPTitle() {
		return pTitle;
	}

	public void setPTitle(PersonTitle title) {
		pTitle = title;
	}

}
