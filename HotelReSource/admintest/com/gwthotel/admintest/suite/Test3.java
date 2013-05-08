package com.gwthotel.admintest.suite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;

import static org.junit.Assert.*;


public class Test3 extends TestHelper {

    @Test
    public void test1() {
        iAdmin.clearAll();
        assertFalse(iAdmin.validatePasswordForPerson("user", "secret"));
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(pe, roles);
        assertFalse(iAdmin.validatePasswordForPerson("user", "secret"));
        iAdmin.changePasswordForPerson("user", "secret");
        assertTrue(iAdmin.validatePasswordForPerson("user", "secret"));
    }
    
}
