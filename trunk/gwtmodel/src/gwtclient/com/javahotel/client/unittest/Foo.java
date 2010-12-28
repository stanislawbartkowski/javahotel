/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

/**
 * A sample routine that runs in the module that we want to run a unit test on.
 * Accepts the strings "foo" and "bar" in any case, and the string "Baz" as an
 * exact match. All other strings fail.
 * 
 * @param input
 *            A string to validate
 * @return true if the string passes validation.
 */
public class Foo {
	public boolean exampleValidator(String input) {
		if (input.toLowerCase().equals("foo")) {
			return true;
		} else if (input.toLowerCase().equals("bar")) {
			return true;
		} else if (input.equals("Baz")) {
			return true;
		}
		return false;
	}
}