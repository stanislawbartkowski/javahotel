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
package com.jythonui.server.binderstyle;

import com.jythonui.server.IBinderUIStyle;
import com.jythonui.server.IObfuscateName;
import com.jythonui.server.IParseRegString;

public class BinderStyle implements IBinderUIStyle {

	private final ResourceC resC = new ResourceC();
	private final IObfuscateName iObf;
	private final IParseRegString gPa;
	
	public BinderStyle(IParseRegString gPa,IObfuscateName iObf) {
		this.iObf = iObf;
		this.gPa = gPa;		
	}

	@Override
	public String parseStyle(String s) {
		ParseStyleC.parseStyle(gPa,iObf,resC, s);
		String sty = resC.styleRes.toString();
		resC.styleRes.setLength(0);
		return sty;
	}

	@Override
	public String fixAttrValue(String val) {
		return ParseStyleC.replaceResource(resC, val);
	}

}
