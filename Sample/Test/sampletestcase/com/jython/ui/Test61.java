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

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.TypedefDescr;
import com.jythonui.shared.TypesDescr;

public class Test61 extends TestHelper {

	@Test
	public void test1() {
		DialogFormat d = findDialog("test108.xml");
		assertNotNull(d);
		assertNotNull(d.getTypeList());
		assertEquals(1, d.getTypeList().size());
		assertEquals(1, d.getTypeList().get(0).getTypeList().size());
		boolean ok = false;
		for (TypesDescr t : d.getTypeList()) {
			for (TypedefDescr ty : t.getTypeList()) {
				System.out.println(ty.getId());
				if (ty.eqId("names") && ty.isSuggestType())
					ok = true;
			}
		}
		assertTrue(ok);
	}

	@Test
	public void test2() {
		DialogFormat d = findDialog("test109.xml");
		assertNotNull(d);
		FieldItem i = d.findFieldItem("recipient");
		assertNotNull(i);
		assertTrue(i.isSuggest());
		assertEquals(100, i.getSuggestSize());
		i = d.findFieldItem("recipient1");
		assertNotNull(i);
		assertTrue(i.isSuggest());
		assertEquals("keys", i.getSuggestKey());
		assertEquals(6, i.getSuggestSize());
		i = d.findFieldItem("reme");
		assertNotNull(i);
		assertTrue(i.isRemember());
		assertEquals("hello", i.getRememberKey());
	}
}
