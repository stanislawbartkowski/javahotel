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
package org.migration.fix.impl;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.migration.extractor.ObjectExtractor;
import org.migration.properties.PropHolder;
import org.migration.tokenizer.ITokenize;
import org.migration.tokenizer.TokenizeFactory;

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

	public static boolean foundor(String prevW, String w1, String w2) {
		return prevW.equalsIgnoreCase(w1) || prevW.equalsIgnoreCase(w2);
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

//	private final static String TERMINATES = "/";

	public static boolean isStatementTerm(String line) {
		String TERMINATES = (String) PropHolder.getProp().get(PropHolder.INPUTSTATTERM);
		return line.trim().equals(TERMINATES);
	}

	// ===============
	
	private static void addLines(StringBuffer buf, List<String> l) {
		l.forEach(line -> {
			buf.append(line);
			buf.append(System.lineSeparator());
		});
	}

	private static BufferedReader getB(ObjectExtractor.IObjectExtracted i) {
		StringBuffer buf = new StringBuffer();
		addLines(buf, i.getLines());
		return new BufferedReader(new StringReader(buf.toString()));
	}

	public static ITokenize getT(ObjectExtractor.IObjectExtracted i) {
		return TokenizeFactory.provide(getB(i));
	}


}
