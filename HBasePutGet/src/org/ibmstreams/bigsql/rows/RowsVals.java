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
package org.ibmstreams.bigsql.rows;

import java.util.ArrayList;
import java.util.List;

import org.ibmstreams.bigsql.mapping.HVal;

public class RowsVals {

	private final HVal key;

	private final List<HVal> vlist = new ArrayList<HVal>();

	public RowsVals(HVal key) {
		this.key = key;
	}

	public HVal getKey() {
		return key;
	}

	public List<HVal> getVlist() {
		return vlist;
	}

	public void addVal(HVal val) {
		vlist.add(val);
	}

}
