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

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.remoteinterfaces.HotelT;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite14 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        hot.persistDic(seu, DictType.BookingList, bok);
        List<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(1, res.size());
        for (DictionaryP p : res) {
            BookingP pp = (BookingP) p;
            String na = pp.getName();
            System.out.println(na);
            assertEquals("1/10/2008", na);
        }
        bok = createB();
        bok.setHotel(HOTEL1);
        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("2/10/2008", ret.getIdName());

        itest.setTodayDate(D("2007/10/08"));
        bok = createB();
        bok.setHotel(HOTEL1);
        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("1/10/2007", ret.getIdName());
        bok = createB();
        bok.setHotel(HOTEL1);
        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("2/10/2007", ret.getIdName());

        itest.setTodayDate(D("2008/10/08"));
        bok = createB();
        bok.setHotel(HOTEL1);
        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("3/10/2008", ret.getIdName());

    }

    @Test
    public void Test2() {
        System.out.println("try with 100 bookings");
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        BookingP bok;
        ReturnPersist ret = null;
        for (int i = 0; i < 100; i++) {
            bok = createB();
            bok.setHotel(HOTEL1);
            ret = hot.persistDic(se, DictType.BookingList, bok);
            System.out.println(ret.getIdName());
        }
        assertEquals("100/10/2008", ret.getIdName());
        List<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(100, res.size());
    }


}
