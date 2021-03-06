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
package com.gwthotel.hotel.services;

import com.gwthotel.hotel.ServiceType;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.shared.PropDescription;

public class HotelServices extends PropDescription {

    private static final long serialVersionUID = 1L;

    private int noPersons;
    private ServiceType serviceType = ServiceType.HOTEL;
    private boolean perperson;
    private int noExtraBeds;
    private int noChildren;

    public int getNoPersons() {
        return noPersons;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setNoPersons(int noPersons) {
        this.noPersons = noPersons;
    }

    public void setVat(String vat) {
        setAttr(IHotelConsts.VATPROP, vat);
    }

    public String getVat() {
        return getAttr(IHotelConsts.VATPROP);
    }

    public boolean isPerperson() {
        return perperson;
    }

    public void setPerperson(boolean perperson) {
        this.perperson = perperson;
    }

    public int getNoExtraBeds() {
        return noExtraBeds;
    }

    public void setNoExtraBeds(int noExtraBeds) {
        this.noExtraBeds = noExtraBeds;
    }

    public int getNoChildren() {
        return noChildren;
    }

    public void setNoChildren(int noChildren) {
        this.noChildren = noChildren;
    }

}
