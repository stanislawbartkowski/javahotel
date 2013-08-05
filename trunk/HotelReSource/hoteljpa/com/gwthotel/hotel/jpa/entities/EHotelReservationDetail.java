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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "findReservation", query = "SELECT x FROM EHotelReservationDetail x WHERE x.reservation.hotel = ?1 AND x.room.name = ?2 AND x.resDate >= ?3 AND x.resDate <= ?4 AND x.reservation.status != com.gwthotel.hotel.reservation.ResStatus.CANCEL ORDER BY x.room.name,x.resDate"),
        @NamedQuery(name = "deleteAllReservationsDetailsForReservation", query = "DELETE FROM EHotelReservationDetail x WHERE x.reservation=?1"),
        @NamedQuery(name = "deleteAllReservationsDetails", query = "DELETE FROM EHotelReservationDetail x WHERE x.reservation.hotel = ?1") })
public class EHotelReservationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "room_id", nullable = true)
    private EHotelRoom room;

    @JoinColumn(name = "service_id", nullable = true)
    private EHotelServices service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private EHotelReservation reservation;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date resDate;

    @Column(nullable = false)
    private int noP;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private BigDecimal priceList;

    public BigDecimal getPriceList() {
        return priceList;
    }

    public void setPriceList(BigDecimal priceList) {
        this.priceList = priceList;
    }

    public EHotelRoom getRoom() {
        return room;
    }

    public void setRoom(EHotelRoom room) {
        this.room = room;
    }

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

    public EHotelReservation getReservation() {
        return reservation;
    }

    public void setReservation(EHotelReservation reservation) {
        this.reservation = reservation;
    }

    public EHotelServices getService() {
        return service;
    }

    public void setService(EHotelServices service) {
        this.service = service;
    }

}
