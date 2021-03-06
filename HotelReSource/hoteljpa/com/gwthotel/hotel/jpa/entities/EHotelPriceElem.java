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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel",
        "service_id", "pricelist_id" }))
@NamedQueries({
        @NamedQuery(name = "deletePricesForHotel", query = "DELETE FROM EHotelPriceElem x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deletePricesForHotelAndPriceList", query = "DELETE FROM EHotelPriceElem x WHERE x.pricelist=?1"),
        @NamedQuery(name = "deletePricesForHotelAndService", query = "DELETE FROM EHotelPriceElem x WHERE x.service=?1"),
        @NamedQuery(name = "findPricesForPriceList", query = "SELECT x FROM EHotelPriceElem x WHERE x.hotel = ?1 AND x.pricelist.name = ?2") })
public class EHotelPriceElem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hotel;

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private EHotelServices service;

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pricelist_id", nullable = false)
    private EHotelPriceList pricelist;

    
    @Column(nullable = false)
    private BigDecimal price;
    
    private BigDecimal childrenPrice;
    private BigDecimal extrabedsPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotel() {
        return hotel;
    }

    public void setHotel(Long hotel) {
        this.hotel = hotel;
    }

    public EHotelServices getService() {
        return service;
    }

    public void setService(EHotelServices service) {
        this.service = service;
    }

    public EHotelPriceList getPricelist() {
        return pricelist;
    }

    public void setPricelist(EHotelPriceList pricelist) {
        this.pricelist = pricelist;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChildrenPrice() {
        return childrenPrice;
    }

    public void setChildrenPrice(BigDecimal childrenPrice) {
        this.childrenPrice = childrenPrice;
    }

    public BigDecimal getExtrabedsPrice() {
        return extrabedsPrice;
    }

    public void setExtrabedsPrice(BigDecimal extrabedsPrice) {
        this.extrabedsPrice = extrabedsPrice;
    }
    
    

}
