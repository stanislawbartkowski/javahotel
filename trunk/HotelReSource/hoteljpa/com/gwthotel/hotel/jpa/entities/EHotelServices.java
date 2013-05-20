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

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllServices", query = "SELECT x FROM EHotelServices x WHERE x.hotel = ?1"),
    @NamedQuery(name = "deleteAllServices", query = "DELETE FROM EHotelServices x WHERE x.hotel = ?1"),
    @NamedQuery(name = "findOneService", query = "SELECT x FROM EHotelServices x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelServices extends EHotelDict {
    
    private String vat;
    private int noPersons;

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public int getNoPersons() {
        return noPersons;
    }

    public void setNoPersons(int noPersons) {
        this.noPersons = noPersons;
    }        
        
}
