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
package com.javahotel.dbjpa.ejb3;

import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PersProperties {

	static final String DEFAULT = "default.";
	private final Properties persproperties = new Properties();

	private static String getVal(final String key, final String s) {
		if (!s.startsWith(key)) {
			return null;
		}
		int l = key.length();
		return s.substring(l);
	}

	private static boolean putVal(final Map<String, String> prop,
			final Properties properties, final String key) {
		boolean snew = false;
		for (final String s : prop.keySet()) {
			String sk = getVal(key, s);
			if (sk == null) {
				continue;
			}
			snew = true;
			String val = prop.get(s);
			properties.put(sk, val);
		}
		return snew;
	}

	boolean getProperties(final String dName) {
		Map<String, String> prop = CommonData.getProp();
		putVal(prop, persproperties, DEFAULT);
		boolean snew = putVal(prop, persproperties, dName + ".");
		return snew;
	}

	Properties getMap() {
		return persproperties;
	}
}
