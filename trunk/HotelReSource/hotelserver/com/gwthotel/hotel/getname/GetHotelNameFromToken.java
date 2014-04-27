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
package com.gwthotel.hotel.getname;

import javax.inject.Inject;

import com.gwthotel.auth.HotelCustom;
import com.gwthotel.hotel.IHotelGetName;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.OObjectId;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;

public class GetHotelNameFromToken implements IHotelGetName {

    private final ISecurity iSec;
    private final IGetInstanceOObjectIdCache iGet;

    @Inject
    public GetHotelNameFromToken(ISecurity iSec, IGetInstanceOObjectIdCache iGet) {
        this.iSec = iSec;
        this.iGet = iGet;
    }

    @Override
    public OObjectId getHotel(String token) {
        ICustomSecurity sec = iSec.getCustom(token);
        HotelCustom cust = (HotelCustom) sec;
        String instanceId = cust.getInstanceId();
        String hotelName = cust.getHotelName();
        return iGet.getOObject(instanceId, hotelName, iSec.getUserName(token));
    }

    @Override
    public AppInstanceId getInstance(String token) {
        ICustomSecurity sec = iSec.getCustom(token);
        HotelCustom cust = (HotelCustom) sec;
        String instanceId = cust.getInstanceId();
        return iGet.getInstance(instanceId, iSec.getUserName(token));
    }

}
