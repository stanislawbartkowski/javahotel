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
package com.gwthotel.auth;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.CustomSecurity;

public class SecurityConverter implements ISecurityConvert {

    @Override
    public ICustomSecurity construct(CustomSecurity sou) {
        String hotelName = sou.getAttr(IHotelConsts.HOTELNAME);
        HotelCustom ho = new HotelCustom();
        ho.setHotelName(hotelName);
        String instanceId = sou.getAttr(IHotelConsts.INSTANCEID);
        ho.setInstanceId(instanceId);
        return ho;
    }

}
