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
package com.gwthotel.hotelejb.clearop;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import com.google.inject.Inject;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = IHotelConsts.HOTELCLEAROPJNDI, beanInterface = IClearHotel.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class HotelClearOpEJB implements IClearHotel {

    private IClearHotel clearOp;

    @Inject
    public void injectHotelServices(IClearHotel injectedServices,
            ISetTestToday iSetToday) {
        clearOp = injectedServices;
    }

    @Override
    public void clearObjects(OObjectId hotel) {
        clearOp.clearObjects(hotel);
    }

    @Override
    public void setTestDataToday(Date d) {
        clearOp.setTestDataToday(d);

    }

    @Override
    public long numberOf(OObjectId hotel, HotelObjects o) {
        return clearOp.numberOf(hotel, o);
    }

}