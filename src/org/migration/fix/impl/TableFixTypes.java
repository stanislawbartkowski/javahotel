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
import org.migration.properties.PropHolder;
import org.migration.tokenizer.ITokenize;

public class TableFixTypes extends FixHelper {

	@Override
	public void fix(ObjectExtracted o) {
		ObjectExtractor.IObjectExtracted i = o.getO();
		assert i.getType() == ObjectExtractor.OBJECT.TABLE;
		ITokenize tot = getT(i);
		String w;
		Replace R = new Replace();

		while ((w = tot.readNextWord()) != null)
			if (U.isOBracket(w))
				break;
		if (w == null)
			return;
		// now we have "("
		while (true) {
			w = tot.readNextWord(); // column name
			if (w == null)
				break;
			w = tot.readNextWord();
			if (w == null)
				break;
			// column type
			String replcol = PropHolder.REPLACECOL + w;
			String repla = PropHolder.getProp().getProperty(replcol);
			if (repla != null) {
				R.addReplace(tot.getLines().size() - 1, w, repla);
			}
			// to the ,
			while ((w = tot.readNextWord()) != null)
				if (U.isComma(w))
					break;
			if (w == null)
				break;
		} // while
		R.replace(i, tot);

	}

}
