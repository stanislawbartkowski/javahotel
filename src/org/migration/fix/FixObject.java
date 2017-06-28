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
package org.migration.fix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.migration.extractor.ObjectExtractor;

public class FixObject {

	private FixObject() {

	}

	private static Map<ObjectExtractor.OBJECT, List<IFix>> registerd = new HashMap<ObjectExtractor.OBJECT, List<IFix>>();
	
	public static void clearall() {
		registerd.clear();
	}

	public static void register(ObjectExtractor.OBJECT o, IFix i) {
		List<IFix> li = registerd.get(o);
		if (li == null) {
			li = new ArrayList<IFix>();
			registerd.put(o, li);
		}
		li.add(i);
	}

	public static void fix(ObjectExtracted o) {
		List<IFix> l = registerd.get(o.getO().getType());
		if (l == null)
			return;
		else
			l.forEach(i -> i.fix(o));
	}

}
