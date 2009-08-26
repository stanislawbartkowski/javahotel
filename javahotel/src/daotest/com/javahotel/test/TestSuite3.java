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


import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.PasswordT;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite3 extends TestHelper {

    public TestSuite3() {
    }
    
    @After
    public void tearDown() {
        aut.clearAuthBase(se); // important: clear auth base - problem with next test
        tearDownG();
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Negative test");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        Collection<String> perm = new ArrayList<String>();
        perm.add("czytaj");

        try {
        	// fail expected because there no hotel1
            aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);
            fail();
        } catch (Exception e) {
            // expected
        }
        clearAll();
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        try {
            aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);
            fail();
        } catch (Exception e) {
            // expected
        }

        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);
    // positive
    }
    @Test
    public void Test2() throws Exception {
        System.out.println("Add one role");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        Collection<String> perm = new ArrayList<String>();
        perm.add("read");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);
        Collection<String> res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel1"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("read", s);
        }
    }
    @Test
    public void Test3() throws Exception {
        System.out.println("Add one role");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        aut.persistHotel(se, new HotelT("hotel2"), "super hotel", "did");
        Collection<String> perm = new ArrayList<String>();
        perm.add("read");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel2"), perm);

        Collection<String> res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel1"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("read", s);
        }

        res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel2"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("write", s);
        }
    }
    @Test
    public void Test4() throws Exception {
        System.out.println("Remove role");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        aut.persistHotel(se, new HotelT("hotel2"), "super hotel", "did");
        Collection<String> perm = new ArrayList<String>();
        perm.add("read");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel2"), perm);

        perm = new ArrayList<String>();
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        Collection<String> res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel1"));
        assertEquals(0, res.size());

        res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel2"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("write", s);
        }


    }
   @Test
    public void Test5() throws Exception {
        System.out.println("Change roles");
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT("hotel1"), "super hotel", "did");
        aut.persistHotel(se, new HotelT("hotel2"), "super hotel", "did");
        Collection<String> perm = new ArrayList<String>();
        perm.add("read");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel2"), perm);

        perm = new ArrayList<String>();
        perm.add("write");
        aut.persistPersonHotel(se, "user1", new HotelT("hotel1"), perm);

        Collection<String> res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel1"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("write", s);
        }

        res = aut.getPersonHotelRoles(se, "user1", new HotelT("hotel2"));
        assertEquals(1, res.size());
        for (final String s : res) {
            assertEquals("write", s);
        }


    }
}
