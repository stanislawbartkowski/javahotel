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
package com.gwthotel.hotel.service.gae.entities;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.gwthotel.hotel.ServiceType;

@Entity
public class EResDetails extends EHotelRoomGuest {

    private Ref<EHotelReservation> reservation;

    @Index
    private String resName;

    @Index
    private Date resDate;

    private int noP;

    private Double price;

    private Double priceList;

    private Double priceTotal;

    private ServiceType serviceType;

    private Ref<EHotelServices> service;

    private String description;

    private String vatTax;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceList() {
        return priceList;
    }

    public void setPriceList(Double priceList) {
        this.priceList = priceList;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public EHotelReservation getReservation() {
        return reservation.get();
    }

    public void setReservation(EHotelReservation reservation) {
        if (reservation == null)
            return;
        this.resName = reservation.getName();
        this.reservation = Ref.create(reservation);
    }

    public void setService(EHotelServices service) {
        if (service == null)
            this.service = null;
        this.service = Ref.create(service);
    }

    public EHotelServices getService() {
        if (service == null)
            return null;
        return service.get();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVatTax() {
        return vatTax;
    }

    public void setVatTax(String vatTax) {
        this.vatTax = vatTax;
    }

}
