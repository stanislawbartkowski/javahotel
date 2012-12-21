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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class PaymentRow extends AbstractIID {

    
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date rowFrom;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date rowTo;
    @Basic(optional=false)
    private BigDecimal offerPrice;
    @Basic(optional=false)
    private BigDecimal customerPrice;
    @ManyToOne(optional = false)
    @JoinColumn(name = "bookelem", nullable = false)
    private BookElem bookelem;


    public Date getRowFrom() {
        return rowFrom;
    }

    public void setRowFrom(final Date rowFrom) {
        this.rowFrom = rowFrom;
    }

    public Date getRowTo() {
        return rowTo;
    }

    public void setRowTo(final Date rowTo) {
        this.rowTo = rowTo;
    }

    public BigDecimal getOfferPrice() {
        return offerPrice;
    }

    public void setOfferRate(BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }

    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerRate(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    public BookElem getBookelem() {
        return bookelem;
    }

    public void setBookelem(BookElem bookelem) {
        this.bookelem = bookelem;
    }

}
