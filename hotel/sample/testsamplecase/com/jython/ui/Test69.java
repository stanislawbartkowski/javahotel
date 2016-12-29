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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.jythonui.shared.DialogFormat;

@RunWith(Parameterized.class)
public class Test69 extends TestHelper {

	@Parameters(name = "{index}: {0}")
	public static Object[] data() {
		return new Object[] { "test134.xml", "test135.xml", "test136.xml", "test137.xml", "test138.xml", "test139.xml",
<<<<<<< HEAD
				"test142.xml", "test143.xml", "test144.xml", "test146.xml", "test147.xml", "test151.xml", "test152.xml", "test155.xml" };
=======
				"test142.xml", "test143.xml", "test144.xml", "test146.xml", "test147.xml", "test151.xml", "test152.xml" };
>>>>>>> branch 'master' of ssh://githotel@localhost/home/githotel/git/javahotel.git
	}

	@Parameter
	public String dialogName;

	@Test
	public void Test() {
		DialogFormat d = findDialog(dialogName);
		assertNotNull(d);
		assertTrue(d.isPolymer());
	}

}
