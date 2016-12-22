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

import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixHelper;
import org.migration.fix.ObjectExtracted;
import org.migration.tokenizer.ITokenize;

public class TableFixTail extends FixHelper {

	private void modify(ObjectExtractor.IObjectExtracted i) {
		ITokenize tot = getT(i);
		String w;

		int counter = 0;
		while ((w = tot.readNextWord()) != null) {
			if (w.equals("("))
				counter++;
			if (w.equals(")"))
				if (--counter == 0) {
					removeRestOfLine(tot);
					String line = getLastLine(tot);
					if (i.getType() == ObjectExtractor.OBJECT.TABLE)
						line = U.addTails(line);
					replaceLastLine(tot, line + " ;");
					break;
				}
		}
		// replace
		replaceLines(i, tot);
	}

	@Override
	public void fix(ObjectExtracted o) {
		// do not remove tail from GLOBAL TEMPORARY TABLES
		if (o.getO().getType() == ObjectExtractor.OBJECT.TABLE) modify(o.getO());
		o.getAdded().forEach(i -> modify(i));
	}

}
