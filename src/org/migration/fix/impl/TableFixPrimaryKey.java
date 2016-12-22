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

public class TableFixPrimaryKey extends FixHelper {

	@Override
	public void fix(ObjectExtracted o) {
		ObjectExtractor.IObjectExtracted i = o.getO();
		assert i.getType() == ObjectExtractor.OBJECT.TABLE || i.getType() == ObjectExtractor.OBJECT.GLOBALTEMP;
		ITokenize tot = getT(i);
		String prevw = tot.readNextWord();
		if (prevw == null)
			return;
		String w;
		boolean foundP = false;
		while ((w = tot.readNextWord()) != null) {
			if (U.found(prevw, w, "PRIMARY", "KEY")) {
				// ignore all
				foundP = true;
				break;
			}
			prevw = w;
		}
		if (foundP) {
			while ((w = tot.readNextWord()) != null)
				if (U.isOBracket(w))
					break;
			if (w != null)
				while ((w = tot.readNextWord()) != null)
					if (U.isCBracket(w))
						break;
			// close bracket
			removeRestOfLine(tot); // forget rest
			replaceLines(i, tot);
			String line = U.addTails(")") + ";";
			i.getLines().add(line);
		}
		// nothing changed
	}

}
