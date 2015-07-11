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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;

public class Test62 extends TestHelper {

	private static final String KEY1 = "key1";
	private static final String KEY2 = "key2";

	@Test
	public void test1() {
		List<String> sugg = iSugg.getSuggestion(KEY1);
		assertTrue(sugg.isEmpty());
		iSugg.saveSugestion(KEY1, "hello", 10);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(1, sugg.size());
		assertEquals("hello", sugg.get(0));
		iSugg.saveSugestion(KEY1, "hello", 10);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(1, sugg.size());
		assertEquals("hello", sugg.get(0));
		iSugg.saveSugestion(KEY1, "wow", 10);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(2, sugg.size());
		assertEquals("wow", sugg.get(0));
		assertEquals("hello", sugg.get(1));
		iSugg.saveSugestion(KEY1, "s1", 5);
		iSugg.saveSugestion(KEY1, "s2", 5);
		iSugg.saveSugestion(KEY1, "s3", 5);
		iSugg.saveSugestion(KEY1, "s4", 5);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(5, sugg.size());
		assertEquals("s4", sugg.get(0));
		assertEquals("s3", sugg.get(1));

		for (int i = 0; i < 100; i++) {
			iSugg.saveSugestion(KEY1, "next" + i, 5);
			sugg = iSugg.getSuggestion(KEY1);
			assertEquals(5, sugg.size());
		}
		assertEquals("next99", sugg.get(0));
		assertEquals("next98", sugg.get(1));
		assertEquals("next97", sugg.get(2));
		assertEquals("next96", sugg.get(3));
		assertEquals("next95", sugg.get(4));
	}

	@Test
	public void test2() {
		List<String> sugg = iSugg.getSuggestion(KEY1);
		assertTrue(sugg.isEmpty());
		sugg = iSugg.getSuggestion(KEY2);
		assertTrue(sugg.isEmpty());
		for (int i = 0; i < 100; i++) {
			iSugg.saveSugestion(KEY1, "next" + i, 5);
			iSugg.saveSugestion(KEY2, "oooo" + i, 5);
		}
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(5, sugg.size());
		assertEquals("next99", sugg.get(0));
		assertEquals("next98", sugg.get(1));
		assertEquals("next97", sugg.get(2));
		assertEquals("next96", sugg.get(3));
		assertEquals("next95", sugg.get(4));
		sugg = iSugg.getSuggestion(KEY2);
		assertEquals(5, sugg.size());
		assertEquals("oooo99", sugg.get(0));
		assertEquals("oooo98", sugg.get(1));
		assertEquals("oooo97", sugg.get(2));
		assertEquals("oooo96", sugg.get(3));
		assertEquals("oooo95", sugg.get(4));
	}

	@Test
	public void test3() {
		String t = authenticateToken(realmIni, "darkhelmet", "ludicrousspeed");
		assertNotNull(t);
		setToken(t);
		iSugg.clearAll();
		String t1 = authenticateToken(realmIni, "lonestarr", "vespa");
		assertNotNull(t1);
		setToken(t1);
		iSugg.clearAll();

		setToken(t);
		List<String> sugg = iSugg.getSuggestion(KEY1);
		assertTrue(sugg.isEmpty());
		iSugg.saveSugestion(KEY1, "hello", 10);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(1, sugg.size());
		assertEquals("hello", sugg.get(0));

		setToken(t1);
		sugg = iSugg.getSuggestion(KEY1);
		assertTrue(sugg.isEmpty());
		iSugg.saveSugestion(KEY1, "wow", 10);
		sugg = iSugg.getSuggestion(KEY1);
		assertEquals(1, sugg.size());
		assertEquals("wow", sugg.get(0));
	}

	@Test
	public void test4() {
		String val = iSugg.getRemember(KEY1);
		assertNull(val);
		iSugg.saveRemember(KEY1, "Hi");
		assertEquals("Hi", iSugg.getRemember(KEY1));
		assertNull(iSugg.getRemember(KEY2));
		iSugg.removeRemember(KEY1);
		assertNull(iSugg.getRemember(KEY1));
	}

	@Test
	public void test5() {

		FieldItem ite = new FieldItem();
		ite.setAttr(ICommonConsts.TYPE, ICommonConsts.STRINGTYPE);
		FieldValue val = iRem.getRemember(KEY1, ite);
		assertNull(val);
		val = new FieldValue();
		val.setValue("hello");
		iRem.saveRemember(KEY1, val);
		val = iRem.getRemember(KEY1, ite);
		assertNotNull(val);
		assertEquals("hello", val.getValueS());
	}

	@Test
	public void test6() {

		FieldItem ite = new FieldItem();
		ite.setAttr(ICommonConsts.TYPE, ICommonConsts.INTTYPE);
		FieldValue val = iRem.getRemember(KEY1, ite);
		assertNull(val);
		val = new FieldValue();
		val.setValue(56);
		iRem.saveRemember(KEY1, val);
		val = iRem.getRemember(KEY1, ite);
		assertNotNull(val);
		assertEquals(new Integer(56), val.getValueI());
	}

	@Test
	public void test7() {

		FieldItem ite = new FieldItem();
		ite.setAttr(ICommonConsts.TYPE, ICommonConsts.DECIMALTYPE);
		ite.setAttr(ICommonConsts.AFTERDOT, "3");
		FieldValue val = iRem.getRemember(KEY1, ite);
		assertNull(val);
		val = new FieldValue();
		val.setValue(new BigDecimal(1.345), 3);
		iRem.saveRemember(KEY1, val);
		val = iRem.getRemember(KEY1, ite);
		assertNotNull(val);
		equalB(1.345, val.getValueBD(), 3);
	}

	@Test
	public void test8() {

		FieldItem ite = new FieldItem();
		ite.setAttr(ICommonConsts.TYPE, ICommonConsts.BOOLTYPE);
		FieldValue val = iRem.getRemember(KEY1, ite);
		assertNull(val);
		val = new FieldValue();
		val.setValue(true);
		iRem.saveRemember(KEY1, val);
		val = iRem.getRemember(KEY1, ite);
		assertNotNull(val);
		assertTrue(val.getValueB());
	}

	@Test
	public void test9() {

		FieldItem ite = new FieldItem();
		ite.setAttr(ICommonConsts.TYPE, ICommonConsts.DATETYPE);
		FieldValue val = iRem.getRemember(KEY1, ite);
		assertNull(val);
		val = new FieldValue();
		Date da = getD(2010, 10, 5);
		val.setValue(da);
		iRem.saveRemember(KEY1, val);
		val = iRem.getRemember(KEY1, ite);
		assertNotNull(val);
		eqD(2010, 10, 5, val.getValueD());
	}

}
