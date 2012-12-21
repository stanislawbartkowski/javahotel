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

package com.javahotel.db.hotelbase.jpa;

import com.javahotel.common.toobject.ServiceType;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class ServiceDictionary extends AbstractDictionary {
    
    @Basic(optional = false)
    private ServiceType servType;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "vat_id", nullable = false)
    private VatDictionary vat;
    
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
