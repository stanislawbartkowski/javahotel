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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.remoteinterfaces.HotelT;

public class TestSuite30 extends TestHelper {

    @Test
	public void Test1() {
		loginuser();
		logInfo("Test id for customer");
		DictionaryP a = getDict(DictType.CustomerList, HOTEL1);
		a.setName("C001");
		hot.persistDic(seu, DictType.CustomerList, a);
		CustomerP ca = getOneNameN(DictType.CustomerList, "C001");
		assertNotNull(ca);
		logInfo("id:" + ca.getId());
		assertNotNull(ca.getId());
		assertFalse(ca.getId().isNull());
		Collection<DictionaryP> col = getDicList(se, DictType.CustomerList,
				new HotelT(HOTEL1));
		assertEquals(1, col.size());
		ca.setDescription("Kowalski");
		hot.persistDic(seu, DictType.CustomerList, ca);
		col = getDicList(se, DictType.CustomerList, new HotelT(HOTEL1));
		assertEquals(1, col.size());
		CustomerP ca1 = getOneNameN(DictType.CustomerList, "C001");
		assertNotNull(ca1);
		System.out.println(ca1.getId());
		assertNotNull(ca1.getId());
		assertFalse(ca1.getId().isNull());
		assertEquals("Kowalski", ca1.getDescription());
		assertTrue(ca.getId().equals(ca1.getId()));
	}

	/*
	 * Check that modyfing roomstandard with list of services does not change
	 * the list os ervices at all Test scenario: 1. Add service 2. Add
	 * roomstandard with empty list 3. Modify roomstandard with one service
	 * Expected result There is still on service in the database
	 */

	@Test
	public void Test2() {
		loginuser();
		logInfo("Test standard and service");
		DictionaryP a = getDict(DictType.ServiceDict, HOTEL1);
		a.setName("1p1");
		// Step 1
		hot.persistDicReturn(se, DictType.ServiceDict, a);
		ServiceDictionaryP sp = getOneName(DictType.ServiceDict, "1p1");
		assertNotNull(sp);
		DictionaryP a1 = getDict(DictType.RoomStandard, HOTEL1);
		// Step 2
		RoomStandardP st = getpersistName(DictType.RoomStandard, a1, "STAND");
		assertNotNull(st);
		Collection<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
		li.add(sp);
		st.setServices(li);
		// Step 3
		hot.persistDicReturn(se, DictType.RoomStandard, st);
		st = getOneName(DictType.RoomStandard, "STAND");
		assertNotNull(st);
		assertEquals(1, st.getServices().size());

		// important : should be one
		// Expectedd result
		Collection<AbstractTo> col = hot.getDicList(se, DictType.ServiceDict,
				new HotelT(HOTEL1));
		assertEquals(1, col.size());
	}

	/*
	 * Test vat registry Step 1: read vat (should be 4) Step 2: create and
	 * persist ServiceDict Expected result: vat registry should contain 4
	 * entries
	 */

	@Test
	public void Test3() {
		loginuser();
		// Step 1
		Collection<AbstractTo> col = hot.getDicList(se, DictType.VatDict,
				new HotelT(HOTEL1));
		assertEquals(4, col.size());
		DictionaryP a = getDict(DictType.ServiceDict, HOTEL1);
		a.setName("1p1");
		// Step 2
		hot.persistDicReturn(se, DictType.ServiceDict, a);
		ServiceDictionaryP sp = getOneName(DictType.ServiceDict, "1p1");
		assertNotNull(sp);
		col = hot.getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
		// Expected result
		System.out.println("Expected result - 4 vat");
		assertEquals(4, col.size());
	}

	/*
	 * Test vat registry consistency Step 1: read vat (should be 4) Step 2:
	 * create and persits ServiceDictionart Step 3: create and persist
	 * RoomStandard having this Service Expected result: vat registry should
	 * contain 4 entries (not 5)
	 */

	@Test
	public void Test4() {
		loginuser();

		logInfo("Test vat consistency");
		Collection<AbstractTo> col = hot.getDicList(se, DictType.VatDict,
				new HotelT(HOTEL1));
		// Step 1
		assertEquals(4, col.size());
		logInfo("Test standard and service");
		DictionaryP a = getDict(DictType.ServiceDict, HOTEL1);
		a.setName("1p1");
		// Step 2
		hot.persistDicReturn(se, DictType.ServiceDict, a);

		col = hot.getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
		assertEquals(4, col.size());

		ServiceDictionaryP sp = getOneName(DictType.ServiceDict, "1p1");
		assertNotNull(sp);
		DictionaryP a1 = getDict(DictType.RoomStandard, HOTEL1);
		RoomStandardP st = getpersistName(DictType.RoomStandard, a1, "STAND");
		assertNotNull(st);
		Collection<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
		li.add(sp);
		st.setServices(li);
		// Step 3
		hot.persistDicReturn(se, DictType.RoomStandard, st);

		col = hot.getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
		logInfo("Expected result - 4 vat");
		// Expected result
		assertEquals(4, col.size());
	}
	
	/*
	 * Test ServiceDictionary after  updating RoomStandard
	 * 1. Create and persist two ServiceDictionary
	 * 2. Create and persist RoomStandard having two Service
	 * 3. Modify this RoomSTandard to have only one Service
	 * 4. Persist
	 * Expected result:
	 * There are two Services left (not one)
	 */

	@Test
	public void Test5() {
		loginuser();

		logInfo("Test standard and service");
		
		// Step1 
		DictionaryP d = getDict(DictType.ServiceDict, HOTEL1);
		ServiceDictionaryP a = getpersistName(DictType.ServiceDict, d, "1p1");

		d = getDict(DictType.ServiceDict, HOTEL1);
		ServiceDictionaryP a1 = getpersistName(DictType.ServiceDict, d, "1p2");

		Collection<AbstractTo> col = hot.getDicList(se, DictType.ServiceDict,
				new HotelT(HOTEL1));
		assertEquals(2, col.size());

		DictionaryP r = getDict(DictType.RoomStandard, HOTEL1);
		RoomStandardP st = getpersistName(DictType.RoomStandard, r, "STAND");
		assertNotNull(st);
		Collection<ServiceDictionaryP> li = new ArrayList<ServiceDictionaryP>();
		li.add(a);
		li.add(a1);
		st.setServices(li);
		// Step 2
		hot.persistDicReturn(se, DictType.RoomStandard, st);
		st = getOneName(DictType.RoomStandard, "STAND");
		assertNotNull(st);
		assertEquals(2, st.getServices().size());
		
		col = hot.getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
		assertEquals(2, col.size());

		li = new ArrayList<ServiceDictionaryP>();
		li.add(a);
		// Step 3
		st.setServices(li);
		hot.persistDicReturn(se, DictType.RoomStandard, st);
		// Step 4
		st = getOneName(DictType.RoomStandard, "STAND");
		assertNotNull(st);
		assertEquals(1, st.getServices().size());

		// Expected result
		col = hot.getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
		assertEquals(2, col.size());

	}

}
