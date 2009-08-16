/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.unittest;

import com.google.gwt.junit.client.GWTTestCase;

public class FooTest extends GWTTestCase {

	/**
	 * Specifies a module to use when running this test case. The returned
	 * module must cause the source for this class to be included.
	 * 
	 * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
	 */
	public String getModuleName() {
		return "com.javahotel.web";
	}

	/**
	 * Test the method 'exampleValidator()' in module 'Foo'
	 */
	public void testExampleValidator() {
		Foo myModule = new Foo();

		assertTrue(myModule.exampleValidator("foo"));
		assertTrue(myModule.exampleValidator("Foo"));
		assertTrue(myModule.exampleValidator("FOO"));
		assertTrue(myModule.exampleValidator("fOo"));
		assertTrue(myModule.exampleValidator("bar"));
		assertTrue(myModule.exampleValidator("Baz"));

		assertFalse(myModule.exampleValidator("fooo"));
		assertFalse(myModule.exampleValidator("oo"));
		assertFalse(myModule.exampleValidator("baz"));
	}
}