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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;

public class Test67 extends TestHelper {

	@Test
	public void test1() {
		DialogFormat d = findDialog("test126.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}
	
	@Test
	public void test2() {
		DialogFormat d = findDialog("test127.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}
	
	@Test
	public void test3() {
		DialogFormat d = findDialog("test128.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	} 
	
	@Test
	public void test4() {
		DialogFormat d = findDialog("test129.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	} 

	@Test
	public void test5() {
		DialogFormat d = findDialog("test130.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	} 

	@Test
	public void test6() {
		DialogFormat d = findDialog("test131.xml");
		assertNotNull(d);
		assertTrue(d.isPolymer());
	} 

}
