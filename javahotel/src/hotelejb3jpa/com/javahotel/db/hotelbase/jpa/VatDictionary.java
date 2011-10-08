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

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Entity;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class VatDictionary extends AbstractDictionary {

    @Basic
    private BigDecimal vatPercent;
    @Basic(optional = false)
    private boolean defVat;

    public VatDictionary() {
        defVat = false;
    }

    public BigDecimal getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(BigDecimal vatPercent) {
        this.vatPercent = vatPercent;
    }

    public boolean isDefVat() {
        return defVat;
    }

    public void setDefVat(boolean defVat) {
        this.defVat = defVat;
    }
}
