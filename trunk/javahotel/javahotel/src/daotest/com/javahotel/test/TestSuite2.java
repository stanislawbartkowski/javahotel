/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javahotel.test;

import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.PasswordT;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite2 extends TestHelper {


    public TestSuite2() {
    }
    
    @After
    public void tearDown() {
        aut.clearAuthBase(se); // important: clear auth base - problem with next test
        tearDownG();
    }
    
    @Test
    public void Test2() throws Exception {
        System.out.println("Add one hotel");
        aut.persistHotel(se, new HotelT("holtel1"), "super hotel", "did");
        Collection<HotelP> res = aut.getHotelList(se);
        assertEquals(1,res.size());
        for (HotelP hp : res) {
            assertEquals("holtel1",hp.getName());
            assertEquals("super hotel",hp.getDescription());
            assertEquals("did",hp.getDatabase());
        }
    }
    
    
    @Test
    public void Test3() throws Exception {
        System.out.println("Add and remove hotels");
        clearAll();
        aut.persistHotel(se, new HotelT("holtel1"), "super hotel", "did");
        Collection<HotelP> res = aut.getHotelList(se);
        assertEquals(1,res.size());
        for (HotelP hp : res) {
            assertEquals("holtel1",hp.getName());
            assertEquals("super hotel",hp.getDescription());
            assertEquals("did",hp.getDatabase());
        }
        aut.persistHotel(se, new HotelT("holtel2"), "super hotel2", "did");
        aut.persistHotel(se, new HotelT("holtel3"), "super hotel3", "did");
        res = aut.getHotelList(se);
        assertEquals(3,res.size());
        aut.removeHotel(se, new HotelT("holtel3"));
        res = aut.getHotelList(se);
        assertEquals(2,res.size());        
        aut.removeHotel(se, new HotelT("holtel2"));
        res = aut.getHotelList(se);
        assertEquals(1,res.size());        
        for (HotelP hp : res) {
            assertEquals("holtel1",hp.getName());
            assertEquals("super hotel",hp.getDescription());
            assertEquals("did",hp.getDatabase());
        }
    }
    
    @Test
    public void Test4() throws Exception {
        System.out.println("Add and remove userd");
        clearAll();
        aut.persistPerson(se, "user1",new PasswordT("password1"));
        Collection<PersonP> res = aut.getPersonList(se);
        assertEquals(1,res.size());
        for (PersonP hp : res) {
            assertEquals("user1",hp.getName());
        }
        aut.persistPerson(se, "user2",new PasswordT("password2"));
        aut.persistPerson(se, "user3",new PasswordT("password3"));
        res = aut.getPersonList(se);
        assertEquals(3,res.size());
        aut.removePerson(se, "user3");
        aut.removePerson(se, "user2");
        res = aut.getPersonList(se);
        assertEquals(1,res.size());
        for (PersonP hp : res) {
            assertEquals("user1",hp.getName());
        }
    }
    
}
