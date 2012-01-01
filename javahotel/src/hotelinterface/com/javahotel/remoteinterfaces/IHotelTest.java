/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.remoteinterfaces;

import java.util.Date;
import javax.ejb.Remote;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Remote
public interface IHotelTest {

    int OFFERSEASONSPECIALPRICE = 0;
    int OFFERSERVICEPRICE = 1;
    int CUSTOMERREMARKS = 2;
    int BOOKINGPAYMENTREGISTER = 3;
    int BOOKINTPAYMENTROWS = 4;

    int getNumberOfRecord(SessionT sessionId, int rType, HotelT hotel);

    void setTodayDate(Date d);
}
