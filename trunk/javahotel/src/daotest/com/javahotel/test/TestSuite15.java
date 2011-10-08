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
public class TestSuite15 extends TestHelper {

    private class AddB extends Thread {

        private final String name;
        private final int no;

        AddB(String a, int no) {
            name = a;
            this.no = no;
        }

        @Override
        public void run() {
            BookingP bok;
            System.out.println("Start " + name);
            ReturnPersist ret = null;
            for (int i = 0; i < no; i++) {
                bok = createB();
                bok.setHotel(HOTEL1);
                System.out.println("Persist " + name);
                ret = hot.persistDicReturn(se, DictType.BookingList, bok);
                System.out.println(name + " " + ret.getIdName());
            }

        }
    }

    @Test
    public void Test1() throws InterruptedException {
        System.out.println("Multi thread");
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        AddB t1 = new AddB("T1", 1);
        t1.start();
        AddB t2 = new AddB("T2", 1);
        t2.start();
        t1.join();
        t2.join();
        List<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(2, res.size());
        int no = 0;
        for (DictionaryP d : res) {
            if (d.getName().equals("2/10/2008")) {
                no++;
            }
        }
        assertEquals(1, no);
    }

    @Test
    public void Test2() throws InterruptedException {
        System.out.println("Multi thread");
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        AddB t1 = new AddB("T1", 50);
        t1.start();
        AddB t2 = new AddB("T2", 50);
        t2.start();
        t1.join();
        t2.join();
        List<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(100, res.size());
        DictionaryP de = null;
        int no = 0;
        for (DictionaryP d : res) {
            de = d;
            if ("100/10/2008".equals(de.getName())) {
                no++;
            }
        }
        assertEquals(1, no);
    }

    @Test
    public void Test3() throws InterruptedException, Exception {
        System.out.println("Multi thread");
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        AddB ta[] = new AddB[10];
//        AddB ta[] = new AddB[2];
        for (int i = 0; i < ta.length; i++) {
            AddB t = new AddB("T" + i, 20);
            ta[i] = t;
            t.start();
        }
        for (int i = 0; i < ta.length; i++) {
            ta[i].join();
        }
        List<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(200, res.size());
        DictionaryP de = null;
        boolean isOk = false;
        for (DictionaryP d : res) {
            de = d;
            if (de.getName().equals("200/10/2008")) {
                isOk = true;
            }
        }
        assertTrue(isOk);
    }
}
