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

import java.math.BigInteger;

import org.migration.properties.PropHolder;

public class SequenceFixMaxValue extends AbstractFixReplace {

	private final BigInteger maxV = new BigInteger(PropHolder.getProp().getProperty(PropHolder.SEQUENCEMAX));

	@Override
	void fixword() {
		if (prevw.equalsIgnoreCase("MAXVALUE")) {
			BigInteger v = new BigInteger(w);
			if (v.compareTo(maxV) == 1)
				replace(w, PropHolder.getProp().getProperty(PropHolder.SEQUENCEMAX));
		}

	}

}
