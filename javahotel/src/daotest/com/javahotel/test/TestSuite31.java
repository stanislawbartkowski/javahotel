/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;

import static org.junit.Assert.*;

public class TestSuite31 extends TestHelper {

    /*
     * SimplePath
     * Test that sequential integer is properly set for one book record
     * Step 1:
     *   Create and persist Booking and one BookRecord with empty seqId
     * Expected result:
     *   BookRecord constains as seq value number 1
     */
    @Test
    public void Test1() {
        loginuser();
        // Step 1:
        logInfo("Testbook record seq id");
        BookingP bok = createB();
        List<BookRecordP> col = new ArrayList<BookRecordP>();
        BookRecordP p = new BookRecordP();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getBookrecords();
        // Expected result
        assertEquals(1, col.size());
        for (BookRecordP br : col) {
            assertNotNull(br.getSeqId());
            assertEquals(new Long(1), new Long(br.getSeqId()));
        }
    }

    /**
     * Test with 2 BookRecord
     * Step1: Create and persists Booking with one BookRecord
     * Step2: Add next (second) BookRecord and persist
     * Expected result:
     * Booking contains two BookRecord with 1 and 2 as sequential id
     */
    @Test
    public void Test2() {
        // Step 1
        Test1();
        // Step 2
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        List<BookRecordP> col = bok.getBookrecords();
        BookRecordP p = new BookRecordP();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(2));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getBookrecords();
        assertEquals(2, col.size());
        // Expected result
        int noc = 0;
        for (BookRecordP br : col) {
            assertNotNull(br.getSeqId());
            Integer lp = br.getLp();
            Integer sid = br.getSeqId();
            if (lp.intValue() == 1) {
                noc++;
                assertEquals(new Integer(1), sid);
            }
            if (lp.intValue() == 2) {
                noc++;
                assertEquals(new Integer(2), sid);
            }
        }
        assertEquals(2, noc);
    }

    /**
     * Step 1: Create Booking with two BookRecords 
     * Step 2: Create another Booking with one BookRecord
     * Expected result:
     * Newly created Booking contains BookRecord with seqid equals 3
     */
    @Test
    public void Test3() {
        // Step 1
        Test2();
        // Step 2
        BookingP bok = createB();
        List<BookRecordP> col = new ArrayList<BookRecordP>();
        BookRecordP p = new BookRecordP();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0002");
        col = bok.getBookrecords();
        // Expected result
        assertEquals(1, col.size());
        for (BookRecordP br : col) {
            assertNotNull(br.getSeqId());
            assertEquals(new Integer(3), br.getSeqId());
        }

    }

    @Test
    public void Test4() {
        Test3();
        CommandParam pa = new CommandParam();
        pa.setReservName("BOK0002");
        pa.setHotel(HOTEL1);

        ReturnPersist ret2 = hotop.hotelOpRet(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret2.getIdName());
        assertNotNull(ret2.getIdName());

        BookingP bok = getOneName(DictType.BookingList, ret2.getIdName());
        List<BookRecordP> col = bok.getBookrecords();
        assertEquals(1, col.size());
        for (BookRecordP br : col) {
            assertNotNull(br.getSeqId());
            assertEquals(new Integer(4), br.getSeqId());
        }

    }
}
