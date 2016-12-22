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

import java.io.FileNotFoundException;

import org.junit.Test;
import org.migration.tasks.ExtractObjects;

public class Test3 extends TestHelper {

	@Test
	public void test1() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("sample4.ora"), getOutputDir());
		verifyFile("package/schema_tempproc.db2", 10);
	}

	@Test
	public void test2() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("sample9.ora"), getOutputDir());
		verifyFile("procedure/schema_procedurex.db2", 11);
	}

	@Test
	public void test3() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("sample2.ora"), getOutputDir());
		verifyFile("type/schem_rowobject.db2", 7);
	}

	@Test
	public void test4() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("sample14.ora"), getOutputDir());
		verifyFile("foreignkey/schema_nexttable.db2", 4);
	}

	@Test
	public void test5() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("sample1.ora"), getOutputDir());
		verifyFile("table/schem_test1.db2", 10);
	}

	@Test
	public void test6() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("rsample1.ora"), getOutputDir());
		verifyFile("table/sche_samplecollection.db2", 23);
		verifyFile("trigger/schema_trigger.db2", 27);
	}
	
	@Test
	public void test7() throws FileNotFoundException, Exception {
		ExtractObjects.extractObjects(getFileName("rsample8.ora"), getOutputDir());
		verifyFile("globaltemp/sche_temp_customer.db2", 20);
	}


}
