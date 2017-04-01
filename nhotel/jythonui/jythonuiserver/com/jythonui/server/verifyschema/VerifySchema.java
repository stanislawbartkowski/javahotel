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
package com.jythonui.server.verifyschema;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Named;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.jythonui.server.BUtil;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IValidateXML;
import com.jythonui.server.IVerifySchema;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.ReadResourceFactory;

public class VerifySchema extends UtilHelper implements IVerifySchema {

	private static final String XSDDIR = "xsd";
	private final IGetLogMess logMess;
	private final IValidateXML iValidate;

	private static IReadResource iRead = new ReadResourceFactory().constructLoader(VerifySchema.class.getClassLoader());

	@Inject
	public VerifySchema(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess logMess, IValidateXML iValidate) {
		this.logMess = logMess;
		this.iValidate = iValidate;
	}

	private URL getURLSchema(String schemaname) {
		logDebug("Search schema " + schemaname);
		URL ur = iRead.getRes(BUtil.addNameToPath(XSDDIR, schemaname));
		if (ur == null) {
			errorLog(logMess.getMess(IErrorCode.ERRORCODE16, ILogMess.SCHEMANOTFOUND));
		}
		return ur;
	}

	@Override
	public void verify(InputStream sou, String... xsdFile) throws SAXException, IOException {
		URL[] ulist = new URL[xsdFile.length];
		for (int i = 0; i < xsdFile.length; i++)
			ulist[i] = getURLSchema(xsdFile[i]);
		logDebug("Verify using xsd schema " + xsdFile);
		iValidate.validate(new StreamSource(sou), ulist);
	}

}
