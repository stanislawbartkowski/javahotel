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
package com.gwthotel.hotel.reservation;

import java.math.BigDecimal;
import java.util.Date;

import com.gwthotel.hotel.ServiceType;
import com.gwthotel.shared.IHotelConsts;

public class ReservationPaymentDetail extends AbstractResHotelGuest {

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    private Date resDate;

    private int noP;

    private BigDecimal price;

    private BigDecimal priceList;

    private BigDecimal priceTotal;

    private ServiceType serviceType;

    public Date getResDate() {
        return resDate;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public int getNoP() {
        return noP;
    }

    public void setNoP(int noP) {
        this.noP = noP;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setService(String service) {
        setAttr(IHotelConsts.RESDETSERVICENAMEPROP, service);
    }

    public String getService() {
        return getAttr(IHotelConsts.RESDETSERVICENAMEPROP);
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

    public void setVat(String vat) {
        setAttr(IHotelConsts.RESDETVATPROP, vat);
    }

    public String getVat() {
        return getAttr(IHotelConsts.RESDETVATPROP);
    }

    public int getQuantity() {
        return noP;
    }

    public void setQuantity(int quantity) {
        this.noP = quantity;
    }

    public Date getServDate() {
        return resDate;
    }

    public void setServDate(Date servDate) {
        this.resDate = servDate;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

}
