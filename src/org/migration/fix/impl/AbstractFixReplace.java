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

abstract class AbstractFixReplace extends FixHelper {

	protected String prevw;
	protected String w;
	protected Replace R;
	protected ITokenize tot;

	abstract void fixword();

	protected void replace(String replaced, String toreplace) {
		R.addReplace(tot.getLines().size() - 1, replaced, toreplace);
	}

	@Override
	public void fix(ObjectExtracted o) {
		ObjectExtractor.IObjectExtracted i = o.getO();
		tot = getT(i);
		prevw = tot.readNextWord();
		if (prevw == null)
			return;
		R = new Replace();
		while ((w = tot.readNextWord()) != null) {
			fixword();
			prevw = w;
		}
		R.replace(i, tot);
	}
}
