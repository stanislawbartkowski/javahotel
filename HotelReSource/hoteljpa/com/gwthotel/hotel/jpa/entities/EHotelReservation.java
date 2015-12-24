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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.gwthotel.hotel.reservation.ResStatus;
import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findReservationsForCustomer", query = "SELECT x FROM EHotelReservation x WHERE x.customer = ?1"),
        @NamedQuery(name = "findAllReservations", query = "SELECT x FROM EHotelReservation x WHERE x.hotel = ?1 ORDER BY x.id DESC"),
        @NamedQuery(name = "deleteAllReservations", query = "DELETE FROM EHotelReservation x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneReservation", query = "SELECT x FROM EHotelReservation x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelReservation extends EObjectDict {

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ResStatus status;

    // 2014/04/18
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private EHotelCustomer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date termOfAdvanceDeposit;

    private BigDecimal advanceDeposit;

    private BigDecimal advancePayment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateofadvancePayment;

    public EHotelCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(EHotelCustomer customer) {
        this.customer = customer;
    }

    public ResStatus getStatus() {
        return status;
    }

    public void setStatus(ResStatus status) {
        this.status = status;
    }

    public Date getTermOfAdvanceDeposit() {
        return termOfAdvanceDeposit;
    }

    public void setTermOfAdvanceDeposit(Date termOfAdvanceDeposit) {
        this.termOfAdvanceDeposit = termOfAdvanceDeposit;
    }

    public BigDecimal getAdvanceDeposit() {
        return advanceDeposit;
    }

    public void setAdvanceDeposit(BigDecimal advanceDeposit) {
        this.advanceDeposit = advanceDeposit;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
    }

    public Date getDateofadvancePayment() {
        return dateofadvancePayment;
    }

    public void setDateofadvancePayment(Date dateofadvancePayment) {
        this.dateofadvancePayment = dateofadvancePayment;
    }

}
