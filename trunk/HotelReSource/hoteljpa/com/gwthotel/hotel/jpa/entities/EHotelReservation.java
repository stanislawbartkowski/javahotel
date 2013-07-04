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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.gwthotel.hotel.reservation.ResStatus;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findAllReservations", query = "SELECT x FROM EHotelReservation x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllReservations", query = "DELETE FROM EHotelReservation x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOneReservation", query = "SELECT x FROM EHotelReservation x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelReservation extends EHotelDict {

    @Column(nullable = false)
    private ResStatus status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL )
    private List<EHotelReservationDetail> resDetails;

    @JoinColumn(name = "customer_id", nullable = false)
    private EHotelCustomer customer;

    public List<EHotelReservationDetail> getResDetails() {
        return resDetails;
    }

    public void setResDetails(List<EHotelReservationDetail> resDetails) {
        this.resDetails = resDetails;
    }

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

}