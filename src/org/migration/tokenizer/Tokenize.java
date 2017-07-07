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
package org.migration.tokenizer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

import org.migration.filereader.IFileReader;
import org.migration.fix.impl.U;

class Tokenize implements ITokenize {

	// unlikely string

	private final IFileReader reader;

	private final static String DELIM = " ();/-";

	private final static String COMMLINE = "--";
	private final static String COMMBEG = "/*";
	private final static String COMMEND = "*/";

	// private final static String TERMINATES = "/";

	private String line;
	private int c;
	private boolean insidecomment = false;

	private final List<String> lines = new ArrayList<String>();

	Tokenize(IFileReader reader) {
		this.reader = reader;
	}

	@Override
	public void startCollectingLines() {
		lines.clear();
		lines.add(line);
	}

	private boolean isComm(char ch1, char ch2, String w) {
		return ch1 == w.charAt(0) && ch2 == w.charAt(1);
	}

	private boolean isDelim(char ch) {
		return DELIM.indexOf(ch) != -1;
	}

	@Override
	public String readNextWord() {

		// complicated, because method recognizes and ignores comments
		do {
			if ((line == null) || c >= line.length()) {
				try {
					line = reader.readLine();
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
				c = 0;
				if (line != null)
					lines.add(line);
			}
			if (line == null)
				return null;

			if (insidecomment) {
				// 2017/07/05 : substring
				int i = line.substring(c).indexOf(COMMEND);
				if (i == -1) {
					// next line
					line = null;
					continue;
				}
				// 2017/07/05
				c = c + i + COMMEND.length();
				// start after end of comment
				insidecomment = false;
			}

			// check if terminator
			// if (line.trim().equals(TERMINATES)) {
			if (U.isStatementTerm(line)) {
				line = null;
				return TERMINATEWORD;
			}

			// ignore white characters
			for (; c < line.length() && Character.isWhitespace(line.charAt(c)); c++)
				;
			// nothing to the end of line
			if (c >= line.length())
				continue;
			char ch = line.charAt(c++);
			// last character in line ?
			if (c >= line.length())
				return "" + ch;

			// comment ?
			if (isComm(ch, line.charAt(c), COMMLINE)) {
				line = null;
				continue;
			}
			if (isComm(ch, line.charAt(c), COMMBEG)) {
				insidecomment = true;
				c++;
				continue;
			}

			if (isDelim(ch))
				return "" + ch;

			StringBuffer buf = new StringBuffer();
			buf.append(ch);
			for (; c < line.length() && !isDelim(line.charAt(c)); c++)
				buf.append(line.charAt(c));
			return buf.toString();
		} while (true);

	}

	@Override
	public List<String> getLines() {
		return lines;
	}

	@Override
	public void close() throws Exception {
		reader.close();
	}

	@Override
	public int currentC() {
		return c;
	}

}
