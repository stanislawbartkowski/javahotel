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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BankAccountP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.common.toobject.RemarkP;
import com.javahotel.remoteinterfaces.HotelT;

/**
 * @author hotel
 * 
 */
public class TestSuite35 extends TestHelper {

    // Basic test for IsserInvoiceList
    // Step 1 : create object
    // Step 2 : set name "Name1", "Name2"
    // Verification: if persisted properly
    @Test
    public void test1() {
        loginuser();
        // Step1
        logInfo("Test id for customer");
        DictionaryP a = getDict(DictType.IssuerInvoiceList, HOTEL1);
        a.setName("C001");
        hot.persistDic(seu, DictType.IssuerInvoiceList, a);
        List<AbstractTo> res = hot.getDicList(se, DictType.IssuerInvoiceList,
                new HotelT(HOTEL1));
        assertEquals(1, res.size());
        List<AbstractTo> res1 = hot.getDicList(se, DictType.CustomerList,
                new HotelT(HOTEL1));
        assertEquals(0, res1.size());

        InvoiceIssuerP ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        assertNotNull(ca);
        assertEquals("C001", ca.getName());

        // Step 2
        ca.setName1("Name1");
        ca.setName2("Name2");
        hot.persistDic(seu, DictType.IssuerInvoiceList, ca);
        ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        // Verification
        assertEquals("Name1", ca.getName1());
        assertEquals("Name2", ca.getName2());
    }
    
    // Test that list of bank accounts attached to InvoiceIssuer is persisted properly
    // Step1 : create C001 issuer
    // Step2 : add one account to issuer
    // Step3 :persist
    // Step4 : read
    // Verification 1: check that number of accounts is 1
    // Step 5: add 50 remarks
    // Verification 2: check if 50 persisted
    @Test
    public void test2() {
        loginuser();
        
        // Step 1
        DictionaryP a = getDict(DictType.IssuerInvoiceList, HOTEL1);
        a.setName("C001");
        hot.persistDic(seu, DictType.IssuerInvoiceList, a);
        CustomerP ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        assertNotNull(ca);
        ca.setName1("Name1");
        
        // Step2
        List<BankAccountP> accounts = new ArrayList<BankAccountP>();
        BankAccountP acc1 = new BankAccountP();
        acc1.setAccountNumber("aaa bbb");
        accounts.add(acc1);
        ca.setAccounts(accounts);
        // Step 3
        hot.persistDic(seu, DictType.IssuerInvoiceList, ca);
        List<DictionaryP> res = getDicList(se, DictType.IssuerInvoiceList, new HotelT(HOTEL1));
        ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        accounts = ca.getAccounts();
        assertEquals(1,accounts.size());
        
        // Step 4
        List<RemarkP> rList = new ArrayList<RemarkP>();
        for (int i=0; i < 50; i++) {
            RemarkP re  = new RemarkP();
            re.setRemark("rema" + i);
            re.setAddDate(DateFormatUtil.toT("2011/10/15 11:30:14"));
            re.setLp(i+1);
            rList.add(re);
        }
        ca.setRemarks(rList);
        hot.persistDic(seu, DictType.IssuerInvoiceList, ca);
        ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        accounts = ca.getAccounts();
        assertEquals(1,accounts.size());
        assertEquals(50,ca.getRemarks().size());
    }

    // Test if InvoiceIssuer properly persists additinal (not customer) data
    // Step 1 : Create one IssuerInvoice
    // Step 2:  Set bank account
    // Verification 1 : Check if bank account data are persisted
    // Step 3:  Set town and person making
    // Verification 2 : Check if town and person are persisted
    @Test
    public void test3() {
        loginuser();
        // Step 1
        DictionaryP a = getDict(DictType.IssuerInvoiceList, HOTEL1);
        a.setName("C001");
        hot.persistDic(seu, DictType.IssuerInvoiceList, a);
        InvoiceIssuerP ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        assertNotNull(ca);
        // Step 2
        ca.setF(DictionaryP.F.description,"descr");
        ca.setF(CustomerP.F.name1, "name1");
        ca.setF(InvoiceIssuerP.F.bankAccount,"99999");
        hot.persistDic(seu, DictType.IssuerInvoiceList, ca);        
        ca = getOneNameN(DictType.IssuerInvoiceList, "C001");
        assertEquals("descr",ca.getF(DictionaryP.F.description));
        assertEquals("name1",ca.getF(CustomerP.F.name1));
        // Verification 1
        assertEquals("99999",ca.getF(InvoiceIssuerP.F.bankAccount));
       
        // Step 3
        ca.setTownMaking("Pacanowo");
        ca.setPersonMaking("Kadafi");
        ca.setCity("CITY");
        hot.persistDic(seu, DictType.IssuerInvoiceList, ca);
        InvoiceIssuerP ca1 = getOneNameN(DictType.IssuerInvoiceList, "C001");
        List<AbstractTo> res = hot.getDicList(se, DictType.IssuerInvoiceList,
                new HotelT(HOTEL1));
        assertEquals(1,res.size());
        // Verification 2
        assertEquals("CITY",ca1.getCity());
        assertEquals("Pacanowo",ca1.getTownMaking());
        assertEquals("Kadafi",ca1.getPersonMaking());
    }
        
    
    @Test
    public void test4() {
        loginuser();
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());
        
        BookingP bok = createB();
        bok.setValidationAmount(new BigDecimal(100));
        bok.setValidationDate(D("2008/03/07"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");

        par = new CommandParam();
        par.setHotel(HOTEL1);
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());
        
        DictionaryP a = getDict(DictType.IssuerInvoiceList, HOTEL1);
        a.setName("C001");
        hot.persistDic(seu, DictType.IssuerInvoiceList, a);
        List<AbstractTo> res1 = hot.getDicList(se, DictType.IssuerInvoiceList,
                new HotelT(HOTEL1));
        assertEquals(1, res1.size());

        
        hot.clearHotelBase(se, new HotelT(HOTEL1));
        hot.clearHotelBase(se, new HotelT(HOTEL2));

        par = new CommandParam();
        par.setHotel(HOTEL1);
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());

        res1 = hot.getDicList(se, DictType.IssuerInvoiceList,
                new HotelT(HOTEL1));
        assertEquals(0, res1.size());
        
    }

}
