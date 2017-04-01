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
package com.jythonui.server.parsereg.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IParseRegString;

public class ParseRegString implements IParseRegString {

	@Override
	public void run(String html, String begPatt, String endS, IVisitor i, StringBuffer brest) {
		if (CUtil.EmptyS(html))
			return;
		int start = 0;
		Pattern patt = Pattern.compile(begPatt);
		String nextH = html;
		while (true) {
			nextH = nextH.substring(start);
			Matcher ma = patt.matcher(nextH);
			if (!ma.find()) {
				if (brest != null)
					brest.append(nextH);
				break;
			}
			int i1 = ma.start();
			int i2 = ma.end();
			int end = nextH.indexOf(endS, i2);
			if (end == -1) {
				if (brest != null)
					brest.append(nextH);
				break;
			}
			if (brest != null)
				brest.append(nextH.substring(0, i1));
			end += endS.length();
			String csshtml = nextH.substring(i1, end);
			String f = null;
			if (ma.groupCount() >= 1)
				f = ma.group(1);
			i.visit(ma.group(), f, csshtml);
			start = end;
		}

	}

}
