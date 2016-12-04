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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.shared.JythonUIFatal;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;

public class Test68 extends TestHelper {

	@Test
	public void test1() {
		DialogFormat d = findDialog("test133.xml");
		assertNotNull(d);
		assertFalse(d.isPolymer());
		FieldItem f = d.findFieldItem("menu");
		assertNotNull(f);
		assertFalse(f.isMulti());
		assertTrue(f.isMenu());
		assertEquals(TT.STRING, f.getFieldType());
		assertTrue(f.isMenu());
		FieldItem f1 = d.findFieldItem("menu1");
		assertTrue(f1.isMenu());
		assertTrue(f1.isMulti());
		FieldItem f2 = d.findFieldItem("menu2");
		assertEquals("5", f2.getVisLines());
	}
	
	@Test
	public void test2() {
		DialogFormat d = findDialog("test140.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
		FieldItem f = d.findFieldItem("glob1");
		assertFalse(f.isBinderField());
		f = d.findFieldItem("globbool");
		assertTrue(f.isBinderField());		
	}
	
	@Test
	public void test3() {
		DialogFormat d = findDialog("test141.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
		FieldItem f = d.findFieldItem("ratings");
		assertNotNull(f);
		assertEquals("XX",f.getJsSignalChange());
	}
	
	@Test(expected=JythonUIFatal.class)
	public void test4() {
		DialogFormat d = findDialog("test148.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}

	@Test(expected=JythonUIFatal.class)
	public void test5() {
		DialogFormat d = findDialog("test149.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}
	
	@Test(expected=JythonUIFatal.class)
	public void test6() {
		DialogFormat d = findDialog("test150.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}


}
