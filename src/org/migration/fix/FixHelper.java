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
package org.migration.fix;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.migration.extractor.ObjectExtractor;
import org.migration.tokenizer.ITokenize;
import org.migration.tokenizer.TokenizeFactory;

abstract public class FixHelper implements IFix {

	private void addLines(StringBuffer buf, List<String> l) {
		l.forEach(line -> {
			buf.append(line);
			buf.append(System.lineSeparator());
		});
	}

	private BufferedReader getB(ObjectExtractor.IObjectExtracted i) {
		StringBuffer buf = new StringBuffer();
		addLines(buf, i.getLines());
		return new BufferedReader(new StringReader(buf.toString()));
	}

	protected ITokenize getT(ObjectExtractor.IObjectExtracted i) {
		return TokenizeFactory.provide(getB(i));
	}

	protected void replaceLines(ObjectExtractor.IObjectExtracted i, ITokenize tot) {
		i.getLines().clear();
		i.getLines().addAll(tot.getLines());
	}

	protected String getLastLine(ITokenize tot) {
		return tot.getLines().get(tot.getLines().size() - 1);
	}

	protected void replaceLastLine(ITokenize tot, String line) {
		tot.getLines().set(tot.getLines().size() - 1, line);
	}

	// public for test purpose
	public void removeRestOfLine(ITokenize tot) {
		String line = getLastLine(tot);
		// line and last line in List
		if (line == null)
			return;
		line = line.substring(0, tot.currentC());
		// replace last line
		replaceLastLine(tot, line);
	}
	
}
