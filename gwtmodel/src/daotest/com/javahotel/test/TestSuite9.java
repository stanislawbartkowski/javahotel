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

import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersonTitle;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.PhoneNumberP;
import com.javahotel.common.toobject.RemarkP;

import com.javahotel.remoteinterfaces.HotelT;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite9 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Customer list");
        loginuser();
        CustomerP cust = new CustomerP();
        cust.setHotel(HOTEL1);
        cust.setCType(CustomerType.Company);
        cust.setCountry("PL");
        cust.setName("P003");
        hot.persistDic(seu, DictType.CustomerList, cust);
        List<DictionaryP> res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(res);
        assertEquals(1, res.size());
        cust = null;
        for (DictionaryP o : res) {
            cust = (CustomerP) o;
        }
        assertEquals(HOTEL1, cust.getHotel());
        assertEquals("PL", cust.getCountry());
        assertEquals(CustomerType.Company, cust.getCType());
        assertEquals("P003", cust.getName());
        assertNotNull(cust.getId());

        System.out.println("Now personal data");
        cust.setFirstName("Fist");
        cust.setLastName("last");
        cust.setPTitle(PersonTitle.Mr);
        hot.persistDic(seu, DictType.CustomerList, cust);
        res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(res);
        assertEquals(1, res.size());
        cust = null;
        for (DictionaryP o : res) {
            cust = (CustomerP) o;
        }
        assertEquals("Fist", cust.getFirstName());
        assertEquals("last", cust.getLastName());

        System.out.println("now try remarks");
        List<RemarkP> rCol = new ArrayList<RemarkP>();
        RemarkP r1 = new RemarkP();
        r1.setRemark("rybka");
        r1.setAddDate(DateFormatUtil.toT("2008/10/10"));
        r1.setLp(new Integer(1));
        rCol.add(r1);
        r1 = new RemarkP();
        r1.setRemark("rurka");
        r1.setAddDate(DateFormatUtil.toT("2008/10/11"));
        r1.setLp(new Integer(2));
        rCol.add(r1);
        cust.setRemarks(rCol);
        hot.persistDic(seu, DictType.CustomerList, cust);
        res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(res);
        assertEquals(1, res.size());
        cust = null;
        for (DictionaryP o : res) {
            cust = (CustomerP) o;
        }
        assertNotNull(cust.getRemarks());
        assertEquals(2, cust.getRemarks().size());
        int no = 0;
        for (RemarkP re : cust.getRemarks()) {
            no++;
            String s = DateFormatUtil.toS(re.getAddDate());
            String reM = re.getRemark();
            switch (no) {
                case 1:
                    assertEquals("2008/10/10", s);
                    assertEquals("rybka", reM);
                    break;
                case 2:
                    assertEquals("2008/10/11", s);
                    assertEquals("rurka", reM);
                    break;
            }
        }

        System.out.println("now add new remark");
        rCol = cust.getRemarks();
        r1 = new RemarkP();
        r1.setRemark("rureczka");
        r1.setAddDate(DateFormatUtil.toT("2008/10/14 12:13:14"));
        r1.setLp(new Integer(3));
        rCol.add(r1);
        hot.persistDic(seu, DictType.CustomerList, cust);
        res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(cust.getRemarks());
        assertEquals(3, cust.getRemarks().size());
        no = 0;
        for (RemarkP re : cust.getRemarks()) {
            no++;
            String s = DateFormatUtil.toS(re.getAddDate());
            String ss = DateFormatUtil.toTS(re.getAddDate());
            String reM = re.getRemark();
            switch (no) {
                case 1:
                    assertEquals("2008/10/10", s);
                    assertEquals("rybka", reM);
                    break;
                case 2:
                    assertEquals("2008/10/11", s);
                    assertEquals("rurka", reM);
                    break;
                case 3:
                    assertEquals("2008/10/14 12:13:14", ss);
                    assertEquals("rureczka", reM);
                    break;
            }
        }

    }

    @Test
    public void Test2() {
        System.out.println("Customer list - phone list");
        loginuser();
        CustomerP cust = new CustomerP();
        cust.setHotel(HOTEL1);
        cust.setCType(CustomerType.Company);
        cust.setCountry("PL");
        cust.setName("P003");
        List<PhoneNumberP> col = new ArrayList<PhoneNumberP>();
        PhoneNumberP ph = new PhoneNumberP();
        ph.setPhoneNumber("123345");
        col.add(ph);
        cust.setPhones(col);
        hot.persistDic(seu, DictType.CustomerList, cust);
        List<DictionaryP> res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(res);
        assertEquals(1, res.size());
        cust = null;
        for (DictionaryP o : res) {
            cust = (CustomerP) o;
        }
        assertEquals(1, cust.getPhones().size());
        for (PhoneNumberP po : cust.getPhones()) {
            assertEquals("123345", po.getPhoneNumber());
        }

    }

    @Test
    public void Test3() {
        System.out.println("Customer list - bank accounts");
        loginuser();
        CustomerP cust = new CustomerP();
        cust.setHotel(HOTEL1);
        cust.setCType(CustomerType.Company);
        cust.setCountry("PL");
        cust.setName("P003");
        List<BankAccountP> col = new ArrayList<BankAccountP>();
        BankAccountP ph = new BankAccountP();
        ph.setAccountNumber("123345");
        col.add(ph);
        cust.setAccounts(col);
        hot.persistDic(seu, DictType.CustomerList, cust);
        List<DictionaryP> res = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
        assertNotNull(res);
        assertEquals(1, res.size());
        cust = null;
        for (DictionaryP o : res) {
            cust = (CustomerP) o;
        }
        assertEquals(1, cust.getAccounts().size());
        for (BankAccountP po : cust.getAccounts()) {
            assertEquals("123345", po.getAccountNumber());
        }

    }
}
