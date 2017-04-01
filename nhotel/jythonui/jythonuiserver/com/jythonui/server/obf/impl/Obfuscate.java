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
package com.jythonui.server.obf.impl;

import com.jythonui.server.IObfuscateName;

public class Obfuscate implements IObfuscateName {

	private static final char[] sChars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9' };
	
	private static final int ISIZE = 8;

	@Override
	public String obf(String name) {
		int i = name.hashCode();
		if (i < 0) i = -i;
		char[] sBuffer = new char[ISIZE];
		int k = 0;
		while (i != 0 && k < ISIZE) {
			// first character cannot be a digit
			int div = sChars.length;
			if (k == 0) div -= 10;
			sBuffer[k++] = sChars[i % div];
			i /= div;
		}
		while (k < ISIZE)
			sBuffer[k++] = 'Q';
		return new String(sBuffer);
	}

}
