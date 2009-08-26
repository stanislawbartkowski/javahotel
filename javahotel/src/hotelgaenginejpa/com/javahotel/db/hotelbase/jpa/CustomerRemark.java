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

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;
import com.javahotel.types.INumerable;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class CustomerRemark implements IId,INumerable {

	@Basic(optional = false)
	private String remark;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date addDate;
	@Basic(optional = false)
	private Integer lp;

	public String getRemark() {
		return remark;
	}

	public void setRemark(final String remark) {
		this.remark = remark;
	}


	public Timestamp getAddDate() {
		return new Timestamp(addDate.getTime());
	}

	public void setAddDate(Timestamp addDate) {
		this.addDate = new Date(addDate.getTime());
	}

	public Integer getLp() {
		return lp;
	}

	public void setLp(Integer lp) {
		this.lp = lp;
	}

	// ==========================================================
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();
	}

	// ==========================================================

	@ManyToOne(optional = false)
	@JoinColumn(name = "customer", nullable = false)
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
