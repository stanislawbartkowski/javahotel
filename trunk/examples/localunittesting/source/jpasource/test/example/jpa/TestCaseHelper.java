/*
 * Copyright 2009 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package test.example.jpa;

import org.example.jpa.entities.PMF;
import org.example.jpa.entities.TestCaseEnvironment;
import org.junit.After;
import org.junit.Before;

abstract public class TestCaseHelper {

	private final TestCaseEnvironment testE = new TestCaseEnvironment();

	@Before
	public void setUp() {
		testE.beforeTest();
		PMF.resetFactory();
	}

	@After
	public void tearDown() {
		testE.afterTest();
	}

}
