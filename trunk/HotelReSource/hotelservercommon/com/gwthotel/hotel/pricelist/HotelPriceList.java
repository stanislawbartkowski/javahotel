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
package com.gwthotel.hotel.pricelist;

import java.util.Date;

import com.jythonui.shared.PropDescription;

public class HotelPriceList extends PropDescription {

    private static final long serialVersionUID = 1L;
    
    private Date dateFrom;
    private Date dateTo;

    public void setFromDate(Date d) {
        dateFrom = d;
    }

    public void setToDate(Date d) {
        dateTo = d;
    }

    public Date getFromDate() {
        return dateFrom;
    }

    public Date getToDate() {
        return dateTo;
    }

}
