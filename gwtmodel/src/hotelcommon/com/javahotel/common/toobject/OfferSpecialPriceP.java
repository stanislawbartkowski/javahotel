/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.toobject;

import com.javahotel.types.DecimalP;
import java.math.BigDecimal;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class OfferSpecialPriceP extends AbstractTo {

    private Long id;
    private DecimalP price;
    private Long specialperiod;

    public OfferSpecialPriceP() {
        this.price = new DecimalP();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price.getDecim();
    }

    public void setPrice(BigDecimal price) {
        this.price.setDecim(price);
    }

    public Long getSpecialperiod() {
        return specialperiod;
    }

    public void setSpecialperiod(Long specialperiod) {
        this.specialperiod = specialperiod;
    }

    public enum F implements IField {

        id, price
    }

    @Override
    public Class<?> getT(final IField f) {
        F fi = (F) f;
        Class<?> cla = BigDecimal.class;
        switch (fi) {
        case id:
            cla = Long.class;
            break;
        }
        return cla;
    }

    @Override
    public IField[] getT() {
        return F.values();
    }

    @Override
    public Object getF(IField f) {
        F fi = (F) f;
        switch (fi) {
        case id:
            return getId();
        case price:
            return getPrice();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {
        F fi = (F) f;
        switch (fi) {
        case id:
            setId((Long) o);
            break;
        case price:
            setPrice((BigDecimal) o);
            break;
        }
    }
}
