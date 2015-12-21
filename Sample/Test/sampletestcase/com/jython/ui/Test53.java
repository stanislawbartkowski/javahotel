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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jythonui.server.UObjects;
import com.jythonui.server.mail.Note;
import com.jythonui.server.mail.NoteAttach;

public class Test53 extends TestHelper {

	// @Before
	// public void setUp() {
	// super.setUp();
	// createObjects();
	// }

	@Test
	public void test1() {
		Note no = new Note();
		no.setDescription("My note");
		no.setRecipient("I do not like you");
		no = iNoteStorage.addElem(getH(HOTEL1), no);
		System.out.println(no.getName());
		System.out.println(no.getId());
		assertNotNull(no.getId());
		assertNotNull(no.getName());
		Long id = no.getId();
		no = iNoteStorage.findElemById(getH(HOTEL1), id);
		assertNotNull(no);
		assertEquals("I do not like you", no.getRecipient());
		List<Note> nList = iNoteStorage.getList(getH(HOTEL1));
		assertEquals(1, nList.size());
	}

	@Test
	public void test2() {
		Note no = new Note();
		no.setDescription("My second note");
		no.setRecipient("I do not like you");
		String s = "This is an attachment !";
		byte[] val = UObjects.put(s, "!!!");
		String realm = "Hello";
		String key = iAddBlob.addNewBlob(realm, "Wow", val);
		NoteAttach a = new NoteAttach();
		a.setRealm(realm);
		a.setBlobKey(key);
		no.getaList().add(a);
		no = iNoteStorage.addElem(getH(HOTEL1), no);
		assertNotNull(no);
		Long id = no.getId();
		no = iNoteStorage.findElemById(getH(HOTEL1), id);
		assertEquals(1, no.getaList().size());
		for (NoteAttach aa : no.getaList()) {
			String rea = aa.getRealm();
			String k = aa.getBlobKey();
			System.out.println(aa.getBlobKey());
			val = iBlob.findBlob(rea, k);
			assertNotNull(val);
			String atta = (String) UObjects.get(val, "!!!!");
			System.out.println(atta);
			assertEquals(s, atta);
		}

	}

	@Test
	public void test3() {
		Note no = new Note();
		no.setDescription("My second note");
		no.setRecipient("I do not like you");
		for (int i = 0; i < 10; i++) {
			String s = "This is an attachment no " + i;
			byte[] val = UObjects.put(s, "!!!");
			String realm = "Hello";
			String key = iAddBlob.addNewBlob(realm, "Wow", val);
			String filename = "file" + i + ".txt";
			NoteAttach a = new NoteAttach();
			a.setRealm(realm);
			a.setBlobKey(key);
			a.setFileName(filename);
			no.getaList().add(a);
		}
		no = iNoteStorage.addElem(getH(HOTEL1), no);
		assertNotNull(no);
		Long id = no.getId();
		no = iNoteStorage.findElemById(getH(HOTEL1), id);
		assertEquals(10, no.getaList().size());
		for (NoteAttach aa : no.getaList()) {
			String rea = aa.getRealm();
			String k = aa.getBlobKey();
			System.out.println(aa.getBlobKey());
			byte[] val = iBlob.findBlob(rea, k);
			assertNotNull(val);
			String atta = (String) UObjects.get(val, "!!!!");
			System.out.println(atta);
			assertNotNull(atta);
			System.out.println(aa.getFileName());
			assertNotNull(aa.getFileName());
		}

		no = new Note();
		no.setDescription("Another note");
		no.setRecipient("I do not like you, check if duplicated");
		no.setContent("This is the content");
		no = iNoteStorage.addElem(getH(HOTEL1), no);
		assertNotNull(no);
		id = no.getId();
		no = iNoteStorage.findElemById(getH(HOTEL1), id);
		assertEquals("This is the content", no.getContent());
	}

}
