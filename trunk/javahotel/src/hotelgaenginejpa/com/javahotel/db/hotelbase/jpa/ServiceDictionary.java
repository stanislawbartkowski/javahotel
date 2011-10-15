/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.javahotel.common.command.ServiceType;
import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObjects(keyFields = { "hotelId", "vatKey" }, objectFields = { "hotel",
        "vat" })
public class ServiceDictionary extends AbstractDictionary implements
        IHotelDictionary {

    @Basic
    private Integer noPerson;

    @Basic(optional = false)
    private ServiceType servType;

    @Basic
    private Long vatKey;

    @Transient
    private VatDictionary vat;

    public Long getVatKey() {
        return vatKey;
    }

    public void setVatKey(Long vatKey) {
        this.vatKey = vatKey;
    }

    /**
     * @return the noPerson
     */
    public Integer getNoPerson() {
        return noPerson;
    }

    /**
     * @param noPerson
     *            the noPerson to set
     */
    public void setNoPerson(Integer noPerson) {
        this.noPerson = noPerson;
    }

    public ServiceDictionary() {
        super();
        this.servType = ServiceType.NOCLEG;
    }

    public ServiceType getServType() {
        return servType;
    }

    public void setServType(ServiceType servType) {
        this.servType = servType;
    }

    public VatDictionary getVat() {
        return vat;
    }

    public void setVat(VatDictionary vat) {
        this.vat = vat;
    }

}
