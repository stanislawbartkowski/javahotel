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

import java.util.List;
import java.util.stream.Collectors;

import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixHelper;
import org.migration.fix.ObjectExtracted;
import org.migration.tokenizer.ITokenize;

public class GlobalTableFixPrimary extends FixHelper {

	private boolean isPrimary(ObjectExtractor.IObjectExtracted i) {
		ITokenize tot = this.getT(i);
		String w;

		String prevw = tot.readNextWord();
		if (prevw == null)
			return false;
		while ((w = tot.readNextWord()) != null) {
			if (U.found(prevw, w, "PRIMARY", "KEY"))
				return true;
			prevw = w;
		}
		return false;
	}

	@Override
	public void fix(ObjectExtracted o) {
		ObjectExtractor.IObjectExtracted i = o.getO();
		assert i.getType() == ObjectExtractor.OBJECT.GLOBALTEMP;
		// remove primary keys
		List<ObjectExtractor.IObjectExtracted> toremove = o.getAdded().stream()
				.filter(oo -> oo.getType() == ObjectExtractor.OBJECT.ALTERTABLE).filter(oo -> isPrimary(oo))
				.collect(Collectors.toList());
		o.getAdded().removeAll(toremove);
	}

}
