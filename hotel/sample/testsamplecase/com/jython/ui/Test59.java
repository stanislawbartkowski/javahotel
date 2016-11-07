/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import static org.junit.Assert.*;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;

public class Test59 extends TestHelper {

	@Test
	public void test1() {
		DialogFormat d = findDialog("test104.xml");
		FieldItem i = d.findFieldItem("glob");
		assertNotNull(i);
		assertNull(i.getCellTitle());
		assertFalse(i.isSuggest());
		i = d.findFieldItem("ctitle");
		assertNotNull(i);
		assertEquals("hello", i.getCellTitle());
	}

	@Test
	public void test2() {
		DialogFormat d = findDialog("test105.xml");
		assertNotNull(d);
		ListFormat f = d.findList("list");
		assertTrue(f.isNoPropertyColumn());
		f = d.findList("list1");
		assertFalse(f.isNoPropertyColumn());
	}

	@Test
	public void test3() {
		DialogFormat d = findDialog("test106.xml");
		assertNotNull(d);
	}

	@Test
	public void test4() {
		DialogFormat d = findDialog("test104.xml");
		FieldItem i = d.findFieldItem("recipient");
		assertNotNull(i);
		assertTrue(i.isSuggest());
		assertEquals(i.getFieldType(), TT.STRING);

	}

}
