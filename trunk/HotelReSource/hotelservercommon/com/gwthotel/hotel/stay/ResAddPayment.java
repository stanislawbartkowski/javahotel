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
package com.gwthotel.hotel.stay;

import java.math.BigDecimal;
import java.util.Date;

import com.gwthotel.shared.IHotelConsts;

public class ResAddPayment extends AbstractResHotelGuest {

    private static final long serialVersionUID = 1L;

    private int quantity;
    private Date servDate;
    private BigDecimal price;
    private BigDecimal priceList;
    private BigDecimal priceTotal;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getServDate() {
        return servDate;
    }

    public void setServDate(Date servDate) {
        this.servDate = servDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceList() {
        return priceList;
    }

    public void setPriceList(BigDecimal priceList) {
        this.priceList = priceList;
    }

    public BigDecimal getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(BigDecimal priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void setServiceName(String name) {
        setAttr(IHotelConsts.RESADDSERVICE, name);
    }

    public String getServiceName() {
        return getAttr(IHotelConsts.RESADDSERVICE);
    }

}
