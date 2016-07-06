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
package com.jythonui.server.binder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.gwtmodel.table.common.CUtil;

class ExtractStyle {

	private ExtractStyle() {

	}

	// TODO: !!!!!!!
	static String getStyle(String html) throws ParserConfigurationException, SAXException, IOException {
		if (CUtil.EmptyS(html))
			return null;
		StringBuffer css = new StringBuffer();
		int start = 0;
        Pattern patt = Pattern.compile("(<style.*>)");
		while (true) {
			String nextH = html.substring(start);
	        Matcher ma = patt.matcher(html);
	        if (!ma.find()) break;
	        int i1 = ma.start();
	        int i2 = ma.end();
	        String sty = html.substring(i1,i2);
			int end = nextH.indexOf("</style>",start);
			if (end == -1) break;
			css.append(html.substring(i2, end));
		}
		return css.toString();
	
	}

}
