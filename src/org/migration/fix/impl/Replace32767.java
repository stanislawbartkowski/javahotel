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
package org.migration.fix.impl;

public class Replace32767 extends AbstractFixReplace {

	private boolean waschartype = false;

	private static final String VARCHAR2 = "VARCHAR2";
	private static final String CHAR = "CHAR";
	private static final String MAX = "MAX";

	private final static int MAXDB2 = 32672;

	@Override
	void fixword() {
		if (waschartype) {
			// now the size
			boolean replace = false;
			if (w.equalsIgnoreCase(MAX))
				replace = true;
			else {
				int csize = Integer.parseInt(w);
				if (csize > MAXDB2)
					replace = true;
			}
			if (replace)
				replace(w, Integer.toString(MAXDB2));
			waschartype = false;
			return;
		}
		waschartype = U.found(prevw, w, VARCHAR2, "(") || U.found(prevw, w, CHAR, "(");
	}

}
