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
package org.migration.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.migration.extractor.ObjectExtractor;

public class Test2 extends TestHelper {

	private void printi(ObjectExtractor.IObjectExtracted i) {
		System.out.println(i.getType() + " " + i.getName() + " on table " + i.onTable());
		System.out.println(i.getType());
		System.out.println("-------------");
		i.getLines().forEach(s -> System.out.println(s));
	}

	private void assertI(ObjectExtractor.IObjectExtracted i, ObjectExtractor.OBJECT oExpected, String nameExpected,
			int nofLines) {
		assertEquals(oExpected, i.getType());
		assertEquals(nameExpected, i.getName());
		assertEquals(nofLines, i.getLines().size());
	}

	@Test
	public void test1() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample1.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.TABLE, "SCHEM.test1", 10);
	}

	@Test
	public void test2() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample2.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.TYPE, "schem.ROWOBJECT", 7);
	}

	@Test
	public void test3() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample3.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.PACKAGE, "SCHEMA.TEMPPROC", 9);
	}

	@Test
	public void test4() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample5.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.BODY, "SCHEMA.TEMPPROC", 14);
	}

	@Test
	public void test5() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample5.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.TABLE, "X", 1);
	}

	@Test
	public void test6() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample6.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.INDEX, "SCHEMA.IX", 6);
		assertEquals("SCHEMA.SAMPLETABLE", i.onTable());
	}

	@Test
	public void test7() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample7.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.UNIQUE, "SCHEMA.INDEX_UQ", 5);
		assertEquals("SCHEMA.SAMPLETABLE", i.onTable());
	}

	@Test
	public void test8() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample8.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.ALTERTABLE, "SCHEMA.TABLE1", 2);
	}

	@Test
	public void test9() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample9.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.PROCEDURE, "SCHEMA.PROCEDUREX", 11);
	}

	@Test
	public void test10() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample10.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.FUNCTION, "SCHEMA.SAMPLE_FUNCTION", 17);
	}

	@Test
	public void test11() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample11.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.TRIGGER, "SCHEMA.TRIGGER", 27);
	}

	@Test
	public void test12() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample12.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.SEQUENCE, "SCHEMA.TEST_SEQ", 2);
	}

	@Test
	public void test13() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample13.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.VIEW, "SCHEMA.MYVIEW", 7);
	}

	@Test
	public void test14() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample14.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.FOREIGNKEY, "SCHEMA.NEXTTABLE", 4);
	}

	@Test
	public void test15() throws URISyntaxException, IOException {
		BufferedReader r = openFile("rsample1.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i;
		int co = 0;
		while ((i = o.extractNext()) != null) {
			assertNotNull(i);
			printi(i);
			switch (co) {
			case 0:
				assertI(i, ObjectExtractor.OBJECT.TABLE, "SCHE.SAMPLECOLLECTION", 16);
				break;
			case 1:
				assertI(i, ObjectExtractor.OBJECT.TRIGGER, "SCHEMA.TRIGGER", 27);
				break;
			case 2:
				assertI(i, ObjectExtractor.OBJECT.UNIQUE, "SCHE.SAMPLECOLLECTION_PK", 5);
				break;
			case 3:
				assertI(i, ObjectExtractor.OBJECT.ALTERTABLE, "SCHE.SAMPLECOLLECTION", 2);
				break;
			default:
				fail();
			}
			co++;
		}
	}
	
	@Test
	public void test16() throws URISyntaxException, IOException {
		BufferedReader r = openFile("rsample7.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.GLOBALTEMP, "SCHEM.CUSTOMER_TMP1", 4);
	}

	@Test
	public void test17() throws URISyntaxException, IOException {
		BufferedReader r = openFile("rsample8.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.GLOBALTEMP, "SCHE.TEMP_CUSTOMER", 8);
		i = o.extractNext();
		assertNotNull(i);
		printi(i);		
		assertI(i, ObjectExtractor.OBJECT.INDEX, "EOASDEV.LISTVALUESTEMP_PK", 5);
		i = o.extractNext();
		assertNotNull(i);
		printi(i);		
	}
	
	@Test
	public void test18() throws URISyntaxException, IOException {
		BufferedReader r = openFile("rsample11.ora");
		ObjectExtractor o = new ObjectExtractor(r);
		ObjectExtractor.IObjectExtracted i = o.extractNext();
		assertNotNull(i);
		printi(i);
		assertI(i, ObjectExtractor.OBJECT.SEQUENCE, "PPPP.BUSEQ", 2);
	}


}
