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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixObject;
import org.migration.fix.impl.ForeignFixTail;
import org.migration.fix.impl.GlobalTableFixPrimary;
import org.migration.fix.impl.ProcedFixNEq;
import org.migration.fix.impl.Replace32767;
import org.migration.fix.impl.SequenceFixMaxValue;
import org.migration.fix.impl.TableFixIndexName;
import org.migration.fix.impl.TableFixPrimaryKey;
import org.migration.fix.impl.TableFixTail;
import org.migration.fix.impl.TableFixTypes;
import org.migration.fix.impl.TableFixUnique;
import org.migration.properties.PropHolder;
import org.migration.tasks.ExtractObjects;

public class Test4 extends TestHelper {

	@Test
	public void test1() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixUnique());
		ExtractObjects.extractObjects(getFileName("rsample1.ora"), getOutputDir());
		verifyFile("table/sche_samplecollection.db2", 18);
		verifyFile("trigger/schema_trigger.db2", 27);
	}

	@Test
	public void test2() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixUnique());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTail());
		ExtractObjects.extractObjects(getFileName("rsample1.ora"), getOutputDir());
		verifyFile("table/sche_samplecollection.db2", 17);
		String line = lastL.get(lastL.size() - 1);
		assertEquals(-1, line.indexOf("USING"));
		verifyFile("trigger/schema_trigger.db2", 27);
	}

	@Test
	public void test3() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixUnique());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTail());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixIndexName());
		PropHolder.getProp().put(PropHolder.TABLESPACE, "userspace32");
		ExtractObjects.extractObjects(getFileName("rsample1.ora"), getOutputDir());
		verifyFile("table/sche_samplecollection.db2", 17);
		boolean found = false;
		boolean foundS = false;
		for (String s : lastL) {
			if (s.contains("IN userspace32"))
				found = true;
			if (s.contains("SCHE.SAMPLECOLLECTION_PK"))
				foundS = true;
		}
		assertTrue(found);
		assertFalse(foundS);
	}

	@Test
	public void test4() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixUnique());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTail());
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixIndexName());
		PropHolder.getProp().put(PropHolder.TABLESPACE, "userspace32");
		ExtractObjects.extractObjects(getFileName("rsample2.ora"), getOutputDir());
	}

	@Test
	public void test5() throws FileNotFoundException, Exception {
		FixObject.clearall();
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixTypes());
		ExtractObjects.extractObjects(getFileName("rsample3.ora"), getOutputDir());
		verifyFile("table/sche_test_export_new.db2", 20);
		boolean found = false;
		for (String s : lastL) {
			if (s.contains(" BLOB (16)")) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void test6() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.TABLE, new TableFixPrimaryKey());
		ExtractObjects.extractObjects(getFileName("rsample4.ora"), getOutputDir());
		verifyFile("table/schema_test_part.db2", 8);
	}

	@Test
	public void test7() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.SEQUENCE, new SequenceFixMaxValue());
		ExtractObjects.extractObjects(getFileName("rsample5.ora"), getOutputDir());
		verifyFile("sequence/schema_superbig.db2", 1);
		boolean found = false;
		for (String s : lastL) {
			if (s.contains("9999999999999999999999999999")) {
				found = true;
				break;
			}
		}
		assertFalse(found);
	}

	@Test
	public void test8() throws FileNotFoundException, Exception {
		PropHolder.getProp().put(PropHolder.PROCTERMINATOR, "@");
		ExtractObjects.extractObjects(getFileName("rsample6.ora"), getOutputDir());
		verifyFile("procedure/procedure2.db2", 15);
		lastL.forEach(System.out::println);
		String line = lastL.get(lastL.size() - 1);
		// check if terminated by @
		System.out.println(line);
		assertTrue(line.startsWith("@"));

	}

	@Test
	public void test9() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixUnique());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixIndexName());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixTail());
		ExtractObjects.extractObjects(getFileName("rsample8.ora"), getOutputDir());
		verifyFile("globaltemp/sche_temp_customer.db2", 15);
		String line = lastL.get(lastL.size() - 1);
		assertEquals(-1, line.indexOf("USING"));

	}

	@Test
	public void test10() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixIndexName());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new TableFixTail());
		FixObject.register(ObjectExtractor.OBJECT.GLOBALTEMP, new GlobalTableFixPrimary());
		ExtractObjects.extractObjects(getFileName("rsample8.ora"), getOutputDir());
		verifyFile("globaltemp/sche_temp_customer.db2", 17);
		String line = lastL.get(lastL.size() - 1);
		System.out.println(line);
		assertEquals(-1, line.indexOf("PRIMARY"));
	}

	@Test
	public void test11() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.PROCEDURE, new ProcedFixNEq());
		ExtractObjects.extractObjects(getFileName("rsample9.ora"), getOutputDir());
		verifyFile("procedure/sche_proc.db2", 21);
		int no = 0;
		for (String l : lastL) {
			if (l.indexOf("<=") != -1)
				no++;
			if (l.indexOf(">=") != -1)
				no++;
		}
		assertEquals(2, no);
	}

	@Test
	public void test12() throws FileNotFoundException, Exception {
		FixObject.clearall();
		FixObject.register(ObjectExtractor.OBJECT.FOREIGNKEY, new ForeignFixTail());
		ExtractObjects.extractObjects(getFileName("rsample10.ora"), getOutputDir());
		verifyFile("foreignkey/schema_test_ddd.db2", 3);

	}

	@Test
	public void test13() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.SEQUENCE, new SequenceFixMaxValue());
		ExtractObjects.extractObjects(getFileName("rsample11.ora"), getOutputDir());
		verifyFile("sequence/pppp_buseq.db2", 2);
	}

	@Test
	public void test14() throws FileNotFoundException, Exception {
		FixObject.register(ObjectExtractor.OBJECT.FUNCTION, new Replace32767());
		ExtractObjects.extractObjects(getFileName("rsample12.ora"), getOutputDir());
		verifyFile("function/proc_text_proc.db2", 28);
		int no = 0;
		for (String l : lastL) {
			System.out.println(l);
			if (l.indexOf("32672") != -1)
				no++;
		}
		assertEquals(3, no);
	}

}
