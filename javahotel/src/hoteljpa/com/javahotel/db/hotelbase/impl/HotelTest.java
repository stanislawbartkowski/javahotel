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
package com.javahotel.db.hotelbase.impl;

import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.gwtmodel.table.common.dateutil.DateUtil;
import com.javahotel.db.commands.TestGetNumber;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.SessionT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@Stateless(mappedName = "hoteltestEJB")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class HotelTest implements IHotelTest {

    public int getNumberOfRecord(final SessionT sessionId, final int rType,
            final HotelT hotel) {
        TestGetNumber tCommand = new TestGetNumber(sessionId, rType, hotel);
        tCommand.run();
        return tCommand.getRes();
    }

    public void setTodayDate(Date d) {
        DateUtil.setTestToday(d);

    }
}
