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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jythonui.server.journal.JournalRecord;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;

public class Test46 extends TestHelper {

	@Test
	public void test1() {
		setTestToday(null);
		String token = scriptTest("dialog46.xml", "test1");
		iSec.logout(token);
		scriptTest("dialog46.xml", "test2");
	}

	@Test
	public void test2() {
		setTestToday(null);
		String token = scriptTest("dialog46.xml", "test1");
		iSec.logout(token);
		DialogVariables v = new DialogVariables();
		scriptTest("dialog46.xml", "test3", v);
		assertOK(v);
		ListOfRows l = v.getList("list");
		assertNotNull(l);
		assertEquals(3, l.getRowList().size());
		for (RowContent r : l.getRowList()) {
			for (int i = 0; i < r.getLength(); i++) {
				FieldValue val = r.getRow(i);
				System.out.println("i=" + i + " = " + val.getValue());
			}
		}

	}

	@Test
	public void test3() {
		setTestToday(null);
		String token = scriptTest("dialog46.xml", "test4");
		iSec.logout(token);
		DialogVariables v = new DialogVariables();
		token = scriptTest("dialog46.xml", "user1", "test4", null, v);
		iSec.logout(token);
		scriptTest("dialog46.xml", "test4", v);
		assertOK(v);
		ListOfRows l = v.getList("list1");
		assertNotNull(l);
		assertEquals(5, l.getRowList().size());
		int user1no = 0;
		for (RowContent r : l.getRowList()) {
			for (int i = 0; i < r.getLength(); i++) {
				FieldValue val = r.getRow(i);
				System.out.println("i=" + i + " = " + val.getValue());
				if (i == 0) {
					String username = val.getValueS();
					System.out.println("user=" + username);
					if ("user1".equals(username))
						user1no++;
				}
			}
		}
		assertEquals(2, user1no);
	}

	private void testD(Date d) {
		System.out.println("dat to check=" + d);
		Date da = new Date();
		long diff = da.getTime() - d.getTime();
		System.out.println("diff sec=" + diff / 1000);
		// less then minute (60 sec)
		if (diff < 0) diff = 0-diff; 
		assertTrue(diff < 60000);
	}

	@Test
	public void test4() {
		setTestToday(null);
		List<JournalRecord> list = iJournal.getList(getH(HOTEL));
		assertTrue(list.isEmpty());
		JournalRecord r = new JournalRecord();
		r.setJournalType("HELLO");
		iJournal.addElem(getH(HOTEL), r);
		list = iJournal.getList(getH(HOTEL));
		assertEquals(1, list.size());
		r = list.get(0);
		testD(r.getCreationDate());
	}

	@Test
	public void test5() {
		setTestToday(null);
		DialogVariables v = new DialogVariables();
		scriptTest("dialog46.xml", "test4", v);
		ListOfRows l = v.getList("list1");
		assertNotNull(l);
		List<RowContent> li = l.getRowList();
		assertEquals(1, li.size());
		RowContent r = li.get(0);
		Date d = r.getRow(3).getValueD();
		System.out.println(d);
		Date jd = r.getRow(4).getValueD();
		System.out.println(jd);
		testD(d);
		testD(jd);
	}

}
