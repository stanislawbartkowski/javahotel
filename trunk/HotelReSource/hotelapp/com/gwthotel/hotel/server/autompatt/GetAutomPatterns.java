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
package com.gwthotel.hotel.server.autompatt;

import java.util.Properties;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.Util;

public class GetAutomPatterns implements IGetAutomPatterns {

    @Override
    public String getPatt(HotelId hotel, HotelObjects t) {
        Properties prop = Util.getProperties(IHotelConsts.PROPAUTOM);
        String s = t.toString();
        return prop.getProperty(s);
    }

}
