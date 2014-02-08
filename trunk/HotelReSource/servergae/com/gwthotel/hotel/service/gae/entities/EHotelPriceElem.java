/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.service.gae.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class EHotelPriceElem extends EHotelParent {

    private Double childrenPrice;
    private Double extrabedsPrice;
    private Double price;


    @Index
    private String serviceName;
    @Index
    private String pricelistName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPricelistName() {
        return pricelistName;
    }

    public void setPricelistName(String pricelistName) {
        this.pricelistName = pricelistName;
    }

    public Double getChildrenPrice() {
        return childrenPrice;
    }

    public void setChildrenPrice(Double childrenPrice) {
        this.childrenPrice = childrenPrice;
    }

    public Double getExtrabedsPrice() {
        return extrabedsPrice;
    }

    public void setExtrabedsPrice(Double extrabedsPrice) {
        this.extrabedsPrice = extrabedsPrice;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    
    

}
