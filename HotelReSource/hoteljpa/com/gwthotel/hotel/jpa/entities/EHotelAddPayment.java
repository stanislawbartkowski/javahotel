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
package com.gwthotel.hotel.jpa.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "findAddPaymentForReservation", query = "SELECT x FROM EHotelAddPayment x WHERE x.reservation = ?1"),
        @NamedQuery(name = "deleteAllAddPaymentForReservation", query = "DELETE FROM EHotelAddPayment x WHERE x.reservation=?1"),
        @NamedQuery(name = "deleteAllAddPaymentForRoom", query = "DELETE FROM EHotelAddPayment x WHERE x.room=?1"),
        @NamedQuery(name = "deleteAllAddPaymentForService", query = "DELETE FROM EHotelAddPayment x WHERE x.service=?1"),
        @NamedQuery(name = "deleteAllAddPaymentForCustomer", query = "DELETE FROM EHotelAddPayment x WHERE x.customer=?1"),
        @NamedQuery(name = "deleteAllAddPayment", query = "DELETE FROM EHotelAddPayment x WHERE x.reservation.hotel = ?1") })
class EHotelAddPayment extends EHotelRoomCustomer {

    @Column(nullable = false)
    private int quantity;
    private String description;
    private String servicevat;
    @Column(nullable = false)
    private BigDecimal price;
    private BigDecimal listPrice;
    @Column(nullable = false)
    private BigDecimal total;
    @JoinColumn(name = "service_id", nullable = true)
    private EHotelServices service;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date servDate;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EHotelServices getService() {
        return service;
    }

    public void setService(EHotelServices service) {
        this.service = service;
    }

    public Date getServDate() {
        return servDate;
    }

    public void setServDate(Date servDate) {
        this.servDate = servDate;
    }

    public String getServicevat() {
        return servicevat;
    }

    public void setServicevat(String servicevat) {
        this.servicevat = servicevat;
    }
    
}
