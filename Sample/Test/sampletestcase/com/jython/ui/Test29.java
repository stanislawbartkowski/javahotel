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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;

public class Test29 extends TestHelper {

	private void verify(DialogVariables v) {
		assertEquals("Hello", v.getValueS("glob1"));
		assertEquals(123, v.getValue("globint").getValueI().intValue());
		assertTrue(v.getValue("globbool").getValueB());
		assertEquals(new BigDecimal(12.5612).setScale(4, BigDecimal.ROUND_HALF_UP), v.getValue("globdec").getValueBD());
		Date da = v.getValue("globdate").getValueD();
		System.out.println("globdate=" + da);
		assertTrue(eqD(2001, 10, 2, da));
		ListOfRows fo = v.getRowList().get("lista");
		assertNotNull(fo);
		assertEquals(10, fo.getRowList().size());
		int no = 0;
		for (RowContent ro : fo.getRowList()) {
			int i = ro.getRow(0).getValueI();
			String na = (String) ro.getRow(1).getValue();
			System.out.println(i + " " + na);
			assertEquals(no, i);
			no++;
		}
	}

	@Test
	public void test1() throws ParserConfigurationException, FactoryConfigurationError, TransformerException {
		DialogFormat d = findDialog("test60.xml");
		assertNotNull(d);
		DialogVariables v = new DialogVariables();
		runAction(v, "test60.xml", "before");
		String s = iXml.toXML("test60.xml", v);
		System.out.println(s);
		// opposite
		v = new DialogVariables();
		iXml.fromXML("test60.xml", v, s);
		verify(v);
	}

	@Test
	public void test2() {
		DialogFormat d = findDialog("test60.xml");
		assertNotNull(d);
		assertTrue(d.isAsXmlList());
		DialogVariables v = new DialogVariables();
		runAction(v, "test60.xml", "before");
		// if passes test passed
		String s = iXml.toXML("test60.xml", v);
		runAction(v, "test60.xml", "persist");
		v = new DialogVariables();
		v.setValueS("XML", s);
		runAction(v, "test60.xml", "setxml");
		verify(v);
	}

	@Test
	public void test3() {
		DialogFormat d = findDialog("test60.xml");
		assertNotNull(d);
		assertTrue(d.isAsXmlList());
		DialogVariables v = new DialogVariables();
		runAction(v, "test60.xml", "before");

		FieldValue val = v.getValue("globdate");
		assertNotNull(val);
		Date da = val.getValueD();
		System.out.println("java=" + da);
		assertNotNull(da);
		assertEquals(101, da.getYear());
		assertEquals(9, da.getMonth());
		assertEquals(2, da.getDate());

		val = v.getValue("globtime");
		assertNotNull(val);
		assertNotNull(val.getValueD());
		assertEquals(101, val.getValueT().getYear());
		assertEquals(9, val.getValueT().getMonth());
		assertEquals(2, val.getValueT().getDate());
		// if passes test passed
		String s = iXml.toXML("test60.xml", v);
		runAction(v, "test60.xml", "persist");
		v = new DialogVariables();
		v.setValueS("XML", s);
		runAction(v, "test60.xml", "checkxml");
	}
	
	//   if action == "testglobdate" :
	@Test
	public void test4() {
		DialogFormat d = findDialog("test60.xml");
		assertNotNull(d);
		DialogVariables v = new DialogVariables();
		runAction(v, "test60.xml", "testglobdate");
		Date da = v.getValue("globdate").getValueD();
		System.out.println("globdate=" + da);
		assertTrue(eqD(2001, 10, 2, da));
	}

}
