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
package com.gwthotel.admintest.guice;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.jython.serversecurity.cache.OObjectId;

public class GetTestPatterns implements IGetAutomPatterns {

    @Override
    public String getPatt(OObjectId hotel, HotelObjects t) {
        switch (t) {
        case CUSTOMER:
//            return "(Y) / (N) /P";
// important: /C does not work for postgress            
          return "(Y) / (N) /C";
        case RESERVATION:
            return "(Y)/(M) (N)R";
        case BILL:
            return "(Y)/(M) (N) BILL";
        }
        return null;
    }

}
