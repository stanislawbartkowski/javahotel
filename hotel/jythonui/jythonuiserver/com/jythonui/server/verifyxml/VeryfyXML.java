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
package com.jythonui.server.verifyxml;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.IValidateXML;
import com.jythonui.server.IVerifyXML;
import com.jythonui.server.Util;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class VeryfyXML extends UtilHelper implements IVerifyXML {

	private final IJythonUIServerProperties p;
	private final IValidateXML iValidate;

	@Inject
	public VeryfyXML(IJythonUIServerProperties p,IValidateXML iValidate) {
		this.p = p;
		this.iValidate = iValidate;
	}

	@Override
	public boolean verify(String xsdDir, String xsdFile, String xml) {
		URL u = Util.getFirstURL(p, true, IConsts.DIALOGDIR, xsdDir, xsdFile);
		if (u == null)
			return false;
		StreamSource is = new StreamSource(new StringReader(xml));
		try {
//			VerifyXML.verify(u, is);
			iValidate.validate(is, u);
		} catch (SAXException | IOException e) {
			errorMess(L(), IErrorCode.ERRORCODE122, ILogMess.ERRORWHILEPARSINGXMLFILEWITHSCHEMA, e,
					Util.getFileName(p, IConsts.DIALOGDIR, xsdDir, xsdFile));
			return false;
		}
		return true;
	}
}
