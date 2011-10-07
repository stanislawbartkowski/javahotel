/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.db2.sb.scriptlauncher;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Comparator - compare entry by entry
 * @author sbartkowski
 *
 */
public class DB2ScriptLauncherComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Map<String, String> m1 = (Map<String, String>) o1;
		Map<String, String> m2 = (Map<String, String>) o2;
		for (Entry<String, String> e : m1.entrySet()) {
			String val2 = m2.get(e.getKey());
			if (!e.getValue().equals(val2)) {
				return -1;
			}
		}
		return 0;
	}

}
