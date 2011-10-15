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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author hotel Entity for keeping data of invoice issuer It is derived from
 *         Customer with some additional data
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@KeyObject(keyField = "hotelId", objectField = "hotel")
public class InvoiceIssuer extends Customer {

    @Basic
    private Integer paymentDays; // time (number of day) for payment

    @Basic
    private String townMaking; // place where invoice was made

    @Basic
    private String personMaking; // person making invoice

    /**
     * @return the paymentDays
     */
    public Integer getPaymentDays() {
        return paymentDays;
    }

    /**
     * @param paymentDays
     *            the paymentDays to set
     */
    public void setPaymentDays(Integer paymentDays) {
        this.paymentDays = paymentDays;
    }

    /**
     * @return the townMaking
     */
    public String getTownMaking() {
        return townMaking;
    }

    /**
     * @param townMaking
     *            the townMaking to set
     */
    public void setTownMaking(String townMaking) {
        this.townMaking = townMaking;
    }

    /**
     * @return the personMaking
     */
    public String getPersonMaking() {
        return personMaking;
    }

    /**
     * @param personMaking
     *            the personMaking to set
     */

    public void setPersonMaking(String personMaking) {
        this.personMaking = personMaking;
    }

}
