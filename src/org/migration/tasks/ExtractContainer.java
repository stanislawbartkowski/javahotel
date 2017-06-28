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

package org.migration.tasks;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.migration.extractor.ObjectExtractor;

public class ExtractContainer {

	private ExtractContainer() {

	}

	@FunctionalInterface
	public interface IRunner {
		void run(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
				Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma);
	}

	public static void run(BufferedReader reader, IRunner runner) throws Exception {

		Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma = new HashMap<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>>();

		try (ObjectExtractor o = new ObjectExtractor(reader)) {

			ObjectExtractor.IObjectExtracted i;
			while ((i = o.extractNext()) != null) {
				List<ObjectExtractor.IObjectExtracted> li = ma.get(i.getType());
				if (li == null) {
					li = new ArrayList<ObjectExtractor.IObjectExtracted>();
					ma.put(i.getType(), li);
				}
				li.add(i);
			}
		}

		for (ObjectExtractor.OBJECT ob : ObjectExtractor.OBJECT.values())
			runner.run(ob, ma.get(ob), ma);
	}

}
