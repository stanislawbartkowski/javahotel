/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField="customerId",objectField="customer")
public class Guest implements IId {

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

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
	@ManyToOne(optional = false)
	@JoinColumn(name = "bookelem", nullable = false)
	private BookElem bookelem;

	@Basic(optional=false)
	private Long customerId;
	
	@Transient
    private Customer customer;
    

    @Temporal(TemporalType.DATE)
    private Date checkIn;

    @Temporal(TemporalType.DATE)
    private Date checkOut;


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BookElem getBookelem() {
        return bookelem;
    }

    public void setBookelem(BookElem bookelem) {
        this.bookelem = bookelem;
    }

    /**
     * @return the checkIn
     */
    public Date getCheckIn() {
        return checkIn;
    }

    /**
     * @param checkIn the checkIn to set
     */
    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * @return the checkOut
     */
    public Date getCheckOut() {
        return checkOut;
    }

    /**
     * @param checkOut the checkOut to set
     */
    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    
}
