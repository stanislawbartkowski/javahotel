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
package com.gwthotel.hotel.getname;

import javax.inject.Inject;

import com.gwthotel.auth.HotelCustom;
import com.gwthotel.hotel.IHotelGetName;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;

public class GetHotelNameFromToken implements IHotelGetName {

    private final ISecurity iSec;

    @Inject
    public GetHotelNameFromToken(ISecurity iSec) {
        this.iSec = iSec;
    }

    @Override
    public String getHotel(String token) {
        ICustomSecurity sec = iSec.getCustom(token);
        HotelCustom cust = (HotelCustom) sec;
        return cust.getHotelName();
    }

}
