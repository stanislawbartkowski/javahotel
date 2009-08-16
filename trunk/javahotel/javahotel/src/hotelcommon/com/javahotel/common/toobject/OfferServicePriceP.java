/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import com.javahotel.types.DecimalP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class OfferServicePriceP extends AbstractTo {

    private DecimalP highseasonprice;
    private DecimalP lowseasonprice;
    private DecimalP highseasonweekendprice;
    private DecimalP lowseasonweekendprice;
    private String service;
    private Collection<OfferSpecialPriceP> specialprice;

    public OfferServicePriceP() {
        highseasonprice = new DecimalP();
        lowseasonprice = new DecimalP();
        highseasonweekendprice = new DecimalP();
        lowseasonweekendprice = new DecimalP();
        specialprice = new ArrayList<OfferSpecialPriceP>();
    }

    public String getService() {
        return service;
    }

    public void setService(final String service) {
        this.service = service;
    }

    public Collection<OfferSpecialPriceP> getSpecialprice() {
        return specialprice;
    }

    public void setSpecialprice(final Collection<OfferSpecialPriceP> specialprice) {
        this.specialprice = specialprice;
    }

    public enum F implements IField {

        highseasonprice, lowseasonprice,
        highseasonweekendprice, lowseasonweekendprice,
        service
    }

    @Override
    public Class getT(IField f) {
        F fi = (F) f;
        Class cla = BigDecimal.class;
        switch (fi) {
            case service:
                cla = String.class;
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
            case highseasonprice:
                return getHighseasonprice();
            case lowseasonprice:
                return getLowseasonprice();
            case highseasonweekendprice:
                return getHighseasonweekendprice();
            case lowseasonweekendprice:
                return getLowseasonweekendprice();
            case service:
                return getService();
        }
        return null;
    }

    @Override
    public void setF(IField f, Object o) {

        F fi = (F) f;
        switch (fi) {
            case highseasonprice:
                setHighseasonprice((BigDecimal) o);
                break;
            case lowseasonprice:
                setLowseasonprice((BigDecimal) o);
                break;
            case highseasonweekendprice:
                setHighseasonweekendprice((BigDecimal) o);
                break;
            case lowseasonweekendprice:
                setLowseasonweekendprice((BigDecimal) o);
                break;
            case service:
                setService((String) o);
                break;
        }
    }

    public BigDecimal getHighseasonprice() {
        return highseasonprice.getDecim();
    }

    public void setHighseasonprice(BigDecimal highseasonprice) {
        this.highseasonprice.setDecim(highseasonprice);
    }

    public BigDecimal getLowseasonprice() {
        return lowseasonprice.getDecim();
    }

    public void setLowseasonprice(BigDecimal lowseasonprice) {
        this.lowseasonprice.setDecim(lowseasonprice);
    }

    public BigDecimal getHighseasonweekendprice() {
        return highseasonweekendprice.getDecim();
    }

    public void setHighseasonweekendprice(BigDecimal highseasonweekendprice) {
        this.highseasonweekendprice.setDecim(highseasonweekendprice);
    }

    public BigDecimal getLowseasonweekendprice() {
        return lowseasonweekendprice.getDecim();
    }

    public void setLowseasonweekendprice(BigDecimal lowseasonweekendprice) {
        this.lowseasonweekendprice.setDecim(lowseasonweekendprice);
    }
}
