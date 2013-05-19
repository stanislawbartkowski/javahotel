package com.gwthotel.admintest.suite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

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
    
    @Test
    public void test2() {
        iAdmin.clearAll();
        DialogFormat d = findDialog("dialog1.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        v.setValueS("name", "hotel1");
        v.setValueS("descr", "Pod pieskiem");
        runAction(v, "dialog1.xml", "modif");
        List<HotelRoles> hList = iAdmin.getListOfRolesForHotel("hotel1");
        assertNotNull(hList);
        assertEquals(1,hList.size());
        for (HotelRoles rol : hList) {
            System.out.println(rol.getObject().getName());
            assertEquals(1,rol.getRoles().size());
            for (String s : rol.getRoles()) {
                System.out.println(s);
                assertEquals("man",s);
            }
        }

    }
    
}
