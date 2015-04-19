/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.mailing.HotelMailElem;

public class Test34 extends TestHelper {

    @Test
    public void test1() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p.setAttr("country", "gb");
        p = iCustomers.addElem(getH(HOTEL), p);
        String custsym = p.getName();
        HotelMailElem ma = new HotelMailElem();
        ma.setmType(HotelMailElem.MailType.RECEIPTSENT);
        ma.setCustomerName(p.getName());
        ma.setName("M01");
        String sym = iHotelMail.addElem(getH(HOTEL), ma).getName();
        System.out.println(sym);
        assertEquals("M01", sym);
        ma = iHotelMail.findElem(getH(HOTEL), sym);
        assertNotNull(ma);
        assertEquals(custsym, ma.getCustomerName());
        assertEquals(HotelMailElem.MailType.RECEIPTSENT, ma.getmType());        
    }
    
    @Test
    public void test2() {
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        CustomerBill b = createP();
        String mName = null;
        for (int i =0; i<10; i++) {
            HotelMailElem ma = new HotelMailElem();
            ma.setmType(HotelMailElem.MailType.RECEIPTSENT);
            ma.setCustomerName(p.getName());
            ma.setName("MM" + i);
            ma.setReseName(b.getReseName());
            String sym = iHotelMail.addElem(getH(HOTEL), ma).getName();
            if (mName == null) mName = sym;
        }
        
        List<HotelMailElem> list = iHotelMail.getList(getH(HOTEL));
        assertEquals(10,list.size());
        iHotelMail.deleteElem(getH(HOTEL), iHotelMail.findElem(getH(HOTEL), mName));
        list = iHotelMail.getList(getH(HOTEL));
        assertEquals(9,list.size());
    }    

}
