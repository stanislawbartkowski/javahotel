/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

import com.gwtmodel.table.shared.JythonUIFatal;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.SUtil;

public class Test71 extends TestHelper {
	
	
	@Test
	public void test1() {
		
		DialogFormat d = findDialog("test156.xml");
		assertNotNull(d);
		ButtonItem bu = d.findE(d.getButtonList(),"button");
		assertNotNull(bu);
		FieldItem fi = d.findFieldItem("ajaxid");
		assertTrue(fi.isAjaxField());
		assertEquals("template:hello",fi.getDefValue());
		assertEquals("hello",SUtil.getTemplateId(fi.getDefValue()));
		fi = d.findFieldItem("ratings");
		assertFalse(fi.isAjaxField());
	}
	
	@Test(expected=JythonUIFatal.class)
	public void test2() {
		
		DialogFormat d = findDialog("test161.xml");
	}
	
	@Test
	public void test3() {
		
		DialogFormat d = findDialog("test163.xml");
		assertNotNull(d);
		FieldItem fi = d.findFieldItem("select");
		assertTrue(fi.isSelectorField());
		fi = d.findFieldItem("ratings");
		assertFalse(fi.isSelectorField());
	}


}
