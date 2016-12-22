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

import java.util.ArrayList;
import java.util.List;

import org.migration.extractor.ObjectExtractor;
import org.migration.tokenizer.ITokenize;

class Replace {

	private class ColReplac {
		int lineno;
		String replaced;
		String toreplace;
	};

	private final List<ColReplac> replList = new ArrayList<ColReplac>();

	void addReplace(int lineno, String replaced, String toreplace) {
		ColReplac c = new ColReplac();
		c.replaced = replaced;
		c.toreplace = toreplace;
		c.lineno = lineno;
		replList.add(c);
	}

	void replace(ObjectExtractor.IObjectExtracted i, ITokenize tot) {
		i.getLines().clear();
		for (int j = 0; j < tot.getLines().size(); j++) {
			StringBuffer s = new StringBuffer(tot.getLines().get(j));
			// not optimal
			int jj = j;
			replList.stream().filter(c -> c.lineno == jj).forEach(c -> {
				int r = s.indexOf(c.replaced);
				if (r != -1)
					s.replace(r, r + c.replaced.length(), c.toreplace);
			});
			i.getLines().add(s.toString());
		}
	}

}
