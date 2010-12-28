/*
 * Copyright 2009 stanislawbartkowski@gmail.com
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

import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.StringP;

import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.PasswordT;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite4 extends TestHelper {
    @Test
    public void Test1() throws Exception {
        System.out.println("Negative login");
        try {
            se = sec.loginSession(SESSIONU, "admin", new PasswordT("rybka"));
            fail();
        } catch (Exception e) {
        // expected
        }
    }

    public TestSuite4() {
    }
    
    @After
    public void tearDown() {
        aut.clearAuthBase(se); // important: clear auth base - problem with next test
        tearDownG();
    }


 
    @Test
    public void Test2() throws Exception {
        System.out.println("Simple login");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        List<String> perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        try {
            seu = sec.loginSession(SESSIONU, "user1", new PasswordT("rybka"));
            fail();
        } catch (Exception e) {
        // expected
        }
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
        try {
            seu = sec.loginSession(SESSIONU, "user2", new PasswordT("password1"));
            fail();
        } catch (Exception e) {
        // expected
        }
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
        try {
            aut.persistPerson(seu, "user2", new PasswordT("password2"));
            fail();
        } catch (Exception e) {
        // exptected
        }
    }

    @Test
    public void Test3() throws Exception {
        System.out.println("Simple login");
        clearAll();
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        List<HotelT> co = sec.getListHotels(seu);
        assertEquals(0, co.size());
        List<String> perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);
        sec.logoutSession(seu);
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
        co = sec.getListHotels(seu);
        assertEquals(1, co.size());
        for (HotelT ho : co) {
            assertEquals("hotel1", ho.getName());
        }

        List<String> ro = sec.getListRoles(seu, new HotelT("hotel1"));
        assertEquals(1, ro.size());
        for (final String s : ro) {
            assertEquals("write", s);
        }
    }

    @Test
    public void Test4() throws Exception {
        System.out.println("Simple login");
        clearAll();
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
        assertEquals(false,sec.isAdminSession(seu));
        sec.logoutSession(seu);
        assertEquals(false,sec.isValidSession(seu));
        try {
            sec.getListHotels(seu);
            fail();
        } catch (Exception e) {
            // expected
        }
    }
    
    @Test
    public void Test5() throws Exception {
        System.out.println("get database list");
        clearAll();
        List<AbstractTo> d = list.getList(se,RType.DataBases, null);
        for (final AbstractTo a : d) {
            StringP ps = (StringP)a;
            System.out.println(ps.getName());
        }
        assertEquals(1,d.size());
    }

}
