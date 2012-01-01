/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.command.BillEnumTypes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class Bill extends AbstractPureDictionary {

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(name = "booking", nullable = false)
    private Booking booking;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<AdvancePayment> advancePay;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<Payment> payments;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill")
    private List<AddPayment> addpayments;
    @Basic(optional = false)
    private BillEnumTypes billType;
	@Basic
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

    public List<AdvancePayment> getAdvancePay() {
        return advancePay;
    }

    public void setAdvancePay(List<AdvancePayment> advancePay) {
        this.advancePay = advancePay;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
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
    public List<AddPayment> getAddpayments() {
        return addpayments;
    }

    /**
     * @param addpayments the addpayments to set
     */
    public void setAddpayments(List<AddPayment> addpayments) {
        this.addpayments = addpayments;
    }

	public String getOPrice() {
		return oPrice;
	}

	public void setOPrice(String price) {
		oPrice = price;
	}

}
