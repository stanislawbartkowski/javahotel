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
package com.jython.ui;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.jythonui.server.ISharedConsts;
import com.jythonui.server.journal.JournalRecord;
import com.jythonui.server.security.token.ICustomSecurity;

public class Test64 extends TestHelper {

	@Test
	public void test1() {
		List<JournalRecord> list = iJournal.getList(getH(HOTEL));
		assertTrue(list.isEmpty());
		JournalRecord r = new JournalRecord();
		r.setJournalType("HELLO");
		iJournal.addElem(getH(HOTEL), r);
		list = iJournal.getList(getH(HOTEL));
		assertEquals(1, list.size());
		r = new JournalRecord();
		r.setJournalType("HELLO");
		r.setJournalTypeSpec("SPEC");
		r.setJournalElem1("ELEM1");
		r.setJournalElem2("ELEM2");
		iJournal.addElem(getH(HOTEL), r);
		list = iJournal.getList(getH(HOTEL));
		assertEquals(2, list.size());
		boolean found = false;
		for (JournalRecord e : list) {
			System.out.println(e.getName());
			if ("SPEC".equals(e.getJournalTypeSpec())) {
				assertEquals("ELEM1", e.getJournalElem1());
				assertEquals("ELEM2", e.getJournalElem2());
				found = true;
			}
		}
		assertTrue(found);
	}

	@Test
	public void test2() {
		List<JournalRecord> list;
//		List<JournalRecord> list = iJournal.getList(getH(ISharedConsts.SINGLEOBJECTHOLDER));
//		assertTrue(list.isEmpty());
		ICustomSecurity cu = getPersonSec();
		String t = iSec.authenticateToken(realmIni, "guest", "guest", cu);
		assertNotNull(t);
		list = iJournal.getList(getH(ISharedConsts.SINGLEOBJECTHOLDER));
		assertEquals(1,list.size());
		iSec.logout(t);
		list = iJournal.getList(getH(ISharedConsts.SINGLEOBJECTHOLDER));
		assertEquals(2,list.size());
		for (JournalRecord re : list) {
			System.out.println(re.getJournalType());
			System.out.println(re.getName());
			System.out.println(re.getCreationDate());
			System.out.println(re.getCreationPerson());
		}

	}

}
