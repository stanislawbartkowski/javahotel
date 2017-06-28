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
package org.migration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.migration.fix.FixHelper;
import org.migration.fix.ObjectExtracted;
import org.migration.tokenizer.ITokenize;
import org.migration.tokenizer.TokenizeFactory;

public class Test1 extends TestHelper {

	@Test
	public void test1() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample1.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		String w;

		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("CREATE", w);
		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("TABLE", w);
		String lastw = w;
		while ((w = t.readNextWord()) != null) {
			lastw = w;
			System.out.println(w);
		}
		assertEquals(";", lastw);

	}

	@Test
	public void test2() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample2.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		String w;

		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("CREATE", w);
		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("OR", w);
		String lastw = w;
		boolean isAS = false;
		while ((w = t.readNextWord()) != null) {
			lastw = w;
			System.out.println(w);
			isAS = isAS || w.equals("AS");
		}
		assertEquals(ITokenize.TERMINATEWORD, lastw);
		assertTrue(isAS);
	}

	@Test
	public void test3() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample3.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		String w;

		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("CREATE", w);
		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("OR", w);
		String lastw = w;
		while ((w = t.readNextWord()) != null) {
			lastw = w;
			System.out.println(w);
			// ignore comments
			assertFalse(w.startsWith("--"));
		}
		assertEquals(ITokenize.TERMINATEWORD, lastw);
	}

	@Test
	public void test4() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample4.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		String w;

		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("CREATE", w);
		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("OR", w);
		String lastw = w;
		boolean word1 = false, word2 = false, word3 = false, word4 = false;
		while ((w = t.readNextWord()) != null) {
			lastw = w;
			System.out.println(w);
			// ignore comments
			assertFalse(w.startsWith("--"));
			word1 = word1 || "WORD1".equals(w);
			word2 = word2 || "WORD2".equals(w);
			word3 = word3 || "WORD3".equals(w);
			word4 = word4 || "WORD4".equals(w);
		}
		assertEquals(ITokenize.TERMINATEWORD, lastw);
		assertTrue(word1);
		assertTrue(word4);
		assertFalse(word2);
		assertFalse(word3);
	}

	@Test
	public void test5() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample5.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		String w;

		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("CREATE", w);
		w = t.readNextWord();
		assertNotNull(w);
		assertEquals("OR", w);
		String lastw = w;
		boolean word1 = false, word2 = false, word3 = false, word4 = false;
		while ((w = t.readNextWord()) != null) {
			lastw = w;
			System.out.println(w);
			// ignore comments
			assertFalse(w.startsWith("--"));
			word1 = word1 || "WORD1".equals(w);
			word2 = word2 || "WORD2".equals(w);
			word3 = word3 || "WORD3".equals(w);
			word4 = word4 || "WORD4".equals(w);
		}
		assertTrue(word1);
		assertTrue(word4);
		assertFalse(word2);
		assertFalse(word3);
	}

	@Test
	public void test6() throws URISyntaxException, IOException {
		BufferedReader r = openFile("sample15.ora");
		ITokenize t = TokenizeFactory.provide(r);
		assertNotNull(t);
		int count = 0;
		String w;
		while ((w = t.readNextWord()) != null) {
			System.out.println(w);
			if (w.equals("("))
				count++;
			if (w.equals(")")) {
				count--;
				if (count == 0) {
					// t.removeRestOfLine();
					new FixHelper() {

						@Override
						public void fix(ObjectExtracted o) {
							// TODO Auto-generated method stub

						}

					}.removeRestOfLine(t);
					break;
				}
			}
		} // while
		assertEquals(5, t.getLines().size());
		String l = t.getLines().get(t.getLines().size() - 1);
		System.out.println(l);
		assertEquals("  )", l);
	}

}
