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
package com.gwthotel.hotel.mailing;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.shared.PropDescription;

public class HotelMailElem extends PropDescription {

    public enum MailType {
        RESCONFIRMATION, RECEIPTSENT
    }

    private static final long serialVersionUID = 1L;

    private MailType mType;

    public String getCustomerName() {
        return getAttr(IHotelConsts.HOTELMAILCUSTID);
    }

    public void setCustomerName(String custId) {
        setAttr(IHotelConsts.HOTELMAILCUSTID, custId);
    }

    public String getReseName() {
        return getAttr(IHotelConsts.HOTELMAILRESNAME);
    }

    public void setReseName(String reseName) {
        setAttr(IHotelConsts.HOTELMAILRESNAME, reseName);
    }

    public boolean isReseName() {
        return isAttr(IHotelConsts.HOTELMAILRESNAME);
    }

    public MailType getmType() {
        return mType;
    }

    public void setmType(MailType mType) {
        this.mType = mType;
    }

}
