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

import org.junit.Test;
import static org.junit.Assert.*;

import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

public class Test63 extends TestHelper {

	private DialogVariables runTest() {
		DialogVariables v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		v.setValueS("glob1", "hello");
		runAction(v, "test110.xml", "accept");
		v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		assertOK(v);
		return v;
	}

	@Test
	public void test1() {
		runTest();
	}

	@Test
	public void test2() {
		DialogVariables v = runTest();
		// now remove
		v.setValueS("glob1", "");
		runAction(v, "test110.xml", "accept");
		v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		assertNotOK(v);
	}

	@Test
	public void test3() {
		DialogVariables v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		v.setValueS("val1", "hello");
		runAction(v, "test110.xml", "accept");
		v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		FieldValue val1 = v.getValue("val1");
		assertNotNull(val1);
		assertEquals("hello", val1.getValueS());
		FieldValue valco = v.getValue("JCOPY_val1");
		assertNotNull(valco);
		assertTrue(valco.getValueB());

		FieldValue val2 = v.getValue("val2");
		assertNotNull(val2);
		assertEquals("hello", val2.getValueS());
		valco = v.getValue("JCOPY_val2");
		assertNotNull(valco);
		assertTrue(valco.getValueB());

	}

	@Test
	public void test4() {
		DialogVariables v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		v.setValueS("rem1", "hello");
		v.setValueS("rem2", "");
		runAction(v, "test110.xml", "accept");
		v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		FieldValue val1 = v.getValue("rem1");
		assertNotNull(val1);
		assertEquals("hello", val1.getValueS());
		FieldValue valco = v.getValue("JCOPY_rem1");
		assertNotNull(valco);
		assertTrue(valco.getValueB());

		FieldValue val2 = v.getValue("rem2");
		assertNotNull(val2);
		assertEquals("hello", val2.getValueS());
		valco = v.getValue("JCOPY_rem2");
		assertNotNull(valco);
		assertTrue(valco.getValueB());
	}

	@Test
	public void test5() {
		DialogVariables v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		FieldValue val = v.getValue("varint");
		assertNull(val);
		v.setValueL("varint", 5);
		runAction(v, "test110.xml", "accept");
		v = new DialogVariables();
		runAction(v, "test110.xml", "before");
		val = v.getValue("varint");
		assertNotNull(val);
		assertEquals(new Integer(5), val.getValueI());
	}

}
