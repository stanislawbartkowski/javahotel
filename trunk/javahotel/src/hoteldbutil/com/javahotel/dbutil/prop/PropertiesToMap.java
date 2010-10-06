/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.dbutil.prop;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PropertiesToMap {

	static private class retName {

		final String prefix;
		final String key;

		retName(String p1, String p2) {
			prefix = p1;
			key = p2;
		}
	}

	private static retName getPrefix(final String p) {
		int k = p.indexOf(".");
		if (k == -1) {
			return new retName(null, p);
		}
		String pe = p.substring(0, k);
		String ke = p.substring(k + 1);
		return new retName(pe, ke);
	}

	static Map<String, String> toM(final Properties p, final String preFix) {

		Map<String, String> m = new HashMap<String, String>();
		for (Entry<Object, Object> o : p.entrySet()) {
			String k = (String) o.getKey();
			retName re = getPrefix(k);
			String v = (String) o.getValue();
			if (preFix != null) {
				if (re.prefix != null) {
					if (preFix.equals(re.prefix)) {
						m.put(re.key, v);
					}
					continue;
				}
			}
			m.put(k, v);
		}
		return m;

	}
}
