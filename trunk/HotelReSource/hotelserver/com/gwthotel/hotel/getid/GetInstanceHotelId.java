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
package com.gwthotel.hotel.getid;

import java.io.InvalidClassException;

import javax.inject.Inject;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.hotel.IGetInstanceHotelId;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;

public class GetInstanceHotelId implements IGetInstanceHotelId {

    private final IAppInstanceHotel iApp;
    private final ICommonCache iCache;

    @Inject
    public GetInstanceHotelId(IAppInstanceHotel iApp,
            ICommonCacheFactory cFactory) {
        this.iApp = iApp;
        iCache = cFactory.construct(IHotelConsts.CACHEREALMHOTELINSTANCE);
    }

    @Override
    public AppInstanceId getInstance(String instanceName) {
        Object o = null;
        try {
            o = iCache.get(instanceName);
        } catch (InvalidClassException e) {
            iCache.remove(instanceName);
        }
        if (o != null)
            return (AppInstanceId) o;
        AppInstanceId a = iApp.getInstanceId(instanceName);
        iCache.put(instanceName, a);
        return a;
    }

    @Override
    public HotelId getHotel(String instanceName, String hotelName) {
        String key = instanceName + " " + hotelName;
        Object o = null;
        try {
            o = iCache.get(instanceName);
        } catch (InvalidClassException e) {
            iCache.remove(instanceName);
        }
        if (o != null)
            return (HotelId) o;
        AppInstanceId a = getInstance(instanceName);
        HotelId h = iApp.getHotelId(a, hotelName);
        iCache.put(key, h);
        return h;
    }

}
