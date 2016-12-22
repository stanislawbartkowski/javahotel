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
package org.migration.fix.impl;

import org.migration.properties.PropHolder;

public class U {

	private U() {
	}
	

	public static String addTails(String line) {
		if (PropHolder.getProp().get(PropHolder.TABLESPACE) != null)
			return line += " IN " + PropHolder.getProp().getProperty(PropHolder.TABLESPACE);
		return line;
	}

	public static boolean found(String prevw, String w, String w1, String w2) {
		return prevw.equalsIgnoreCase(w1) && w.equalsIgnoreCase(w2);
	}

	public static boolean isComma(String w) {
		return w.equals(",");
	}

	public static boolean isOBracket(String w) {
		return w.equals("(");
	}

	public static boolean isCBracket(String w) {
		return w.equals(")");
	}

	private final static String TERMINATES = "/";

	public static boolean isStatementTerm(String line) {
		return line.trim().equals(TERMINATES);
	}

}
