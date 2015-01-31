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
package com.gwthotel.hotel.reservation;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.shared.PropDescription;

@SuppressWarnings("serial")
public abstract class AbstractResHotelGuest extends PropDescription {

    public String getGuestName() {
        return getAttr(IHotelConsts.RESGUESTCUSTID);
    }

    public void setGuestName(String name) {
        setAttr(IHotelConsts.RESGUESTCUSTID, name);
    }

    public String getRoomName() {
        return getAttr(IHotelConsts.RESGUESTROOMID);
    }

    public void setRoomName(String name) {
        setAttr(IHotelConsts.RESGUESTROOMID, name);
    }

}
