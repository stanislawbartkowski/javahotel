/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gwthotel.hotel.ServiceType;

@Entity
@NamedQueries({

        @NamedQuery(name = "countAllReservationDetails", query = "SELECT COUNT(x) FROM EHotelReservationDetail x,EHotelReservation r WHERE x.reservation = r AND r.hotel = ?1"),
        @NamedQuery(name = "findReservationForReservation", query = "SELECT x FROM EHotelReservationDetail x WHERE x.reservation = ?1 AND x.serviceType = ?2 "),
        @NamedQuery(name = "deleteAllReservationsForReservation", query = "DELETE FROM EHotelReservationDetail x WHERE x.reservation=?1 AND x.serviceType = ?2"),

        @NamedQuery(name = "deleteAllReservationDetailsForReservation", query = "DELETE FROM EHotelReservationDetail x WHERE x.reservation=?1"),
        @NamedQuery(name = "deleteAllReservationDetailsForRoom", query = "DELETE FROM EHotelReservationDetail x WHERE x.room=?1"),
        @NamedQuery(name = "deleteAllReservationDetailsForService", query = "DELETE FROM EHotelReservationDetail x WHERE x.service=?1"),
        @NamedQuery(name = "deleteAllReservationDetailsForCustomer", query = "DELETE FROM EHotelReservationDetail x WHERE x.customer=?1"),
        @NamedQuery(name = "deleteAllReservationDetails", query = "DELETE FROM EHotelReservationDetail x WHERE x.reservation IN (SELECT r FROM EHotelReservation r  WHERE r.hotel= ?1)"),
        @NamedQuery(name = "searchReservation", query = "SELECT r FROM EHotelRoom r WHERE r.hotel = ?1 AND r NOT IN (SELECT x.room FROM EHotelReservationDetail x WHERE x.resDate >= ?2 AND x.resDate <?3 AND x.reservation.status != com.gwthotel.hotel.reservation.ResStatus.CANCEL)"),
        @NamedQuery(name = "searchReservationForService", query = "SELECT DISTINCT r FROM EHotelReservation r,EHotelReservationDetail e WHERE (e.reservation = r) AND (e.service = ?1)"),          
        @NamedQuery(name = "searchReservationForRoom", query = "SELECT DISTINCT r FROM EHotelReservation r,EHotelReservationDetail e WHERE (e.reservation = r) AND (e.room = ?1)"),          
        @NamedQuery(name = "searchReservationForPriceList", query = "SELECT DISTINCT r FROM EHotelReservation r,EHotelReservationDetail e WHERE (e.reservation = r) AND (e.pricelist = ?1)"),          
        @NamedQuery(name = "findReservation", query = "SELECT x FROM EHotelReservationDetail x WHERE x.serviceType = com.gwthotel.hotel.ServiceType.HOTEL AND x.reservation.hotel = ?1 AND x.room.name = ?2 AND x.resDate >= ?3 AND x.resDate <= ?4 AND x.reservation.status != com.gwthotel.hotel.reservation.ResStatus.CANCEL ORDER BY x.room.name,x.resDate") })
public class EHotelReservationDetail extends EHotelRoomCustomer {

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = true)
    private EHotelServices service;

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricelist_id")
    private EHotelPriceList pricelist;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date resDate;

    @Column(nullable = false)
    private int noP;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private BigDecimal priceList;

    @Column(nullable = true)
    private int noChildren;

    @Column(nullable = true)
    private BigDecimal priceChildren;

    @Column(nullable = true)
    private BigDecimal priceListChildren;

    @Column(nullable = true)
    private int noExtraBeds;

    @Column(nullable = true)
    private BigDecimal priceExtraBeds;

    @Column(nullable = true)
    private BigDecimal priceListExtraBeds;

    @Column(nullable = false)
    private BigDecimal total;

    private String description;
    
    private String servicevat;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ServiceType serviceType;

    @Column(nullable = false)
    private boolean perperson;

    public BigDecimal getPriceList() {
        return priceList;
    }

    public void setPriceList(BigDecimal priceList) {
        this.priceList = priceList;
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

    public EHotelServices getService() {
        return service;
    }

    public void setService(EHotelServices service) {
        this.service = service;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServicevat() {
        return servicevat;
    }

    public void setServicevat(String servicevat) {
        this.servicevat = servicevat;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public int getNoChildren() {
        return noChildren;
    }

    public void setNoChildren(int noChildren) {
        this.noChildren = noChildren;
    }

    public BigDecimal getPriceChildren() {
        return priceChildren;
    }

    public void setPriceChildren(BigDecimal priceChildren) {
        this.priceChildren = priceChildren;
    }

    public BigDecimal getPriceListChildren() {
        return priceListChildren;
    }

    public void setPriceListChildren(BigDecimal priceListChildren) {
        this.priceListChildren = priceListChildren;
    }

    public int getNoExtraBeds() {
        return noExtraBeds;
    }

    public void setNoExtraBeds(int noExtraBeds) {
        this.noExtraBeds = noExtraBeds;
    }

    public BigDecimal getPriceExtraBeds() {
        return priceExtraBeds;
    }

    public void setPriceExtraBeds(BigDecimal priceExtraBeds) {
        this.priceExtraBeds = priceExtraBeds;
    }

    public BigDecimal getPriceListExtraBeds() {
        return priceListExtraBeds;
    }

    public void setPriceListExtraBeds(BigDecimal priceListExtraBeds) {
        this.priceListExtraBeds = priceListExtraBeds;
    }

    public boolean isPerperson() {
        return perperson;
    }

    public void setPerperson(boolean perperson) {
        this.perperson = perperson;
    }

    public EHotelPriceList getPriceListName() {
        return pricelist;
    }

    public void setPriceListName(EHotelPriceList pricelist) {
        this.pricelist = pricelist;
    }

}
