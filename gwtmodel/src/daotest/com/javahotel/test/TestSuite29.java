/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.PasswordT;

public class TestSuite29 extends TestHelper {

	// aut.clearAuthBase(se);

	@Before
	public void setUp() throws Exception {
		super.setUp();
		setUpG();
		login();
		aut.clearAuthBase(se);
//		super.setUp();
	}

	@Test
	public void Test1() {
		loginuser();
		System.out.println("Add one more hotel");
		aut.persistHotel(se, new HotelT("hotel24"), "super hotel", "did");
		List<HotelP> res = aut.getHotelList(se);
		assertEquals(3, res.size());
		HotelP te = new HotelP();
		te.setName("hotelsuper");
		ReturnPersist ret = aut.testPersistHotel(se, PersistType.ADD, te);
		assertNull(ret.getErrorMessage());
		te = new HotelP();
		te.setName("hotel1");
		ret = aut.testPersistHotel(se, PersistType.ADD, te);
		System.out.println(ret.getErrorMessage());
		assertNotNull(ret.getErrorMessage());
		assertNotNull(ret.getViewName());
		assertEquals("name",ret.getViewName());
	}

	@Test
	public void Test2() {
		loginuser();
		System.out.println("Add one more person");
		aut.persistPerson(se, "user2", new PasswordT("password2"));
		PersonP pe = new PersonP();
		pe.setName("user1");
		ReturnPersist ret = aut.testPersistPerson(se, PersistType.ADD, pe);
		System.out.println(ret.getErrorMessage());
		assertNotNull(ret.getErrorMessage());
		pe = new PersonP();
		pe.setName("user2");
		ret = aut.testPersistPerson(se, PersistType.ADD, pe);
		System.out.println(ret.getErrorMessage());
		assertNotNull(ret.getErrorMessage());
		pe = new PersonP();
		pe.setName("user3");
		ret = aut.testPersistPerson(se, PersistType.ADD, pe);
		assertNull(ret.getErrorMessage());
	}
	
	private void testD(DictType d) {
        final String hotel1 = "hotel1";
        DictionaryP a = getDict(d, hotel1);
        a.setName("rybka");
        a.setHotel(hotel1);
        hot.persistDic(se, d, a);
        ReturnPersist ret = hot.testDicPersist(se, PersistType.ADD, d, a);
		System.out.println(ret.getErrorMessage());
		assertNotNull(ret.getErrorMessage());
        a.setName("pipka");
        ret = hot.testDicPersist(se, PersistType.ADD, d, a);
		System.out.println(ret.getErrorMessage());
		assertNull(ret.getErrorMessage());
	}
	
	@Test
	public void Test3() {
		loginuser();
		testD(DictType.VatDict);
        testD(DictType.RoomFacility);
	    testD(DictType.RoomStandard);
	    testD(DictType.RoomObjects);
	    testD(DictType.ServiceDict);
	    testD(DictType.OffSeasonDict);
	    testD(DictType.PriceListDict);
	    testD(DictType.CustomerList);
	    testD(DictType.BookingList);
	}

}
