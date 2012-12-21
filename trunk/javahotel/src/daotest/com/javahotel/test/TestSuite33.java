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
package com.javahotel.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 *
 */
public class TestSuite33 extends TestHelper {
    
    /**
     * Test if operation ChangeBookingToStay works properly if more than two
     * Step 1: Add booking BOK0001 and change to stay
     * Verification: equals to 1/09/2011/S
     * 
     * Step 2: Add booking BOK0002 and change to stay
     * Verification: equals to 2/09/2011/S
     * 
     * Step 3: Add booking BOK0003 and change to stay
     * Verification: equals to 3/09/2011/S
     */
    @Test
    public void Test1() {
        loginuser();
        Date today = DateFormatUtil.toD("2011/09/24");
        DateUtil.setTestToday(today);
        
        /** Step1: first booking */
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        CommandParam pa = new CommandParam();
        logInfo("Testbook record seq id");
        pa.setReservName("BOK0001");
        pa.setHotel(HOTEL1);

        ReturnPersist ret2 = hotop.hotelOp(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret2.getIdName());
        assertEquals("1/09/2011/S",ret2.getIdName());

        /** Step2 : next booking */
        bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0002");
        pa.setReservName("BOK0002");
        pa.setHotel(HOTEL1);

        ret2 = hotop.hotelOp(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret2.getIdName());
        assertEquals("2/09/2011/S",ret2.getIdName());

        /* Step3: next booking */
        bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0003");
        pa.setReservName("BOK0003");
        pa.setHotel(HOTEL1);

        ret2 = hotop.hotelOp(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret2.getIdName());
        assertEquals("3/09/2011/S",ret2.getIdName());
    }
    
    /**
     * Bulk test : run the same but 50 times
     */
    @Test
    public void Test2() {
        loginuser();
        Date today = DateFormatUtil.toD("2011/09/24");
        DateUtil.setTestToday(today);
        
        List<String> rName = new ArrayList<String>();
        for (int i=0; i<50; i++) {
            rName.add("BOOK" + i);
        }
        for (String s : rName) {
            BookingP bok = createB();
            bok = getpersistName(DictType.BookingList, bok, s);
        }
        
        ReturnPersist ret2 = null;
        for (String s: rName) {
            CommandParam pa = new CommandParam();
            logInfo("Testbook record seq id");
            pa.setReservName(s);
            pa.setHotel(HOTEL1);

            ret2 = hotop.hotelOp(se, HotelOpType.ChangeBookingToStay, pa);
            System.out.println(ret2.getIdName());
        }
        assertEquals("50/09/2011/S",ret2.getIdName());        
    }

}
