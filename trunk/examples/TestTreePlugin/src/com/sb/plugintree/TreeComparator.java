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

package com.sb.plugintree;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator : compares to trees in form of list of values
 * 
 * @author sbartkowski
 * 
 */
public class TreeComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		List<String> li1 = (List<String>) o1;
		List<String> li2 = (List<String>) o2;
		if (li1.size() != li2.size()) {
			return -1;
		}
		for (int i = 0; i < li1.size(); i++) {
			if (!li1.get(i).equals(li2.get(i))) {
				return -1;
			}
		}
		return 0;
	}

}
