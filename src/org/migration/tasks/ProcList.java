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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.migration.comparedb2.CUtil;
import org.migration.extractor.ObjectExtractor;

public class ProcList {

	private ProcList() {
	}

	private static void e(String s) {
		System.out.println(s);
	}

	private static void draw(ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li, boolean listOf) {
		e("------------------------");
		if (li == null)
			e(oType.name() + " None");
		else {
			e(oType.name() + " " + li.size());
			if (listOf) {
				e("");
				li.forEach(i -> e(i.getName()));
			}
		}
	}

	public static void stat(String inputName, boolean listOf) throws Exception {
		ExtractContainer.run(inputName, (ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
				Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma) -> draw(oType, li, listOf));
	}

	private static final String EMPTYSCHEMA = "<no schema>";

	private static void gatherSchemas(Set<String> s, List<ObjectExtractor.IObjectExtracted> li) {
		if (li == null)
			return;
		li.forEach(i -> {
			CUtil.IObjectName o = CUtil.objectName(i.getName());
			if (o.getSchema() == null)
				s.add(EMPTYSCHEMA);
			else
				s.add(o.getSchema());
		});
	}

	public static void listofSchemas(String inputName) throws Exception {
		Set<String> se = new HashSet<String>();
		ExtractContainer.run(inputName, (ObjectExtractor.OBJECT oType, List<ObjectExtractor.IObjectExtracted> li,
				Map<ObjectExtractor.OBJECT, List<ObjectExtractor.IObjectExtracted>> ma) -> gatherSchemas(se, li));
		se.forEach(s -> e(s));
	}

}
