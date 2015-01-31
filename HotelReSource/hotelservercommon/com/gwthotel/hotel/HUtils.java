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
package com.gwthotel.hotel;

import java.math.BigDecimal;

import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.MUtil;
import com.jythonui.server.UtilHelper;

public class HUtils extends UtilHelper {

    private HUtils() {
    }

    public static String[] getCustomerFields() {
        String[] lProperty = { IHotelConsts.CUSTOMERFIRSTNAMEPROP,
                IHotelConsts.CUSTOMERSURNAMEPROP,
                IHotelConsts.CUSTOMERPOSTALCODEPROP,
                IHotelConsts.CUSTOMEREMAILPROP,
                IHotelConsts.CUSTOMERPHONE1PROP,
                IHotelConsts.CUSTOMERDOCNUMBPROP,
                IHotelConsts.CUSTOMERPHONE2PROP, IHotelConsts.CUSTOMERFAXPROP,
                IHotelConsts.CUSTOMERCOUNTRYPROP,
                IHotelConsts.CUSTOMERSTREETPROP, IHotelConsts.CUSTOMERCITYPROP,
                IHotelConsts.CUSTOMERREGIONPROP };
        return lProperty;
    }

    public static BigDecimal roundB(BigDecimal b) {
        return MUtil.roundB(b);
    }
}
