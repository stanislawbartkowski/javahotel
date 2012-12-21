/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class GetValidDataBaseNames {

	private static final String RELEASE = ".databasevalid";
	private static final String OK = "ok";

	static List<String> getNames() {
		Map<String, String> prop = CommonData.getProp();
		Vector<String> v = new Vector<String>();
		for (final String s : prop.keySet()) {
			int i = s.indexOf(RELEASE);
			if (i == -1) {
				continue;
			}
			String val = prop.get(s);
			if (!val.equals(OK)) {
				continue;
			}
			String r = s.substring(0, i);
			v.add(r);
		}
		return v;
	}
}
