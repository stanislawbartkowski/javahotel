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

import org.junit.Test;
import static org.junit.Assert.*;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;

public class Test59 extends TestHelper {

	@Test
	public void test1() {
		DialogFormat d = findDialog("test104.xml");
		FieldItem i = d.findFieldItem("glob");
		assertNotNull(i);
		assertNull(i.getCellTitle());
		i = d.findFieldItem("ctitle");
		assertNotNull(i);
		assertEquals("hello", i.getCellTitle());
	}
}
