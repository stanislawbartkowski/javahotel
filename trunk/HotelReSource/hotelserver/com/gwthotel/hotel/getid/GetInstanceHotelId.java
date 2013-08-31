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

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.hotel.IGetInstanceHotelId;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

public class GetInstanceHotelId implements IGetInstanceHotelId {

    private final IAppInstanceHotel iApp;
    private final ICommonCache iCache;
    private final IGetLogMess lMess;

    static final private Logger log = Logger.getLogger(GetInstanceHotelId.class
            .getName());

    @Inject
    public GetInstanceHotelId(IAppInstanceHotel iApp,
            ICommonCacheFactory cFactory,
            @Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.iApp = iApp;
        iCache = cFactory.construct(IHotelConsts.CACHEREALMHOTELINSTANCE);
        this.lMess = lMess;
    }

    @Override
    public AppInstanceId getInstance(String instanceName, String userName) {
        Object o = null;
        o = iCache.get(instanceName);
        if (o != null)
            return (AppInstanceId) o;
        AppInstanceId a = iApp.getInstanceId(instanceName, userName);
        if (a.getId() == null) {
            String mess = lMess.getMess(IHError.HERROR010,
                    IHMess.INSTANCEIDCANNOTNENULLHERE);
            log.severe(mess);
            throw new JythonUIFatal(mess);
        }
        iCache.put(instanceName, a);
        return a;
    }

    @Override
    public HotelId getHotel(String instanceName, String hotelName,
            String userName) {
        String key = instanceName + " " + hotelName;
        Object o = null;
        o = iCache.get(key);
        if (o != null)
            return (HotelId) o;
        AppInstanceId a = getInstance(instanceName,userName);
        HotelId h = iApp.getHotelId(a, hotelName, userName);
        iCache.put(key, h);
        return h;
    }

    @Override
    public void invalidateCache() {
        iCache.invalidate();
    }

}
