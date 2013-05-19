package com.gwthotel.admintest.suite;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;
import com.gwthotel.auth.HotelCustom;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.CustomSecurity;

public class Test5 extends TestHelper {

    @Test
    public void test1() {
        iAdmin.clearAll();
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(pe, roles);
        iAdmin.changePasswordForPerson("user", "secret");
        Hotel ho = new Hotel();

        ho.setName("hotel");
        ho.setDescription("Pod Pieskiem");
        roles = new ArrayList<HotelRoles>();
        pe = new Person();
        pe.setName("user");
        HotelRoles rol = new HotelRoles(pe);
        rol.getRoles().add("mana");
        rol.getRoles().add("acc");
        roles.add(rol);
        iAdmin.addOrModifHotel(ho, roles);

        String token = iSec.authenticateToken(realM, "user", "wrong", null);
        assertNull(token);
        CustomSecurity cust = new CustomSecurity();
        cust.setAttr(IHotelConsts.HOTELNAME, "hotel");
        ICustomSecurity cu = Holder.getSecurityConvert().construct(cust);
        token = iSec.authenticateToken(realM, "user", "wrong", cu);
        assertNull(token);

        cust = new CustomSecurity();
        cust.setAttr(IHotelConsts.HOTELNAME, "hotel");
        cu = Holder.getSecurityConvert().construct(cust);
        token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);

        System.out.println(token);
        assertTrue(iSec.isAuthorized(token, "sec.u('user')"));
        assertFalse(iSec.isAuthorized(token, "sec.u('stranger')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('mana')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('acc')"));
        assertFalse(iSec.isAuthorized(token, "sec.r('admin')"));

        ICustomSecurity cus = iSec.getCustom(token);
        assertNotNull(cus);
        HotelCustom h = (HotelCustom) cus;
        assertEquals("hotel", h.getHotelName());
    }

}
