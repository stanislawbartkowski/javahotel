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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gwtmodel.table.binder.BinderWidget;
import com.gwtmodel.table.binder.WidgetTypes;
import com.jythonui.server.IBinderUIStyle;
import com.jythonui.server.IParseRegString;
import com.jythonui.server.Util;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.JythonUIFatal;

public class Test70 extends TestHelper {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void test1() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/css.test");
		assertNotNull(s);
		System.out.println(s);
		String pa = ".*(\\.{1}[\\d\\w\\-\\.]+)[\\ {]";
		String end = "}";
		StringBuffer b = new StringBuffer();
		iParse.run(s, pa, end, new IParseRegString.IVisitor() {

			private int i = 0;

			@Override
			public void visit(String begS, String ma, String content) {
				switch (i) {
				case 0:
				case 1:
					assertEquals(".toolbar", ma);
					break;
				case 2:
					assertEquals(".hello", ma);
					break;
				case 3:
					assertEquals(".source-buttons", ma);
					break;
				}
				System.out.println("* " + begS);
				System.out.println("* " + ma);
				System.out.println(content);
				System.out.println("------------------");
				i++;
				b.append(content + "\n");
			}
		}, null);

		System.out.println(b);
		assertNotEquals(b.toString(), "");
	}

	private void testp(String name) {
		String p = iObf.obf(name);
		assertNotNull(p);
		System.out.println(p);
		assertEquals(p.toUpperCase(), p);
		assertTrue(Character.isLetter(p.charAt(0)));
	}

	@Test
	public void test2() {
		testp(".toolbar");
		testp(".hello");
		testp(".category");
	}

	private void check(BinderWidget w) {
		if (w.getType() == WidgetTypes.PaperToolbar) {
			System.out.println(w.getContentHtml());
			Iterator<String> k = w.getKeys();
			while (k.hasNext()) {
				String a = k.next();
				String val = w.getAttr(a);
				System.out.println(a + ":" + val + "!");
				assertNotEquals("{style.toolbar}", val.trim());
				assertEquals(-1, val.indexOf('{'));
				assertEquals(-1, val.indexOf('}'));
			}
		}
		if (w.getType() == WidgetTypes.FlowPanel) {
			System.out.println(w.getContentHtml());
			Iterator<String> k = w.getKeys();
			while (k.hasNext()) {
				String a = k.next();
				String val = w.getAttr(a);
				System.out.println(a + ":" + val + "!");
				if ("addStyleNames".equals(a)) {
					assertNotEquals("{style.list} hello", val.trim());
					assertEquals(-1, val.indexOf('{'));
					assertEquals(-1, val.indexOf('}'));
					assertNotEquals(-1, val.indexOf(" hello"));
				}
			}
		}
		for (BinderWidget ww : w.getwList())
			// recursive
			check(ww);
	}

	@Test
	public void test3() {
		DialogFormat d = findDialog("test145.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
		BinderWidget w = d.getBinderW();
		String h = w.getContentHtml();
		System.out.println(h);
		assertEquals(1, w.getStyleList().size());
		for (BinderWidget.StyleClass s : w.getStyleList()) {
			System.out.println(s.getContent());
			assertEquals(-1, s.getContent().indexOf(".toolbar"));
		}
		check(w);
	}

	@Test
	public void test4() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test1.css");
		assertNotNull(s);
		System.out.println(s);
		String pa = "@external";
		String end = ";";
		StringBuffer b = new StringBuffer();
		StringBuffer b1 = new StringBuffer();
		iParse.run(s, pa, end, new IParseRegString.IVisitor() {

			private int i = 0;

			@Override
			public void visit(String begS, String ma, String content) {
				System.out.println("* " + begS);
				System.out.println("* " + ma);
				System.out.println(content);
				System.out.println("------------------");
				i++;
				b.append(content + "\n");
			}
		}, b1);

		System.out.println(b);
		assertNotEquals(b.toString().trim(), "");
		System.out.println("-------------------");
		System.out.println(b1);
		assertNotEquals(b1.toString().trim(), "");
	}

	@Test
	public void test5() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test1.css");
		assertNotNull(s);
		System.out.println(s);
		IBinderUIStyle iS = ibFactory.construct();
		String res = iS.parseStyle(s);
		System.out.println("-------------------");
		System.out.println(res);
		// if external is removed
		assertEquals(-1, res.indexOf("@external"));
		// .toolbar is removed
		assertEquals(-1, res.indexOf(".toolbar"));
		// test resulr
		String x = iS.fixAttrValue("aaaa");
		assertEquals("aaaa", x);
		String a = iS.fixAttrValue("{style.toolbar}");
		System.out.println(a);
		assertNotEquals("{style.toolbar}", a);
		String a1 = iS.fixAttrValue("{style.toolbar} hello");
		System.out.println(a1);
		assertEquals(a + " hello", a1);

		exception.expect(JythonUIFatal.class);
		iS.fixAttrValue("{style.iron-selected}");
		// not expected here
		fail();
	}

	@Test
	public void test6() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test2.css");
		assertNotNull(s);
		System.out.println(s);
		IBinderUIStyle iS = ibFactory.construct();
		String res = iS.parseStyle(s);
		System.out.println("-------------------");
		System.out.println(res);
		// .toolbar is removed
		assertEquals(-1, res.indexOf(".toolbar"));
		// .iron-selected not removed
		assertNotEquals(-1, res.indexOf(".iron-selected"));
		String a = iS.fixAttrValue("{style.toolbar}");
		System.out.println(a);
		assertNotEquals("{style.toolbar}", a);

		exception.expect(JythonUIFatal.class);
		String a1 = iS.fixAttrValue("{style.iron-selected}");
		// not expected here
		fail();
	}

	@Test
	public void test7() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test3.css");
		assertNotNull(s);
		System.out.println(s);
//		String pa = "[^\\}](.*)\\{";
		String pa = IBinderUIStyle.PATT;
		String end = "}";
		StringBuffer b = new StringBuffer();
		iParse.run(s, pa, end, new IParseRegString.IVisitor() {

			private int i = 0;

			@Override
			public void visit(String begS, String ma, String content) {
				System.out.println("* " + begS);
				System.out.println("* " + ma);
				System.out.println(content);
				System.out.println("------------------");
				i++;
				b.append(content + "\n");
			}
		}, null);
		assertNotEquals(b.toString(), "");
		assertNotEquals(-1, b.toString().indexOf("[drawer]"));
	}

	@Test
	public void test8() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test3.css");
		assertNotNull(s);
		System.out.println(s);
		IBinderUIStyle iS = ibFactory.construct();
		String res = iS.parseStyle(s);
		System.out.println("-------------------");
		System.out.println(res);
	}

	@Test
	public void test9() throws IOException, URISyntaxException {
		String s = ".toolbar {";
		String pa = IBinderUIStyle.SEL;
		Pattern patt = Pattern.compile(pa);
		Matcher ma = patt.matcher(s);
		assertTrue(ma.find());
		assertEquals(1, ma.groupCount());
		assertEquals(".toolbar", ma.group(1));

		String s1 = " .toolbar.selector ";
		ma = patt.matcher(s1);
		assertTrue(ma.find());
		assertEquals(1, ma.groupCount());
		System.out.println(ma.group(1));
		assertEquals(".toolbar", ma.group(1));

		assertTrue(ma.find());
		assertEquals(1, ma.groupCount());
		System.out.println(ma.group(1));
		assertEquals(".selector", ma.group(1));

		String s2 = ".toolbar.tall #bottomBar";
		ma = patt.matcher(s2);
		int no = 0;
		while (ma.find()) {
			String x = ma.group(1);
			System.out.println(x);
			switch (no) {
			case 0:
				assertEquals(".toolbar", x);
				break;
			case 1:
				assertEquals(".tall", x);
				break;
			}
			no++;
		}
		assertEquals(2, no);
	}
	
	@Test
	public void test10() throws IOException, URISyntaxException {
		String s = Util.getStringFromFile(TestHelper.class, "resources/testdata/test/test4.css");
		assertNotNull(s);
		System.out.println(s);
		IBinderUIStyle iS = ibFactory.construct();
		String res = iS.parseStyle(s);
		System.out.println("-------------------");
		System.out.println(res);
		assertEquals(-1,res.indexOf(".toolbar"));
		assertNotEquals(-1,res.indexOf(".tall"));
	}

}
