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
package com.jythonui.server.binderstyle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IBinderUIStyle;
import com.jythonui.server.IConsts;
import com.jythonui.server.IObfuscateName;
import com.jythonui.server.IParseRegString;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

class ParseStyleC extends UtilHelper {

	private ParseStyleC() {
	}

	// private static final String reg = ".*(\\.{1}[\\d\\w\\-\\.]+)[\\ {]";
	private static final String reg = IBinderUIStyle.PATT;
	private static final String end = "}";

	private static final String extpa = "@external";
	private static final String extend = ";";

	private static void extractExternal(IParseRegString gPa, final ResourceC res, String content, StringBuffer restS) {
		gPa.run(content, extpa, extend, new IParseRegString.IVisitor() {

			@Override
			public void visit(String begS, String ma, String content) {
				String[] plist = content.replaceFirst(extpa, "").split("[,;]");
				for (String p : plist)
					// remove . at the beginning
					res.external.add(p.trim().substring(1));
			}
		}, restS);

	}

	static void parseStyle(IParseRegString gPa, final IObfuscateName iObf, final ResourceC res, String content) {
		StringBuffer prest = new StringBuffer();

		// remove external and prepare external set
		extractExternal(gPa, res, content, prest);

		gPa.run(prest.toString(), reg, end, new IParseRegString.IVisitor() {

			@Override
			public void visit(String foundS, String matchSS, String content) {
				// extract all .name from the selector
				String pa = IBinderUIStyle.SEL;
				Pattern patt = Pattern.compile(pa);
				Matcher ma = patt.matcher(matchSS);
				String newC = content;
				while (ma.find()) {
					String matchS = ma.group(1);
					// remove . at the beginning
					String matchedS = matchS.substring(1);
					if (!res.external.contains(matchedS)) {
						String obf = res.lookup.get(matchedS);
						if (obf == null) {
							obf = iObf.obf(matchedS);
							res.lookup.put(matchedS, obf);
						}
						newC = newC.replace(matchedS, obf);
					}
				} // while
				res.styleRes.append(newC);
				res.styleRes.append('\n');
			}
		}, null);
	}

	static String replaceResource(ResourceC res, String val) {
		if (CUtil.EmptyS(val))
			return val;
		if (val.charAt(0) != '{')
			return val;
		// double {{ or {" at the beginning
		if (val.length() > 1 && (val.charAt(1) == '{' || val.charAt(1) == '"'))
			return val;
		int i = val.indexOf('}');
		if (i == -1)
			errorLog(L().getMess(IErrorCode.ERRORCODE136, ILogMess.LASTCHARACTERINSTYLEESOURCESHOULDBE, val, "}"));

		String content = val.substring(1, i);
		String sty = IConsts.STYLEW + '.';
		if (!content.startsWith(sty))
			errorLog(L().getMess(IErrorCode.ERRORCODE137, ILogMess.STYLERESOURCESHOUlDSTARWITH, val, sty));
		String resS = content.substring(sty.length());
		String repl = res.lookup.get(resS);
		if (repl == null)
			errorLog(L().getMess(IErrorCode.ERRORCODE138, ILogMess.STYLERESOURCENOTRECOGNIZED, val, resS));
		return repl + val.substring(i + 1);
	}

}
