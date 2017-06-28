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
import java.util.Optional;
import java.util.stream.Collectors;

import org.migration.extractor.ObjectExtractor;
import org.migration.fix.FixHelper;
import org.migration.fix.ObjectExtracted;
import org.migration.tokenizer.ITokenize;

public class TableFixUnique extends FixHelper {

	private static final String PRIMARY = "PRIMARY";
	private static final String KEY = "KEY";
	private static final String USING = "USING";
	private static final String INDEX = "INDEX";

	private String extractPrimarytoUnique(ObjectExtractor.IObjectExtracted i) {
		ITokenize t = this.getT(i);
		String prevw = t.readNextWord();
		String w;
		boolean isPrimary = false;
		boolean isUsingIndex = false;
		while ((w = t.readNextWord()) != null) {
			if (isUsingIndex)
				return w.toUpperCase();
			if (!isPrimary)
				isPrimary = U.found(prevw, w, PRIMARY, KEY);
			if (isPrimary)
				isUsingIndex = U.found(prevw, w, USING, INDEX);
			// isUsingIndex detected then next word is unique index name and
			// return immediately
			prevw = w;
		} // while
		return null;
	}

	private Optional<ObjectExtractor.IObjectExtracted> existUniq(List<ObjectExtractor.IObjectExtracted> uniqL,
			ObjectExtractor.IObjectExtracted alter) {
		String uName = extractPrimarytoUnique(alter);
		if (uName == null)
			return Optional.empty();
		return uniqL.stream().filter(i -> uName.equalsIgnoreCase(i.getName())).findFirst();
	}

	@Override
	public void fix(ObjectExtracted o) {
		// look for unique keys
		List<ObjectExtractor.IObjectExtracted> uniqL = o.getAdded().stream()
				.filter(i -> i.getType() == ObjectExtractor.OBJECT.UNIQUE).collect(Collectors.toList());

		if (uniqL.isEmpty())
			return;
		// at least one unique key

		// first creates a list of optional then final list of ALTER TABLE
		// PRIMARY without UNIQUE

		List<ObjectExtractor.IObjectExtracted> toRemove = o.getAdded().stream()
				.filter(i -> i.getType() == ObjectExtractor.OBJECT.ALTERTABLE).map(i -> existUniq(uniqL, i))
				.filter(i -> i.isPresent()).map(i -> i.get()).collect(Collectors.toList());
		// remove all unique index overlapping with primary keys
		o.getAdded().removeAll(toRemove);
	}

}
